package com.hjzddata.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.hjzddata.modular.system.model.OperationLog;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 操作日志 服务类
 * </p>
 *
 * 123
 * @since 2018-02-22
 */
public interface IOperationLogService extends IService<OperationLog> {

    /**
     * 获取操作日志列表
     */
    List<Map<String, Object>> getOperationLogs(Page<OperationLog> page, String userName, Integer customId,String beginTime, String endTime, String logName, String s, String orderByField, boolean asc);
}
