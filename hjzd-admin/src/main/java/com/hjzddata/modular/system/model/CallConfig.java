package com.hjzddata.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hjzd
 * @since 2018-07-17
 */
@TableName("hj_call_config")
public class CallConfig extends Model<CallConfig> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 基地名称
     */
    @TableField("base_name")
    private String baseName;
    /**
     * 基地id
     */
    @TableField("base_id")
    private Integer baseId;
    /**
     * 外呼系统id
     */
    @TableField("call_system_id")
    private Integer callSystemId;
    /**
     * 外呼系统配置
     */
    private String config;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;
    /**
     * 是否删除：1 是；0 否
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

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }


    public Integer getCallSystemId() {
        return callSystemId;
    }

    public void setCallSystemId(Integer callSystemId) {
        this.callSystemId = callSystemId;
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CallConfig{" +
        "id=" + id +
        ", baseName=" + baseName +
        ", baseId=" + baseId +
        ", config=" + config +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", isDel=" + isDel +
        "}";
    }
}
