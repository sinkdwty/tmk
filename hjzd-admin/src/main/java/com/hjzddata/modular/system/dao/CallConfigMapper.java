package com.hjzddata.modular.system.dao;

import com.hjzddata.modular.system.model.CallConfig;
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
 * @since 2018-07-17
 */
public interface CallConfigMapper extends BaseMapper<CallConfig> {
    /**
     * 获取列表
     */
    List<Map<String, Object>> list();

    CallConfig getcallConfig(@Param("call_system") Integer call_system, @Param("baseId") Integer baseId);
}
