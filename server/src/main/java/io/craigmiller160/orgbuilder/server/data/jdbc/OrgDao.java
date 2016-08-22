package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 8/21/16.
 */
public class OrgDao extends AbstractJdbcDao<OrgDTO,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 2;

    private static final String INSERT_QUERY =
            "INSERT INTO orgs (org_name) " +
            "VALUES (?);";

    private static final String UPDATE_QUERY =
            "UPDATE orgs " +
            "SET org_name = ? " +
            "WHERE org_id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM orgs " +
            "WHERE org_id = ?;";

    private static final String GET_BY_ID_QUERY =
            "SELECT * " +
            "FROM orgs " +
            "WHERE org_id = ?;";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) AS org_count " +
            "FROM orgs;";

    private static final String GET_ALL_QUERY =
            "SELECT * " +
            "FROM orgs " +
            "ORDER BY org_id ASC;";

    private static final String GET_ALL_LIMIT_QUERY =
            "SELECT * " +
            "FROM orgs " +
            "ORDER BY org_id ASC " +
            "LIMIT ?,?;";

    public OrgDao(Connection connection) {
        super(connection);
    }

    private void parameterizeOrg(PreparedStatement stmt, OrgDTO element) throws SQLException{
        if(element.getOrgName() != null){
            stmt.setString(1, element.getOrgName());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }
    }

    private OrgDTO parseResult(ResultSet resultSet) throws SQLException{
        OrgDTO element = new OrgDTO();
        element.setOrgId(resultSet.getLong("org_id"));
        element.setOrgName(resultSet.getString("org_name"));
        Date createdDate = resultSet.getDate("created_date");
        if(createdDate != null){
            element.setCreatedDate(createdDate.toLocalDate());
        }
        element.setSchemaName(resultSet.getString("schema_name"));

        return element;
    }

    @Override
    public OrgDTO insert(OrgDTO element) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Org Insert Query:\n" + INSERT_QUERY);
        try{
            long orgId = -1;
            try(PreparedStatement stmt = getConnection().prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)){
                parameterizeOrg(stmt, element);
                stmt.executeUpdate();
                try(ResultSet resultSet = stmt.getGeneratedKeys()){
                    if(!resultSet.next()){
                        throw new SQLException("Unable to retrieve ID from inserted Org. Org: " + element.toString());
                    }
                    orgId = resultSet.getLong(1);
                }
            }

            element = get(orgId);
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to insert Org. Org: " + element.toString(), ex);
        }

        return element;
    }

    @Override
    public OrgDTO update(OrgDTO element) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Org Update Query:\n" + UPDATE_QUERY);
        try(PreparedStatement stmt = getConnection().prepareStatement(UPDATE_QUERY)){
            parameterizeOrg(stmt, element);
            stmt.setLong(UPDATE_KEY_PARAM_INDEX, element.getOrgId());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to update Org. Org: " + element.toString(), ex);
        }

        return element;
    }

    @Override
    public OrgDTO delete(Long id) throws OrgApiDataException {
        OrgDTO element = null;
        try{
            element = get(id);
            OrgApiLogger.getDataLogger().trace("Org Delete Query:\n" + DELETE_QUERY);
            try(PreparedStatement stmt = getConnection().prepareStatement(DELETE_QUERY)){
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete Org. ID: " + id, ex);
        }

        return element;
    }

    @Override
    public OrgDTO get(Long id) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Org Get By ID Query:\n" + GET_BY_ID_QUERY);
        OrgDTO element = null;
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_BY_ID_QUERY)){
            stmt.setLong(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    element = parseResult(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get Org by ID. ID: " + id, ex);
        }

        return element;
    }

    @Override
    public int getCount() throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Org Count Query:\n" + COUNT_QUERY);
        int count = -1;
        try(Statement stmt = getConnection().createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(COUNT_QUERY)){
                if(resultSet.next()){
                    count = resultSet.getInt("org_count");
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get count of Orgs table", ex);
        }

        return count;
    }

    @Override
    public List<OrgDTO> getAll() throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Org Get All Query:\n" + GET_ALL_QUERY);
        List<OrgDTO> elements = new ArrayList<>();
        try(Statement stmt = getConnection().createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(GET_ALL_QUERY)){
                while(resultSet.next()){
                    OrgDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve all orgs", ex);
        }

        return elements;
    }

    @Override
    public List<OrgDTO> getAll(long offset, long size) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Org Get All Limit Query:\n" + GET_ALL_LIMIT_QUERY);
        List<OrgDTO> elements = new ArrayList<>();
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_ALL_LIMIT_QUERY)){
            stmt.setLong(1, offset);
            stmt.setLong(2, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    OrgDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve orgs within range. Offset: " + offset + " Size: " + size, ex);
        }

        return elements;
    }
}
