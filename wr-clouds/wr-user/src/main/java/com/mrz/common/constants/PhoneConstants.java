package com.wr.common.constants;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PhoneConstants implements InitializingBean {

    @Value("${config.ali.sms.accessid}")
    private String accessId;

    @Value("${config.ali.sms.accesssecret}")
    private String accessSecret;

    @Value("${config.ali.sms.templatecode}")
    private String templateCode;


    public static String ACCESS_ID;
    public static String ACCESS_SECRET;
    public static String TEMPLATE_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_ID = accessId;
        ACCESS_SECRET = accessSecret;
        TEMPLATE_CODE = templateCode;
    }

    public final static String SING_NAME = "麦瑞哲低碳科技有限公司";

}
