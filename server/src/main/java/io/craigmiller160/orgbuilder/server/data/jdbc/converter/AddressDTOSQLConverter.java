package io.craigmiller160.orgbuilder.server.data.jdbc.converter;

import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.State;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by craig on 8/28/16.
 */
public class AddressDTOSQLConverter implements DTOSQLConverter<AddressDTO> {

    private static final int UPDATE_KEY_PARAM_INDEX = 10;

    @Override
    public void parameterizeElement(PreparedStatement stmt, AddressDTO element) throws SQLException {
        if(element.getElementId() > 0){
            stmt.setLong(1, element.getElementId());
        }
        else{
            stmt.setNull(1, Types.BIGINT);
        }

        if(element.getAddressType() != null){
            stmt.setString(2, element.getAddressType().toString());
        }
        else{
            stmt.setNull(2, Types.VARCHAR);
        }

        if(element.getAddress() != null){
            stmt.setString(3, element.getAddress());
        }
        else{
            stmt.setNull(3, Types.VARCHAR);
        }

        if(element.getUnit() != null){
            stmt.setString(4, element.getUnit());
        }
        else{
            stmt.setNull(4, Types.VARCHAR);
        }

        if(element.getCity() != null){
            stmt.setString(5, element.getCity());
        }
        else{
            stmt.setNull(5, Types.VARCHAR);
        }

        if(element.getState() != null){
            stmt.setString(6, element.getState().toString());
        }
        else{
            stmt.setNull(6, Types.CHAR);
        }

        if(element.getZipCode() != null){
            stmt.setString(7, element.getZipCode());
        }
        else{
            stmt.setNull(7, Types.CHAR);
        }

        stmt.setBoolean(8, element.isPreferred());

        if(element.getMemberId() > 0){
            stmt.setLong(9, element.getMemberId());
        }
        else{
            stmt.setNull(9, Types.BIGINT);
        }
    }

    @Override
    public AddressDTO parseResultSet(ResultSet resultSet) throws SQLException {
        AddressDTO element = new AddressDTO();
        element.setElementId(resultSet.getLong("address_id"));
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
        element.setPreferred(resultSet.getBoolean("preferred_address"));
        element.setMemberId(resultSet.getLong("address_member_id"));

        return element;
    }

    @Override
    public int getUpdateKeyParamIndex() {
        return UPDATE_KEY_PARAM_INDEX;
    }
}
