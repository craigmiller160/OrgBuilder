package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
public class EmailDao extends AbstractJdbcMemberJoinDao<EmailDTO,Long,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 4;

    private static final String INSERT_QUERY =
            "INSERT INTO emails (email_type, email_address, member_id) " +
            "VALUES (?,?,?);";

    private static final String UPDATE_QUERY =
            "UPDATE emails " +
            "SET email_type = ?, email_address = ?, member_id = ? " +
            "WHERE email_id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM emails " +
            "WHERE email_id = ?;";

    private static final String GET_BY_ID_QUERY =
            "SELECT * " +
            "FROM emails " +
            "WHERE email_id = ?;";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) " +
            "FROM emails;";

    private static final String GET_ALL_QUERY =
            "SELECT * " +
            "FROM emails " +
            "ORDER BY email_id ASC;";

    private static final String GET_ALL_LIMIT_QUERY =
            "SELECT * " +
            "FROM emails " +
            "ORDER BY email_id ASC " +
            "LIMIT ?,?;";

    private static final String GET_ALL_BY_MEMBER_QUERY =
            "SELECT * " +
            "FROM emails " +
            "WHERE member_id = ? " +
            "ORDER BY email_id ASC;";

    private static final String GET_ALL_BY_MEMBER_LIMIT_QUERY =
            "SELECT * " +
            "FROM emails " +
            "WHERE member_id = ? " +
            "ORDER BY email_id ASC " +
            "LIMIT ?,?;";

    private static final String COUNT_BY_MEMBER_QUERY =
            "SELECT COUNT(*) " +
            "FROM emails " +
            "WHERE member_id = ?;";

    public EmailDao(Connection connection) {
        super(connection);
    }

    @Override
    protected void parameterizeElement(PreparedStatement stmt, EmailDTO element) throws SQLException {
        if(element.getEmailType() != null){
            stmt.setString(1, element.getEmailType().toString());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }

        if(element.getEmailAddress() != null){
            stmt.setString(2, element.getEmailAddress());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        if(element.getMemberId() > 0){
            stmt.setLong(3, element.getMemberId());
        }
        else{
            stmt.setNull(3, Types.BIGINT);
        }
    }

    @Override
    protected EmailDTO parseResult(ResultSet resultSet) throws SQLException {
        EmailDTO element = new EmailDTO();
        element.setEmailId(resultSet.getLong("email_id"));
        String emailType = resultSet.getString("email_type");
        if(!StringUtils.isEmpty(emailType)){
            element.setEmailType(EmailDTO.EmailType.valueOf(emailType));
        }

        element.setEmailAddress(resultSet.getString("email_address"));
        element.setMemberId(resultSet.getLong("member_id"));

        return element;
    }

    @Override
    protected String getElementName() {
        return EmailDTO.class.getSimpleName();
    }

    @Override
    public EmailDTO insert(EmailDTO element) throws OrgApiDataException {
        return executeInsert(element, INSERT_QUERY);
    }

    @Override
    public EmailDTO update(EmailDTO element) throws OrgApiDataException {
        return executeUpdate(element, element.getEmailId(), UPDATE_KEY_PARAM_INDEX, UPDATE_QUERY);
    }

    @Override
    public EmailDTO delete(Long id) throws OrgApiDataException {
        return executeDelete(id, DELETE_QUERY);
    }

    @Override
    public EmailDTO get(Long id) throws OrgApiDataException {
        return executeGet(id, GET_BY_ID_QUERY);
    }

    @Override
    public long getCount() throws OrgApiDataException {
        return executeCount(COUNT_QUERY);
    }

    @Override
    public List<EmailDTO> getAll() throws OrgApiDataException {
        return executeGetAll(GET_ALL_QUERY);
    }

    @Override
    public List<EmailDTO> getAll(long offset, long size) throws OrgApiDataException {
        return executeGetAllLimit(offset, size, GET_ALL_LIMIT_QUERY);
    }

    @Override
    public List<EmailDTO> getAllByMember(Long id) throws OrgApiDataException {
        return executeGetAllByMember(id, GET_ALL_BY_MEMBER_QUERY);
    }

    @Override
    public List<EmailDTO> getAllByMember(Long id, long offset, long size) throws OrgApiDataException {
        return executeGetAllByMemberLimit(id, offset, size, GET_ALL_BY_MEMBER_LIMIT_QUERY);
    }

    @Override
    public long getCountByMember(Long id) throws OrgApiDataException {
        return executeGetCountByMember(id, COUNT_BY_MEMBER_QUERY);
    }
}
