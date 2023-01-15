package com.exam.controller;

import com.exam.controller.result.Response;
import com.exam.module.vo.DurationVo;
import com.exam.service.OrderService;
import com.exam.util.HttpUtil;
import com.exam.util.ParamCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 订单相关OrderController
 *
 * @author wangpeng
 */
@Api(tags = "OrderController")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/reserve")
    @ResponseBody
    @ApiOperation(value = "预定")
    @ApiImplicitParam(value = "鉴权token", name = "token", paramType = "header", dataType = "String", required = true)
    public Response reserve(HttpServletRequest request, @RequestParam Integer modelId, @RequestBody DurationVo durationVo) {

        Integer userId = HttpUtil.getUserId(request);

        if (userId == null || modelId == null || ParamCheckUtil.checkNotNull(durationVo)) {
            return Response.paramCheckFail();
        }

        return orderService.reserve(userId, modelId, durationVo);
    }

    @PostMapping("/pay")
    @ResponseBody
    @ApiOperation(value = "支付订单")
    @ApiImplicitParam(value = "鉴权token", name = "token", paramType = "header", dataType = "String", required = true)
    public Response pay(HttpServletRequest request, @RequestBody Integer orderId) {
        Integer userId = HttpUtil.getUserId(request);
        if (userId == null || orderId == null) {
            return Response.paramCheckFail();
        }
        return orderService.pay(userId, orderId);

    }

    @GetMapping("/query")
    @ResponseBody
    @ApiOperation(value = "查询本用户订单")
    @ApiImplicitParam(value = "鉴权token", name = "token", paramType = "header", dataType = "String", required = true)
    public Response query(HttpServletRequest request) {
        Integer userId = HttpUtil.getUserId(request);
        if (userId == null) {
            return Response.paramCheckFail();
        }
        return orderService.query(userId);
    }

    @PostMapping("/cancel")
    @ResponseBody
    @ApiOperation(value = "取消订单")
    @ApiImplicitParam(value = "鉴权token", name = "token", paramType = "header", dataType = "String", required = true)
    public Response cancel(HttpServletRequest request, @RequestBody Integer orderId) {
        Integer userId = HttpUtil.getUserId(request);
        if (orderId == null) {
            return Response.paramCheckFail();
        }
        return orderService.cancel(userId, orderId);
    }
}
