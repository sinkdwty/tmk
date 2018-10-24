package com.hjzddata.modular.task.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.CallLog;
import com.baomidou.mybatisplus.service.IService;
import java.util.List;

/**
 * <p>
 * 呼叫日志表 服务类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-18
 */
public interface ICallLogService extends IService<CallLog> {

    List selectCallList(Page<CallLog> page, Integer caseId,String column, String condition, String beginTime, String endTime,String checkStatus,Integer call_status_id, String orderByField, boolean asc);

    String getCallConfig(Integer userId);

    List selectLastLogBycase(Integer caseId);
}
