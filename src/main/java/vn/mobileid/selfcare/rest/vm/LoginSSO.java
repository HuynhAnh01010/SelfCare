package vn.mobileid.selfcare.rest.vm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSSO {
    private String state;
    private String code;
    private boolean rememberMe;
    private String clientUUID;
    private String tokenType;
    private String token;
    private String profile;
    private String lang;
    private String session_state;
}
