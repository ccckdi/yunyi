package com.cy.yunyi.common.api;

/**
 * @Author: chx
 * @Description: 封装API的返回结果
 * @DateTime: 2021/11/15 23:18
 **/
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    AUTH_OPENID_UNACCESS(708, "openid 获取失败"),
    AUTH_OPENID_BINDED(709, "openid已绑定账号");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
