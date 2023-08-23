package com.wr.common.constants;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeChatConstants implements InitializingBean {

    @Value("${config.wx.appid}")
    private String appId;

    @Value("${config.wx.appsecret}")
    private String appSecret;

    @Value("${config.wx.redirecturl}")
    private String redirectUrl;
    public static String WX_APP_ID;
    public static String WX_APP_SECRET;
    public static String WX_REDIRECT_URL;
    @Override
    public void afterPropertiesSet() throws Exception {
        WX_APP_ID = appId;
        WX_APP_SECRET = appSecret;
        WX_REDIRECT_URL = redirectUrl;
    }

    // 首先是这个TOKENURL 用于获取微信用户的授权认证 来拿到“accessToken”
    public final static String WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    // 第二是用于请求获取用户手机号的地址（结尾的“access_token”需要拼接上TOKENURL返回的参数）
    public final static String WX_PHONEN_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";
}
