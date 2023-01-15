package com.exam.controller;

import com.exam.controller.result.ResultCode;
import com.exam.module.vo.DurationVo;
import com.exam.service.CarService;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class CarControllerTest {
    @Tested
    private CarController controller;

    @Injectable
    private CarService carService;

    /**
     * 验证：查询指定时间段内的可用车型信息
     *
     * @Given: DurationVo 参数校验不符
     * @When: 执行query方法
     * @Then: 返回Response失败
     */
    @Test
    public void testQueryWhenDurationVoNotQualified() {
        // 开始和结束时间为空
        DurationVo durationVo1 = new DurationVo();
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), controller.query(durationVo1).getCode());
        // 结束时间为空
        DurationVo durationVo2 = new DurationVo();
        durationVo2.setStartTime(getCurrent(10000L));
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), controller.query(durationVo2).getCode());
        // 开始时间晚于结束时间
        DurationVo durationVo3 = new DurationVo();
        durationVo3.setStartTime(getCurrent(10000L));
        durationVo3.setEndTime(getCurrent(-10000L));
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), controller.query(durationVo3).getCode());

        // 开始时间晚于当前时间
        DurationVo durationVo4 = new DurationVo();
        durationVo3.setStartTime(getCurrent(-10000L));
        durationVo3.setEndTime(getCurrent(10000L));
        Assert.assertEquals(ResultCode.PARAM_CHECK_FAILED.getCode(), controller.query(durationVo4).getCode());

    }

    /**
     * 验证：查询指定时间段内的可用车型信息
     *
     * @Given: DurationVo 参数校验通过
     * @When: 执行query方法
     * @Then: 返回Response成功
     */
    @Test
    public void testQueryWhenDurationVoQualified() {
        DurationVo durationVo = new DurationVo();
        durationVo.setStartTime(getCurrent(10000L));
        durationVo.setEndTime(getCurrent(20000L));
        Assert.assertEquals(ResultCode.SUCCESS.getCode(), controller.query(durationVo).getCode());
    }


    private Date getCurrent(long plus) {
        return new Date(System.currentTimeMillis() + plus);
    }


}