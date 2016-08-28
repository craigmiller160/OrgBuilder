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
        email = emailDao.insert(email);
        assertEquals("Failed to insert email", 1, email.getEmailId());
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        String newEmail = "cmiller@buonoforgovernor.com";
        testInsert();
        EmailDTO email = daoTestUtils.getEmail1();
        email.setEmailId(1);
        email.setEmailAddress(newEmail);
        emailDao.update(email);
        email = emailDao.get(1L);
        assertNotNull("EmailDTO is null", email);
        assertEquals("EmailDTO has thew wrong email address value", newEmail, email.getEmailAddress());
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        testInsert();
        EmailDTO email1 = daoTestUtils.getEmail1();
        email1.setEmailId(1);
        EmailDTO email2 = emailDao.delete(1L);
        assertEquals("Deleted email is not the same as inserted email", email1, email2);
        email2 = emailDao.get(1L);
        assertNull("Email record was not deleted", email2);
    }

    @Test
    public void testCount() throws Exception{
        insertManyEmails();
        long count = emailDao.getCount();
        assertEquals("Wrong count of emails", 15, count);
    }

    @Test
    public void testGetAll() throws Exception{
        insertManyEmails();
        List<EmailDTO> emails = emailDao.getAll();
        assertEquals("Get All returned the wrong number of emails", 15, emails.size());
    }

    @Test
    public void testGetAllLimit() throws Exception{
        insertManyEmails();
        List<EmailDTO> emails = emailDao.getAll(3, 3);
        assertEquals("Get All Limit returned the wrong number of emails", 3, emails.size());
        assertEquals("First returned email is incorrect", 4, emails.get(0).getEmailId());
        assertEquals("Second returned email is incorrect", 5, emails.get(1).getEmailId());
        assertEquals("Third returned email is incorrect", 6, emails.get(2).getEmailId());
    }

    @Test
    public void testGetAllByMember() throws Exception{
        insertManyEmails();
        List<EmailDTO> emails = emailDao.getAllByMember(1001L);
        assertEquals("Wrong number of emails returned for member with ID 1001", 5, emails.size());
    }

    @Test
    public void testGetAllByMemberLimit() throws Exception{
        insertManyEmails();
        List<EmailDTO> emails = emailDao.getAllByMember(1001L, 1, 3);
        assertEquals("Wrong number of emails returned for member with ID 1001", 3, emails.size());
        assertEquals("First returned email is incorrect", 12, emails.get(0).getEmailId());
        assertEquals("Second returned email is incorrect", 13, emails.get(1).getEmailId());
        assertEquals("Third returned email is incorrect", 14, emails.get(2).getEmailId());
    }

    @Test
    public void testGetCountByMember() throws Exception{
        insertManyEmails();
        long count = emailDao.getCountByMember(1001L);
        assertEquals("Wrong count of emails returned for member with ID 1001", 5, count);
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
