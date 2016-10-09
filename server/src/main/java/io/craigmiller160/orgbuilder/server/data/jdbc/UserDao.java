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
import java.util.ArrayList;
import java.util.List;
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
                    result = parseResultSet(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get " + getElementName() + " with provided name. Name: " + name, ex);
        }

        return result;
    }

    public UserDTO getByIdAndOrg(long userId, long orgId) throws OrgApiDataException{
        String getByIdAndOrgQuery = queries.get(JdbcManager.Query.GET_BY_ID_AND_ORG);
        OrgApiLogger.getDataLogger().trace(getElementName() + " GetByIdAndOrg Query:\n" + getByIdAndOrgQuery);
        UserDTO result = null;
        try(PreparedStatement stmt = connection.prepareStatement(getByIdAndOrgQuery)){
            stmt.setLong(1, userId);
            stmt.setLong(2, orgId);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    result = parseResultSet(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get " + getElementName() + " by userId and orgId. UserId: " + userId + " | OrgId: " + orgId);
        }

        return result;
    }

    public long countByOrg(long orgId) throws OrgApiDataException{
        String countByOrgQuery = queries.get(JdbcManager.Query.COUNT_BY_ORG);
        OrgApiLogger.getDataLogger().trace(getElementName() + " CountByOrgQuery Query:\n" + countByOrgQuery);
        long result = -1;
        try(PreparedStatement stmt = connection.prepareStatement(countByOrgQuery)){
            stmt.setLong(1, orgId);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    result = resultSet.getLong(1);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to count " + getElementName() + " by orgId. OrgId: " + orgId);
        }

        return result;
    }

    public List<UserDTO> getAllByOrg(long orgId) throws OrgApiDataException{
        String getAllByOrgQuery = queries.get(JdbcManager.Query.GET_ALL_BY_ORG);
        OrgApiLogger.getDataLogger().trace(getElementName() + " GetAllByOrgQuery Query:\n" + getAllByOrgQuery);
        List<UserDTO> result = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(getAllByOrgQuery)){
            stmt.setLong(1, orgId);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    result.add(parseResultSet(resultSet));
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get all " + getElementName() + " by orgId. OrgId: " + orgId);
        }

        return result;
    }

    public List<UserDTO> getAllLimitByOrg(long orgId, long offset, long size) throws OrgApiDataException{
        String getAllByOrgLimitQuery = queries.get(JdbcManager.Query.GET_ALL_LIMIT_BY_ORG);
        OrgApiLogger.getDataLogger().trace(getElementName() + " GetAllByOrgLimitQuery Query:\n" + getAllByOrgLimitQuery);
        List<UserDTO> result = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(getAllByOrgLimitQuery)){
            stmt.setLong(1, orgId);
            stmt.setLong(2, offset);
            stmt.setLong(3, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    result.add(parseResultSet(resultSet));
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get all " + getElementName() + " within range by orgId. OrgId: " + orgId + " | Offset: " + offset + " | Size: " + size);
        }

        return result;
    }

}
