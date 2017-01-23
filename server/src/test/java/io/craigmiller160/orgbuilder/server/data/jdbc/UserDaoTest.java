package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 9/4/16.
 */
public class UserDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_user";
    private static final String RESET_USER_AUTO_INC_SQL =
            "alter table old.users " +
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
    public void testGetByIdAndOrg() throws Exception{
        UserDTO user1 = daoTestUtils.getUser1();
        UserDTO user2 = daoTestUtils.getUser2();
        user1 = userDao.insert(user1);
        user2 = userDao.insert(user2);

        UserDTO result1 = userDao.getByIdAndOrg(user1.getElementId(), user1.getOrgId());
        assertNotNull("User with correct userId and orgId was not returned", result1);

        UserDTO result2 = userDao.getByIdAndOrg(user1.getElementId(), user2.getOrgId());
        assertNull("User with incorrect orgId was returned", result2);
    }

    @Test
    public void testCountByOrg() throws Exception{
        insertManyUsers();
        UserDTO user1 = daoTestUtils.getUser1();
        UserDTO user2 = daoTestUtils.getUser2();
        user2 = userDao.insert(user2);

        long result = userDao.countByOrg(user1.getOrgId());
        assertEquals("Incorrect count of old.users by orgId", 10, result);
    }

    @Test
    public void testGetAllByOrg() throws Exception{
        insertManyUsers();
        UserDTO user1 = daoTestUtils.getUser1();
        UserDTO user2 = daoTestUtils.getUser2();
        user2 = userDao.insert(user2);

        List<UserDTO> result = userDao.getAllByOrg(user1.getOrgId());
        assertNotNull("List of old.users by orgId not returned", result);
        assertEquals("Incorrect size of list of old.users by orgId was returned", 10, result.size());
        assertFalse("List of old.users by orgId contains a record it should not", result.contains(user2));
    }

    @Test
    public void testGetAllLimitByOrg() throws Exception{
        insertManyUsers();
        UserDTO user1 = daoTestUtils.getUser1();
        UserDTO user2 = daoTestUtils.getUser2();
        user2 = userDao.insert(user2);

        List<UserDTO> result = userDao.getAllLimitByOrg(user1.getOrgId(), 3, 3);
        assertNotNull("List of old.users by orgId not returned", result);
        assertEquals("Incorrect size of list of old.users by orgId was returned", 3, result.size());
        assertFalse("List of old.users by orgId contains a record it should not", result.contains(user2));
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

    @Test
    public void testDeleteByOrg() throws Exception{
        insertManyUsers();
        UserDTO user = daoTestUtils.getUser1();

        long result = userDao.deleteByOrg(user.getOrgId());
        assertEquals("Incorrect number of records deleted", 10, result);

        long count = userDao.getCount();
        assertEquals("Some records were not deleted", 0, count);
    }

    private void insertManyUsers() throws Exception{
        for(int i = 0; i < 10; i++){
            UserDTO user = daoTestUtils.getUser1();
            user.setUserEmail(user.getUserEmail() + i);
            userDao.insert(user);
        }
    }
}
