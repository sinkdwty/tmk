package com.hjzddata.modular.task.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.mutidatasource.annotion.DataSource;
import com.hjzddata.modular.task.model.Policy;
import com.hjzddata.modular.task.dao.PolicyMapper;
import com.hjzddata.modular.task.service.IPolicyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-05
 */
@Service
public class PolicyServiceImpl extends ServiceImpl<PolicyMapper, Policy> implements IPolicyService {
    /**
     * 根据条件查询项目列表
     */
    @Override
    @DataSource(name="bizDataSource")
    public List<Map<String, Object>> selectPolicies(Page<Policy> page, String name, String beginTime, String endTime, String orderByField, boolean asc) {
        return this.baseMapper.selectPolicies(page, name, beginTime, endTime, orderByField, asc);
    }

    /**
     * 根据项目名称获取项目数据
     * @param name
     * @return
     */
    @Override
    public Policy getByName(String name) {
        return this.baseMapper.getByName(name);
    }


    /**
     * 设置项目状态
     * @param policyId
     * @param status
     * @return
     */
    @Override
    public int setStatus(Integer policyId, int status) {
        return this.baseMapper.setStatus(policyId, status);
    }

    /* 查询是否有相同的策略 */
    @Override
    public List<Map> policyExistence(String policyName ,Integer id) {
        return this.baseMapper.policyExistence(policyName,id);
    }
}
