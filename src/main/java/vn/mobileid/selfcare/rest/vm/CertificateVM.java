package vn.mobileid.selfcare.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateVM implements Serializable {
    private CertificateProfile certificateProfile;
    private String certificateAuthority;
    private SigningProfile signingProfile;
//    private String signingProfileValue;
    private String sharedMode;
    private int scal;
    private String authMode;
    private int multisign;
    private String email;
    private String phone;
    private CertDetails certDetails;

    @Getter@Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CertDetails{
        private String commonName;
        private String organization;
        private String organizationUnit;
        private String title;
        private String email;
        private String telephoneNumber;
        private String location;
        private StateOrProvince stateOrProvince;
        private String country;
        private Identifications[] Identifications;
    }

    @Getter@Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CertificateProfile{
        private String algorithm;
        private String duration;
        private String name;
        private String type;
    }

    @Getter@Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SigningProfile{
        private String description;
        private String name;
        private int amount;
        private int signingCounter;
    }

    @Getter@Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identifications{
        private String value;
        @Getter(AccessLevel.NONE)
        private String type;

        public String getType() {
            switch (type){
                case "CMND:":
                return "PERSONAL-ID";
                case "HC:":
                    return "PASSPORT-ID";
                case "CCCD:":
                    return "CITIZEN-IDENTITY-CARD";
                case "MNS:":
                    return "BUDGET-CODE";
                case "MST:":
                    return "TAX-CODE";
            }
            return type;
        }
    }

    @Getter@Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StateOrProvince{
        private String code;
        private String name;
    }
}
