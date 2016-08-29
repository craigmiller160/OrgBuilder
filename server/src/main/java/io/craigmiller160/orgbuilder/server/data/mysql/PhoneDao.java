package io.craigmiller160.orgbuilder.server.data.mysql;

import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.dto.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.converter.DTOSQLConverterFactory;

import java.sql.Connection;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.mysql.JdbcManager.Query;

/**
 * Created by craig on 8/22/16.
 */
public class PhoneDao extends AbstractJdbcMemberJoinDao<PhoneDTO,Long> {

    public PhoneDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected DTOSQLConverter<PhoneDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(PhoneDTO.class);
    }

    @Override
    protected String getElementName() {
        return PhoneDTO.class.getSimpleName();
    }

    @Override
    protected Long getIdForElement(PhoneDTO element) {
        return element.getPhoneId();
    }
}
