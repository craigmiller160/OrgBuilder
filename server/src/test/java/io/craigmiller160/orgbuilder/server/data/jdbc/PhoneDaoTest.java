package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
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
    private final MemberJoinDaoTestMethods<PhoneDTO,Long,PhoneDao> daoTestMethods;

    public PhoneDaoTest(){
        this.daoTestMethods = new MemberJoinDaoTestMethods<>(PhoneDTO.class.getSimpleName());
    }

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
        daoTestMethods.testInsert(phone, phoneDao, 1L);
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        PhoneDTO phone1 = daoTestUtils.getPhone1();
        PhoneDTO phone2 = daoTestUtils.getPhone2();
        daoTestMethods.testUpdateAndGet(phone1, phone2, phoneDao, 1L);
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        PhoneDTO phone1 = daoTestUtils.getPhone1();
        daoTestMethods.testDeleteAndGet(phone1, phoneDao, 1L);
    }

    @Test
    public void testCount() throws Exception{
        daoTestMethods.testCount(this::insertManyPhones, phoneDao, 15);
    }

    @Test
    public void testGetAll() throws Exception{
        daoTestMethods.testGetAll(this::insertManyPhones, phoneDao, 15);
    }

    @Test
    public void testGetAllLimit() throws Exception{
        daoTestMethods.testGetAllLimit(this::insertManyPhones, phoneDao, 3, 3, new Long[]{4L, 5L, 6L});
    }

    @Test
    public void testGetAllByMember() throws Exception{
        daoTestMethods.testGetAllByMember(this::insertManyPhones, phoneDao, 1001L, 5);
    }

    @Test
    public void testGetAllByMemberLimit() throws Exception{
        daoTestMethods.testGetAllByMemberLimit(this::insertManyPhones, phoneDao, 1001L, 1, 3, new Long[]{12L, 13L, 14L});
    }

    @Test
    public void testGetCountByMember() throws Exception{
        daoTestMethods.testGetCountByMember(this::insertManyPhones, phoneDao, 1001, 5);
    }

    @Test
    public void testGetPreferredForMember() throws Exception{
        PhoneDTO phone1 = daoTestUtils.getPhone1();
        daoTestMethods.testGetPreferredForMember(phone1, phoneDao, 1000);
    }

    @Test
    public void testInsertOrUpdate() throws Exception{
        PhoneDTO phoneToUpdate = daoTestUtils.getPhone1();
        PhoneDTO phoneToInsert = daoTestUtils.getPhone2();
        daoTestMethods.testInsertOrUpdate(phoneToUpdate, phoneToInsert, phoneDao, 1L, 2L);
    }

    @Test
    public void testDeleteByMember() throws Exception{
        PhoneDTO phone = daoTestUtils.getPhone1();
        daoTestMethods.testDeleteByMember(phone, phoneDao, 1000, 1);
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
