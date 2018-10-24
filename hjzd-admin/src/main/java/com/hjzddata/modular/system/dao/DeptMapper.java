package com.hjzddata.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hjzddata.modular.system.model.Dept;
import com.hjzddata.core.node.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 *
 * @since 2017-07-11
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree(@Param("deptId") Integer deptId);

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);

    Dept selectByName(@Param("name") String name, @Param("id") Integer id);
}