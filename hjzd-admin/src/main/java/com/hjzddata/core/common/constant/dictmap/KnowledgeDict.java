package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 知识的映射
 */
public class KnowledgeDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "知识Id");
        put("name", "知识名称");
        put("keyWord", "关键字");
        put("category", "知识分类");
        put("status", "状态");
        put("accessory", "附件");
        put("content", "知识内容");
    }

    @Override
    protected void initBeWrapped() {

    }
}
