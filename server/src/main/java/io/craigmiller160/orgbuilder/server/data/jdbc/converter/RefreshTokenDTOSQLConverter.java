package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by craig on 9/27/16.
 */
public class RefreshTokenDTOSQLConverter implements DTOSQLConverter<RefreshTokenDTO> {

    @Override
    public void parameterizeElement(PreparedStatement stmt, RefreshTokenDTO element) throws SQLException {

    }

    @Override
    public RefreshTokenDTO parseResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return 0;
    }
}
