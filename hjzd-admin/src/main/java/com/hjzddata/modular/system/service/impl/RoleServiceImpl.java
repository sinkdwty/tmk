package com.hjzddata.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjzddata.modular.system.dao.RelationMapper;
import com.hjzddata.modular.system.dao.RoleMapper;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.system.model.Relation;
import com.hjzddata.modular.system.model.Role;
import com.hjzddata.modular.system.service.IRoleService;
import com.hjzddata.core.node.ZTreeNode;
import com.hjzddata.core.util.Convert;
import com.hjzddata.modular.system.dao.RelationMapper;
import com.hjzddata.modular.system.dao.RoleMapper;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.system.model.Relation;
import com.hjzddata.modular.system.model.Role;
import com.hjzddata.modular.system.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RelationMapper relationMapper;

    @Override
    @Transactional(readOnly = false)
    public void setAuthority(Integer roleId, String ids) {

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Convert.toLongArray(true, Convert.toStrArray(",", ids))) {
            Relation relation = new Relation();
            relation.setRoleid(roleId);
            relation.setMenuid(id);
            this.relationMapper.insert(relation);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delRoleById(Integer roleId) {
        //删除角色
        this.roleMapper.deleteById(roleId);

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);
    }

    @Override
    public List<Map<String, Object>> selectRoles(Page<OperationLog> page, String condition, String orderByField, boolean asc) {
        return this.baseMapper.selectRoles(page,condition, orderByField, asc);
    }

    @Override
    public int deleteRolesById(Integer roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    @Override
    public List<ZTreeNode> roleTreeList() {
        return this.baseMapper.roleTreeList();
    }

    @Override
    public List<ZTreeNode> roleTreeListByRoleId(String[] roleId) {
        return this.baseMapper.roleTreeListByRoleId(roleId);
    }

    @Override
    public Role selectRoleByName(String name, String tips) {
        return this.baseMapper.selectRoleByName(name , tips);
    }

}
