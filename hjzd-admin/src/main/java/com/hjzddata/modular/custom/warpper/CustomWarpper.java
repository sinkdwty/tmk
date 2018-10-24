package com.hjzddata.modular.custom.warpper;

import com.hjzddata.core.base.warpper.BaseControllerWarpper;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import java.util.List;
import java.util.Map;

public class CustomWarpper extends BaseControllerWarpper {

    public CustomWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("customStatus", ConstantFactory.me().getCustomStatus((Integer) map.get("status")));
        map.put("username", ConstantFactory.me().getUserNameById((Integer) map.get("user_id")));
    }
}
