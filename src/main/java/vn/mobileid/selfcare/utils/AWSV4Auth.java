package vn.mobileid.selfcare.utils;

import lombok.extern.slf4j.Slf4j;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Slf4j

public class AWSV4Auth {

    private String accessKeyID;
    private String secretAccessKey;
    private String regionName;
    private String serviceName;
    private String httpMethodName;
    private String canonicalURI;
    private TreeMap<String, String> queryParametes;
    private TreeMap<String, String> awsHeaders;
    private String payload;
    private String strSignedHeader;
    private String xAmzDate;
    private String currentDate;
    private String credentialScope;
    private String authorization;
    protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private AWSV4Auth() {
    }

    private AWSV4Auth(Builder builder) {
        this.accessKeyID = builder.accessKeyID;
        this.secretAccessKey = builder.secretAccessKey;
        this.regionName = builder.regionName;
        this.serviceName = builder.serviceName;
        this.httpMethodName = builder.httpMethodName;
        this.canonicalURI = builder.endpointURL.getPath();
        this.queryParametes = builder.queryParametes;
        this.awsHeaders = builder.awsHeaders;
        if (!this.awsHeaders.containsKey("Host")) {
            if(builder.endpointURL.getPort() == -1){
                this.awsHeaders.put("Host", builder.endpointURL.getHost() );
            }else {
                this.awsHeaders.put("Host", builder.endpointURL.getHost() + ":" + builder.endpointURL.getPort());
            }

        }

        this.payload = builder.payload;
        this.getDateTime();
    }

    private String prepareCanonicalRequest() {
        StringBuilder canonicalURL = new StringBuilder("");
        canonicalURL.append(this.httpMethodName).append("\n");
        this.canonicalURI = this.canonicalURI != null && !this.canonicalURI.trim().isEmpty() ? this.canonicalURI : "/";
        canonicalURL.append(this.canonicalURI).append("\n");
        StringBuilder queryString = new StringBuilder("");
        String key;
        if (this.queryParametes != null && !this.queryParametes.isEmpty()) {
            Iterator var4 = this.queryParametes.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, String> entrySet = (Map.Entry)var4.next();
                 key = (String)entrySet.getKey();
                key = (String)entrySet.getValue();
                queryString.append(key).append('=').append(this.encodeParameter(key)).append('&');
            }

            queryString.deleteCharAt(queryString.lastIndexOf(String.valueOf('&')));
            queryString.append("\n");
        } else {
            queryString.append("\n");
        }

        canonicalURL.append(queryString);
        StringBuilder signedHeaders = new StringBuilder("");
        if (this.awsHeaders != null && !this.awsHeaders.isEmpty()) {
            Iterator var10 = this.awsHeaders.entrySet().iterator();

            while(var10.hasNext()) {
                Map.Entry<String, String> entrySet = (Map.Entry)var10.next();
                key = ((String)entrySet.getKey()).toLowerCase();
                String value = (String)entrySet.getValue();
                signedHeaders.append(key).append(";");
                canonicalURL.append(key).append(":").append(value).append("\n");
            }

            canonicalURL.append("\n");
        } else {
            canonicalURL.append("\n");
        }

        this.strSignedHeader = signedHeaders.substring(0, signedHeaders.length() - 1);
        canonicalURL.append(this.strSignedHeader).append("\n");
        this.payload = this.payload == null ? "" : this.payload;
        canonicalURL.append(this.generateHex(this.payload));
        return canonicalURL.toString();
    }

    private String prepareStringToSign(String canonicalURL) {
        String stringToSign = "";
        stringToSign = "AWS4-HMAC-SHA256\n";
        stringToSign = stringToSign + this.xAmzDate + "\n";
        this.credentialScope = this.currentDate + "/" + this.regionName + "/" + this.serviceName + "/" + "aws4_request";

        stringToSign = stringToSign + this.credentialScope + "\n";
        stringToSign = stringToSign + this.generateHex(canonicalURL);
        return stringToSign;
    }

    private String calculateSignature(String stringToSign) {
        try {
            byte[] signatureKey = this.getSignatureKey(this.secretAccessKey, this.currentDate, this.regionName, this.serviceName);
            byte[] signature = this.HmacSHA256(signatureKey, stringToSign);
            String strHexSignature = this.bytesToHex(signature);
            return strHexSignature;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public Map<String, String> getHeaders() {
        this.awsHeaders.put("X-Amz-Date", this.xAmzDate);
        String canonicalURL = this.prepareCanonicalRequest();
        log.debug("Canonical Request: {}", canonicalURL);
        String stringToSign = this.prepareStringToSign(canonicalURL);
        log.debug("String to sign: {}", stringToSign);
        String signature = this.calculateSignature(stringToSign);
        log.debug("Signature: {}", signature);
        if (signature != null) {
            this.authorization = this.buildAuthorizationString(signature);
            this.awsHeaders.put("Authorization", this.authorization);
            return this.awsHeaders;
        } else {
            return null;
        }
    }

    public String getxAmzDate(){
        return this.xAmzDate;
    }

    public String getAuthorization() {
        return this.authorization;
    }

    private String buildAuthorizationString(String strSignature) {
        return "AWS4-HMAC-SHA256 Credential=" + this.accessKeyID + "/" + this.credentialScope + "," + " SignedHeaders=" + this.strSignedHeader + "," + " Signature=" + strSignature;
    }

    private String generateHex(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            return String.format("%064x", new BigInteger(1, digest));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private byte[] HmacSHA256(byte[] key, String data) throws Exception {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

    private byte[] getSignatureKey(String key, String date, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate = this.HmacSHA256(kSecret, date);
        byte[] kRegion = this.HmacSHA256(kDate, regionName);
        byte[] kService = this.HmacSHA256(kRegion, serviceName);
        byte[] kSigning = this.HmacSHA256(kService, "aws4_request");
        return kSigning;
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for(int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 15];
        }

        return (new String(hexChars)).toLowerCase();
    }

    private void getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date now = new Date();
        this.xAmzDate = dateFormat.format(now);
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        this.currentDate = dateFormat.format(now);
    }

    private String encodeParameter(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (Exception var3) {
            return URLEncoder.encode(param);
        }
    }

    public static class Builder {
        private String accessKeyID;
        private String secretAccessKey;
        private String regionName;
        private String serviceName;
        private String httpMethodName;
        private URL endpointURL;
        private TreeMap<String, String> queryParametes;
        private TreeMap<String, String> awsHeaders;
        private String payload;

        public Builder(String accessKeyID, String secretAccessKey) {
            this.accessKeyID = accessKeyID;
            this.secretAccessKey = secretAccessKey;
        }

        public Builder regionName(String regionName) {
            this.regionName = regionName;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder httpMethodName(String httpMethodName) {
            this.httpMethodName = httpMethodName;
            return this;
        }

        public Builder endpointURI(URL endpointURL) {
            this.endpointURL = endpointURL;
            return this;
        }


        public Builder queryParametes(TreeMap<String, String> queryParametes) {
            this.queryParametes = queryParametes;
            return this;
        }

        public Builder awsHeaders(TreeMap<String, String> awsHeaders) {
            this.awsHeaders = awsHeaders;
            return this;
        }

        public Builder payload(String payload) {
            this.payload = payload;
            return this;
        }

        public AWSV4Auth build() {
            return new AWSV4Auth(this);
        }
    }
}

