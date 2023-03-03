package vn.mobileid.selfcare.tse;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.KeyStore.LoadStoreParameter;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jcajce.provider.util.DigestFactory;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.ejbca.util.CertTools;
import vn.mobileid.selfcare.tse.exception.RSSP_InvalidParamException;

@Slf4j
public class Crypto {

    public static final String HASH_MD5 = "MD5";
    public static final String HASH_SHA1 = "SHA-1";
    public static final String HASH_SHA256 = "SHA-256";
    public static final String HASH_SHA384 = "SHA-384";
    public static final String HASH_SHA512 = "SHA-512";
    public static final String HASH_SHA1_ = "SHA1";
    public static final String HASH_SHA256_ = "SHA256";
    public static final String HASH_SHA384_ = "SHA384";
    public static final String HASH_SHA512_ = "SHA512";
    public static final int HASH_MD5_LEN = 16;
    public static final int HASH_MD5_LEN_PADDED = 34;
    public static final int HASH_SHA1_LEN = 20;
    public static final int HASH_SHA1_LEN_PADDED = 35;
    public static final int HASH_SHA256_LEN = 32;
    public static final int HASH_SHA256_LEN_PADDED = 51;
    public static final int HASH_SHA384_LEN = 48;
    public static final int HASH_SHA384_LEN_PADDED = 67;
    public static final int HASH_SHA512_LEN = 64;
    public static final int HASH_SHA512_LEN_PADDED = 83;
    public static final String KEY_ALGORITHM_RSA = "RSA";
    public static final String KEY_ALGORITHM_DSA = "DSA";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_UTF16LE = "UTF-16LE";
    public static final String CHARSET_UTF16BE = "UTF-16BE";
    public static final String SECURE_BLACKBOX_LICENSE = "A6FF3228BE7138FECDEC31C2C99A5AA8F210D38478CD1C257489A48892330D033BF93983DC971DBB8F6665BCB6298984EE82265EE5C4416B7EB7396E33150675C69BF663B9EAE3D2A96D8C523BF1C5A2B4A09D16A8CD905C87A05EE80726DC0491382879DC4E23DF64888841704169E5CDD8157A7A9A782211A31EBA8531406FD3AF310E3AF618070CC280E98EDB522F57C9A8A5A3BE2A60E0B55486512A44B12B014E8B3C499D082D9F84FCD62FA560C29F54513F1B76DC7B92116CE741BD17080040C65F838E4DEE7744F5D7A6257740E8077EFF01C1B57A661AD51C83D94BA962707FFAE0C25EBFDBBDF7DC5A3A92DBD8C60FCED08AF7F874F3A02805C3D7";
    public static int CERT_EXPIRED;
    public static int CERT_VALID;
    public static int CERT_NOT_YET_VALID;

    public Crypto() {
    }

    public static long crc32(String data) {
        byte[] bytes = data.getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        long checksumValue = checksum.getValue();
        return checksumValue;
    }

    public static byte[] hashData(byte[] data, String algorithm) {
        byte[] result = null;

        try {
            if (algorithm.compareToIgnoreCase("MD5") == 0) {
                algorithm = "MD5";
            } else if (algorithm.compareToIgnoreCase("SHA-1") != 0 && algorithm.compareToIgnoreCase("SHA1") != 0) {
                if (algorithm.compareToIgnoreCase("SHA-256") != 0 && algorithm.compareToIgnoreCase("SHA256") != 0) {
                    if (algorithm.compareToIgnoreCase("SHA-384") != 0 && algorithm.compareToIgnoreCase("SHA384") != 0) {
                        if (algorithm.compareToIgnoreCase("SHA-512") != 0 && algorithm.compareToIgnoreCase("SHA512") != 0) {
                            algorithm = "SHA-256";
                        } else {
                            algorithm = "SHA-512";
                        }
                    } else {
                        algorithm = "SHA-384";
                    }
                } else {
                    algorithm = "SHA-256";
                }
            } else {
                algorithm = "SHA-1";
            }

            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException var4) {
            log.error("No Such Algorithm Exception. Details: " + var4.toString());
            var4.printStackTrace();
        }

        return result;
    }

