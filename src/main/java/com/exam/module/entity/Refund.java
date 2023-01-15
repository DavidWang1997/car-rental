package com.exam.module.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Table Refund
 *
 * @author wangpeng
 */
@Data
public class Refund {
    private int id;
    private int paymentId;
    private BigDecimal amount;
    private int status;
    private Date gmtCreate;
    private Date gmtModified;
}
