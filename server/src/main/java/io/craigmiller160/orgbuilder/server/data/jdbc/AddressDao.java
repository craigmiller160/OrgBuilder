package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;

import java.sql.Connection;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/21/16.
 */
public class AddressDao extends AbstractJdbcMemberJoinDao<AddressDTO,Long> {

    public AddressDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected DTOSQLConverter<AddressDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(AddressDTO.class);
    }

    @Override
    protected String getElementName() {
        return AddressDTO.class.getSimpleName();
    }

    @Override
    protected Long getIdForElement(AddressDTO element) {
        return element.getElementId();
    }
}
