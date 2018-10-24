package com.hjzddata.modular.custom.service.impl;

import com.hjzddata.modular.custom.model.SendMessageLog;
import com.hjzddata.modular.custom.dao.SendMessageLogMapper;
import com.hjzddata.modular.custom.service.ISendMessageLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 短信发送日志表 服务实现类
 * </p>
 *
 * @author meikupeng
 * @since 2018-07-25
 */
@Service
public class SendMessageLogServiceImpl extends ServiceImpl<SendMessageLogMapper, SendMessageLog> implements ISendMessageLogService {

}
