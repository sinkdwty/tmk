package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 短信模板的映射
 */
public class MessageDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "短信模板id");
        put("messageId", "短信模板id");
        put("message", "短信内容");
        put("note", "备注");
        put("status", "状态 1:-可用  0-禁用");
    }

    @Override
    protected void initBeWrapped() {

    }
}
