package com.hjzddata.modular.ticket.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjzddata.core.excel.ExcelLogs;
import com.hjzddata.core.excel.ExcelUtil;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.util.DateUtil;
import com.hjzddata.modular.ticket.dao.TicketMapper;
import com.hjzddata.modular.ticket.model.Ticket;
import com.hjzddata.modular.ticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements ITicketService {
    @Autowired
    private TicketMapper ticketMapper;

    @Override
    public int setStatus(Integer ticketId, int status) {
        return this.baseMapper.setStatus(ticketId, status);
    }

    @Override
    public List<Map<String, Object>> selectTickets(
            Page<Ticket> page,
            String title,
            String column,
            String beginTime,
            String endTime,
            Integer assignUserId,
            String orderByField,
            boolean asc
    ) {
        return this.baseMapper.selectTickets(page, title, column,beginTime, endTime, assignUserId, orderByField, asc);
    }

    @Override
    public  List<Map<String, Object>>  selectPage(
            Page<Ticket> page,
            Integer type,
            String title,
            String column,
            String beginTime,
            String endTime,
            Integer assignUserId,
            String orderByField,
            boolean asc
    ) {
        EntityWrapper wrapper = new EntityWrapper<Ticket>();
        wrapper.orderBy("id", false);
        wrapper.orderBy(page.getOrderByField(), page.isAsc());

        if (type == null || type == 1) {
            /** 我的未解决工单 **/
            wrapper.eq("assign_user_id", ShiroKit.getUser().getId());
            wrapper.in("status", new Integer[]{Ticket.STATUS_ACTIVE, Ticket.STATUS_RESOLVING});
        } else if (type == 2) {
            /** 组内未解决工单 **/
            wrapper.eq("assign_dept_id", ShiroKit.getUser().getDeptId());
            wrapper.in("status", new Integer[]{Ticket.STATUS_ACTIVE, Ticket.STATUS_RESOLVING});
        } else if (type == 3) {
            /** 所有未解决工单 **/
            wrapper.in("status", new Integer[]{Ticket.STATUS_ACTIVE, Ticket.STATUS_RESOLVING});
        } else if (type == 4) {
            /** 我的已解决工单 **/
            wrapper.eq("assign_user_id", ShiroKit.getUser().getId());
            wrapper.in("status", new Integer[]{Ticket.STATUS_RESOLVED});
        } else if (type == 5) {
            /** 所有工单 **/

        } else if (type == 6) {
            /** 所有紧急工单 **/
            //wrapper.in("status", new Integer[]{Ticket.STATUS_ACTIVE, Ticket.STATUS_RESOLVING});
            wrapper.eq("levels", Ticket.LEVEL_URGENT);
        } else if (type == 7) {
            /** 所有未分配工单 **/
            wrapper.andNew("(assign_dept_id IS NULL OR assign_dept_id = 0)");
            wrapper.andNew("(assign_user_id IS NULL OR assign_user_id = 0)");
        } else if (type == 8) {
            /** 所有已关闭工单 **/
            wrapper.eq("status", Ticket.STATUS_CLOSED);
        } else if (type == 9) {
            /** 所有最近一天更新工单 **/
            wrapper.in("status", new Integer[]{Ticket.STATUS_ACTIVE, Ticket.STATUS_RESOLVING});
            wrapper.between("updated_at", DateUtil.getDay() + " 00:00:00", DateUtil.getDay() + " 23:59:59");
        } else if (type == 10) {
            /** 所有长时间未更新工单 **/
            wrapper.le("updated_at", DateUtil.getNewDate(DateUtil.getDay(), -1) + " 23:59:59");
        }

        if (title != null && column != null) {
            wrapper.like(column, title);
        }

        if (beginTime != null && beginTime.length() > 1) {
            wrapper.ge("created_at", beginTime + " 00:00:00");
        }

        if (endTime != null && endTime.length() > 1) {
            wrapper.le("created_at", endTime + " 23:59:59");
        }

        if (assignUserId != null) {
            wrapper.eq("assign_user_id", assignUserId);
        }

        Ticket ticket = new Ticket();
        ticket.selectPage(page, wrapper);
        List tickets = page.getRecords();

        return  tickets;
    }

    @Override
    public Collection parseExcel(String fileName) throws Exception {
        File f = new File(fileName);
        InputStream inputStream= new FileInputStream(f);

        ExcelLogs logs = new ExcelLogs();
        Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);

        return importExcel;
    }

    /* 根据填写的工单标题，判断是否已存在该工单，如果不存在则允许更新，否则不允许更新 */
    @Override
    public List<Map> ticketTitleExit(String title,Integer id) {
        return this.baseMapper.ticketTitleExit(title,id);
    }
}
