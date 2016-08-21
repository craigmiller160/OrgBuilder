package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
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
            "SELECT * FROM members;";

    private static final String GET_ALL_LIMIT_QUERY =
            "SELECT * " +
            "FROM members " +
            "LIMIT ?,?;";

    public MemberDao(Connection connection) {
        super(connection);
    }

    private void parameterizeMember(PreparedStatement stmt, MemberDTO element) throws SQLException{
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

    private MemberDTO parseResult(ResultSet resultSet) throws SQLException{
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
    public MemberDTO insert(MemberDTO element)  throws OrgApiDataException {
        try(PreparedStatement stmt = getConnection().prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)){
            parameterizeMember(stmt, element);
            stmt.executeUpdate();
            try(ResultSet resultSet = stmt.getGeneratedKeys()){
                if(!resultSet.next()){
                    throw new OrgApiDataException("Unable to retrieve ID from inserted Member. Member: " + element.toString());
                }
                element.setMemberId(resultSet.getLong(1));
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to insert Member. Member: " + element.toString(), ex);
        }

        return element;
    }

    @Override
    public MemberDTO update(MemberDTO element)  throws OrgApiDataException{
        try(PreparedStatement stmt = getConnection().prepareStatement(UPDATE_QUERY)){
            parameterizeMember(stmt, element);
            stmt.setLong(UPDATE_KEY_PARAM_INDEX, element.getMemberId());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to update Member. Member: " + element.toString(), ex);
        }

        return element;
    }

    @Override
    public MemberDTO delete(Long id) throws OrgApiDataException {
        MemberDTO element = null;
        try{
            try(PreparedStatement stmt = getConnection().prepareStatement(GET_BY_ID_QUERY)){
                stmt.setLong(1, id);
                try(ResultSet resultSet = stmt.executeQuery()){
                    if(resultSet.next()){
                        element = parseResult(resultSet);
                    }
                }
            }

            try(PreparedStatement stmt = getConnection().prepareStatement(DELETE_QUERY)){
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete member. ID: " + id, ex);
        }

        return element;
    }

    @Override
    public MemberDTO get(Long id) throws OrgApiDataException {
        MemberDTO element = null;
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_BY_ID_QUERY)){
            stmt.setLong(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    element = parseResult(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve member by id. ID: " + id, ex);
        }

        return element;
    }

    @Override
    public int getCount() throws OrgApiDataException {
        int count = -1;
        try(Statement stmt = getConnection().createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(COUNT_QUERY)){
                if(resultSet.next()){
                    count = resultSet.getInt("member_count");
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve count from members table", ex);
        }

        return count;
    }

    @Override
    public List<MemberDTO> getAll() throws OrgApiDataException {
        List<MemberDTO> elements = new ArrayList<>();
        try(Statement stmt = getConnection().createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(GET_ALL_QUERY)){
                while(resultSet.next()){
                    MemberDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve all members", ex);
        }

        return elements;
    }

    @Override
    public List<MemberDTO> getAll(long offset, long size) throws OrgApiDataException {
        List<MemberDTO> elements = new ArrayList<>();
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_ALL_LIMIT_QUERY)){
            stmt.setLong(1, offset);
            stmt.setLong(2, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    MemberDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve members within range. Offset: " + offset + " Size: " + size, ex);
        }

        return elements;
    }
}
