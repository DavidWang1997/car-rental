package com.exam.module.entity;

import lombok.Data;

import java.util.Date;
/**
 * Table car
 *
 * @author wangpeng
 */
@Data
public class Car {
    private int id;
    private int carModelId;
    private String license;
    private Date gmtCreate;
    private Date gmtModified;
}
