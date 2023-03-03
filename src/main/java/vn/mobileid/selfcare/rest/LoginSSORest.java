//package vn.mobileid.selfcare.rest;
//
//import java.util.Map;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import vn.mobileid.selfcare.jwt.JWTFilter;
//import vn.mobileid.selfcare.service.CallApiRsspService;
//import vn.mobileid.selfcare.utils.CommonUtil;
//import vn.mobileid.selfcare.utils.SelfCareProperties;
//import vn.mobileid.selfcare.utils.ApiResult;
//import vn.mobileid.selfcare.rest.vm.*;
//
//@RestController
//@RequestMapping("/api")
//public class LoginSSORest {
//
//    private final Logger log = LoggerFactory.getLogger(LoginSSORest.class);
//
//    private final CallApiRsspService callApiRsspService;
//
//    @Autowired
//    private SelfCareProperties selfCareProperties;
//
//    public LoginSSORest(CallApiRsspService callApiRsspService) {
//        this.callApiRsspService = callApiRsspService;
//    }
//
//    
//
////        String fullUrl = selfCareProperties.getUrl() + CallApiRsspService.URL_LOGIN_SSO + "?" + request.getQueryString();
////
////        HttpHeaders headers = new HttpHeaders();
////
////        log.info("After Pre Login SSO SSL2: {}", responseTse.getAuthenSsl());
////
////        headers.set("Authorization", "SSL2 " + responseTse.getAuthenSsl());
////
////        RestTemplate restTemplate = new RestTemplate();
////        JSONObject json = new JSONObject();
////
////        json.put("state", mapData.get("state"));
////        json.put("code", mapData.get("code"));
////        json.put("lang", getCookieLanguage(request));
////        json.put("profile", selfCareProperties.getProfile());
////
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
////
////        HttpEntity<String> requestData = new HttpEntity<String>(json.toString(), headers);
//
////        return restTemplate.exchange(fullUrl, HttpMethod.POST, requestData, Map.class);
//    
//
//    private String getCookieLanguage(HttpServletRequest request) {
//        try {
//            Cookie[] cookies = request.getCookies();
//            for (int i = 0; i < cookies.length; i++) {
//                log.info("{} - {} ", cookies[i].getName(), cookies[i].getValue());
//                if ("NG_TRANSLATE_LANG_KEY".equalsIgnoreCase(cookies[i].getName())) {
//                    log.info("key: {}", cookies[i].getValue());
//                    if ("%22en%22".equalsIgnoreCase(cookies[i].getValue())) {
//                        return "EN";
//                    } else {
//                        return "VN";
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            log.error(ex.getMessage());
//        }
//        return "EN";
//    }
//
//}
