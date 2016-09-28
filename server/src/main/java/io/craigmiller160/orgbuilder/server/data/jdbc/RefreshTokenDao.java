package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;

import java.sql.Connection;
import java.util.Map;

/**
 * Created by craig on 9/28/16.
 */
public class RefreshTokenDao extends AbstractJdbcDao<RefreshTokenDTO,Long> {

    public RefreshTokenDao(Connection connection, Map<JdbcManager.Query, String> queries) {
        super(connection, queries);
    }

    @Override
    protected String getElementName() {
        return RefreshTokenDTO.class.getSimpleName();
    }

    @Override
    protected DTOSQLConverter<RefreshTokenDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(RefreshTokenDTO.class);
    }
}
