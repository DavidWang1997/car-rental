package com.exam.service.cache;

import com.exam.module.entity.CarModel;
import com.exam.module.entity.Order;
import com.exam.module.vo.DurationVo;

import java.util.List;

/**
 * 汽车可用时间段信息缓存
 *
 * @author wangpeng
 */
public interface CacheManagerService {
    // 新增汽车预定信息
    public void addCarReserveInfo(DurationVo vo, Integer carId);
    // 新增汽车预定信息
    public void addCarReserveInfo(Order order);
    // 取消汽车预定信息
    public void cancelCarReserveInfo(DurationVo vo, Integer carId);
    // 取消汽车预定信息
    public void cancelCarReserveInfo(Order order);

    // 查询某种车型下在某个时间段内是否有可用车
    public Integer isAvailableCarModel(DurationVo vo, Integer carModelId);

    // 查询在给定时间段内，某一辆车是否可用
    public boolean isAvailableCar(DurationVo vo, Integer carId);

    // 获取给定时间段内所有可用车型
    public List<CarModel> getAvailableCarModels(DurationVo vo);
    // 通过车型获取汽车ID
    public List<Integer> getCarIdsByModelId(int modelId);
    // 通过车型ID获取车型信息
    public CarModel getCarModelById(int modelId);
}
