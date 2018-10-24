package com.hjzddata.modular.system.model;

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
 * @since 2018-09-27
 */
@TableName("hj_sms_batch")
public class HjSmsBatch extends Model<HjSmsBatch> {

    private static final long serialVersionUID = 1L;

    /**
     * 自动增长列
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 批次名称
     */
    @TableField("sms_batch_name")
    private String smsBatchName;
    /**
     * 批次描述
     */
    @TableField("batch_desc")
    private String batchDesc;
    /**
     * 创建时间
     */
    @TableField("create_at")
    private Date createAt;
    /**
     * 短信内容
     */
    @TableField("sms_content")
    private String smsContent;
    /**
     * 批次容量
     */
    @TableField("batch_capacity")
    private Integer batchCapacity;
    /**
     * 批次剩余容量
     */
    @TableField("left_capacity")
    private Integer leftCapacity;
    /**
     * (0-待发送，1-预约待发送，2-已发送，3-处理成功，4-处理失败（可做补充处理，先查询，在决定是否重新发送），5- 可重发）
     */
    private Integer status;
    /**
     * 发送结果描述
     */
    @TableField("send_note")
    private String sendNote;
    /**
     * 成功数
     */
    @TableField("success_sms")
    private Integer successSms;
    /**
     * 失败数
     */
    @TableField("failure_sms")
    private Integer failureSms;
    /**
     * 操作人ID
     */
    private Integer userid;
    /**
     * 批次结果
     */
    @TableField("result_text")
    private String resultText;
    /**
     * 批次明细
     */
    @TableField("result_detail")
    private String resultDetail;

    /**
     * 返回任务ID
     */
    @TableField("ret_task_id")
    private String retTaskId;

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    @TableField("phones")
    private String phones;


    public String getReserve_time() {
        return reserve_time;
    }

    public void setReserve_time(String reserve_time) {
        this.reserve_time = reserve_time;
    }

    @TableField("reserve_time")
    private  String reserve_time;





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSmsBatchName() {
        return smsBatchName;
    }

    public void setSmsBatchName(String smsBatchName) {
        this.smsBatchName = smsBatchName;
    }

    public String getBatchDesc() {
        return batchDesc;
    }

    public void setBatchDesc(String batchDesc) {
        this.batchDesc = batchDesc;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Integer getBatchCapacity() {
        return batchCapacity;
    }

    public void setBatchCapacity(Integer batchCapacity) {
        this.batchCapacity = batchCapacity;
    }

    public Integer getLeftCapacity() {
        return leftCapacity;
    }

    public void setLeftCapacity(Integer leftCapacity) {
        this.leftCapacity = leftCapacity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSendNote() {
        return sendNote;
    }

    public void setSendNote(String sendNote) {
        this.sendNote = sendNote;
    }

    public Integer getSuccessSms() {
        return successSms;
    }

    public void setSuccessSms(Integer successSms) {
        this.successSms = successSms;
    }

    public Integer getFailureSms() {
        return failureSms;
    }

    public void setFailureSms(Integer failureSms) {
        this.failureSms = failureSms;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getRetTaskId() {
        return retTaskId;
    }

    public void setRetTaskId(String retTaskId) {
        this.retTaskId = retTaskId;
    }

    @Override
    public String toString() {
        return "HjSmsBatch{" +
        "id=" + id +
        ", smsBatchName=" + smsBatchName +
        ", batchDesc=" + batchDesc +
        ", createAt=" + createAt +
        ", smsContent=" + smsContent +
        ", batchCapacity=" + batchCapacity +
        ", leftCapacity=" + leftCapacity +
        ", status=" + status +
        ", sendNote=" + sendNote +
        ", successSms=" + successSms +
        ", failureSms=" + failureSms +
        ", userid=" + userid +
        ", resultText=" + resultText +
        ", resultDetail=" + resultDetail +
        "}";
    }
}
