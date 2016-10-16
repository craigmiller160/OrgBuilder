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

    public UserDTO addUser(UserDTO user) throws OrgApiDataException, OrgApiSecurityException{
        user.setElementId(-1L);
        user.setPassword(HashingUtils.hashBCrypt(user.getPassword()));
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Adding new user. Subject: " + serviceCommons.getSubjectName());
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

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
        user.setPassword(HashingUtils.hashBCrypt(user.getPassword()));
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Updating existing user. Subject: " + serviceCommons.getSubjectName() +
                    " | ID: " + userId);
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

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

    public UserListDTO getAllUsers(long orgId, long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserListDTO results = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Getting list of users. Subject: " + serviceCommons.getSubjectName());
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            List<UserDTO> list = null;
            if(orgId > 0){
                list = (offset >= 0 && size >= 0) ? (List<UserDTO>) userDao.query(AdditionalQueries.GET_ALL_LIMIT_BY_ORG, orgId, offset, size) :
                        (List<UserDTO>) userDao.query(AdditionalQueries.GET_ALL_BY_ORG, orgId);
            }
            else{
                list = (offset >= 0 && size >= 0) ? userDao.getAll(offset, size) : userDao.getAll();
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
