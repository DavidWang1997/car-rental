package com.exam.util;

/**
 * 时间工具类
 *
 * @author wangpeng
 */
public class TimeUtil {
    private static final long TIMEZONE_ADDITION = 8 * 60 * 60 * 1000L;

    /**
     * 获取北京时间毫秒数
     * @return
     */
    public static long getSystemCurrentMills() {
        return System.currentTimeMillis() + TIMEZONE_ADDITION;
    }
}
