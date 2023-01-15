package com.exam.controller.interceptor;

import com.exam.util.TokenState;
import com.exam.util.Tokenizer;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * jwt拦截实现类
 *
 * @author wangpeng
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    /**
     * 鉴权处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 从request中得到token
        String token = request.getHeader("token");
        Map<String, Object> result = Tokenizer.validToken(token);
        String state = (String) result.get("state");
        if (state.equals(TokenState.VALID.toString())) {
            JSONObject payload = (JSONObject) result.get("data");
            request.setAttribute("payload", payload);
            return true;
        } else if (state.equals(TokenState.INVALID.toString())) {
            // 按照通用错误格式将返回数据写入response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":\"500\", \"msg\":\"Invalid token\"}");
            return false;
        } else if (state.equals(TokenState.EXPIRED.toString())) {
            //按照通用错误格式将返回数据写入response.
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"code\":\"500\", \"msg\":\"Expired token\"}");
            return false;
        }
        return false;
    }
}
