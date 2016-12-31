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

    private static final int UPDATE_KEY_PARAM_INDEX = 4;

    @Override
    public void parameterizeElement(PreparedStatement stmt, OrgDTO element) throws SQLException {
        if(element.getElementId() > 0){
            stmt.setLong(1, element.getElementId());
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

        if(element.getOrgDescription() != null){
            stmt.setString(3, element.getOrgDescription());
        }
        else{
            stmt.setNull(3, Types.VARCHAR);
        }
    }

    @Override
    public OrgDTO parseResultSet(ResultSet resultSet) throws SQLException {
        OrgDTO element = new OrgDTO();
        element.setElementId(resultSet.getLong("org_id"));
        element.setOrgName(resultSet.getString("org_name"));
        element.setOrgDescription(resultSet.getString("org_description"));
        Date createdDate = resultSet.getDate("created_date");
        if(createdDate != null){
            element.setCreatedDate(createdDate.toLocalDate());
        }
        element.setSchemaName(resultSet.getString("org_schema_name"));

        return element;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
