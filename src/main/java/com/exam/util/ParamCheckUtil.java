package com.exam.util;

import com.exam.module.vo.query.LoginQuery;
import com.exam.module.vo.DurationVo;
import com.exam.module.vo.UserVo;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 参数检查工具类
 *
 * @author wangpeng
 */
public class ParamCheckUtil {
    public static boolean checkNotNull(DurationVo durationVo) {
        return !(Objects.isNull(durationVo) || Objects.isNull(durationVo.getStartTime()) || Objects.isNull(durationVo.getEndTime())
                || durationVo.getEndTime().getTime() <= durationVo.getStartTime().getTime()
                || durationVo.getStartTime().getTime() < System.currentTimeMillis());
    }

    public static boolean checkNotNull(LoginQuery loginQuery) {
        return !(Objects.isNull(loginQuery) || StringUtils.isEmpty(loginQuery.getUserName()) || StringUtils.isEmpty(loginQuery.getPassword()));
    }

    public static boolean checkNotNull(UserVo userVo) {
        return !(Objects.isNull(userVo) || StringUtils.isEmpty(userVo.getUserName()) || StringUtils.isEmpty(userVo.getPassword()));
    }
}
