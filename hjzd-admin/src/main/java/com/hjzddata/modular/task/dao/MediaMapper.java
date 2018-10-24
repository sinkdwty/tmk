package com.hjzddata.modular.task.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.task.model.Media;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 媒体表 Mapper 接口
 * </p>
 *
 * @author hjzd
 * @since 2018-07-05
 */
public interface MediaMapper extends BaseMapper<Media> {

    /**
     * 获取媒体列表
     * @param page
     * @param category
     * @return
     */
    List<Map<String, Object>> selectMedias(@Param("page") Page<Media> page, @Param("name") String name, @Param("category") Integer category);
}
