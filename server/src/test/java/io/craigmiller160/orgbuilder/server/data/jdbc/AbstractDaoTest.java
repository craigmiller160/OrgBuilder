package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerTestUtils;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.Statement;

/**
 * An abstract superclass for all DAO testing logic,
 * except for the OrgDao, due to its unique schema
 * setup.
 *
 * Created by craigmiller on 8/24/16.
 */
public abstract class AbstractDaoTest {

    private static OrgDataSource dataSource;
    private MemberDao memberDao;
    private Connection connection;

    @BeforeClass
    public static void init() throws Exception{
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);

        dataSource = ServerTestUtils.getOrgDataSource(ServerCore.getOrgDataManager());
        ServerCore.getOrgDataManager().createNewSchema(getTestSchemaName());
    }

    @Before
    public void setUp() throws Exception{
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        memberDao = new MemberDao(connection);
    }

    @After
    public void cleanUp() throws Exception{
        connection.rollback();
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate(getResetAutoIncSql());
        }
        connection.commit();
        connection.close();
    }

    @AfterClass
    public static void tearDown() throws Exception{
        ServerCore.getOrgDataManager().deleteSchema(getTestSchemaName());
    }

    protected abstract String getTestSchemaName();

    protected abstract String getResetAutoIncSql();

}
