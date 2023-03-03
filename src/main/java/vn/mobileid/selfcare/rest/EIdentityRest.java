package vn.mobileid.selfcare.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mobileid.selfcare.rest.exception.HttpRequestExceptionCustom;
import vn.mobileid.selfcare.rest.vm.OwnerCreateVm;
import vn.mobileid.selfcare.rest.vm.SubjectCreateVM;
import vn.mobileid.selfcare.service.CalleIdentityService;
import vn.mobileid.selfcare.service.util.AppUtils;
import vn.mobileid.selfcare.utils.ApiResult;
import vn.mobileid.selfcare.utils.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/e-identity/v1")
@Slf4j
public class EIdentityRest {

    @Autowired
    private CalleIdentityService calleIdentityService;

    @Autowired
    private TseRest tseRest;

    @Autowired
    private vn.mobileid.selfcare.utils.eIdentityProperties eIdentityProperties;

    @PostMapping("/subjects/create")
    public ResponseEntity subjectCreate(HttpServletRequest httpRequest, @RequestBody SubjectCreateVM subjectCreateVM) {
//      nhập sđt | email để lấy mã otp 
        try {
            ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);
            if (apiResult.getCode() == 0) {
                Map<String, Object> response = (Map<String, Object>) apiResult.getData();
                log.info("data: {}", CommonUtil.convertObjectToString(response));
//                JSONObject data = (JSONObject) apiResult.getData();
                if ((int) response.get("status") == 0) {
                    JSONObject request = new JSONObject();
                    request.put("email", subjectCreateVM.getEmail());
                    request.put("mobile", subjectCreateVM.getMobile());

                    ApiResult apiResult2 = calleIdentityService.eIdentitySubjectCreate(httpRequest, request, response);

                    return AppUtils.returnFromServereIdentity(apiResult2);
                } else {
                    return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/processes/create")
    public ResponseEntity processesCreate(HttpServletRequest httpRequest, @RequestBody Map<String, String> stringStringMap) {
//      
        try {

            ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);
            if (apiResult.getCode() == 0) {
                Map<String, Object> data = (Map<String, Object>) apiResult.getData();
                log.info("data: {}", CommonUtil.convertObjectToString(stringStringMap));
//                JSONObject data = (JSONObject) apiResult.getData();
                if ((int) data.get("status") == 0) {
                    JSONObject request = new JSONObject();
                    request.put("subject_id", stringStringMap.get("subject_id"));
//                    request.put("process_type", eIdentityProperties.getApi().getProcessType());
                    request.put("process_type", stringStringMap.get("process_type"));
                    request.put("provider", stringStringMap.get("provider"));
                    JSONObject requestUserAgent = new JSONObject();
                    switch (stringStringMap.get("process_type")) {
                        case "LIVENESS":
                            requestUserAgent.put("user_agent", stringStringMap.get("userAgent"));
                            request.put("process_parameters", requestUserAgent);
                            break;
                    }

                    ApiResult apiResult2 = calleIdentityService.processesCreate(httpRequest, request, data);

                    return AppUtils.returnFromServereIdentity(apiResult2);
                } else {
                    return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/images/upload")
    public ResponseEntity imagesUpload(HttpServletRequest httpRequest) {
        try {
            ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);
            if (apiResult.getCode() == 0) {
                Map<String, Object> data = (Map<String, Object>) apiResult.getData();
                log.info("data: {}", CommonUtil.convertObjectToString(data));
//                JSONObject data = (JSONObject) apiResult.getData();
                if ((int) data.get("status") == 0) {
                    JSONObject obj = new JSONObject();
                    obj.put("access_token", data.get("access_token"));

                    ApiResult apiResult2 = calleIdentityService.imagesUpload(httpRequest, obj, data);

                    return AppUtils.returnFromServereIdentity(apiResult2);
                } else {
                    return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verification")
    public ResponseEntity verification(HttpServletRequest httpRequest, @RequestBody Map<String, String> stringStringMap) {
        try {

            ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);
            if (apiResult.getCode() == 0) {
                Map<String, Object> data = (Map<String, Object>) apiResult.getData();
                log.info("data: {}", CommonUtil.convertObjectToString(data));
//                JSONObject data = (JSONObject) apiResult.getData();
                if ((int) data.get("status") == 0) {
                    JSONObject request = new JSONObject();
                    request.put("subject_id", stringStringMap.get("subject_id"));
                    request.put("process_id", stringStringMap.get("process_id"));

                    switch (stringStringMap.get("type")) {
                        case "OTP":
                            request.put("otp", stringStringMap.get("otp"));
                            break;
                        case "LIVENESS":
                            request.put("face_scan", stringStringMap.get("faceScan"));
                            request.put("audit_trail_image", stringStringMap.get("auditTrailImage"));
                            request.put("low_quality_audit_trail_image", stringStringMap.get("lowQualityAuditTrailImage"));

                            break;
                        case "DOCUMENTVIDEO":

//                            request.put("face_scan", stringStringMap.get("idScan"));
                            request.put("front_image", stringStringMap.get("idScanFrontImage"));

                            if (stringStringMap.containsKey("idScanBackImage")) {
                                request.put("back_image", stringStringMap.get("idScanBackImage"));
                                request.put("document_type", eIdentityProperties.getApi().getDocumentType()); //CITIZENCARD //IDENTITYCARD
                            } else {
                                request.put("document_type", "PASSPORT");
                                request.put("back_image", "");
                            }

//                            JSONObject requestUserAgent = new JSONObject();
//                            requestUserAgent.put("user_agent", stringStringMap.get("userAgent"));
//                            request.put("process_parameters", requestUserAgent);
                            break;
                    }


                    ApiResult apiResult2 = calleIdentityService.verification(httpRequest, request, data);

                    return AppUtils.returnFromServereIdentity(apiResult2);
                } else {
                    return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    private ApiResult processesGetPolling(HttpServletRequest httpRequest, @RequestBody Map<String, String> stringStringMap) {
        try {
            ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);
            if (apiResult.getCode() == 0) {
                Map<String, Object> data = (Map<String, Object>) apiResult.getData();
                log.info("data: {}", CommonUtil.convertObjectToString(data));
                if ((int) data.get("status") == 0) {

                    JSONObject request = new JSONObject();
                    request.put("subject_id", stringStringMap.get("subject_id"));
                    request.put("process_id", stringStringMap.get("process_id"));

                    return calleIdentityService.processesGet(httpRequest, request, data);
                } else {
                    return ApiResult.fail("SERVER UNAVAILABLE");
                }

            }
            return ApiResult.fail("SERVER UNAVAILABLE");

        } catch (Exception ae) {
            ae.printStackTrace();
            log.error("Authentication exception trace: {}", ae);
            return ApiResult.fail("SERVER UNAVAILABLE");
        }
    }

    @PostMapping("/processes/get")
    public ResponseEntity processesGet(HttpServletRequest httpRequest, @RequestBody Map<String, String> stringStringMap) {
        try {
            ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);
            if (apiResult.getCode() == 0) {
                Map<String, Object> data = (Map<String, Object>) apiResult.getData();
                log.info("data: {}", CommonUtil.convertObjectToString(data));
//                JSONObject data = (JSONObject) apiResult.getData();
                if ((int) data.get("status") == 0) {

                    JSONObject request = new JSONObject();
                    request.put("subject_id", stringStringMap.get("subject_id"));
                    request.put("process_id", stringStringMap.get("process_id"));

                    ApiResult apiResult2 = calleIdentityService.processesGet(httpRequest, request, data);

                    return AppUtils.returnFromServereIdentity(apiResult2);
                } else {
                    return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/processes/self-revise")
    public ResponseEntity processesSelfRevise(HttpServletRequest httpRequest, @RequestBody OwnerCreateVm ownerCreateVm) {
        try {
            if (ArrayUtils.isNotEmpty(ownerCreateVm.getChangeColumn())) {
                ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);

                if (apiResult.getCode() == 0) {
                    Map<String, Object> data = (Map<String, Object>) apiResult.getData();

////                JSONObject data = (JSONObject) apiResult.getData();
//                    if ((int) data.get("status") == 0) {
//
//
//
//                        ApiResult apiResult2 = calleIdentityService.processesSelfRevise(httpRequest, requestData, data);
//
//                        return AppUtils.returnFromServereIdentity(apiResult2);
//                    } else {
//                        return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
//                    }

                }
            }

            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    private OwnerCreateVm fProcessesSelfRevise(HttpServletRequest httpRequest, OwnerCreateVm ownerCreateVm) throws HttpRequestExceptionCustom {
        if (ArrayUtils.isNotEmpty(ownerCreateVm.getChangeColumn())) {
            ApiResult apiResult = calleIdentityService.getAccessToken(httpRequest);

            if (apiResult.getCode() == 0) {
                Map<String, Object> data = (Map<String, Object>) apiResult.getData();

                if ((int) data.get("status") == 0) {

                    JSONObject requestData = new JSONObject();
                    requestData.put("subject_id", ownerCreateVm.getSubjectId());
                    requestData.put("process_id", ownerCreateVm.getProcessId());

                    for (String str : ownerCreateVm.getChangeColumn()) {
                        switch (str) {
                            case "fullname":
                                requestData.put("name", ownerCreateVm.getFullname());
                                break;
                            case "identification":
                                requestData.put("document_type", ownerCreateVm.getIdentificationType());
                                requestData.put("document_number", ownerCreateVm.getIdentification());
                                break;
                            case "gender":
                                requestData.put("gender", ownerCreateVm.getGender());
                                break;
                            case "dob":
                                requestData.put("dob", ownerCreateVm.getDob());
                                break;
                            case "location":
                                requestData.put("address", ownerCreateVm.getLocation());
                                break;
                        }
                    }
                    ApiResult apiResult2 = calleIdentityService.processesSelfRevise(httpRequest, requestData, data);
                    if (apiResult2.getCode() == 0) {
                        Map<String, Object> mapResponse = (Map<String, Object>) apiResult2.getData();
                        if ((int) mapResponse.get("status") == 0) {
                            return ownerCreateVm;
                        } else {
                            throw new HttpRequestExceptionCustom((String) mapResponse.get("message"));
                        }
                    } else {
                        throw new HttpRequestExceptionCustom("SERVER UNAVAILABLE");
                    }
                } else {
                    throw new HttpRequestExceptionCustom("SERVER UNAVAILABLE");
                }

            }
            throw new HttpRequestExceptionCustom("SERVER UNAVAILABLE");
        }
        return ownerCreateVm;
    }

    @PostMapping("/processes/self-revise-and-tse-register")
    public ResponseEntity processesSelfReviseAndTseRegister(HttpServletRequest httpRequest, @RequestBody OwnerCreateVm ownerCreateVm) {
        try {
            OwnerCreateVm ownerCreateVm1 = fProcessesSelfRevise(httpRequest, ownerCreateVm);
            return tseRest.ownerCreate(ownerCreateVm1, httpRequest);

        } catch (HttpRequestExceptionCustom httpRequestExceptionCustom) {
            httpRequestExceptionCustom.printStackTrace();
            log.error(httpRequestExceptionCustom.getMessage());
            return new ResponseEntity<>(ApiResult.fail(httpRequestExceptionCustom.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception ae) {
            ae.printStackTrace();
            log.error("Error self-revise-and-tse-register: {}", ae.getMessage());
            return new ResponseEntity<>(ApiResult.fail(ae.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
