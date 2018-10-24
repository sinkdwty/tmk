package com.hjzddata.modular.knowledge.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.knowledge.model.Message;
import com.hjzddata.modular.knowledge.dao.MessageMapper;
import com.hjzddata.modular.knowledge.service.IMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 短信模板表 服务实现类
 * </p>
 *
 * @author meikunpeng
 * @since 2018-07-16
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Override
    public List<Map<String, Object>> selectMessage(Page<Message> page, String part, String startTime, String endTime) {
        return this.baseMapper.selectMessage(page, part, startTime,endTime);
    }

    @Override
    public int editStatus (Integer messageId, int status) {
        return this.baseMapper.editStatus(messageId, status);
    }

    @Override
    public List<Map> messageList() {
        return this.baseMapper.messageList();
    }
    @Override
    public List<Map> messageExistence (String message, Integer id) {
        return this.baseMapper.messageExistence(message, id);
    }
}
