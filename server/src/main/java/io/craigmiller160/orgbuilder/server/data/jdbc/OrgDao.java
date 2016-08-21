package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;

import java.sql.*;
import java.util.List;

/**
 * Created by craig on 8/21/16.
 */
public class OrgDao extends AbstractJdbcDao<OrgDTO,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 2;

    private static final String INSERT_SQL =
            "INSERT INTO orgs (org_name) " +
            "VALUES (?);";

    private static final String UPDATE_SQL =
            "UPDATE orgs " +
            "SET org_name = ? " +
            "WHERE org_id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM orgs " +
            "WHERE org_id = ?;";

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

    @Override
    public OrgDTO insert(OrgDTO element) throws OrgApiDataException {
        try(PreparedStatement stmt = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){
            parameterizeOrg(stmt, element);
            stmt.executeUpdate();
            long orgId = -1;
            try(ResultSet resultSet = stmt.getGeneratedKeys()){
                if(!resultSet.next()){
                    throw new SQLException("Unable to retrieve ID from inserted Org. Org: " + element.toString());
                }
                orgId = resultSet.getLong(1);
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
        try(PreparedStatement stmt = getConnection().prepareStatement(UPDATE_SQL)){
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
        try(PreparedStatement stmt = getConnection().prepareStatement(DELETE_QUERY)){
            //TODO finish this
        }
        catch(SQLException ex){
            //TODO handle this
        }

        return null;
    }

    @Override
    public OrgDTO get(Long id) throws OrgApiDataException {
        return null;
    }

    @Override
    public int getCount() throws OrgApiDataException {
        return 0;
    }

    @Override
    public List<OrgDTO> getAll() throws OrgApiDataException {
        return null;
    }

    @Override
    public List<OrgDTO> getAll(long offset, long size) throws OrgApiDataException {
        return null;
    }
}
