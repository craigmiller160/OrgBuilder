package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.util.DataDTOMap;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

/**
 * Created by craig on 8/28/16.
 */
public class MemberDTOSQLConverter implements DTOSQLConverter<MemberDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 6;

    @Override
    public void parameterizeElement(PreparedStatement stmt, MemberDTO element) throws SQLException {
        if(element.getFirstName() != null){
            stmt.setString(1, element.getFirstName());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }

        if(element.getMiddleName() != null){
            stmt.setString(2, element.getMiddleName());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        if(element.getLastName() != null){
            stmt.setString(3, element.getLastName());
        }
        else{
            stmt.setNull(3, Types.VARCHAR);
        }

        if(element.getDateOfBirth() != null){
            stmt.setDate(4, Date.valueOf(element.getDateOfBirth()));
        }
        else{
            stmt.setNull(4, Types.DATE);
        }

        if(element.getGender() != null){
            stmt.setString(5, element.getGender().toString());
        }
        else{
            stmt.setNull(5, Types.VARCHAR);
        }
    }

    @Override
    public MemberDTO parseResultSet(ResultSet resultSet) throws SQLException {
        MemberDTO element = new MemberDTO();
        element.setMemberId(resultSet.getLong("member_id"));
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

        Map<Class,DataDTOMap> map = ServerCore.getDataDTOMap();

        if(resultSet.getObject("address_id") != null){
            AddressDTO preferredAddress = (AddressDTO) map.get(AddressDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.setPreferredAddress(preferredAddress);
        }

        if(resultSet.getObject("phone_id") != null){
            PhoneDTO preferredPhone = (PhoneDTO) map.get(PhoneDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.setPreferredPhone(preferredPhone);
        }

        if(resultSet.getObject("email_id") != null){
            EmailDTO preferredEmail = (EmailDTO) map.get(EmailDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.setPreferredEmail(preferredEmail);
        }

        return element;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }

}
