package com.exam.service;

import com.exam.constant.Constant;
import com.exam.controller.result.Response;
import com.exam.controller.result.ResultCode;
import com.exam.dao.mapper.UserMapper;
import com.exam.module.entity.User;
import com.exam.util.MD5Util;
import com.exam.util.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关Service
 *
 * @author wangpeng
 */
@Service("userService")
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param name
     * @param pass
     * @param response
     * @return
     */
    public Response login(String name, String pass, HttpServletResponse response) {
        User user = userMapper.selectUserByName(name);
        if (user == null) {
            return Response.fail(ResultCode.USER_NOT_EXISTS, false);
        }
        if (!MD5Util.getMD5(pass).equals(user.getPassword())) {
            return Response.fail(ResultCode.USER_WRONG_PASS, false);
        }

        String token = generateToken(user.getId());
        response.addHeader("header", token);
        return Response.success(token);
    }

    /**
     * 用户注册
     * @param name
     * @param pass
     * @param phone
     * @param email
     * @return
     */
    public Response register(String name, String pass, String phone, String email) {

        // 如果数据库中包含相同名称用户，则拒绝插入
        if(userMapper.selectUserByName(name) != null){
            return Response.fail(ResultCode.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUserName(name);
        user.setPassword(MD5Util.getMD5(pass));
        user.setPhone(phone);
        user.setEmail(email);
        try{
            userMapper.addUser(user);
        }catch (Exception e){
            LOGGER.error("注册失败",e);
            return Response.fail(ResultCode.SYSTEM_ERROR);
        }
        return Response.success(true);
    }

    /**
     * 生成token
     * @param userId
     * @return
     */
    private String generateToken(Integer userId) {
        //生成 token
        Map<String, Object> payload = new HashMap<>();
        Date date = new Date();
        payload.put("userId", userId);// userID 植入token
        payload.put("startTime", date.getTime());//生成时间
        payload.put("expiryTime", date.getTime() + Constant.EXPIRY_TIME);//过期时间1小时
        return Tokenizer.createToken(payload);
    }
}
