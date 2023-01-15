package com.exam.module.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Table Order
 *
 * @author wangpeng
 */
@Data
public class Order {
    private int id;
    private int userId;
    private int carId;
    private Date reserveStartTime;
    private Date reserveEndTime;
    private Date actualStartTime;
    private Date actualEndTime;
    private int days;
    private BigDecimal amount;
    private int status;
    private Date gmtCreate;
    private Date gmtModified;
}
