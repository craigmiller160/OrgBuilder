package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/18/16.
 */
public class MemberDao extends AbstractJdbcDao<MemberDTO,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 6;

    public MemberDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected void parameterizeElement(PreparedStatement stmt, MemberDTO element) throws SQLException{
        if(element.getFirstName() != null){
            stmt.setString(1, element.getFirstName());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }

        if(element.getMiddleName() != null){
            stmt.setString(2, element.getMiddleName());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        if(element.getLastName() != null){
            stmt.setString(3, element.getLastName());
        }
        else{
            stmt.setNull(3, Types.VARCHAR);
        }

        if(element.getDateOfBirth() != null){
            stmt.setDate(4, Date.valueOf(element.getDateOfBirth()));
        }
        else{
            stmt.setNull(4, Types.DATE);
        }

        if(element.getGender() != null){
            stmt.setString(5, element.getGender().toString());
        }
        else{
            stmt.setNull(5, Types.VARCHAR);
        }
    }

    @Override
    protected MemberDTO parseResult(ResultSet resultSet) throws SQLException{
        MemberDTO element = new MemberDTO();
        element.setMemberId(resultSet.getLong("member_id"));
        element.setFirstName(resultSet.getString("first_name"));
        element.setMiddleName(resultSet.getString("middle_name"));
        element.setLastName(resultSet.getString("last_name"));
        Date dateOfBirth = resultSet.getDate("date_of_birth");
        if(dateOfBirth != null){
            element.setDateOfBirth(dateOfBirth.toLocalDate());
        }
        String gender = resultSet.getString("gender");
        if(!StringUtils.isEmpty(gender)){
            element.setGender(Gender.valueOf(gender));
        }

        return element;
    }

    @Override
    protected String getElementName() {
        return MemberDTO.class.getSimpleName();
    }

    @Override
    protected int getUpdatedParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
