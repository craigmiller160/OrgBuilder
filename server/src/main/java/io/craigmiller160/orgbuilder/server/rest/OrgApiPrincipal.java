package io.craigmiller160.orgbuilder.server.rest;

import java.security.Principal;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by craig on 9/29/16.
 */
public class OrgApiPrincipal implements Principal {

    private Set<String> roles = new TreeSet<>();
    private String name;
    private String schema;
    private long userId;
    private long orgId;

    /**
     * {@inheritDoc}
     *
     * This returns the full principal
     * name, in the format "orgName::userName".
     *
     * @return the full principal name.
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles != null ? roles : new TreeSet<>();
    }

    public boolean isUserInRole(String role){
        return this.roles.contains(role);
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public boolean isAuthorizedForAppSchema(){
        return isUserInRole(Role.MASTER) || isUserInRole(Role.ADMIN);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrgApiPrincipal principal = (OrgApiPrincipal) o;

        if (userId != principal.userId) return false;
        if (orgId != principal.orgId) return false;
        if (roles != null ? !roles.equals(principal.roles) : principal.roles != null) return false;
        if (name != null ? !name.equals(principal.name) : principal.name != null) return false;
        return schema != null ? schema.equals(principal.schema) : principal.schema == null;

    }

    @Override
    public int hashCode() {
        int result = roles != null ? roles.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (orgId ^ (orgId >>> 32));
        return result;
    }

    @Override
    public String toString(){
        return "Principal: " + getName();
    }
}
