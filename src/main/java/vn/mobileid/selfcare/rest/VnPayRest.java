//package vn.mobileid.selfcare.rest;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.app.login.domain.VnPayVM;
//import com.app.login.utils.VNPayUtils;
//import com.app.login.web.rest.util.ApiResult;
//import com.app.login.web.rest.util.CommonUtil;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/api")
//@Slf4j
//public class VnPayRest {
//
//    @PostMapping("/vnpay/pay")
//    public ResponseEntity vnPay_pay(@RequestBody VnPayVM vnPayVM, HttpServletResponse resp, HttpServletRequest req) throws IOException {
//
//        log.info("vnPay: {}", CommonUtil.convertObjectToString(vnPayVM));
//
//        String vnp_Version = "2.0.0";
//        String vnp_Command = "pay";
//        String vnp_OrderInfo = vnPayVM.getVnpOrderInfo();
//        String orderType = vnPayVM.getOrdertype();
////        UUID uuid = UUID.randomUUID().toString();
////        String vnp_TxnRef = VNPayUtils.getRandomNumber(8);
//        String vnp_TxnRef = UUID.randomUUID().toString();
//        String vnp_IpAddr = VNPayUtils.getIpAddress(req);
//
//        String vnp_TmnCode = VNPayUtils.vnp_TmnCode;
//
//        String vnp_TransactionNo = vnp_TxnRef;
//        String vnp_hashSecret = VNPayUtils.vnp_HashSecret;
//
//        int amount = vnPayVM.getAmount() * 100;
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        String bank_code = vnPayVM.getBankcode();
//        if (bank_code != null && bank_code.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bank_code);
//        }
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
//        vnp_Params.put("vnp_OrderType", orderType);
//
//        String locate = vnPayVM.getLanguage();
//        if (locate != null && !locate.isEmpty()) {
//            vnp_Params.put("vnp_Locale", locate);
//        } else {
//            vnp_Params.put("vnp_Locale", "vn");
//        }
//        vnp_Params.put("vnp_ReturnUrl", VNPayUtils.vnp_Returnurl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Date dt = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String dateString = formatter.format(dt);
//        String vnp_CreateDate = dateString;
//        String vnp_TransDate = vnp_CreateDate;
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        //Build data to hash and querystring
//        List fieldNames = new ArrayList(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = (String) vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(fieldValue);
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = VNPayUtils.Sha256(VNPayUtils.vnp_HashSecret + hashData.toString());
//        System.out.println("HashData=" + hashData.toString());
//        queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = VNPayUtils.vnp_PayUrl + "?" + queryUrl;
//        JsonObject job = new JsonObject();
//        job.addProperty("code", "00");
//        job.addProperty("message", "success");
//        job.addProperty("data", paymentUrl);
//
//        Map<String, String> map = new HashMap<>();
//        map.put("code","00");
//        map.put("message","success");
//        map.put("data",paymentUrl);
//
//        return new ResponseEntity<>(ApiResult.success(map), HttpStatus.OK);
////        return ResponseEntity.ok(job);
//    }
//
//    @GetMapping("/vnpay/result")
//    public ResponseEntity vnPay_result(HttpServletResponse resp, HttpServletRequest request) throws IOException {
//
//        Map fields = new HashMap();
////        fields = request.getTrailerFields();
//        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
//            String fieldName = (String) params.nextElement();
//            String fieldValue = request.getParameter(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                fields.put(fieldName, fieldValue);
//            }
//        }
//
//        log.info("fileds: {}", CommonUtil.convertObjectToString(fields));
//
//        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//        log.info("vnp_SecureHash: {}",vnp_SecureHash);
//        if (fields.containsKey("vnp_SecureHashType")) {
//            fields.remove("vnp_SecureHashType");
//        }
//        if (fields.containsKey("cacheBuster")) {
//            fields.remove("cacheBuster");
//        }
//        if (fields.containsKey("vnp_SecureHash")) {
//            fields.remove("vnp_SecureHash");
//        }
//        String signValue = VNPayUtils.hashAllFields(fields);
//
//        return new ResponseEntity<>(ApiResult.success(signValue), HttpStatus.OK);
////        return ResponseEntity.ok(job);
//    }
//}