    public static byte[] hashData(InputStream stream, String algorithm) {
        byte[] result = null;

        try {
            if (algorithm.compareToIgnoreCase("MD5") == 0) {
                algorithm = "MD5";
            } else if (algorithm.compareToIgnoreCase("SHA-1") != 0 && algorithm.compareToIgnoreCase("SHA1") != 0) {
                if (algorithm.compareToIgnoreCase("SHA-256") != 0 && algorithm.compareToIgnoreCase("SHA256") != 0) {
                    if (algorithm.compareToIgnoreCase("SHA-384") != 0 && algorithm.compareToIgnoreCase("SHA384") != 0) {
                        if (algorithm.compareToIgnoreCase("SHA-512") != 0 && algorithm.compareToIgnoreCase("SHA512") != 0) {
                            algorithm = "SHA-256";
                        } else {
                            algorithm = "SHA-512";
                        }
                    } else {
                        algorithm = "SHA-384";
                    }
                } else {
                    algorithm = "SHA-256";
                }
            } else {
                algorithm = "SHA-1";
            }

            MessageDigest md = MessageDigest.getInstance(algorithm);
            DigestInputStream dis = new DigestInputStream(stream, md);
            byte[] buffer = new byte[1024];

            while(true) {
                if (dis.read(buffer) <= -1) {
                    stream.reset();
                    result = md.digest();
                    break;
                }
            }
        } catch (IOException | NoSuchAlgorithmException var6) {
            log.error("No Such Algorithm Exception. Details: " + var6.toString());
            var6.printStackTrace();
        }

        return result;
    }

    public static String hashPass(byte[] data) {
        return DatatypeConverter.printHexBinary(hashData(hashData(data, "SHA-384"), "SHA-384"));
    }

    public static byte[] hashPassToBytes(byte[] data) {
        return hashData(hashData(data, "SHA-384"), "SHA-384");
    }

