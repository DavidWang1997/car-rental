package com.exam.module.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Table Payment
 *
 * @author wangpeng
 */
@Data
public class Payment {
    private int id;
    private int orderId;
    private BigDecimal amount;
    private int status;
    private Date gmtCreate;
    private Date gmtModified;
}
