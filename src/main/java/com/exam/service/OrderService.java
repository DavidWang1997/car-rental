package com.exam.service;

import com.exam.controller.result.Response;
import com.exam.controller.result.ResultCode;
import com.exam.dao.mapper.*;
import com.exam.module.entity.CarModel;
import com.exam.module.entity.Order;
import com.exam.module.entity.Payment;
import com.exam.module.entity.Refund;
import com.exam.module.vo.DurationVo;
import com.exam.service.cache.CacheManagerService;
import com.exam.service.lock.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单相关Service
 *
 * @author wangpeng
 */
@Service("orderService")
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final long MILLS_ONE_DAY = 24 * 60 * 60 * 1000L;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CarMapper carMapper;

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private RefundMapper refundMapper;

    @Resource
    private CarModelMapper carModelMapper;

    @Resource
    private CacheManagerService cacheManager;

    @Autowired
    private Lock lock;

    /**
     * 预定
     * @param userId
     * @param modelId
     * @param durationVo
     * @return
     */
    public Response reserve(int userId, int modelId, DurationVo durationVo) {
        Date startTime = durationVo.getStartTime();
        Date endTime = durationVo.getEndTime();

        // 查询该车型下的汽车
        List<Integer> carIdList = cacheManager.getCarIdsByModelId(modelId);
        // 查询车型信息
        CarModel carModel = cacheManager.getCarModelById(modelId);

        if(CollectionUtils.isEmpty(carIdList) || carModel == null){
            return Response.fail(ResultCode.MODEL_NOT_EXISTS);
        }

        // 创建订单
        Order order = createOrder(startTime, endTime,userId, carModel.getPrice());

        order.setId(-1);

        for (int carId : carIdList) {
            if (lock.lock(carId)) {
                try {
                    // 查询该车是否可用
                    if (cacheManager.isAvailableCar(durationVo, carId)) {
                        order.setCarId(carId);
                        orderMapper.addOrder(order);
                        order = orderMapper.selectOrderById(order.getId());
                        // 更新缓存信息
                        cacheManager.addCarReserveInfo(order);
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock(carId);
                }
            }
        }
        // 未创建成功
        if (order.getId() == -1) {
            return Response.fail(ResultCode.RESERVER_FAILED);
        }

        return Response.success(order);
    }

    /**
     * 创建订单
     * @param startTime
     * @param endTime
     * @param userId
     * @param price
     * @return
     */
    private static Order createOrder(Date startTime, Date endTime,int userId, BigDecimal price) {
        long dur = endTime.getTime() - startTime.getTime();

        int days = (int) (dur % MILLS_ONE_DAY == 0 ? dur / MILLS_ONE_DAY : dur / MILLS_ONE_DAY + 1);

        Order order = new Order();
        order.setReserveStartTime(startTime);
        order.setReserveEndTime(endTime);
        order.setDays(days);
        order.setAmount(price.multiply(BigDecimal.valueOf(days)));
        order.setUserId(userId);
        return order;
    }

    /**
     * 支付
     * @param userId
     * @param orderId
     * @return
     */
    public Response pay(int userId, int orderId) {
        Order order = orderMapper.selectOrderById(orderId);
        // 订单不存在
        if (order == null) {
            return Response.fail(ResultCode.ORDER_NOT_EXISTS);
        }

        // 订单对应用户id不符
        if (userId != order.getUserId()) {
            return Response.fail(ResultCode.USER_HAS_NO_RIGHT);
        }
        // 订单状态不正确或已存在支付记录
        Payment exist = paymentMapper.selectPaymentByOrderId(orderId);
        if(order.getStatus() != 0 || Objects.nonNull(exist)){
            return Response.fail(ResultCode.PAY_FAILED);
        }

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getAmount());
        try{
            paymentMapper.addPayment(payment);
            paymentMapper.updateStatusByPaymentId(1, payment.getId());
            orderMapper.updateStatusByOrderId(orderId, 1);
        }catch (Exception e){
            LOGGER.error("支付失败", e);
            return Response.fail(ResultCode.SYSTEM_ERROR);
        }

        return Response.success(true);
    }

    /**
     * 查询用户订单
     * @param userId
     * @return
     */
    public Response query(int userId) {
        return Response.success(orderMapper.selectOrderByUserId(userId));
    }

    /**
     * 取消订单（退款）
     * @param userId
     * @param orderId
     * @return
     */
    public Response cancel(int userId, int orderId) {
        Order order = orderMapper.selectOrderById(orderId);

        if (order == null || order.getUserId() != userId) {
            return Response.fail(ResultCode.ORDER_NOT_EXISTS);
        }

        if (order.getUserId() != userId) {
            return Response.fail(ResultCode.USER_HAS_NO_RIGHT);
        }

        try{
            switch (order.getStatus()) {
                case 0:
                    orderMapper.updateStatusByOrderId(orderId, 4);
                    return Response.success(orderMapper.selectOrderById(orderId));
                case 1:
                    Payment payment = paymentMapper.selectPaymentByOrderId(orderId);
                    Refund refund = new Refund();
                    refund.setPaymentId(payment.getId());
                    refund.setAmount(payment.getAmount());
                    refundMapper.addRefund(refund);
                    refundMapper.updateStatusByRefundId(1, refund.getId());
                    orderMapper.updateStatusByOrderId(orderId, 5);
                    cacheManager.cancelCarReserveInfo(order);
                    return Response.success(orderMapper.selectOrderById(orderId));
                case 2:
                case 3:
                case 4:
                case 5:
                default:
                    return Response.fail(ResultCode.CANCEL_FAILED, order);

            }
        }catch (Exception e){
            LOGGER.error("取消订单失败", e);
            return Response.fail(ResultCode.SYSTEM_ERROR);
        }


    }
}
