package com.hjzddata.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.system.model.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知表 Mapper 接口
 * </p>
 *
 *
 * @since 2017-07-11
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 获取通知列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);

    List<Map<String, Object>> getNotice(@Param("page") Page<Notice> page, @Param("condition") String condition);

    /**
     * 检查是否存在相同的通知内容
     * */
    List<Map> noticeExistence(@Param("id") Integer id, @Param("content") String content, @Param("title") String title);

}