package io.craigmiller160.orgbuilder.server.dto;

import io.craigmiller160.orgbuilder.server.rest.Role;

import java.security.Principal;

/**
 * Created by craig on 9/4/16.
 */
public class UserDTO implements Principal, Comparable<UserDTO>{

    private long userId;
    private String userName;
    private String userEmail;
    private Role role;
    private OrgDTO org;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public OrgDTO getOrg() {
        return org;
    }

    public void setOrg(OrgDTO org) {
        this.org = org;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (userId != userDTO.userId) return false;
        if (userName != null ? !userName.equals(userDTO.userName) : userDTO.userName != null) return false;
        if (userEmail != null ? !userEmail.equals(userDTO.userEmail) : userDTO.userEmail != null) return false;
        if (role != userDTO.role) return false;
        return org != null ? org.equals(userDTO.org) : userDTO.org == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (org != null ? org.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "User: " + userName;
    }

    @Override
    public int compareTo(UserDTO o) {
        return new Long(this.userId).compareTo(o.userId);
    }
}
