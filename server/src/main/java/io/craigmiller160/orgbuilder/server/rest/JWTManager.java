package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.util.LegacyDateConverter;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A utility class for handling the JSON Web Tokens.
 *
 * Created by craig on 10/1/16.
 */
public class JWTManager {

    public static final String SCHEMA_CLAIM_KEY = "SMA";
    public static final String ROLE_CLAIM_KEY = "ROL";

    /**
     * The KeyStore for the JWT signing/verifying keys.
     * Please note that this KeyStore is only thread safe
     * if it's state is never changed during the application
     * runtime.
     */
    private final KeyStore keystore;

    public JWTManager() throws OrgApiSecurityException{
        this.keystore = loadKeyStore();
    }

    private KeyStore loadKeyStore() throws OrgApiSecurityException{
        KeyStore keyStore = null;
        try{
            String keystorePath = ServerCore.getProperty(ServerProps.KEYSTORE_PATH);
            String keystorePass = ServerCore.getProperty(ServerProps.KEYSTORE_PASS);

            InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream(keystorePath);
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keystoreStream, keystorePass.toCharArray());
        }
        catch(KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex){
            throw new OrgApiSecurityException("Unable to load KeyStore", ex);
        }

        return keyStore;
    }

    private PrivateKey getApiPrivateKey() throws OrgApiSecurityException{
        PrivateKey privateKey = null;
        try{
            String keystorePass = ServerCore.getProperty(ServerProps.KEYSTORE_PASS);
            String keystoreKeyName = ServerCore.getProperty(ServerProps.KEYSTORE_KEY_NAME);
            privateKey = (PrivateKey) keystore.getKey(keystoreKeyName, keystorePass.toCharArray());
        }
        catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex){
            throw new OrgApiSecurityException("Unable to retrieve PrivateKey from keystore", ex);
        }

        return privateKey;
    }

    private PublicKey getApiPublicKey() throws OrgApiSecurityException{
        PublicKey publicKey = null;
        try{
            String keystoreKeyName = ServerCore.getProperty(ServerProps.KEYSTORE_KEY_NAME);
            Certificate cert = keystore.getCertificate(keystoreKeyName);
            publicKey = cert.getPublicKey();
        }
        catch(KeyStoreException ex){
            throw new OrgApiSecurityException("Unable to retrieve PublicKey from keystore", ex);
        }

        return publicKey;
    }

    public String generateNewToken(long tokenId, String username, Set<String> roles, String schema) throws OrgApiSecurityException{
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
            JWSSigner signer = new RSASSASigner(getApiPrivateKey());
            jwt.sign(signer);
            token = jwt.serialize();
        }
        catch(JOSEException ex){
            throw new OrgApiSecurityException("Unable to sign JSON Web Token", ex);
        }

        return token;
    }



}
