package com.hjzddata.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.system.model.HjSmsBatch;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjzd
 * @since 2018-09-27
 */
public interface IHjSmsBatchService extends IService<HjSmsBatch> {


    List<Map<String,Object>> selectHjSmsBatchList(Page<HjSmsBatch> page, Integer userid, String column,
                                                         String condition, String beginTime, String endTime,
                                                         Integer status, String way, String orderByField, boolean isAsc);

}
