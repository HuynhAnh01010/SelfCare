package vn.mobileid.selfcare.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("selfcare") // prefix app, find app.* values
@Getter
@Setter
public class SelfCareProperties {
    private String url;
    private String urlPay;
    private String profile;
    private String relyingPartyName;
    private String relyingPartyUser;
    private String relyingPartyPass;
    private String relyingPartySignature;
    private String p12Password;
    private String p12Path;
    private Notification notification;
    private Certificate certificate;

    @Getter@Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Notification{
        private String scalIdentity;
        private String hashAlgorithmOID;
        private String notificationMessage;
        private String messageCaption;
        private String message;
        private String rpName;
        private int validityPeriod;
        private String hashValue;
        private String vc;
        private boolean vcEnabled;
        private boolean showCancelButton;
        private int timeout;
        private int numSignatures;
        private String method;
//        ssoEnabled
        private String ssoEnabled;
        private int code;
        private String state;
        private String scope;
        private String authUrl;
        private String tokenUrl;
        private String clientId;
    }

    @Getter@Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Certificate{
        private String profileName;
        private String signingProfile;
        private String sharedMode;
        private String authMode;
        private int multiSign;
        private String type;
        private int scal;
    }
}
