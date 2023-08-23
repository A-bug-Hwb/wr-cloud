package com.wr.common.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.wr.common.constants.PhoneConstants;
import com.wr.exception.ServiceException;

public class SMSUtils {

    /**
     * 发送短信
     * @param phoneNumbers 手机号
     * @param code 参数
     */
    public static void sendMessage(String phoneNumbers,String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", PhoneConstants.ACCESS_ID, PhoneConstants.ACCESS_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSysRegionId("cn-hangzhou");
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(PhoneConstants.SING_NAME);
        request.setTemplateCode(PhoneConstants.TEMPLATE_CODE);
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
        }catch (ClientException e) {
            throw new ServiceException("短信发送失败");
        }
    }
}
