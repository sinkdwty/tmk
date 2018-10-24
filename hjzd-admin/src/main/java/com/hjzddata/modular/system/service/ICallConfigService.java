package com.hjzddata.modular.system.service;

import com.hjzddata.modular.system.model.CallConfig;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-17
 */
public interface ICallConfigService extends IService<CallConfig> {
    List<Map<String, Object>> list();

    CallConfig getcallConfig(Integer call_system, Integer baseId);
}
