package com.hjzddata.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.hjzddata.modular.system.model.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典服务
 *
 */
public interface IDictService extends IService<Dict> {

    /**
     * 添加字典
     */
    void addDict(String dictName, String code, String dictValues);

    /**
     *  修改某条字典信息 add by eric 2018-09-25
     */
    void updateDict(Integer dictId ,String dictName, String code, String dicts);

    /**
     * 编辑字典
     */
    void editDict(Integer dictId, String dictName, String code, String dicts);

    /**
     * 删除字典
     */
    void delteDict(Integer dictId);

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

    /**
     * 根据父类id获取词典列表
     */
    List<Dict> selectByPid(Integer value);

    List<Map<String, Object>> getDict(Page<Dict> page, String condition);

    /**
     * 根据编码获取词典列表
     */
    List<Dict> selectAllByCode(@Param("product_id") Integer product_id, @Param("level") Integer level, @Param("code") String code);
}
