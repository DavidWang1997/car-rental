package com.exam.controller;

import com.exam.constant.Constant;
import com.exam.controller.mock.MockHttpServletRequest;
import com.exam.controller.result.Response;
import com.exam.controller.result.ResultCode;
import com.exam.module.entity.Order;
import com.exam.module.vo.DurationVo;
import com.exam.service.OrderService;
import com.exam.util.HttpUtil;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class OrderControllerTest {

    @Tested
    private OrderController orderController;

    @Injectable
    private OrderService orderService;

    /**
     * 验证：预定功能
     *
     * @Given: 参数校验不符
     * @When: 执行reserve方法
     * @Then: 返回Response失败 1001-参数校验不通过
     */
    @Test
    public void testReserveWhenParamCheckFailed() {
        HttpServletRequest request = new MockHttpServletRequest();
        new MockUp<HttpUtil>() {
            @Mock
            public Integer getUserId(HttpServletRequest request) {
                return 1;
            }
        };
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), orderController.reserve(request, 1, new DurationVo()).getCode());
    }

    /**
     * 验证：预定功能
     *
     * @Given: 参数正常
     * @When: 执行reserve方法
     * @Then: 返回Response成功
     */
    @Test
    public void testReserve() {
        HttpServletRequest request = new MockHttpServletRequest();
        new MockUp<HttpUtil>() {
            @Mock
            public Integer getUserId(HttpServletRequest request) {
                return 1;
            }
        };
        DurationVo durationVo = new DurationVo();
        durationVo.setStartTime(new Date(System.currentTimeMillis() + 10000L));
        durationVo.setEndTime(new Date(System.currentTimeMillis() + Constant.MILL_SEC_PER_DAY));
        new Expectations() {{
            orderService.reserve(anyInt, anyInt, (DurationVo) any);
            result = Response.success(new Order());
        }};
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), orderController.reserve(request, 1, durationVo).getCode());
    }
}