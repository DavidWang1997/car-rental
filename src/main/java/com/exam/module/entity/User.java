package com.exam.module.entity;

import lombok.Data;

import java.util.Date;

/**
 * Table User
 *
 * @author wangpeng
 */
@Data
public class User {
    private int id;
    private String userName;
    private String password;
    private int staus;
    private String phone;
    private String email;
    private Date gmtCreate;
    private Date gmtModified;
}
