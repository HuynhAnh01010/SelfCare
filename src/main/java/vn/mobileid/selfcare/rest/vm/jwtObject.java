package vn.mobileid.selfcare.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class jwtObject {
    private String relyingParty;
    private String userName;
    private String token;
    private RequesterInfo requesterInfo;

    @Getter@Setter
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class RequesterInfo{
        private String ip;
        private String rp;
    }
}
