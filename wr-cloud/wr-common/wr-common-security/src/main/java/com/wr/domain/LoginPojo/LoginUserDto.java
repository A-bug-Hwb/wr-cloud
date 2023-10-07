package com.wr.domain.LoginPojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("用户登录")
public class LoginUserDto {


    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;
    @ApiModelProperty(value = "验证码uuid",required = true)
    @NotBlank(message = "验证码uuid不能为空")
    private String uuid;
    @ApiModelProperty(value = "加密秘钥id",required = true)
    private String rsaKeyId;
}
