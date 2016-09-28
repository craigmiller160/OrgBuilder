package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by craig on 9/4/16.
 */
public class UserDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_user";
    private static final String RESET_USER_AUTO_INC_SQL =
            "alter table users " +
            "auto_increment = 1;";
    private static final String RESET_ORG_AUTO_INC_SQL =
            "alter table orgs " +
            "auto_increment = 1";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private UserDao userDao;
    private final DaoTestMethods<UserDTO,Long,UserDao> daoTestMethods;

    public UserDaoTest(){
        this.daoTestMethods = new DaoTestMethods<>(UserDTO.class.getSimpleName());
    }

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, true);
    }

    @Before
    public void setUp() throws Exception{
        this.userDao = daoTestUtils.prepareTestDao(UserDao.class);
        OrgDao orgDao = daoTestUtils.prepareTestDao(OrgDao.class);
        OrgDTO org = daoTestUtils.getOrg1();
        //If this fails, the issue is with the OrgDao
        orgDao.insert(org);
        org = daoTestUtils.getOrg2();
        orgDao.insert(org);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(RESET_USER_AUTO_INC_SQL, RESET_ORG_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        UserDTO user = daoTestUtils.getUser1();
        daoTestMethods.testInsert(user, userDao, 1L);
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        UserDTO user1 = daoTestUtils.getUser1();
        UserDTO user2 = daoTestUtils.getUser2();
        daoTestMethods.testUpdateAndGet(user1, user2, userDao, 1L);
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        UserDTO user1 = daoTestUtils.getUser1();
        daoTestMethods.testDeleteAndGet(user1, userDao, 1L);
    }

    @Test
    public void testCount() throws Exception{
        daoTestMethods.testCount(this::insertManyUsers, userDao, 10);
    }

    @Test
    public void testGetAll() throws Exception{
        daoTestMethods.testGetAll(this::insertManyUsers, userDao, 10);
    }

    @Test
    public void testGetAllLimit() throws Exception{
        daoTestMethods.testGetAllLimit(this::insertManyUsers, userDao, 3, 3, new Long[]{4L, 5L, 6L});
    }

    @Test
    public void testInsertOrUpdate() throws Exception{
        UserDTO userToUpdate = daoTestUtils.getUser1();
        UserDTO userToInsert = daoTestUtils.getUser2();
        daoTestMethods.testInsertOrUpdate(userToUpdate, userToInsert, userDao, 1L, 2L);
    }

    @Test
    public void testGetWithName() throws Exception{
        UserDTO user = daoTestUtils.getUser1();
        daoTestMethods.testInsert(user, userDao, 1L);
        user.setElementId(1L);
        UserDTO result = userDao.getWithName(user.getUserEmail());
        assertNotNull("Result is null", result);
        assertEquals("Result is invalid", user, result);
    }

    private void insertManyUsers() throws Exception{
        for(int i = 0; i < 10; i++){
            UserDTO user = daoTestUtils.getUser1();
            user.setUserEmail(user.getUserEmail() + i);
            userDao.insert(user);
        }
    }
}
