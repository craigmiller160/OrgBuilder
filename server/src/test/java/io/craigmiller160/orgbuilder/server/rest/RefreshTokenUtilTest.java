package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.craigmiller160.orgbuilder.server.rest.TokenTestUtils.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 10/2/16.
 */
public class RefreshTokenUtilTest {

    private static final String USER_AGENT = "TestUserAgent";

    @BeforeClass
    public static void setUp() throws Exception{
        ServerCore core = new ServerCore();
        core.contextInitialized(null);
    }

    @Test
    public void testIsValidRefreshToken() throws Exception{
        String token = generateToken(false);

        //If it fails here, that means that JWTUtil is broken
        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Access Token wasn't validated and returned", jwt);

        String tokenHash = RefreshTokenUtil.generateRefreshTokenHash(JWTUtil.getTokenSubjectClaim(jwt), USER_AGENT);
        boolean result = RefreshTokenUtil.isValidRefreshToken(jwt, USER_AGENT, tokenHash);
        assertTrue("Refresh token validation failed", result);
    }

}
