package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.util.LegacyDateConverter;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * A utility class for handling the JSON Web Tokens.
 *
 * Created by craig on 10/1/16.
 */
public class JWTUtil {

    public static final String SCHEMA_CLAIM_KEY = "SMA";
    public static final String ROLE_CLAIM_KEY = "ROL";

    private JWTUtil(){}

    public static String generateNewToken(long tokenId, String username, Set<String> roles, String schema) throws OrgApiSecurityException{
        //If this blows up with NumberFormatException, the property is invalid
        int expMins = Integer.parseInt(ServerCore.getProperty(ServerProps.ACCESS_EXP_MINS));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.plusMinutes(expMins);
        Date expiration = LegacyDateConverter.convertLocalDateTimeToDate(exp);
        return generateNewToken(tokenId, username, roles, schema, expiration);
    }

    static String generateNewToken(long tokenId, String username, Set<String> roles, String schema, Date expiration) throws OrgApiSecurityException{
        String token = null;
        try{
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(LegacyDateConverter.convertLocalDateTimeToDate(LocalDateTime.now()))
                    .issuer(ServerCore.getProperty(ServerProps.API_NAME))
                    .expirationTime(expiration)
                    .jwtID("" + tokenId)
                    .claim(SCHEMA_CLAIM_KEY, schema)
                    .claim(ROLE_CLAIM_KEY, roles)
                    .build();
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
            JWSSigner signer = new RSASSASigner(ServerCore.getKeyManager().getTokenPrivateKey());
            jwt.sign(signer);
            token = jwt.serialize();
        }
        catch(JOSEException ex){
            throw new OrgApiSecurityException("Unable to sign JSON Web Token", ex);
        }

        return token;
    }

    public static SignedJWT parseAndValidateTokenSignature(String token) throws OrgApiSecurityException{
        SignedJWT jwt = null;
        try{
            jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) ServerCore.getKeyManager().getTokenPublicKey());
            if(!jwt.verify(verifier)){
                throw new OrgApiInvalidTokenException("Token signature is invalid");
            }
        }
        catch(ParseException | JOSEException ex){
            throw new OrgApiSecurityException("Unable to parse JSON Web Token", ex);
        }

        return jwt;
    }

    public static boolean isTokenExpired(SignedJWT jwt) throws OrgApiSecurityException{
        boolean result = false;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            LocalDateTime expiration = LegacyDateConverter.convertDateToLocalDateTime(claimsSet.getExpirationTime());
            LocalDateTime now = LocalDateTime.now();
            result = now.compareTo(expiration) <= 0;
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to validate JSON Web Token expiration", ex);
        }

        return result;
    }

}
