package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 8/21/16.
 */
public class SchemaManagerTest {

    private static final String SCHEMA_NAME = "test_schema";
    private static final String CLEANUP_SQL = "drop schema if exists " + SCHEMA_NAME;

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

    @AfterClass
    public static void tearDown() throws Exception{
        Class<?> clazz = ServerCore.getOrgDataManager().getClass();
        Method m = clazz.getDeclaredMethod("getDataSource");
        m.setAccessible(true);
        OrgDataSource dataSource = (OrgDataSource) m.invoke(ServerCore.getOrgDataManager());
        try(Connection conn = dataSource.getConnection()){
            try(Statement stmt = conn.createStatement()){
                stmt.executeUpdate(CLEANUP_SQL);
            }
        }
    }

    @Test
    public void testSchemaExists() throws Exception{
        boolean exists = schemaManager.schemaExists("information_schema");
        assertTrue("Schema doesn't exist when it should", exists);
    }

    @Test
    public void testCreateSchema() throws Exception{
        schemaManager.createSchema(SCHEMA_NAME);
        String[] tableNames = schemaManager.getTableNames(SCHEMA_NAME);
        assertNotNull("Table Names is null", tableNames);
        assertEquals("Table names is the wrong size", 4, tableNames.length);
    }

}
