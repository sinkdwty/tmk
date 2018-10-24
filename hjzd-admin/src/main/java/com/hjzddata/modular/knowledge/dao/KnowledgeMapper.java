package com.hjzddata.modular.knowledge.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.knowledge.model.Knowledge;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 知识表 Mapper 接口
 * </p>
 *
 * @author hjzd
 * @since 2018-07-10
 */
public interface KnowledgeMapper extends BaseMapper<Knowledge> {

    List<Map<String, Object>> selectKnowledges(@Param("page") Page<Knowledge> page, @Param("key_word") String key_word,
                                               @Param("column") String column,@Param("category") Integer category);

    /* 判断是否存在相同的知识名称 */
    List<Map> knowledgeExistence(@Param("name") String name,@Param("id") Integer id);

}
