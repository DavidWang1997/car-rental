package com.exam.controller;

import com.exam.controller.result.Response;
import com.exam.module.vo.DurationVo;
import com.exam.service.OrderService;
import com.exam.util.HttpUtil;
import com.exam.util.ParamCheckUtil;
import io.swagger.annotations.*;
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
    @ApiResponses({
            @ApiResponse(code = 1001, message = "参数校验不通过"),
            @ApiResponse(code = 3001, message = "预定失败"),
            @ApiResponse(code = 3005, message = "车型不存在"),
    })
    public Response reserve(HttpServletRequest request, @RequestParam Integer modelId, @RequestBody DurationVo durationVo) {

        Integer userId = HttpUtil.getUserId(request);

        if (userId == null || modelId == null || !ParamCheckUtil.checkNotNull(durationVo)) {
            return Response.paramCheckFail();
        }

        return orderService.reserve(userId, modelId, durationVo);
    }

    @PostMapping("/pay")
    @ResponseBody
    @ApiOperation(value = "支付订单")
    @ApiImplicitParam(value = "鉴权token", name = "token", paramType = "header", dataType = "String", required = true)
    @ApiResponses({
            @ApiResponse(code = 1001, message = "参数校验不通过"),
            @ApiResponse(code = 2004, message = "用户权限不足"),
            @ApiResponse(code = 3003, message = "当前状态不支持付款"),
            @ApiResponse(code = 3004, message = "订单不存在"),
    })
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
    @ApiResponses({
            @ApiResponse(code = 1001, message = "参数校验不通过"),
    })
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
    @ApiResponses({
            @ApiResponse(code = 1001, message = "参数校验不通过"),
            @ApiResponse(code = 2004, message = "用户权限不足"),
            @ApiResponse(code = 3002, message = "当前状态不支持取消"),
            @ApiResponse(code = 3004, message = "订单不存在"),
    })
    public Response cancel(HttpServletRequest request, @RequestBody Integer orderId) {
        Integer userId = HttpUtil.getUserId(request);
        if (orderId == null) {
            return Response.paramCheckFail();
        }
        return orderService.cancel(userId, orderId);
    }
}
