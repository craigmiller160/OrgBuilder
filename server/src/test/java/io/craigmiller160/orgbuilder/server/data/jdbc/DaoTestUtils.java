package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerTestUtils;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.State;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * An abstract superclass for all DAO testing logic,
 * except for the OrgDao, due to its unique schema
 * setup.
 *
 * Created by craigmiller on 8/24/16.
 */
public class DaoTestUtils {

    private static OrgDataSource dataSource;
    private Connection connection;
    private String testSchemaName;

    public void initializeTestClass(String testSchemaName) throws Exception{
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);
        this.testSchemaName = testSchemaName;

        dataSource = ServerTestUtils.getOrgDataSource(ServerCore.getOrgDataManager());
        ServerCore.getOrgDataManager().createNewSchema(testSchemaName);
    }

    public <T extends Dao> T prepareTestDao(Class<T> daoClazz) throws Exception{
        if(connection == null){
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            try(Statement stmt = connection.createStatement()){
                stmt.executeUpdate("use " + testSchemaName);
            }
        }

        Constructor<T> constructor = daoClazz.getConstructor(Connection.class);
        return constructor.newInstance(connection);
    }

    public void cleanUpTest(String... resetAutoIncSql) throws Exception{
        connection.rollback();
        for(String sql : resetAutoIncSql){
            try(Statement stmt = connection.createStatement()){
                stmt.executeUpdate(sql);
                connection.commit();
            }
        }

        connection.commit();
        connection.close();
        connection = null;
    }

    public void tearDownTestClass(String testSchemaName) throws Exception{
        ServerCore.getOrgDataManager().deleteSchema(testSchemaName);
    }

    public AddressDTO getAddress1(){
        AddressDTO address = new AddressDTO();
        address.setAddressType(AddressDTO.AddressType.HOME);
        address.setAddress("3 Brookside Ct");
        address.setUnit("6");
        address.setCity("East Brunswick");
        address.setState(State.NJ);
        address.setZipCode("08816");
        address.setMemberId(1000);
        return address;
    }

    public AddressDTO getAddress2(){
        AddressDTO address = new AddressDTO();
        address.setAddressType(AddressDTO.AddressType.WORK);
        address.setAddress("10306 Casa Palarmo Dr");
        address.setUnit("6");
        address.setCity("Riverview");
        address.setState(State.FL);
        address.setZipCode("33578");
        address.setMemberId(1001);
        return address;
    }

    public MemberDTO getMember1(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setFirstName("Craig");
        memberDTO.setMiddleName("Evan");
        memberDTO.setLastName("Miller");
        memberDTO.setGender(Gender.MALE);
        memberDTO.setDateOfBirth(LocalDate.of(1988, 10, 26));
        return memberDTO;
    }

    public MemberDTO getMember2(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setFirstName("Gary");
        memberDTO.setMiddleName("Brian");
        memberDTO.setLastName("Miller");
        memberDTO.setGender(Gender.MALE);
        memberDTO.setDateOfBirth(LocalDate.of(1959, 4, 29));

        return memberDTO;
    }

}
