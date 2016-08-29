package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 8/21/16.
 */
public class SchemaManagerTest {

    private static final String SCHEMA_NAME = "test_schema";

    private static SchemaManager schemaManager;

    @BeforeClass
    public static void init() throws Exception{
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);
        Class<?> clazz = ServerCore.getOrgDataManager().getClass();
        Method m = clazz.getDeclaredMethod("getSchemaManager");
        m.setAccessible(true);
        schemaManager = (SchemaManager) m.invoke(ServerCore.getOrgDataManager());
    }

    //This also tests the deleteSchema() method
    @AfterClass
    public static void tearDown() throws Exception{
        schemaManager.deleteSchema(SCHEMA_NAME);
    }

    @Test
    public void testSchemaExists() throws Exception{
        boolean exists = schemaManager.schemaExists("information_schema");
        assertTrue("Schema doesn't exist when it should", exists);
    }

    @Test
    public void testCreateOrgSchema() throws Exception{
        schemaManager.createSchema(SCHEMA_NAME, false);
        String[] tableNames = schemaManager.getTableNames(SCHEMA_NAME);
        assertNotNull("Table Names is null", tableNames);
        assertEquals("Table names is the wrong size", 4, tableNames.length);
    }

}
