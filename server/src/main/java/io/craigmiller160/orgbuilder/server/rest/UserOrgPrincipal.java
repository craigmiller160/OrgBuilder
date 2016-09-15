package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;

import java.security.Principal;

/**
 * Created by craig on 9/4/16.
 */
public class UserOrgPrincipal implements Principal {

    private UserDTO user;
    private OrgDTO org;

    public UserOrgPrincipal(){
        this(null,null);
    }

    public UserOrgPrincipal(UserDTO user, OrgDTO org){
        this.user = user;
        this.org = org;
    }

    public void setUser(UserDTO user){
        this.user = user;
    }

    public void setOrg(OrgDTO org){
        this.org = org;
    }

    @Override
    public String getName() {
        String userName = user != null ? user.getUserName() : "NULL";
        String orgName = org != null ? org.getOrgName() : "NULL";
        return String.format("%s (%s)", userName, orgName);
    }

    public UserDTO getUser(){
        return user;
    }

    public OrgDTO getOrg(){
        return org;
    }

    public boolean isUserInRole(String role){
        return user != null && user.getRoles().contains(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOrgPrincipal that = (UserOrgPrincipal) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return org != null ? org.equals(that.org) : that.org == null;

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (org != null ? org.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return getName();
    }
}
