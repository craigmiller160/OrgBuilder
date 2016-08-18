package io.craigmiller160.orgbuilder.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by craig on 8/13/16.
 */
public class ServerCoreTest {

    private ServerCore serverCore;

    @Before
    public void init(){
        this.serverCore = new ServerCore();
        this.serverCore.contextInitialized(null);
    }

    @Test
    public void testProperties(){
        String dbUrl = ServerCore.properties.getProperty(ServerProps.DB_URL_PROP);
        String dbClass = ServerCore.properties.getProperty(ServerProps.DB_CLASS_PROP);
        String dbUser = ServerCore.properties.getProperty(ServerProps.DB_USER_PROP);
        String dbPass = ServerCore.properties.getProperty(ServerProps.DB_PASS_PROP);
        String initSize = ServerCore.properties.getProperty(ServerProps.POOL_INIT_SIZE_PROP);
        String maxSize = ServerCore.properties.getProperty(ServerProps.POOL_MAX_SIZE_PROP);
        String defaultDao = ServerCore.properties.getProperty(ServerProps.DEFAULT_DAO_PROP);

        assertEquals("Invalid! " + ServerProps.DB_URL_PROP, "url", dbUrl);
        assertEquals("Invalid! " + ServerProps.DB_CLASS_PROP, "class", dbClass);
        assertEquals("Invalid! " + ServerProps.DB_USER_PROP, "user", dbUser);
        assertEquals("Invalid! " + ServerProps.DB_PASS_PROP, "password", dbPass);
        assertEquals("Invalid! " + ServerProps.POOL_INIT_SIZE_PROP, "initSize", initSize);
        assertEquals("Invalid! " + ServerProps.POOL_MAX_SIZE_PROP, "maxSize", maxSize);
        assertEquals("Invalid! " + ServerProps.DEFAULT_DAO_PROP, "daoFactory", defaultDao);
    }

}
