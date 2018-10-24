package com.hjzddata.modular.task.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjzddata.modular.task.dao.MediaMapper;
import com.hjzddata.modular.task.model.Media;
import com.hjzddata.modular.task.service.IMediaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 媒体表 服务实现类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-05
 */
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements IMediaService {

    @Override
    public List<Map<String, Object>> selectMedias(Page<Media> page, String name, Integer category) {
        return this.baseMapper.selectMedias(page, name, category);
    }
}
