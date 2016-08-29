package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerTestUtils;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * These tests test complex operations of the
 * JdbcDataConnection. These tests aren't
 * truly unit tests, since they greatly
 * depend on other behavior that is unit
 * tested in other areas. If the other behavior
 * fails, the tests here will fail.
 *
 * Created by craig on 8/28/16.
 */
public class JdbcDataConnectionTest {

    private static final String TEST_SCHEMA = "test_jdbc_connect";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
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

    @Test
    public void testNewDaoAndClose() throws Exception{
        try(JdbcDataConnection jdbcDataConnection = new JdbcDataConnection(dataSource, jdbcManager, TEST_SCHEMA)){
            Dao<OrgDTO,Long> orgDao = jdbcDataConnection.newDao(OrgDTO.class);
            assertNotNull("OrgDao is null", orgDao);
            assertEquals("OrgDao is wrong type", OrgDao.class, orgDao.getClass());
        }
    }

    @Test
    public void testCommitAndClose() throws Exception{
        try(JdbcDataConnection jdbcDataConnection = new JdbcDataConnection(dataSource, jdbcManager, TEST_SCHEMA)){
            Dao<MemberDTO,Long> memberDao = jdbcDataConnection.newDao(MemberDTO.class);
            assertNotNull("memberDao is null", memberDao);

            MemberDTO member = daoTestUtils.getMember1();
            memberDao.insert(member);
            jdbcDataConnection.commit();
        }

        try(JdbcDataConnection jdbcDataConnection = new JdbcDataConnection(dataSource, jdbcManager, TEST_SCHEMA)){
            Dao<MemberDTO,Long> memberDao = jdbcDataConnection.newDao(MemberDTO.class);
            assertNotNull("memberDao is null", memberDao);

            long count = memberDao.getCount();
            assertEquals("Wrong count of MemberDTO records", 1, count);
        }
    }

    @Test
    public void testRollbackAndClose() throws Exception{
        try(JdbcDataConnection jdbcDataConnection = new JdbcDataConnection(dataSource, jdbcManager, TEST_SCHEMA)){
            Dao<MemberDTO,Long> memberDao = jdbcDataConnection.newDao(MemberDTO.class);
            assertNotNull("memberDao is null", memberDao);
            MemberDTO member = daoTestUtils.getMember1();
            memberDao.insert(member);

            Dao<AddressDTO,Long> addressDao = jdbcDataConnection.newDao(AddressDTO.class);
            assertNotNull("addressDao is null", addressDao);

            AddressDTO address = daoTestUtils.getAddress1();
            addressDao.insert(address);

            long count = addressDao.getCount();
            assertEquals("Count before rollback invalid", 1, count);

            jdbcDataConnection.rollback();

            count = addressDao.getCount();
            assertEquals("Count after rollback invalid", 0, count);

        }
    }

    @AfterClass
    public static void destroy() throws Exception{
        ServerCore.getOrgDataManager().deleteSchema(TEST_SCHEMA);
    }

}
