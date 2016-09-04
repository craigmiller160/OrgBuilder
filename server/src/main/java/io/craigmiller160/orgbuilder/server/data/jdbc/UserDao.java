package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.UserDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;

import java.sql.Connection;
import java.util.Map;

/**
 * Created by craig on 9/4/16.
 */
public class UserDao extends AbstractJdbcDao<UserDTO,Long> {

    public UserDao(Connection connection, Map<JdbcManager.Query, String> queries) {
        super(connection, queries);
    }

    @Override
    protected String getElementName() {
        return UserDTO.class.getSimpleName();
    }

    @Override
    protected DTOSQLConverter<UserDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(UserDTO.class);
    }

}
