/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.selfcare.rest;

import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import vn.mobileid.selfcare.service.CallApiRsspService;
import vn.mobileid.selfcare.utils.SelfCareProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import javax.servlet.http.Cookie;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.selfcare.jwt.JWTFilter;
import vn.mobileid.selfcare.utils.ApiResult;
import vn.mobileid.selfcare.utils.CommonUtil;
import vn.mobileid.selfcare.utils.SSLUtilities;

/**
 *
 * @author Mobile ID 21
 */
@Controller
public class UpgradeRest {

    private final Logger log = LoggerFactory.getLogger(UpgradeRest.class);

    private final CallApiRsspService callApiRsspService;

    private final SelfCareProperties selfCareProperties;

    private final AuthenticationManager authenticationManager;

    private final String URL_UPGRADE_RESPONSE = "/ipns/uatVnpay";

    public UpgradeRest(CallApiRsspService callApiRsspService, SelfCareProperties selfCareProperties, AuthenticationManager authenticationManager) {
        this.callApiRsspService = callApiRsspService;
        this.selfCareProperties = selfCareProperties;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "/testResponse", method = RequestMethod.GET)
    public String testResponse(HttpServletRequest request) {
        String getLG = getCookieLanguage(request);
        if (getLG == "VN") {
            return "app/credential/detail/upgrade/resTemplate/res-md-new";
        } else {
            return "app/credential/detail/upgrade/resTemplate/res-md";
        }

    }

