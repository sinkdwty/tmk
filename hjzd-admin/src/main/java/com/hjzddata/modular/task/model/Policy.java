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
 * 
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-05
 */
@TableName("hj_policy")
public class Policy extends Model<Policy> {

    private static final long serialVersionUID = 1L;

    /**
     * 策略id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 策略名
     */
    @TableField("policy_name")
    private String policyName;
    /**
     * 策略处理
     */
    @TableField("policy_key")
    private String policyKey;
    /**
     * 备注
     */
    @TableField("note")
    private String note;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 左后修改时间
     */
    @TableField("updated_at")
    private Date updatedAt;
    /**
     * 策略状态，0-禁用，1-启用
     */
    private Integer status;
    /**
     * 是否删除，0-否，1-删除
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

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyKey() {
        return policyKey;
    }

    public void setPolicyKey(String policyKey) {
        this.policyKey = policyKey;
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
        return "Policy{" +
        "id=" + id +
        ", policyName=" + policyName +
        ", policyKey=" + policyKey +
        ", note=" + note +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", status=" + status +
        ", isDel=" + isDel +
        "}";
    }
}
