package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;


/**
 * 商品的映射
 */
public class CustomDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "客户Id");
        put("case_id", "客户Id");
        put("customName", "客户姓名");
        put("phone", "手机号码");
        put("email", "邮箱");
        put("company", "客户公司");
        put("address", "联系地址");
        put("principal", "负责人");
        put("label", "标签");
        put("customSource", "客户来源");
        put("note", "客户备注");
    }

    @Override
    protected void initBeWrapped() {

    }
}
