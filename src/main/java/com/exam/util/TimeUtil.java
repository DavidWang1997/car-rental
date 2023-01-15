package com.exam.util;

import static com.exam.constant.Constant.TIMEZONE_ADDITION;

/**
 * 时间工具类
 *
 * @author wangpeng
 */
public class TimeUtil {


    /**
     * 获取北京时间毫秒数
     * @return
     */
    public static long getSystemCurrentMills() {
        return System.currentTimeMillis() + TIMEZONE_ADDITION;
    }
}
