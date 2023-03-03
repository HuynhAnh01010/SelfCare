package vn.mobileid.selfcare.jwt;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.StringUtils;
import org.json.JSONObject;
import vn.mobileid.selfcare.config.Constants;
import vn.mobileid.selfcare.rest.vm.jwtObject;
import vn.mobileid.selfcare.service.CallApiRsspService;
import vn.mobileid.selfcare.utils.ApiResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;
import vn.mobileid.selfcare.utils.CommonUtil;

@Slf4j
public class JWTFilter {

    public static String ACCESS_TOKEN = "accessToken";
    public static String REFRESH_TOKEN = "refreshToken";
    public static String COOKIE_PATH = "/";
    public static String SSOENABLED = "ssoEnabled";

    public static void setCookie(Map<String, Object> data, HttpServletResponse response, boolean remember) {

        Cookie cookieAccessToken = new Cookie(ACCESS_TOKEN, (String) data.get(ACCESS_TOKEN));
        // expires
        cookieAccessToken.setMaxAge((int) data.get("expiresIn"));
        // optional properties
        cookieAccessToken.setSecure(true);
        cookieAccessToken.setHttpOnly(true);
        cookieAccessToken.setPath(COOKIE_PATH);

        // add cookie to response
        log.info("SET COOKIE: {}", CommonUtil.convertObjectToString(cookieAccessToken));
        response.addCookie(cookieAccessToken);

        log.info("remember: ", remember);
        if (remember) {
            if (data.get(REFRESH_TOKEN) != null) {
                Cookie cookieRefresh = new Cookie(REFRESH_TOKEN, (String) data.get(REFRESH_TOKEN));
                // optional properties
                cookieRefresh.setMaxAge(60 * 60 * 24 * 30);
                cookieRefresh.setSecure(true);
                cookieRefresh.setHttpOnly(true);
                cookieRefresh.setPath(COOKIE_PATH);

                // add cookie to response
                response.addCookie(cookieRefresh);
            }
        }

    }

    public static void removeCookie(String cookieName, HttpServletResponse response) {
        // expires in 7 days
        Cookie cookie = new Cookie(cookieName, "");
        // optional properties
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        log.info("Remove refresh token: {}", CommonUtil.convertObjectToString(cookie));
        response.addCookie(cookie);

    }

    public static String extractAccessTokenFromCookie(HttpServletRequest req, HttpServletResponse resp, CallApiRsspService callApiRsspService) {
        if (req.getCookies() == null) {
            return null;
        }
        String accessToken = null;
        String refreshToken = null;

        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(ACCESS_TOKEN)) {
                accessToken = c.getValue();
                log.info("FOR 1 accessToken: {}", accessToken);
            }
            if (c.getName().equals(REFRESH_TOKEN)) {
                refreshToken = c.getValue();
                log.info("FOR 2 refreshToken: {}", refreshToken);
            }
        }

        if (Strings.isNullOrEmpty(accessToken) && !Strings.isNullOrEmpty(refreshToken)) {

            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(refreshToken);

            JSONObject json = new JSONObject();
            ApiResult apiResult = callApiRsspService.refreshToken(responseTse, json, req);

            if (apiResult.getCode() == 0) {
                Map<String, Object> mapResponse = (Map<String, Object>) apiResult.getData();

                if ((int) mapResponse.get("error") == 0) {
                    setCookie(mapResponse, resp, false);
                    return (String) mapResponse.get(ACCESS_TOKEN);
                } else {
//                    cookieRefresh.setMaxAge(0);
                    removeCookie(REFRESH_TOKEN, resp);
                }
            }
        }
        log.info("accessToken: {}", accessToken);
        return accessToken;
    }

    public static String extractRefreshTokenFromCookie(HttpServletRequest req) {
        if (req.getCookies() == null) {
            return null;
        }
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(REFRESH_TOKEN)) {
                return c.getValue();
            }
        }
        return null;
    }

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.BEARER)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public static jwtObject convertJwtToObject(String jwt) {
        if (!StringUtils.hasText(jwt)) {
            return null;
        }
        try {
            byte[] decodeJwt = Base64.getDecoder().decode(jwt);
            String objJwt = new String(decodeJwt);
            log.info("Jwt Object: {}", objJwt);
            Gson gson = new Gson();
            jwtObject jwtObject = gson.fromJson(objJwt, jwtObject.class);

            return jwtObject;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            return null;
        }

    }
}
