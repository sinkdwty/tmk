package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 话术的映射
 */
public class SpeechcraftDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "话术id");
        put("speechId", "话术id");
        put("contents", "话术内容");
        put("note", "备注");
        put("status", "状态 1:-可用  0-禁用");
    }

    @Override
    protected void initBeWrapped() {

    }
}
