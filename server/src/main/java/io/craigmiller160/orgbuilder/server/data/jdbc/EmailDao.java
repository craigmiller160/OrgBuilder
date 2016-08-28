package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.*;

/**
 * Created by craig on 8/23/16.
 */
public class EmailDao extends AbstractJdbcMemberJoinDao<EmailDTO,Long,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 4;

    public EmailDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected void parameterizeElement(PreparedStatement stmt, EmailDTO element) throws SQLException {
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

        if(element.getMemberId() > 0){
            stmt.setLong(3, element.getMemberId());
        }
        else{
            stmt.setNull(3, Types.BIGINT);
        }
    }

    @Override
    protected EmailDTO parseResult(ResultSet resultSet) throws SQLException {
        EmailDTO element = new EmailDTO();
        element.setEmailId(resultSet.getLong("email_id"));
        String emailType = resultSet.getString("email_type");
        if(!StringUtils.isEmpty(emailType)){
            element.setEmailType(EmailDTO.EmailType.valueOf(emailType));
        }

        element.setEmailAddress(resultSet.getString("email_address"));
        element.setMemberId(resultSet.getLong("member_id"));

        return element;
    }

    @Override
    protected String getElementName() {
        return EmailDTO.class.getSimpleName();
    }

    @Override
    protected int getUpdatedParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }

}
