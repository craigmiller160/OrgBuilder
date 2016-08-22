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
            "insert into addresses (address_type, address, unit, city, state, zip_code, member_id) " +
            "values (?,?,?,?,?,?,?);";

    private static final String UPDATE_QUERY =
            "UPDATE addresses " +
            "SET addressType = ?, address = ?, unit = ?, city = ?, state = ?, zip_code = ?, member_id = ? " +
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

    private void parameterizeAddress(PreparedStatement stmt, AddressDTO element) throws SQLException{
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

    private AddressDTO parseResult(ResultSet resultSet) throws SQLException{
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
        OrgApiLogger.getDataLogger().trace("Address Insert Query:\n" + INSERT_QUERY);
        try{
            try(PreparedStatement stmt = getConnection().prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)){
                parameterizeAddress(stmt, element);
                stmt.executeUpdate();
                try(ResultSet resultSet = stmt.getGeneratedKeys()){
                    if(!resultSet.next()){
                        throw new SQLException("Unable to retrieve ID from inserted Address. Address: " + element.toString());
                    }
                    element.setAddressId(resultSet.getLong(1));
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to insert Address. Address: " + element.toString(), ex);
        }

        return element;
    }

    @Override
    public AddressDTO update(AddressDTO element) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Update Query:\n" + UPDATE_QUERY);
        try(PreparedStatement stmt = getConnection().prepareStatement(UPDATE_QUERY)){
            parameterizeAddress(stmt, element);
            stmt.setLong(UPDATE_KEY_PARAM_INDEX, element.getAddressId());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to update Address. Address: " + element.toString(), ex);
        }

        return element;
    }

    @Override
    public AddressDTO delete(Long id) throws OrgApiDataException {
        AddressDTO element = null;
        try{
            element = get(id);

            try(PreparedStatement stmt = getConnection().prepareStatement(DELETE_QUERY)){
                OrgApiLogger.getDataLogger().trace("Address Delete Query:\n" + DELETE_QUERY);
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to delete Address. ID: " + id, ex);
        }

        return element;
    }

    @Override
    public AddressDTO get(Long id) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Get By ID Query:\n" + GET_BY_ID_QUERY);
        AddressDTO element = null;
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_BY_ID_QUERY)){
            stmt.setLong(1, id);
            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    element = parseResult(resultSet);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve address by ID. ID: " + id, ex);
        }

        return element;
    }

    @Override
    public long getCount() throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Count Query:\n" + COUNT_QUERY);
        long count = -1;
        try(Statement stmt = getConnection().createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(COUNT_QUERY)){
                if(resultSet.next()){
                    count = resultSet.getLong("address_count");
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve count from address table", ex);
        }

        return count;
    }

    @Override
    public List<AddressDTO> getAll() throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Get All Query:\n" + GET_ALL_QUERY);
        List<AddressDTO> elements = new ArrayList<>();
        try(Statement stmt = getConnection().createStatement()){
            try(ResultSet resultSet = stmt.executeQuery(GET_ALL_QUERY)){
                while(resultSet.next()){
                    AddressDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve all addresses", ex);
        }

        return elements;
    }

    @Override
    public List<AddressDTO> getAll(long offset, long size) throws OrgApiDataException {
        OrgApiLogger.getDataLogger().trace("Address Get All Limit Query:\n" + GET_ALL_LIMIT_QUERY);
        List<AddressDTO> elements = new ArrayList<>();
        try(PreparedStatement stmt = getConnection().prepareStatement(GET_ALL_LIMIT_QUERY)){
            stmt.setLong(1, offset);
            stmt.setLong(2, size);
            try(ResultSet resultSet = stmt.executeQuery()){
                while(resultSet.next()){
                    AddressDTO element = parseResult(resultSet);
                    elements.add(element);
                }
            }
        }
        catch(SQLException ex){
            throw new OrgApiDataException("Unable to retrieve addresses within range. Offset: " + offset + " Size: " + size, ex);
        }

        return elements;
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
