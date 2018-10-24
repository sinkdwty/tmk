package com.hjzddata.modular.task.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.Policy;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-05
 */
public interface PolicyMapper extends BaseMapper<Policy> {
    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectPolicies(@Param("page") Page<Policy> page, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 通过账号获取用户
     */
    Policy getByName(@Param("name") String name);

    /**
     * 修改用户状态
     */
    int setStatus(@Param("policyId") Integer productId, @Param("status") int status);

    /* 查询是否有相同的策略 */
    List<Map> policyExistence(@Param("policyName") String policyName,@Param("id") Integer id);
}
