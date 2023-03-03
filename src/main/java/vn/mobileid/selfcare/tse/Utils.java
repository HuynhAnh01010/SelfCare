package vn.mobileid.selfcare.tse;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    static Gson gson;
    static GsonBuilder gsonBuilder;
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._+/";
    private static final String ACTIVATION__STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ONLY_NUMERIC_ACTIVATION_STRING = "0123456789";
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

    public Utils() {
    }

    public static boolean isNullOrEmpty(String in) {
        return in == null || in.isEmpty();
    }

    public static byte[] stringToBytes(String str, Charset charset) {
        try {
            return str.getBytes(charset);
        } catch (Exception var3) {
            var3.printStackTrace();
            return str.getBytes();
        }
    }

    public static String getRandomKeyID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(new Date()).concat(UUID.randomUUID().toString().replace("-", "")).toUpperCase();
    }

    public static byte[] genRandomArray(int size) {
        byte[] random = new byte[size];
        (new Random()).nextBytes(random);
        return random;
    }

    public static byte[] saveByteArrayOutputStream(InputStream body) {
        byte[] r = null;

        try {
            ByteArrayOutputStream f = new ByteArrayOutputStream();

            int c;
            while((c = body.read()) > -1) {
                f.write(c);
            }

            r = f.toByteArray();
            f.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return r;
    }

    public static String getPropertiesFile() {
        File folder = new File(System.getProperty("jboss.server.base.dir"));
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; ++i) {
            if (listOfFiles[i].isFile()) {
                String filePath = listOfFiles[i].getAbsolutePath();
                if (filePath.contains("properties.conf")) {
                    return filePath;
                }
            }
        }

        return null;
    }

    public static String generateBillCodeNoThrows(String relyingParty, long logId, Date logDatetime) {
        String billCode = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String dateTime = sdf.format(logDatetime);
            billCode = relyingParty + "-" + dateTime + "-" + logId + "-" + generateOneTimePassword(6);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return billCode;
    }

    public static String generateBillCode(String relyingParty, long logId, Date logDatetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String dateTime = sdf.format(logDatetime);
        return relyingParty + "-" + dateTime + "-" + logId + "-" + generateOneTimePassword(6);
    }

    public static String generateBillCode(int logId, Date logDatetime) {
        String billCode = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String dateTime = sdf.format(logDatetime);
            billCode = "-" + dateTime + "-" + logId + "-" + generateOneTimePassword(6);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return billCode;
    }

    public static String generateOneTimePassword(int len) {
        String numbers = "0123456789";
        Random rndm_method = new Random();
        char[] otp = new char[len];

        for(int i = 0; i < len; ++i) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }

        return new String(otp);
    }

    public static String getValueInHashMap(HashMap<String, String> hashMap, String key) {
        String value = null;
        if (hashMap != null) {
            Iterator var3 = hashMap.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, String> entry = (Entry)var3.next();
                if (entry.getKey() != null && ((String)entry.getKey()).compareToIgnoreCase(key) == 0) {
                    value = (String)entry.getValue();
                }
            }
        }

        return value;
    }

    public static int convertToMinute(int value) {
        return value < 60 ? value : value / 60;
    }

    public static long getDifferenceBetweenDatesInSecond(Date date1, Date date2) {
        long diffMs = date2.getTime() - date1.getTime();
        long diffSec = diffMs / 1000L;
        return diffSec;
    }

    public static long getDifferenceBetweenDatesInMinute(Date date1, Date date2) {
        return getDifferenceBetweenDatesInSecond(date1, date2) / 60L;
    }

    public static String makeNotificationMessage(String notificationTemplate, String passCode) {
        String notificationMessage = notificationTemplate;
        String[] patterns = new String[]{"{AuthorizeCode}", "{OTP}", "{AuthoriseCode}"};
        String[] var4 = patterns;
        int var5 = patterns.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String pattern = var4[var6];
            if (notificationTemplate.contains(pattern)) {
                notificationMessage = notificationTemplate.replace(pattern, passCode);
                break;
            }
        }

        return notificationMessage;
    }

    public static String makeNotificationMessage(String notificationTemplate, String passCode, int expiresIn) {
        String resp = notificationTemplate;
        String[] patterns = new String[]{"{AuthorizeCode}", "{OTP}", "{AuthoriseCode}", "{Activationcode}", "{ActivationCode}"};
        String[] var5 = patterns;
        int var6 = patterns.length;

        int var7;
        String pattern;
        for(var7 = 0; var7 < var6; ++var7) {
            pattern = var5[var7];
            if (notificationTemplate.contains(pattern)) {
                resp = notificationTemplate.replace(pattern, passCode);
                break;
            }
        }

        patterns = new String[]{"{ExpiredIn}", "{ExpiresIn}", "{Timeout}"};
        var5 = patterns;
        var6 = patterns.length;

        for(var7 = 0; var7 < var6; ++var7) {
            pattern = var5[var7];
            if (notificationTemplate.contains(pattern)) {
                DecimalFormat df = new DecimalFormat("#.##");
                String formatted = df.format((double)((float)expiresIn / 60.0F));
                resp = resp.replace(pattern, formatted);
                break;
            }
        }

        return resp;
    }

    public static String makeNotificationMessage(String notificationTemplate, String username, String password) {
        String resp = notificationTemplate;
        String[] patterns = new String[]{"{Username}", "{User}"};
        String[] var5 = patterns;
        int var6 = patterns.length;

        int var7;
        String pattern;
        for(var7 = 0; var7 < var6; ++var7) {
            pattern = var5[var7];
            if (notificationTemplate.contains(pattern)) {
                resp = notificationTemplate.replace(pattern, username);
                break;
            }
        }

        patterns = new String[]{"{Password}", "{PassCode}", "{Passcode}", "{Activationcode}", "{ActivationCode}", "{RecoveryCode}", "{Recoverycode}"};
        var5 = patterns;
        var6 = patterns.length;

        for(var7 = 0; var7 < var6; ++var7) {
            pattern = var5[var7];
            if (notificationTemplate.contains(pattern)) {
                resp = resp.replace(pattern, password);
                break;
            }
        }

        return resp;
    }

    public static String makeNotificationMessageCertSN(String notificationTemplate, String serialSn, String passcode) {
        String resp = notificationTemplate;
        String[] patterns = new String[]{"{SerialNumber}", "{Serial}"};
        String[] var5 = patterns;
        int var6 = patterns.length;

        int var7;
        String pattern;
        for(var7 = 0; var7 < var6; ++var7) {
            pattern = var5[var7];
            if (notificationTemplate.contains(pattern)) {
                resp = notificationTemplate.replace(pattern, serialSn);
                break;
            }
        }

        patterns = new String[]{"{AuthorizeCode}", "{Password}", "{PassCode}", "{Passcode}"};
        var5 = patterns;
        var6 = patterns.length;

        for(var7 = 0; var7 < var6; ++var7) {
            pattern = var5[var7];
            if (notificationTemplate.contains(pattern)) {
                resp = resp.replace(pattern, passcode);
                break;
            }
        }

        return resp;
    }

    public static boolean isEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        } else {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pat = Pattern.compile(emailRegex);
            return email == null ? false : pat.matcher(email).matches();
        }
    }

    public static boolean isPhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        } else {
            String phoneRegex = "^(([+]{1}|[0-9]{2})[0-9]{4,11})$";
            Pattern pat = Pattern.compile(phoneRegex);
            return pat.matcher(phone).matches();
        }
    }

    public static String detectSequenceNumber(String str) {
        List<String> seqNumber = new ArrayList();

        for(int i = 0; i < str.length(); ++i) {
            char currentChar = str.charAt(i);
            if (currentChar >= '0' && currentChar <= '9') {
                seqNumber.add(String.valueOf(currentChar));
            } else {
                if (seqNumber.size() > 3) {
                    break;
                }

                seqNumber.clear();
            }
        }

        StringBuilder builder = new StringBuilder();
        Iterator var6 = seqNumber.iterator();

        while(var6.hasNext()) {
            String s = (String)var6.next();
            builder.append(s);
        }

        return builder.toString();
    }

    public static String replaceSecureCode(String emailContent) {
        String resp = new String(emailContent);
        if (!isNullOrEmpty(emailContent)) {
            String oneTimePassword = detectSequenceNumber(emailContent);
            if (!isNullOrEmpty(oneTimePassword)) {
                resp = emailContent.replace(oneTimePassword, "********");
            }
        }

        return resp;
    }

    public static String base64Encode(Object o) {
        return base64Encode(toJson(o));
    }

    public static String base64Encode(String s) {
        return base64Encode(s.getBytes());
    }

    public static String base64Encode(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static byte[] base64Decode(String s) {
        return Base64.getMimeDecoder().decode(s);
    }

    public static byte[] base64Decode(byte[] b) {
        return Base64.getMimeDecoder().decode(b);
    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }


    public static String toJson(Object o, double version) {
        return gsonBuilder.setVersion(version).create().toJson(o);
    }

    public static <T> T fromJson(ResultSet rs, String colName, Class<T> classOfT) {
        try {
            String json = rs.getString(colName);
            return json == null ? null : gson.fromJson(json, classOfT);
        } catch (SQLException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static JsonObject toJsonObject(Object obj) {
        return (JsonObject)gson.toJsonTree(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return json != null && !json.isEmpty() ? gson.fromJson(json, classOfT) : null;
    }

    public static <T> T fromJsonNoThrow(String json, Class<T> classOfT) {
        if (!isNullOrEmpty(json) && classOfT != null) {
            try {
                return gson.fromJson(json, classOfT);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static <T> T fromJsonNoThrow(String json, Type classOfT) {
        if (!isNullOrEmpty(json) && classOfT != null) {
            try {
                return gson.fromJson(json, classOfT);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static <T> ArrayList<T> fromJsonArray(String json, Type type) throws FileNotFoundException {
        return (ArrayList)gson.fromJson(json, type);
    }

    public static boolean parseBooleanFromZeroAndOne(String string) {
        return string != null && (string.equals("1") || string.equalsIgnoreCase("true"));
    }

    public static String randomToken(int count) {
        StringBuilder builder = new StringBuilder();

        while(count-- != 0) {
            int character = (int)(Math.random() * (double)"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._+/".length());
            builder.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._+/".charAt(character));
        }

        return builder.toString();
    }

    public static String randomActivation(int countGroup, int countMember) {
        StringBuilder builder = new StringBuilder();

        while(countGroup-- > 0) {
            int var3 = 0;

            while(var3++ < countMember) {
                int character = (int)(Math.random() * (double)"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length());
                builder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(character));
            }

            builder.append("-");
        }

        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static String randomActivation(String format, boolean onlyNumber) {
        Pattern pattern = Pattern.compile("[#-]{4,}$");
        if (!pattern.matcher(format).matches()) {
            throw new IllegalArgumentException("Format of activation code is invalid");
        } else {
            String lookup;
            if (onlyNumber) {
                lookup = "0123456789";
            } else {
                lookup = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            }

            String[] groups = format.split("-");
            int countGroup = groups.length;
            StringBuilder builder = new StringBuilder();
            int groupIdx = 0;

            while(true) {
                int countMemeber;
                do {
                    if (groupIdx >= countGroup) {
                        return builder.deleteCharAt(builder.length() - 1).toString();
                    }

                    countMemeber = groups[groupIdx++].length();
                } while(countMemeber == 0);

                int var9 = 0;

                while(var9++ < countMemeber) {
                    int character = (int)(Math.random() * (double)lookup.length());
                    builder.append(lookup.charAt(character));
                }

                if (builder.length() > 0) {
                    builder.append("-");
                }
            }
        }
    }


    public static <T> ArrayList<T> enumerationToArrayList(Enumeration<T> authElements) {
        if (authElements != null && authElements.hasMoreElements()) {
            ArrayList resp = new ArrayList();

            do {
                resp.add(authElements.nextElement());
            } while(authElements.hasMoreElements());

            return resp;
        } else {
            return null;
        }
    }

    public static String randomToken() {
        return UUID.randomUUID().toString();
    }

    public static String randomDeviceUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean stringEquals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        } else {
            return str1.equals(str2);
        }
    }

    public static boolean checkByteArrayTwoEquals(byte[][] str1, byte[][] str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 != null && str2 != null) {
            if (str1.length != str2.length) {
                return false;
            } else {
                List<byte[]> arrayStr1 = new ArrayList(Arrays.asList(str1));
                List<byte[]> arrayStr2 = new ArrayList(Arrays.asList(str2));
                byte[][] var4 = str1;
                int var5 = str1.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    byte[] str11 = var4[var6];
                    boolean foundEquals = false;
                    Iterator var9 = arrayStr2.iterator();

                    while(var9.hasNext()) {
                        byte[] str21 = (byte[])var9.next();
                        if (checkByteArrayOneEquals(str11, str21)) {
                            arrayStr1.remove(str11);
                            arrayStr2.remove(str21);
                            foundEquals = true;
                            break;
                        }
                    }

                    if (!foundEquals) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean checkByteArrayOneEquals(byte[] str1, byte[] str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 != null && str2 != null) {
            if (str1.length != str2.length) {
                return false;
            } else {
                for(int i = 0; i < str1.length; ++i) {
                    if (str1[i] != str2[i]) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isContain(String str, String[] sam) {
        if (isNullOrEmpty(str)) {
            return false;
        } else {
            String[] var2 = sam;
            int var3 = sam.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String sss = var2[var4];
                if (str.equals(sss)) {
                    return true;
                }
            }

            return false;
        }
    }


    public static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return (String)letters.stream().collect(Collectors.joining());
    }

    public static String generateRegex(boolean isOnlyNumeric, boolean isMustContainNumeric, boolean isMustContainLowerCase, boolean isMustContainUpperCase, boolean isMustContainSpecialCharater, int min, int max) {
        StringBuilder str = new StringBuilder("");
        if (isOnlyNumeric) {
            str.append("[0-9]");
        } else {
            if (isMustContainNumeric) {
                str.append("(?=.*[0-9])");
            }

            if (isMustContainLowerCase) {
                str.append("(?=.*[a-z])");
            }

            if (isMustContainUpperCase) {
                str.append("(?=.*[A-Z])");
            }

            if (isMustContainSpecialCharater) {
                str.append("(?=.*[^a-zA-Z0-9])");
            }

            str.append(".");
        }

        str.append("{");
        str.append(min);
        str.append(",");
        str.append(max);
        str.append("}");
        return str.toString();
    }

    public static boolean checkIPwithPattern(String ip, String pattern) {
        if (pattern.length() == 1) {
            return true;
        } else {
            String c = pattern.substring(0, pattern.indexOf("*"));
            String b = ip.substring(0, c.length());
            return c.compareToIgnoreCase(b) == 0;
        }
    }

    public static String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            return (String)env.get("COMPUTERNAME");
        } else {
            return env.containsKey("HOSTNAME") ? (String)env.get("HOSTNAME") : "Unknown Computer";
        }
    }

    public static class ByteArray2DimensionsToBase64TypeAdapter implements JsonSerializer<byte[][]>, JsonDeserializer<byte[][]> {
        public ByteArray2DimensionsToBase64TypeAdapter() {
        }

        public byte[][] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            if (jsonArray != null && jsonArray.size() != 0) {
                byte[][] response = new byte[jsonArray.size()][];

                for(int i = 0; i < jsonArray.size(); ++i) {
                    response[i] = Base64.getMimeDecoder().decode(jsonArray.get(i).getAsString());
                }

                return response;
            } else {
                return (byte[][])null;
            }
        }

        public JsonElement serialize(byte[][] src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray jsonArray = new JsonArray(src.length);

            for(int i = 0; i < src.length; ++i) {
                jsonArray.add(new JsonPrimitive(Base64.getEncoder().encodeToString(src[i])));
            }

            return jsonArray;
        }
    }

    public static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public ByteArrayToBase64TypeAdapter() {
        }

        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.getMimeDecoder().decode(json.getAsString());
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
        }
    }

    public static class IntToBooleanTypeAdapter implements JsonDeserializer<Boolean> {
        public IntToBooleanTypeAdapter() {
        }

        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String in = json.getAsString();
            return in != null && (in.equals("true") || in.equals("1"));
        }
    }

}
