package com.hjzddata.modular.task.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.Speechcraft;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 话术表 Mapper 接口
 * </p>
 *
 * @author hjzd123
 * @since 2018-07-10
 */
public interface SpeechcraftMapper extends BaseMapper<Speechcraft> {
    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectSpeechs(@Param("page") Page<Speechcraft> page, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 获取ztree的节点列表
     */
    List<Map> selectList();

    /**
     * 通过账号获取用户
     */
    Speechcraft getByName(@Param("name") String name);

    /**
     * 修改用户状态
     */
    int setStatus(@Param("speechId") Integer speechId, @Param("status") int status);
}
