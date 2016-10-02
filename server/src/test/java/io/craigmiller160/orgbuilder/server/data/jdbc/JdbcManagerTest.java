package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.Dao;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by craig on 8/27/16.
 */
public class JdbcManagerTest {

    private static ServerCore serverCore;

    @BeforeClass
    public static void setUp(){
        serverCore = new ServerCore();
        serverCore.contextInitialized(null);
    }

    @AfterClass
    public static void tearDown(){
        serverCore.contextDestroyed(null);
    }

    @Test
    public void testQueryLoading() throws Exception{
        long start = System.currentTimeMillis();
        JdbcManager jdbcManager = JdbcManager.newInstance();
        long end = System.currentTimeMillis();
        //Keep printing out the timestamp, for checking how it performs in the future
        System.out.println("TIME: " + (end - start));
        Map<Class<? extends Dao>,Map<JdbcManager.Query,String>> mappedQueries = jdbcManager.getMappedQueries();
        assertNotNull("Mapped Queries map is null", mappedQueries);
        assertEquals("Mapped Queries map has wrong number of child maps", 7, mappedQueries.size());

        //OrgDao query tests
        Map<JdbcManager.Query,String> orgDaoQueries = mappedQueries.get(OrgDao.class);
        assertNotNull("OrgDaoQueries map is null", orgDaoQueries);
        assertEquals("OrgDaoQueries map has wrong number of queries", 8, orgDaoQueries.size());

        //AddressDao query tests
        Map<JdbcManager.Query,String> addressDaoQueries = mappedQueries.get(AddressDao.class);
        assertNotNull("addressDaoQueries map is null", addressDaoQueries);
        assertEquals("addressDaoQueries map has wrong number of queries", 15, addressDaoQueries.size());

        //EmailDao query tests
        Map<JdbcManager.Query,String> emailDaoQueries = mappedQueries.get(EmailDao.class);
        assertNotNull("emailDaoQueries map is null", emailDaoQueries);
        assertEquals("emailDaoQueries map has wrong number of queries", 15, emailDaoQueries.size());

        //MemberDao query tests
        Map<JdbcManager.Query,String> memberDaoQueries = mappedQueries.get(MemberDao.class);
        assertNotNull("memberDaoQueries map is null", memberDaoQueries);
        assertEquals("memberDaoQueries map has wrong number of queries", 9, memberDaoQueries.size());

        //PhoneDao query tests
        Map<JdbcManager.Query,String> phoneDaoQueries = mappedQueries.get(PhoneDao.class);
        assertNotNull("phoneDaoQueries map is null", phoneDaoQueries);
        assertEquals("phoneDaoQueries map has wrong number of queries", 15, phoneDaoQueries.size());

        //UserDao query tests
        Map<JdbcManager.Query,String> userDaoQueries = mappedQueries.get(UserDao.class);
        assertNotNull("userDaoQueries map is null", userDaoQueries);
        assertEquals("userDaoQueries map has wrong number of queries", 9, userDaoQueries.size());

        //TokenDao query tests
        Map<JdbcManager.Query,String> tokenDaoQueries = mappedQueries.get(RefreshTokenDao.class);
        assertNotNull("tokenDaoQueries map is null", tokenDaoQueries);
        assertEquals("tokenDaoQueries map has wrong number of queries", 8, tokenDaoQueries.size());

        //Schema Scripts tests
        Map<JdbcManager.Schema,List<String>> schemaScripts = jdbcManager.getSchemaScripts();
        List<String> orgSchema = schemaScripts.get(JdbcManager.Schema.ORG_SCHEMA);
        assertNotNull("OrgSchema was not loaded", orgSchema);
        assertEquals("OrgSchema has wrong number of queries", 5, orgSchema.size());

        List<String> appSchema = schemaScripts.get(JdbcManager.Schema.APP_SCHEMA);
        assertNotNull("AppSchema was not loaded", appSchema);
        assertEquals("AppSchema has wrong number of queries", 5, appSchema.size());
    }

}
