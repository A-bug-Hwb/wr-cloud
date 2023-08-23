package com.wr.domain.LoginPojo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("用户登录")
public class LoginUserDto {


    private String userName;
    private String password;
    private String code;
    private String uuid;
}
