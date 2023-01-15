package com.exam.service;

import com.exam.constant.Constant;
import com.exam.controller.result.ResultCode;
import com.exam.dao.mapper.*;
import com.exam.module.entity.CarModel;
import com.exam.module.entity.Order;
import com.exam.module.entity.Payment;
import com.exam.module.entity.Refund;
import com.exam.module.vo.DurationVo;
import com.exam.service.cache.CacheManagerService;
import com.exam.service.lock.Lock;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderServiceTest {
    @Tested
    private OrderService orderService;
    @Injectable
    private OrderMapper orderMapper;

    @Injectable
    private CarMapper carMapper;

    @Injectable
    private PaymentMapper paymentMapper;

    @Injectable
    private RefundMapper refundMapper;

    @Injectable
    private CarModelMapper carModelMapper;

    @Injectable
    private CacheManagerService cacheManager;

    @Injectable
    private Lock lock;

    /**
     * 验证：预定功能
     *
     * @Given: 预定信息，cacheManager中未记录相关信息
     * @When: 执行reserve方法
     * @Then: 返回Response失败 3005-车型不存在
     */
    @Test
    public void testReserveWhenCarModelWrong() {
        new Expectations() {{
            cacheManager.getCarIdsByModelId(anyInt);
            result = null;
            cacheManager.getCarModelById(anyInt);
            result = null;
        }};
        Assert.assertEquals(ResultCode.MODEL_NOT_EXISTS.getCode(), orderService.reserve(1, 1, new DurationVo()).getCode());
    }

    /**
     * 验证：预定功能
     *
     * @Given: 预定信息，加锁前被其他用户抢先预定
     * @When: 执行reserve方法
     * @Then: 返回Response失败 3001-预定失败
     */
    @Test
    public void testReserveWhenNoCarToReserve() {
        new Expectations() {{
            cacheManager.getCarIdsByModelId(anyInt);
            List<Integer> carIds = new ArrayList<>();
            carIds.add(1);
            result = carIds;
            cacheManager.getCarModelById(anyInt);
            CarModel carModel = new CarModel();
            carModel.setPrice(new BigDecimal(100));
            carModel.setId(1);
            result = carModel;

            lock.lock(anyInt);
            result = true;

            cacheManager.isAvailableCar((DurationVo) any, anyInt);
            result = false;

            lock.unlock(anyInt);
        }};
        DurationVo durationVo = new DurationVo();
        durationVo.setStartTime(new Date());
        durationVo.setEndTime(new Date(System.currentTimeMillis() + Constant.MILL_SEC_PER_DAY));
        Assert.assertEquals(ResultCode.RESERVER_FAILED.getCode(), orderService.reserve(1, 1, durationVo).getCode());
    }

    /**
     * 验证：预定功能
     *
     * @Given: 预定信息
     * @When: 执行reserve方法
     * @Then: 返回Response成功
     */
    @Test
    public void testReserve() {
        new Expectations() {{
            cacheManager.getCarIdsByModelId(anyInt);
            List<Integer> carIds = new ArrayList<>();
            carIds.add(1);
            result = carIds;
            cacheManager.getCarModelById(anyInt);
            CarModel carModel = new CarModel();
            carModel.setPrice(new BigDecimal(100));
            carModel.setId(1);
            result = carModel;

            lock.lock(anyInt);
            result = true;

            cacheManager.isAvailableCar((DurationVo) any, anyInt);
            result = true;

            orderMapper.addOrder((Order) any);
            result = 1;

            lock.unlock(anyInt);
        }};
        DurationVo durationVo = new DurationVo();
        durationVo.setStartTime(new Date());
        durationVo.setEndTime(new Date(System.currentTimeMillis() + Constant.MILL_SEC_PER_DAY));
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), orderService.reserve(1, 1, durationVo).getCode());
    }

    /**
     * 验证：支付功能
     *
     * @Given: 用户及订单ID，订单不存在
     * @When: 执行pay方法
     * @Then: 返回Response失败 3004-订单不存在
     */
    @Test
    public void testPayWhenOrderNotExist() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            result = null;
        }};
        Assert.assertEquals(ResultCode.ORDER_NOT_EXISTS.getCode(), orderService.pay(1, 1).getCode());
    }

    /**
     * 验证：支付功能
     *
     * @Given: 用户及订单ID，用户无权限操作
     * @When: 执行pay方法
     * @Then: 返回Response失败 2004-用户权限不足
     */
    @Test
    public void testPayWhenUserHasNoRight() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            Order order = new Order();
            order.setUserId(2);
            result = order;
        }};
        Assert.assertEquals(ResultCode.USER_HAS_NO_RIGHT.getCode(), orderService.pay(1, 1).getCode());
    }

    /**
     * 验证：支付功能
     *
     * @Given: 用户及订单ID，账单状态无法支付
     * @When: 执行pay方法
     * @Then: 返回Response失败 3003-当前状态不支持付款
     */
    @Test
    public void testPayWhenOrderStatusWrong() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            Order order = new Order();
            order.setStatus(1);
            order.setUserId(1);
            result = order;
            paymentMapper.selectPaymentByOrderId(anyInt);
            result = null;
        }};
        Assert.assertEquals(ResultCode.PAY_FAILED.getCode(), orderService.pay(1, 1).getCode());
    }

    /**
     * 验证：支付功能
     *
     * @Given: 用户及订单ID
     * @When: 执行pay方法
     * @Then: 返回Response成功
     */
    @Test
    public void testPay() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            Order order = new Order();
            order.setStatus(0);
            order.setUserId(1);
            result = order;
            paymentMapper.selectPaymentByOrderId(anyInt);
            result = null;
            paymentMapper.addPayment((Payment) any);
            result = 1;
            paymentMapper.updateStatusByPaymentId(anyInt, anyInt);
            orderMapper.updateStatusByOrderId(anyInt, anyInt);

        }};
        Assert.assertEquals(ResultCode.SUCCESS.getCode(),
                orderService.pay(1, 1).getCode());
    }

    /**
     * 验证：取消订单功能
     *
     * @Given: 用户及订单ID，订单不存在
     * @When: 执行cancel方法
     * @Then: 返回Response失败 3004-订单不存在
     */
    @Test
    public void testCancelWhenOrderNotExist() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            result = null;
        }};
        Assert.assertEquals(ResultCode.ORDER_NOT_EXISTS.getCode(), orderService.cancel(1, 1).getCode());
    }

    /**
     * 验证：取消订单功能
     *
     * @Given: 用户及订单ID，用户无权限操作
     * @When: 执行cancel方法
     * @Then: 返回Response失败 2004-用户权限不足
     */
    @Test
    public void testCancelWhenUserHasNoRight() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            Order order = new Order();
            order.setUserId(2);
            result = order;
        }};
        Assert.assertEquals(ResultCode.USER_HAS_NO_RIGHT.getCode(), orderService.cancel(1, 1).getCode());
    }

    /**
     * 验证：取消订单功能
     *
     * @Given: 用户及订单ID, 订单未付款
     * @When: 执行cancel方法
     * @Then: 返回Response成功
     */
    @Test
    public void testCancelWhenOrderNotPaid() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            Order order = new Order();
            order.setStatus(0);
            order.setUserId(1);
            result = order;
        }};
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), orderService.cancel(1, 1).getCode());
    }

    /**
     * 验证：取消订单功能
     *
     * @Given: 用户及订单ID, 订单已付款
     * @When: 执行cancel方法
     * @Then: 返回Response成功
     */
    @Test
    public void testCancelWhenOrderIsPaid() {
        new Expectations() {{
            orderMapper.selectOrderById(anyInt);
            Order order = new Order();
            order.setStatus(1);
            order.setUserId(1);
            result = order;

            paymentMapper.selectPaymentByOrderId(anyInt);
            Payment payment = new Payment();
            payment.setId(1);
            payment.setAmount(new BigDecimal(100));
            refundMapper.addRefund((Refund) any);
            refundMapper.updateStatusByRefundId(anyInt, anyInt);
            orderMapper.updateStatusByOrderId(anyInt, anyInt);
            cacheManager.cancelCarReserveInfo((Order) any);

        }};
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), orderService.cancel(1, 1).getCode());
    }
}