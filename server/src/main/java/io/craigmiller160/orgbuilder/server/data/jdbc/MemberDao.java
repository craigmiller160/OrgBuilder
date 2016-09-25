package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverter;
import io.craigmiller160.orgbuilder.server.data.jdbc.converter.DTOSQLConverterFactory;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.data.DataDTOMap;
import io.craigmiller160.orgbuilder.server.rest.MemberFilterBean;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.craigmiller160.orgbuilder.server.data.jdbc.JdbcManager.Query;

/**
 * Created by craig on 8/18/16.
 */
public class MemberDao extends AbstractJdbcDao<MemberDTO,Long> {

    private final DaoSearchUtil<MemberDTO> daoSearchUtil = new DaoSearchUtil<>();

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
            element.getAddresses().add(preferredAddress);
        }

        if(resultSet.getObject("phone_id") != null){
            PhoneDTO preferredPhone = (PhoneDTO) map.get(PhoneDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.getPhones().add(preferredPhone);
        }

        if(resultSet.getObject("email_id") != null){
            EmailDTO preferredEmail = (EmailDTO) map.get(EmailDTO.class).getDTOSQLConverter().parseResultSet(resultSet);
            element.getEmails().add(preferredEmail);
        }
        return element;
    }

    public List<MemberDTO> search(MemberFilterBean memberFilterBean) throws OrgApiDataException{
        SearchQuery.Builder builder = new SearchQuery.Builder(queries.get(Query.SEARCH_BASE));
        if(!StringUtils.isEmpty(memberFilterBean.getFirstName())){
            builder.addParameter(MemberSearchColumns.FIRST_NAME, memberFilterBean.getFirstName());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getMiddleName())){
            builder.addParameter(MemberSearchColumns.MIDDLE_NAME, memberFilterBean.getMiddleName());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getLastName())){
            builder.addParameter(MemberSearchColumns.LAST_NAME, memberFilterBean.getLastName());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getGender())){
            builder.addParameter(MemberSearchColumns.GENDER, memberFilterBean.getGender());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getAddress())){
            builder.addParameter(MemberSearchColumns.ADDRESS, memberFilterBean.getAddress());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getUnit())){
            builder.addParameter(MemberSearchColumns.UNIT, memberFilterBean.getUnit());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getCity())){
            builder.addParameter(MemberSearchColumns.CITY, memberFilterBean.getCity());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getState())){
            builder.addParameter(MemberSearchColumns.STATE, memberFilterBean.getState());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getZipCode())){
            builder.addParameter(MemberSearchColumns.ZIP_CODE, memberFilterBean.getZipCode());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getAreaCode())){
            builder.addParameter(MemberSearchColumns.AREA_CODE, memberFilterBean.getAreaCode());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getPrefix())){
            builder.addParameter(MemberSearchColumns.PREFIX, memberFilterBean.getPrefix());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getLineNumber())){
            builder.addParameter(MemberSearchColumns.LINE_NUMBER, memberFilterBean.getLineNumber());
        }

        if(!StringUtils.isEmpty(memberFilterBean.getEmailAddress())){
            builder.addParameter(MemberSearchColumns.EMAIL_ADDRESS, memberFilterBean.getEmailAddress());
        }

        builder.setOrderByClause(MemberSearchColumns.ORDER_BY_CLAUSE);

        if(memberFilterBean.getOffset() >= 0 && memberFilterBean.getSize() >= 0){
            builder.setLimit(memberFilterBean.getOffset(), memberFilterBean.getSize());
        }

        return daoSearchUtil.search(builder.build(), getElementName(), connection, this::parseResultSet);
    }
}
