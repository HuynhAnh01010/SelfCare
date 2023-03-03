package vn.mobileid.selfcare.tse.exception;

public class RSSP_Exception extends Exception {
    private final ResponseCodeType errorCode;

    public RSSP_Exception(ResponseCodeType errorCode) {
        this.errorCode = errorCode;
    }

    public RSSP_Exception(ResponseCodeType errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RSSP_Exception(ResponseCodeType errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public RSSP_Exception(ResponseCodeType errorCode, String message, Throwable cause) {
        super(message);
        super.addSuppressed(cause);
        this.errorCode = errorCode;
    }

    public ResponseCodeType getErrorCode() {
        return this.errorCode;
    }
}
