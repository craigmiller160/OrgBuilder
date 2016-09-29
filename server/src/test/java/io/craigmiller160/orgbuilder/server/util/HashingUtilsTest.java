package io.craigmiller160.orgbuilder.server.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 9/28/16.
 */
public class HashingUtilsTest {

    @Test
    public void testBCrypt(){
        String value = "ThisIsTheValue";
        String hash = HashingUtils.hashBCrypt(value);
        assertTrue("Hash is unable to be verified", HashingUtils.verifyBCryptHash(value, hash));
    }

    @Test
    public void testSha256() throws Exception{
        String value = "ThisIsTheValue";
        String hash = HashingUtils.hashSHA256(value);
        assertTrue("Hash is unable to be verified", HashingUtils.verifySHA256(value, hash));
    }

}
