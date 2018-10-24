package com.hjzddata.modular.ticket.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.hjzddata.modular.ticket.model.Ticket;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ITicketService extends IService<Ticket> {

    public int setStatus(Integer ticketId, int status);


    public List<Map<String, Object>> selectTickets(
            Page<Ticket> page,
            String title,
            String column,
            String beginTime,
            String endTime,
            Integer assignUserId,
            String orderByField,
            boolean asc
    );

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
    );

    /**
     * 解析数据表
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    Collection parseExcel(String fileName) throws Exception;

    /* 根据填写的工单标题，判断是否已存在该工单，如果不存在则允许更新，否则不允许更新 */
    List<Map> ticketTitleExit(@Param("title") String title,@Param("id") Integer id);
}
