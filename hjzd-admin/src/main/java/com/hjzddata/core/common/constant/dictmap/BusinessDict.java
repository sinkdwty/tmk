package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

public class BusinessDict extends AbstractDictMap {
    @Override
    public void init() {
        put("Id", "商品Id");
        put("title", "名称");
        put("companyName", "客户公司名称");
        put("phone", "手机号码");
        put("money", "金额");
        put("phase", "阶段");
        put("status", "状态");
        put("bargainProb", "成交概率");
        put("createdAt", "创建日期");
        put("updatedAt", "修改日期");
        put("endAt", "结束日期");
        put("createdIp", "创建人ip");
        put("updatedIp", "修改人ip");
    }

    @Override
    protected void initBeWrapped() {

    }
}
