package vn.mobileid.selfcare.tse.exception;

public class CXI_ParseApduException extends Exception {
    public CXI_ParseApduException() {
    }

    public CXI_ParseApduException(String message) {
        super(message);
    }

    public CXI_ParseApduException(Throwable cause) {
        super(cause);
    }

    public CXI_ParseApduException(String message, Throwable cause) {
        super(message);
        super.addSuppressed(cause);
    }
}
