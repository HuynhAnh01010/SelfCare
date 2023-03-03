package vn.mobileid.selfcare.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("eidentity") // prefix app, find app.* values
@Getter
@Setter
public class eIdentityProperties {
    private String awsIamAccessKey;
    private String awsIamSecretKey;
    private String awsRegion;
    private String awsApiGatewayEndpoint;
    private String xApiKey;
    private String apiGatewaySeviceName;
    private String xAmzSecurityToken;
    private Api api;
    private LogOCR logOCR;

    @Getter@Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Api{
        private String processType;
        private String documentType;

    }

    @Getter@Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LogOCR{
        private boolean enabled;
        private String path;

    }
}
