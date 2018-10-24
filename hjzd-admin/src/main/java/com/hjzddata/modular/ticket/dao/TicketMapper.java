package com.hjzddata.modular.ticket.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.ticket.model.Ticket;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TicketMapper extends BaseMapper<Ticket> {
    /**
     * 修改用户状态
     */
    int setStatus(@Param("ticketId") Integer ticketId, @Param("status") int status);

    /**
     * 根据条件查询工单列表
     */
    List<Map<String, Object>> selectTickets(
            @Param("page") Page<Ticket> page,
            @Param("title") String title,
            @Param("column") String column,
            @Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("assignUserId") Integer assignUserId,
            @Param("orderByField") String orderByField,
            @Param("isAsc") boolean isAsc
    );

    /* 根据填写的工单标题，判断是否已存在该工单，如果不存在则允许更新，否则不允许更新 */
    List<Map> ticketTitleExit(@Param("title") String title,@Param("id") Integer id);
}
