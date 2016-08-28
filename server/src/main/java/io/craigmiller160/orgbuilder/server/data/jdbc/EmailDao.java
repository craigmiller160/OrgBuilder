package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.JoinedWithMemberDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/23/16.
 */
public class EmailDao extends AbstractJdbcMemberJoinDao<EmailDTO,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 5;

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

        stmt.setBoolean(3, element.isPreferred());

        if(element.getMemberId() > 0){
            stmt.setLong(4, element.getMemberId());
        }
        else{
            stmt.setNull(4, Types.BIGINT);
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
        element.setPreferred(resultSet.getBoolean("preferred_email"));
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

    @Override
    protected Long getIdForElement(EmailDTO element) {
        return element.getEmailId();
    }
}
