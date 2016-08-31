package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by craig on 8/28/16.
 */
public class OrgDTOSQLConverter implements DTOSQLConverter<OrgDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 3;

    @Override
    public void parameterizeElement(PreparedStatement stmt, OrgDTO element) throws SQLException {
        if(element.getOrgId() > 0){
            stmt.setLong(1, element.getOrgId());
        }
        else{
            stmt.setNull(1, Types.BIGINT);
        }

        if(element.getOrgName() != null){
            stmt.setString(2, element.getOrgName());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }
    }

    @Override
    public OrgDTO parseResultSet(ResultSet resultSet) throws SQLException {
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
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
