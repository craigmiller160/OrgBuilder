package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.*;

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

    public OrgDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected String getElementName() {
        return OrgDTO.class.getSimpleName();
    }

    @Override
    protected void parameterizeElement(PreparedStatement stmt, OrgDTO element) throws SQLException{
        if(element.getOrgName() != null){
            stmt.setString(1, element.getOrgName());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }
    }

    @Override
    protected OrgDTO parseResult(ResultSet resultSet) throws SQLException{
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
        return executeInsert(element);
    }

    @Override
    public OrgDTO update(OrgDTO element) throws OrgApiDataException {
        return executeUpdate(element, element.getOrgId(), UPDATE_KEY_PARAM_INDEX);
    }

    @Override
    public OrgDTO delete(Long id) throws OrgApiDataException {
        return executeDelete(id);
    }

    @Override
    public OrgDTO get(Long id) throws OrgApiDataException {
        return executeGet(id);
    }

    @Override
    public long getCount() throws OrgApiDataException {
        return executeCount();
    }

    @Override
    public List<OrgDTO> getAll() throws OrgApiDataException {
        return executeGetAll();
    }

    @Override
    public List<OrgDTO> getAll(long offset, long size) throws OrgApiDataException {
        return executeGetAllLimit(offset, size);
    }
}
