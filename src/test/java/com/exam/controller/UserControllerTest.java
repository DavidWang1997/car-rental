package com.exam.controller;

import com.exam.controller.mock.MockHttpServletResponse;
import com.exam.controller.result.Response;
import com.exam.controller.result.ResultCode;
import com.exam.module.entity.User;
import com.exam.module.vo.UserVo;
import com.exam.module.vo.query.LoginQuery;
import com.exam.service.UserService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

public class UserControllerTest {
    @Tested
    private UserController userController;
    @Injectable
    private UserService userService;

    /**
     * 验证：登录功能
     *
     * @Given: 参数不符
     * @When: 执行login方法
     * @Then: 返回Response失败 1001-参数校验不通过
     */
    @Test
    public void testLoginWhenParamCheckFailed() {
        LoginQuery query = new LoginQuery();
        query.setUserName("");
        query.setPassword("");
        HttpServletResponse res = new MockHttpServletResponse();
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), userController.login(query, res).getCode());

        query.setUserName("Tom");
        query.setPassword("");
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), userController.login(query, res).getCode());

    }

    /**
     * 验证：登录功能
     *
     * @Given: 参数正常
     * @When: 执行login方法
     * @Then: 返回Response成功
     */
    @Test
    public void testLogin() {
        LoginQuery query = new LoginQuery();
        HttpServletResponse res = new MockHttpServletResponse();

        query.setUserName("Tom");
        query.setPassword("1234");
        new Expectations() {{
            userService.login(anyString, anyString, (MockHttpServletResponse) any);
            result = Response.success(new User());
        }};
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), userController.login(query, res).getCode());
    }

    /**
     * 验证：注册功能
     *
     * @Given: 参数不符
     * @When: 执行register方法
     * @Then: 返回Response失败 1001-参数校验不通过
     */
    @Test
    public void testRegisterWhenParamCheckFailed() {
        UserVo userVo = new UserVo();
        userVo.setUserName("Tom");
        userVo.setPassword("");
        HttpServletResponse res = new MockHttpServletResponse();
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), userController.register(userVo).getCode());
    }

    /**
     * 验证：注册功能
     *
     * @Given: 参数正常
     * @When: 执行register方法
     * @Then: 返回Response成功
     */
    @Test
    public void testRegister() {
        UserVo userVo = new UserVo();
        userVo.setUserName("Tom");
        userVo.setPassword("1234");
        new Expectations() {{
            userService.register(anyString, anyString, anyString, anyString);
            result = Response.success(new User());
        }};

        Assert.assertEquals(ResultCode.SUCCESS.getCode(), userController.register(userVo).getCode());
    }
}