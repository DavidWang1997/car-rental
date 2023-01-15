package com.exam.module.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户vo
 *
 * @author wangpeng
 */
@ApiModel(value="UserVo",description = "用户注册信息")
@Data
public class UserVo {
    @ApiModelProperty(value = "用户名",required = true,example = "Tom",notes = "")
    private String userName;
    @ApiModelProperty(value = "密码",required = true,example = "asdf1234",notes = "")
    private String password;
    @ApiModelProperty(value = "电话号码",required = false,example = "000-0000-0000",notes = "")
    private String phone;
    @ApiModelProperty(value = "电子邮箱",required = false,example = "000000000@qq.com",notes = "")
    private String email;
}
