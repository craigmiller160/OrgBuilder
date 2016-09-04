package io.craigmiller160.orgbuilder.server.rest;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by craig on 9/4/16.
 */
public class OrgApiSecurityContext implements SecurityContext {

    private UserOrgPrincipal principal;

    public OrgApiSecurityContext(){
        this(null);
    }

    public OrgApiSecurityContext(UserOrgPrincipal principal){
        this.principal = principal;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    public UserOrgPrincipal getUser(){
        return principal;
    }

    public void setUserPrincipal(UserOrgPrincipal principal){
        this.principal = principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return principal != null && principal.isUserInRole(role);
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
