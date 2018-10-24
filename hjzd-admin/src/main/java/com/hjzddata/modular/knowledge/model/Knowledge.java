package com.hjzddata.modular.knowledge.model;

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
 * 知识表
 * </p>
 *
 * @author hjzd
 * @since 2018-07-10
 */
@TableName("hj_knowledge")
public class Knowledge extends Model<Knowledge> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 知识名称
     */
    private String name;
    /**
     * 关键词（多个关键词英文逗号,隔开）
     */
    @TableField("key_word")
    private String keyWord;
    /**
     * 知识分类
     */
    private Integer category;
    /**
     * 知识状态：0 未发布，1已发布
     */
    private Integer status;
    /**
     * 知识内容
     */
    private String content;
    /**
     * 附件
     */
    private String accessory;
    /**
     * 是否删除：0 未删除，1 已删除
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;
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
     * 创建主机ip
     */
    @TableField("created_ip")
    private String createdIp;
    @TableField("updated_ip")
    private String updatedIp;


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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }

    public String getUpdatedIp() {
        return updatedIp;
    }

    public void setUpdatedIp(String updatedIp) {
        this.updatedIp = updatedIp;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Knowledge{" +
        "id=" + id +
        ", name=" + name +
        ", keyWord=" + keyWord +
        ", category=" + category +
        ", status=" + status +
        ", content=" + content +
        ", accessory=" + accessory +
        ", isDel=" + isDel +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", createdIp=" + createdIp +
        ", updatedIp=" + updatedIp +
        "}";
    }
}
