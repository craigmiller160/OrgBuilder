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
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.util.LegacyDateConverter;
import org.apache.commons.lang3.StringUtils;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A utility class for handling the JSON Web Tokens.
 *
 * Token Claims:
 *
 * IAT : Issued-At Time
 * ISS : Issuer
 * SUB : Subject
 * EXP : Expiration Time
 * SMA : Schema
 * ROL : Roles
 *
 * Created by craig on 10/1/16.
 */
public class JWTUtil {

    public static final String SCHEMA_CLAIM_KEY = "SMA";
    public static final String ROLES_CLAIM_KEY = "ROL";
    public static final String ORG_ID_CLAIM_KEY = "OID";
    public static final String USER_ID_CLAIM_KEY = "UID";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_NAME_CLAIM_KEY = "UNM";
    public static final String ORG_NAME_CLAIM_KEY = "ONM";

    private JWTUtil(){}

    public static String combineUserNameOrgName(String userName, String orgName){
        return userName + "::" + orgName;
    }

    public static String[] splitUserNameOrgName(String subject){
        String[] split = subject.split("::");
        if(split.length == 0){
            return new String[] {"", ""};
        }
        else if(split.length == 1){
            int index = subject.indexOf("::");
            if(index == 0){
                return new String[] {"", split[0]};
            }
            else{
                return new String[] {split[0], ""};
            }
        }

        return split;
    }

    private static Date generateNewExpiration(){
        //If this blows up with NumberFormatException, the property is invalid
        int expMins = Integer.parseInt(ServerCore.getProperty(ServerProps.ACCESS_EXP_MINS));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.plusMinutes(expMins);
        return LegacyDateConverter.convertLocalDateTimeToDate(exp);
    }

    public static String generateNewToken(long tokenId, String userName, String orgName, long userId, long orgId,
                                          String schemaName, Set<String> roles) throws OrgApiSecurityException{
        Date expiration = generateNewExpiration();
        return generateNewToken(tokenId, userName, orgName, userId, orgId, schemaName, roles, expiration);
    }

    public static String generateNewToken(SignedJWT jwt) throws OrgApiSecurityException{
        long tokenId = getTokenIdClaim(jwt);
        String userName = getTokenUserNameClaim(jwt);
        String orgName = getTokenOrgNameClaim(jwt);
        Set<String> roles = getTokenRolesClaim(jwt);
        String schema = getTokenSchemaClaim(jwt);
        long orgId = getTokenOrgIdClaim(jwt);
        long userId = getTokenUserIdClaim(jwt);
        Date expiration = generateNewExpiration();
        return generateNewToken(tokenId, userName, orgName, userId, orgId, schema, roles, expiration);
    }

    static String generateNewToken(long tokenId, String userName, String orgName, long userId, long orgId, String schemaName,
                                   Set<String> roles, Date expiration) throws OrgApiSecurityException{
        String token = null;
        try{
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(combineUserNameOrgName(userName, orgName))
                    .issueTime(LegacyDateConverter.convertLocalDateTimeToDate(LocalDateTime.now()))
                    .issuer(ServerCore.getProperty(ServerProps.API_NAME))
                    .expirationTime(expiration)
                    .jwtID("" + tokenId)
                    .claim(SCHEMA_CLAIM_KEY, schemaName)
                    .claim(ROLES_CLAIM_KEY, roles)
                    .claim(ORG_ID_CLAIM_KEY, orgId)
                    .claim(USER_ID_CLAIM_KEY, userId)
                    .claim(USER_NAME_CLAIM_KEY, userName)
                    .claim(ORG_NAME_CLAIM_KEY, orgName)
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
        if(StringUtils.isEmpty(token)){
            throw new OrgApiSecurityException("Empty token String cannot be parsed");
        }

        if(token.startsWith(BEARER_PREFIX)){
            token = token.substring(7);
        }

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

    public static boolean isTokenIssuedByOrgApi(SignedJWT jwt) throws OrgApiSecurityException{
        boolean result = false;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            String issuer = claimsSet.getIssuer();
            result = ServerCore.getProperty(ServerProps.API_NAME).equals(issuer);
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to validate JSON Web Token expiration", ex);
        }

        return result;
    }

    public static boolean isTokenExpired(SignedJWT jwt) throws OrgApiSecurityException{
        boolean result = false;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            LocalDateTime expiration = LegacyDateConverter.convertDateToLocalDateTime(claimsSet.getExpirationTime());
            LocalDateTime now = LocalDateTime.now();
            result = now.compareTo(expiration) >= 0;
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to validate JSON Web Token expiration", ex);
        }

        return result;
    }

