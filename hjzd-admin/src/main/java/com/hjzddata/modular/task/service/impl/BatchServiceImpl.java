package com.hjzddata.modular.task.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.modular.task.model.Batch;
import com.hjzddata.modular.task.dao.BatchMapper;
import com.hjzddata.modular.task.service.IBatchService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据批次表 服务实现类
 * </p>
 *
 * @author hjzd
 * @since 2018-09-17
 */
@Service
public class BatchServiceImpl extends ServiceImpl<BatchMapper, Batch> implements IBatchService {

    /**
     * 根据条件查询
     */
    @Override
    public List<Map<String, Object>> selectBatchs(Page<Batch> page, String batch_no, String beginTime, String endTime) {
        if (ShiroKit.isAdmin()) {
            return this.baseMapper.selectBatchs(page, batch_no, beginTime, endTime, null);
        } else {
            Integer product_id = ShiroKit.getUser().getProductId();
            return this.baseMapper.selectBatchs(page, batch_no, beginTime, endTime, product_id);
        }
    }
}
