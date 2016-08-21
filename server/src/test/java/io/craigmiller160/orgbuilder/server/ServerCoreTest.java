package io.craigmiller160.orgbuilder.server;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by craig on 8/13/16.
 */
public class ServerCoreTest {

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
    public void testDDL(){
        String ddlScript = ServerCore.getDDLScript();
        assertNotNull("Missing! DDL Script", ddlScript);
    }

}
