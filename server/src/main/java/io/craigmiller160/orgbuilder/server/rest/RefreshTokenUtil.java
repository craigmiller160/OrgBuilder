package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;

/**
 * Created by craig on 10/2/16.
 */
public class RefreshTokenUtil {

    /**
     * Generate the refresh token hash, which is a combination of the
     * user's username and user agent String.
     *
     * @param userName the user name.
     * @param userAgent the user agent String.
     * @return the token hash.
     * @throws OrgApiSecurityException if an error occurs generating the hash.
     */
    public static String generateRefreshTokenHash(String userName, String userAgent) throws OrgApiSecurityException{
        return HashingUtils.hashSHA256(userName + userAgent);
    }

    /**
     * Test that this refresh token is a valid
     * match for the access token that has its
     * ID by re-generating the hash and comparing it.
     *
     * @param jwt the JSON Web Token.
     * @param userAgent the user agent String.
     * @param tokenHash the hash of the refresh token.
     * @return true if it's a valid refresh token.
     * @throws OrgApiSecurityException if an error occurs validating the hash.
     */
    public static boolean isValidRefreshToken(SignedJWT jwt, String userAgent, String tokenHash) throws OrgApiSecurityException{
        String userName = JWTUtil.splitUserNameOrgName(JWTUtil.getTokenSubjectClaim(jwt))[0];
        return HashingUtils.verifySHA256(userName + userAgent, tokenHash);
    }

}
