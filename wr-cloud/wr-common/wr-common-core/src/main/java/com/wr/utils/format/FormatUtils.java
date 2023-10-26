package com.wr.utils.format;

import com.wr.exception.ServiceException;
import com.wr.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {

    private static String MOBILE_REGEX = "^1[3456789]\\d{9}$";

    private static String MAILBOX_REGEX = "[a-zA-Z0-9]+[\\.]{0,1}[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+";

    //校验手机号格式
    public static boolean isMobile(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        Pattern pattern = Pattern.compile(MOBILE_REGEX);
        return pattern.matcher(phone).matches();
    }

    //校验邮箱格式
    public static boolean isMailbox(String mailbox) {
        if (StringUtils.isEmpty(mailbox)) {
            return false;
        }
        Pattern pattern = Pattern.compile(MAILBOX_REGEX);
        return pattern.matcher(mailbox).matches();
    }
}
