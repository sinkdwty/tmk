package com.hjzddata.modular.ticket.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.HashMap;

@TableName("hj_ticket_note")
public class TicketNote extends Model<TicketNote> {
    private static final long serialVersionUID = 1L;

    /**
     * 记录类型
     */
    public static final Integer TYPE_NOTE = 1;  // 记录
    public static final Integer TYPE_UPDATE = 2;   // 变更


    public static final Integer getTypeByName(String str) {
        HashMap<String, Integer> map = new HashMap(4);
        map.put("记录", 1);
        map.put("变更", 2);
        return map.get(str);
    }

    /**
     * 主键id
     */
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**
     * 记录信息
     */
    @TableField("note")
    private String note;
    /**
     * 类型
     */
    @TableField("type")
    private Integer type;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private String createdAt;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 工单ID
     */
    @TableField("ticket_id")
    private Integer ticketId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return "TicketNote{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", type=" + type +
                ", createdAt='" + createdAt + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", ticketId=" + ticketId +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
