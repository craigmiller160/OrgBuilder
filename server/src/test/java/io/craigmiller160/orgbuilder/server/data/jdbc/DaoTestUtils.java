package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerTestUtils;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.State;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import throwing.stream.ThrowingStream;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

/**
 * An abstract superclass for all DAO testing logic,
 * except for the OrgDao, due to its unique schema
 * setup.
 *
 * Created by craigmiller on 8/24/16.
 */
public class DaoTestUtils {

    private OrgDataSource dataSource;
    private JdbcManager jdbcManager;
    private Connection connection;
    private String testSchemaName;
    private ServerCore serverCore;

    public void initializeTestClass(String testSchemaName, boolean isAppSchema) throws Exception{
        serverCore = new ServerCore();
        serverCore.contextInitialized(null);
        this.testSchemaName = testSchemaName;

        dataSource = ServerTestUtils.getOrgDataSource(ServerCore.getOrgDataManager());
        jdbcManager = ServerTestUtils.getJdbcManager(ServerCore.getOrgDataManager());
        if(isAppSchema){
            ServerCore.getOrgDataManager().createAppSchema(testSchemaName);
        }
        else{
            ServerCore.getOrgDataManager().createOrgSchema(testSchemaName);
        }

    }

    public <T extends Dao> T prepareTestDao(Class<T> daoClazz) throws Exception{
        if(connection == null){
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            try(Statement stmt = connection.createStatement()){
                stmt.executeUpdate("use " + testSchemaName);
            }
        }

        Constructor<T> constructor = daoClazz.getConstructor(Connection.class, Map.class);
        return constructor.newInstance(connection, jdbcManager.getMappedQueries().get(daoClazz));
    }

    public void cleanUpTest(String... resetAutoIncSql) throws Exception{
        connection.rollback();
        ThrowingStream.of(Arrays.stream(resetAutoIncSql), SQLException.class)
                .forEach(this::executeCleanupSQL);

        connection.commit();
        connection.close();
        connection = null;
    }

    private void executeCleanupSQL(String sql) throws SQLException{
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate(sql);
            connection.commit();
        }
    }

    public void tearDownTestClass(String testSchemaName) throws Exception{
        ServerCore.getOrgDataManager().deleteSchema(testSchemaName, true);
        serverCore.contextDestroyed(null);
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

    public PhoneDTO getPhone1(){
        PhoneDTO phone = new PhoneDTO();
        phone.setPhoneType(PhoneDTO.PhoneType.HOME);
        phone.setAreaCode("732");
        phone.setPrefix("613");
        phone.setLineNumber("7826");
        phone.setMemberId(1000);
        return phone;
    }

    public PhoneDTO getPhone2(){
        PhoneDTO phone = new PhoneDTO();
        phone.setPhoneType(PhoneDTO.PhoneType.MOBILE);
        phone.setAreaCode("908");
        phone.setPrefix("616");
        phone.setLineNumber("2695");
        phone.setMemberId(1001);
        return phone;
    }

    public EmailDTO getEmail1(){
        EmailDTO email = new EmailDTO();
        email.setEmailType(EmailDTO.EmailType.PERSONAL);
        email.setEmailAddress("craigmiller160@gmail.com");
        email.setMemberId(1000);
        return email;
    }

    public EmailDTO getEmail2(){
        EmailDTO email = new EmailDTO();
        email.setEmailType(EmailDTO.EmailType.WORK);
        email.setEmailAddress("cmiller@pilotfishtechnology.com");
        email.setMemberId(1001);
        return email;
    }

    public OrgDTO getOrg1(){
        OrgDTO org = new OrgDTO();
        org.setOrgName("MyOrg");
        return org;
    }

    public OrgDTO getOrg2(){
        OrgDTO org = new OrgDTO();
        org.setOrgName("NewOrg");
        return org;
    }

    public UserDTO getUser1(){
        UserDTO user = new UserDTO();
        user.setPassword("MyPassword");
        user.setUserEmail("MyEmail@gmail.com");
        user.convertStringToRoles("ADMIN,WRITE,READ");
        user.setOrgId(1);
        user.setOrgName("MyOrg");

        return user;
    }

    public UserDTO getUser2(){
        UserDTO user = new UserDTO();
        user.setPassword("NewPassword");
        user.setUserEmail("NewEmail@gmail.com");
        user.convertStringToRoles("READ");
        user.setOrgId(2);
        user.setOrgName("NewOrg");

        return user;
    }

    public RefreshTokenDTO getToken1(){
        RefreshTokenDTO token = new RefreshTokenDTO();
        token.setUserId(1L);
        token.setOrgId(1L);
        token.setTokenHash("ThisIsTheHash");
        token.setExpiration(LocalDateTime.now());
        return token;
    }

    public RefreshTokenDTO getToken2(){
        RefreshTokenDTO token = new RefreshTokenDTO();
        token.setUserId(1L);
        token.setOrgId(1L);
        token.setTokenHash("MyNewHash");
        token.setExpiration(LocalDateTime.now());
        return token;
    }

}
