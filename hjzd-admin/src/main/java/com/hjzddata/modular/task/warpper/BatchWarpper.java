package com.hjzddata.modular.task.warpper;

import com.hjzddata.core.base.warpper.BaseControllerWarpper;
import com.hjzddata.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class BatchWarpper extends BaseControllerWarpper {

    public BatchWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("productName", ConstantFactory.me().getProductNameById((Integer) map.get("product_id")));
        map.put("userName", ConstantFactory.me().getUserAccountById((Integer) map.get("user_id")));
    }
}