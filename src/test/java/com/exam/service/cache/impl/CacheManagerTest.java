package com.exam.service.cache.impl;

import com.exam.constant.Constant;
import com.exam.dao.mapper.CarMapper;
import com.exam.dao.mapper.CarModelMapper;
import com.exam.dao.mapper.OrderMapper;
import com.exam.module.entity.Order;
import mockit.Deencapsulation;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CacheManagerTest {
    @Tested
    private CacheManager cacheManager;

    @Injectable
    private OrderMapper orderMapper;

    @Injectable
    private CarModelMapper carModelMapper;

    @Injectable
    private CarMapper carMapper;

    /**
     * 验证：carScheduleMap中的过期信息被释放
     *
     * @Given: carScheduleMap中包含过期信息
     * @When: 执行releaseSchedule方法
     * @Then: 过期订单信息被释放
     */
    @Test
    public void testReleaseSchedule() {
        Map<Integer, TreeMap<Integer, BitSet>> mockMap = new HashMap<>();
        TreeMap<Integer, BitSet> map = new TreeMap<>();
        BitSet bitSet = new BitSet(1);
        bitSet.set(0);
        map.put((int) (System.currentTimeMillis() / Constant.MILL_SEC_PER_DAY) - 2, bitSet);
        mockMap.put(1, map);
        Deencapsulation.setField(cacheManager, "carScheduleMap", mockMap);
        cacheManager.releaseSchedule();
        Assert.assertEquals(0, mockMap.get(1).size());
    }

    /**
     * 验证：carScheduleMap中添加预定信息
     *
     * @Given: 预定信息
     * @When: 执行addCarReserveInfo方法
     * @Then: 预定信息被添加至缓存中
     */
    @Test
    public void testAddCarReserveInfo() {
        Order order = new Order();
        order.setCarId(2);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 1, 25, 0, 0, 0);

        order.setReserveStartTime(calendar.getTime());
        calendar.add(Calendar.HOUR, 2);

        order.setReserveEndTime(calendar.getTime());
        Map<Integer, TreeMap<Integer, BitSet>> mockMap = new HashMap<>();
        Deencapsulation.setField(cacheManager, "carScheduleMap", mockMap);

        cacheManager.addCarReserveInfo(order);

        Assert.assertEquals(1, mockMap.get(order.getCarId()).size());
        Assert.assertTrue(mockMap.get(order.getCarId()).get((int) ((calendar.getTime().getTime() + Constant.TIMEZONE_ADDITION) / Constant.MILL_SEC_PER_DAY)).get(0));
    }

    /**
     * 验证：carScheduleMap中删除预定信息
     *
     * @Given: 预定信息
     * @When: 执行cancelCarReserveInfo方法
     * @Then: 预定信息被删除
     */
    @Test
    public void testCancelCarReserveInfo() {
        Order order = new Order();
        order.setCarId(2);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 1, 25, 0, 0, 0);
        int day = (int) ((calendar.getTime().getTime() + Constant.TIMEZONE_ADDITION) / Constant.MILL_SEC_PER_DAY);
        order.setReserveStartTime(calendar.getTime());
        calendar.add(Calendar.HOUR, 2);
        order.setReserveEndTime(calendar.getTime());

        Map<Integer, TreeMap<Integer, BitSet>> mockMap = new HashMap<>();
        TreeMap<Integer, BitSet> map = new TreeMap<>();
        BitSet bitSet = new BitSet(Constant.TEN_MINS_PER_DAY);
        bitSet.set(0, 13);
        map.put(day, bitSet);
        mockMap.put(order.getCarId(), map);
        Deencapsulation.setField(cacheManager, "carScheduleMap", mockMap);

        cacheManager.cancelCarReserveInfo(order);

        Assert.assertEquals(0, mockMap.get(order.getCarId()).size());
    }
}