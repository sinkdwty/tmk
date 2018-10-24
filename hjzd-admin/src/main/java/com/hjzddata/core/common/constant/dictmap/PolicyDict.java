package com.hjzddata.core.common.constant.dictmap;

import com.hjzddata.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 策略的映射
 */
public class PolicyDict extends AbstractDictMap {

    @Override
    public void init() {
        put("id", "策略id");
        put("policyId", "策略id");
        put("policyName", "策略名");
        put("policyKey", "策略处理");
        put("note", "备注");
        put("status", "状态 1:-可用  0-禁用");
    }

    @Override
    protected void initBeWrapped() {

    }
}
