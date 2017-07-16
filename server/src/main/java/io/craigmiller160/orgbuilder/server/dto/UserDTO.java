package io.craigmiller160.orgbuilder.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.text.StrBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by craig on 9/4/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "UserDTO")
public class UserDTO extends LoginDTO implements Comparable<UserDTO>, DTO<Long>{

    private long userId;
    private Set<String> roles = new TreeSet<>();
    private long orgId;
    private String orgName;
    private String firstName;
    private String lastName;

    @Override
    @ApiModelProperty(name = "userId")
    public Long getElementId() {
        return userId;
    }

    @Override
    public void setElementId(Long id) {
        this.userId = id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = new TreeSet<>();
        if(roles != null){
            this.roles.addAll(roles);
        }
    }

    public String convertRolesToString(){
        if(roles.size() > 0){
            StrBuilder builder = new StrBuilder();
            roles.forEach((r) -> builder.append(r).append(","));
            builder.replace(builder.length() - 1, builder.length(), "");
            return builder.toString();
        }
        return "";
    }

    public void convertStringToRoles(String rolesString){
        String[] roleStrings = rolesString.split(",");
        Arrays.stream(roleStrings).forEach((r) -> roles.add(r));
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }



    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserDTO userDTO = (UserDTO) o;

        if (userId != userDTO.userId) return false;
        if (orgId != userDTO.orgId) return false;
        if (roles != null ? !roles.equals(userDTO.roles) : userDTO.roles != null) return false;
        if (orgName != null ? !orgName.equals(userDTO.orgName) : userDTO.orgName != null) return false;
        if (firstName != null ? !firstName.equals(userDTO.firstName) : userDTO.firstName != null) return false;
        return lastName != null ? lastName.equals(userDTO.lastName) : userDTO.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        result = 31 * result + (int) (orgId ^ (orgId >>> 32));
        result = 31 * result + (orgName != null ? orgName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "User: " + getUserEmail();
    }

    @Override
    public int compareTo(UserDTO o) {
        return new Long(this.userId).compareTo(o.userId);
    }
}
