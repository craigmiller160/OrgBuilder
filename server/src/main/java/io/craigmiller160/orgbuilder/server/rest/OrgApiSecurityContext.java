package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.dto.UserDTO;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by craig on 9/4/16.
 */
public class OrgApiSecurityContext implements SecurityContext {

    private UserDTO user;

    public OrgApiSecurityContext(){
        this(null);
    }

    public OrgApiSecurityContext(UserDTO user){
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    public UserDTO getUser(){
        return user;
    }

    public void setUser(UserDTO user){
        this.user = user;
    }

    @Override
    public boolean isUserInRole(String roleString) {
        return user != null && user.getRole().equals(Role.valueOf(roleString));
    }

    @Override
    public boolean isSecure() {
        //TODO figure out how to finish this
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        //TODO figure out how to finish this
        return null;
    }
}
