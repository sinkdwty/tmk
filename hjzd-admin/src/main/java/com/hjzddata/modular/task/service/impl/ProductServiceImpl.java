package com.hjzddata.modular.task.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.datascope.DataScope;
import com.hjzddata.core.mutidatasource.annotion.DataSource;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.task.model.Product;
import com.hjzddata.modular.task.dao.ProductMapper;
import com.hjzddata.modular.task.service.IProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-02
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {


    /**
     * 根据条件查询项目列表
     */
    @Override
    @DataSource(name="bizDataSource")
    public List<Map<String, Object>> selectProducts(Page<OperationLog> page, DataScope dataScope, String name, String beginTime, String endTime, Integer baseId, String orderByField, boolean asc) {
        Integer product_id = 0;
        if (!ShiroKit.isAdmin()) {
            product_id = ShiroKit.getUser().getProductId();
        }
        return this.baseMapper.selectProducts(page,dataScope, name, beginTime, endTime, baseId, product_id,orderByField, asc);
    }

    /**
     * 根据项目名称获取项目数据
     * @param name
     * @return
     */
    @Override
    public Product getByName(String name) {
        return this.baseMapper.getByName(name);
    }

    /**
     * 设置项目状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public int setStatus(Integer id, int status) {
        return this.baseMapper.setStatus(id, status);
    }

}
