package io.craigmiller160.orgbuilder.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by craig on 10/2/16.
 */
public class KeyManagerTest {

    private static ServerCore serverCore;

    @BeforeClass
    public static void setUp(){
        serverCore = new ServerCore();
        serverCore.contextInitialized(null);
    }

    @AfterClass
    public static void tearDown(){
        serverCore.contextDestroyed(null);
    }

    @Test
    public void testPublicKey() throws Exception{
        PublicKey publicKey = ServerCore.getKeyManager().getTokenPublicKey();
        assertNotNull("PublicKey not found", publicKey);
        assertEquals("PublicKey algorithm incorrect", "RSA", publicKey.getAlgorithm());
    }

    @Test
    public void testPrivateKey() throws Exception{
        PrivateKey privateKey = ServerCore.getKeyManager().getTokenPrivateKey();
        assertNotNull("PrivateKey not found", privateKey);
        assertEquals("PrivateKey algorithm incorrect", "RSA", privateKey.getAlgorithm());
    }

//    @Test
//    public void testSecretKey() throws Exception{
//        SecretKey secretKey = ServerCore.getKeyManager().getDataSecretKey();
//        assertNotNull("SecretKey not found", secretKey);
//        assertEquals("SecretKey algorithm incorrect", "AES", secretKey.getAlgorithm());
//    }

//    @Test
//    public void testCACertificate() throws Exception{
//        Certificate certificate = ServerCore.getKeyManager().getCACertificate();
//        assertNotNull("CA Certificate not found", certificate);
//    }

}
