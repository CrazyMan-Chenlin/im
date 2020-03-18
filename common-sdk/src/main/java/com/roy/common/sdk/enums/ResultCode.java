package com.roy.common.sdk.enums;

/**
 * @author chenlin
 */
public enum  ResultCode {
    //成功
    SUCCESS(1,"成功"),
    SERVER_ERROR(2,"服务异常"),
    //1000～1999 区间表示参数错误
    PARAM_IS_INVALID(1001,"参数无效"),
    PARAM_IS_BLACK(1002,"参数为空"),
    PARAM_TYPE_BIND_ERROR(1003,"参数类型绑定错误"),
    PARAM_NOT_COMPLETE(1004,"参数缺失"),
    //2000～2999 区间表示用户错误
    USER_NOT_LOGGED_IN(2001,"用户未登录"),
    USER_LOGIN_ERROR(2002,"账户不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003,"用户被禁用"),
    USER_NOT_EXIST(2004,"用户不存在"),
    USER_HAS_EXISTED(2005,"用户已存在"),
    USERNAME_EXIST(2006,"用户名已被注册"),
    EMAIL_EXIST(2007,"邮箱已被注册"),
    LOGIN_ERROR(2008,"登录异常"),
    PASSWORD_ERROR(2009,"密码错误");
    //3000～3999 区间表示接口异常

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
