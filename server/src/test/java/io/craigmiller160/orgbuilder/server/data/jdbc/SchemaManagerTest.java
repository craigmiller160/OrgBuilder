package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 8/21/16.
 */
public class SchemaManagerTest {

    private static final String SCHEMA_NAME = "test_schema";

    private static SchemaManager schemaManager;
    private static Connection connection;
    private static ServerCore serverCore;

    @BeforeClass
    public static void init() throws Exception{
        serverCore = new ServerCore();
        serverCore.contextInitialized(null);
        Class<?> clazz = ServerCore.getOrgDataManager().getClass();
        Method m = clazz.getDeclaredMethod("getSchemaManager");
        m.setAccessible(true);
        schemaManager = (SchemaManager) m.invoke(ServerCore.getOrgDataManager());
        OrgDataManager dataManager = ServerCore.getOrgDataManager();
        Method m2 = dataManager.getClass().getDeclaredMethod("getDataSource");
        m2.setAccessible(true);
        OrgDataSource dataSource = (OrgDataSource) m2.invoke(dataManager);
        connection = dataSource.getConnection();
    }

    //This also tests the deleteSchema() method
    @AfterClass
    public static void tearDown() throws Exception{
        schemaManager.deleteSchema(connection, SCHEMA_NAME, true);
        connection.close();
        serverCore.contextDestroyed(null);
    }

    @Test
    public void testSchemaExists() throws Exception{
        boolean exists = schemaManager.schemaExists(connection, "information_schema");
        assertTrue("Schema doesn't exist when it should", exists);
    }

    @Test
    public void testCreateOrgSchema() throws Exception{
        schemaManager.createSchema(connection, SCHEMA_NAME, false, true);
        String[] tableNames = schemaManager.getTableNames(connection, SCHEMA_NAME);
        assertNotNull("Table Names is null", tableNames);
        assertEquals("Table names is the wrong size", 4, tableNames.length);
    }

}
