package vn.mobileid.selfcare.utils;



/**
 *  <p> API返回参数 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/7/20 11:09
 */
public class ApiResult {
    /**
     * 消息内容
     */
    private String message;
    private String extend;
    

    /**
     * 响应码：参考`ResultCode`
     */
    private Integer code;

    /**
     * 响应中的数据
     */
    private Object data;

    /***
     * 过期
     *
     * @param message:
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static ApiResult expired(String message) {
        return new ApiResult(ApiResultCode.UN_LOGIN.getCode(), message, null);
    }

    public static ApiResult fail(String message) {
        return new ApiResult(ApiResultCode.FAILURE.getCode(), message, null);
    }

    public static ApiResult fail(String message,Object obj) {
        return new ApiResult(ApiResultCode.FAILURE.getCode(), message, obj);
    }

    public static ApiResult fail() {
        return new ApiResult(ApiResultCode.FAILURE.getCode(), ApiResultCode.FAILURE.getDesc(), null);
    }

    /***
     * 自定义错误返回码
     *
     * @param code
     * @param message:
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static ApiResult fail(Integer code, String message) {
        return new ApiResult(code, message, null);
    }

    public static ApiResult failRes(Integer code, String message) {
        return new ApiResult(code, message, null);
    }

    public static ApiResult ok(String message) {
        return new ApiResult(ApiResultCode.SUCCESS.getCode(), message, null);
    }

    public static ApiResult ok() {
        return new ApiResult(ApiResultCode.SUCCESS.getCode(), "OK", null);
    }

    public static ApiResult build(Integer code, String msg, Object data) {
        return new ApiResult(ApiResultCode.SUCCESS.getCode(), msg, data);
    }

    public static ApiResult ok(String message, Object data) {
        return new ApiResult(ApiResultCode.SUCCESS.getCode(), message, data);
    }

    public static ApiResult success(Object data) {
        return new ApiResult(ApiResultCode.SUCCESS.getCode(), ApiResultCode.SUCCESS.getDesc(), data);
    }

    public static ApiResult success() {
        return new ApiResult(ApiResultCode.SUCCESS.getCode(), ApiResultCode.SUCCESS.getDesc(), null);
    }

    /**
     * 自定义返回码
     */
    public static ApiResult ok(Integer code, String message) {
        return new ApiResult(code, message);
    }

    /**
     * 自定义
     *
     * @param code：验证码
     * @param message：返回消息内容
     * @param data：返回数据
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static ApiResult ok(Integer code, String message, Object data) {
        return new ApiResult(code, message, data);
    }

    public ApiResult() { }

    public static ApiResult build(Integer code, String msg) {
        return new ApiResult(code, msg, null);
    }
    
    

    public ApiResult(Integer code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ApiResult(Object data) {
        this.code = ApiResultCode.SUCCESS.getCode();
        this.message = "OK";
        this.data = data;
    }

    public ApiResult(String message) {
        this(ApiResultCode.SUCCESS.getCode(), message, null);
    }

    public ApiResult(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public ApiResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
    
    
}