    public static String getTokenSchemaClaim(SignedJWT jwt) throws OrgApiSecurityException{
        String result = null;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            result = claimsSet.getStringClaim(SCHEMA_CLAIM_KEY);
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to retrieve token schema claim", ex);
        }

        return result;
    }

    public static Set<String> getTokenRolesClaim(SignedJWT jwt) throws OrgApiSecurityException{
        Set<String> result = null;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            List<String> rolesList = claimsSet.getStringListClaim(ROLES_CLAIM_KEY);
            if(rolesList != null && rolesList.size() > 0){
                result = new HashSet<>();
                result.addAll(rolesList);
            }
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to retrieve token roles claim", ex);
        }

        return result;
    }

    public static String getTokenSubjectClaim(SignedJWT jwt) throws OrgApiSecurityException{
        String result = null;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            result = claimsSet.getSubject();
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to retrieve token roles claim", ex);
        }

        return result;
    }

    public static LocalDateTime getTokenIssuedAtClaim(SignedJWT jwt) throws OrgApiSecurityException{
        LocalDateTime result = null;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            result = LegacyDateConverter.convertDateToLocalDateTime(claimsSet.getIssueTime());
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to retrieve token roles claim", ex);
        }

        return result;
    }

    public static LocalDateTime getTokenExpirationClaim(SignedJWT jwt) throws OrgApiSecurityException{
        LocalDateTime result = null;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            result = LegacyDateConverter.convertDateToLocalDateTime(claimsSet.getExpirationTime());
        }
        catch(ParseException ex){
            throw new OrgApiSecurityException("Unable to retrieve token roles claim", ex);
        }

        return result;
    }

    public static long getTokenIdClaim(SignedJWT jwt) throws OrgApiSecurityException{
        long result = -1;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            result = Long.parseLong(claimsSet.getJWTID());
        }
        catch(ParseException | NumberFormatException ex){
            throw new OrgApiSecurityException("Unable to retrieve token roles claim", ex);
        }

        return result;
    }

    public static long getTokenUserIdClaim(SignedJWT jwt) throws OrgApiSecurityException{
        long result = -1;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            Object userIdClaim = claimsSet.getClaim(USER_ID_CLAIM_KEY);
            if(userIdClaim != null){
                result = Long.parseLong(userIdClaim.toString());
            }
        }
        catch(ParseException | NumberFormatException ex){
            throw new OrgApiSecurityException("Unable to retrieve token userId claim", ex);
        }

        return result;
    }

    public static long getTokenOrgIdClaim(SignedJWT jwt) throws OrgApiSecurityException{
        long result = -1;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            Object orgIdClaim = claimsSet.getClaim(ORG_ID_CLAIM_KEY);
            if(orgIdClaim != null){
                result = Long.parseLong(orgIdClaim.toString());
            }
        }
        catch(ParseException | NumberFormatException ex){
            throw new OrgApiSecurityException("Unable to retrieve token orgId claim", ex);
        }

        return result;
    }

    public static String getTokenUserNameClaim(SignedJWT jwt) throws OrgApiSecurityException{
        String result = null;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            result = claimsSet.getStringClaim(USER_NAME_CLAIM_KEY);
        }
        catch(ParseException | NumberFormatException ex){
            throw new OrgApiSecurityException("Unable to retrieve token orgId claim", ex);
        }

        return result;
    }

    public static String getTokenOrgNameClaim(SignedJWT jwt) throws OrgApiSecurityException{
        String result = null;
        try{
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            result = claimsSet.getStringClaim(ORG_NAME_CLAIM_KEY);
        }
        catch(ParseException | NumberFormatException ex){
            throw new OrgApiSecurityException("Unable to retrieve token orgId claim", ex);
        }

        return result;
    }

}
