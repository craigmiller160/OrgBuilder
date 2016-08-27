package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;

/**
 * Created by craig on 8/22/16.
 */
public class PhoneDao extends AbstractJdbcMemberJoinDao<PhoneDTO,Long,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 7;

    private static final String INSERT_QUERY =
            "INSERT INTO phones (phone_type, area_code, prefix, line_number, extension, member_id) " +
            "VALUES (?,?,?,?,?,?)";

    private static final String UPDATE_QUERY =
            "UPDATE phones " +
            "SET phone_type = ?, area_code = ?, prefix = ?, line_number = ?, extension = ?, member_id = ? " +
            "WHERE phone_id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM phones " +
            "WHERE phone_id = ?;";

    private static final String GET_BY_ID_QUERY =
            "SELECT * " +
            "FROM phones " +
            "WHERE phone_id = ?;";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) AS phone_count " +
            "FROM phones;";

    private static final String GET_ALL_QUERY =
            "SELECT * " +
            "FROM phones " +
            "ORDER BY phone_id ASC;";

    private static final String GET_ALL_LIMIT_QUERY =
            "SELECT * " +
            "FROM phones " +
            "ORDER BY phone_id ASC " +
            "LIMIT ?,?;";

    private static final String GET_ALL_BY_MEMBER_QUERY =
            "SELECT * " +
            "FROM phones " +
            "WHERE member_id = ? " +
            "ORDER BY phone_id ASC;";

    private static final String GET_ALL_BY_MEMBER_LIMIT_QUERY =
            "SELECT * " +
            "FROM phones " +
            "WHERE member_id = ? " +
            "ORDER BY phone_id ASC " +
            "LIMIT ?,?;";

    private static final String COUNT_BY_MEMBER_QUERY =
            "SELECT COUNT(*) AS phone_by_member_count " +
            "FROM phones " +
            "WHERE member_id = ?;";

    public PhoneDao(Connection connection) {
        super(connection);
    }

    @Override
    protected void parameterizeElement(PreparedStatement stmt, PhoneDTO element) throws SQLException{
        if(element.getPhoneType() != null){
            stmt.setString(1, element.getPhoneType().toString());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }

        if(!StringUtils.isEmpty(element.getAreaCode())){
            stmt.setString(2, element.getAreaCode());
        }
        else{
            stmt.setNull(2, Types.CHAR);
        }

        if(!StringUtils.isEmpty(element.getPrefix())){
            stmt.setString(3, element.getPrefix());
        }
        else{
            stmt.setNull(3, Types.CHAR);
        }

        if(!StringUtils.isEmpty(element.getLineNumber())){
            stmt.setString(4, element.getLineNumber());
        }
        else{
            stmt.setNull(4, Types.CHAR);
        }

        if(!StringUtils.isEmpty(element.getExtension())){
            stmt.setString(5, element.getExtension());
        }
        else{
            stmt.setNull(5, Types.VARCHAR);
        }

        if(element.getMemberId() > 0){
            stmt.setLong(6, element.getMemberId());
        }
        else{
            stmt.setNull(6, Types.BIGINT);
        }
    }

    @Override
    protected PhoneDTO parseResult(ResultSet resultSet) throws SQLException {
        PhoneDTO element = new PhoneDTO();
        element.setPhoneId(resultSet.getLong("phone_id"));
        String phoneType = resultSet.getString("phone_type");
        if(!StringUtils.isEmpty(phoneType)){
            element.setPhoneType(PhoneDTO.PhoneType.valueOf(phoneType));
        }
        element.setAreaCode(resultSet.getString("area_code"));
        element.setPrefix(resultSet.getString("prefix"));
        element.setLineNumber(resultSet.getString("line_number"));
        element.setExtension(resultSet.getString("extension"));
        element.setMemberId(resultSet.getLong("member_id"));

        return element;
    }

    @Override
    protected String getElementName() {
        return PhoneDTO.class.getSimpleName();
    }

    @Override
    public PhoneDTO insert(PhoneDTO element) throws OrgApiDataException {
        return executeInsert(element, INSERT_QUERY);
    }

    @Override
    public PhoneDTO update(PhoneDTO element) throws OrgApiDataException {
        return executeUpdate(element, element.getPhoneId(), UPDATE_KEY_PARAM_INDEX,UPDATE_QUERY);
    }

    @Override
    public PhoneDTO delete(Long id) throws OrgApiDataException {
        return executeDelete(id, DELETE_QUERY);
    }

    @Override
    public PhoneDTO get(Long id) throws OrgApiDataException {
        return executeGet(id, GET_BY_ID_QUERY);
    }

    @Override
    public long getCount() throws OrgApiDataException {
        return executeCount(COUNT_QUERY);
    }

    @Override
    public List<PhoneDTO> getAll() throws OrgApiDataException {
        return executeGetAll(GET_ALL_QUERY);
    }

    @Override
    public List<PhoneDTO> getAll(long offset, long size) throws OrgApiDataException {
        return executeGetAllLimit(offset, size, GET_ALL_LIMIT_QUERY);
    }

    @Override
    public List<PhoneDTO> getAllByMember(Long id) throws OrgApiDataException {
        return executeGetAllByMember(id, GET_ALL_BY_MEMBER_QUERY);
    }

    @Override
    public List<PhoneDTO> getAllByMember(Long id, long offset, long size) throws OrgApiDataException {
        return executeGetAllByMemberLimit(id, offset, size, GET_ALL_BY_MEMBER_LIMIT_QUERY);
    }

    @Override
    public long getCountByMember(Long id) throws OrgApiDataException {
        return executeGetCountByMember(id, COUNT_BY_MEMBER_QUERY);
    }
}
