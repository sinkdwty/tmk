package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 通话日志的字典
 */
public class CallLogDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id","通话日志id");
        put("caseId","案件id");
        put("customName","客户姓名");
        put("customPhone","客户手机号码");
        put("userId","案件所属坐席id");
        put("userName","坐席姓名");
        put("note","备注");
    }

    @Override
    protected void initBeWrapped() {

    }
}
