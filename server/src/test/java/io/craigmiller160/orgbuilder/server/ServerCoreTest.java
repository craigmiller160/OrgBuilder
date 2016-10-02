package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 8/13/16.
 */
public class ServerCoreTest {

    private static final String APP_SCHEMA_CHECK_SQL =
            "select schema_name " +
            "from information_schema.schemata " +
            "where schema_name = 'org_app';";

    private static final String CHECK_TABLES_SQL =
            "show tables from org_app;";

    @BeforeClass
    public static void init(){
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);
    }

    @Test
    public void testProperties(){
        String dbUrl = ServerCore.getProperty(ServerProps.DB_URL_PROP);
        String dbClass = ServerCore.getProperty(ServerProps.DB_CLASS_PROP);
        String dbUser = ServerCore.getProperty(ServerProps.DB_USER_PROP);
        String dbPass = ServerCore.getProperty(ServerProps.DB_PASS_PROP);
        String initSize = ServerCore.getProperty(ServerProps.POOL_INIT_SIZE_PROP);
        String maxSize = ServerCore.getProperty(ServerProps.POOL_MAX_SIZE_PROP);

        assertNotNull("Missing! " + ServerProps.DB_URL_PROP, dbUrl);
        assertNotNull("Missing! " + ServerProps.DB_CLASS_PROP, dbClass);
        assertNotNull("Missing! " + ServerProps.DB_USER_PROP, dbUser);
        assertNotNull("Missing! " + ServerProps.DB_PASS_PROP, dbPass);
        assertNotNull("Missing! " + ServerProps.POOL_INIT_SIZE_PROP, initSize);
        assertNotNull("Missing! " + ServerProps.POOL_MAX_SIZE_PROP, maxSize);
    }

    @Test
    public void testDataManager() throws Exception{
        Class<?> clazz = ServerCore.getOrgDataManager().getClass();
        Method m = clazz.getDeclaredMethod("getDataSource");
        m.setAccessible(true);
        OrgDataSource dataSource = (OrgDataSource) m.invoke(ServerCore.getOrgDataManager());
        try(Connection conn = dataSource.getConnection()){
            try(Statement stmt = conn.createStatement()){
                try(ResultSet resultSet = stmt.executeQuery(APP_SCHEMA_CHECK_SQL)){
                    assertTrue("No org_app schema exists", resultSet.next());
                }

                try(ResultSet resultSet = stmt.executeQuery(CHECK_TABLES_SQL)){
                    int tableCount = 0;
                    while(resultSet.next()){
                        tableCount++;
                    }
                    assertEquals("Wrong number of tables in org_app schema", 3, tableCount);
                }
            }
        }
    }

}
