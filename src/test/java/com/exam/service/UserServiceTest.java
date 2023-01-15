package com.exam.service;

import com.exam.controller.mock.MockHttpServletResponse;
import com.exam.controller.result.ResultCode;
import com.exam.dao.mapper.UserMapper;
import com.exam.module.entity.User;
import com.exam.util.MD5Util;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceTest {
    @Tested
    private UserService userService;
    @Injectable
    private UserMapper userMapper;

    /**
     * 验证：登录功能
     *
     * @Given: 用户信息
     * @When: 执行login方法
     * @Then: 返回Response失败 2001-用户不存在
     */
    @Test
    public void testLoginWhenUserNotExist() {
        new Expectations() {{
            userMapper.selectUserByName(anyString);
            result = null;
        }};
        Assert.assertEquals(ResultCode.USER_NOT_EXISTS.getCode(), userService.login("Tom", "1234", new MockHttpServletResponse()).getCode());
    }

    /**
     * 验证：登录功能
     *
     * @Given: 用户信息
     * @When: 执行login方法
     * @Then: 返回Response失败 2003-用户密码错误
     */
    @Test
    public void testLoginWhenUserPassNotCorrect() {
        new Expectations() {{
            userMapper.selectUserByName(anyString);
            User user = new User();
            user.setPassword(MD5Util.getMD5("1235"));
            result = user;
        }};
        Assert.assertEquals(ResultCode.USER_WRONG_PASS.getCode(), userService.login("Tom", "1234", new MockHttpServletResponse()).getCode());
    }

    /**
     * 验证：登录功能
     *
     * @Given: 用户信息
     * @When: 执行login方法
     * @Then: 返回Response成功
     */
    @Test
    public void testLogin() {
        new Expectations() {{
            userMapper.selectUserByName(anyString);
            User user = new User();
            user.setPassword(MD5Util.getMD5("1234"));
            result = user;
        }};
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), userService.login("Tom", "1234", new MockHttpServletResponse()).getCode());
    }

    /**
     * 验证：注册功能
     *
     * @Given: 用户信息
     * @When: 执行register方法
     * @Then: 返回Response失败 2002-用户名已存在
     */
    @Test
    public void testRegisterWhenUserExists() {
        new Expectations() {{
            userMapper.selectUserByName(anyString);
            User user = new User();
            user.setPassword(MD5Util.getMD5("1234"));
            result = user;
        }};
        Assert.assertEquals(ResultCode.USER_ALREADY_EXISTS.getCode(), userService.register("Tom", "1234", "", "").getCode());
    }

    /**
     * 验证：注册功能
     *
     * @Given: 用户信息
     * @When: 执行register方法
     * @Then: 返回Response成功
     */
    @Test
    public void testRegister() {
        new Expectations() {{
            userMapper.selectUserByName(anyString);
            result = null;
            userMapper.addUser((User) any);
        }};
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), userService.register("Tom", "1234", "", "").getCode());
    }
}