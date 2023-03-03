package vn.mobileid.selfcare.service.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.mobileid.selfcare.utils.ApiResult;

import java.util.Map;

public class AppUtils {

    public static ResponseEntity returnFromServer(ApiResult apiResult ){
        if (apiResult.getCode() == 0) {
            Map<String, Object> mapResponse = (Map<String, Object>) apiResult.getData();
            if ((int) mapResponse.get("error") == 0) {
                return new ResponseEntity<>(apiResult, HttpStatus.OK);
            }else if ((int) mapResponse.get("error") == 3005 || (int) mapResponse.get("error") == 3006) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } else {
                apiResult.setMessage((String) mapResponse.get("errorDescription"));
                return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
        }
    }

    public static ResponseEntity returnFromServereIdentity(ApiResult apiResult ){
        if (apiResult.getCode() == 0) {
            Map<String, Object> mapResponse = (Map<String, Object>) apiResult.getData();
            if ((int) mapResponse.get("status") == 0) {
                return new ResponseEntity<>(apiResult, HttpStatus.OK);
            } else {
                apiResult.setMessage((String) mapResponse.get("message"));
                return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
        }
    }
    
//    public static String addParamStore(String function, int count) {
//
//        String suffix = count > 0 ? "?" : "";
//        for (int i = 1; i < count; i++) {
//            suffix += ",?";
//        }
//        return String.format("{CALL %s(%s)}", function, suffix);
//
//    }
    
//    public static User getPrincipal() {
//        try {
//            User user = new User();
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//            if (principal instanceof UserImpl) {
//                UserImpl user1 = (UserImpl) principal;
//                user.setUsername(user1.getUsername());
//                user.setFullName(user1.getFullName());
//                user.setId(user1.getId());
//                user.setRole(user1.getRole());
//                user.setParty(user1.getParty());
//                user.setProperties(user1.getProperties());
//                user.setRegistrationParty(user1.getRegistrationParty());
//
//                return user;
//            }
//            return null;
//        } catch (Exception ex) {
//            return null;
//        }
//
//    }
}
