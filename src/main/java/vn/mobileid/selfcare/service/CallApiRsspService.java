package vn.mobileid.selfcare.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.selfcare.crypto.PKCS1Util;
import vn.mobileid.selfcare.rest.vm.LoginVM;
import vn.mobileid.selfcare.rest.vm.PreLogin;

import vn.mobileid.selfcare.utils.ApiResult;
import vn.mobileid.selfcare.utils.CommonUtil;
import vn.mobileid.selfcare.utils.SSLUtilities;
import vn.mobileid.selfcare.utils.SelfCareProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class CallApiRsspService {

    private final int DIFF = 1 * 60 * 1000;

    private final String URL_LOGIN = "/auth/login";
    private final String URL_PRE_LOGIN = "/auth/preLogin";
    private final String URL_LOGIN_SSO = "/auth/loginSSO";
    private final String URL_LOGIN_QR_SEND_REQUEST = "/qrCode/sendRequest";
    private final String URL_LOGIN_QR_GET_RESULT = "/qrCode/getResult";
    private final String URL_REVOKE = "/auth/revoke";
    private final String URL_AUTH_2FACTOREMETHOD = "/auth/getTwoFactorMethod";
    private final String URL_OWNER_SYSNCANTICLONEOTP = "/owner/syncAnticloningOTP";
    private final String URL_OWNER_CHANGE_PASSWORD = "/owner/changePassword";
    private final String URL_CREDENTIALS_AUTHORIZE = "/credentials/authorize";
    private final String URL_SIGNATURES_SIGNHASH = "/signatures/signHash";
    private final String URL_CREDENTIALS_INFO = "/credentials/info";
    private final String URL_CREDENTIALS_LIST = "/credentials/list";
    private final String URL_CREDENTIALS_CHANGE_AUTH_INFO = "/credentials/changeAuthInfo";
    private final String URL_CREDENTIALS_RESET_PASSPHRASE = "/credentials/resetPassphrase";
    private final String URL_CREDENTIALS_CHANGE_PASSPHRASE = "/credentials/changePassphrase";
    private final String URL_CREDENTIALS_SEND_OTP = "/credentials/sendOTP";
    private final String URL_CREDENTIALS_UPGRADE = "/credentials/upgrade";
    private final String URL_CREDENTIALS_RENEW = "/credentials/renew";
    private final String URL_CREDENTIALS_ENROLL = "/credentials/enroll";
    private final String URL_CREDENTIALS_APPROVE = "/credentials/approve";
    private final String URL_CREDENTIALS_IMPORT = "/credentials/import";

    private final String URL_OWNER_INFO = "/owner/info";
    private final String URL_REFRESH_TOKEN = "/auth/login"; //get accesstoken from refreshtoken
    private final String URL_OWNER_LOGGING = "/owner/logging";
    private final String URL_OWNER_CREATE = "/owner/create";
    private final String URL_OWNER_HISTORY = "/queries/owner/history";
    private final String URL_CREDENTIAL_HISTORY = "/queries/credential/history";
    private final String URL_ACCOUNT_RESET_PASSWORD = "/owner/resetPassword";

    private final String URL_SYSTEM_CERTIFICATE_AUTHORITY = "/systems/getCertificateAuthorities";
    private final String URL_SYSTEM_CERTIFICATE_PROFILES = "/systems/getCertificateProfiles";
    private final String URL_SYSTEM_SIGNING_PROFILES = "/systems/getSigningProfiles";
    private final String URL_SYSTEM_GET_PAYMENT_PROVIDER = "/systems/getPaymentProviders ";
    private final String URL_SYSTEM_GET_COUNTRIES = "/systems/getCountries";
    private final String URL_SYSTEM_GET_STATES_OR_PROVINCES = "/systems/getStatesOrProvinces";
//    
    private final String URL_UPGRADE_RESPONSE = "/ipns/uatVnpay";

    private final String URL_SYSTEM_COUNTRIES = "/systems/getCountries";
    private final String URL_SYSTEM_STATE_OR_PROVINCES = "/systems/getStatesOrProvinces";
    private final String URL_CREDENTIAL_ISSUE = "/credentials/issue";
    private final String URL_CREDENTIAL_CHANGE_EMAIL = "/credentials/changeEmail";
    private final String URL_CREDENTIAL_CHANGE_PHONE = "/credentials/changePhone";

    private final String URL_OWNER_CHANGE_EMAIL = "/owner/changeEmail";
    private final String URL_OWNER_CHANGE_INFO = "/owner/changeInfo";

    private final String URL_ORDERS_CHECKOUT = "/orders/checkout";
    private final String URL_ORDER_LIST = "/orders/list";
    private final String URL_ORDER_DETAIL = "/orders/info";

    private final String URL_OWNER_SEND_OTP = "/owner/sendOTP";

//    public final String USER_TYPE_USERNAME = "USERNAME";
//    public final String USER_TYPE_PERSONAL_ID = "PERSONAL-ID";
//    public final String USER_TYPE_PASSPORT_ID = "PASSPORT-ID";
//    public final String USER_TYPE_CITIZEN_IDENTITY_CARD = "CITIZEN-IDENTITY-CARD";
//    public final String USER_TYPE_BUDGET_CODE = "BUDGET-CODE";
//    public final String USER_TYPE_TAX_CODE = "TAX-CODE";
    @Autowired
    private SelfCareProperties selfCareProperties;

    private final ResourceLoader resourceLoader;

    public CallApiRsspService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        log.info("============ Init CallApiRsspService ============");
    }

    private static Date date = new Date();
    private static ApiResult apiResult = null;

    private String accessTokenServer = null;

    public void resetAccessTokenServer() {
        this.accessTokenServer = null;
    }

    public synchronized ApiResult getAccessTokenForRp(HttpServletRequest rq) {
        long diff = new Date().getTime() - date.getTime();
        log.info("getAccessToken: {}", diff);
        if (diff > DIFF) {
            apiResult = loginRp(rq);
            return apiResult;
        } else {
            if (apiResult == null) {
                log.info("apiResult == null");
                apiResult = loginRp(rq);
                return apiResult;
            } else if (apiResult.getCode() != 0) {
                log.info("code != 0");
                apiResult = loginRp(rq);
                return apiResult;
            } else {
                log.info("apiResult");
                return apiResult;
            }

        }

    }

    public synchronized ApiResult getAccessTokenForRp2(HttpServletRequest rq) {

        long diff = new Date().getTime() - date.getTime();
        log.info("getAccessToken: {}", diff);
        if (diff > DIFF) {
            apiResult = loginRp(rq);
            return apiResult;
        } else {
            if (apiResult == null) {
                log.info("apiResult == null");
                apiResult = loginRp(rq);
                return apiResult;
            } else if (apiResult.getCode() != 0) {
                log.info("code != 0");
                apiResult = loginRp(rq);
                return apiResult;
            } else {
                log.info("apiResult");
                return apiResult;
            }

        }

    }

    private synchronized ApiResult loginRp(HttpServletRequest rq) {
        if (this.accessTokenServer != null) {
            ResponseTse responseTse2 = new ResponseTse();
            responseTse2.setAccessToken(this.accessTokenServer);
            return ApiResult.success(responseTse2);
        } else {
            ResponseTse responseTse = new ResponseTse();
            responseTse.setNeedAuthenticate(true);
            responseTse.setAuthenSsl(getSsl2());

            JSONObject json = new JSONObject();
            json.put("relyingParty", selfCareProperties.getRelyingPartyName());

            //Set countTime
            date = new Date();
            ApiResult apiResult = getApiResult(responseTse, json, rq, URL_LOGIN, HttpMethod.POST);

            Map<String, Object> mapResponseToken = (Map<String, Object>) apiResult.getData();

            if ((int) mapResponseToken.get("error") == 0) {
                ResponseTse responseTse2 = new ResponseTse();
                responseTse2.setAccessToken((String) mapResponseToken.get("accessToken"));
                this.accessTokenServer = (String) mapResponseToken.get("accessToken");
                return ApiResult.success(responseTse2);
            }
//        else {
//            log.info("FAIL {}");
//            //ResponseTse responseTse2 = new ResponseTse();
//            //responseTse2.setAccessToken((String) mapResponseToken.get("accessToken"));
//            //this.accessTokenServer = (String) mapResponseToken.get("accessToken");
//            return ApiResult.fail(401, (String) mapResponseToken.get("errorDescription"));
//        }
            return apiResult;
        }
    }

    public ApiResult qrCodeSendRequest(JSONObject json, HttpServletRequest rq) {
        ResponseTse responseTse = new ResponseTse();
        responseTse.setNeedAuthenticate(true);
        responseTse.setAuthenSsl(getSsl2());

        json.put("relyingParty", selfCareProperties.getRelyingPartyName());
        json.put("lang", getCookieLanguage(rq));
        json.put("profile", selfCareProperties.getProfile());

        date = new Date();
        ApiResult apiResult = getApiResult(responseTse, json, rq, URL_LOGIN_QR_SEND_REQUEST, HttpMethod.POST);

        log.info("Check Login QR Code send rq1: {}", CommonUtil.convertObjectToString(apiResult));

        return apiResult;
    }

    public synchronized ApiResult qrCodeGetResult(JSONObject json, HttpServletRequest rq, HttpServletResponse response) throws Exception {

        ResponseTse responseTse = new ResponseTse();
        responseTse.setNeedAuthenticate(true);
        responseTse.setAuthenSsl(getSsl2());

        json.put("relyingParty", selfCareProperties.getRelyingPartyName());
        json.put("lang", getCookieLanguage(rq));
        json.put("profile", selfCareProperties.getProfile());

        ApiResult apiResult = getApiResult(responseTse, json, rq, URL_LOGIN_QR_GET_RESULT, HttpMethod.POST);
        log.info("Check login QR apiResult: {}", CommonUtil.convertObjectToString(apiResult));
        Map<String, Object> mapResponseToken = (Map<String, Object>) apiResult.getData();
        log.info("Check login QR CARS: {}", CommonUtil.convertObjectToString(mapResponseToken));
        if ((int) mapResponseToken.get("error") == 0) {
            ResponseTse responseTse2 = new ResponseTse();
            responseTse2.setAccessToken((String) mapResponseToken.get("accessToken"));
            responseTse2.setResponseID((String) mapResponseToken.get("responseID"));
            this.accessTokenServer = (String) mapResponseToken.get("accessToken");

            Cookie cookieAccessToken = new Cookie("accessToken", (String) mapResponseToken.get("accessToken"));
            cookieAccessToken.setSecure(true);
            cookieAccessToken.setHttpOnly(true);
            cookieAccessToken.setPath("/");
            response.addCookie(cookieAccessToken);

            Cookie cookieRefresh = new Cookie("refreshToken", (String) mapResponseToken.get("refreshToken"));
            cookieRefresh.setMaxAge(60 * 60 * 24 * 30);
            cookieRefresh.setSecure(true);
            cookieRefresh.setHttpOnly(true);
            cookieRefresh.setPath("/");
            response.addCookie(cookieRefresh);

            return ApiResult.success(apiResult);
        }
        return apiResult;

    }

    public ApiResult loginUser(LoginVM loginVM, SelfCareProperties.Notification notification, boolean isTse, HttpServletRequest rq) {

        StringBuilder stringBuilderBasic = new StringBuilder();
        stringBuilderBasic.append(loginVM.getUserType()).append(":")
                .append(loginVM.getUsername())
                .append(":")
                .append(loginVM.getPassword());

        String basic = Base64.getEncoder().encodeToString(stringBuilderBasic.toString().getBytes());

        ResponseTse responseTse = new ResponseTse();
        responseTse.setNeedAuthenticate(true);
        responseTse.setAuthenSsl(getSsl2());
        responseTse.setAuthenBasic(basic);

        JSONObject json = new JSONObject();
        json.put("relyingParty", selfCareProperties.getRelyingPartyName());
        json.put("profile", selfCareProperties.getProfile());
        json.put("lang", getCookieLanguage(rq));
        json.put("rememberMe", loginVM.isRememberMe());

        if (isTse) {
            JSONObject jsonTseNotification = new JSONObject();
            jsonTseNotification.put("notificationMessage", notification.getNotificationMessage());
            jsonTseNotification.put("messageCaption", notification.getMessageCaption());
            jsonTseNotification.put("message", notification.getMessage());
//                jsonTseNotification.put("rpName", notification.getScalIdentity());
            jsonTseNotification.put("vcEnabled", notification.isVcEnabled());
            jsonTseNotification.put("validityPeriod", notification.getValidityPeriod());
            jsonTseNotification.put("hashes", notification.getHashValue());
            jsonTseNotification.put("hashAlgorithmOID", notification.getHashAlgorithmOID());

            json.put("tseNotification", jsonTseNotification);
        }

        return getApiResult(responseTse, json, rq, URL_LOGIN, HttpMethod.POST);
    }

    public ApiResult preLogin(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_PRE_LOGIN, HttpMethod.POST);
    }

    public ApiResult loginSSO(ResponseTse responseTse, SelfCareProperties.Notification notification, JSONObject json, boolean isTse, HttpServletRequest rq) {

        json.put("relyingParty", selfCareProperties.getRelyingPartyName());
        json.put("profile", selfCareProperties.getProfile());

        if (isTse) {
            JSONObject jsonTseNotification = new JSONObject();
            jsonTseNotification.put("notificationMessage", notification.getNotificationMessage());
            jsonTseNotification.put("messageCaption", notification.getMessageCaption());
            jsonTseNotification.put("message", notification.getMessage());
            jsonTseNotification.put("vcEnabled", notification.isVcEnabled());
            jsonTseNotification.put("validityPeriod", notification.getValidityPeriod());
            jsonTseNotification.put("hashes", notification.getHashValue());
            jsonTseNotification.put("hashAlgorithmOID", notification.getHashAlgorithmOID());

            json.put("tseNotification", jsonTseNotification);
        }
        return getApiResult(responseTse, json, rq, URL_LOGIN_SSO, HttpMethod.POST);
    }

    public ApiResult auth2FactoreMethod(LoginVM loginVM, HttpServletRequest rq) {
        ResponseTse responseTse = new ResponseTse();
        responseTse.setNeedAuthenticate(true);
        responseTse.setAuthenSsl(getSsl2());

        JSONObject json = new JSONObject();
        json.put("relyingParty", selfCareProperties.getRelyingPartyName());
        json.put("userType", loginVM.getUserType());
        json.put("user", loginVM.getUsername());

        return getApiResult(responseTse, json, rq, URL_AUTH_2FACTOREMETHOD, HttpMethod.POST);
    }

    public ApiResult authRevoke(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        this.accessTokenServer = null;
        return getApiResult(responseTse, json, rq, URL_REVOKE, HttpMethod.POST);
    }

    public ApiResult credentialsChangeEmail(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIAL_CHANGE_EMAIL, HttpMethod.POST);
    }

    public ApiResult credentialsChangePhone(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIAL_CHANGE_PHONE, HttpMethod.POST);
    }

    public ApiResult credentialsAuthorize(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_AUTHORIZE, HttpMethod.POST);

    }

    public ApiResult systemCertificateAuthority(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_CERTIFICATE_AUTHORITY, HttpMethod.POST);
    }

    public ApiResult systemCertificateProfile(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_CERTIFICATE_PROFILES, HttpMethod.POST);
    }

    public ApiResult systemSigningProfile(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_SIGNING_PROFILES, HttpMethod.POST);
    }

    public ApiResult systemCountries(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_COUNTRIES, HttpMethod.POST);
    }

    public ApiResult systemsGetPaymentProvider(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_GET_PAYMENT_PROVIDER, HttpMethod.POST);
    }

    public ApiResult authPreLogin(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_PRE_LOGIN, HttpMethod.POST);
    }

    public ApiResult systemStateOrProvince(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_STATE_OR_PROVINCES, HttpMethod.POST);
    }

    public ApiResult credentialsIssue(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIAL_ISSUE, HttpMethod.POST);
    }

    public ApiResult credentialsInfo(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_INFO, HttpMethod.POST);
    }

    public ApiResult credentialsList(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_LIST, HttpMethod.POST);
    }

    public ApiResult ownerLogging(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_LOGGING, HttpMethod.POST);
    }

    public ApiResult ownerCreate(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_CREATE, HttpMethod.POST);
    }

    public ApiResult queriesOwnerHistory(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_HISTORY, HttpMethod.POST);
    }

    public ApiResult queriesCredentialHistory(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIAL_HISTORY, HttpMethod.POST);
    }

    public ApiResult ownerInfo(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_INFO, HttpMethod.POST);
    }

    public ApiResult ownerChangePassword(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_CHANGE_PASSWORD, HttpMethod.POST);
    }

    public ApiResult refreshToken(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        json.put("relyingParty", selfCareProperties.getRelyingPartyName());
        return getApiResult(responseTse, json, rq, URL_REFRESH_TOKEN, HttpMethod.POST);
    }

    public ApiResult signaturesSignHash(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SIGNATURES_SIGNHASH, HttpMethod.POST);
    }

    public ApiResult ownerSyncAnticloningOTP(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        json.put("rpRequestID", responseTse.getResponseID());
        json.put("validityPeriod", 3600);
        json.put("operationMode", "A");
        return getApiResult(responseTse, json, rq, URL_OWNER_SYSNCANTICLONEOTP, HttpMethod.POST);
    }

    public ApiResult accountResetPassword(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_ACCOUNT_RESET_PASSWORD, HttpMethod.POST);
    }

    public ApiResult credentialsChangeAuthInfo(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_CHANGE_AUTH_INFO, HttpMethod.POST);
    }

    public ApiResult credentialsResetPassphrase(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_RESET_PASSPHRASE, HttpMethod.POST);
    }

    public ApiResult credentialsChangePassphrase(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_CHANGE_PASSPHRASE, HttpMethod.POST);
    }

    public ApiResult credentialsSendOtp(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_SEND_OTP, HttpMethod.POST);
    }

    public ApiResult credentialsUpgrade(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_UPGRADE, HttpMethod.POST);
    }

    public ApiResult credentialsRenew(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_RENEW, HttpMethod.POST);
    }

    public ApiResult ownerChangeEmail(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_CHANGE_EMAIL, HttpMethod.POST);
    }

    public ApiResult ownerChangeInfo(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_CHANGE_INFO, HttpMethod.POST);
    }

    public ApiResult ordersCheckout(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_ORDERS_CHECKOUT, HttpMethod.POST);
    }

    public ApiResult ordersList(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_ORDER_LIST, HttpMethod.POST);
    }

    public ApiResult ordersDetail(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_ORDER_DETAIL, HttpMethod.POST);
    }

    public ApiResult ownerSendOTP(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_OWNER_SEND_OTP, HttpMethod.POST);
    }

    public ApiResult systemsGetCountries(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_GET_COUNTRIES, HttpMethod.POST);
    }

    public ApiResult systemsGetStatesOrProvinces(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_SYSTEM_GET_STATES_OR_PROVINCES, HttpMethod.POST);
    }

    public ApiResult credentialsEnroll(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_ENROLL, HttpMethod.POST);
    }

    public ApiResult credentialsApprove(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_APPROVE, HttpMethod.POST);
    }

    public ApiResult credentialsImport(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
        return getApiResult(responseTse, json, rq, URL_CREDENTIALS_IMPORT, HttpMethod.POST);
    }

