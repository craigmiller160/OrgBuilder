package io.craigmiller160.orgbuilder.server.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by craigmiller on 9/28/16.
 */
public class HashingUtils {

    public static String hashBCrypt(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verifyBCryptHash(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }

}
