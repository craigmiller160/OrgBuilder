package io.craigmiller160.orgbuilder.server.dto;

import io.craigmiller160.orgbuilder.server.rest.Role;

/**
 * Created by craig on 9/4/16.
 */
public class UserDTO implements Comparable<UserDTO>, DTO<Long>{

    private long userId;
    private String userName;
    private String userEmail;
    private String password;
    private Role role;
    private long orgId;

    @Override
    public Long getElementId() {
        return userId;
    }

    @Override
    public void setElementId(Long id) {
        this.userId = id;
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

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (userId != userDTO.userId) return false;
        if (orgId != userDTO.orgId) return false;
        if (userName != null ? !userName.equals(userDTO.userName) : userDTO.userName != null) return false;
        if (userEmail != null ? !userEmail.equals(userDTO.userEmail) : userDTO.userEmail != null) return false;
        if (password != null ? !password.equals(userDTO.password) : userDTO.password != null) return false;
        return role == userDTO.role;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (int) (orgId ^ (orgId >>> 32));
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