    public static PublicKey getPublicKeyInPemFormat(String data) {
        PublicKey pubKeyString = null;

        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(data));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pubKeyString = kf.generatePublic(spec);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return pubKeyString;
    }

    public static PublicKey getPublicKeyInHexFormat(String data) {
        PublicKey pubKeyString = null;

        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(DatatypeConverter.parseHexBinary(data));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pubKeyString = kf.generatePublic(spec);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return pubKeyString;
    }

    public static X509Certificate getX509Object(String pem) {
        X509Certificate x509 = null;

        try {
            CertificateFactory certFactoryChild = CertificateFactory.getInstance("X.509", "BC");
            InputStream inChild = new ByteArrayInputStream(getX509Der(pem));
            x509 = (X509Certificate)certFactoryChild.generateCertificate(inChild);
        } catch (Exception var4) {
            log.error("Error occurs when generate certificate, caused by ", var4);
        }

        return x509;
    }

    public static X509Certificate getX509Object(String pem, String provider) {
        X509Certificate x509 = null;

        try {
            CertificateFactory certFactoryChild = CertificateFactory.getInstance("X.509", provider);
            InputStream inChild = new ByteArrayInputStream(getX509Der(pem));
            x509 = (X509Certificate)certFactoryChild.generateCertificate(inChild);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return x509;
    }

    public static byte[] getX509CertificateEncoded(X509Certificate x509) {
        byte[] data = null;

        try {
            data = x509.getEncoded();
        } catch (Exception var3) {
            var3.printStackTrace();
            log.error("Error while getting X509Certificate encoded data");
        }

        return data;
    }

    public static byte[] getPublicKeyEncoded(X509Certificate x509) {
        byte[] data = null;

        try {
            data = x509.getPublicKey().getEncoded();
        } catch (Exception var3) {
            var3.printStackTrace();
            log.error("Error while getting X509Certificate encoded data");
        }

        return data;
    }

    public static PublicKey getPublicKey(String cert) {
        X509Certificate x509 = getX509Object(cert);
        return x509.getPublicKey();
    }

    public static int checkCertificateValidity(X509Certificate x509) {
        int status;
        try {
            x509.checkValidity();
            status = CERT_VALID;
        } catch (CertificateExpiredException var3) {
            log.error("Error occurs when validity the certificate, caused by ", var3);
            status = CERT_EXPIRED;
        } catch (CertificateNotYetValidException var4) {
            log.error("Error occurs when validity the certificate, caused by ", var4);
            status = CERT_NOT_YET_VALID;
        }

        return status;
    }

    private static byte[] getX509Der(String base64Str) throws Exception {
        byte[] binary = null;
        if (base64Str.indexOf("-----BEGIN CERTIFICATE-----") != -1) {
            binary = base64Str.getBytes();
        } else {
            binary = DatatypeConverter.parseBase64Binary(base64Str);
        }

        return binary;
    }

    public static SecretKey computeSecretKey(String keyType, byte[] rawSecretKey) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(rawSecretKey, keyType);
        return secretKeySpec;
    }

    public static byte[] wrapSecrectKey(String algWrapping, SecretKey wrappingKey, byte[] wrappingIv, Key keyToBeWrapped) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidParameterSpecException, IllegalBlockSizeException, NoSuchProviderException {
        log.debug("Wrapping AsymmetricKey (algWrapping): " + algWrapping);
        Cipher wrappingCipher = Cipher.getInstance(algWrapping);
        String[] list = algWrapping.split("/");
        AlgorithmParameters algParams = AlgorithmParameters.getInstance(list[0]);
        algParams.init(new IvParameterSpec(wrappingIv));
        wrappingCipher.init(3, wrappingKey, algParams);
        return wrappingCipher.wrap(keyToBeWrapped);
    }

    public static Key unwrapSecrectKey(String algWrap, String wrappedKeyAlgorithm, SecretKey wrappingKey, byte[] wrappingIv, byte[] wrappedKey, int wrappedKeyType) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidParameterSpecException, IllegalBlockSizeException, NoSuchProviderException {
        log.debug("Unwrapping AsymmetricKey (algWrap/wrappedKeyAlgorithm/wrappedKeyType): " + algWrap + "/" + wrappedKeyAlgorithm + "/" + wrappedKeyType);
        Cipher wrappingCipher = Cipher.getInstance(algWrap);
        String[] list = algWrap.split("/");
        AlgorithmParameters algParams = AlgorithmParameters.getInstance(list[0]);
        algParams.init(new IvParameterSpec(wrappingIv));
        wrappingCipher.init(4, wrappingKey, algParams);
        return wrappingCipher.unwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
    }

    public static byte[] encrypt(String encryptType, SecretKey key, byte[] initVector, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        IvParameterSpec iv = new IvParameterSpec(initVector);
        Cipher cipher = Cipher.getInstance(encryptType);
        cipher.init(1, key, iv);
        byte[] encrypted = cipher.doFinal(data);
        return encrypted;
    }

    public static byte[] decrypt(String encryptType, SecretKey key, byte[] initVector, byte[] encoded) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        IvParameterSpec iv = new IvParameterSpec(initVector);
        Cipher cipher = Cipher.getInstance(encryptType);
        cipher.init(2, key, iv);
        byte[] data = cipher.doFinal(encoded);
        return data;
    }

    public static List<Certificate> getCertificateChain(String caCert1, String caCert2, X509Certificate cert) {
        X509Certificate endCert = null;
        X509Certificate ca1 = null;
        X509Certificate ca2 = null;
        endCert = cert;
        ca1 = getX509Object(caCert1);

        try {
            endCert.verify(ca1.getPublicKey());
            Collection<Certificate> certChain = CertTools.getCertsFromPEM(new ByteArrayInputStream(caCert1.getBytes()));
            List<Certificate> certificates = new ArrayList();
            certificates.add(endCert);
            certificates.addAll(certChain);
            return certificates;
        } catch (Exception var10) {
            log.warn("First CA certificate isn't the one who issues end-user certificate. Try the second one");
            ca2 = getX509Object(caCert2);

            try {
                endCert.verify(ca2.getPublicKey());
                Collection<Certificate> certChain = CertTools.getCertsFromPEM(new ByteArrayInputStream(caCert2.getBytes()));
                List<Certificate> certificates = new ArrayList();
                certificates.add(endCert);
                certificates.addAll(certChain);
                return certificates;
            } catch (Exception var9) {
                var9.printStackTrace();
                return null;
            }
        }
    }

    public static String sign(String data, String keystorePath, String keystorePassword, String keystoreType) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance(keystoreType);
        InputStream is = new FileInputStream(keystorePath);
        Throwable var7 = null;

        try {
            keystore.load(is, keystorePassword.toCharArray());
            Enumeration<String> e = keystore.aliases();
            PrivateKey key = null;

            while(true) {
                if (e.hasMoreElements()) {
                    String aliasName = (String)e.nextElement();
                    key = (PrivateKey)keystore.getKey(aliasName, keystorePassword.toCharArray());
                    if (key == null) {
                        continue;
                    }
                }

                Signature sig = Signature.getInstance("SHA1withRSA");
                sig.initSign(key);
                sig.update(data.getBytes());
                return DatatypeConverter.printBase64Binary(sig.sign());
            }
        } catch (Throwable var18) {
            var7 = var18;
            throw var18;
        } finally {
            if (is != null) {
                if (var7 != null) {
                    try {
                        is.close();
                    } catch (Throwable var17) {
                        var7.addSuppressed(var17);
                    }
                } else {
                    is.close();
                }
            }

        }
    }

    public static String sign(String data, String keystr, String mimeType) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        PrivateKey key = getPrivateKeyFromString(keystr, mimeType);
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return DatatypeConverter.printBase64Binary(sig.sign());
    }

    public static PrivateKey getPrivateKeyFromString(String key, String mimeType) throws IOException, GeneralSecurityException {
        byte[] encoded = null;
        if (mimeType.toLowerCase().contains("base64")) {
            String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----\n", "");
            privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
            encoded = DatatypeConverter.parseBase64Binary(privateKeyPEM);
        } else {
            encoded = DatatypeConverter.parseHexBinary(key);
        }

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        PrivateKey privKey = kf.generatePrivate(keySpec);
        return privKey;
    }

    public static boolean[] getKeyUsage(X509Certificate x509) {
        return x509.getKeyUsage();
    }

    public static int getBasicConstraint(X509Certificate x509) {
        return x509.getBasicConstraints();
    }

    public static byte[] padSHA1Oid(byte[] hashedData) throws Exception {
        DERObjectIdentifier sha1oid_ = new DERObjectIdentifier("1.3.14.3.2.26");
        AlgorithmIdentifier sha1aid_ = new AlgorithmIdentifier(sha1oid_, (ASN1Encodable)null);
        DigestInfo di = new DigestInfo(sha1aid_, hashedData);
        byte[] plainSig = di.getEncoded("DER");
        return plainSig;
    }

    public static boolean checkCertificateRelation(String childCert, String parentCert) {
        boolean isOk = false;

        try {
            CertificateFactory certFactoryChild = CertificateFactory.getInstance("X.509", "BC");
            InputStream inChild = new ByteArrayInputStream(getX509Der(childCert));
            X509Certificate certChild = (X509Certificate)certFactoryChild.generateCertificate(inChild);
            CertificateFactory certFactoryParent = CertificateFactory.getInstance("X.509", "BC");
            InputStream inParent = new ByteArrayInputStream(getX509Der(parentCert));
            X509Certificate certParent = (X509Certificate)certFactoryParent.generateCertificate(inParent);
            certChild.verify(certParent.getPublicKey());
            isOk = true;
        } catch (SignatureException var9) {
            log.error("Invalid certficate. Signature exception");
        } catch (CertificateException var10) {
            log.error("Invalid certficate. Certificate exception");
        } catch (Exception var11) {
            log.error("Invalid certficate. Something wrong exception");
        }

        return isOk;
    }

    public static boolean checkCertificateRelation(X509Certificate childCert, X509Certificate parentCert) {
        boolean isOk = false;

        try {
            childCert.verify(parentCert.getPublicKey());
            isOk = true;
        } catch (SignatureException var4) {
            log.error("Invalid certficate. Signature exception");
            var4.printStackTrace();
        } catch (CertificateException var5) {
            log.error("Invalid certficate. Certificate exception");
            var5.printStackTrace();
        } catch (Exception var6) {
            log.error("Invalid certficate. Something wrong exception");
            var6.printStackTrace();
        }

        return isOk;
    }

    public static boolean checkCertificateAndCsr(String certificate, String csr) {
        boolean isOk = false;

        try {
            X509Certificate x509Certificate = getX509Object(certificate);
            byte[] certPubkeyHash = hashData(x509Certificate.getPublicKey().getEncoded(), "SHA-1");
            PKCS10CertificationRequest pKCS10CertificationRequest = new PKCS10CertificationRequest(Base64.getMimeDecoder().decode(csr));
            byte[] csrPubkeyHash = hashData(pKCS10CertificationRequest.getPublicKey().getEncoded(), "SHA-1");
            if (Arrays.equals(certPubkeyHash, csrPubkeyHash)) {
                isOk = true;
            } else {
                isOk = false;
            }
        } catch (NoSuchAlgorithmException var7) {
            log.error("Invalid certficate. NoSuchAlgorithmExceptionn");
            var7.printStackTrace();
        } catch (NoSuchProviderException var8) {
            log.error("Invalid certficate. NoSuchAlgorithmException");
            var8.printStackTrace();
        } catch (InvalidKeyException var9) {
            log.error("Invalid certficate. InvalidKeyException");
            var9.printStackTrace();
        }

        return isOk;
    }

    public static String encryptRSA(String message, PublicKey publicKey) {
        String result = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, publicKey);
            result = DatatypeConverter.printBase64Binary(cipher.doFinal(message.getBytes()));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static String decryptRSA(String message, PrivateKey privateKey) {
        String result = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, privateKey);
            result = new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(message)));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static boolean validateHashData(String hash) {
        if (hash.length() % 2 != 0) {
            log.error("Invalid HashData=" + hash + " modulus of 2 should be ZERO");
            return false;
        } else {
            byte[] binraryHash = DatatypeConverter.parseHexBinary(hash);
            if (binraryHash.length > 83) {
                log.error("Hash length is greater than 64 bytes. Wtf?");
                return false;
            } else {
                return true;
            }
        }
    }

    public static String getHashAlgorithm(byte[] hashData) {
        int len = hashData.length;
        switch(len) {
            case 16:
                return "MD5";
            case 20:
                return "SHA-1";
            case 32:
                return "SHA-256";
            case 34:
                return "MD5";
            case 35:
                return "SHA-1";
            case 48:
                return "SHA-384";
            case 51:
                return "SHA-256";
            case 64:
                return "SHA-512";
            case 67:
                return "SHA-384";
            case 83:
                return "SHA-512";
            default:
                return "SHA-1";
        }
    }


    public static byte[] getBytes(String data, String charset) {
        byte[] bytes;
        try {
            bytes = data.getBytes(charset);
        } catch (Exception var4) {
            var4.printStackTrace();
            log.error("Invalid charset " + charset + ". Using the default one. It maybe got the unicode issue");
            bytes = data.getBytes();
        }

        return bytes;
    }

    public static String generatePKCS1Signature(String data, String keyStorePath, String keyStorePassword, String keystoreType) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance(keystoreType);
        InputStream is = new FileInputStream(keyStorePath);
        Throwable var7 = null;

        try {
            keystore.load(is, keyStorePassword.toCharArray());
            Enumeration<String> e = keystore.aliases();
            PrivateKey key = null;

            while(true) {
                if (e.hasMoreElements()) {
                    String aliasName = (String)e.nextElement();
                    key = (PrivateKey)keystore.getKey(aliasName, keyStorePassword.toCharArray());
                    if (key == null) {
                        continue;
                    }
                }

                Signature sig = Signature.getInstance("SHA1withRSA");
                sig.initSign(key);
                sig.update(data.getBytes());
                return DatatypeConverter.printBase64Binary(sig.sign());
            }
        } catch (Throwable var18) {
            var7 = var18;
            throw var18;
        } finally {
            if (is != null) {
                if (var7 != null) {
                    try {
                        is.close();
                    } catch (Throwable var17) {
                        var7.addSuppressed(var17);
                    }
                } else {
                    is.close();
                }
            }

        }
    }

    public static PublicKey computePublicKey(BigInteger modulus, BigInteger exponent) {
        PublicKey pubKey = null;

        try {
            pubKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return pubKey;
    }

    public static byte[] paddingSHA1OID(byte[] hashedData) throws Exception {
        log.debug("Padding SHA-1 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find("SHA-1");
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingSHA256OID(byte[] hashedData) throws Exception {
        log.debug("Padding SHA-256 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find("SHA-256");
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingSHA384OID(byte[] hashedData) throws Exception {
        log.debug("Padding SHA-384 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find("SHA-384");
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingSHA512OID(byte[] hashedData) throws Exception {
        log.debug("Padding SHA-512 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find("SHA-512");
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingMD5OID(byte[] hashedData) throws Exception {
        log.debug("Padding MD5 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find("MD5");
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] generateKeystore(String keyName, byte[] encPubKey, PrivateKey privateKey, String password) throws Exception {
        String subjectDn = "CN=" + keyName;
        X509Certificate selfsignCertificate = generateSelfSignCertificate(subjectDn, encPubKey, privateKey);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load((LoadStoreParameter)null);
        X509Certificate[] chain = new X509Certificate[]{selfsignCertificate};
        keyStore.setKeyEntry(keyName, privateKey, password.toCharArray(), chain);
        OutputStream os = new ByteArrayOutputStream();
        keyStore.store(os, password.toCharArray());
        return ((ByteArrayOutputStream)os).toByteArray();
    }

    private static X509Certificate generateSelfSignCertificate(String subjectDN, byte[] encPubKey, PrivateKey privateKey) throws Exception {
        X500Name issuer = new X500Name(subjectDN);
        X500Name subject = new X500Name(subjectDN);
        RDN[] rdns = subject.getRDNs();
        Calendar c = Calendar.getInstance();
        Date validFrom = c.getTime();
        c.add(5, 3650);
        Date validTo = c.getTime();
        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, new BigInteger(1, Utils.genRandomArray(16)), validFrom, validTo, subject, SubjectPublicKeyInfo.getInstance(encPubKey));
        GeneralName ocspLocation = new GeneralName(6, "http://mobile-id.vn:81/ejbca/publicweb/status/ocsp");
        certBuilder.addExtension(new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1"), false, new AuthorityInformationAccess(X509ObjectIdentifiers.ocspAccessMethod, ocspLocation));
        String crls = "https://mobile-id.vn/crl/Mobile-ID.crl";
        StringTokenizer tokenizer = new StringTokenizer(crls, ";", false);
        ArrayList distpoints = new ArrayList();

        while(tokenizer.hasMoreTokens()) {
            String uri = tokenizer.nextToken();
            GeneralName gn = new GeneralName(6, uri);
            ASN1EncodableVector vec = new ASN1EncodableVector();
            vec.add(gn);
            GeneralNames gns = GeneralNames.getInstance(new DERSequence(vec));
            DistributionPointName dpn = new DistributionPointName(0, gns);
            distpoints.add(new DistributionPoint(dpn, (ReasonFlags)null, (GeneralNames)null));
        }

        if (distpoints.size() > 0) {
            CRLDistPoint ext = new CRLDistPoint((DistributionPoint[])((DistributionPoint[])distpoints.toArray(new DistributionPoint[0])));
            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.31"), true, ext);
        }

        SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo((ASN1Sequence)(new ASN1InputStream(encPubKey)).readObject());
        certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.14"), false, new SubjectKeyIdentifier(hashData(subjectPublicKeyInfo.getPublicKeyData().getBytes(), "SHA-1")));
        SubjectPublicKeyInfo info = new SubjectPublicKeyInfo((ASN1Sequence)(new ASN1InputStream(encPubKey)).readObject());
        certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.35"), false, new AuthorityKeyIdentifier(info));
        certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, new BasicConstraints(false));
        certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.15"), true, new KeyUsage(240));
        KeyPurposeId[] keyPurposeId = new KeyPurposeId[]{KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth, KeyPurposeId.id_kp_emailProtection};
        certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.37"), true, new ExtendedKeyUsage(keyPurposeId));
        certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.17"), false, new GeneralNames(new GeneralName(1, "vudp@mobile-id.vn")));
        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = builder.build(privateKey);
        byte[] certBytes = certBuilder.build(signer).getEncoded();
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
        return certificate;
    }

    public static String getPKCS1Signature(String data, String relyingPartyKeyStore, String relyingPartyKeyStorePassword) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream(relyingPartyKeyStore);
        keystore.load(is, relyingPartyKeyStorePassword.toCharArray());
        Enumeration<String> e = keystore.aliases();

        String aliasName;
        for(aliasName = ""; e.hasMoreElements(); aliasName = (String)e.nextElement()) {
        }

        PrivateKey key = (PrivateKey)keystore.getKey(aliasName, relyingPartyKeyStorePassword.toCharArray());
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return DatatypeConverter.printBase64Binary(sig.sign());
    }

    public static byte[] md5(byte[] data) {
        byte[] result = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException var3) {
            log.error("No Such Algorithm Exception. Details: " + var3.toString());
            var3.printStackTrace();
        }

        return result;
    }

    public static String getCRLDistributionPoint(Certificate certificate) {
        String crlUri = null;

        try {
            crlUri = CertTools.getCrlDistributionPoint(certificate).toString();
        } catch (Exception var3) {
            log.error("Error while getting CRL URI. Details: " + var3.toString());
            var3.printStackTrace();
        }

        return crlUri;
    }

    public static String getOcspUri(X509Certificate certificate) {
        Object ocspUri = null;

        try {
            ASN1Object obj = getExtensionValue(certificate, X509Extension.authorityInfoAccess.getId());
            if (obj == null) {
                return null;
            }

            ASN1Sequence AccessDescriptions = (ASN1Sequence)obj;

            for(int i = 0; i < AccessDescriptions.size(); ++i) {
                ASN1Sequence AccessDescription = (ASN1Sequence)AccessDescriptions.getObjectAt(i);
                if (AccessDescription.size() == 2 && AccessDescription.getObjectAt(0) instanceof DERObjectIdentifier && ((DERObjectIdentifier)AccessDescription.getObjectAt(0)).getId().equals("1.3.6.1.5.5.7.48.1")) {
                    String AccessLocation = getStringFromGeneralName((ASN1Object)AccessDescription.getObjectAt(1));
                    if (AccessLocation == null) {
                        return null;
                    }

                    return AccessLocation;
                }
            }
        } catch (Exception var7) {
            log.error("Error while getting OCSP URI. Details: " + var7.toString());
            var7.printStackTrace();
        }

        return (String)ocspUri;
    }

    private static ASN1Object getExtensionValue(X509Certificate cert, String oid) throws IOException {
        byte[] bytes = cert.getExtensionValue(oid);
        if (bytes == null) {
            return null;
        } else {
            ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
            ASN1OctetString octs = (ASN1OctetString)aIn.readObject();
            aIn = new ASN1InputStream(new ByteArrayInputStream(octs.getOctets()));
            return aIn.readObject();
        }
    }

    private static String getStringFromGeneralName(ASN1Object names) throws IOException {
        DERTaggedObject taggedObject = (DERTaggedObject)names;
        return new String(ASN1OctetString.getInstance(taggedObject, false).getOctets(), "ISO-8859-1");
    }

    public static boolean verifyRsaSignature(String hashAlg, String signAlgo, boolean isPss, byte[] data, byte[] signature, PublicKey publicKey) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException, RSSP_InvalidParamException {
        boolean hashed = false;
        String[] sss = signAlgo.split("with");
        if (sss.length != 2) {
            throw new RSSP_InvalidParamException("signAlgo must have format {hash}with{RSA}");
        } else if (!sss[1].equalsIgnoreCase("RSA")) {
            throw new RSSP_InvalidParamException("signAlgo must have format {hash}with{RSA}");
        } else {
            if (sss[0].equalsIgnoreCase("NONE")) {
                hashed = true;
            } else if (!sss[0].equalsIgnoreCase(hashAlg)) {
                throw new RSSP_InvalidParamException("signAlgo not matched with HashAlgo, SignAlgo is [" + signAlgo + "], but HashAlgo is [" + hashAlg + "]");
            }

            String MGF1;
            if (!hashed) {
                if (!isPss) {
                    Signature privateSignature = Signature.getInstance(hashAlg);
                    privateSignature.initVerify(publicKey);
                    privateSignature.update(data);
                    return privateSignature.verify(signature);
                } else {
                    MGF1 = "NONEWITHRSASSA-PSS";
                    Signature sign = Signature.getInstance("NONEWITHRSASSA-PSS", "BC");
                    sign.initVerify(publicKey);
                    sign.update(data);
                    return sign.verify(signature);
                }
            } else if (!isPss) {
                DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
                AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(hashAlg);
                DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, data);
                byte[] hashWithOID = digestInfo.getEncoded();
                Signature privateSignature = Signature.getInstance("NonewithRSA");
                privateSignature.initVerify(publicKey);
                privateSignature.update(hashWithOID);
                return privateSignature.verify(signature);
            } else {
                MGF1 = "MGF1";
                String RAWRSASSA_PSS = "NONEWITHRSASSA-PSS";
                int saltLen = DigestFactory.getDigest(hashAlg).getDigestSize();
                Signature sign = Signature.getInstance("NONEWITHRSASSA-PSS", "BC");
                PSSParameterSpec pssPrams = new PSSParameterSpec(hashAlg, "MGF1", new MGF1ParameterSpec(hashAlg), saltLen, 1);
                sign.setParameter(pssPrams);
                sign.initVerify(publicKey);
                sign.update(data);
                return sign.verify(signature);
            }
        }
    }

    public static boolean verifyRsaSignature(String hashAlgOID, String signAlgOID, String signAlgoParam, byte[] data, byte[] signature, String certificate) throws Exception {
        log.info("Verify AuthorizationSignature, HashOID: [" + hashAlgOID + "] SignAlgo: [" + signAlgOID + "], SignAlgoParams: [" + signAlgoParam + "]");
        String hashAlg = HashAlgorithmOID.valueOfOID(hashAlgOID).name;
        boolean isPss = false;
        String signAlgoString = SignAlgo.valueOfOID(signAlgOID).algName;
        if (!Utils.isNullOrEmpty(signAlgoParam)) {
            if (!SignAlgoParams.RSASSA_PSS.derAsn1.equals(signAlgoParam)) {
                throw new IllegalArgumentException("Unknown SignAlgoParams: [" + signAlgoParam + "]");
            }

            isPss = true;
        }

        return verifyRsaSignature(hashAlg, signAlgoString, isPss, data, signature, getPublicKey(certificate));
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
        CERT_EXPIRED = 1;
        CERT_VALID = 0;
        CERT_NOT_YET_VALID = -1;
    }
}
