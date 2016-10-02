package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.ServerCore;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static io.craigmiller160.orgbuilder.server.rest.TokenTestUtils.*;

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
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        boolean result = JWTUtil.isTokenExpired(jwt);
        assertFalse("Token shouldn't be expired", result);
    }

    @Test
    public void testValidate_Expired_ValidSig() throws Exception{
        String token = generateToken(true);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        boolean result = JWTUtil.isTokenExpired(jwt);
        assertTrue("Token should be expired", result);
    }

    @Test
    public void testGetTokenSchemaClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        String schema = JWTUtil.getTokenSchemaClaim(jwt);
        assertNotNull("Token schema claim wasn't returned", schema);
        assertEquals("Wrong schema claim value returned", SCHEMA_NAME, schema);
    }

    @Test
    public void testGetTokenRolesClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        Set<String> result = JWTUtil.getTokenRolesClaim(jwt);
        assertNotNull("Token roles claim wasn't returned", result);
        assertEquals("Wrong number of roles returned from token", 2, result.size());
        for(String r : result){
            assertTrue("Roles doesn't contain returned role", roles.contains(r));
        }
    }

    @Test
    public void testGetTokenSubjectClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        String subject = JWTUtil.getTokenSubjectClaim(jwt);
        assertNotNull("Token subject claim wasn't returned", subject);
        assertEquals("Wrong subject claim value returned", USER_NAME, subject);
    }

    @Test
    public void testGetTokenIdClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        long tokenId = JWTUtil.getTokenIdClaim(jwt);
        assertTrue("Token ID claim wasn't returned", tokenId > 0);
        assertEquals("Wrong Token ID claim value returned", tokenId, 1);
    }

}
