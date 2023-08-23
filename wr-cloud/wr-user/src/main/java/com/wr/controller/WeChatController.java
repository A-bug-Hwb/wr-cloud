package com.wr.controller;

import com.wr.common.service.WeChatService;
import com.wr.common.constants.WeChatConstants;
import com.wr.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Api(tags = "微信登录接口")
@RestController
@RequestMapping("/weChat")
public class WeChatController {


    @Autowired
    private WeChatService weChatService;

    @ApiOperation("微信小程序手机号登录")
    @GetMapping("/login")
    public R WeChatLogin(@RequestParam String code) {
        // 用户存在，返回用户数据
        return weChatService.letsLogin(code);
    }


    @ApiOperation(value = "获取微信二维码")
    @GetMapping("/getWxCode")
    public R getWxCode() {
        //微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUrl = WeChatConstants.WX_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String qrcodeUrl = String.format(
                baseUrl,
                WeChatConstants.WX_APP_ID,
                redirectUrl,
                //这里的onlineEdu 微信建议第三方带上这个参数 可随机为任何
                "onlineEdu");
        return R.ok(qrcodeUrl);
    }

    @ApiOperation(value = "回调地址")
    @GetMapping("/callBack")
    public R callBack(@RequestParam("code") String code, @RequestParam("state") String state) throws Exception {
        System.out.println(code);
        // 用户存在，返回用户数据
        return weChatService.letsLogin(code);
    }
}
