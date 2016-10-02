package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;

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

/**
 * Created by craig on 10/1/16.
 */
public class KeyManager {

    /**
     * The KeyStore for the JWT signing/verifying keys.
     * Please note that this KeyStore is only thread safe
     * if it's state is never changed during the application
     * runtime.
     */
    private final KeyStore keystore;

    public KeyManager() throws OrgApiSecurityException{
        this.keystore = loadKeyStore();
    }

    private KeyStore loadKeyStore() throws OrgApiSecurityException{
        KeyStore keyStore = null;
        try{
            String keystorePath = ServerCore.getProperty(ServerProps.KEYSTORE_PATH);
            String keystorePass = ServerCore.getProperty(ServerProps.KEYSTORE_PASS);
            String keystoreType = ServerCore.getProperty(ServerProps.KEYSTORE_TYPE);

            InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream(keystorePath);
            keyStore = KeyStore.getInstance(keystoreType);
            keyStore.load(keystoreStream, keystorePass.toCharArray());
        }
        catch(KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex){
            throw new OrgApiSecurityException("Unable to load KeyStore", ex);
        }

        return keyStore;
    }

    public KeyStore getKeyStore(){
        return keystore;
    }

    public PrivateKey getTokenPrivateKey() throws OrgApiSecurityException{
        PrivateKey privateKey = null;
        try{
            String keystorePass = ServerCore.getProperty(ServerProps.KEYSTORE_PASS);
            String tokenKeyName = ServerCore.getProperty(ServerProps.TOKEN_KEY_NAME);
            privateKey = (PrivateKey) keystore.getKey(tokenKeyName, keystorePass.toCharArray());
        }
        catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex){
            throw new OrgApiSecurityException("Unable to retrieve Token PrivateKey from keystore", ex);
        }

        return privateKey;
    }

    public PublicKey getTokenPublicKey() throws OrgApiSecurityException{
        PublicKey publicKey = null;
        try{
            String keystoreKeyName = ServerCore.getProperty(ServerProps.TOKEN_KEY_NAME);
            Certificate cert = keystore.getCertificate(keystoreKeyName);
            publicKey = cert.getPublicKey();
        }
        catch(KeyStoreException ex){
            throw new OrgApiSecurityException("Unable to retrieve Token PublicKey from keystore", ex);
        }

        return publicKey;
    }


}
