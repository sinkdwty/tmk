package com.hjzddata.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjzddata.modular.system.dao.UserMapper;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IUserService;
import com.hjzddata.core.datascope.DataScope;
import com.hjzddata.core.mutidatasource.annotion.DataSource;
import com.hjzddata.modular.system.dao.UserMapper;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * 123
 * @since 2018-02-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public int setStatus(Integer userId, int status) {
        return this.baseMapper.setStatus(userId, status);
    }

    @Override
    public int changePwd(Integer userId, String pwd) {
        return this.baseMapper.changePwd(userId, pwd);
    }

    @Override
    @DataSource(name="bizDataSource")
    public List<Map<String, Object>> selectUsers(Page<User> page, DataScope dataScope, String name, String beginTime, String endTime, Integer deptid, String orderByField, boolean asc) {
        return this.baseMapper.selectUsers(page,dataScope, name, beginTime, endTime, deptid, orderByField, asc);
    }

    @Override
    public int setRoles(Integer userId, String roleIds) {
        return this.baseMapper.setRoles(userId, roleIds);
    }

    @Override
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }
}