    @RequestMapping(value = "/response{param}", method = RequestMethod.GET)
    public String responseData(HttpServletResponse response, HttpServletRequest request, @RequestParam Map<String, String> param,
            Model model) {

        log.info("refesh: {}", CommonUtil.convertObjectToString(param));

        String mapData = request.getQueryString();
        log.info("RESPONSE POST DATA: {}", mapData);

        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            ResponseEntity responseEntity = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            model.addAttribute(responseEntity);
            String getLG = getCookieLanguage(request);

            if (getLG == "VN") {
                return "app/credential/detail/upgrade/resTemplate/res-md-new";
            } else {
                return "app/credential/detail/upgrade/resTemplate/res-md";
            }
        } else {
            JSONObject jsonObject = new JSONObject();
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            RestTemplate restTemplate = new RestTemplate();
            String fullUrl = selfCareProperties.getUrlPay() + URL_UPGRADE_RESPONSE + "?" + mapData;
            log.info("URL FULL: {}", fullUrl);

            jsonObject.put("vnp_Amount", param.get("vnp_Amount"));
            jsonObject.put("vnp_OrderInfo", param.get("vnp_OrderInfo"));

            log.info("AMOUNT: {}", param.get("vnp_Amount"));
            String pattern = "###,###";
            DecimalFormat formatPhanTram = new DecimalFormat(pattern);
            String amount = formatPhanTram.format((Integer.parseInt(param.get("vnp_Amount")) / 100));

//            String amount = formatPhanTram.format(param.get("vnp_Amount"));
            ResponseEntity<Map> rsp = restTemplate.exchange(fullUrl, HttpMethod.GET, HttpEntity.EMPTY, Map.class);

            int rspCode = Integer.parseInt(rsp.getBody().get("RspCode").toString());
            log.info("RspCode: " + rspCode);
            log.info("response url {}: {}", URL_UPGRADE_RESPONSE, CommonUtil.convertObjectToString(rsp.getBody()));

            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
            model.addAttribute(responseEntity);
            model.addAttribute("vnp_Amount", amount);
            model.addAttribute("vnp_OrderInfo", param.get("vnp_OrderInfo"));
            model.addAttribute("message", rsp.getBody().get("Message"));
            model.addAttribute("error", param.get("error"));
            model.addAttribute("rspCode", rspCode);

            log.info("ERROR: {}", param.get("error"));

            String getLG = getCookieLanguage(request);

            log.info("Upgrade Language: {}", getLG);

            if (getLG == "VN") {
                return "app/credential/detail/upgrade/resTemplate/res-md-new";
            } else {
                return "app/credential/detail/upgrade/resTemplate/res-md";
            }

        }
    }

    @RequestMapping(value = "/after-preLogin{mapData}", method = RequestMethod.GET)
    public String getDerectLoginSSO(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Map<String, String> mapData,
            Model model, HttpMethod method
    ) {

        ApiResult apiResult = callApiRsspService.getAccessTokenForRp(request);
        log.info("After Pre Login SSO map: {}", CommonUtil.convertObjectToString(mapData));
        SelfCareProperties.Notification notification = null;
        if (apiResult.getCode() == 0) {
            CallApiRsspService.ResponseTse responseTse = (CallApiRsspService.ResponseTse) apiResult.getData();
            if (responseTse.getError() == 0) {
                JSONObject json = new JSONObject();

                json.put("state", mapData.get("state"));
                json.put("code", mapData.get("code"));
//                json.put("redirectUri", "https://192.168.5.112:8643/SelfCare/after-preLogin");

                json.put("rememberMe", true);

                Cookie[] cookies = request.getCookies();
                String getTwoFactor = "";
                String vc = "";
                String hashValue = "";
                String locationLink = "";
                log.info("============ PRELOGIN AFTER LOSIN SSO ============");

                for (int i = 0; i < cookies.length; i++) {
                    log.info("{} - {} ", cookies[i].getName(), cookies[i].getValue());
                    if ("twoFactorMethod".equals(cookies[i].getName())) {
                        getTwoFactor = cookies[i].getValue().toString();
                    }
                    if ("vCode".equals(cookies[i].getName())) {
                        vc = cookies[i].getValue().toString();
                    }
                    if ("ckHashValue".equals(cookies[i].getName())) {
                        hashValue = cookies[i].getValue().toString();
                    }
                    if ("locationLink".equals(cookies[i].getName())) {
                        locationLink = cookies[i].getValue().toString();
                    }

                }

                json.put("redirectUri", locationLink + "after-preLogin");

                boolean isTse = false;

                notification = selfCareProperties.getNotification();
                log.info("Notification: {}", CommonUtil.convertObjectToString(notification));

                if ("TSE".equals(notification.getMethod())) {
                    isTse = true;

                    JSONObject jsonTseNotification = new JSONObject();
                    jsonTseNotification.put("notificationMessage", notification.getNotificationMessage());
                    jsonTseNotification.put("messageCaption", notification.getMessageCaption());
                    jsonTseNotification.put("message", notification.getMessage());
                    jsonTseNotification.put("vcEnabled", notification.isVcEnabled());
                    jsonTseNotification.put("validityPeriod", notification.getValidityPeriod());
                    jsonTseNotification.put("hashes", notification.getHashValue());
                    jsonTseNotification.put("hashAlgorithmOID", notification.getHashAlgorithmOID());
                    jsonTseNotification.put("vc", notification.getVc());
                    json.put("tseNotification", jsonTseNotification);
                    log.info("tseNotification: {}", CommonUtil.convertObjectToString(jsonTseNotification));
                    try {
                        String userAgent = request.getHeader("User-Agent");
                        HttpHeaders headers = new HttpHeaders();

                        if (responseTse.isNeedAuthenticate()) {
                            if (StringUtils.isNotEmpty(responseTse.getAuthenSsl()) && StringUtils.isNotEmpty(responseTse.getAuthenBasic())) {
                                headers.set("Authorization", "SSL2 " + responseTse.getAuthenSsl() + ",Basic " + responseTse.getAuthenBasic()); // token ng dùng 
                            } else if (StringUtils.isNotEmpty(responseTse.getAuthenSsl())) {
                                headers.set("Authorization", "SSL2 " + responseTse.getAuthenSsl());                                             // token server
                            } else if (StringUtils.isNotEmpty(responseTse.getAuthenBasic())) {
                                headers.set("Authorization", "Basic " + responseTse.getAuthenBasic());                                          // 
                            }
                        } else {
                            headers.set("Authorization", "Bearer " + responseTse.getAccessToken());
                        }

                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("User-Agent", userAgent);
                        log.info("PROFILE: {}", selfCareProperties.getProfile());
                        log.info("LANG: {}", getCookieLanguage(request));

                        json.put("profile", selfCareProperties.getProfile());
                        json.put("lang", getCookieLanguage(request));

                        Cookie cookie = new Cookie("ssoEnabled", "true");
                        cookie.setSecure(true);
                        cookie.setHttpOnly(true);
                        cookie.setPath("/SelfCare");
                        response.addCookie(cookie);

                        HttpEntity<String> requestData = new HttpEntity<String>(json.toString(), headers);
                        model.addAttribute("validityPeriod", notification.getValidityPeriod());
                        model.addAttribute("ckLosinSSO", requestData.getBody());
                        model.addAttribute("vCode", notification.getVc());

                        String getLG = getCookieLanguage(request);

                        log.info("Upgrade Language: {}", getLG);

                        if (getLG == "VN") {
                            return "app/components/login/loginSSO/login-sso-tse-vn";
                        } else {
                            return "app/components/login/loginSSO/login-sso-tse";
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        log.info("LOG ERROR !");
                        log.error("LOG ERROR: {}", e.getMessage());
                    }

                } else {
                    isTse = false;

                    ApiResult apiResultReset = callApiRsspService.loginSSO(responseTse, notification, json, isTse, request);
                    log.info("==================================================================");
                    log.info("AFTER LOGINSSO RESPONSE apiResultReset: {}", CommonUtil.convertObjectToString(apiResultReset));
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

                            return "app/components/login/loginSSO/login-sso";
                            
                        } else if ((int) mapResponse.get("error") == 3001) {
                            model.addAttribute("loginSSOErr", "3001");
                            model.addAttribute("loginSSOErrMess", (String)mapResponse.get("errorDescription"));
                            

                            return "app/components/login/loginSSO/login-sso";
                        }

                    } else if (apiResultReset.getCode() == 3005 || apiResultReset.getCode() == 3006) {
                        callApiRsspService.resetAccessTokenServer();
                        return getDerectLoginSSO(request, response, mapData, model, method);
                    } else if (apiResultReset.getCode() == 3247) {
                        callApiRsspService.resetAccessTokenServer();
                        return getDerectLoginSSO(request, response, mapData, model, method);
                    }
                    return "app/components/login/loginSSO/login-sso";
                }
            } else {
                apiResult.setMessage(responseTse.getErrorDescription());
                return "app/components/login/loginSSO/login-sso";
            }
        } else {
            log.info("ERROR RS PW FN: {}", CommonUtil.convertObjectToString(apiResult.getMessage()));
//            new ResponseEntity<>(apiResult.getMessage(), HttpStatus.SERVICE_UNAVAILABLE)
            return "app/components/login/loginSSO/login-sso";
        }
        return null;
    }

    @RequestMapping(value = "/login-sso-tse", method = RequestMethod.GET)
    public String getLoginSSOisTse(Model model) {
        model.addAttribute("vCode", "ABC-XYZ");
        model.addAttribute("vTime", "120");
        return "app/components/login/loginSSO/login-sso-tse";
    }

    private String getCookieLanguage(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                log.info("{} - {} ", cookies[i].getName(), cookies[i].getValue());
                if ("NG_TRANSLATE_LANG_KEY".equalsIgnoreCase(cookies[i].getName())) {
                    log.info("key: {}", cookies[i].getValue());
                    if ("%22en%22".equalsIgnoreCase(cookies[i].getValue())) {
                        return "EN";
                    } else {
                        return "VN";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
        return "EN";
    }

}
