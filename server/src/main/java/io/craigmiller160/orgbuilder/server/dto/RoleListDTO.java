package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by craigmiller on 10/17/16.
 */
@XmlRootElement
public class RoleListDTO {

    private List<String> roles;

    public RoleListDTO(){}

    public RoleListDTO(List<String> roles) {
        this.roles = roles;
    }

    public RoleListDTO(String[] roles){
        setRoles(roles);
    }

    public void setRoles(String[] roles){
        this.roles = roles != null ? Arrays.asList(roles) : null;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleListDTO that = (RoleListDTO) o;

        return roles != null ? roles.equals(that.roles) : that.roles == null;

    }

    @Override
    public int hashCode() {
        return roles != null ? roles.hashCode() : 0;
    }
}
