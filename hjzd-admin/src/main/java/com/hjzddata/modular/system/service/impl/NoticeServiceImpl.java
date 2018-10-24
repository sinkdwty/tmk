package com.hjzddata.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjzddata.modular.system.dao.NoticeMapper;
import com.hjzddata.modular.system.model.Notice;
import com.hjzddata.modular.system.service.INoticeService;
import com.hjzddata.modular.system.dao.NoticeMapper;
import com.hjzddata.modular.system.model.Notice;
import com.hjzddata.modular.system.service.INoticeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * 123
 * @since 2018-02-22
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }

    @Override
    public List<Map<String, Object>> getNotice(Page<Notice> page, String condition) {
        return this.baseMapper.getNotice(page, condition);
    }

    @Override
    public List<Map> noticeExistence (Integer id, String content, String title)
    {
        return this.baseMapper.noticeExistence(id,content, title);
    }
}
