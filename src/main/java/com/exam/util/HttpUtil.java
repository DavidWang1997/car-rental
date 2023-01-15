package com.exam.util;

import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP工具类
 *
 * @author wangpeng
 */
public class HttpUtil {
    /**
     * 通过request中的payload获取用户ID
     *
     * @param request
     * @return
     */
    public static Integer getUserId(HttpServletRequest request) {
        JSONObject payload = (JSONObject) request.getAttribute("payload");
        return Integer.valueOf(payload.getAsString("userId"));
    }
}
