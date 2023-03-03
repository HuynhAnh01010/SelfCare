package vn.mobileid.selfcare.rest;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.mobileid.selfcare.crypto.Crypto;
import vn.mobileid.selfcare.jwt.JWTFilter;
import vn.mobileid.selfcare.rest.vm.*;
import vn.mobileid.selfcare.service.CallApiRsspService;
import vn.mobileid.selfcare.tse.TSECryptoHelper;
import vn.mobileid.selfcare.utils.ApiResult;
import vn.mobileid.selfcare.utils.CommonUtil;
import vn.mobileid.selfcare.utils.SelfCareProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import org.springframework.ui.Model;

import static vn.mobileid.selfcare.service.util.AppUtils.returnFromServer;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class TseRest {

    private final Logger log = LoggerFactory.getLogger(TseRest.class);

    private final CallApiRsspService callApiRsspService;

    private final SelfCareProperties selfCareProperties;

    private final AuthenticationManager authenticationManager;

    public TseRest(CallApiRsspService callApiRsspService, SelfCareProperties selfCareProperties, AuthenticationManager authenticationManager) {

        this.callApiRsspService = callApiRsspService;
        this.selfCareProperties = selfCareProperties;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity authenticate(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response, HttpServletRequest request, Model model) {
        log.info("AUTHENTICATE");
        JWTFilter.removeCookie("ssoEnabled", response);
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        SelfCareProperties.Notification notification = null;
        if (loginVM.isTse()) {
            notification = selfCareProperties.getNotification();
            notification.setValidityPeriod(loginVM.getValidityPeriod());
            notification.setHashValue(loginVM.getHashValue());
        }

        log.info("loginVm: {}", CommonUtil.convertObjectToString(loginVM));

        ApiResult apiResult = callApiRsspService.loginUser(loginVM, notification, loginVM.isTse(), request);
        if (apiResult.getCode() == 0) {
            Map<String, Object> mapResponse = (Map<String, Object>) apiResult.getData();

            if ((int) mapResponse.get("error") == 0) {

                Cookie cookie = new Cookie("ssoEnabled", "false");
//                cookie.setMaxAge(60 * 60 * 24 * 30);
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setPath("/SelfCare");
//                        cookie.setPath("/SelfCare");
                response.addCookie(cookie);

                JWTFilter.setCookie(mapResponse, response, loginVM.isRememberMe());

                return new ResponseEntity<>(apiResult, HttpStatus.OK);
            } else {
                log.info("getCode == 0 ERROR LOGIN: {}", apiResult.getMessage());
                apiResult.setMessage((String) mapResponse.get("errorDescription"));
                return new ResponseEntity<>(apiResult, HttpStatus.SERVICE_UNAVAILABLE);
            }

        } else {
            log.info("LOGIN ERROR: {}", apiResult.getMessage());
            return new ResponseEntity<>(apiResult, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/auth/revoke")
    public ResponseEntity authRevoke(HttpServletResponse response, HttpServletRequest request) {

        String jwt = null;
        String jwtRefresh = null;
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(JWTFilter.ACCESS_TOKEN)) {
                jwt = c.getValue();
            }
            if (c.getName().equals(JWTFilter.REFRESH_TOKEN)) {
                jwtRefresh = c.getValue();
            }
        }

        if (!StringUtils.hasText(jwt)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            JSONObject json = new JSONObject();
            json.put("tokenType", jwtRefresh == null ? 0 : 1);
            json.put("token", jwtRefresh == null ? jwt : jwtRefresh);

            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            callApiRsspService.authRevoke(responseTse, json, request);

            JWTFilter.removeCookie(JWTFilter.ACCESS_TOKEN, response);
            JWTFilter.removeCookie(JWTFilter.REFRESH_TOKEN, response);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

    @PostMapping("/auth/refresh")
    public ResponseEntity refreshToken(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> map) {

        log.info("refesh: {}", CommonUtil.convertObjectToString(map));
//        String jwt = map.get("refresh"); Old
        String jwt = JWTFilter.extractRefreshTokenFromCookie(request);
        if (!StringUtils.hasText(jwt)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {

            JSONObject json = new JSONObject();
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            ApiResult apiResult = callApiRsspService.refreshToken(responseTse, json, request);
//            return  returnFromServer(apiResult);
            if (apiResult.getCode() == 0) {
                Map<String, Object> mapResponse = (Map<String, Object>) apiResult.getData();

                if ((int) mapResponse.get("error") == 0) {

                    JWTFilter.setCookie(mapResponse, response, false);

                    return ResponseEntity.ok(mapResponse);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/auth/two-factor/{username}/{userType}")
    public ResponseEntity twoFactorWithUsername(
            @PathVariable String username, @PathVariable String userType, HttpServletResponse response, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, username);
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername(username);
        loginVM.setUserType(userType);
        try {
            ApiResult apiResultMethod = callApiRsspService.auth2FactoreMethod(loginVM, request);

            if (apiResultMethod.getCode() == 0) {
                Map<String, Object> mapResultMethod = (Map<String, Object>) apiResultMethod.getData();

                if ((int) mapResultMethod.get("error") == 0) {
                    if ("TSE".equalsIgnoreCase((String) mapResultMethod.get("twoFactorMethod")) || "NONE".equalsIgnoreCase((String) mapResultMethod.get("twoFactorMethod"))) {
                        String signData = RandomStringUtils.random(20, true, true) + System.currentTimeMillis();
                        log.info("SignData: {}", signData);
                        String hashAlg = Crypto.HASH_SHA256;
                        switch (selfCareProperties.getNotification().getHashAlgorithmOID()) {
                            case "1.3.14.3.2.26":

                                hashAlg = Crypto.HASH_SHA1;
                                break;
                            case "2.16.840.1.101.3.4.2.1":
                                hashAlg = Crypto.HASH_SHA256;
                                break;
                            case "2.16.840.1.101.3.4.2.2":
                                hashAlg = Crypto.HASH_SHA384;
                                break;
                            case "2.16.840.1.101.3.4.2.3":
                                hashAlg = Crypto.HASH_SHA512;
                                break;
                            case "2.16.840.1.101.3.4.2.8":
                                hashAlg = Crypto.HASH_SHA3_384;
                                break;
                            case "2.16.840.1.101.3.4.2.9":
                                hashAlg = Crypto.HASH_SHA3_512;
                                break;
                        }
                        byte[] hashSignData = Crypto.hashData(signData.getBytes(), hashAlg);
                        String b64Hash = Base64.getEncoder().encodeToString(hashSignData);

                        String vc = TSECryptoHelper.computeVC(new byte[][]{hashSignData});

                        SelfCareProperties.Notification notification = selfCareProperties.getNotification();
                        notification.setVc(vc);
                        log.info("vCode: ", vc);
                        notification.setHashValue(b64Hash);
                        notification.setMethod((String) mapResultMethod.get("twoFactorMethod"));

                        log.info("NOTIFICATION: {}", CommonUtil.convertObjectToString(notification));
                        return new ResponseEntity<>(ApiResult.success(notification), HttpStatus.ACCEPTED);
                    } else {
                        //# NONE AND # TSE
                        log.info("NONE AND TSE: {}", CommonUtil.convertObjectToString(ApiResult.success()));
                        return new ResponseEntity<>(ApiResult.success(), HttpStatus.ACCEPTED);
                    }
                } else {
                    log.info("ERROR TWO FT MAPRESULT METHOD: {}", CommonUtil.convertObjectToString(mapResultMethod.get("errorDescription")));
                    return new ResponseEntity<>(ApiResult.fail((String) mapResultMethod.get("errorDescription")), HttpStatus.ACCEPTED);
                }
            } else {
                log.info("ERROR TWO FT: {}", CommonUtil.convertObjectToString(apiResultMethod.getMessage()));
                return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/credentials/authentication")
    public ResponseEntity checkAuth(HttpServletResponse response, HttpServletRequest request) {

//        String jwt = JWTFilter.resolveToken(request);
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            byte[] decodeJwt = Base64.getDecoder().decode(jwt);
            String objJwt = new String(decodeJwt);
            log.info(objJwt);
            Gson gson = new Gson();
            jwtObject jwtObject = gson.fromJson(objJwt, jwtObject.class);

            log.info("json: {}", CommonUtil.convertObjectToString(jwtObject));
            JSONObject json = new JSONObject();
            JSONObject searchConditions = new JSONObject();
            searchConditions.put("certificateStatus", "ALL");
            searchConditions.put("certificatePurpose", "ALL");
            json.put("searchConditions", searchConditions);
            json.put("certInfo", false);
            json.put("certificates", "none");
            json.put("authInfo", false);
            json.put("lang", "EN");
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            ApiResult apiResult = callApiRsspService.credentialsList(responseTse, json, request);
//            return  returnFromServer(apiResult);
            log.info("REST request to check if the current user is authenticated {}", request.getRemoteUser());
            if (apiResult.getCode() == 0) {
                Map<String, Object> mapResponse = (Map<String, Object>) apiResult.getData();
                if ((int) mapResponse.get("error") == 0) {
                    return ResponseEntity.ok("ok");
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/owner/info")
    public ResponseEntity onwerInfo(HttpServletResponse response, HttpServletRequest request, Model model) {

        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (!StringUtils.hasText(jwt)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
//            jwtObject jwtObject = JWTFilter.convertJwtToObject(jwt);

            JSONObject json = new JSONObject();
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            ApiResult apiResult = callApiRsspService.ownerInfo(responseTse, json, request);
            if (apiResult.getCode() == 0) {
                Map<String, Object> mapResponse = (Map<String, Object>) apiResult.getData();
                if ((int) mapResponse.get("error") == 0) {
                    Cookie[] cookies = request.getCookies();
                    String getSSOEnabled = "";
                    log.info("============ OWNER INFO ============");
                    for (int i = 0; i < cookies.length; i++) {
                        log.info("{} - {} ", cookies[i].getName(), cookies[i].getValue());
                        if ("ssoEnabled".equals(cookies[i].getName())) {
                            getSSOEnabled = cookies[i].getValue().toString();
                        }
                    }

                    Map<String, Object> mapObject = (Map<String, Object>) mapResponse;

                    if ("true".equals(getSSOEnabled)) {
                        mapObject.put("ssoEnabled", true);
                    } else if ("false".equals(getSSOEnabled)) {
                        mapObject.put("ssoEnabled", false);
                    } else {
                        mapObject.put("ssoEnabled", false);
                    }

                    return ResponseEntity.ok(mapObject);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
                }
            } else if (apiResult.getCode() == 3005 || apiResult.getCode() == 3006) {

                return onwerInfo(response, request, model);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);

    }

    @PostMapping(path = "/account/reset_password/init")
    public ResponseEntity requestPasswordReset(HttpServletRequest request, @RequestBody LoginVM loginVM) {

        ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);

        if (apiResult.getCode() == 0) {
            CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();

            log.info("RS PW INIT SERVER VAILABLE: {}", CommonUtil.convertObjectToString(apiResult.getMessage()));

            if (responseTse.getError() == 0) {
                JSONObject json = new JSONObject();
                json.put("user", loginVM.getUsername());
                json.put("userType", loginVM.getUserType());
                json.put("requestType", "request");

                ApiResult apiResultReset = callApiRsspService.accountResetPassword(responseTse, json, request);
                log.info("RESET PASSWORD INIT APIRESULT: {}", CommonUtil.convertObjectToString(apiResultReset));
                log.info("RESET PASSWORD INIT APIRESULT GETCODE: {}", CommonUtil.convertObjectToString(apiResultReset.getData()));
//            return  returnFromServer(apiResult);
                if (apiResultReset.getCode() == 0) {
                    Map<String, Object> mapResponse = (Map<String, Object>) apiResultReset.getData();
                    if ((int) mapResponse.get("error") == 0) {
                        return ResponseEntity.ok(mapResponse);
                    } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return requestPasswordReset(request, loginVM);

                    } else {
                        log.info("ERROR RS PW INIT API GETCODE: {}", CommonUtil.convertObjectToString(mapResponse.get("message")));
                        return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
                    }
                }
//                else if  {
//                    callApiRsspService.resetAccessTokenServer();
//                    return requestPasswordReset(request, loginVM);
//                }
                apiResult.setMessage(responseTse.getErrorDescription());
                log.info("ERROR RS PW INIT API RESPONSE TSE: {}", CommonUtil.convertObjectToString(responseTse.getMessage()));
                return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);
            } else {
                apiResult.setMessage(responseTse.getErrorDescription());
                log.info("ELSE ERROR RS INIT: {}", CommonUtil.convertObjectToString(responseTse.getErrorDescription()));
                return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
            }

        } else {
            log.info("ERROR RS PW INIT: {}", CommonUtil.convertObjectToString(apiResult.getMessage()));
            return new ResponseEntity<>(apiResult.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }

//        return userService.requestPasswordReset(mail)
//                .map(user -> {
//                    mailService.sendPasswordResetMail(user);
//                    return new ResponseEntity<>("email was sent", HttpStatus.OK);
//                })
//                .orElse(new ResponseEntity<>("email address not registered", HttpStatus.BAD_REQUEST));
    }

    @PostMapping(path = "/account/reset_password/finish")
    public ResponseEntity finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword, HttpServletRequest request) {
        if (!checkPasswordLength(keyAndPassword.getPassword())) {
            return new ResponseEntity<>(ApiResult.fail("Length password not correct, at least 4 characters"), HttpStatus.BAD_REQUEST);
        }

        ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
        log.info("RS PASSWORD FN SERVER VAILABLE: {}", CommonUtil.convertObjectToString(apiResult.getMessage()));
        if (apiResult.getCode() == 0) {
            CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();
            if (responseTse.getError() == 0) {
                JSONObject json = new JSONObject();
                json.put("user", keyAndPassword.getUsername());
                json.put("userType", keyAndPassword.getUserType());
                json.put("requestType", "confirm");
                json.put("authorizeCode", keyAndPassword.getOtpCode());
                json.put("newPassword", keyAndPassword.getPassword());
                json.put("requestID", keyAndPassword.getRequestId());

                ApiResult apiResultReset = callApiRsspService.accountResetPassword(responseTse, json, request);
//            return  returnFromServer(apiResult);
                if (apiResultReset.getCode() == 0) {
                    Map<String, Object> mapResponse = (Map<String, Object>) apiResultReset.getData();
                    if ((int) mapResponse.get("error") == 0) {
                        return ResponseEntity.ok(mapResponse);
                    } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return finishPasswordReset(keyAndPassword, request);
                    } else {
                        return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
                    }
                } else if (apiResultReset.getCode() == 3005 || apiResultReset.getCode() == 3006) {
                    callApiRsspService.resetAccessTokenServer();
                    return finishPasswordReset(keyAndPassword, request);
                }
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                apiResult.setMessage(responseTse.getErrorDescription());
                return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
            }
        } else {
            log.info("ERROR RS PW FN: {}", CommonUtil.convertObjectToString(apiResult.getMessage()));
            return new ResponseEntity<>(apiResult.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @GetMapping("/systems/getCertificateAuthorities")
    public ResponseEntity getCertificateAuthorities(HttpServletResponse response, HttpServletRequest request) {

        try {
            ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
            if (apiResult.getCode() == 0) {
                CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();

                if (responseTse.getError() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("certificates", "none");
                    ApiResult apiResult2 = callApiRsspService.systemCertificateAuthority((CallApiRsspService.ResponseTse) apiResult.getData(), jsonObject, request);

                    if (apiResult2.getCode() == 0) {
                        Map<String, Object> mapResponse = (Map<String, Object>) apiResult2.getData();
                        if ((int) mapResponse.get("error") == 0) {
                            return ResponseEntity.ok(mapResponse);
                        } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                            callApiRsspService.resetAccessTokenServer();
                            return getCertificateAuthorities(response, request);
                        } else {
                            return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
                        }
                    } else if (apiResult2.getCode() == 3005 || apiResult2.getCode() == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return getCertificateAuthorities(response, request);
                    }

                } else {
                    apiResult.setMessage(responseTse.getErrorDescription());
                    return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/systems/getCertificateProfiles/{name}")
    public ResponseEntity getCertificateProfiles(HttpServletResponse response, @PathVariable String name, HttpServletRequest request) {
        try {
            ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
            if (apiResult.getCode() == 0) {
                CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();
                if (responseTse.getError() == 0) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("caName", name);
                    ApiResult apiResult2 = callApiRsspService.systemCertificateProfile((CallApiRsspService.ResponseTse) apiResult.getData(), jsonObject, request);
                    if (apiResult2.getCode() == 0) {
                        Map<String, Object> mapResponse = (Map<String, Object>) apiResult2.getData();
                        if ((int) mapResponse.get("error") == 0) {
                            return ResponseEntity.ok(mapResponse);
                        } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                            callApiRsspService.resetAccessTokenServer();
                            return getCertificateProfiles(response, name, request);
                        } else {
                            log.info("ERROR RS PW INIT API GETCODE: {}", CommonUtil.convertObjectToString(mapResponse.get("message")));
                            return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
                        }
                    } else if (apiResult2.getCode() == 3005 || apiResult2.getCode() == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return getCertificateProfiles(response, name, request);
                    }
                } else {
                    apiResult.setMessage(responseTse.getErrorDescription());
                    return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/systems/getStatesOrProvinces/{name}")
    public ResponseEntity getStatesOrProvinces(HttpServletResponse response, @PathVariable String name, HttpServletRequest request) {

        ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
        if (apiResult.getCode() == 0) {
            CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();
            if (responseTse.getError() == 0) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("country", name);
                ApiResult apiResult2 = callApiRsspService.systemStateOrProvince((CallApiRsspService.ResponseTse) apiResult.getData(), jsonObject, request);
                if (apiResult2.getCode() == 0) {
                    Map<String, Object> mapResponse = (Map<String, Object>) apiResult2.getData();
                    if ((int) mapResponse.get("error") == 0) {
                        return ResponseEntity.ok(mapResponse);
                    } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return getStatesOrProvinces(response, name, request);
                    } else {
                        log.info("ERROR RS PW INIT API GETCODE: {}", CommonUtil.convertObjectToString(mapResponse.get("message")));
                        return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
                    }
                } else if (apiResult2.getCode() == 3005 || apiResult2.getCode() == 3006) {
                    callApiRsspService.resetAccessTokenServer();
                    return getStatesOrProvinces(response, name, request);
                }
                apiResult.setMessage(responseTse.getErrorDescription());
                log.info("ERROR RS PW INIT API RESPONSE TSE: {}", CommonUtil.convertObjectToString(responseTse.getMessage()));
                return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);
            }
        } else {

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(apiResult.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/systems/getSigningProfiles")
    public ResponseEntity getSigningProfiles(HttpServletResponse response, HttpServletRequest request) {
        try {
            ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
            if (apiResult.getCode() == 0) {
                CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();

                if (responseTse.getError() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    ApiResult apiResult2 = callApiRsspService.systemSigningProfile((CallApiRsspService.ResponseTse) apiResult.getData(), jsonObject, request);
                    if (apiResult2.getCode() == 0) {
                        Map<String, Object> mapResponse = (Map<String, Object>) apiResult2.getData();
                        if ((int) mapResponse.get("error") == 0) {
                            return ResponseEntity.ok(mapResponse);
                        } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                            callApiRsspService.resetAccessTokenServer();
                            return getSigningProfiles(response, request);
                        } else {
                            log.info("ERROR RS PW INIT API GETCODE: {}", CommonUtil.convertObjectToString(mapResponse.get("message")));
                            return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
                        }
                    } else if (apiResult2.getCode() == 3005 || apiResult2.getCode() == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return getSigningProfiles(response, request);
                    }

                    apiResult.setMessage(responseTse.getErrorDescription());
                    log.info("ERROR RS PW INIT API RESPONSE TSE: {}", CommonUtil.convertObjectToString(responseTse.getMessage()));
                    return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);

                } else {
                    apiResult.setMessage(responseTse.getErrorDescription());
                    log.info("ELSE ERROR RS INIT: {}", CommonUtil.convertObjectToString(responseTse.getErrorDescription()));
                    return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
                }

            }
            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //    18-11 anh Truong keu comment ly do ko tim thay ly do su dung
//    @PostMapping("/certificate")
//    public ResponseEntity certificate(HttpServletResponse response, @RequestBody CertificateVM certificateVM, HttpServletRequest request) {
//        try {
//            log.info("request: {}", CommonUtil.convertObjectToString(certificateVM));
//
//            ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
//            if (apiResult.getCode() == 0) {
//                if (apiResult.getData() instanceof CallApiRsspService.ResponseTse) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("user", certificateVM.getEmail());
//                    jsonObject.put("userType", "USERNAME");
//                    jsonObject.put("certificateProfile", certificateVM.getCertificateProfile().getName());
//                    jsonObject.put("signingProfile", certificateVM.getSigningProfile().getName());
//                    jsonObject.put("signingProfileValue", certificateVM.getSigningProfile().getAmount());
//                    jsonObject.put("sharedMode", certificateVM.getSharedMode());
//                    jsonObject.put("SCAL", certificateVM.getScal());
//                    jsonObject.put("authMode", certificateVM.getAuthMode());
//                    jsonObject.put("multisign", certificateVM.getMultisign());
//                    jsonObject.put("email", certificateVM.getEmail());
//                    jsonObject.put("phone", certificateVM.getPhone());
//
//                    JSONObject certDetails = new JSONObject();
//                    certDetails.put("commonName", certificateVM.getCertDetails().getCommonName());
//                    certDetails.put("organization", certificateVM.getCertDetails().getOrganization());
//                    certDetails.put("organizationUnit", certificateVM.getCertDetails().getOrganizationUnit());
//                    certDetails.put("title", certificateVM.getCertDetails().getTitle());
//                    certDetails.put("email", certificateVM.getCertDetails().getEmail());
//                    certDetails.put("telephoneNumber", certificateVM.getCertDetails().getTelephoneNumber());
//                    certDetails.put("location", certificateVM.getCertDetails().getLocation());
//                    certDetails.put("stateOrProvince", certificateVM.getCertDetails().getStateOrProvince().getName());
//                    certDetails.put("country", certificateVM.getCertDetails().getCountry());
//
//                    JSONArray jsonArray = new JSONArray();
//                    for (CertificateVM.Identifications identifications : certificateVM.getCertDetails().getIdentifications()) {
//                        if (StringUtils.hasLength(identifications.getValue())) {
//                            JSONObject jsonObject1 = new JSONObject();
//                            jsonObject1.put("type", identifications.getType());
//                            jsonObject1.put("value", identifications.getValue());
//                            jsonArray.put(jsonObject1);
//                        }
//                    }
//
//                    certDetails.put("identifications", jsonArray);
//                    jsonObject.put("certDetails", certDetails);
//
//                    ApiResult apiResult2 = callApiRsspService.credentialsIssue((CallApiRsspService.ResponseTse) apiResult.getData(), jsonObject, request);
//
//                    return AppUtils.returnFromServer(apiResult2);
//                }
//            }
//
//            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);
//
//        } catch (Exception ae) {
//            ae.printStackTrace();
//            log.trace("Authentication exception trace: {}", ae);
//            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
    //    @PostMapping("/certificate")
//    public ResponseEntity ownerCreateCertificate(@RequestBody OwnerCreateVm ownerCreateVm, HttpServletRequest request) {
//        try {
//            log.info("request from client: {}", CommonUtil.convertObjectToString(ownerCreateVm));
//
//            SelfCareProperties.Certificate certificate = selfCareProperties.getCertificate();
//
//            ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
//            if (apiResult.getCode() == 0) {
//                if (apiResult.getData() instanceof CallApiRsspService.ResponseTse) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("user", ownerCreateVm.getUsername());
//                    jsonObject.put("userType", "USERNAME");
//                    jsonObject.put("certificateProfile", certificate.getProfileName());
//                    jsonObject.put("signingProfile", certificate.getSigningProfile());
//                    jsonObject.put("sharedMode", certificate.getSharedMode());
//                    jsonObject.put("SCAL", certificate.getScal());
//                    jsonObject.put("authMode", certificate.getAuthMode());
//                    jsonObject.put("multisign", certificate.getMultiSign());
//                    jsonObject.put("email", ownerCreateVm.getEmail());
//                    jsonObject.put("phone", ownerCreateVm.getPhone());
//
//                    JSONObject certDetails = new JSONObject();
//                    certDetails.put("commonName", ownerCreateVm.getFullname());
//                    certDetails.put("email", ownerCreateVm.getEmail());
//                    certDetails.put("telephoneNumber", ownerCreateVm.getPhone());
//                    certDetails.put("location", ownerCreateVm.getLocation());
//                    certDetails.put("stateOrProvince", ownerCreateVm.getStateOrProvince());
//                    certDetails.put("country", ownerCreateVm.getCountry());
//
//                    JSONArray jsonArray = new JSONArray();
//                    JSONObject jsonObject1 = new JSONObject();
//                    jsonObject1.put("type", ownerCreateVm.getIdentificationType2Tse());
//                    jsonObject1.put("value", ownerCreateVm.getIdentification());
//                    jsonArray.put(jsonObject1);
//
//                    certDetails.put("identifications", jsonArray);
//                    jsonObject.put("certDetails", certDetails);
//
//                    ApiResult apiResult2 = callApiRsspService.credentialsIssue((CallApiRsspService.ResponseTse) apiResult.getData(), jsonObject, request);
//
//                    return AppUtils.returnFromServer(apiResult2);
//                }
//            }
//
//            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
//
//        } catch (Exception ae) {
//            ae.printStackTrace();
//            log.trace("Authentication exception trace: {}", ae);
//            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
    @PostMapping("/tse/owner/create")
    public ResponseEntity ownerCreate(@RequestBody OwnerCreateVm ownerCreateVm, HttpServletRequest request) {
        try {
            log.info("request from client: {}", CommonUtil.convertObjectToString(ownerCreateVm));

            ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
            log.info("request from client apiResult: {}", CommonUtil.convertObjectToString(apiResult));
            if (apiResult.getCode() == 0) {
                CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();
                if (responseTse.getError() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", ownerCreateVm.getUsername());
//                    jsonObject.put("password", ownerCreateVm.getPassword());
                    jsonObject.put("fullname", ownerCreateVm.getFullname());
                    jsonObject.put("email", ownerCreateVm.getEmail());
                    jsonObject.put("phone", ownerCreateVm.getPhone());
                    jsonObject.put("identificationType", ownerCreateVm.getIdentificationType2Tse());
                    jsonObject.put("identification", ownerCreateVm.getIdentification());
//                    jsonObject.put("twoFactorMethod", certificateVM.getScal());
                    jsonObject.put("registerTSEEnabled", true);
                    jsonObject.put("loa", ownerCreateVm.getLoa());
                    jsonObject.put("kycEvidence", ownerCreateVm.getKycEvidence());
                    //
                    jsonObject.put("address", ownerCreateVm.getLocation());
                    jsonObject.put("stateOrProvince", ownerCreateVm.getStateOrProvince());
                    jsonObject.put("country", ownerCreateVm.getCountry());
                    jsonObject.put("registerTrialCert", true);

                    //certDetails
                    JSONObject certDetails = new JSONObject();
                    certDetails.put("commonName", ownerCreateVm.getFullname());
                    certDetails.put("email", ownerCreateVm.getEmail());
                    certDetails.put("telephoneNumber", ownerCreateVm.getPhone());
                    certDetails.put("location", ownerCreateVm.getLocation());
                    certDetails.put("stateOrProvince", ownerCreateVm.getStateOrProvince());
                    certDetails.put("country", ownerCreateVm.getCountry());

                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("type", ownerCreateVm.getIdentificationType2Tse());
                    jsonObject1.put("value", ownerCreateVm.getIdentification());
                    jsonArray.put(jsonObject1);
                    certDetails.put("identifications", jsonArray);

                    jsonObject.put("certDetails", certDetails);

                    ApiResult apiResultOwnerCreate = callApiRsspService.ownerCreate((CallApiRsspService.ResponseTse) apiResult.getData(), jsonObject, request);

                    if (apiResultOwnerCreate.getCode() == 0) {
                        Map<String, Object> mapResponse = (Map<String, Object>) apiResultOwnerCreate.getData();
                        if ((int) mapResponse.get("error") == 0 || mapResponse.get("ownerUUID") != null) {
                            //Create Owner success
                            log.info("Create Owner AND certificate Success");
                            return new ResponseEntity<>(apiResultOwnerCreate, HttpStatus.OK);
                        } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                            callApiRsspService.resetAccessTokenServer();
                            return ownerCreate(ownerCreateVm, request);
                        } else {
                            log.info("Create Owner FAIL");
                            apiResultOwnerCreate.setMessage((String) mapResponse.get("errorDescription"));
                            return new ResponseEntity<>(apiResultOwnerCreate, HttpStatus.BAD_REQUEST);
                        }

                    } else if (apiResultOwnerCreate.getCode() == 3005 || apiResultOwnerCreate.getCode() == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return ownerCreate(ownerCreateVm, request);
                    } else {
                        apiResult.setMessage(responseTse.getErrorDescription());
                        return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
                    }
                }
            }

            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.SERVICE_UNAVAILABLE);

        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tse/credentials/changeAuthInfo")
    public ResponseEntity credentialsChangeAuthInfo(HttpServletResponse response,
            @RequestBody Map<String, String> mapRequest, HttpServletRequest request
    ) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapRequest));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("requestID", mapRequest.get("requestID"));
                jsonObject.put("credentialID", mapRequest.get("credentialID"));

                if (!StringUtils.isEmpty(mapRequest.get("authMode"))) {
                    jsonObject.put("authMode", mapRequest.get("authMode"));
                }
                if (!StringUtils.isEmpty(mapRequest.get("scal"))) {
                    jsonObject.put("SCAL", mapRequest.get("scal"));
                }
                if (!StringUtils.isEmpty(mapRequest.get("authorizeCode"))) {
                    jsonObject.put("authorizeCode", mapRequest.get("authorizeCode"));
                }
                if (!StringUtils.isEmpty(mapRequest.get("shareMode"))) {
                    jsonObject.put("shareMode", mapRequest.get("shareMode"));
                }
                if (!StringUtils.isEmpty(mapRequest.get("multisign"))) {
                    jsonObject.put("multisign", mapRequest.get("multisign"));
                }

                ApiResult apiResult = callApiRsspService.credentialsChangeAuthInfo(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tse/credentials/resetPassphrase")
    public ResponseEntity credentialsResetPassphrase(HttpServletResponse response,
            @RequestBody ResetPassphraseVm resetPassphraseVm, HttpServletRequest request
    ) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(resetPassphraseVm));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("credentialID", resetPassphraseVm.getCredentialID());
                jsonObject.put("requestType", resetPassphraseVm.getRequestType());
                if ("confirm".equalsIgnoreCase(resetPassphraseVm.getRequestType())) {
                    jsonObject.put("authorizeCode", resetPassphraseVm.getAuthorizeCode());
                    jsonObject.put("requestID", resetPassphraseVm.getRequestID());
//                    jsonObject.put("newPassphrase", resetPassphraseVm.getNewPassphrase());
                }

                ApiResult apiResult = callApiRsspService.credentialsResetPassphrase(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tse/credentials/changePassphrase")
    public ResponseEntity credentialsChangePassphrase(HttpServletResponse response,
            @RequestBody ChangePassphraseVm changePassphraseVm, HttpServletRequest request
    ) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(changePassphraseVm));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("credentialID", changePassphraseVm.getCredentialID());
                jsonObject.put("oldPassphrase", changePassphraseVm.getOldPassphrase());
                jsonObject.put("newPassphrase", changePassphraseVm.getNewPassphrase());

                ApiResult apiResult = callApiRsspService.credentialsChangePassphrase(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tse/credentials/sendOTP")
    public ResponseEntity credentialsSendOtp(HttpServletResponse response,
            @RequestBody ChangeAuthInfoVm changeAuthInfoVm, HttpServletRequest request
    ) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(changeAuthInfoVm));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("credentialID", changeAuthInfoVm.getCredentialID());

                ApiResult apiResult = callApiRsspService.credentialsSendOtp(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tse/owner/change-password")
    public ResponseEntity ownerChangePassword(HttpServletResponse response,
            @RequestBody ChangePasswordVm changePasswordVm, HttpServletRequest request
    ) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(changePasswordVm));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("oldPassword", changePasswordVm.getOldPassword());
                jsonObject.put("newPassword", changePasswordVm.getNewPassword());

                ApiResult apiResult = callApiRsspService.ownerChangePassword(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tse/credentials/changeEmail")
    public ResponseEntity credentialsChangeEmail(HttpServletResponse response,
            @RequestBody Map<String, String> mapData, HttpServletRequest request
    ) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapData));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("credentialID", mapData.get("credentialID"));
                jsonObject.put("newEmail", mapData.get("newEmail"));
                jsonObject.put("requestType", mapData.get("requestType"));
                if ("confirm".equalsIgnoreCase(mapData.get("requestType"))) {
                    jsonObject.put("authorizeCode", mapData.get("authorizeCode"));
                    jsonObject.put("requestID", mapData.get("requestID"));
                    jsonObject.put("otpOldEmail", mapData.get("otpOldEmail"));
                    jsonObject.put("otpNewEmail", mapData.get("otpNewEmail"));
                }

                ApiResult apiResult = callApiRsspService.credentialsChangeEmail(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tse/credentials/changePhone")
    public ResponseEntity credentialsChangePhone(HttpServletResponse response,
            @RequestBody Map<String, String> mapData, HttpServletRequest request
    ) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapData));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("credentialID", mapData.get("credentialID"));
                jsonObject.put("newPhone", mapData.get("newPhone"));
                jsonObject.put("requestType", mapData.get("requestType"));
                if ("confirm".equalsIgnoreCase(mapData.get("requestType"))) {
                    jsonObject.put("authorizeCode", mapData.get("authorizeCode"));
                    jsonObject.put("requestID", mapData.get("requestID"));
                    jsonObject.put("otpOldPhone", mapData.get("otpOldPhone"));
                    jsonObject.put("otpNewPhone", mapData.get("otpNewPhone"));
                }

                ApiResult apiResult = callApiRsspService.credentialsChangePhone(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/owner/logging")
    public ResponseEntity ownerLogging(HttpServletResponse response, HttpServletRequest request
    ) {

        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            JSONObject json = new JSONObject();
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            ApiResult apiResult = callApiRsspService.ownerLogging(responseTse, json, request);
            log.info("LOGIN ERROR: {}", apiResult.getMessage());
            return returnFromServer(apiResult);

        }

    }

    @GetMapping("/owner/history")
    public ResponseEntity ownerHistory(
            HttpServletResponse response, HttpServletRequest request,
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "size", required = false) int size
    ) {
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED);
//        String jwt = accessToken;
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
//        String jwt = JWTFilter.resolveToken(request);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {

            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            JSONObject json = new JSONObject();
            json.put("pageNumber", page);
            log.info("size: {}", size);
            json.put("recordCount", size);

            JSONObject searchCondition = new JSONObject();
//            searchCondition.put("fromDate", "20200717000000");
//            searchCondition.put("toDate", "20200901023505");
            searchCondition.put("actions", new String[]{"LOGIN"});

            JSONObject record = new JSONObject();
            record.put("requestData", true);
            record.put("responseData", true);
            record.put("relyingParty", true);
            record.put("responseMessage", true);
            record.put("rpRequestID", true);
            record.put("requestID", true);
            record.put("responseID", true);

//            json.put("searchConditions", searchCondition);
            json.put("record", record);

            ApiResult apiResult = callApiRsspService.queriesOwnerHistory(responseTse, json, request);
            return returnFromServer(apiResult);

        }

    }

    @GetMapping("/credentials/history")
    public ResponseEntity credentialHistory(HttpServletRequest request, HttpServletResponse resp,
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "size", required = false) int size,
            @RequestParam(value = "id", required = false) String id
    ) {
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED);
//        String jwt = accessToken;
        log.info("--- TRY COOKIE ---");
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, resp, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {

            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            JSONObject json = new JSONObject();
            json.put("pageNumber", page);
            json.put("credentialID", id);
            json.put("recordCount", size);

            JSONObject record = new JSONObject();
            record.put("requestData", true);
            record.put("responseData", true);
            record.put("relyingParty", true);
            record.put("responseMessage", true);
            record.put("rpRequestID", true);
            record.put("requestID", true);
            record.put("responseID", true);
            record.put("responseID", true);

            json.put("record", record);

            ApiResult apiResult = callApiRsspService.queriesCredentialHistory(responseTse, json, request);
            return returnFromServer(apiResult);

        }

    }

    @GetMapping("/credentials/list")
    public ResponseEntity credentialList(
            HttpServletResponse response, HttpServletRequest request
    ) {

//        String jwt = JWTFilter.resolveToken(request);
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {

            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            JSONObject json = new JSONObject();
            json.put("certInfo", true);
            json.put("authInfo", true);

            JSONObject searchCondition = new JSONObject();
            searchCondition.put("certificateStatus", "ALL");
            searchCondition.put("certificatePurpose", "ALL");

            json.put("searchConditions", searchCondition);

            ApiResult apiResult = callApiRsspService.credentialsList(responseTse, json, request);
            return returnFromServer(apiResult);

        }

    }

    @GetMapping("/credentials/info")
    public ResponseEntity credentialInfo(HttpServletResponse response, HttpServletRequest request,
            @RequestParam(value = "id") String id
    ) {

        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {

            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            JSONObject json = new JSONObject();
            json.put("credentialID", id);
            json.put("certInfo", true);
            json.put("certificates", "single");
            json.put("authInfo", true);

            ApiResult apiResult = callApiRsspService.credentialsInfo(responseTse, json, request);
            return returnFromServer(apiResult);

        }

    }

    private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    @PostMapping("/credentials/upgrade")
    public ResponseEntity credentialsUpgrade(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapData));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("credentialID", mapData.get("credentialID"));
                jsonObject.put("certificateProfile", mapData.get("certificateProfile"));
                jsonObject.put("signingProfile", mapData.get("signingProfile"));
                jsonObject.put("signingProfileValue", mapData.get("signingProfileValue"));
                jsonObject.put("sharedMode", mapData.get("sharedMode"));
                jsonObject.put("authorizeCode", mapData.get("authorizeCode"));

                ApiResult apiResult = callApiRsspService.credentialsUpgrade(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/systems/getPaymentProviders")
    public ResponseEntity getPaymentProviders(HttpServletResponse response, HttpServletRequest request) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            JSONObject json = new JSONObject();
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            ApiResult apiResult = callApiRsspService.systemsGetPaymentProvider(responseTse, json, request);
            return returnFromServer(apiResult);

        }
    }

    @PostMapping("/orders/checkout")
    public ResponseEntity ordersCheckout(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapData));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {

                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("credentialID", mapData.get("credentialID"));
                jsonObject.put("requestID", mapData.get("requestID"));
                jsonObject.put("orderUUID", mapData.get("orderUUID"));
                jsonObject.put("paymentProvider", mapData.get("paymentProvider"));
                jsonObject.put("bankCode", mapData.get("bankCode"));
                jsonObject.put("returnUrl", mapData.get("returnUrl"));

                Cookie cookie = new Cookie("ipnUrl", mapData.get("ipnUrl"));
                log.info("ipnUrl URL: {}", cookie.toString());
                response.addCookie(cookie);

                ApiResult apiResult = callApiRsspService.ordersCheckout(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/credentials/renew")
    public ResponseEntity credentialsRenew(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapData));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("credentialID", mapData.get("credentialID"));
                jsonObject.put("certificateProfile", mapData.get("certificateProfile"));
                jsonObject.put("signingProfile", mapData.get("signingProfile"));
                jsonObject.put("signingProfileValue", mapData.get("signingProfileValue"));
                jsonObject.put("sharedMode", mapData.get("sharedMode"));
                jsonObject.put("authorizeCode", mapData.get("authorizeCode"));
                jsonObject.put("keepSerialEnabled", mapData.get("keepSerialEnabled"));
                jsonObject.put("keepKeyEnabled", mapData.get("keepKeyEnabled"));
                jsonObject.put("certDetails", mapData.get("certDetails"));
                jsonObject.put("notBefore", mapData.get("notBefore"));
                jsonObject.put("notAfter", mapData.get("notAfter"));
                jsonObject.put("operationMode", mapData.get("operationMode"));

                ApiResult apiResult = callApiRsspService.credentialsRenew(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/owner/sendOTP")
    public ResponseEntity ownerSendOTP(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapData));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("user", mapData.get("user"));
                jsonObject.put("userType", mapData.get("userType"));
                jsonObject.put("otpType", mapData.get("otpType"));

                ApiResult apiResult = callApiRsspService.ownerSendOTP(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/credentials/issue")
    public ResponseEntity credentialsIssue(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        try {
            log.info("request: {}", CommonUtil.convertObjectToString(mapData));
            String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
            if (jwt == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
                responseTse.setAccessToken(jwt);
                JSONObject jsonObject = new JSONObject();
                JSONObject certDetailsOb = new JSONObject();
                JSONArray identifiAr = new JSONArray();

                jsonObject.put("user", mapData.get("user"));
                jsonObject.put("userType", mapData.get("userType"));
                jsonObject.put("certificateProfile", mapData.get("certificateProfile"));
                jsonObject.put("signingProfile", mapData.get("signingProfile"));
                jsonObject.put("signingProfileValue", mapData.get("signingProfileValue"));
                jsonObject.put("SCAL", mapData.get("SCAL"));
                jsonObject.put("authMode", mapData.get("authMode"));
                jsonObject.put("multisign", mapData.get("multisign"));
                jsonObject.put("email", mapData.get("email"));
                jsonObject.put("phone", mapData.get("phone"));
                jsonObject.put("sharedMode", mapData.get("sharedMode"));

                JSONObject idenCMNDOb = new JSONObject();
                if (!("".equalsIgnoreCase(mapData.get("valueCMND")))) {

                    if ("CMND:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "PERSONAL-ID");
                        identifiAr.put(idenCMNDOb);
                    }
                    if ("HC:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "PASSPORT-ID");
                        identifiAr.put(idenCMNDOb);
                    }
                    if ("CCCD:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "CITIZEN-IDENTITY-CARD");
                        identifiAr.put(idenCMNDOb);
                    }
                    if ("MST:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "TAX-CODE");
                        identifiAr.put(idenCMNDOb);
                    }
                    if ("BHXH:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "SOCIAL-INSURANCE");
                        identifiAr.put(idenCMNDOb);
                    }
                    ////////////////////////////////////////////////////////
                    if ("PID:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "PERSONAL-ID");
                        identifiAr.put(idenCMNDOb);
                    }
                    if ("PPT:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "PASSPORT-ID");
                        identifiAr.put(idenCMNDOb);
                    }
                    if ("CZN:".equalsIgnoreCase(mapData.get("typeCMND"))) {
                        idenCMNDOb.put("value", mapData.get("valueCMND"));
                        idenCMNDOb.put("type", "CITIZEN-IDENTITY-CARD");
                        identifiAr.put(idenCMNDOb);
                    }

                }

                JSONObject idenMSTOb = new JSONObject();
                if (!("".equalsIgnoreCase(mapData.get("valueMST")))) {

                    if ("MST:".equalsIgnoreCase(mapData.get("typeMST"))) {
                        idenMSTOb.put("value", mapData.get("valueMST"));
                        idenMSTOb.put("type", "TAX-CODE");
                        identifiAr.put(idenMSTOb);
                    }
                    if ("MNS:".equalsIgnoreCase(mapData.get("typeMST"))) {
                        idenMSTOb.put("value", mapData.get("valueMST"));
                        idenMSTOb.put("type", "BUDGET-CODE");
                        identifiAr.put(idenMSTOb);
                    }
                    if ("QĐ:".equalsIgnoreCase(mapData.get("typeMST"))) {
                        idenMSTOb.put("value", mapData.get("valueMST"));
                        idenMSTOb.put("type", "DECISION-CODE");
                        identifiAr.put(idenMSTOb);
                    }
                    if ("BHXH:".equalsIgnoreCase(mapData.get("typeMST"))) {
                        idenMSTOb.put("value", mapData.get("valueMST"));
                        idenMSTOb.put("type", "SOCIAL-INSURANCE");
                        identifiAr.put(idenMSTOb);
                    }
                    if ("MDV:".equalsIgnoreCase(mapData.get("typeMST"))) {
                        idenMSTOb.put("value", mapData.get("valueMST"));
                        idenMSTOb.put("type", "UNIT-CODE");
                        identifiAr.put(idenMSTOb);
                    }

                    ////////////////////////////////////////////////////////
                    if ("TIN:".equalsIgnoreCase(mapData.get("typeMST"))) {
                        idenMSTOb.put("value", mapData.get("valueMST"));
                        idenMSTOb.put("type", "TAX-CODE");
                        identifiAr.put(idenMSTOb);
                    }
                    if ("BDG:".equalsIgnoreCase(mapData.get("typeMST"))) {
                        idenMSTOb.put("value", mapData.get("valueMST"));
                        idenMSTOb.put("type", "BUDGET-CODE");
                        identifiAr.put(idenMSTOb);
                    }
                }

                certDetailsOb.put("commonName", mapData.get("commonName"));
                certDetailsOb.put("organization", mapData.get("organization"));
                certDetailsOb.put("organizationUnit", mapData.get("organizationUnit"));
                certDetailsOb.put("title", mapData.get("title"));
                certDetailsOb.put("email", mapData.get("email"));
                certDetailsOb.put("telephoneNumber", mapData.get("telephoneNumber"));
                certDetailsOb.put("location", mapData.get("location"));
                certDetailsOb.put("stateOrProvince", mapData.get("stateOrProvince"));
                certDetailsOb.put("country", mapData.get("country"));
                certDetailsOb.put("identifications", identifiAr);
                jsonObject.put("certDetails", certDetailsOb);

                ApiResult apiResult = callApiRsspService.credentialsIssue(responseTse, jsonObject, request);

                return returnFromServer(apiResult);

            }
        } catch (Exception ae) {
            ae.printStackTrace();
            log.trace("Authentication exception trace: {}", ae);
            return new ResponseEntity<>(ApiResult.fail("SERVER UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/systems/getCountries")
    public ResponseEntity systemsGetCountries(HttpServletResponse response, HttpServletRequest request) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            JSONObject json = new JSONObject();
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            ApiResult apiResult = callApiRsspService.systemsGetCountries(responseTse, json, request);
            return returnFromServer(apiResult);

        }
    }

    @PostMapping("/systems/getStatesOrProvinces")
    public ResponseEntity systemsGetStatesOrProvinces(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            log.info("GET STATE OR : {}", CommonUtil.convertObjectToString(mapData));

            JSONObject json = new JSONObject();
            json.put("country", mapData.get("country"));

            ApiResult apiResult = callApiRsspService.systemsGetStatesOrProvinces(responseTse, json, request);
            return returnFromServer(apiResult);

        }
    }

    @PostMapping("/credentials/enroll")
    public ResponseEntity credentialsEnroll(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            log.info("CREDENTIALS ENROLL : {}", CommonUtil.convertObjectToString(mapData));

            JSONObject json = new JSONObject();
            json.put("credentialID", mapData.get("id"));

            ApiResult apiResult = callApiRsspService.credentialsEnroll(responseTse, json, request);
            return returnFromServer(apiResult);
        }
    }

    @PostMapping("/credentials/approve")
    public ResponseEntity credentialsApprove(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            log.info("CREDENTIALS APPROVE : {}", CommonUtil.convertObjectToString(mapData));

            JSONObject json = new JSONObject();
            json.put("credentialID", mapData.get("id"));

            ApiResult apiResult = callApiRsspService.credentialsApprove(responseTse, json, request);
            return returnFromServer(apiResult);
        }
    }

    @PostMapping("/credentials/import")
    public ResponseEntity credentialsImport(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, String> mapData) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);
            log.info("CREDENTIALS IMPORT : {}", CommonUtil.convertObjectToString(mapData));

            JSONObject json = new JSONObject();
            json.put("credentialID", mapData.get("id"));
            json.put("certificate", mapData.get("cts"));

            ApiResult apiResult = callApiRsspService.credentialsImport(responseTse, json, request);
            return returnFromServer(apiResult);
        }

    }

    @PostMapping("/auth/preLogin")
    public ResponseEntity getPreLogin(HttpServletResponse response,
            HttpServletRequest request,
            @Valid @RequestBody PreLogin preLogin) {
//        JWTFilter.removeCookie("ssoEnabled", response);
        ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);

        log.info("PRE LOGIN: {}", CommonUtil.convertObjectToString(preLogin));

        log.info("PRE LOGIN APIRESULT: {}", CommonUtil.convertObjectToString(apiResult));

        if (apiResult.getCode() == 0) {
            CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();
            log.info("LOG RES AUTH PRE: {}", CommonUtil.convertObjectToString(responseTse));
            if (responseTse.getError() == 0) {

                JSONObject json = new JSONObject();

                json.put("user", preLogin.getUser());
                json.put("userType", preLogin.getUserType());
                json.put("ssoInfo", preLogin.isSsoInfo());

                ApiResult apiResultReset = callApiRsspService.preLogin(responseTse, json, request);
                log.info("==================================================================");
                log.info("CALL PRELOGIN 1: {}", CommonUtil.convertObjectToString(apiResultReset));
                int code = apiResultReset.getCode();
                String state = "";
                String scope = "";
                String clientId = "";
                String authUrl = "";
                String tokenUrl = "";
                if (apiResultReset.getCode() == 0) {
                    Map<String, Object> mapResponse = (Map<String, Object>) apiResultReset.getData();
                    log.info("CALL PRELOGIN 2: {}", CommonUtil.convertObjectToString(mapResponse));

                    if ((int) mapResponse.get("error") == 0) {
                        try {
                            if ("TSE".equalsIgnoreCase((String) mapResponse.get("twoFactorMethod"))
                                    || "NONE".equalsIgnoreCase((String) mapResponse.get("twoFactorMethod"))) {
                                String signData = RandomStringUtils.random(20, true, true) + System.currentTimeMillis();
                                log.info("SignData: {}", signData);
                                String hashAlg = Crypto.HASH_SHA256;
                                switch (selfCareProperties.getNotification().getHashAlgorithmOID()) {
                                    case "1.3.14.3.2.26":
                                        hashAlg = Crypto.HASH_SHA1;
                                        break;
                                    case "2.16.840.1.101.3.4.2.1":
                                        hashAlg = Crypto.HASH_SHA256;
                                        break;
                                    case "2.16.840.1.101.3.4.2.2":
                                        hashAlg = Crypto.HASH_SHA384;
                                        break;
                                    case "2.16.840.1.101.3.4.2.3":
                                        hashAlg = Crypto.HASH_SHA512;
                                        break;
                                    case "2.16.840.1.101.3.4.2.8":
                                        hashAlg = Crypto.HASH_SHA3_384;
                                        break;
                                    case "2.16.840.1.101.3.4.2.9":
                                        hashAlg = Crypto.HASH_SHA3_512;
                                        break;
                                }
                                byte[] hashSignData = Crypto.hashData(signData.getBytes(), hashAlg);
                                String b64Hash = Base64.getEncoder().encodeToString(hashSignData);

                                String vc = TSECryptoHelper.computeVC(new byte[][]{hashSignData});

                                SelfCareProperties.Notification notification = selfCareProperties.getNotification();

                                notification.setVc(vc);
                                log.info("vCode: ", vc);
                                notification.setHashValue(b64Hash);
                                notification.setMethod((String) mapResponse.get("twoFactorMethod"));

                                if ((boolean) mapResponse.get("ssoEnabled") == true) {
                                    log.info("ssoEndbled == true");
                                    state = (String) mapResponse.get("state");
                                    scope = (String) mapResponse.get("scope");
                                    Map<String, Object> mapRes2 = (Map<String, Object>) mapResponse.get("sso");
                                    clientId = (String) mapRes2.get("clientId");
                                    authUrl = (String) mapRes2.get("authUrl");
                                    tokenUrl = (String) mapRes2.get("tokenUrl");

                                    notification.setSsoEnabled("true");
                                    notification.setCode(code);
                                    notification.setState(state);
                                    notification.setScope(scope);
                                    notification.setAuthUrl(authUrl);
                                    notification.setTokenUrl(tokenUrl);
                                    notification.setClientId(clientId);
                                } else {
                                    notification.setSsoEnabled("false");
                                }

                                return new ResponseEntity<>(notification, HttpStatus.OK);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                        callApiRsspService.resetAccessTokenServer(); // lỗi thì reset token rồi gọi lại
                        return getPreLogin(response, request, preLogin);
                    } else {
                        return new ResponseEntity<>(mapResponse, HttpStatus.BAD_REQUEST);
                    }
                } else if (apiResultReset.getCode() == 3005 || apiResultReset.getCode() == 3006) {
                    callApiRsspService.resetAccessTokenServer(); // lỗi thì reset token rồi gọi lại
                    return getPreLogin(response, request, preLogin);
                }
                return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);

            } else {
                apiResult.setMessage(responseTse.getErrorDescription());
                return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
            }
        } else {
            log.info("ERROR RS PW FN: {}", CommonUtil.convertObjectToString(apiResult.getMessage()));
            return new ResponseEntity<>(apiResult.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/request/redirectLoginSSO")
    public ResponseEntity redirectLoginSSO(HttpServletResponse response,
            HttpServletRequest request,
            @RequestBody Map<String, String> mapData,
            Model model) {
        Cookie cookie = new Cookie("locationLink", mapData.get("locationLink"));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>(mapData, HttpStatus.OK);

    }

    @PostMapping("/login-sso-isTse")
    public ResponseEntity postLoginSSOisTse(HttpServletResponse response,
            HttpServletRequest request,
            @RequestBody Map<String, String> mapData) {
        log.info("==================================================================");
        log.info("RES isCOUNT: {}", CommonUtil.convertObjectToString(mapData.get("mapData")));

        JSONObject OBmapDataB = new JSONObject(mapData.get("mapData"));
        log.info("JSON OB DB: {}", OBmapDataB);

        String hashValue = "";
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if ("ckHashValue".equals(cookies[i].getName())) {
                hashValue = cookies[i].getValue().toString();
            }
        }
        try {

            ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
            if (apiResult.getCode() == 0) {
                CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();
                if (responseTse.getError() == 0) {
                    JSONObject json = new JSONObject();
                    json.put("state", OBmapDataB.get("state"));
                    json.put("code", OBmapDataB.get("code"));
                    json.put("redirectUri", OBmapDataB.get("redirectUri"));
                    json.put("rememberMe", OBmapDataB.get("rememberMe"));

                    SelfCareProperties.Notification notification = null;
                    notification = selfCareProperties.getNotification();

                    ApiResult apiResultReset = callApiRsspService.loginSSO(responseTse, notification, json, true, request);

                    if (apiResultReset.getCode() == 0) {
                        Map<String, Object> mapResponse = (Map<String, Object>) apiResultReset.getData();
                        if ((int) mapResponse.get("error") == 0) {
                            log.info("==================================================================");
                            log.info("AFTER LOGINSSO RESPONSE - ssoEnabled : {}", CommonUtil.convertObjectToString(mapResponse));
                            log.info("AFTER LOGINSSO RESPONSE - ssoEnabled 2 : {}", (String) mapResponse.get("ssoEnabled"));

                            Cookie cookie = new Cookie("ssoEnabled", "true");
                            cookie.setSecure(true);
                            cookie.setHttpOnly(true);
                            cookie.setPath("/SelfCare");
                            response.addCookie(cookie);

                            JWTFilter.setCookie(mapResponse, response, true);

                            return new ResponseEntity(mapResponse, HttpStatus.OK);
                        }

                    } else if (apiResultReset.getCode() == 3005 || apiResultReset.getCode() == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return postLoginSSOisTse(response, request, mapData);
                    } else if (apiResultReset.getCode() == 3247) {
                        callApiRsspService.resetAccessTokenServer();
                        return postLoginSSOisTse(response, request, mapData);
                    }
                } else {
                    return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(apiResult, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(mapData, HttpStatus.OK);
    }

    @PostMapping("/auth/login-with-qr")
    public ResponseEntity<?> loginWithQR(
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestBody Map<String, String> mapData) {

        JSONObject json = new JSONObject();
        json.put("action", "LOGIN");

        ApiResult apiResult = callApiRsspService.qrCodeSendRequest(json, request);

        log.info("Check login with RQ: {}", CommonUtil.convertObjectToString(apiResult));

        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @PostMapping("/auth/qr-getResult")
    public ResponseEntity<?> loginQRGetResult(
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestBody Map<String, String> mapData
    //            @PathVariable String qrCodeUUID,
    //            @PathVariable String requestID
    ) throws Exception {
        try {
            JSONObject json = new JSONObject();
            json.put("qrCodeUUID", mapData.get("qrCodeUUID"));
            json.put("requestID", mapData.get("requestID"));
            ApiResult apiResult = callApiRsspService.qrCodeGetResult(json, request, response);
            log.info("qr get Result: {}", CommonUtil.convertObjectToString(apiResult));
            return new ResponseEntity<>(apiResult, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ApiResult.fail(), HttpStatus.BAD_REQUEST);
        }
    }

}
