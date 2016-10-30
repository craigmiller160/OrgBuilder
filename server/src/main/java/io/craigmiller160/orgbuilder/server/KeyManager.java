package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
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
            InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream(ServerCore.getProperty(ServerProps.KEYSTORE_PATH));
            keyStore = KeyStore.getInstance(ServerCore.getProperty(ServerProps.KEYSTORE_TYPE));
            keyStore.load(keystoreStream, ServerCore.getProperty(ServerProps.KEYSTORE_PASS).toCharArray());
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
            privateKey = (PrivateKey) keystore.getKey(ServerCore.getProperty(ServerProps.TOKEN_KEY_NAME),
                    ServerCore.getProperty(ServerProps.TOKEN_KEY_PASS).toCharArray());
        }
        catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex){
            throw new OrgApiSecurityException("Unable to retrieve Token PrivateKey from keystore", ex);
        }

        return privateKey;
    }

    public PublicKey getTokenPublicKey() throws OrgApiSecurityException{
        PublicKey publicKey = null;
        try{
            Certificate cert = keystore.getCertificate(ServerCore.getProperty(ServerProps.TOKEN_KEY_NAME));
            publicKey = cert.getPublicKey();
        }
        catch(KeyStoreException ex){
            throw new OrgApiSecurityException("Unable to retrieve Token PublicKey from keystore", ex);
        }

        return publicKey;
    }

    public SecretKey getDataSecretKey() throws OrgApiSecurityException{
        SecretKey secretKey = null;
        try{
            secretKey = (SecretKey) keystore.getKey(ServerCore.getProperty(ServerProps.DATA_KEY_NAME),
                    ServerCore.getProperty(ServerProps.DATA_KEY_PASS).toCharArray());
        }
        catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex){
            throw new OrgApiSecurityException("Unable to retrieve Data SecretKey from keystore", ex);
        }

        return secretKey;
    }

    public Certificate getCACertificate() throws OrgApiSecurityException{
        Certificate certificate = null;
        try{
            certificate = keystore.getCertificate(ServerCore.getProperty(ServerProps.CA_CERT_NAME));
        }
        catch(KeyStoreException ex){
            throw new OrgApiSecurityException("Unable to retrieve CA Certificate from keystore", ex);
        }

        return certificate;
    }

}
