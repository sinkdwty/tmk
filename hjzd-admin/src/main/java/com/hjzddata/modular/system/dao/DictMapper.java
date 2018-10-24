package com.hjzddata.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.system.model.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 *
 * @since 2017-07-11
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 根据编码获取词典列表
     */
    List<Dict> selectByCode(@Param("code") String code);

    /**
     * 查询字典列表
     */
    List<Map<String, Object>> list(@Param("condition") String conditiion);

    /**
     * 根据父类编码获取词典列表
     */
    List<Dict> selectByParentCode(@Param("code") String code);

    List<Dict> selectByPid(@Param("value") Integer value);

    List<Map<String, Object>> getDict(@Param("page") Page<Dict> page, @Param("condition") String condition);

    /**
     * 根据编码获取词典列表
     */
    List<Dict> selectAllByCode(@Param("product_id") Integer product_id, @Param("level") Integer level, @Param("code") String code);
}