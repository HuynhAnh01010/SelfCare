package vn.mobileid.selfcare.rest.vm;

import lombok.Data;

@Data
public class CommonRq {
    private String rpRequestID;
    private String requestID;
    private String lang;
    private String profile;
    private String agreementUUID;
    private String authorizeCode;
    private String credentialID;
}
