package io.craigmiller160.orgbuilder.server.rest;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by craig on 9/4/16.
 */
public class OrgApiSecurityContext implements SecurityContext {

    private OrgApiPrincipal principal;

    public OrgApiSecurityContext(){
        this(null);
    }

    public OrgApiSecurityContext(OrgApiPrincipal principal){
        this.principal = principal;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    public void setUserPrincipal(OrgApiPrincipal principal) {
        this.principal = principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return principal != null && principal.isUserInRole(role);
    }

    @Override
    public boolean isSecure() {
        //TODO FC-14
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        //TODO FC-14
        return null;
    }
}
