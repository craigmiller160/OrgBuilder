package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import org.junit.AfterClass;
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
    private static ServerCore core;

    @BeforeClass
    public static void setUp() throws Exception{
        core = new ServerCore();
        core.contextInitialized(null);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        core.contextDestroyed(null);
    }

    @Test
    public void testIsValidRefreshToken() throws Exception{
        String token = generateToken(false);

        //If it fails here, that means that JWTUtil is broken
        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Access Token wasn't validated and returned", jwt);

        String subject = JWTUtil.getTokenSubjectClaim(jwt);
        String userName = JWTUtil.splitUserNameOrgName(subject)[0];

        String tokenHash = RefreshTokenUtil.generateRefreshTokenHash(userName, USER_AGENT);
        boolean result = RefreshTokenUtil.isValidRefreshToken(jwt, USER_AGENT, tokenHash);
        assertTrue("Refresh token validation failed", result);
    }

}
