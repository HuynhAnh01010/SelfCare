package vn.mobileid.selfcare.rest.vm;

import lombok.Data;

@Data
public class ResetPassphraseVm  extends  CommonRq{
    private String notificationTemplate;
    private String notificationSubject;
    private String credentialID;
    private String newPassphrase;
    private String authorizeCode;
    private String requestType;
    private String requestID;
    private String responseID;
}
