package com.bobiko.common.core.constants;

public enum ResultStatus
{
    SUCCESS(1, "操作成功"),
    ERROR(0, "失败"),
    FAILED(500, "系统繁忙,请稍后重试"),
    UNAUTHORIZED(401, "未登录或登录已过期,请重新登录"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "未找到资源"),
    UNKNOWN(-1, "未知错误,请稍后再试");


    private int code;
    private String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
