package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.UserDTOSQLConverter;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public UserDTO getWithName(String name) throws OrgApiDataException{
        String getWithNameQuery = queries.get(JdbcManager.Query.GET_WITH_NAME);
        OrgApiLogger.getDataLogger().trace(getElementName() + " Get With Name Query:\n" + getWithNameQuery);
        UserDTO result = null;
        try(PreparedStatement stmt = connection.prepareStatement(getWithNameQuery)){
            stmt.setString(1, name);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    result = converter.parseResultSet(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get " + getElementName() + " with provided name. Name: " + name, ex);
        }

        return result;
    }

}
