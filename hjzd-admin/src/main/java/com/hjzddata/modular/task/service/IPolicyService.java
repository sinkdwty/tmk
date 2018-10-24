package com.hjzddata.modular.task.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.Policy;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-05
 */
public interface IPolicyService extends IService<Policy> {
    /**
     * 根据条件查询项目列表
     */
    List<Map<String, Object>> selectPolicies(Page<Policy> page, String name, String beginTime, String endTime, String orderByField, boolean asc);

    /**
     * 通过账号获取用户
     */
    Policy getByName(@Param("name") String name);

    /**
     * 修改用户状态
     */
    int setStatus(@Param("policyId") Integer productId, @Param("status") int status);

    /* 查询是否有相同的策略 */
    List<Map> policyExistence(@Param("policyName") String policyName, Integer id);
}
