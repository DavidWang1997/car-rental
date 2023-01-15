package com.exam.service;

import com.exam.dao.mapper.CarMapper;
import com.exam.module.entity.CarModel;
import com.exam.module.vo.DurationVo;
import com.exam.service.cache.CacheManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 汽车相关Service
 *
 * @author wangpeng
 */
@Service("carService")
public class CarService {
    @Resource
    private CarMapper carMapper;

    @Resource
    private CacheManagerService cacheManagerService;

    public List<CarModel> query(DurationVo durationVo) {
        List<CarModel> carModels = cacheManagerService.getAvailableCarModels(durationVo);
        return carModels;
    }

}
