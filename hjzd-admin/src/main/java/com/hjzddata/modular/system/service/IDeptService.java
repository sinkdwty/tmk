package com.hjzddata.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.hjzddata.modular.system.model.Dept;
import com.hjzddata.core.node.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门服务
 *
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 删除部门
     */
    void deleteDept(Integer deptId);

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);

    Dept selectByName(@Param("name") String name, @Param("id") Integer id);
}
