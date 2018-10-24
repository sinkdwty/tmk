package com.hjzddata.modular.task.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.hjzddata.modular.task.model.Media;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 媒体表 服务类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-05
 */
public interface IMediaService extends IService<Media> {

    /**
     * 获取媒体列表
     * @param name
     * @param category
     * @return
     */
    List<Map<String, Object>> selectMedias(@Param("page") Page<Media> page, @Param("name") String name, @Param("category") Integer category);
}
