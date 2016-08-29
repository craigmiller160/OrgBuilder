package io.craigmiller160.orgbuilder.server.data.mysql;

import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
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
public class PhoneDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_phone";
    private static final String RESET_PHONE_AUTO_INC_SQL =
            "ALTER TABLE phones " +
                    "AUTO_INCREMENT = 1;";
    private static final String RESET_MEMBER_AUTO_INC_SQL =
            "ALTER TABLE members " +
                    "AUTO_INCREMENT = 1000;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private PhoneDao phoneDao;

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, false);
    }

    @Before
    public void setUp() throws Exception{
        this.phoneDao = daoTestUtils.prepareTestDao(PhoneDao.class);
        MemberDao memberDao = daoTestUtils.prepareTestDao(MemberDao.class);
        MemberDTO member = daoTestUtils.getMember1();
        //If this fails, the issue is with the MemberDao
        memberDao.insert(member);
        member = daoTestUtils.getMember2();
        memberDao.insert(member);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(RESET_PHONE_AUTO_INC_SQL, RESET_MEMBER_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        PhoneDTO phone = daoTestUtils.getPhone1();
        phone = phoneDao.insert(phone);
        assertEquals("Failed to insert phone", 1, phone.getPhoneId());
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        String newLineNum = "8024";
        testInsert();
        PhoneDTO phone = daoTestUtils.getPhone1();
        phone.setPhoneId(1);
        phone.setLineNumber(newLineNum);
        phoneDao.update(phone, phone.getPhoneId());
        phone = phoneDao.get(1L);
        assertNotNull("PhoneDTO is null", phone);
        assertEquals("PhoneDTO has the wrong line number value", newLineNum, phone.getLineNumber());
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        testInsert();
        PhoneDTO phone1 = daoTestUtils.getPhone1();
        phone1.setPhoneId(1);
        PhoneDTO phone2 = phoneDao.delete(1L);
        assertEquals("Deleted phone is not the same as inserted phone", phone1, phone2);
        phone2 = phoneDao.get(1L);
        assertNull("Phone record was not deleted", phone2);
    }

    @Test
    public void testCount() throws Exception{
        insertManyPhones();
        long count = phoneDao.getCount();
        assertEquals("Incorrect count of phones", 15, count);
    }

    @Test
    public void testGetAll() throws Exception{
        insertManyPhones();
        List<PhoneDTO> phones = phoneDao.getAll();
        assertEquals("Get All returned the wrong number of phones", 15, phones.size());
    }

    @Test
    public void testGetAllLimit() throws Exception{
        insertManyPhones();
        List<PhoneDTO> phones = phoneDao.getAll(3, 3);
        assertEquals("Get All Limit returned the wrong number of phones", 3, phones.size());
        assertEquals("First returned phone is incorrect", 4, phones.get(0).getPhoneId());
        assertEquals("Second returned phone is incorrect", 5, phones.get(1).getPhoneId());
        assertEquals("Third returned phone is incorrect", 6, phones.get(2).getPhoneId());
    }

    @Test
    public void testGetAllByMember() throws Exception{
        insertManyPhones();
        List<PhoneDTO> phones = phoneDao.getAllByMember(1001L);
        assertEquals("Wrong number of phones returned for member with ID 1001", 5, phones.size());
    }

    @Test
    public void testGetAllByMemberLimit() throws Exception{
        insertManyPhones();
        List<PhoneDTO> phones = phoneDao.getAllByMember(1001L, 1, 3);
        assertEquals("Wrong number of phones returned for member with ID 1001", 3, phones.size());
        assertEquals("First returned phone is incorrect", 12, phones.get(0).getPhoneId());
        assertEquals("Second returned phone is incorrect", 13, phones.get(1).getPhoneId());
        assertEquals("Third returned phone is incorrect", 14, phones.get(2).getPhoneId());
    }

    @Test
    public void testGetCountByMember() throws Exception{
        insertManyPhones();
        long count = phoneDao.getCountByMember(1001L);
        assertEquals("Wrong count of phones returned for member with ID 1001", 5, count);
    }

    private void insertManyPhones() throws Exception{
        //10 of phone1, tied to member1
        for(int i = 0; i < 10; i++){
            PhoneDTO phone = daoTestUtils.getPhone1();
            phoneDao.insert(phone);
        }

        //5 of phone2, tied to member2
        for(int i = 0; i < 5; i++){
            PhoneDTO phone = daoTestUtils.getPhone2();
            phoneDao.insert(phone);
        }
    }

}
