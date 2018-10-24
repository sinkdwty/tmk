package com.hjzddata.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目配置
 *
 * @author hjzd
 * @Date 2017/5/23 22:31
 */
@Component
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

    public static final String PREFIX = "smss";

    private String uid;

    private String key;

    private String user;

    private String url;

    public String getAppend_url() {
        return append_url;
    }

    public void setAppend_url(String append_url) {
        this.append_url = append_url;
    }

    /**
     * 附加地址，目前存放查询接口地址
     */
    private String append_url;

    private String code;

    private String volume;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser () {
        return user;
    }

    public String getKey () {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }

    public String getUid() {
        return uid;
    }

    public String getVolume() {
        return volume;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
