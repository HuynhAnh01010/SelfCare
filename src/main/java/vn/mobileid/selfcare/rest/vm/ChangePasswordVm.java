package vn.mobileid.selfcare.rest.vm;

import lombok.Data;

@Data
public class ChangePasswordVm extends CommonRq{
    private String oldPassword;
    private String newPassword;
    private String newPasswordRepeat;
}
