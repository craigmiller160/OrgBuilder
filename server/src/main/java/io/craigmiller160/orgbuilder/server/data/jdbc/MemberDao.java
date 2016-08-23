package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 8/18/16.
 */
public class MemberDao extends AbstractJdbcDao<MemberDTO,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 6;

    private static final String INSERT_QUERY =
            "INSERT INTO members (first_name, middle_name, last_name, date_of_birth, gender) " +
            "VALUES (?,?,?,?,?);";

    private static final String UPDATE_QUERY =
            "UPDATE members " +
            "SET first_name = ?, middle_name = ?, last_name = ?, date_of_birth = ?, gender = ? " +
            "WHERE member_id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM members " +
            "WHERE member_id = ?;";

    private static final String GET_BY_ID_QUERY =
            "SELECT * " +
            "FROM members " +
            "WHERE member_id = ?;";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) AS member_count FROM members;";

    private static final String GET_ALL_QUERY =
            "SELECT * " +
            "FROM members " +
            "ORDER BY member_id ASC;";

    private static final String GET_ALL_LIMIT_QUERY =
            "SELECT * " +
            "FROM members " +
            "ORDER BY member_id ASC " +
            "LIMIT ?,?;";

    public MemberDao(Connection connection) {
        super(connection);
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
    public MemberDTO insert(MemberDTO element)  throws OrgApiDataException {
        return executeInsert(element, INSERT_QUERY);
    }

    @Override
    public MemberDTO update(MemberDTO element)  throws OrgApiDataException{
        return executeUpdate(element, element.getMemberId(), UPDATE_KEY_PARAM_INDEX, UPDATE_QUERY);
    }

    @Override
    public MemberDTO delete(Long id) throws OrgApiDataException {
        return executeDelete(id, DELETE_QUERY);
    }

    @Override
    public MemberDTO get(Long id) throws OrgApiDataException {
        return executeGet(id, GET_BY_ID_QUERY);
    }

    @Override
    public long getCount() throws OrgApiDataException {
        return executeCount(COUNT_QUERY);
    }

    @Override
    public List<MemberDTO> getAll() throws OrgApiDataException {
        return executeGetAll(GET_ALL_QUERY);
    }

    @Override
    public List<MemberDTO> getAll(long offset, long size) throws OrgApiDataException {
        return executeGetAllLimit(offset, size, GET_ALL_LIMIT_QUERY);
    }
}
