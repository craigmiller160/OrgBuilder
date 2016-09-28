package io.craigmiller160.orgbuilder.server.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by craigmiller on 9/28/16.
 */
public class HashingUtils {

    private static String generateSalt() throws NoSuchAlgorithmException{
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String hashPassword(String password, String salt){
        //TODO finish this
        return null;
    }

}
