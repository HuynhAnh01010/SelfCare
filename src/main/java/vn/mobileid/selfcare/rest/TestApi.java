package vn.mobileid.selfcare.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.mobileid.selfcare.rest.vm.SubjectCreateVM;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test/v1")
@Slf4j
public class TestApi {

    @GetMapping("/file")
    public void subjectCreate(HttpServletRequest httpRequest, @RequestParam String size) {
        log.info("size: {}", size);

    }


    @PostMapping("/file")
    public void subjectCreate(HttpServletRequest httpRequest, @RequestParam MultipartFile videofile) throws IOException {
        if(videofile != null){
            String PATH = "D:\\CR";
            Files.createDirectories(Paths.get(PATH));
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String fileName = timeStamp + "_video.mp4";
            Path out = Paths.get(PATH + File.separator + fileName);
            Files.write(out, videofile.getBytes());
            log.info("size: {}", videofile.getSize());
        }

    }
}
