package vn.mobileid.selfcare.tse;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.jcajce.provider.util.DigestFactory;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class TSECryptoHelper {

    private static final String kakPrivateEncrypted = "nZXJ9JdbYOkKNIZogbePx2KOBpoOSMZ4/rSfO5sk/HpF/DlT5vZ+9OVXmBq411OU0ZM/xxoL2EToTIq8pgSV1WKXAoSfr3m0rAKban4XTUNGAagzr0xt0L+Vi/g3j5VuvPH4XZJp+c+7B6x2fOABt35SxAz+zTHhUmmYy3zHSVHd1Mg7R7beHte19OGcXDVFMhlbOyA+/Xx+OMJwmwh/IX2yjSh9Tv9T6wzviqKu3hgW0BYB1NmtIRPMOoBxN/7nq0s9iBwPf5nmOt/IA1KjQdrwcyNQ/qtbTVfRocF9HvIt8XbpqgGq1Fl2SP3bYfMqsS0FeBVh4M5xeGkEh5e76E7zUTG9VgAvJAnJ6B9DOXOm7AeKZpXyHdZalekdZSbKte0u/HusrYNmuMZZR0y1nqLeS/Tf6gxb4b7G2tvIHBbE0Hy37tMk/IYchXsyTWL3S9x2qW5x1Xai/kpkR4FdoEBKsq54lUfy2+bdnK0IxYkqB+I5jXUSgX5ZezeBYk3y56J+rFwAqOu9lpbqYjQWCKWZdFVzzEo7gSw6gVaEw41gNHtgDXB8UDGMmBmtlPaVI49SslOCIlSmzeLGAPnoq3TE7XJqDA7tR3txT0i3lnqutjM/GNdLHglKVHh4cpwV5qkkXyWkEx/ngfa9u4fUb+oR/dsAKbihwLtg0dNBYVTg5CD+ofTVLYi5qsApIBjhV5tt8YDW60NIFzCENB3Iy8K5Hxyc2e3Ct6iLMBD2FSgLizsNKKR8CtnbBjl4808ZgRSe2OVRbJbiKTgsk9ck1+wASjUmwumJLAyb2hapLdc1uIyde707oOYcq+yLyUh1DoixL6fp8+zZk35mNEmHLMGZ0wI+GONk9FRR7/Zij/q9s4nB0tQYxUztsb6pmiZnJO5WP6dNPf5jvhQ9sZAmARY3QH+pM4b1zwx1geXNLzxMKWeqIw0uJCJBIsAr4c2tpM3vH3XhQ9RT95NOm+AjTu4ZgGWcT9D8BF+rb+Z2CwjgJ4KuRpsTyaoSXIhb6wtWEsNRL6PmjTUSgANJ33Ep6SQ4iKN7yvYcqw+NGei9MEV8TbY4Gvg5pihYS5Ra6z3XFh/7q/Ww/9bqSzpIAAFWBgPCz96MaBaPc+2fmAsxDnIdsvFcikuwLvC1FWfUacl864C6Iy6oJRnevcy9nL9vVB3k/l01d4EvDoI2BxgGUg1wJ35vfjPlEvKSOxLyKN7cmd2lg4ShBzYDqvb/G+7Z5vIYFw1PYtQvW99hdQQY5fwWVLbvcZkPL6zawfQ14vo3saLaFIcZUZI2rAy/3ncWIWuWbzmninkUNnud10CBBedtEf+65BUh4kKUPvCOSXOawC3YqTGQeQlyRluWNO5obQSHgWzXzK9u5jobVR3qcNxPepHPO1OJjgEiE0NlDxPKSgnsYbZrly7uboKP2hrolgqvej6K7kkTzJfPl9bBxxl2wiWknS8m/6wi8Nl2w7Lge9Sl9HWz/hF+5nqpuWON9pkt0FFcUsENdMxktDGq+L1HJYNZEOm6Lw0gZ/vBakF4Hxos05uebpLftihlmdmql4NHOm9gw/fea75pDEs1wT2ZbcOjckufGFm5ZEzm7w9GYnbEWtl8J5j18P4BVzokiEJ5zR4lrADqp3049RuvDBc=";
    private static final String OID_SHA1 = "1.3.14.3.2.26";
    private static final String OID_SHA224 = "2.16.840.1.101.3.4.2.4";
    private static final String OID_SHA256 = "2.16.840.1.101.3.4.2.1";
    private static final String OID_SHA384 = "2.16.840.1.101.3.4.2.2";
    private static final String OID_SHA512 = "2.16.840.1.101.3.4.2.3";
    private static final String OID_RSA = "1.2.840.113549.1.1.1";
    private static final String OID_RSA_SHA1 = "1.2.840.113549.1.1.5";
    private static final String OID_RSA_SHA224 = "1.2.840.113549.1.1.14";
    private static final String OID_RSA_SHA256 = "1.2.840.113549.1.1.11";
    private static final String OID_RSA_SHA384 = "1.2.840.113549.1.1.12";
    private static final String OID_RSA_SHA512 = "1.2.840.113549.1.1.13";

    public TSECryptoHelper() {
    }

    public static void main(String[] args) throws Exception {
        SecretKey key = computeSecretKey("H4H2-V8UM-2QEC", "thuan003", "5a5dfa84-72d9-45b2-b6dc-dd1cfc335d68");
        System.out.println("SecretKey-Enc::: " + DatatypeConverter.printHexBinary(key.getEncoded()));
        System.out.println("SecretKey-Alg::: " + key.getAlgorithm());
////        byte[] cipherText = Utils.base64Decode("nZXJ9JdbYOkKNIZogbePx2KOBpoOSMZ4/rSfO5sk/HpF/DlT5vZ+9OVXmBq411OU0ZM/xxoL2EToTIq8pgSV1WKXAoSfr3m0rAKban4XTUNGAagzr0xt0L+Vi/g3j5VuvPH4XZJp+c+7B6x2fOABt35SxAz+zTHhUmmYy3zHSVHd1Mg7R7beHte19OGcXDVFMhlbOyA+/Xx+OMJwmwh/IX2yjSh9Tv9T6wzviqKu3hgW0BYB1NmtIRPMOoBxN/7nq0s9iBwPf5nmOt/IA1KjQdrwcyNQ/qtbTVfRocF9HvIt8XbpqgGq1Fl2SP3bYfMqsS0FeBVh4M5xeGkEh5e76E7zUTG9VgAvJAnJ6B9DOXOm7AeKZpXyHdZalekdZSbKte0u/HusrYNmuMZZR0y1nqLeS/Tf6gxb4b7G2tvIHBbE0Hy37tMk/IYchXsyTWL3S9x2qW5x1Xai/kpkR4FdoEBKsq54lUfy2+bdnK0IxYkqB+I5jXUSgX5ZezeBYk3y56J+rFwAqOu9lpbqYjQWCKWZdFVzzEo7gSw6gVaEw41gNHtgDXB8UDGMmBmtlPaVI49SslOCIlSmzeLGAPnoq3TE7XJqDA7tR3txT0i3lnqutjM/GNdLHglKVHh4cpwV5qkkXyWkEx/ngfa9u4fUb+oR/dsAKbihwLtg0dNBYVTg5CD+ofTVLYi5qsApIBjhV5tt8YDW60NIFzCENB3Iy8K5Hxyc2e3Ct6iLMBD2FSgLizsNKKR8CtnbBjl4808ZgRSe2OVRbJbiKTgsk9ck1+wASjUmwumJLAyb2hapLdc1uIyde707oOYcq+yLyUh1DoixL6fp8+zZk35mNEmHLMGZ0wI+GONk9FRR7/Zij/q9s4nB0tQYxUztsb6pmiZnJO5WP6dNPf5jvhQ9sZAmARY3QH+pM4b1zwx1geXNLzxMKWeqIw0uJCJBIsAr4c2tpM3vH3XhQ9RT95NOm+AjTu4ZgGWcT9D8BF+rb+Z2CwjgJ4KuRpsTyaoSXIhb6wtWEsNRL6PmjTUSgANJ33Ep6SQ4iKN7yvYcqw+NGei9MEV8TbY4Gvg5pihYS5Ra6z3XFh/7q/Ww/9bqSzpIAAFWBgPCz96MaBaPc+2fmAsxDnIdsvFcikuwLvC1FWfUacl864C6Iy6oJRnevcy9nL9vVB3k/l01d4EvDoI2BxgGUg1wJ35vfjPlEvKSOxLyKN7cmd2lg4ShBzYDqvb/G+7Z5vIYFw1PYtQvW99hdQQY5fwWVLbvcZkPL6zawfQ14vo3saLaFIcZUZI2rAy/3ncWIWuWbzmninkUNnud10CBBedtEf+65BUh4kKUPvCOSXOawC3YqTGQeQlyRluWNO5obQSHgWzXzK9u5jobVR3qcNxPepHPO1OJjgEiE0NlDxPKSgnsYbZrly7uboKP2hrolgqvej6K7kkTzJfPl9bBxxl2wiWknS8m/6wi8Nl2w7Lge9Sl9HWz/hF+5nqpuWON9pkt0FFcUsENdMxktDGq+L1HJYNZEOm6Lw0gZ/vBakF4Hxos05uebpLftihlmdmql4NHOm9gw/fea75pDEs1wT2ZbcOjckufGFm5ZEzm7w9GYnbEWtl8J5j18P4BVzokiEJ5zR4lrADqp3049RuvDBc=");
//        System.out.println("Cipher::: " + DatatypeConverter.printHexBinary(cipherText));
//        byte[] plaint = decrypt("AES/CBC/PKCS5Padding", key, new byte[16], cipherText);
//        System.out.println("Key-Raw::: " + Hex.toHexString(plaint));
//        System.out.println("Key-Raw::: " + Hex.toHexString(plaint));
//        RSAPrivateKey privateKey = RSAUtils.getPrivateKey(plaint);
//        System.out.println("Modulus: " + Hex.toHexString(privateKey.getModulus().toByteArray()));
    }

    public static String computeVC(byte[][] hashes) throws NoSuchAlgorithmException {
        if (hashes != null && hashes.length != 0) {
            byte[] vcData = new byte[hashes[0].length];
            System.arraycopy(hashes[0], 0, vcData, 0, vcData.length);
            byte[] tmp;
            if (hashes.length > 1) {
                padding(hashes);

                for(int ii = 1; ii < hashes.length; ++ii) {
                    if (hashes[ii].length > vcData.length) {
                        tmp = new byte[hashes[ii].length];
                        System.arraycopy(vcData, 0, tmp, 0, vcData.length);

                        for(int ttt = vcData.length; ttt < hashes[ii].length; ++ttt) {
                            tmp[ttt] = -1;
                        }

                        vcData = new byte[tmp.length];
                        System.arraycopy(tmp, 0, vcData, 0, tmp.length);
                    }

                    for(int idx = 0; idx < hashes[ii].length; ++idx) {
                        vcData[idx] |= hashes[ii][idx];
                    }
                }
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(vcData);
            tmp = md.digest();
            short first = (short)(tmp[0] << 8 | tmp[1] & 255);
            short last = (short)(tmp[tmp.length - 2] << 8 | tmp[tmp.length - 1] & 255);
            return String.format("%04X-%04X", first, last);
        } else {
            throw new RuntimeException("The input is null or empty");
        }
    }

    public static byte[][] padding(byte[][] hashes) {
        int max = findMaxLen(hashes);
        byte[][] rsp = new byte[hashes.length][];

        for(int idx = 0; idx < hashes.length; ++idx) {
            int len = hashes[idx].length;
            if (len < max) {
                byte[] tmp = new byte[len];
                System.arraycopy(hashes[idx], 0, tmp, 0, len);
                hashes[idx] = new byte[max];
                System.arraycopy(tmp, 0, hashes[idx], 0, len);

                for(int ii = len; ii < max; ++ii) {
                    hashes[idx][ii] = -1;
                }
            }
        }

        return rsp;
    }

    private static int findMaxLen(byte[][] hashes) {
        int max = 0;
        byte[][] var2 = hashes;
        int var3 = hashes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte[] hh = var2[var4];
            if (max < hh.length) {
                max = hh.length;
            }
        }

        return max;
    }

    public static SecretKey computeSecretKey(String recoveryCode, String username, String deviceUUID) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (recoveryCode != null && username != null && deviceUUID != null) {
            String mix = recoveryCode.concat(username).concat(username).concat(deviceUUID).concat(deviceUUID).concat(deviceUUID);
            System.out.println("mix::: " + mix);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(recoveryCode.getBytes("UTF-8"));
            byte[] recoveryCodes = shiftBytes(digest.digest(), 1);
            System.out.println("recoveryCodes::: " + DatatypeConverter.printHexBinary(recoveryCodes));
            digest.reset();
            digest.update(username.getBytes("UTF-8"));
            byte[] usernames = shiftBytes(digest.digest(), 6);
            System.out.println("usernames::: " + DatatypeConverter.printHexBinary(usernames));
            digest.reset();
            digest.update(deviceUUID.getBytes("UTF-8"));
            byte[] deviceUUIDs = shiftBytes(digest.digest(), 1);
            System.out.println("deviceUUIDs::: " + DatatypeConverter.printHexBinary(deviceUUIDs));
            digest.reset();
            digest.update(mix.getBytes("UTF-8"));
            byte[] mixes = shiftBytes(digest.digest(), 8);
            System.out.println("mixes::: " + DatatypeConverter.printHexBinary(mixes));
            digest.reset();
            digest.update(recoveryCodes);
            digest.update(usernames);
            digest.update(deviceUUIDs);
            digest.update(mixes);
            byte[] data = digest.digest();
            System.out.println("Password: " + DatatypeConverter.printHexBinary(data));
            PBEKeySpec spec = new PBEKeySpec(DatatypeConverter.printHexBinary(data).toCharArray(), new byte[16], 256, 256);
            SecretKey tmp = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec);
            System.out.println("OutPut: " + Hex.toHexString(tmp.getEncoded()));
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } else {
            throw new InvalidParameterException("Input is null");
        }
    }

    public static byte[] shiftBytes(byte[] in, int len) {
        byte[] result = new byte[in.length];
        System.arraycopy(in, len, result, 0, in.length - len);
        System.arraycopy(in, 0, result, in.length - len, len);
        return result;
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

    public static byte[] computeAuthSignature(String hashOID, String signAlgoOID, String signAlgoParamsPem, byte[] challenge, PrivateKey pk) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
        if (hashOID == null || hashOID.isEmpty()) {
            hashOID = "1.2.840.113549.1.1.11";
        }

        if (signAlgoOID == null || signAlgoOID.isEmpty()) {
            signAlgoOID = "1.2.840.113549.1.1.11";
        }

        boolean isPss = false;
        if (null != signAlgoParamsPem) {
            byte var7 = -1;
            switch(signAlgoParamsPem.hashCode()) {
                case -1189467110:
                    if (signAlgoParamsPem.equals("BgkqhkiG9w0BAQo=")) {
                        var7 = 0;
                    }
                default:
                    switch(var7) {
                        case 0:
                            isPss = true;
                            break;
                        default:
                            throw new NoSuchAlgorithmException("Not support SignAlgoParams: " + signAlgoParamsPem);
                    }
            }
        }

        if (null == hashOID) {
            throw new NoSuchAlgorithmException("Not support hash-OID: " + hashOID);
        } else {
            byte var9 = -1;
            switch(hashOID.hashCode()) {
                case -1225949696:
                    if (hashOID.equals("2.16.840.1.101.3.4.2.1")) {
                        var9 = 2;
                    }
                    break;
                case -1225949695:
                    if (hashOID.equals("2.16.840.1.101.3.4.2.2")) {
                        var9 = 3;
                    }
                    break;
                case -1225949694:
                    if (hashOID.equals("2.16.840.1.101.3.4.2.3")) {
                        var9 = 4;
                    }
                    break;
                case -1225949693:
                    if (hashOID.equals("2.16.840.1.101.3.4.2.4")) {
                        var9 = 1;
                    }
                    break;
                case -308431282:
                    if (hashOID.equals("1.3.14.3.2.26")) {
                        var9 = 0;
                    }
            }

            String hashName;
            byte[] hash;
            switch(var9) {
                case 0:
                    hash = checkHashAndSign(signAlgoOID, "1.2.840.113549.1.1.5", challenge, 20, "SHA1");
                    hashName = "SHA1";
                    break;
                case 1:
                    hash = checkHashAndSign(signAlgoOID, "1.2.840.113549.1.1.14", challenge, 28, "SHA-224");
                    hashName = "SHA224";
                    break;
                case 2:
                    hash = checkHashAndSign(signAlgoOID, "1.2.840.113549.1.1.11", challenge, 32, "SHA-256");
                    hashName = "SHA256";
                    break;
                case 3:
                    hash = checkHashAndSign(signAlgoOID, "1.2.840.113549.1.1.12", challenge, 48, "SHA-384");
                    hashName = "SHA384";
                    break;
                case 4:
                    hash = checkHashAndSign(signAlgoOID, "1.2.840.113549.1.1.13", challenge, 64, "SHA-512");
                    hashName = "SHA512";
                    break;
                default:
                    throw new NoSuchAlgorithmException("Not support hash-OID: " + hashOID);
            }

            return isPss ? calcSignaturePss(hash, hashName, pk) : calcSignaturePKCS1_V1_5(hash, hashOID, pk);
        }
    }

    private static byte[] checkHashAndSign(String signAlgoOID, String signAlgo, byte[] challenge, int hashLen, String hashName) throws NoSuchAlgorithmException {
        if ("1.2.840.113549.1.1.1".equals(signAlgoOID)) {
            if (challenge.length == hashLen) {
                return challenge;
            } else {
                throw new InvalidParameterException("Length of challenge is invalid");
            }
        } else if (signAlgo.equals(signAlgoOID)) {
            MessageDigest disgest = MessageDigest.getInstance(hashName);
            return disgest.digest(challenge);
        } else {
            throw new InvalidParameterException("HashOID is not matched with SignAlgo");
        }
    }

    public static byte[] calcSignaturePKCS1_V1_5(byte[] hash, String oid, PrivateKey privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        ASN1ObjectIdentifier digestOid = new ASN1ObjectIdentifier(oid);
        AlgorithmIdentifier sha256oid = new AlgorithmIdentifier(digestOid, DERNull.INSTANCE);
        DigestInfo di = new DigestInfo(sha256oid, hash);
        byte[] hashWithOID = di.getEncoded("DER");
        Signature privateSignature = Signature.getInstance("NonewithRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(hashWithOID);
        return privateSignature.sign();
    }

    public static byte[] calcSignaturePss(byte[] hash, String hashName, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
//        LOGGER.debug("hash: " + DatatypeConverter.printHexBinary(hash));
//        LOGGER.debug("hashName: " + hashName);
        String MGF1 = "MGF1";
        String RAWRSASSA_PSS = "NONEWITHRSASSA-PSS";
        int saltLen = DigestFactory.getDigest(hashName).getDigestSize();
        Signature signature = Signature.getInstance("NONEWITHRSASSA-PSS", "BC");
        PSSParameterSpec pssPrams = new PSSParameterSpec(hashName, "MGF1", new MGF1ParameterSpec(hashName), saltLen, 1);
        signature.setParameter(pssPrams);
        signature.initSign(privateKey);
        signature.update(hash);
        return signature.sign();
    }
}
