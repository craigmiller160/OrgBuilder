package io.craigmiller160.orgbuilder.server.util;

import java.security.SecureRandom;

/**
 * Created by craigmiller on 9/28/16.
 */
public class HashingUtils {

    private static void foo(){
        SecureRandom secureRandom = new SecureRandom();
        //Use secureRandom to create the salt
        //Use BCrypt to hash the password
        //Return salt and hash for storage in the database
    }

}
