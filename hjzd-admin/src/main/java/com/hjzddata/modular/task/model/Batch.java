package com.hjzddata.modular.task.model;

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
 * 数据批次表
 * </p>
 *
 * @author hjzd
 * @since 2018-09-17
 */
@TableName("hj_batch")
public class Batch extends Model<Batch> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 项目id
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 批次编号
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 导入人id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 上传文件路径
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 导入数量
     */
    @TableField("import_num")
    private Integer importNum;
    /**
     * 上传时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 小组id（备用）
     */
    @TableField("group_id")
    private Integer groupId;
    /**
     * 是否删除：0否； 1是
     */
    @TableLogic
    @TableField("is_del")
    private Integer isDel;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getImportNum() {
        return importNum;
    }

    public void setImportNum(Integer importNum) {
        this.importNum = importNum;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
        return "Batch{" +
        "id=" + id +
        ", productId=" + productId +
        ", batchNo=" + batchNo +
        ", userId=" + userId +
        ", filePath=" + filePath +
        ", importNum=" + importNum +
        ", createdAt=" + createdAt +
        ", groupId=" + groupId +
        ", isDel=" + isDel +
        "}";
    }
}
