package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 9/28/16.
 */
public class RefreshTokenDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_token";

    private static final String RESET_TOKEN_AUTO_INC_SQL =
            "alter table tokens " +
            "auto_increment = 1;";

    private static final String RESET_USER_AUTO_INC_SQL =
            "alter table users " +
            "auto_increment = 1;";

    private static final String RESET_ORG_AUTO_INC_SQL =
            "alter table orgs " +
            "auto_increment = 1;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private RefreshTokenDao tokenDao;

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, true);
    }

    @Before
    public void setUp() throws Exception{
        this.tokenDao = daoTestUtils.prepareTestDao(RefreshTokenDao.class);
        OrgDao orgDao = daoTestUtils.prepareTestDao(OrgDao.class);
        UserDao userDao = daoTestUtils.prepareTestDao(UserDao.class);
        UserDTO user1 = daoTestUtils.getUser1();
        OrgDTO org1 = daoTestUtils.getOrg1();

        //If this fails, the issue is with the OrgDao
        orgDao.insert(org1);

        //If this fails, the issue is with the UserDao
        userDao.insert(user1);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(RESET_TOKEN_AUTO_INC_SQL, RESET_USER_AUTO_INC_SQL, RESET_ORG_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        RefreshTokenDTO token = daoTestUtils.getToken1();
        token = tokenDao.insert(token);
        assertTrue("Failed to insert TokenDTO", 1 == token.getElementId());
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        testInsert();
        RefreshTokenDTO token = daoTestUtils.getToken1();
        token.setTokenHash("AnotherHash");
        token.setElementId(1L);
        tokenDao.update(token, token.getElementId());
        RefreshTokenDTO token2 = tokenDao.get(1L);
        assertNotNull("TokenDTO is null", token2);
        assertEquals("TokenDTO has the wrong hash", "AnotherHash", token2.getTokenHash());
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        testInsert();
        RefreshTokenDTO token = daoTestUtils.getToken1();
        token = tokenDao.get(1L);
        RefreshTokenDTO token2 = tokenDao.delete(1L);
        assertEquals("Deleted TokenDTO is not the same as inserted TokenDTO", token, token2);
        token2 = tokenDao.get(1L);
        assertNull("TokenDTO record was not deleted", token2);
    }

    @Test
    public void testCount() throws Exception{
        testInsert();
        long count = tokenDao.getCount();
        assertEquals("Incorrect count of TokenDTO in database", 1, count);
    }

    @Test
    public void testGetAll() throws Exception{
        insertManyTokens();

        List<RefreshTokenDTO> tokens = tokenDao.getAll();
        assertEquals("Get All returned the wrong number of tokens", 10, tokens.size());
    }

    @Test
    public void testGetAllLimit() throws Exception{
        insertManyTokens();

        List<RefreshTokenDTO> tokens = tokenDao.getAll(3, 3);
        assertEquals("Get All Limit returned the wrong number of tokens", 3, tokens.size());
        assertTrue("First returned token is incorrect", 4 == tokens.get(0).getElementId());
        assertTrue("Second returned token is incorrect", 5 == tokens.get(1).getElementId());
        assertTrue("Third returned token is incorrect", 6 == tokens.get(2).getElementId());
    }

    @Test
    public void testInsertOrUpdate() throws Exception{
        testInsert();
        RefreshTokenDTO tokenToUpdate = daoTestUtils.getToken1();
        tokenToUpdate.setTokenHash("AnotherValue");
        tokenToUpdate.setElementId(1L);
        tokenDao.insertOrUpdate(tokenToUpdate);

        RefreshTokenDTO result = tokenDao.get(1L);
        assertNotNull("Update result token is null", result);
        assertEquals("TokenDao insertOrUpdate method did not update existing org", tokenToUpdate.getTokenHash(), result.getTokenHash());

        RefreshTokenDTO tokenToInsert = daoTestUtils.getToken2();
        tokenDao.insertOrUpdate(tokenToInsert);
        tokenToInsert.setElementId(2L);

        result = tokenDao.get(2L);
        assertNotNull("Insert result token is null", result);
    }

    @Test
    public void testGetWithHash() throws Exception{
        testInsert();
        RefreshTokenDTO tokenToUpdate = daoTestUtils.getToken1();
        String hash = tokenToUpdate.getTokenHash();

        RefreshTokenDTO result = tokenDao.getWithHash(hash);
        assertNotNull("Token with hash was not returned", result);
    }

    private void insertManyTokens() throws Exception{
        for(int i = 0; i < 10; i++){
            RefreshTokenDTO token = daoTestUtils.getToken1();
            token.setTokenHash(token.getTokenHash() + i);
            tokenDao.insert(token);
        }
    }

}
