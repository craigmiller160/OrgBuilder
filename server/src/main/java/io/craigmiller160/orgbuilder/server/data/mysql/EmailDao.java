package io.craigmiller160.orgbuilder.server.data.mysql;

import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.converter.DTOSQLConverterFactory;

import java.sql.Connection;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.mysql.JdbcManager.Query;

/**
 * Created by craig on 8/23/16.
 */
public class EmailDao extends AbstractJdbcMemberJoinDao<EmailDTO,Long> {



    public EmailDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected DTOSQLConverter<EmailDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(EmailDTO.class);
    }

    @Override
    protected String getElementName() {
        return EmailDTO.class.getSimpleName();
    }

    @Override
    protected Long getIdForElement(EmailDTO element) {
        return element.getEmailId();
    }
}
