package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 9/15/16.
 */

public class UserService {

    //TODO when adding a new user, must first check if that user exists... or maybe just rely on unique constraint on table... or maybe on the forthcoming search functionality...

    //TODO when adding or updating a user, the password must be hashed

    private final ServiceCommons serviceCommons;

    UserService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, true);
    }

    public UserDTO addUser(UserDTO user) throws OrgApiDataException, OrgApiSecurityException{
        user.setElementId(-1L);
        user.setPassword(HashingUtils.hashBCrypt(user.getPassword()));
        DataConnection connection = null;
        UserDTO result = null;
        try{
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
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            user.setElementId(userId);
            result = userDao.update(user, userId);

            connection.commit();
        }
        catch (OrgApiDataException ex){
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
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            result = userDao.delete(userId);

            connection.commit();
        }
        catch (OrgApiDataException ex){
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
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            result = userDao.get(userId);

            connection.commit();
        }
        catch (OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally {
            serviceCommons.closeConnection(connection);
        }

        return result;
    }

    public UserListDTO getAllUsers(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        UserListDTO results = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<UserDTO,Long> userDao = connection.newDao(UserDTO.class);

            List<UserDTO> list = (offset >= 0 && size >= 0) ? userDao.getAll(offset, size) : userDao.getAll();
            if(list.size() > 0){
                results = new UserListDTO(list);
            }
        }
        catch (OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally {
            serviceCommons.closeConnection(connection);
        }

        return results;
    }

}
