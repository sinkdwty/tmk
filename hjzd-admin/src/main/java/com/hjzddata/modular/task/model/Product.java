package com.hjzddata.modular.task.model;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author meikunpeng123
 * @since 2018-07-02
 */
@TableName("sys_product")
public class Product extends Model<Product> {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    private Integer id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目状态，0-禁用，1-可用
     */
    private Integer status;
    /**
     * 基地Id
     */
    @TableField("base_id")
    private Integer baseId;
    /**
     * 是否删除，0-删除，1-正常
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updateAt;
    /**
     * 创建Ip
     */
    @TableField("created_ip")
    private String createIp;
    /**
     * 更新Ip
     */
    @TableField("updated_ip")
    private String updateIp;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getUpdateIp() {
        return updateIp;
    }

    public void setUpdateIp(String updateIp) {
        this.updateIp = updateIp;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Product{" +
        "id=" + id +
        ", name=" + name +
        ", status=" + status +
        ", baseId=" + baseId +
        ", isDel=" + isDel +
        ", createAt=" + createAt +
        ", updateAt=" + updateAt +
        ", createIp=" + createIp +
        ", updateIp=" + updateIp +
        "}";
    }
}
