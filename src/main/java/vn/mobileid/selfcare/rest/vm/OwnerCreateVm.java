package vn.mobileid.selfcare.rest.vm;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class OwnerCreateVm {

    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private String identificationType;
    private String[] changeColumn;

    private String subjectId;
    private String processId;
    private String dob;
    private String gender;
    private String identification;
    private String twoFactorMethod;
    private String registerTSEEnabled;
    private String loa;
    private String kycEvidence;

    //Detail
    private String location;
    private String country;
    private String certificateProfile;
    private String signingProfile;
    private String sharedMode;
    private int scal;
    private String authMode;
    private int multisign;
    private String commonName;
    private String organization;
    private String organizationUnit;
    private String title;
    private String telephoneNumber;
    private String stateOrProvince;
    private String stateOrProvinceId;

    public String getIdentificationType2Tse() {
        String forTse = this.identificationType;
        if(forTse == null) return null;
        switch (this.identificationType) {
            case "PASSPORT":
                forTse = "PASSPORT-ID";
                break;
            case "IDENTITYCARD":
                forTse = "PERSONAL-ID";
                break;
            case "CITIZENCARD":
                forTse = "CITIZEN-IDENTITY-CARD";
                break;
            default:
                return forTse;
        }
        return forTse;
    }
}
