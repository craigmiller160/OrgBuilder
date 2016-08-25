package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private MemberDao memberDao;

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME);
    }

    @Before
    public void setUp() throws Exception{
        this.memberDao = daoTestUtils.prepareTestDao(MemberDao.class);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(REST_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        MemberDTO memberDTO = daoTestUtils.getMember1();
        memberDTO = memberDao.insert(memberDTO);
        assertEquals("Failed to insert MemberDTO", 1000, memberDTO.getMemberId());
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        testInsert();
        MemberDTO memberDTO = daoTestUtils.getMember1();
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
        MemberDTO member1 = daoTestUtils.getMember1();
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
            MemberDTO member = daoTestUtils.getMember1();
            memberDao.insert(member);
        }

        List<MemberDTO> members = memberDao.getAll();
        assertEquals("Get All returned the wrong number of members", 10, members.size());
    }

    @Test
    public void testGetAllLimit() throws Exception{
        for(int i = 0; i < 10; i++){
            MemberDTO member = daoTestUtils.getMember1();
            memberDao.insert(member);
        }

        List<MemberDTO> members = memberDao.getAll(3, 3);
        assertEquals("Get All Limit returned the wrong number of members", 3, members.size());
        assertEquals("First returned member is incorrect", 1003L, members.get(0).getMemberId());
        assertEquals("Second returned member is incorrect", 1004L, members.get(1).getMemberId());
        assertEquals("Third returned member is incorrect", 1005L, members.get(2).getMemberId());
    }



}
