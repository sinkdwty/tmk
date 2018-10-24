package com.hjzddata.modular.task.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.mutidatasource.annotion.DataSource;
import com.hjzddata.modular.task.model.Speechcraft;
import com.hjzddata.modular.task.dao.SpeechcraftMapper;
import com.hjzddata.modular.task.service.ISpeechcraftService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 话术表 服务实现类
 * </p>
 *
 * @author hjzd123
 * @since 2018-07-10
 */
@Service
public class SpeechcraftServiceImpl extends ServiceImpl<SpeechcraftMapper, Speechcraft> implements ISpeechcraftService {
    /**
     * 根据条件查询项目列表
     */
    @Override
    @DataSource(name="bizDataSource")
    public List<Map<String, Object>> selectSpeechs(Page<Speechcraft> page, String name, String beginTime, String endTime, String orderByField, boolean asc) {
        return this.baseMapper.selectSpeechs(page, name, beginTime, endTime, orderByField, asc);
    }

    @Override
    public List<Map> selectList() {
        return this.baseMapper.selectList();
    }

    /**
     * 根据项目名称获取项目数据
     * @param name
     * @return
     */
    @Override
    public Speechcraft getByName(String name) {
        return this.baseMapper.getByName(name);
    }

    /**
     * 设置项目状态
     * @param speechId
     * @param status
     * @return
     */
    @Override
    public int setStatus(Integer speechId, int status) {
        return this.baseMapper.setStatus(speechId, status);
    }

}
