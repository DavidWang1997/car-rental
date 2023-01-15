package com.exam.controller.result;


import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果封装
 *
 * @param <T>
 * @author wangpeng
 */
@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int SUCCESS_CODE = 0;
    private static final int ERROR_CODE = 1;

    private int code;

    private String msg;

    private T data;

    public Response() {

    }

    public Response(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public Response(ResultCode code, T data) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }

    /**
     * 通用成功
     */
    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.data = data;
        response.code = ResultCode.SUCCESS.getCode();
        response.msg = ResultCode.SUCCESS.getMsg();
        return response;
    }

    public static <T> Response<T> fail(T data) {
        Response<T> response = new Response<>();
        response.data = data;
        response.code = ResultCode.FAIL.getCode();
        response.msg = ResultCode.FAIL.getMsg();
        return response;
    }

    public static <T> Response<T> fail(ResultCode code) {
        Response<T> response = new Response<>();
        response.code = code.getCode();
        response.msg = code.getMsg();
        return response;
    }

    public static <T> Response<T> fail(ResultCode code, T data) {
        Response<T> response = new Response<>();
        response.data = data;
        response.code = code.getCode();
        response.msg = code.getMsg();
        return response;
    }


    public static <T> Response<T> paramCheckFail() {
        Response<T> response = new Response<>();
        response.code = ResultCode.PARAM_CHECK_FAILED.getCode();
        response.msg = ResultCode.PARAM_CHECK_FAILED.getMsg();
        return response;
    }
}
