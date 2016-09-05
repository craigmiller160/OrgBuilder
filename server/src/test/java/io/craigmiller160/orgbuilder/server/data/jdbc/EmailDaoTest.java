package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
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
 * Created by craig on 8/27/16.
 */
public class EmailDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_email";
    private static final String RESET_EMAIL_AUTO_INC_SQL =
            "ALTER TABLE emails " +
            "AUTO_INCREMENT = 1;";
    private static final String RESET_MEMBER_AUTO_INC_SQL =
            "ALTER TABLE members " +
            "AUTO_INCREMENT = 1000;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private EmailDao emailDao;
    private final MemberJoinDaoTestMethods<EmailDTO,Long,EmailDao> daoTestMethods;

    public EmailDaoTest(){
        this.daoTestMethods = new MemberJoinDaoTestMethods<>(EmailDTO.class.getSimpleName());
    }

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, false);
    }

    @Before
    public void setUp() throws Exception{
        this.emailDao = daoTestUtils.prepareTestDao(EmailDao.class);
        MemberDao memberDao = daoTestUtils.prepareTestDao(MemberDao.class);
        MemberDTO member = daoTestUtils.getMember1();
        //If this fails, the issue is with the MemberDao
        memberDao.insert(member);
        member = daoTestUtils.getMember2();
        memberDao.insert(member);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(RESET_EMAIL_AUTO_INC_SQL, RESET_MEMBER_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        EmailDTO email = daoTestUtils.getEmail1();
        daoTestMethods.testInsert(email, emailDao, 1L);
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        EmailDTO email1 = daoTestUtils.getEmail1();
        EmailDTO email2 = daoTestUtils.getEmail2();
        daoTestMethods.testUpdateAndGet(email1, email2, emailDao, 1L);
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        EmailDTO email1 = daoTestUtils.getEmail1();
        daoTestMethods.testDeleteAndGet(email1, emailDao, 1L);
    }

    @Test
    public void testCount() throws Exception{
        daoTestMethods.testCount(this::insertManyEmails, emailDao, 15);
    }

    @Test
    public void testGetAll() throws Exception{
        daoTestMethods.testGetAll(this::insertManyEmails, emailDao, 15);
    }

    @Test
    public void testGetAllLimit() throws Exception{
        daoTestMethods.testGetAllLimit(this::insertManyEmails, emailDao, 3, 3, new Long[]{4L, 5L, 6L});
    }

    @Test
    public void testGetAllByMember() throws Exception{
        daoTestMethods.testGetAllByMember(this::insertManyEmails, emailDao, 1001L, 5);
    }

    @Test
    public void testGetAllByMemberLimit() throws Exception{
        daoTestMethods.testGetAllByMemberLimit(this::insertManyEmails, emailDao, 1001L, 1, 3, new Long[]{12L, 13L, 14L});
    }

    @Test
    public void testGetCountByMember() throws Exception{
        daoTestMethods.testGetCountByMember(this::insertManyEmails, emailDao, 1001, 5);
    }

    @Test
    public void testGetPreferredForMember() throws Exception{
        EmailDTO email1 = daoTestUtils.getEmail1();
        daoTestMethods.testGetPreferredForMember(email1, emailDao, 1000);
    }

    @Test
    public void testInsertOrUpdate() throws Exception{
        EmailDTO emailToUpdate = daoTestUtils.getEmail1();
        EmailDTO emailToInsert = daoTestUtils.getEmail2();
        daoTestMethods.testInsertOrUpdate(emailToUpdate, emailToInsert, emailDao, 1L, 2L);
    }

    private void insertManyEmails() throws Exception{
        //10 of email1, tied to member1
        for(int i = 0; i < 10; i++){
            EmailDTO email = daoTestUtils.getEmail1();
            emailDao.insert(email);
        }

        //5 of email2, tied to member2
        for(int i = 0; i < 5; i++){
            EmailDTO email = daoTestUtils.getEmail2();
            emailDao.insert(email);
        }
    }

}
