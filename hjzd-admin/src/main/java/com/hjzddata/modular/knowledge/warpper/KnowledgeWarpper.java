package com.hjzddata.modular.knowledge.warpper;

import com.hjzddata.core.base.warpper.BaseControllerWarpper;
import com.hjzddata.core.common.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;


public class KnowledgeWarpper extends BaseControllerWarpper {

    public KnowledgeWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("knowledgeCategoryName", ConstantFactory.me().getCategoryKnowledgeCategoryName((Integer) map.get("category")));
    }

}
