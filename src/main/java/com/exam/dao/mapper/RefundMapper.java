package com.exam.dao.mapper;

import com.exam.module.entity.Refund;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mybatis RefundMapper
 *
 * @author wangpeng
 */
@Mapper
public interface RefundMapper {
    void addRefund(Refund refund);

    void updateStatusByRefundId(Integer status, Integer refundId);
}
