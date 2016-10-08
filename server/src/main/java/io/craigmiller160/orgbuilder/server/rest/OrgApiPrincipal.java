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
}
