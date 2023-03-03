package vn.mobileid.selfcare.rest.vm;

import lombok.Data;

/**
 * View Model object for storing the user's key and password.
 */
@Data
public class KeyAndPasswordVM {

    private String otpCode;
    private String username;
    private String userType;
    private String password;
    private String confirmPassword;
    private String requestId;

}
