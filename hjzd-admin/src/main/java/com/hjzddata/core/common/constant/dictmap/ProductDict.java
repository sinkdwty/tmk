package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 项目的映射
 */
public class ProductDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "项目Id");
        put("productId", "项目Id");
        put("name", "项目名称");
        put("status", "项目状态");
        put("baseId", "基地id");
    }

    @Override
    protected void initBeWrapped() {

    }
}
