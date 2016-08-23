package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerTestUtils;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import org.junit.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by craig on 8/21/16.
 */
public class MemberDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_member";
    private static final String REST_AUTO_INC_SQL =
            "alter table members " +
            "auto_increment = 1000;";

    private static OrgDataSource dataSource;
    private MemberDao memberDao;
    private Connection connection;

    @BeforeClass
    public static void init() throws Exception{
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);

        dataSource = ServerTestUtils.getOrgDataSource(ServerCore.getOrgDataManager());
        ServerCore.getOrgDataManager().createNewSchema(TEST_SCHEMA_NAME);


    }

    @Before
    public void setUp() throws Exception{
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        memberDao = new MemberDao(connection);
    }

    @After
    public void cleanUp() throws Exception{
        connection.rollback();
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate(REST_AUTO_INC_SQL);
        }
        connection.commit();
        connection.close();
    }

    @AfterClass
    public static void tearDown() throws Exception{
        ServerCore.getOrgDataManager().deleteSchema(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        MemberDTO memberDTO = getMember1();
        memberDTO = memberDao.insert(memberDTO);
        assertEquals("Failed to insert MemberDTO", 1000, memberDTO.getMemberId());
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        testInsert();
        MemberDTO memberDTO = getMember1();
        memberDTO.setFirstName("John");
        memberDTO.setMemberId(1000L);
        memberDao.update(memberDTO);
        MemberDTO memberDTO2 = memberDao.get(1000L);
        assertNotNull("MemberDTO is null", memberDTO2);
        assertEquals("MemberDTO has the wrong first name", "John", memberDTO2.getFirstName());
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        testInsert();
        MemberDTO member1 = getMember1();
        member1.setMemberId(1000L);
        MemberDTO member2 = memberDao.delete(1000L);
        assertEquals("Deleted member is not the same as inserted member", member1, member2);
        member2 = memberDao.get(1000L);
        assertNull("Member record was not deleted", member2);
    }

    @Test
    public void testCount() throws Exception{
        testInsert();
        long count = memberDao.getCount();
        assertEquals("Incorrect count of members in database", 1, count);
    }

    @Test
    public void testGetAll() throws Exception{
        for(int i = 0; i < 10; i++){
            MemberDTO member = getMember1();
            memberDao.insert(member);
        }

        List<MemberDTO> members = memberDao.getAll();
        assertEquals("Get All returned the wrong number of members", 10, members.size());
    }

    @Test
    public void testGetAllLimit() throws Exception{
        for(int i = 0; i < 10; i++){
            MemberDTO member = getMember1();
            memberDao.insert(member);
        }

        List<MemberDTO> members = memberDao.getAll(3, 3);
        assertEquals("Get All Limit returned the wrong number of members", 3, members.size());
        assertEquals("First returned member is incorrect", 1003L, members.get(0).getMemberId());
        assertEquals("Second returned member is incorrect", 1004L, members.get(1).getMemberId());
        assertEquals("Third returned member is incorrect", 1005L, members.get(2).getMemberId());
    }

    private MemberDTO getMember1(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setFirstName("Craig");
        memberDTO.setMiddleName("Evan");
        memberDTO.setLastName("Miller");
        memberDTO.setGender(Gender.MALE);
        memberDTO.setDateOfBirth(LocalDate.of(1988, 10, 26));
        return memberDTO;
    }

}
