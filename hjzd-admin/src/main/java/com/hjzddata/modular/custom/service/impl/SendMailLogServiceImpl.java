package com.hjzddata.modular.custom.service.impl;

import com.hjzddata.modular.custom.model.SendMailLog;
import com.hjzddata.modular.custom.dao.SendMailLogMapper;
import com.hjzddata.modular.custom.service.ISendMailLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮件发送日志表 服务实现类
 * </p>
 *
 * @author meikupeng
 * @since 2018-07-25
 */
@Service
public class SendMailLogServiceImpl extends ServiceImpl<SendMailLogMapper, SendMailLog> implements ISendMailLogService {

}
