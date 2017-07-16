package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.AdditionalQueries;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;
import org.apache.commons.lang3.StringUtils;

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

    public UserDTO updateUser(UserDTO updatedUser, Long userId, ExtraSecurityFilter filter) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Updating existing user. Subject: " + serviceCommons.getSubjectName() +
                    " | ID: " + userId);
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            UserDTO existingUser = userDao.get(userId);
            if(existingUser == null){
                return null;
            }
            filter.filter(existingUser);

            if(!StringUtils.isEmpty(updatedUser.getPassword())){
                updatedUser.setPassword(HashingUtils.hashBCrypt(updatedUser.getPassword()));
            }
            else{
                updatedUser.setPassword(existingUser.getPassword());
            }

            updatedUser.setElementId(userId);
            result = userDao.update(updatedUser, userId);

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

    public UserDTO deleteUser(Long userId, ExtraSecurityFilter filter) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Deleting user. Subject: " + serviceCommons.getSubjectName() +
                    " | ID: " + userId);
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);
            Dao<RefreshTokenDTO,Long> tokenDao = connection.newDao(RefreshTokenDTO.class);

            UserDTO existingUser = userDao.get(userId);
            //If it passes this filter without an exception, it can safely proceed. The filter is provided via a lambda from the calling class
            filter.filter(existingUser);

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
            if(result != null){
                result.setPassword(null);
            }

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

    @FunctionalInterface
    public interface ExtraSecurityFilter {
        void filter(UserDTO user);
    }

}
