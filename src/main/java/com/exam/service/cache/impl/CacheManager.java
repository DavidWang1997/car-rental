package com.exam.service.cache.impl;

import com.alibaba.fastjson2.JSON;
import com.exam.dao.mapper.CarMapper;
import com.exam.dao.mapper.CarModelMapper;
import com.exam.dao.mapper.OrderMapper;
import com.exam.module.entity.Car;
import com.exam.module.entity.CarModel;
import com.exam.module.entity.Order;
import com.exam.module.vo.DurationVo;
import com.exam.service.cache.CacheManagerService;
import com.exam.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.exam.constant.Constant.*;

/**
 * 汽车可用时间段信息缓存类
 *
 * @author wangpeng
 */
@Service
@EnableScheduling
public class CacheManager implements CacheManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
    private static Map<Integer, List<Integer>> carModelRelationMap = new HashMap<>();

    private static Map<Integer, CarModel> carModelMap = new HashMap<>();
    private static volatile Map<Integer, TreeMap<Integer, BitSet>> carScheduleMap = new ConcurrentHashMap<>();



    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CarModelMapper carModelMapper;

    @Resource
    private CarMapper carMapper;

    /**
     * 从数据库查询订单，对车辆信息缓存初始化
     */
    @PostConstruct
    private void init() {
        LOGGER.info("init CacheManager begin");

        List<CarModel> carModels = carModelMapper.selectCarModels();
        for(CarModel carModel : carModels){
            carModelMap.put(carModel.getId(), carModel);
        }
        LOGGER.info("init carModel done, size = {}", carModels.size());
        LOGGER.debug(JSON.toJSONString(carModels));
        List<Car> cars = carMapper.selectCars();
        for(Car car: cars){
            if(!carModelRelationMap.containsKey(car.getCarModelId())){
                carModelRelationMap.put(car.getCarModelId(), new ArrayList<>());
            }
            carModelRelationMap.get(car.getCarModelId()).add(car.getId());
        }
        LOGGER.debug(JSON.toJSONString(cars));
        LOGGER.debug(JSON.toJSONString(carModelRelationMap));
        LOGGER.info("init car done, size = {}", cars.size());

        List<Order> orders = orderMapper.selectOrderByTime(new Date());
        for (Order order : orders) {
            DurationVo vo = new DurationVo();
            vo.setStartTime(order.getReserveStartTime());
            vo.setEndTime(order.getReserveEndTime());
            addCarReserveInfo(vo, order.getCarId());
        }
        LOGGER.debug(JSON.toJSONString(orders));

        LOGGER.info("init order done, size = {}", orders.size());

    }


    /**
     * 每日凌晨1点定时清除过期信息
     */
    @Scheduled(cron = "0 0 1 * * ?", zone = "GMT+8:00")
    public void releaseSchedule() {
        int currentDay = (int) (TimeUtil.getSystemCurrentMills() / MILL_SEC_PER_DAY);
        LOGGER.info("定时任务清除信息{}", currentDay);
        for (TreeMap<Integer, BitSet> tmpMap : carScheduleMap.values()) {
            while (!CollectionUtils.isEmpty(tmpMap) && tmpMap.firstKey() < currentDay) {
                tmpMap.remove(tmpMap.firstKey());
            }
        }
    }

    /**
     * 新增汽车预定信息
     *
     * @param vo
     * @param carId
     */
    @Override
    public void addCarReserveInfo(DurationVo vo, Integer carId) {

        if (!carScheduleMap.containsKey(carId)) {
            carScheduleMap.put(carId, new TreeMap<>());
        }

        Map<Integer, BitSet> map = carScheduleMap.get(carId);

        Map<Integer, BitSet> bitSetMap = convertToBitSet(vo);

        // 遍历需要预定的每一天
        for (Map.Entry<Integer, BitSet> entry : bitSetMap.entrySet()) {
            // 当天未被预定, 则直接放入
            if (!map.containsKey(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }

            // 更新位集
            map.get(entry.getKey()).or(entry.getValue());
        }
        LOGGER.info(JSON.toJSONString(carScheduleMap));
    }

    /**
     * 新增汽车预定信息
     *
     * @param order
     */
    @Override
    public void addCarReserveInfo(Order order) {
        DurationVo vo = new DurationVo();
        vo.setStartTime(order.getReserveStartTime());
        vo.setEndTime(order.getReserveEndTime());
        addCarReserveInfo(vo, order.getCarId());
    }

    /**
     * 取消汽车预定信息
     *
     * @param vo
     * @param carId
     */
    @Override
    public void cancelCarReserveInfo(DurationVo vo, Integer carId) {

        if (!carScheduleMap.containsKey(carId)) {
            carScheduleMap.put(carId, new TreeMap<>());
        }

        Map<Integer, BitSet> map = carScheduleMap.get(carId);

        Map<Integer, BitSet> bitSetMap = convertToBitSet(vo);

        // 遍历需要取消预定的每一天
        for (Map.Entry<Integer, BitSet> entry : bitSetMap.entrySet()) {
            if (!map.containsKey(entry.getKey())) {
                LOGGER.error("缓存异常，删除未预定信息");
                continue;
            }
            // 当天全部被预定
            if (map.get(entry.getKey()).equals(entry.getValue())) {
                map.remove(entry.getKey());
                continue;
            }

            // 更新位集
            map.get(entry.getKey()).andNot(entry.getValue());
        }
    }

    /**
     * 取消汽车预定信息
     *
     * @param order
     */
    @Override
    public void cancelCarReserveInfo(Order order) {
        DurationVo vo = new DurationVo();
        vo.setStartTime(order.getReserveStartTime());
        vo.setEndTime(order.getReserveEndTime());
        cancelCarReserveInfo(vo, order.getCarId());
    }

    /**
     * 获取给定时间段内所有可用车型
     *
     * @param vo
     * @return
     */
    public List<CarModel> getAvailableCarModels(DurationVo vo) {
        List<CarModel> res = new ArrayList<>();
        for (Map.Entry<Integer, CarModel> entry : carModelMap.entrySet()) {
            if (isAvailableCarModel(vo, entry.getKey()) != null) {
                res.add(entry.getValue());
            }
        }
        return res;
    }

    /**
     * 查询某种车型下在某个时间段内是否有可用车
     * 返回第一辆查询到的可用车的carId
     * 没有可用车时 返回null
     *
     * @param vo
     * @param carModelId
     * @return
     */
    @Override
    public Integer isAvailableCarModel(DurationVo vo, Integer carModelId) {
        // 没有此种车型
        if (CollectionUtils.isEmpty(carModelRelationMap.get(carModelId))) {
            LOGGER.info("没有此种车型{}",carModelId);
            return null;
        }
        // 遍历此车型下所有车，任意一辆可用即返回true
        for (Integer carId : carModelRelationMap.get(carModelId)) {
            if (isAvailableCar(vo, carId)) {
                return carId;
            }
        }
        return null;
    }

    /**
     * 查询在给定时间段内，某一辆车是否可用
     *
     * @param vo
     * @param carId
     * @return
     */
    @Override
    public boolean isAvailableCar(DurationVo vo, Integer carId) {
        LOGGER.info("vo is {}, carID is {}",vo,carId);
        // 若预定信息中不包含此车，则此车可用
        if (!carScheduleMap.containsKey(carId) || CollectionUtils.isEmpty(carScheduleMap.get(carId))) {
            return true;
        }
        Map<Integer, BitSet> map = carScheduleMap.get(carId);

        Map<Integer, BitSet> bitSetMap = convertToBitSet(vo);

        // 遍历需要预定的每一天
        for (Map.Entry<Integer, BitSet> entry : bitSetMap.entrySet()) {
            // 当天未被预定
            if (!map.containsKey(entry.getKey())) {
                continue;
            }
            // 当天全部时段不可用
            if (map.get(entry.getKey()).size() == 1) {
                return false;
            }
            // 当天有部分时段不可用，但需要预定全部，不可用
            if (entry.getValue().size() == 1) {
                return false;
            }
            // 预定区间有交集，不可用
            if (map.get(entry.getKey()).intersects(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将预定区间段转为map, key=日期 value为当日预定情况的位图
     *
     * @param durationVo
     * @return
     */
    private Map<Integer, BitSet> convertToBitSet(DurationVo durationVo) {
        Map<Integer, BitSet> map = new TreeMap<>();

        long startDay = (durationVo.getStartTime().getTime() + TIMEZONE_ADDITION)  / MILL_SEC_PER_DAY;
        int startIndex = (int) (((durationVo.getStartTime().getTime() + TIMEZONE_ADDITION) % MILL_SEC_PER_DAY) / MILL_SEC_PER_TEN_MINS);

        long endDay = (durationVo.getEndTime().getTime() + TIMEZONE_ADDITION) / MILL_SEC_PER_DAY;
        int endIndex = (int) (((durationVo.getEndTime().getTime() + TIMEZONE_ADDITION) % MILL_SEC_PER_DAY) / MILL_SEC_PER_TEN_MINS);

        // 预定区间在同一天内
        if (startDay == endDay) {
            BitSet bitSet = new BitSet(TEN_MINS_PER_DAY);
            bitSet.set(startIndex, endIndex + 1);
            map.put((int) startDay, bitSet);
            return map;
        }

        // 预定区间跨天,先将首尾两天存入map，再存剩余天
        BitSet startBitSet = new BitSet(TEN_MINS_PER_DAY);
        startBitSet.set(startIndex, TEN_MINS_PER_DAY + 1);
        map.put((int) startDay, startBitSet);

        BitSet endBitSet = new BitSet(TEN_MINS_PER_DAY);
        endBitSet.set(0, endIndex + 1);
        map.put((int) endDay, endBitSet);

        // 遍历预定区间内的整数天，BitSet只存放一位，减少内存占用
        for (long i = startDay + 1; i < endDay; i++) {
            BitSet tmp = new BitSet(1);
            tmp.set(0);
            map.put((int) i, tmp);
        }

        return map;
    }

    /**
     * 通过车型获取汽车ID
     * @param modelId
     * @return
     */
    public List<Integer> getCarIdsByModelId(int modelId){
        LOGGER.info(JSON.toJSONString(carModelRelationMap));
        return carModelRelationMap.get(modelId);
    }

    /**
     * 通过车型ID获取车型信息
     * @param modelId
     * @return
     */
    public CarModel getCarModelById(int modelId){
        return carModelMap.get(modelId);
    }
}
