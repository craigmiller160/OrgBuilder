package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.KeyManager;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.util.LegacyDateConverter;
import java.time.LocalDateTime;
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
        KeyManager keyManager = ServerCore.getKeyManager();
        String token = null;
        try{
            //If this blows up with NumberFormatException, the property is invalid
            int expMins = Integer.parseInt(ServerCore.getProperty(ServerProps.ACCESS_EXP_MINS));
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime exp = now.plusMinutes(expMins);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(LegacyDateConverter.convertLocalDateTimeToDate(now))
                    .issuer(ServerCore.getProperty(ServerProps.API_NAME))
                    .expirationTime(LegacyDateConverter.convertLocalDateTimeToDate(exp))
                    .jwtID("" + tokenId)
                    .claim(SCHEMA_CLAIM_KEY, schema)
                    .claim(ROLE_CLAIM_KEY, roles)
                    .build();
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
            JWSSigner signer = new RSASSASigner(keyManager.getTokenPrivateKey());
            jwt.sign(signer);
            token = jwt.serialize();
        }
        catch(JOSEException ex){
            throw new OrgApiSecurityException("Unable to sign JSON Web Token", ex);
        }

        return token;
    }



}
