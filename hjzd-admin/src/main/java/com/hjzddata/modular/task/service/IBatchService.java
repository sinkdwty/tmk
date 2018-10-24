package com.hjzddata.modular.task.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.Batch;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据批次表 服务类
 * </p>
 *
 * @author hjzd
 * @since 2018-09-17
 */
public interface IBatchService extends IService<Batch> {

    /**
     * 根据条件查询批次列表
     */
    List<Map<String, Object>> selectBatchs(Page<Batch> page, @Param("batch_no") String batch_no, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
