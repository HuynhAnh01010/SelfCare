//package vn.mobileid.selfcare.rest;
//
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.web.bind.annotation.*;
//import vn.mobileid.selfcare.jwt.JWTFilter;
//import vn.mobileid.selfcare.service.CallApiRsspService;
//import vn.mobileid.selfcare.service.util.AppUtils;
//import vn.mobileid.selfcare.utils.ApiResult;
//import vn.mobileid.selfcare.utils.SelfCareProperties;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * Controller to authenticate users.
// */
//@RestController
//@RequestMapping("/api")
//public class HistoryRest {
//
//    private final Logger log = LoggerFactory.getLogger(TseRest.class);
//
//    private final CallApiRsspService callApiRsspService;
//
//    private final SelfCareProperties selfCareProperties;
//
//
//    private final AuthenticationManager authenticationManager;
//
//    public HistoryRest(CallApiRsspService callApiRsspService, SelfCareProperties selfCareProperties, AuthenticationManager authenticationManager) {
//
//        this.callApiRsspService = callApiRsspService;
//        this.selfCareProperties = selfCareProperties;
//        this.authenticationManager = authenticationManager;
//    }
//
//    @GetMapping("/owner/logging")
//    public ResponseEntity ownerLogging(HttpServletResponse response, HttpServletRequest request) {
////        String jwt = JWTFilter.resolveToken(request);
//        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
//        if (jwt == null) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        } else {
//            JSONObject json = new JSONObject();
//            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
//            responseTse.setAccessToken(jwt);
//            ApiResult apiResult = callApiRsspService.ownerLogging(responseTse, json, request);
//            return AppUtils.returnFromServer(apiResult);
//
//        }
//
//    }
//
//    @GetMapping("/owner/history")
//    public ResponseEntity ownerHistory(
//            HttpServletResponse response, HttpServletRequest request,
//            @RequestParam(value = "page", required = false) int page,
//            @RequestParam(value = "size", required = false) int size
//    ) {
////        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED);
////        String jwt = accessToken;
//        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
////        String jwt = JWTFilter.resolveToken(request);
//        if (jwt == null) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        } else {
//
//            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
//            responseTse.setAccessToken(jwt);
//
//            JSONObject json = new JSONObject();
//            json.put("pageNumber", page);
//            log.info("size: {}", size);
//            json.put("recordCount", size);
//
//            JSONObject searchCondition = new JSONObject();
////            searchCondition.put("fromDate", "20200717000000");
////            searchCondition.put("toDate", "20200901023505");
//            searchCondition.put("actions", new String[]{"LOGIN"});
//
//            JSONObject record = new JSONObject();
//            record.put("requestData", true);
//            record.put("responseData", true);
//            record.put("relyingParty", true);
//            record.put("responseMessage", true);
//            record.put("rpRequestID", true);
//            record.put("requestID", true);
//            record.put("responseID", true);
//
////            json.put("searchConditions", searchCondition);
//            json.put("record", record);
//
//            ApiResult apiResult = callApiRsspService.queriesOwnerHistory(responseTse, json, request);
//            return AppUtils.returnFromServer(apiResult);
//
//        }
//
//    }
//
//    @GetMapping("/credentials/history")
//    public ResponseEntity credentialHistory(HttpServletRequest request, HttpServletResponse resp,
//                                            @RequestParam(value = "page", required = false) int page,
//                                            @RequestParam(value = "size", required = false) int size,
//                                            @RequestParam(value = "id", required = false) String id
//    ) {
////        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED);
////        String jwt = accessToken;
//        log.info("--- TRY COOKIE ---");
//        String jwt = JWTFilter.extractAccessTokenFromCookie(request, resp, callApiRsspService);
//        if (jwt == null) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        } else {
//
//            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
//            responseTse.setAccessToken(jwt);
//
//            JSONObject json = new JSONObject();
//            json.put("pageNumber", page);
//            json.put("credentialID", id);
//            json.put("recordCount", size);
//
//            JSONObject record = new JSONObject();
//            record.put("requestData", true);
//            record.put("responseData", true);
//            record.put("relyingParty", true);
//            record.put("responseMessage", true);
//            record.put("rpRequestID", true);
//            record.put("requestID", true);
//            record.put("responseID", true);
//            record.put("responseID", true);
//
//            json.put("record", record);
//
//            ApiResult apiResult = callApiRsspService.queriesCredentialHistory(responseTse, json, request);
//            return AppUtils.returnFromServer(apiResult);
//
//        }
//
//    }
//
//    @GetMapping("/credentials/list")
//    public ResponseEntity credentialList(
//            HttpServletResponse response, HttpServletRequest request
//    ) {
//
////        String jwt = JWTFilter.resolveToken(request);
//        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
//        if (jwt == null) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        } else {
//
//            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
//            responseTse.setAccessToken(jwt);
//
//            JSONObject json = new JSONObject();
//            json.put("certInfo", true);
//            json.put("authInfo", true);
//
//            JSONObject searchCondition = new JSONObject();
//            searchCondition.put("certificateStatus", "ALL");
//            searchCondition.put("certificatePurpose", "ALL");
//
//            json.put("searchConditions", searchCondition);
//
//            ApiResult apiResult = callApiRsspService.credentialsList(responseTse, json, request);
//            return AppUtils.returnFromServer(apiResult);
//
//        }
//
//    }
//
//    @GetMapping("/credentials/info")
//    public ResponseEntity credentialInfo(HttpServletResponse response, HttpServletRequest request,
//                                         @RequestParam(value = "id") String id
//    ) {
//
//        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
//        if (jwt == null) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        } else {
//
//            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
//            responseTse.setAccessToken(jwt);
//
//            JSONObject json = new JSONObject();
//            json.put("credentialID", id);
//            json.put("certInfo", true);
//            json.put("certificates", "none");
//            json.put("authInfo", true);
//
//
//            ApiResult apiResult = callApiRsspService.credentialsInfo(responseTse, json, request);
//            return AppUtils.returnFromServer(apiResult);
//
//        }
//
//    }
//
//
//}
