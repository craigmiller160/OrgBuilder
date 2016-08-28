package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerTestUtils;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by craig on 8/28/16.
 */
public class JdbcDataConnectionTest {

    private static final String TEST_SCHEMA = "test_jdbc_connect";

    private JdbcDataConnection jdbcDataConnection;
    private static OrgDataSource dataSource;
    private static JdbcManager jdbcManager;

    @BeforeClass
    public static void init() throws Exception{
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);

        ServerCore.getOrgDataManager().createOrgSchema(TEST_SCHEMA);
        dataSource = ServerTestUtils.getOrgDataSource(ServerCore.getOrgDataManager());
        jdbcManager = ServerTestUtils.getJdbcManager(ServerCore.getOrgDataManager());
    }

    @Before
    public void setup() throws Exception{
        jdbcDataConnection = new JdbcDataConnection(dataSource, jdbcManager, TEST_SCHEMA);
    }

    @Test
    public void testNewDaoAndClose() throws Exception{
        try{
            Dao<OrgDTO,Long> orgDao = jdbcDataConnection.newDao(OrgDTO.class);
            assertNotNull("OrgDao is null", orgDao);
            assertEquals("OrgDao is wrong type", OrgDao.class, orgDao.getClass());
        }
        finally{
            jdbcDataConnection.close();
        }
    }

    @AfterClass
    public static void destroy() throws Exception{
        ServerCore.getOrgDataManager().deleteSchema(TEST_SCHEMA);
    }

}
