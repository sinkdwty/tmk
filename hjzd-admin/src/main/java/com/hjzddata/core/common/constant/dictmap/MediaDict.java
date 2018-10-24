package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 媒体的映射
 */
public class MediaDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "媒体Id");
        put("name", "媒体名称");
        put("pic", "媒体图片");
        put("url", "媒体链接");
        put("category", "媒体分类");
        put("introduce", "媒体简介");
        put("tags", "媒体标签");
    }

    @Override
    protected void initBeWrapped() {
//        putFieldWrapperMethodName("id","getMediaName");
    }
}
