package vn.mobileid.selfcare.tse.exception;


public enum ResponseCodeType {
    SUCCESS(0, "Successfully", "Thành công"),
    AUTHORIZATION_CODE_INVALID(1004, "Invalid authorization code", "Lỗi sai mã xác thực"),
    AUTHORIZATION_BLOCKED(1005, "Authorization blocked", "Lỗi mã xác thực bị khoá"),
    AUTHORIZATION_CODE_TIMEOUT(1006, "Authorization code timeout", "Lỗi mã xác thực bị hết hạn"),
    BASIC_CREDENTIALS_INVALID(3000, "Login information is invalid", "Thông tin đăng nhập không hợp lệ"),
    OWNER_NOT_READY(3001, "The account is not ready for use", "Tài khoản chưa sẵn sàng để sử dụng"),
    OWNER_BLOCKED(3002, "The account has been locked", "Tài khoản đã bị khóa"),
    OWNER_NOT_EXIST(3003, "The account is not exists", "Tài khoản không tồn tại"),
    SSL2_CREDENTIALS_INVALID(3004, "SSL2 credentials is invalid", "Thông tin đăng nhập SSL2 không hợp lệ"),
    BEARER_TOKEN_INVALID(3005, "Bearer token is invalid", "Mã thông báo không hợp lệ"),
    BEARER_TOKEN_EXPIRED(3006, "Bearer token has been expired", "Mã thông báo đã hết hạn"),
    TOKEN_INVALID(3007, "Invalid string parameter token", "Tham số mã thông báo không hợp lệ"),
    PARAMETER_INVALID(3008, "The parameter is invalid", "Tham số không hợp lệ"),
    NOT_FOUND_CERTIFICATE(3009, "Certificate not found.", "Không tìm thấy chứng thư số"),
    CERTIFICATE_NOT_READY(3010, "The certificate is not eligible for use", "Chứng thư số không đủ điều kiện để sử dụng"),
    CERTIFICATE_BLOCKED(3011, "The certificate has been temporarily blocked due to false credentials.", "Chứng thư số bị tạm khóa do xác thực sai."),
    MULTI_MACHINES_USING_CERTIFICATE(3012, "Multiple machines using the same certificate", "Nhiều máy sử dụng cùng một chứng thư số"),
    AUTHORIZATION_CODE_NOT_AVAILABLE(3013, "Authorization code is not available, call credentials/sendOTP function to retrieve the new OTP", "Mã OTP không khả dụng, gọi hàm credentials/sendOTP để lấy OTP mới."),
    NOT_FOUND_RELYING_PARTY(3014, "RelyingParty is not exist", "RelyingParty không tồn tại"),
    RELYING_PARTY_NOT_ALLOW_METHOD(3015, "Relying party does not allow this operation.", "Relying party không cho phép chức năng này."),
    NOT_ALLOW_IP(3016, "IP address is not allowed.", "Địa chỉ IP không được chấp nhận."),
    NOT_ALLOW_FUNCTION(3017, "Function is not allowed.", "Chức năng không được chấp nhận."),
    ENDPOINT_ERROR(3018, "Error when interacting with another entity.", "Lỗi khi tương tác thực thể khác."),
    SAD_TIMEOUT(3019, "SAD was expired, call credentials/authorize to retrieve the new SAD.", "SAD đã hết hạn, hãy gọi credentials/authorize để lấy SAD mới."),
    SAD_NOT_EXITS(3020, "SAD is not exits.", "SAD không tồn tại."),
    SAD_BLOCKED(3021, "SAD was blocked.", "SAD bị khóa."),
    SAD_INVALID(3022, "SAD is invalid.", "SAD không hợp lệ."),
    NOT_ENOUGH_SIGNING_COUNTER(3023, "The remaining signing counter of your certificate is not enough.", "Số lần ký còn lại của chứng thư số của bạn  không đủ."),
    UNACCEPTABLE_OPERATION(3024, "Unacceptable operation.", "Không chấp hành động."),
    INTERNAL_SERVER_ERROR(3025, "Internal Server Error", "Lỗi máy chủ nội bộ"),
    UNKNOWN(3026, "Unknown error", "Lỗi không xác định"),
    BEARER_TOKEN_NOT_PERMISSION(3027, "Bearer token does not have access to this function", "Mã thông báo không có quyền truy cập vào chức năng này"),
    NOT_MATCHED_PASSWORD_POLICY(3028, "The new password is not valid", "Mật khẩu mới không hợp lệ"),
    NOT_SUPPORT_ALGORITHM(3029, "The algorithm is not supported by the system.", "Thuật toán không được hệ thống hỗ trợ."),
    PASSPHRASE_IS_WRONG(3031, "The passphrase is wrong.", "Cụm mật khẩu sai."),
    TSE_ALREADY_EXIST(3032, "The TSE already exist. If you change device please contact to service provider for help.", "TSE đã tồn tại. Nếu bạn thay đổi thiết bị, vui lòng liên hệ với nhà cung cấp dịch vụ để được giúp đỡ."),
    NOT_SUPPORT_LOGIN_METHOD(3033, "Login method is not supported by the system.", "Phương thức đăng nhập không được hỗ trợ bởi hệ thống."),
    REQUEST_ACCEPTED(3034, "Request is accepted.", "Yêu cầu được chấp nhận."),
    REQUEST_PROCESSING(3035, "Request is being processed.", "Yêu cầu đang được xử lý."),
    REQUEST_EXPIRED(3036, "Request expired.", "Yêu cầu hết hạn."),
    REQUEST_WAITING_USER_CONFIRM(3037, "Request is waiting confirmation, please check your mobile equipment.", "Yêu cầu đang chờ xác nhận, vui lòng kiểm tra thiết bị di động của bạn."),
    REQUEST_RECEIVED_USER_ACTION(3038, "The request has been received action from user.", "Yêu cầu đã được nhận hành động từ người dùng."),
    REQUEST_CANCELLED(3039, "The request denied by user", "Yêu cầu bị từ chối bởi người dùng"),
    PROCESS_TIMEOUT(3040, "Timeout to process request", "Hết thời gian để xử lý yêu cầu"),
    NOT_MATCHED_CLIENT_INFO(3041, "The client info does not match with that has been authorized", "Thông tin client không khớp với thông tin đã được ủy quyền"),
    NOT_MATCHED_HASH(3042, "The hashes do not match with that has been authorized", "Mã băm không khớp với thông tin đã được ủy quyền"),
    NOT_MATCHED_PASSPHRASE_POLICY(3043, "The new passphrase is not match with policy", "Mật khẩu mới không hợp lệ"),
    PERMISSION_DENIED_CERTIFICATE(3044, "Device does not have permission to access this certificate", "Thiết bị không được quyền truy cập chứng chỉ này"),
    NOT_SUPPORT_TWO_FACTOR_METHOD(3045, "No support logon with this two-factor method", "Không hỗ trợ đăng nhập với phương thức 2 yếu tố này"),
    TSE_NOT_READY(3213, "The TSE is not ready for use", "Thiết bị TSE chưa sẵn sàng để sử dụng"),
    REQUEST_NOT_FOUND(3214, "Not found the transaction", "Không tìm thấy giao dịch"),
    REQUEST_PERMISSION_DENIED(3218, "Permission denied to access this request", "Quyền bị từ chối truy cập yêu cầu này"),
    UNAUTHORIZE(3221, "The authorize signature is invalid", "Chữ ký xác thực không hợp lệ"),
    TSE_BLOCKED(3222, "TSE device has been locked due to an incorrect the otp anti cloning", "Thiết bị TSE đã bị khóa do sai mã otp chống nhân bản"),
    TSE_TEMP_BLOCKED(3223, "Temporary TSE device has been blocked since invalid authorization signature", "Thiết bị TSE tạm thời đã bị khóa do chữ ký ủy quyền không hợp lệ");

    public final int val;
    public final String remarkEn;
    public final String remark;

    private ResponseCodeType(int val, String remarkEn, String remark) {
        this.val = val;
        this.remarkEn = remarkEn;
        this.remark = remark;
    }

    public static ResponseCodeType valueOf(int name) throws RSSP_InternalServerErrorException {
        ResponseCodeType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ResponseCodeType type = var1[var3];
            if (type.val == name) {
                return type;
            }
        }

        throw new RSSP_InternalServerErrorException("Unknown ResponseCodeName: [" + name + "]");
    }
}
