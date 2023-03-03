package vn.mobileid.selfcare.rest.vm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreLogin {
    private String user;
    private String userType;
    private boolean ssoInfo;
}
