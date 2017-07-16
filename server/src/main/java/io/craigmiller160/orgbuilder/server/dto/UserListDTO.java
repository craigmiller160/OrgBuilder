package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserListDTO {

    private List<UserDTO> userList;

    public UserListDTO(){
        this(null);
    }

    public UserListDTO(List<UserDTO> userList) {
        this.userList = userList;
    }

    public List<UserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDTO> userList) {
        this.userList = userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserListDTO that = (UserListDTO) o;

        return userList != null ? userList.equals(that.userList) : that.userList == null;

    }

    @Override
    public int hashCode() {
        return userList != null ? userList.hashCode() : 0;
    }
}
