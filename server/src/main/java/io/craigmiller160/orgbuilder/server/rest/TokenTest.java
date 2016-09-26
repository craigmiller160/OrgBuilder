package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by craigmiller on 9/25/16.
 */
public class TokenTest {

    public static void main(String[] args) throws Exception{
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(1024);
//
//        KeyPair keyPair = keyGen.generateKeyPair();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        File f = new File("./server/orgkeystore");
        System.out.println(f.getAbsolutePath());
        System.out.println(f.exists());

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(f), "".toCharArray());

        Key key = keyStore.getKey("orgapikey", "".toCharArray());
        KeyPair keyPair = null;
        if(key instanceof PrivateKey){
            java.security.cert.Certificate cert = keyStore.getCertificate("orgapikey");
            PublicKey publicKey = cert.getPublicKey();
            keyPair = new KeyPair(publicKey, (PrivateKey) key);
        }

        JWSSigner signer = new RSASSASigner(keyPair.getPrivate());
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), new Payload("In RSA we trust!"));
        jwsObject.sign(signer);

        String s = jwsObject.serialize();
        String[] parts = s.split("\\.");
        System.out.println(org.glassfish.jersey.internal.util.Base64.decodeAsString(parts[0]));
        System.out.println(org.glassfish.jersey.internal.util.Base64.decodeAsString(parts[1]));
        System.out.println(parts[2]);

        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) keyPair.getPublic());
        JWSObject toBeVerified = JWSObject.parse(s);
        System.out.println(toBeVerified.verify(verifier));
        System.out.println(toBeVerified.getPayload().toString());

        SignedJWT signedJWT = SignedJWT.parse(s);
        System.out.println(signedJWT.verify(verifier));
        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
        JWKSource source;

        JWTClaimsSet claimSet = jwtProcessor.process(signedJWT, null);
    }

}
