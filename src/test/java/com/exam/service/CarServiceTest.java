package com.exam.service;

import com.exam.constant.Constant;
import com.exam.module.vo.DurationVo;
import com.exam.service.cache.CacheManagerService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class CarServiceTest {
    @Tested
    private CarService carService;

    @Injectable
    private CacheManagerService cacheManagerService;

    /**
     * 验证：查询指定时间段内的可用车型信息
     *
     * @Given: DurationVo
     * @When: 执行query方法
     * @Then: 返回预定信息
     */
    @Test
    public void testQuery() {
        DurationVo durationVo = new DurationVo();
        durationVo.setStartTime(new Date(System.currentTimeMillis()));
        durationVo.setEndTime(new Date(System.currentTimeMillis() + Constant.MILL_SEC_PER_DAY));
        new Expectations() {{
            cacheManagerService.getAvailableCarModels((DurationVo) any);
            result = new ArrayList<>();
        }};
        Assert.assertEquals(0, carService.query(durationVo).size());
    }
}