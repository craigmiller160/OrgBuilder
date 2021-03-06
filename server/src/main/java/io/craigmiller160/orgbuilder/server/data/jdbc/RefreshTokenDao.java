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
            throw new OrgApiDataException("Unable to retrieve " + getElementName() + " with hash. Hash: " + hash, ex);
        }
        return result;
    }

    public long deleteByOrg(long orgId) throws OrgApiDataException{
        String deleteByOrgQuery = queries.get(JdbcManager.Query.DELETE_BY_ORG);
        OrgApiLogger.getDataLogger().trace(getElementName() + " DeleteByOrgQuery Query:\n" + deleteByOrgQuery);
        long result = -1;
        try(PreparedStatement stmt = connection.prepareStatement(deleteByOrgQuery)){
            stmt.setLong(1, orgId);
            result = stmt.executeUpdate();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete all " + getElementName() + " with orgId. OrgId: " + orgId, ex);
        }

        return result;
    }

    public long deleteByUser(long userId) throws OrgApiDataException{
        String deleteByUserQuery = queries.get(JdbcManager.Query.DELETE_BY_USER);
        OrgApiLogger.getDataLogger().trace(getElementName() + " DeleteByUserQuery Query:\n" + deleteByUserQuery);
        long result = -1;
        try(PreparedStatement stmt = connection.prepareStatement(deleteByUserQuery)){
            stmt.setLong(1, userId);
            result = stmt.executeUpdate();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete all " + getElementName() + " with userId. UserId: " + userId, ex);
        }

        return result;
    }
}
