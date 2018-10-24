package com.hjzddata.modular.task.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.Batch;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据批次表 Mapper 接口
 * </p>
 *
 * @author hjzd
 * @since 2018-09-17
 */
public interface BatchMapper extends BaseMapper<Batch> {
    /**
     * 根据条件查询
     */
    List<Map<String, Object>> selectBatchs(@Param("page") Page<Batch> page, @Param("batch_no") String batch_no, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("product_id") Integer product_id);

}
