package com.hjzddata.modular.knowledge.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.knowledge.model.Message;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 短信模板表 服务类
 * </p>
 *
 * @author meikunpeng
 * @since 2018-07-16
 */
public interface IMessageService extends IService<Message> {

    List<Map<String, Object>> selectMessage(@Param("page") Page<Message> page, @Param("part") String part, @Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 修改状态
     */
    int editStatus(@Param("messageId") Integer messageId, @Param("status") int status);

    /**
     * 获取短信模板
     */
    List<Map> messageList();

    /* 判断是否存在短信模板 */
    List<Map> messageExistence(@Param("message") String message, @Param("id") Integer id);

}
