package com.hjzddata.modular.knowledge.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.knowledge.model.Knowledge;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 知识表 服务类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-10
 */
public interface IKnowledgeService extends IService<Knowledge> {

    List<Map<String, Object>> selectKnowledges(@Param("page") Page<Knowledge> page, @Param("key_word") String key_word,
                                               @Param("column") String column,@Param("category") Integer category);
    /* 判断是否存在相同的短信模板 */
    List<Map> knowledgeExistence(@Param("name") String name,@Param("id") Integer id);
}
