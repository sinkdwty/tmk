package com.hjzddata.modular.system.service.impl;

import com.hjzddata.modular.system.model.CallConfig;
import com.hjzddata.modular.system.dao.CallConfigMapper;
import com.hjzddata.modular.system.service.ICallConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-17
 */
@Service
public class CallConfigServiceImpl extends ServiceImpl<CallConfigMapper, CallConfig> implements ICallConfigService {

    @Override
    public List<Map<String, Object>> list() {
        return this.baseMapper.list();
    }

    @Override
    public CallConfig getcallConfig(Integer call_system, Integer baseId) { return this.baseMapper.getcallConfig(call_system, baseId);};
}
