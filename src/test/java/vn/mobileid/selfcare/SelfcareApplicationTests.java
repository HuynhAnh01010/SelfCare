package vn.mobileid.selfcare;

//import com.sun.jndi.toolkit.url.Uri;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

//@SpringBootTest
@Slf4j
class SelfcareApplicationTests {

    static String filePath = "D:\\MOBILE-ID\\factec\\Readfile\\rs\\3a4b797006cfdb1c6b9d47dc24c4ffbe38684c82.js";
    static String fileResult = "D:\\MOBILE-ID\\factec\\Readfile\\rs\\getValue.js";
    static String fileResultJs = "D:\\MOBILE-ID\\factec\\Readfile\\rs\\3a4b797006cfdb1c6b9d47dc24c4ffbe38684c82_raw.js";
    static String fileLogValue = "D:\\MOBILE-ID\\factec\\Readfile\\rs\\-1618822689377.log";

    public static void main(String[] args) throws IOException {
        //Step1
//        readFileSdk();

//        step2
//        changeDecode2File();

    }

    public static void readFileSdk() throws IOException {
        String src = readLineByLineJava8(filePath);
        getValue(src, fileResult);
    }

    public static void changeDecode2File() throws IOException {
        List<String> logValue = readLineByLineJava8R(fileLogValue);
        String src = readLineByLineJava8(filePath);

        int page = 0;
        String result = replaceValueFromLog(src, logValue, page);

        writeString2File(fileResultJs, result);
    }

    //    @Test
    void contextLoads() throws IOException {

//		String path = "";
//		log.info("downloads: {}", path);
//		File file = new File(path);
//
//		Path pathU = Paths.get(path);
//		byte[] data = Files.readAllBytes(pathU);

        String filePath = "D:/MOBILE-ID/factec/FaceTecSDK-browser-9.1.3-Mobile-ID-202104152444/core-sdk/FaceTecSDK.js/FaceTecSDK.js";
//        String filePath = "F:/FaceTecSDK-ios-9.1.0/FaceTecSDK.framework/FaceTecSDK";
//        String filePath = "E:/ReadFile/v9.0/FaceTecSDK.js";
        String fileLogValue = "D:\\MOBILE-ID\\factec\\Readfile\\result.txt";
//		String filePath = "E:/ReadFile/test.txt";
        String fileResult = "D:\\MOBILE-ID\\factec\\Readfile\\result.txt";
        String fileResultJs = "E:/ReadFile/resource/final.js";
//		String fileResultJs = "F:/FaceTecSDK-ios-9.1.0/FaceTecSDK.framework/str.txt";
//        String src = readLineByLineJava8(filePath);
//
//        System.out.println("src: " + src);
////        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
//
//        // Java 8 - Base64 class, finally.
//        // byte[] to base64 encoded string
////        String s = new String(bytes, StandardCharsets.ISO_8859_1.name());
////        String s = new String(bytes, StandardCharsets.UTF_8);
////        String s = Base64.getEncoder().encodeToString(bytes);
//
////        get all value
//        getValue(src, fileResult);

//        List<String> logValue =  readLineByLineJava8R(fileLogValue);
//1
//
//
//
////        Map<String, String> map = new HashMap<>();
////
////     for (Map.Entry<String, String> entry : map.entrySet()) {
////      log.info(entry.getKey() + "/" + java.net.URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8.name()));
////     }
////
////        String result = replaceValue(src,map);
//        int page = 0;
//        String result = replaceValueFromLog(src,logValue,page);
////////        log.info("result: {}", result);
//////
//        writeString2File(fileResultJs, result );
    }

    public static String replaceValueFromLog(String target, List<String> logValue, int page) throws UnsupportedEncodingException {
        int i = page;
        for (String str : logValue) {


            try {
                String key = str.split(":")[1];
                String val = str.split(":")[2];
                String replacement = "`" + java.net.URLDecoder.decode(val, StandardCharsets.UTF_8.name()) + "`";
                if (i % 500 == 0) {
                    log.info("str {}: {} - {}: {}", i, key, val, replacement);
                }
                target = target.replaceAll("_0x.{3,6}\\(" + key + "\\)", replacement);
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("index {}: {} ", i,str);
            }

            ++i;
//            if(++i >= 20) break;
        }

        return target;
    }

    public static String replaceValue(String target, Map<String, String> map) {

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {

                target = target.replaceAll("_0x.{3,6}\\('" + entry.getKey() + "'\\)", "`" + java.net.URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8.name()) + "`");
            } catch (Exception ex) {
                log.error(entry.getKey() + "/" + entry.getValue());
            }

        }
        return target;
    }

    public static void getValue(String src, String fileResult) throws IOException {
        System.out.println("getValue ---");
        List<String> allMatches = new ArrayList<String>();
        List<String> writeMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("(?<=_.{5,8}\\()(0x[0123456789abcdef]{3,7})(?=\\))")
                .matcher(src);
        int i = 0;
        while (m.find()) {
            if (!allMatches.contains(m.group())) {
                allMatches.add(m.group());
//                writeMatches.add(String.format("console.log(\"map.put(\\\"%s\\\",\\\"\"+_0x15ec('%s')+\"\\\");\");", m.group(), m.group()));
                writeMatches.add(String.format("console.log(':%s:'+ _0x4efb9a(%s));", m.group(), m.group()));
            }

        }
        Path out = Paths.get(fileResult);
        Files.write(out, writeMatches, Charset.defaultCharset());
    }

    public static void writeString2File(String fileResult, String str) throws IOException {
        Path out = Paths.get(fileResult);
        Files.write(out, str.getBytes());
    }

    private static List<String> readLineByLineJava8R(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        List<String> strings = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> strings.add(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> {
                contentBuilder.append(s).append("\n");
            });
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

//        log.info("content: {}", contentBuilder.toString());
        return contentBuilder.toString();
    }

    private static String readAllBytesJava7(String filePath) {
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
