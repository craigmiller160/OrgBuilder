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
    protected int getUpdatedParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
