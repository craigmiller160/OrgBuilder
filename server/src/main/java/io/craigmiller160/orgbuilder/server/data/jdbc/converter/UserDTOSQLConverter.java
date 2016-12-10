package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.rest.Role;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by craig on 9/4/16.
 */
public class UserDTOSQLConverter implements DTOSQLConverter<UserDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 8;

    @Override
    public void parameterizeElement(PreparedStatement stmt, UserDTO element) throws SQLException {
        if(element.getElementId() > 0){
            stmt.setLong(1, element.getElementId());
        }
        else{
            stmt.setNull(1, Types.BIGINT);
        }

        if(!StringUtils.isEmpty(element.getUserEmail())){
            stmt.setString(2, element.getUserEmail());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        if(!StringUtils.isEmpty(element.getPassword())){
            stmt.setString(3, element.getPassword());
        }
        else{
            stmt.setNull(3, Types.VARCHAR);
        }

        if(!StringUtils.isEmpty(element.getFirstName())){
            stmt.setString(4, element.getFirstName());
        }
        else{
            stmt.setNull(4, Types.VARCHAR);
        }

        if(!StringUtils.isEmpty(element.getLastName())){
            stmt.setString(5, element.getLastName());
        }
        else{
            stmt.setNull(5, Types.VARCHAR);
        }

        if(!StringUtils.isEmpty(element.convertRolesToString())){
            stmt.setString(6, element.convertRolesToString());
        }
        else{
            stmt.setNull(6, Types.VARCHAR);
        }

        if(element.getOrgId() > 0){
            stmt.setLong(7, element.getOrgId());
        }
        else{
            stmt.setNull(7, Types.BIGINT);
        }
    }

    @Override
    public UserDTO parseResultSet(ResultSet resultSet) throws SQLException {
        UserDTO userDTO = new UserDTO();
        userDTO.setElementId(resultSet.getLong("user_id"));
        userDTO.setUserEmail(resultSet.getString("user_email"));
        userDTO.setPassword(resultSet.getString("passwd"));
        String roles = resultSet.getString("role");
        if(!StringUtils.isEmpty(roles)){
            userDTO.convertStringToRoles(roles);
        }
        userDTO.setOrgId(resultSet.getLong("org_id"));
        userDTO.setOrgName(resultSet.getString("org_name"));

        return userDTO;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
