package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/22/16.
 */
public class PhoneDao extends AbstractJdbcMemberJoinDao<PhoneDTO,Long,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 8;

    public PhoneDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected void parameterizeElement(PreparedStatement stmt, PhoneDTO element) throws SQLException{
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
    protected PhoneDTO parseResult(ResultSet resultSet) throws SQLException {
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
        element.setPreferred(resultSet.getBoolean("preferred"));
        element.setMemberId(resultSet.getLong("member_id"));

        return element;
    }

    @Override
    protected String getElementName() {
        return PhoneDTO.class.getSimpleName();
    }

    @Override
    protected int getUpdatedParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
