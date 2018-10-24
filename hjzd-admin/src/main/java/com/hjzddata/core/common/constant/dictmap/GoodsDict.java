package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 商品的映射
 */
public class GoodsDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "商品Id");
        put("goodsName", "商品名称");
        put("stockNum", "库存数量");
        put("picUrl", "商品图片");
        put("url", "商品详情跳转地址");
        put("batchNo", "商品批次");
        put("categoryNo", "商品分类");
        put("note", "备注");
        put("goodsNote", "商品备注");
        put("isSale", "上下架");
    }

    @Override
    protected void initBeWrapped() {

    }
}
