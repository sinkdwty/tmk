package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 外呼系统的映射
 */
public class CallConfigDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "配置Id");
        put("baseName", "基地名称");
        put("callSystemId", "外呼系统id");
        put("baseId", "基地id");
        put("config", "配置内容");
    }

    @Override
    protected void initBeWrapped() {

    }
}
