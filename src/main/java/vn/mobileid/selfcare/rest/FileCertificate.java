///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.mobileid.selfcare.rest;
//
//import com.amazonaws.util.IOUtils;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.mobileid.selfcare.domain.User;
//import vn.mobileid.selfcare.excel.ExcelGenerator;
//import vn.mobileid.selfcare.service.dto.csr.model.AuditLogService;
//import vn.mobileid.selfcare.service.dto.csr.model.FileManagerImpl;
//import vn.mobileid.selfcare.service.dto.csr.model.ResponseObject;
//import vn.mobileid.selfcare.service.util.AppUtils;
//import vn.mobileid.selfcare.utils.ApiResult;
//
///**
// *
// * @author Mobile ID 21
// */
//@Slf4j
//@RestController
//@RequestMapping("/api")
//public class FileCertificate {
//    
////    private final AuditLogService auditLogService;
////    private final FileManagerImpl fileManager;
//
//    @PostMapping(path = "/download/csr/{type}")
//    public ResponseEntity<ApiResult> downloadCSR(Model model, HttpServletRequest request, HttpSession session, Locale locale, @PathVariable String type,
//            @RequestBody Map<String, Object> map) {
//
//        User u = AppUtils.getPrincipal();
//        try {
//            String ids = (String) map.get("ids");
//
//            List cloudCertificates = new ArrayList();
//            
//            
//            ResponseObject responseObject = new ResponseObject();
//            switch (type.toUpperCase()) {
//                case "XLS":
//                    ByteArrayInputStream byteArrayInputStream = ExcelGenerator.exportCsrFromCertificateIds(cloudCertificates);
//                    responseObject.setBytes(IOUtils.toByteArray(byteArrayInputStream));
//                    responseObject.setCode("0");
//                    responseObject.setContent("CSR_" + System.currentTimeMillis() + ".xls");
//                    break;
//                case "ZIP":
//                    ByteArrayOutputStream byteArrayOutputStream = fileManager.csr2Zip(cloudCertificates);
//                    responseObject.setBytes(byteArrayOutputStream.toByteArray());
//                    responseObject.setCode("0");
//                    responseObject.setContent("CSR_" + System.currentTimeMillis() + ".zip");
//                    break;
//                default:
//                    break;
//            }
//
//            auditLogService.saveLog(map, ApiResult.success(responseObject), u, "/api/download/csr/{type}", request);
//            return ResponseEntity.ok().body(ApiResult.success(responseObject));
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//            auditLogService.saveLog(map, ApiResult.fail(ex.getMessage()), u, "/api/download/csr/{type}", request);
//            return ResponseEntity.ok().body(ApiResult.fail(ex.getMessage()));
//        }
//    }
//}
