package vn.mobileid.selfcare.rest.vm;

import lombok.Data;

@Data
public class ChangePassphraseVm extends CommonRq{
    private String oldPassphrase;
    private String newPassphrase;
    private String newPassphraseRepeat;
}
