package com.hjzddata.modular.task.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.datascope.DataScope;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.task.model.Product;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-02
 */
public interface IProductService extends IService<Product> {

    /**
     * 根据条件查询项目列表
     */
    List<Map<String, Object>> selectProducts(Page<OperationLog> page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("baseId") Integer baseId, String orderByField, boolean asc);


    /**
     * 通过账号获取用户
     */
    Product getByName(@Param("name") String name);

    /**
     * 修改用户状态
     */
    int setStatus(@Param("id") Integer id, @Param("status") int status);

}
