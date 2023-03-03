package vn.mobileid.selfcare.rest.vm;

import lombok.Getter;
import lombok.Setter;
import vn.mobileid.selfcare.config.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * View Model object for storing a user's credentials.
 */
@Getter
@Setter
public class LoginVM {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = 0, max = 64)
    private String password;

    private String userType;

    private boolean rememberMe;

    private String vc;
    private String hashValue;
    private int validityPeriod;
    private boolean tse;

    @Override
    public String toString() {
        return "LoginVM{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", rememberMe=" + rememberMe +
                ", vc='" + vc + '\'' +
                ", hashValue='" + hashValue + '\'' +
                '}';
    }
}

