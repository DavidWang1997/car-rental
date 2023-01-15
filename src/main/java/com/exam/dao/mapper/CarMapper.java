package com.exam.dao.mapper;

import com.exam.module.entity.Car;
import com.exam.module.entity.CarModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
/**
 * Mybatis CarMapper
 *
 * @author wangpeng
 */
@Mapper
public interface CarMapper {
    List<Car> selectCars();
    List<Car> selectAvailableCarByTime(Date startTime, Date endTime);

    List<CarModel> selectAvailableCarModelByTime(Date startTime, Date endTime);
    List<Integer> selectAvailableCarIdByTimeAndModel(Integer modelId, Date startTime, Date endTime);
}
