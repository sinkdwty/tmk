package com.hjzddata.modular.system.warpper;

import com.hjzddata.core.base.warpper.BaseControllerWarpper;
import com.hjzddata.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class CallConfigWarpper extends BaseControllerWarpper {

    public CallConfigWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("callSystemName", ConstantFactory.me().getCallSystemName((Integer) map.get("call_system_id")));
    }

}
