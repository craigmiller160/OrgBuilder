package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Created by craig on 9/27/16.
 */
public class RefreshTokenDTOSQLConverter implements DTOSQLConverter<RefreshTokenDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 6;

    @Override
    public void parameterizeElement(PreparedStatement stmt, RefreshTokenDTO element) throws SQLException {
        if(element.getElementId() > 0){
            stmt.setLong(1, element.getElementId());
        }
        else{
            stmt.setNull(1, Types.BIGINT);
        }

        if(element.getUserId() > 0){
            stmt.setLong(2, element.getUserId());
        }
        else{
            stmt.setNull(2, Types.BIGINT);
        }

        if(element.getOrgId() > 0){
            stmt.setLong(3, element.getOrgId());
        }
        else{
            stmt.setNull(3, Types.BIGINT);
        }

        if(!StringUtils.isEmpty(element.getTokenHash())){
            stmt.setString(4, element.getTokenHash());
        }
        else{
            stmt.setNull(4, Types.VARCHAR);
        }

        if(element.getExpiration() != null){
            stmt.setTimestamp(5, Timestamp.valueOf(element.getExpiration()));
        }
        else{
            stmt.setNull(5, Types.TIMESTAMP);
        }
    }

    @Override
    public RefreshTokenDTO parseResultSet(ResultSet resultSet) throws SQLException {
        RefreshTokenDTO token = new RefreshTokenDTO();
        token.setElementId(resultSet.getLong("token_id"));
        token.setUserId(resultSet.getLong("user_id"));
        token.setOrgId(resultSet.getLong("org_id"));
        token.setTokenHash(resultSet.getString("token_hash"));
        Timestamp timestamp = resultSet.getTimestamp("expiration");
        if(timestamp != null){
            token.setExpiration(timestamp.toLocalDateTime());
        }

        return token;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
