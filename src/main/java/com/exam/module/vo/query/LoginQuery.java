package com.exam.module.vo.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录接口vo
 *
 * @author wangpeng
 */
@Data
public class LoginQuery {
    @ApiModelProperty(value = "用户名",required = true,example = "Tom",notes = "")
    private String userName;
    @ApiModelProperty(value = "密码",required = true,example = "asdf1234",notes = "")
    private String password;
}
