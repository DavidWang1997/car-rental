package com.exam.controller.result;

/**
 * 返回结果code枚举
 * @author wangpeng
 */
public enum ResultCode {
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),

    SYSTEM_ERROR(3,"系统异常"),

    PARAM_CHECK_FAILED(1001, "参数校验不通过"),

    USER_NOT_EXISTS(2001, "用户不存在"),
    USER_ALREADY_EXISTS(2002, "用户名已存在"),

    USER_WRONG_PASS(2003, "用户密码错误"),

    USER_HAS_NO_RIGHT(2004, "用户权限不足"),


    RESERVER_FAILED(3001, "预定失败"),
    CANCEL_FAILED(3002, "当前状态不支持取消"),
    PAY_FAILED(3003, "当前状态不支持付款"),

    ORDER_NOT_EXISTS(3004, "订单不存在"),
    MODEL_NOT_EXISTS(3005, "车型不存在"),

    ;

    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
