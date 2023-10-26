package com.wr.controller;

import com.wr.common.service.PhoneService;
import com.wr.result.R;
import com.wr.utils.format.FormatUtils;
import com.wr.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phone")
@Api(tags = "手机号相关接口")
public class PhoneController extends BaseController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping("/login")
    @ApiOperation("短信验证码登录")
    public R getCode(@RequestParam String mobile,@RequestParam String key,@RequestParam String code){
        if (!FormatUtils.isMobile(mobile)){
            return R.fail("手机号格式不正确");
        }
        return phoneService.logReg(mobile,key,code);
    }

    @GetMapping("/getCode")
    @ApiOperation("获取短信验证码")
    public R getCode(@RequestParam String mobile){
        if (!FormatUtils.isMobile(mobile)){
            return R.fail("手机号格式不正确");
        }
        return phoneService.getCode(mobile);
    }
}
