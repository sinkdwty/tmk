package com.hjzddata.modular.task.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.datascope.DataScope;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.task.model.Product;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-02
 */
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectProducts(@Param("page") Page<OperationLog> page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("baseId") Integer baseId, @Param("product_id") Integer product_id, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    /**
     * 通过账号获取用户
     */
    Product getByName(@Param("name") String name);

    /**
     * 修改用户状态
     */
    int setStatus(@Param("id") Integer id, @Param("status") int status);
}
