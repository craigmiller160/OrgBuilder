package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.MemberJoins;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.State;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 8/21/16.
 */
public class AddressDao extends AbstractJdbcDao<AddressDTO,Long> implements MemberJoins<AddressDTO,Long> {

    private static final int UPDATE_KEY_PARAM_INDEX = 8;

    private static final String INSERT_QUERY =
            "INSERT INTO addresses (address_type, address, unit, city, state, zip_code, member_id) " +
            "VALUES (?,?,?,?,?,?,?);";

    private static final String UPDATE_QUERY =
            "UPDATE addresses " +
            "SET address_type = ?, address = ?, unit = ?, city = ?, state = ?, zip_code = ?, member_id = ? " +
            "WHERE address_id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM addresses " +
            "WHERE address_id = ?;";

    private static final String GET_BY_ID_QUERY =
            "SELECT * " +
            "FROM addresses " +
            "WHERE address_id = ?;";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) AS address_count " +
            "FROM addresses;";

    private static final String GET_ALL_QUERY =
            "SELECT * " +
            "FROM addresses " +
            "ORDER BY address_id ASC;";

    private static final String GET_ALL_LIMIT_QUERY =
            "SELECT * " +
            "FROM addresses " +
            "ORDER BY address_id ASC " +
            "LIMIT ?,?;";

    private static final String GET_ALL_BY_MEMBER_QUERY =
            "SELECT * " +
            "FROM addresses " +
            "WHERE member_id = ? " +
            "ORDER BY address_id ASC;";

    private static final String GET_ALL_BY_MEMBER_LIMIT_QUERY =
            "SELECT * " +
            "FROM addresses " +
            "WHERE member_id = ? " +
            "ORDER BY address_id ASC " +
            "LIMIT ?,?;";

    private static final String COUNT_BY_MEMBER_QUERY =
            "SELECT COUNT(*) AS member_count " +
            "FROM addresses " +
            "WHERE member_id = ?;";

    public AddressDao(Connection connection) {
        super(connection);
    }

    @Override
    protected void parameterizeElement(PreparedStatement stmt, AddressDTO element) throws SQLException{
        if(element.getAddressType() != null){
            stmt.setString(1, element.getAddressType().toString());
        }
        else{
            stmt.setNull(1, Types.VARCHAR);
        }

        if(element.getAddress() != null){
            stmt.setString(2, element.getAddress());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        if(element.getUnit() != null){
            stmt.setString(3, element.getAddress());
        }
        else{
            stmt.setNull(3, Types.VARCHAR);
        }

        if(element.getCity() != null){
            stmt.setString(4, element.getCity());
        }
        else{
            stmt.setNull(4, Types.VARCHAR);
        }

        if(element.getState() != null){
            stmt.setString(5, element.getState().toString());
        }
        else{
            stmt.setNull(5, Types.CHAR);
        }

        if(element.getZipCode() != null){
            stmt.setString(6, element.getZipCode());
        }
        else{
            stmt.setNull(6, Types.CHAR);
        }

        if(element.getMemberId() > 0){
            stmt.setLong(7, element.getMemberId());
        }
        else{
            stmt.setNull(7, Types.BIGINT);
        }
    }

    @Override
    protected AddressDTO parseResult(ResultSet resultSet) throws SQLException{
        AddressDTO element = new AddressDTO();
        String addressType = resultSet.getString("address_type");
        if(!StringUtils.isEmpty(addressType)){
            element.setAddressType(AddressDTO.AddressType.valueOf(addressType));
        }
        element.setAddress(resultSet.getString("address"));
        element.setUnit(resultSet.getString("unit"));
        element.setCity(resultSet.getString("city"));
        String state = resultSet.getString("state");
        if(!StringUtils.isEmpty(state)){
            element.setState(State.valueOf(state));
        }
        element.setZipCode(resultSet.getString("zip_code"));
        element.setMemberId(resultSet.getLong("member_id"));

        return element;
    }

    @Override
    public AddressDTO insert(AddressDTO element) throws OrgApiDataException {
        return executeInsert(element, INSERT_QUERY);
    }

    @Override
    public AddressDTO update(AddressDTO element) throws OrgApiDataException {
        return executeUpdate(element, element.getAddressId(), UPDATE_KEY_PARAM_INDEX, UPDATE_QUERY);
    }

    @Override
    public AddressDTO delete(Long id) throws OrgApiDataException {
        return executeDelete(id, DELETE_QUERY);
    }

    @Override
    public AddressDTO get(Long id) throws OrgApiDataException {
        return executeGet(id, AddressDTO.class.getSimpleName(), GET_BY_ID_QUERY);
    }

    @Override
    public long getCount() throws OrgApiDataException {
        return executeCount(AddressDTO.class.getSimpleName(), COUNT_QUERY);
    }

    @Override
    public List<AddressDTO> getAll() throws OrgApiDataException {
        return executeGetAll(AddressDTO.class.getSimpleName(), GET_ALL_QUERY);
    }

    @Override
    public List<AddressDTO> getAll(long offset, long size) throws OrgApiDataException {
        return executeGetAllLimit(AddressDTO.class.getSimpleName(), offset, size, GET_ALL_LIMIT_QUERY);
    }

    @Override
    public List<AddressDTO> getAllByMember(Long id) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Get All By Member Query:\n" + GET_ALL_BY_MEMBER_QUERY);
        List<AddressDTO> elements = new ArrayList<>();
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_ALL_BY_MEMBER_QUERY)){
            stmt.setLong(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    AddressDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get all addresses by member id. Member ID: " + id, ex);
        }

        return elements;
    }

    @Override
    public List<AddressDTO> getAllByMember(Long id, long offset, long size) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Get All By Member Limit Query:\n" + GET_ALL_BY_MEMBER_LIMIT_QUERY);
        List<AddressDTO> elements = new ArrayList<>();
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_ALL_BY_MEMBER_LIMIT_QUERY)){
            stmt.setLong(1, id);
            stmt.setLong(2, offset);
            stmt.setLong(3, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    AddressDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get all address by member id, with limit. Member ID: " + id + " Offset: " + offset + " Size: " + size, ex);
        }

        return elements;
    }

    @Override
    public long getCountByMember(Long id) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Count By Member Query:\n" + GET_ALL_BY_MEMBER_LIMIT_QUERY);
        long count = -1;
        try(PreparedStatement stmt = getConnection().prepareStatement(COUNT_BY_MEMBER_QUERY)){
            stmt.setLong(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    count = resultSet.getLong("address_count");
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to get count of addresses by member id. Member ID: " + id, ex);
        }

        return count;
    }
}
