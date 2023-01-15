package com.exam.dao.mapper;

import com.exam.module.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mybatis PaymentMapper
 *
 * @author wangpeng
 */
@Mapper
public interface PaymentMapper {
    Payment selectPaymentByOrderId(Integer orderId);

    int addPayment(Payment payment);

    void updateStatusByPaymentId(Integer status, Integer paymentId);
}
