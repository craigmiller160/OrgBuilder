package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by craig on 8/21/16.
 */
public class MemberDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_member";
    private static final String RESET_AUTO_INC_SQL =
            "alter table members " +
            "auto_increment = 1000;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private MemberDao memberDao;
    private final DaoTestMethods<MemberDTO,Long,MemberDao> daoTestMethods;

    public MemberDaoTest(){
        this.daoTestMethods = new DaoTestMethods<>(MemberDTO.class.getSimpleName());
    }

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, false);
    }

    @Before
    public void setUp() throws Exception{
        this.memberDao = daoTestUtils.prepareTestDao(MemberDao.class);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(RESET_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        MemberDTO memberDTO = daoTestUtils.getMember1();
        daoTestMethods.testInsert(memberDTO, memberDao, 1000L);
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        MemberDTO member1 = daoTestUtils.getMember1();
        MemberDTO member2 = daoTestUtils.getMember2();
        daoTestMethods.testUpdateAndGet(member1, member2, memberDao, 1000L);
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        MemberDTO member1 = daoTestUtils.getMember1();
        daoTestMethods.testDeleteAndGet(member1, memberDao, 1000L);
    }

    @Test
    public void testCount() throws Exception{
        daoTestMethods.testCount(this::insertManyMembers, memberDao, 10);
    }

    @Test
    public void testGetAll() throws Exception{
        daoTestMethods.testGetAll(this::insertManyMembers, memberDao, 10);
    }

    @Test
    public void testGetAllLimit() throws Exception{
        daoTestMethods.testGetAllLimit(this::insertManyMembers, memberDao, 3, 3, new Long[]{1003L, 1004L, 1005L});
    }

    @Test
    public void testInsertOrUpdate() throws Exception{
        MemberDTO memberToUpdate = daoTestUtils.getMember1();
        MemberDTO memberToInsert = daoTestUtils.getMember2();
        daoTestMethods.testInsertOrUpdate(memberToUpdate, memberToInsert, memberDao, 1000L, 1001L);
    }

    private void insertManyMembers() throws Exception{
        for(int i = 0; i < 10; i++){
            MemberDTO member = daoTestUtils.getMember1();
            memberDao.insert(member);
        }
    }

}
