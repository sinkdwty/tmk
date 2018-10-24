package com.hjzddata.modular.task.warpper;

import com.hjzddata.core.base.warpper.BaseControllerWarpper;
import com.hjzddata.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class ProductWarpper extends BaseControllerWarpper {

    public ProductWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("deptName", ConstantFactory.me().getDeptName((Integer) map.get("base_id")));
    }
}