package com.exam.controller;

import com.exam.controller.result.Response;
import com.exam.module.entity.CarModel;
import com.exam.module.vo.DurationVo;
import com.exam.service.CarService;
import com.exam.util.ParamCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 汽车相关CarController
 *
 * @author wangpeng
 */
@Api(tags = "CarController")
@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @ApiOperation(value = "查询指定时间段内的可用车型信息")
    @PostMapping("/query")
    @ResponseBody
    @ApiResponses({
            @ApiResponse(code = 1001, message = "参数校验不通过"),
    })
    public Response query(@RequestBody DurationVo durationVo) {

        if (!ParamCheckUtil.checkNotNull(durationVo)) {
            return Response.paramCheckFail();
        }

        List<CarModel> carModels = carService.query(durationVo);
        return Response.success(carModels);
    }


}
