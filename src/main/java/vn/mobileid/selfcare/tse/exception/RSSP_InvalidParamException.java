package vn.mobileid.selfcare.tse.exception;

public class RSSP_InvalidParamException extends RSSP_Exception {
    public RSSP_InvalidParamException() {
        super(ResponseCodeType.PARAMETER_INVALID);
    }

    public RSSP_InvalidParamException(String message) {
        super(ResponseCodeType.PARAMETER_INVALID, message);
    }

    public RSSP_InvalidParamException(Throwable cause) {
        super(ResponseCodeType.PARAMETER_INVALID, cause);
    }

    public RSSP_InvalidParamException(String message, Throwable cause) {
        super(ResponseCodeType.PARAMETER_INVALID, message, cause);
    }
}
