package com.wr.controller;

import com.wr.common.service.PhoneService;
import com.wr.common.utils.SMSUtils;
import com.wr.exception.ServiceException;
import com.wr.result.AjaxResult;
import com.wr.result.R;
import com.wr.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/phone")
@Api(tags = "手机号相关接口")
public class PhoneController extends BaseController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping("/login")
    @ApiOperation("短信验证码登录")
    public R getCode(@RequestParam String phone,@RequestParam String key,@RequestParam String code){
        isPhone(phone);
        return phoneService.logReg(phone,key,code);
    }

    @GetMapping("/getCode")
    @ApiOperation("获取短信验证码")
    public R getCode(@RequestParam String phoneNumbers){
        isPhone(phoneNumbers);
        return phoneService.getCode(phoneNumbers);
    }

    private void isPhone(String phone){
        String regex = "^1[3456789]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()){
            throw new ServiceException("手机号格式不正确");
        }
    }
}
