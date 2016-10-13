package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.AdditionalQueries;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.OrgApiPrincipal;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 9/15/16.
 */

public class UserService {

    private final ServiceCommons serviceCommons;
    private final SecurityContext securityContext;

    UserService(SecurityContext securityContext){
        this.securityContext = securityContext;
        this.serviceCommons = new ServiceCommons(securityContext, true);
    }

    private boolean hasAccessToResource(UserDTO userResource){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        long userResourceUserId = userResource.getElementId();
        long userResourceOrgId = userResource.getOrgId();
        if(principal.getUserId() == userResourceUserId){
            return true;
        }

        if(isUserAdmin()){
            return userResourceOrgId == principal.getOrgId();
        }

        return false;
    }

    private boolean isUserAdmin(){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.isUserInRole(Role.ADMIN);
    }

    private void ensureMasterAccessRestriction(UserDTO user) throws OrgApiSecurityException{
        if(user.getRoles().contains(Role.MASTER) && (user.getRoles().size() != 1 || user.getOrgId() > 0)){
            throw new OrgApiSecurityException("Cannot add a user with Master role that has other roles or an Org assignment.");
        }
    }

    public UserDTO addUser(UserDTO user) throws OrgApiDataException, OrgApiSecurityException{
        ensureMasterAccessRestriction(user);
        user.setElementId(-1L);
        user.setPassword(HashingUtils.hashBCrypt(user.getPassword()));
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Adding new user. Subject: " + serviceCommons.getSubjectName());
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            if(isUserAdmin() && getOrgId() != user.getOrgId()){
                throw new ForbiddenException("Admin user cannot add a user outside of their own org");
            }

            result = userDao.insert(user);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }

        return result;
    }

    public UserDTO updateUser(UserDTO user, Long userId) throws OrgApiDataException, OrgApiSecurityException{
        ensureMasterAccessRestriction(user);
        user.setPassword(HashingUtils.hashBCrypt(user.getPassword()));
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Updating existing user. Subject: " + serviceCommons.getSubjectName() +
                    " | ID: " + userId);
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            if(!hasAccessToResource(userDao.get(userId))){
                throw new ForbiddenException("User doesn't have access to resource");
            }

            if(isUserAdmin() && getOrgId() != user.getOrgId()){
                throw new ForbiddenException("Admin user cannot reassign a user outside of their own org");
            }

            user.setElementId(userId);
            result = userDao.update(user, userId);

            connection.commit();
        }
        catch (OrgApiDataException | ForbiddenException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally {
            serviceCommons.closeConnection(connection);
        }
        return result;
    }

    public UserDTO deleteUser(Long userId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Deleting user. Subject: " + serviceCommons.getSubjectName() +
                    " | ID: " + userId);
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);
            Dao<RefreshTokenDTO,Long> tokenDao = connection.newDao(RefreshTokenDTO.class);

            if(!hasAccessToResource(userDao.get(userId))){
                throw new ForbiddenException("User doesn't have access to resource");
            }

            tokenDao.query(AdditionalQueries.DELETE_BY_USER, userId);
            result = userDao.delete(userId);

            connection.commit();
        }
        catch (OrgApiDataException | ForbiddenException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally {
            serviceCommons.closeConnection(connection);
        }

        return result;
    }

    public UserDTO getUser(Long userId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Retrieving user by ID. Subject: " + serviceCommons.getSubjectName() +
                    " | ID: " + userId);
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            result = userDao.get(userId);
            if(!hasAccessToResource(result)){
                throw new ForbiddenException("User doesn't have access to resource");
            }

            connection.commit();
        }
        catch (OrgApiDataException | ForbiddenException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally {
            serviceCommons.closeConnection(connection);
        }

        return result;
    }

    private long getOrgId() throws OrgApiSecurityException{
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.getOrgId();
    }

    public UserListDTO getAllUsers(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserListDTO results = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Getting list of users. Subject: " + serviceCommons.getSubjectName());
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            List<UserDTO> list = null;
            long orgId = getOrgId();
            if(orgId > 0){
                list = (offset >= 0 && size >= 0) ? userDao.getAll(offset, size) : userDao.getAll();
            }
            else{
                list = (offset >= 0 && size >= 0) ? (List<UserDTO>) userDao.query(AdditionalQueries.GET_ALL_LIMIT_BY_ORG, orgId, offset, size) :
                        (List<UserDTO>) userDao.query(AdditionalQueries.GET_ALL_BY_ORG, orgId);
            }

            if(list.size() > 0){
                results = new UserListDTO(list);
            }

            connection.commit();
        }
        catch (OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally {
            serviceCommons.closeConnection(connection);
        }

        return results;
    }

    public UserDTO getUserByName(String name) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            result = (UserDTO) userDao.query(AdditionalQueries.GET_WITH_NAME, name);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }
        return result;
    }

}
