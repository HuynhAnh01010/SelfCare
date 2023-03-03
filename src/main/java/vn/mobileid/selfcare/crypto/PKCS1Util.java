/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.selfcare.crypto;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author TRUONG.NNT
 */
@Slf4j
public class PKCS1Util {

    private static PKCS1Util instance;
    
    private static ReentrantLock lock = new ReentrantLock();
    
    private PrivateKey privateKey;

    /**
     *
     */
    private PKCS1Util(String keystorePath, String psw) throws Exception {
        // TODO Auto-generated constructor stub
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream(keystorePath);
        keystore.load(is, psw.toCharArray());
        
        Enumeration<String> e = keystore.aliases();
        String aliasName = "";
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
        }
        privateKey = (PrivateKey) keystore.getKey(aliasName, psw.toCharArray());
        is.close();
    }

    /**
     * @param keystore
     * @param psw
     * @return the instance
     */
    public static PKCS1Util getInstance(String keystore, String psw) {
        if (instance != null) {
            return instance;
        }
        
        lock.lock();
        try {
            if (instance != null) {
                return instance;
            }
            
            instance = new PKCS1Util(keystore, psw);
            
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();
        }
        
        return instance;
    }
    
    public String getPKCS1Signature(String data) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return DatatypeConverter.printBase64Binary(signature.sign());
    }
    
    public static String getPKCS1Signature(String data, InputStream relyingPartyKeyStore, String relyingPartyKeyStorePassword) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = null;
        try {
//            is = Utils.getInputStream(relyingPartyKeyStore);//new FileInputStream(relyingPartyKeyStore);
            keystore.load(relyingPartyKeyStore, relyingPartyKeyStorePassword.toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Eror when load Keystore-file, caused by {}", ex.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }
        
        Enumeration<String> e = keystore.aliases();
        String aliasName = "";
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
        }
        PrivateKey key = (PrivateKey) keystore.getKey(aliasName,
                relyingPartyKeyStorePassword.toCharArray());

        //for test wrong pkcs1Signature
        //KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        //keyGen.initialize(512);
        //PrivateKey key = keyGen.genKeyPair().getPrivate();
        //END for test wrong pkcs1Signature
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return DatatypeConverter.printBase64Binary(sig.sign());
    }
}
