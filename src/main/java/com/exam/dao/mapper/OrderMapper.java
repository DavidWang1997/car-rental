package com.exam.dao.mapper;

import com.exam.module.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * Mybatis OrderMapper
 *
 * @author wangpeng
 */
@Mapper
public interface OrderMapper {
    int selectOrderCountByTimeAndCarId(Integer carId, Date startTime, Date endTime);

    Order selectOrderById(Integer orderId);

    List<Order> selectOrderByUserId(Integer userId);

    List<Order> selectOrderByTime(Date today);

    int addOrder(Order order);

    void updateStatusByOrderId(Integer orderId, Integer status);
}
