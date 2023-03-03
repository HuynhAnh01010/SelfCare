package vn.mobileid.selfcare.rest.vm;

import lombok.Data;

@Data
public class ChangeAuthInfoVm extends  CommonRq{
        private  String sharedMode;
        private  int scal;
        private  String authMode;
        private  int multisign;
        private  String authorizeCode;
        private  String requestID;
}
