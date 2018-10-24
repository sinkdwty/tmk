package com.hjzddata.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.modular.system.model.HjSmsBatch;
import com.hjzddata.modular.system.dao.HjSmsBatchMapper;
import com.hjzddata.modular.system.service.IHjSmsBatchService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjzd
 * @since 2018-09-27
 */
@Service
public class HjSmsBatchServiceImpl extends ServiceImpl<HjSmsBatchMapper, HjSmsBatch> implements IHjSmsBatchService {

    @Override
    public List<Map<String, Object>> selectHjSmsBatchList(Page<HjSmsBatch> page, Integer userid, String column,
                                                          String condition, String beginTime, String endTime,
                                                          Integer status,String way, String orderByField, boolean isAsc) {

        if (ShiroKit.hasRole("worker")) {
            userid = ShiroKit.getUser().getId();
        }
        if (userid != null && userid > 0) {
            return this.baseMapper.selectHjSmsBatchList(page, userid, column, condition, beginTime, endTime, status, way, orderByField, isAsc);
        }else
            return this.baseMapper.selectHjSmsBatchList(page, -1, column, condition, beginTime, endTime, status, way, orderByField, isAsc);
    }
}
