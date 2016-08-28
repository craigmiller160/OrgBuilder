package io.craigmiller160.orgbuilder.server.dto.converter;

import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by craig on 8/28/16.
 */
public class PhoneDTOSQLConverter implements DTOSQLConverter<PhoneDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 8;

    @Override
    public void parameterizeElement(PreparedStatement stmt, PhoneDTO element) throws SQLException {
        if(element.getPhoneType() != null){
            stmt.setString(1, element.getPhoneType().toString());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }

        if(!StringUtils.isEmpty(element.getAreaCode())){
            stmt.setString(2, element.getAreaCode());
        }
        else{
            stmt.setNull(2, Types.CHAR);
        }

        if(!StringUtils.isEmpty(element.getPrefix())){
            stmt.setString(3, element.getPrefix());
        }
        else{
            stmt.setNull(3, Types.CHAR);
        }

        if(!StringUtils.isEmpty(element.getLineNumber())){
            stmt.setString(4, element.getLineNumber());
        }
        else{
            stmt.setNull(4, Types.CHAR);
        }

        if(!StringUtils.isEmpty(element.getExtension())){
            stmt.setString(5, element.getExtension());
        }
        else{
            stmt.setNull(5, Types.VARCHAR);
        }

        stmt.setBoolean(6, element.isPreferred());

        if(element.getMemberId() > 0){
            stmt.setLong(7, element.getMemberId());
        }
        else{
            stmt.setNull(7, Types.BIGINT);
        }
    }

    @Override
    public PhoneDTO parseResultSet(ResultSet resultSet) throws SQLException {
        PhoneDTO element = new PhoneDTO();
        element.setPhoneId(resultSet.getLong("phone_id"));
        String phoneType = resultSet.getString("phone_type");
        if(!StringUtils.isEmpty(phoneType)){
            element.setPhoneType(PhoneDTO.PhoneType.valueOf(phoneType));
        }
        element.setAreaCode(resultSet.getString("area_code"));
        element.setPrefix(resultSet.getString("prefix"));
        element.setLineNumber(resultSet.getString("line_number"));
        element.setExtension(resultSet.getString("extension"));
        element.setPreferred(resultSet.getBoolean("preferred_phone"));
        element.setMemberId(resultSet.getLong("phone_member_id"));

        return element;

    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
