package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by craig on 8/28/16.
 */
public class EmailDTOSQLConverter implements DTOSQLConverter<EmailDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 5;

    @Override
    public void parameterizeElement(PreparedStatement stmt, EmailDTO element) throws SQLException {
        if(element.getEmailType() != null){
            stmt.setString(1, element.getEmailType().toString());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }

        if(element.getEmailAddress() != null){
            stmt.setString(2, element.getEmailAddress());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        stmt.setBoolean(3, element.isPreferred());

        if(element.getMemberId() > 0){
            stmt.setLong(4, element.getMemberId());
        }
        else{
            stmt.setNull(4, Types.BIGINT);
        }
    }

    @Override
    public EmailDTO parseResultSet(ResultSet resultSet) throws SQLException {
        EmailDTO element = new EmailDTO();
        element.setEmailId(resultSet.getLong("email_id"));
        String emailType = resultSet.getString("email_type");
        if(!StringUtils.isEmpty(emailType)){
            element.setEmailType(EmailDTO.EmailType.valueOf(emailType));
        }

        element.setEmailAddress(resultSet.getString("email_address"));
        element.setPreferred(resultSet.getBoolean("preferred_email"));
        element.setMemberId(resultSet.getLong("email_member_id"));

        return element;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}