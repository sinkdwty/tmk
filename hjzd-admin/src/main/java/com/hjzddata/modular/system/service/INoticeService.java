package com.hjzddata.modular.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.hjzddata.modular.system.model.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知表 服务类
 * </p>
 *
 * 123
 * @since 2018-02-22
 */
public interface INoticeService extends IService<Notice> {

    /**
     * 获取通知列表
     */
    List<Map<String, Object>> list(String condition);

    List<Map<String, Object>> getNotice(Page<Notice> page, String condition);

    /**
     * 检查是否存在相同的通知内容
     * */
    List<Map> noticeExistence(@Param("id") Integer id, @Param("content") String content, @Param("title") String title);
}
