/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.selfcare.crypto;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.ejbca.util.CertTools;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 *
 * @author VUDP
 */
@Slf4j
public class Crypto {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    final public static String HASH_MD5 = "MD5";
    final public static String HASH_SHA1 = "SHA-1";
    final public static String HASH_SHA256 = "SHA-256";
    final public static String HASH_SHA384 = "SHA-384";
    final public static String HASH_SHA512 = "SHA-512";
    final public static String HASH_SHA3_384 = "SHA3-384";
    final public static String HASH_SHA3_512 = "SHA3-512";

    final public static String HASH_SHA1_ = "SHA1";
    final public static String HASH_SHA256_ = "SHA256";
    final public static String HASH_SHA384_ = "SHA384";
    final public static String HASH_SHA512_ = "SHA512";

    public static String hashPass(byte[] data) {
        return DatatypeConverter.printBase64Binary(hashData(hashData(data, HASH_SHA384), HASH_SHA384));
    }

    public static byte[] hashData(byte[] data, String algorithm) {
        byte[] result = null;
        try {
            if (algorithm.compareToIgnoreCase(HASH_MD5) == 0) {
                algorithm = HASH_MD5;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA1) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA1_) == 0) {
                algorithm = HASH_SHA1;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA256) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA256_) == 0) {
                algorithm = HASH_SHA256;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA384) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA384_) == 0) {
                algorithm = HASH_SHA384;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA512) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA512_) == 0) {
                algorithm = HASH_SHA512;
            } else {
                algorithm = HASH_SHA256;
            }
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] generateKeystore(
            String keyName,
            byte[] encPubKey,
            PrivateKey privateKey,
            String password) throws Exception {

        String subjectDn = "CN=" + keyName;
        X509Certificate selfsignCertificate = generateSelfSignCertificate(
                subjectDn, encPubKey, privateKey);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null);

        X509Certificate[] chain = new X509Certificate[1];
        chain[0] = selfsignCertificate;
        keyStore.setKeyEntry(keyName, privateKey, password.toCharArray(), chain);
        OutputStream os = new ByteArrayOutputStream();
        keyStore.store(os, password.toCharArray());
        return ((ByteArrayOutputStream) os).toByteArray();
    }

    private static X509Certificate generateSelfSignCertificate(
            String subjectDN,
            byte[] encPubKey,
            PrivateKey privateKey) throws Exception {

        X500Name issuer = new X500Name(subjectDN);
        X500Name subject = new X500Name(subjectDN);

        RDN[] rdns = subject.getRDNs();

        Calendar c = Calendar.getInstance();
        Date validFrom = c.getTime();
        c.add(Calendar.DATE, 3650);
        Date validTo = c.getTime();

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                issuer,
                new BigInteger(1, genRandomArray(16)),
                validFrom,
                validTo,
                subject,
                SubjectPublicKeyInfo.getInstance(encPubKey));

        // Authority Info Access
        GeneralName ocspLocation = new GeneralName(6, "http://yourocsp.vn/status/ocsp");
        certBuilder.addExtension(new ASN1ObjectIdentifier("1.3.6.1.5.5.7.1.1"),
                false, new AuthorityInformationAccess(X509ObjectIdentifiers.ocspAccessMethod, ocspLocation));

        // CRL Distribution Points
        String crls = "http://yourcrl.vn";
        StringTokenizer tokenizer = new StringTokenizer(crls, ";", false);
        ArrayList distpoints = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            // 6 is URI
            String uri = tokenizer.nextToken();
            GeneralName gn = new GeneralName(6, uri);
            ASN1EncodableVector vec = new ASN1EncodableVector();
            vec.add(gn);
            GeneralNames gns = GeneralNames.getInstance(new DERSequence(vec));
            DistributionPointName dpn = new DistributionPointName(0, gns);
            distpoints.add(new DistributionPoint(dpn, null, null));
        }
        if (distpoints.size() > 0) {
            CRLDistPoint ext = new CRLDistPoint((DistributionPoint[]) distpoints.toArray(new DistributionPoint[0]));
            certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.31"),
                    true, ext);
        }

        // Subject Key Identifier
        SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(
                (ASN1Sequence) new ASN1InputStream(encPubKey).readObject());
        certBuilder.addExtension(
                new ASN1ObjectIdentifier("2.5.29.14"),
                false,
                new SubjectKeyIdentifier(hashData(subjectPublicKeyInfo.getPublicKeyData().getBytes(), HASH_SHA1)));

        // Authority Key Identifier
        SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(
                (ASN1Sequence) new ASN1InputStream(encPubKey).readObject());
        certBuilder.addExtension(
                new ASN1ObjectIdentifier("2.5.29.35"),
                false,
                new AuthorityKeyIdentifier(info));

        // Basic Constraints
        certBuilder.addExtension(
                new ASN1ObjectIdentifier("2.5.29.19"),
                true,
                new BasicConstraints(false));

        // Key Usage
        certBuilder.addExtension(
                new ASN1ObjectIdentifier("2.5.29.15"),
                true,
                new KeyUsage(
                        KeyUsage.digitalSignature
                                | KeyUsage.keyEncipherment
                                | KeyUsage.dataEncipherment
                                | KeyUsage.nonRepudiation));

        // Extended Key Usage
        KeyPurposeId keyPurposeId[] = {
                KeyPurposeId.id_kp_serverAuth,
                KeyPurposeId.id_kp_clientAuth,
                KeyPurposeId.id_kp_emailProtection};
        certBuilder.addExtension(
                new ASN1ObjectIdentifier("2.5.29.37"),
                true,
                new ExtendedKeyUsage(keyPurposeId));

        // Subject Alternative Name
        certBuilder.addExtension(
                new ASN1ObjectIdentifier("2.5.29.17"),
                false,
                new GeneralNames(new GeneralName(GeneralName.rfc822Name, "vudp@mobile-id.vn")));

        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = builder.build(privateKey);
        byte[] certBytes = certBuilder.build(signer).getEncoded();
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));
        return certificate;
    }

    private static byte[] genRandomArray(int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        // TODO Auto-generated method stub
        byte[] random = new byte[size];
        new Random().nextBytes(random);
        return random;
    }

    public static X509Certificate getX509ObjectFromDer(byte[] data) throws Exception {
        X509Certificate x509 = null;
        CertificateFactory certFactoryChild = CertificateFactory
                .getInstance("X.509", "BC");
        InputStream inChild = new ByteArrayInputStream(data);
        x509 = (X509Certificate) certFactoryChild
                .generateCertificate(inChild);
        return x509;
    }

    public static X509Certificate getX509Object(String pem) throws Exception {
        X509Certificate x509 = null;
        CertificateFactory certFactoryChild = CertificateFactory
                .getInstance("X.509", "BC");
        InputStream inChild = new ByteArrayInputStream(
                getX509Der(pem));
        x509 = (X509Certificate) certFactoryChild
                .generateCertificate(inChild);
        return x509;
    }

    private static byte[] getX509Der(String base64Str)
            throws Exception {
        byte[] binary = null;
        if (base64Str.indexOf("-----BEGIN CERTIFICATE-----") != -1) {
            binary = base64Str.getBytes();
        } else {
            binary = DatatypeConverter.parseBase64Binary(base64Str);
        }
        return binary;
    }

    public static String getCRLDistributionPoint(final Certificate certificate) {
        String crlUri = null;
        try {
            crlUri = CertTools.getCrlDistributionPoint(certificate).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crlUri;
    }

    public static String getOcspUri(X509Certificate certificate) {
        String ocspUri = null;
        try {
            ASN1Object obj = getExtensionValue(certificate, X509Extension.authorityInfoAccess.getId());
            if (obj == null) {
                return null;
            }
            ASN1Sequence AccessDescriptions = (ASN1Sequence) obj;
            for (int i = 0; i < AccessDescriptions.size(); i++) {
                ASN1Sequence AccessDescription = (ASN1Sequence) AccessDescriptions.getObjectAt(i);
                if (AccessDescription.size() != 2) {
                    continue;
                } else {
                    if ((AccessDescription.getObjectAt(0) instanceof DERObjectIdentifier) && ((DERObjectIdentifier) AccessDescription.getObjectAt(0)).getId().equals("1.3.6.1.5.5.7.48.1")) {
                        String AccessLocation = getStringFromGeneralName((ASN1Object) AccessDescription.getObjectAt(1));
                        if (AccessLocation == null) {
                            return null;
                        } else {
                            return AccessLocation;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ocspUri;
    }

    private static ASN1Object getExtensionValue(X509Certificate cert, String oid) throws IOException {
        byte[] bytes = cert.getExtensionValue(oid);
        if (bytes == null) {
            return null;
        }
        ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
        ASN1OctetString octs = (ASN1OctetString) aIn.readObject();
        aIn = new ASN1InputStream(new ByteArrayInputStream(octs.getOctets()));
        return aIn.readObject();
    }

    private static String getStringFromGeneralName(ASN1Object names) throws IOException {
        DERTaggedObject taggedObject = (DERTaggedObject) names;
        return new String(ASN1OctetString.getInstance(taggedObject, false).getOctets(), "ISO-8859-1");
    }

//    ///LIB
//    public static boolean verifyRsaSignature(String hashAlg, String signAlgo, boolean isPss, byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
//        boolean hashed = false;
//        String[] sss = signAlgo.split("with");
//        if (sss.length != 2) {
//            throw new Exception("signAlgo must have format {hash}with{RSA}");
//        } else if (!sss[1].equalsIgnoreCase("RSA")) {
//            throw new Exception("signAlgo must have format {hash}with{RSA}");
//        } else {
//            if (sss[0].equalsIgnoreCase("NONE")) {
//                hashed = true;
//            } else if (!sss[0].equalsIgnoreCase(hashAlg)) {
//                throw new Exception("signAlgo not matched with HashAlgo, SignAlgo is [" + signAlgo + "], but HashAlgo is [" + hashAlg + "]");
//            }
//
//            String MGF1;
//            if (!hashed) {
//                if (!isPss) {
//                    Signature privateSignature = Signature.getInstance(hashAlg);
//                    privateSignature.initVerify(publicKey);
//                    privateSignature.update(data);
//                    return privateSignature.verify(signature);
//                } else {
//                    MGF1 = "NONEWITHRSASSA-PSS";
//                    Signature sign = Signature.getInstance("NONEWITHRSASSA-PSS", "BC");
//                    sign.initVerify(publicKey);
//                    sign.update(data);
//                    return sign.verify(signature);
//                }
//            } else if (!isPss) {
//                DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
//                AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(hashAlg);
//                DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, data);
//                byte[] hashWithOID = digestInfo.getEncoded();
//                Signature privateSignature = Signature.getInstance("NonewithRSA");
//                privateSignature.initVerify(publicKey);
//                privateSignature.update(hashWithOID);
//                return privateSignature.verify(signature);
//            } else {
//                MGF1 = "MGF1";
//                String RAWRSASSA_PSS = "NONEWITHRSASSA-PSS";
//                int saltLen = DigestFactory.getDigest(hashAlg).getDigestSize();
//                Signature sign = Signature.getInstance("NONEWITHRSASSA-PSS", "BC");
//                PSSParameterSpec pssPrams = new PSSParameterSpec(hashAlg, "MGF1", new MGF1ParameterSpec(hashAlg), saltLen, 1);
//                sign.setParameter(pssPrams);
//                sign.initVerify(publicKey);
//                sign.update(data);
//                return sign.verify(signature);
//            }
//        }
//    }
//
//    public static boolean verifyRsaSignature(String hashAlgOID, String signAlgOID, String signAlgoParam, byte[] data, byte[] signature, String certificate) throws Exception {
//        log.info("Verify AuthorizationSignature, HashOID: [" + hashAlgOID + "] SignAlgo: [" + signAlgOID + "], SignAlgoParams: [" + signAlgoParam + "]");
//        String hashAlg = HashAlgorithmOID.valueOfOID(hashAlgOID).name;
//        boolean isPss = false;
//        String signAlgoString = SignAlgo.valueOfOID(signAlgOID).algName;
//        if (!(signAlgoParam)) {
//            if (!SignAlgoParams.RSASSA_PSS.derAsn1.equals(signAlgoParam)) {
//                throw new IllegalArgumentException("Unknown SignAlgoParams: [" + signAlgoParam + "]");
//            }
//
//            isPss = true;
//        }
//
//        return verifyRsaSignature(hashAlg, signAlgoString, isPss, data, signature, getPublicKey(certificate));
//    }
}
