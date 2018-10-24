package com.hjzddata.modular.task.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.CallLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 呼叫日志表 Mapper 接口
 * </p>
 *
 * @author hjzd
 * @since 2018-07-18
 */
public interface CallLogMapper extends BaseMapper<CallLog> {

    List<Map<String,Object>> selectCallList(@Param("page") Page<CallLog> page, @Param("user_id") List<Integer> user_id, @Param("caseId") Integer caseId,
                                            @Param("column") String column, @Param("condition") String condition,
                                            @Param("beginTime") String beginTime, @Param("endTime") String endTime,
                                            @Param("checkStatus") String checkStatus,@Param("call_status_id") Integer call_status_id,
                                            @Param("orderByField") String orderByField,
                                            @Param("isAsc") boolean isAsc
    );

    String getCallConfig(@Param("userId") Integer userId);

    List selectLastLogBycase(@Param("caseId") Integer caseId);
}
