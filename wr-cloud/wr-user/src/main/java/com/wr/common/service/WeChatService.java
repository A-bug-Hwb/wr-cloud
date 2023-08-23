package com.wr.common.service;

import com.alibaba.fastjson2.JSONObject;
import com.wr.common.constants.WeChatConstants;
import com.wr.exception.ServiceException;
import com.wr.result.R;
import com.wr.utils.HttpUtil;
import com.wr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeChatService {

    @Autowired
    private PhoneService phoneService;

    public R letsLogin(String code) {
        // 先拿到前端通过wx.login()拿到的code -- 5分钟过期哦
        if (StringUtils.isEmpty(code)) {
            return R.fail(101, "code不可以为空！");
        }
        // 获取手机号 -- 这边需要定义一个传参的map
        Map<String, String> params = new HashMap<>();
        // 此处为固定值不需要修改
        params.put("grant_type", "client_credential");
        // APP_ID和APP_SERCRET需要根据实际情况进行传参
        params.put("appid", WeChatConstants.WX_APP_ID);
        params.put("secret", WeChatConstants.WX_APP_SECRET);
        // 调用TOKENURL获取授权凭证access_token
        String getRes = HttpUtil.sendGet(WeChatConstants.WX_TOKEN_URL, params);
        JSONObject tokenJson = JSONObject.parseObject(getRes);
        String accessToken = (String) tokenJson.get("access_token");
        // 使用前端code获取手机号码（accessToken一定要以get的方式请求）其他参数为json格式
        String phoneUrl = WeChatConstants.WX_PHONEN_URL + accessToken;
        JSONObject body = new JSONObject();
        body.put("code",code);
        String phone = "";
        try{
            String res = HttpUtil.sendPost(phoneUrl,body);
            JSONObject jsonRes = JSONObject.parseObject(res);
            JSONObject phoneInfo = JSONObject.parseObject(JSONObject.toJSONString(jsonRes.get("phone_info")));
            phone = (String) phoneInfo.get("phoneNumber");
        }catch (Exception e){
            throw new ServiceException("获取手机号失败，请检查配置信息");
        }
        //进入手机号登录
        return phoneService.logReg(phone);
    }
}
