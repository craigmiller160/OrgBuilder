package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.data.DataDTOMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/18/16.
 */
public class MemberDao extends AbstractJdbcDao<MemberDTO,Long> {

    public MemberDao(Connection connection, Map<Query,String> queries) {
        super(connection, queries);
    }

    @Override
    protected DTOSQLConverter<MemberDTO> getDTOSQLConverter() {
        return DTOSQLConverterFactory.newInstance().getDTOSQLConverter(MemberDTO.class);
    }

    @Override
    protected String getElementName() {
        return MemberDTO.class.getSimpleName();
    }

    @Override
    protected MemberDTO parseResultSetAdditional(MemberDTO element, ResultSet resultSet) throws SQLException{
        Map<Class,DataDTOMap> map = ServerCore.getDataDTOMap();

        if(resultSet.getObject("address_id") != null){
            AddressDTO preferredAddress = (AddressDTO) map.get(AddressDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.setPreferredAddress(preferredAddress);
        }

        if(resultSet.getObject("phone_id") != null){
            PhoneDTO preferredPhone = (PhoneDTO) map.get(PhoneDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.setPreferredPhone(preferredPhone);
        }

        if(resultSet.getObject("email_id") != null){
            EmailDTO preferredEmail = (EmailDTO) map.get(EmailDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.setPreferredEmail(preferredEmail);
        }
        return element;
    }
}
