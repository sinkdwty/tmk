package com.hjzddata.modular.task.warpper;

import com.hjzddata.core.base.warpper.BaseControllerWarpper;

import java.util.List;
import java.util.Map;
public class PolicyWarpper  extends BaseControllerWarpper {

    public PolicyWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {}
}