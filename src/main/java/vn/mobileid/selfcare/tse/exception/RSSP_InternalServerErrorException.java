package vn.mobileid.selfcare.tse.exception;


public class RSSP_InternalServerErrorException extends RSSP_Exception {
    public RSSP_InternalServerErrorException() {
        super(ResponseCodeType.INTERNAL_SERVER_ERROR);
    }

    public RSSP_InternalServerErrorException(String message) {
        super(ResponseCodeType.INTERNAL_SERVER_ERROR, message);
    }

    public RSSP_InternalServerErrorException(Throwable cause) {
        super(ResponseCodeType.INTERNAL_SERVER_ERROR, cause);
    }

    public RSSP_InternalServerErrorException(String message, Throwable cause) {
        super(ResponseCodeType.INTERNAL_SERVER_ERROR, message, cause);
        super.addSuppressed(cause);
    }
}
