package com.hjzddata.modular.task.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 媒体表
 * </p>
 *
 * @author hjzd
 * @since 2018-07-05
 */
@TableName("hj_media")
public class Media extends Model<Media> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 媒体名称
     */
    private String name;
    /**
     * 媒体logo图片
     */
    private String pic;
    /**
     * 媒体链接
     */
    private String url;
    /**
     * 媒体分类
     */
    private Integer category;
    /**
     * 媒体简介
     */
    private String introduce;
    /**
     * 媒体标签
     */
    private String tags;
    /**
     * 是否删除：1 是； 0 否
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    @TableField("updated_at")
    private Date updatedAt;
    /**
     * 创建主机ip
     */
    @TableField("created_ip")
    private String createdIp;
    /**
     * 更新主机Ip
     */
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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
        return "Media{" +
        "id=" + id +
        ", name=" + name +
        ", pic=" + pic +
        ", url=" + url +
        ", category=" + category +
        ", introduce=" + introduce +
        ", tags=" + tags +
        ", isDel=" + isDel +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", createdIp=" + createdIp +
        ", updatedIp=" + updatedIp +
        "}";
    }
}
