package com.exam.module.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预定时间段vo
 *
 * @author wangpeng
 */
@ApiModel(value="DurationVo",description = "时间段")
@Data
public class DurationVo {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预定开始时间",required = true,example = "2023-01-25 13:00:10",notes = "预定开始时间应大于当前时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预定结束时间",required = true,example = "2023-01-26 13:00:10",notes = "预定开始时间应大于预定开始时间")
    private Date endTime;
}
