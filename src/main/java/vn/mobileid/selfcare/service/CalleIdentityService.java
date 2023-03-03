package vn.mobileid.selfcare.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.selfcare.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CalleIdentityService {

    private final int DIFF = 50 * 60 * 1000;

    private final String URL_V1_EIDENTITY_OIDC_TOKEN = "/v1/e-identity/oidc/token";
    private final String URL_V1_EIDENTITY_SUBJECTS_CREATE = "/v1/e-identity/subjects/create";
    private final String URL_V1_EIDENTITY_PROCESSES_CREATE = "/v1/e-identity/processes/create";
    private final String URL_V1_EIDENTITY_VERIFICATION = "/v1/e-identity/verification";
    private final String URL_V1_EIDENTITY_PROCESSES_GET = "/v1/e-identity/processes/get";
    private final String URL_V1_EIDENTITY_PROCESSES_SELF_REVISE = "/v1/e-identity/processes/self-revise";
    private final String URL_V1_EIDENTITY_IMAGES_UPLOAD = "/v1/e-identity/images/upload";

    @Autowired
    private eIdentityProperties eIdentityProperties;

    private static Date date = new Date();
    private static ApiResult apiResult = null;

    public synchronized ApiResult getAccessToken(HttpServletRequest rq) {

        long diff = new Date().getTime() - date.getTime();
        log.info("getAccessToken: {}", diff);
        if (diff > DIFF) {
            apiResult = eIdentityOidcToken(rq);
            return apiResult;
        } else {
            if (apiResult == null) {
                log.info("apiResult == null");
                apiResult = eIdentityOidcToken(rq);
                return apiResult;
            } else if (apiResult.getCode() != 0) {
                log.info("code != 0");
                apiResult = eIdentityOidcToken(rq);
                return apiResult;
            } else {
                log.info("apiResult");
                return apiResult;
            }
        }
    }

    public ApiResult eIdentityOidcToken(HttpServletRequest rq) {

        try {
            JSONObject request = new JSONObject();
            //Thêm header
            TreeMap<String, String> awsHeaders = new TreeMap<String, String>();
            awsHeaders.put("X-Amz-Security-Token", eIdentityProperties.getXAmzSecurityToken());
            date = new Date();
            return commonRequest(request, awsHeaders, URL_V1_EIDENTITY_OIDC_TOKEN, HttpMethod.POST, rq);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult processesCreate(HttpServletRequest rq, JSONObject request, Map<String, Object> data) {
        return getApiResult(request, data,URL_V1_EIDENTITY_PROCESSES_CREATE, HttpMethod.POST, rq);
    }

    public ApiResult processesGet(HttpServletRequest rq, JSONObject request, Map<String, Object> data) {
        return getApiResult(request, data,URL_V1_EIDENTITY_PROCESSES_GET, HttpMethod.POST, rq);
    }

    public ApiResult processesSelfRevise(HttpServletRequest rq, JSONObject request, Map<String, Object> data) {
        return getApiResult(request, data,URL_V1_EIDENTITY_PROCESSES_SELF_REVISE, HttpMethod.POST, rq);
    }

    public ApiResult verification(HttpServletRequest rq, JSONObject request, Map<String, Object> data) {
        return getApiResult(request, data,URL_V1_EIDENTITY_VERIFICATION, HttpMethod.POST, rq);
    }


    public ApiResult eIdentitySubjectCreate(HttpServletRequest rq, JSONObject request, Map<String, Object> data) {

        return getApiResult(request, data,URL_V1_EIDENTITY_SUBJECTS_CREATE, HttpMethod.POST, rq);
    }

    public ApiResult imagesUpload(HttpServletRequest rq, JSONObject request, Map<String, Object> data) {
        return getApiResult(request, data,URL_V1_EIDENTITY_IMAGES_UPLOAD, HttpMethod.POST, rq);
    }

    private ApiResult getApiResult( JSONObject request, Map<String, Object> data, String url_v1,HttpMethod httpMethod,HttpServletRequest rq) {
        try {
            //Thêm header
            TreeMap<String, String> awsHeaders = new TreeMap<String, String>();
            awsHeaders.put("X-Amz-Security-Token", data.get("token_type") + " " + data.get("access_token"));

            return commonRequest(request, awsHeaders, url_v1, httpMethod, rq);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    private ApiResult commonRequest(JSONObject payload, TreeMap<String, String> awsHeaders, String path, HttpMethod method, HttpServletRequest rq) {
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
        try {
            String uri = eIdentityProperties.getAwsApiGatewayEndpoint() + path;
            URL url = new URL(uri);
            log.info("REQUEST URI: {}", uri);

            awsHeaders.put("X-api-key", eIdentityProperties.getXApiKey());

            AWSV4Auth aWSV4Auth = new AWSV4Auth.Builder(eIdentityProperties.getAwsIamAccessKey(), eIdentityProperties.getAwsIamSecretKey())
                    .regionName(eIdentityProperties.getAwsRegion())        //region name
                    .serviceName(eIdentityProperties.getApiGatewaySeviceName()) // service name
                    .httpMethodName(method.toString()) // GET, PUT, POST, DELETE
                    .endpointURI(url) // end point
                    .queryParametes(null) // query parameters if any
                    .awsHeaders(awsHeaders) // aws header parameters
                    .payload(payload.toString()) // payload if any
                    .build();
            aWSV4Auth.getHeaders();

            HttpHeaders headers = new HttpHeaders();
            for (Map.Entry<String, String> entry : awsHeaders.entrySet()) {
                headers.set(entry.getKey(), entry.getValue());
            }

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<String>(payload.toString(), headers);

            RestTemplate restTemplate = new RestTemplate();

            log.info("eIdentityProperties: {}", CommonUtil.convertObjectToString(eIdentityProperties));

            if (eIdentityProperties.getLogOCR().isEnabled()) {
                AppFileUtils.writeLogOcr2File(eIdentityProperties.getLogOCR().getPath() + File.separator,payload);
            }

            log.info("REQUEST DATA Header: {}", request.getHeaders());
            log.info("REQUEST DATA Body: {}", payload);

            ResponseEntity<Map> response = restTemplate.exchange(uri,
                    method, request, Map.class);
            log.info("response: {}", CommonUtil.convertObjectToString(response.getBody()));
            return ApiResult.success(response.getBody());
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

}
