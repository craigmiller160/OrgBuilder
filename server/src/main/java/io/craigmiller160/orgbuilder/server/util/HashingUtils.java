package io.craigmiller160.orgbuilder.server.util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by craigmiller on 9/28/16.
 */
public class HashingUtils {

    //TODO need a JUnit test case for this, especially for the SHA 256 part

    public static String hashBCrypt(String value){
        return BCrypt.hashpw(value, BCrypt.gensalt(12));
    }

    public static boolean verifyBCryptHash(String value, String hash){
        return BCrypt.checkpw(value, hash);
    }

    public static String hashSHA256(String value) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(value.getBytes());
        return Base64.getEncoder().encodeToString(digest.digest());
    }

    public static boolean verifySHA256(String value, String hash) throws NoSuchAlgorithmException{
        return hashSHA256(value).equals(hash);
    }

}
