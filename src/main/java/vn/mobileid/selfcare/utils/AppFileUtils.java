package vn.mobileid.selfcare.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppFileUtils {

    private static void write2File(String path, byte[] content) throws IOException {
        Path out = Paths.get(path);
        Files.write(out, content);
    }

    private static void writeContentFile(String path, String prefix, String extension, byte[] content) throws IOException {
//        String PATH = eIdentityProperties.getLogOCR().getPath() + File.separator + folder + File.separator;
        Files.createDirectories(Paths.get(path));
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String fileName = timeStamp + "_" + prefix  + extension;
        write2File(path + File.separator + fileName, content);
    }

    public static void writeLogOcr2File(String path, JSONObject obj) throws IOException {
        String subjectId = obj.has("subject_id") ? obj.get("subject_id").toString() : "Undefined";
        String processId = obj.has("process_id") ? obj.get("process_id").toString() : "Undefined";
        path = path + subjectId;
        if (obj.has("face_scan") && StringUtils.isNotEmpty((String) obj.get("face_scan"))) {
            byte[] decriptBase64 = Base64.decodeBase64(obj.get("face_scan").toString());
            writeContentFile(path, processId, ".mp4", decriptBase64);
            obj.put("face_scan","<face_scan>");
        }
        if (obj.has("back_image") && StringUtils.isNotEmpty((String) obj.get("back_image") )) {
            byte[] decriptBase64 = Base64.decodeBase64(obj.get("back_image").toString());
            writeContentFile(path, processId, "back_image.png", decriptBase64);
            obj.put("back_image","<back_image>");
        }
        if (obj.has("audit_trail_image") && StringUtils.isNotEmpty((String) obj.get("audit_trail_image"))) {
            byte[] decriptBase64 = Base64.decodeBase64(obj.get("audit_trail_image").toString());
            writeContentFile(path, processId, "audit_trail_image.png", decriptBase64);
            obj.put("audit_trail_image","<audit_trail_image>");

        }
        if (obj.has("low_quality_audit_trail_image") && StringUtils.isNotEmpty((String) obj.get("low_quality_audit_trail_image") )) {
            byte[] decriptBase64 = Base64.decodeBase64(obj.get("low_quality_audit_trail_image").toString());
            writeContentFile(path, processId, "low_quality_audit_trail_image.png", decriptBase64);
            obj.put("low_quality_audit_trail_image","<low_quality_audit_trail_image>");
        }
        if (obj.has("front_image") && StringUtils.isNotEmpty((String) obj.get("front_image") )) {
            byte[] decriptBase64 = Base64.decodeBase64(obj.get("front_image").toString());
            writeContentFile(path, processId, "front_image.png", decriptBase64);
            obj.put("front_image","<front_image>");
        }
    }
}
