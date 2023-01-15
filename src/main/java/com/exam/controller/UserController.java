package com.exam.controller;

import com.exam.controller.result.Response;
import com.exam.module.vo.query.LoginQuery;
import com.exam.module.vo.UserVo;
import com.exam.service.UserService;
import com.exam.util.ParamCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户相关UserController
 *
 * @author wangpeng
 */
@Api(tags = "UserController")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    @ResponseBody
    public Response login(@RequestBody LoginQuery query, HttpServletResponse response) {
        if (!ParamCheckUtil.checkNotNull(query)) {
            return Response.paramCheckFail();
        }
        return userService.login(query.getUserName(), query.getPassword(), response);
    }

    @ApiOperation(value = "用户注册接口")
    @PostMapping("/register")
    @ResponseBody
    public Response register(@RequestBody UserVo userVo) {
        if (!ParamCheckUtil.checkNotNull(userVo)) {
            return Response.paramCheckFail();
        }
        return userService.register(userVo.getUserName(), userVo.getPassword(), userVo.getPhone(), userVo.getEmail());

    }
}
