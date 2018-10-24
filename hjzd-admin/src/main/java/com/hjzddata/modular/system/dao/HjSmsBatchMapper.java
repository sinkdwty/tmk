package com.hjzddata.modular.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.system.model.HjSmsBatch;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjzd
 * @since 2018-09-27
 */
public interface HjSmsBatchMapper extends BaseMapper<HjSmsBatch> {

    List<Map<String,Object>> selectHjSmsBatchList(@Param("page") Page<HjSmsBatch> page, @Param("userid") Integer userid, @Param("column") String column,
                                              @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime,
                                              @Param("status") Integer status,@Param("way") String way,@Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc
    );


}
