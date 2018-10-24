package com.hjzddata.core.util;

import com.hjzddata.config.properties.HjzdProperties;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(HjzdProperties.class).getKaptchaOpen();
    }
}