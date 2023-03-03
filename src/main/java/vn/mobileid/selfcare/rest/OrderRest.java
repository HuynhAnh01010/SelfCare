/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import vn.mobileid.selfcare.service.util.AppUtils;
import vn.mobileid.selfcare.tse.TSECryptoHelper;
import vn.mobileid.selfcare.utils.ApiResult;
import vn.mobileid.selfcare.utils.CommonUtil;
import vn.mobileid.selfcare.utils.SelfCareProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 *
 * @author Mobile ID 21
 */
@RestController
@RequestMapping("/api")
public class OrderRest {

    private final Logger log = LoggerFactory.getLogger(OrderRest.class);

    private final CallApiRsspService callApiRsspService;

    private final SelfCareProperties selfCareProperties;

    private final AuthenticationManager authenticationManager;

    public OrderRest(CallApiRsspService callApiRsspService, SelfCareProperties selfCareProperties, AuthenticationManager authenticationManager) {

        this.callApiRsspService = callApiRsspService;
        this.selfCareProperties = selfCareProperties;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/order/list")
    public ResponseEntity orderList(HttpServletResponse response, HttpServletRequest request,
            @RequestBody Map<String, String> mapData) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);

            JSONObject jsonSearch = new JSONObject();
            jsonSearch.put("orderTypes", mapData.get("mapData"));
            jsonSearch.put("orderStates", mapData.get("orderStates"));
            jsonSearch.put("fromDate", mapData.get("fromDate"));
            jsonSearch.put("toDate", mapData.get("toDate"));

            JSONObject json = new JSONObject();
            json.put("pageNumber", mapData.get("page"));
            log.info("size: {}", mapData.get("page"));
            json.put("recordCount", mapData.get("size"));

            ApiResult apiResult = callApiRsspService.ordersList(responseTse, json, request);

            return AppUtils.returnFromServer(apiResult);
        }

    }
    
    @PostMapping("/orders/info")
    public ResponseEntity orderDetail(HttpServletResponse response, HttpServletRequest request,
            @RequestBody Map<String, String> mapData) {
        String jwt = JWTFilter.extractAccessTokenFromCookie(request, response, callApiRsspService);
        if (jwt == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else {
            CallApiRsspService.ResponseTse responseTse = new CallApiRsspService.ResponseTse();
            responseTse.setAccessToken(jwt);


            JSONObject json = new JSONObject();
            json.put("orderUUID", mapData.get("orderUUID"));

            ApiResult apiResult = callApiRsspService.ordersDetail(responseTse, json, request);

            return AppUtils.returnFromServer(apiResult);
        }

    }
}
