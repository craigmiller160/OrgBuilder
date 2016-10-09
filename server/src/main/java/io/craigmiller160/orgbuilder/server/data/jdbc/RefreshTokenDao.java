package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public RefreshTokenDTO getWithHash(String hash) throws OrgApiDataException{
        RefreshTokenDTO result = null;
        String getWithHashQuery = queries.get(JdbcManager.Query.GET_WITH_HASH);
        OrgApiLogger.getDataLogger().trace(getElementName() + " GetWithHash Query:\n" + getWithHashQuery);
        try(PreparedStatement stmt = connection.prepareStatement(getWithHashQuery)){
            stmt.setString(1, hash);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    result = parseResultSet(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve " + getElementName() + " with hash. Hash: " + hash);
        }
        return result;
    }
}
