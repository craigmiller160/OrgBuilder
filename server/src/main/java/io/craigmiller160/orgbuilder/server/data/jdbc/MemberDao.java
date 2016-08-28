package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.converter.DTOSQLConverterFactory;

import java.sql.Connection;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/18/16.
 */
public class MemberDao extends AbstractJdbcDao<MemberDTO,Long> {

    public MemberDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected DTOSQLConverter<MemberDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(MemberDTO.class);
    }

    @Override
    protected String getElementName() {
        return MemberDTO.class.getSimpleName();
    }
}
