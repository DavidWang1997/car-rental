package com.exam.dao.mapper;

import com.exam.module.entity.CarModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mybatis CarModelMapper
 *
 * @author wangpeng
 */
@Mapper
public interface CarModelMapper {
    CarModel selectCarModelByCarModelId(int carModelId);

    List<CarModel> selectCarModels();
}
