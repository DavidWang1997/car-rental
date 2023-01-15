package com.exam.module.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * Table CarModel
 *
 * @author wangpeng
 */
@Data
public class CarModel {
    private int id;
    private String name;
    private String type;
    private int capacity;
    private int number;
    private BigDecimal price;
    private Date gmtCreate;
    private Date gmtModified;
}
