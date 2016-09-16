package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;

import javax.ws.rs.core.SecurityContext;

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
        //TODO finish this
        return null;
    }

    public UserDTO updateUser(UserDTO user) throws OrgApiDataException, OrgApiSecurityException{
        //TODO finish this
        return null;
    }

    public UserDTO deleteUser(Long userId) throws OrgApiDataException, OrgApiSecurityException{
        //TODO finish this
        return null;
    }

    public UserDTO getUser(Long userId) throws OrgApiDataException, OrgApiSecurityException{
        //TODO finish this
        return null;
    }

    public UserListDTO getAllUsers(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        //TODO finish this
        return null;
    }

}