//    public ApiResult qrCodeSendRequest(ResponseTse responseTse, JSONObject json, HttpServletRequest rq) {
//        return getApiResult(responseTse, json, rq, URL_LOGIN_QR_SEND_REQUEST, HttpMethod.POST);
//    }
    private String getSsl2() {
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis());

            String data2Sign = selfCareProperties.getRelyingPartyUser() + selfCareProperties.getRelyingPartyPass() + selfCareProperties.getRelyingPartySignature() + timeStamp;

            Resource resource = resourceLoader.getResource(selfCareProperties.getP12Path());

            String pkcs1Signature = PKCS1Util.getPKCS1Signature(data2Sign, resource.getInputStream(), selfCareProperties.getP12Password());

            StringBuilder stringBuilderSSL = new StringBuilder();

            stringBuilderSSL.append(selfCareProperties.getRelyingPartyUser())
                    .append(":")
                    .append(selfCareProperties.getRelyingPartyPass())
                    .append(":")
                    .append(selfCareProperties.getRelyingPartySignature())
                    .append(":")
                    .append(timeStamp)
                    .append(":")
                    .append(pkcs1Signature);

            return Base64.getEncoder().encodeToString(stringBuilderSSL.toString().getBytes());
        } catch (Exception ex) {
            return "";
        }

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

    private ApiResult getApiResult(ResponseTse responseTse, JSONObject jsonData, HttpServletRequest rq, String url, HttpMethod method) {
        try {
            String userAgent = rq.getHeader("User-Agent");
            HttpHeaders headers = new HttpHeaders();

//            log.info("return ve day ~~");
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

//            headers.set("Authorization", "Bearer " + responseTse.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("User-Agent", userAgent);
            log.info("PROFILE: {}", selfCareProperties.getProfile());
            log.info("LANG: {}", getCookieLanguage(rq));

            jsonData.put("profile", selfCareProperties.getProfile());
            jsonData.put("lang", getCookieLanguage(rq));

            HttpEntity<String> requestData = new HttpEntity<String>(jsonData.toString(), headers);
            log.info("RETURN LOGIN");
            return commonRequest(requestData, url, method); 
//trả về ngược lại sau khi commonRequest xử lý xong

        } catch (Exception e) {
            e.printStackTrace();
            log.error("LOGIN ERROR: {}", e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    private ApiResult commonRequest(HttpEntity<String> request, String url, HttpMethod method) {
        try {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            RestTemplate restTemplate = new RestTemplate();
            log.info("URL FULL: {}", selfCareProperties.getUrl() + url);
            log.info("request  url {}: {}", url, CommonUtil.convertObjectToString(request));
            ResponseEntity<Map> response = restTemplate.exchange(selfCareProperties.getUrl() + url,
                    method, request, Map.class);

            log.info("response url {}: {}", url, CommonUtil.convertObjectToString(response.getBody()));

            return ApiResult.success(response.getBody()); // trả về wsv
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            log.error("httpStatus Exception: {}", e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error("REST Client Exception: {}", e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }
    //post lên server api

    public ApiResult authPreLogin(PreLogin preLogin, Object object, HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ApiResult preLogin(JSONObject json, HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseTse {

        private String accessToken;
        private int expiresIn;
        private int error;
        private String errorDescription;
        private String responseID;
        private String SAD;
        private boolean needAuthenticate;
        private String authenSsl;
        private String authenBasic;
        private String ipnUrl;
        private String message;

//        public void setAccessToken(String acc){
//            this.accessToken = acc;
//        }
    }

}
