package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;

import java.sql.Connection;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/21/16.
 */
public class OrgDao extends AbstractJdbcDao<OrgDTO,Long> {



    public OrgDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected String getElementName() {
        return OrgDTO.class.getSimpleName();
    }

    @Override
    protected DTOSQLConverter<OrgDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(OrgDTO.class);
    }
}
