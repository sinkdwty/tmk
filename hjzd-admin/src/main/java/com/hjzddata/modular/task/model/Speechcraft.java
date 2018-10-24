package com.hjzddata.modular.task.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 话术表
 * </p>
 *
 * @author hjzd123
 * @since 2018-07-10
 */
@TableName("hj_speechcraft")
public class Speechcraft extends Model<Speechcraft> {

    private static final long serialVersionUID = 1L;

    /**
     * 话术id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 话术内容
     */
    private String contents;
    /**
     * 备注
     */
    private String note;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 最后修改时间
     */
    @TableField("updated_at")
    private Date updatedAt;
    /**
     * 状态 1-可用，0禁用
     */
    private Integer status;
    /**
     * 是否删除，1-是，0-否
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Speechcraft{" +
        "id=" + id +
        ", contents=" + contents +
        ", note=" + note +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", status=" + status +
        ", isDel=" + isDel +
        "}";
    }
}
