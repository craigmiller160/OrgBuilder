package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.util.LegacyDateConverter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 10/2/16.
 */
public class JWTUtilTest {

    @BeforeClass
    public static void setUp(){
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);
    }

    @Test
    public void testValidate_Unexpired_ValidSig() throws Exception{
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.plusMinutes(20);
        Date expiration = LegacyDateConverter.convertLocalDateTimeToDate(exp);
        Set<String> roles = new HashSet<>();
        roles.add("Me");
        String token = JWTUtil.generateNewToken(1, "Bob", roles, "TestSchema", expiration);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        boolean result = JWTUtil.isTokenExpired(jwt);
        assertTrue("Token shouldn't be expired", result);
    }

    @Test
    public void testValidate_Expired_ValidSig() throws Exception{
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.minusMinutes(20);
        Date expiration = LegacyDateConverter.convertLocalDateTimeToDate(exp);
        Set<String> roles = new HashSet<>();
        roles.add("Me");
        String token = JWTUtil.generateNewToken(1, "Bob", roles, "TestSchema", expiration);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        boolean result = JWTUtil.isTokenExpired(jwt);
        assertFalse("Token should be expired", result);
    }

}
