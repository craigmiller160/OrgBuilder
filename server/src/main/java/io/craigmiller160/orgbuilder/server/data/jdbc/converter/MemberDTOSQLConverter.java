package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by craig on 8/28/16.
 */
public class MemberDTOSQLConverter implements DTOSQLConverter<MemberDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 7;

    @Override
    public void parameterizeElement(PreparedStatement stmt, MemberDTO element) throws SQLException {
        if(element.getElementId() > 0){
            stmt.setLong(1, element.getElementId());
        }
        else{
            stmt.setNull(1, Types.BIGINT);
        }

        if(element.getFirstName() != null){
            stmt.setString(2, element.getFirstName());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        if(element.getMiddleName() != null){
            stmt.setString(3, element.getMiddleName());
        }
        else{
            stmt.setNull(3, Types.VARCHAR);
        }

        if(element.getLastName() != null){
            stmt.setString(4, element.getLastName());
        }
        else{
            stmt.setNull(4, Types.VARCHAR);
        }

        if(element.getDateOfBirth() != null){
            stmt.setDate(5, Date.valueOf(element.getDateOfBirth()));
        }
        else{
            stmt.setNull(5, Types.DATE);
        }

        if(element.getGender() != null){
            stmt.setString(6, element.getGender().toString());
        }
        else{
            stmt.setNull(6, Types.VARCHAR);
        }
    }

    @Override
    public MemberDTO parseResultSet(ResultSet resultSet) throws SQLException {
        MemberDTO element = new MemberDTO();
        element.setElementId(resultSet.getLong("member_id"));
        element.setFirstName(resultSet.getString("first_name"));
        element.setMiddleName(resultSet.getString("middle_name"));
        element.setLastName(resultSet.getString("last_name"));
        Date dateOfBirth = resultSet.getDate("date_of_birth");
        if(dateOfBirth != null){
            element.setDateOfBirth(dateOfBirth.toLocalDate());
        }
        String gender = resultSet.getString("gender");
        if(!StringUtils.isEmpty(gender)){
            element.setGender(Gender.valueOf(gender));
        }

        return element;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }

}
