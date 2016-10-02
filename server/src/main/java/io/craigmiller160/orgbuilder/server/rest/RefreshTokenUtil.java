package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;

/**
 * Created by craig on 10/2/16.
 */
public class RefreshTokenUtil {

    public static String generateRefreshTokenHash(String userEmail, String userAgent) throws OrgApiSecurityException{
        return HashingUtils.hashSHA256(userEmail + userAgent);
    }

    /**
     * Test that this refresh token is a valid
     * match for the access token that has its
     * ID by re-generating the hash and comparing it.
     *
     * @param jwt
     * @param userAgent
     * @param tokenHash
     * @return true if it's a valid refresh token.
     */
    public static boolean isValidRefreshToken(SignedJWT jwt, String userAgent, String tokenHash) throws OrgApiSecurityException{
        String subject = JWTUtil.getTokenSubjectClaim(jwt);
        return HashingUtils.verifySHA256(subject + userAgent, tokenHash);
    }

}
