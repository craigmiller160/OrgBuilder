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
        String defaultDao = ServerCore.properties.getProperty(ServerProps.DEFAULT_DAO_PROP);
        assertEquals("DB URL is the wrong value", "urlTest", dbUrl);
        assertEquals("Default DAO is the wrong value", "daoTest", defaultDao);
    }

}
