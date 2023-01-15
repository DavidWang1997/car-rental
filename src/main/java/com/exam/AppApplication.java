package com.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

/**
 * 启动类
 *
 * @author wangpeng
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.exam.dao.mapper", "com.exam.controller", "com.exam.controller.config", "com.exam.service"})
public class AppApplication {
    public static void main(String[] args) {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
        TimeZone.setDefault(timeZone);
        SpringApplication.run(AppApplication.class, args);
    }
}
