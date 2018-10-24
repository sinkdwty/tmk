package com.hjzddata.modular.custom.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 邮件发送日志表
 * </p>
 *
 * @author meikupeng
 * @since 2018-07-25
 */
@TableName("hj_send_mail_log")
public class SendMailLog extends Model<SendMailLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 发送邮件日志id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户id
     */
    @TableField("custom_id")
    private Integer customId;
    /**
     * 客户名
     */
    @TableField("custom_name")
    private String customName;
    /**
     * 操作用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 操作用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 邮箱地址
     */
    @TableField("email_address")
    private String emailAddress;
    /**
     * 发送结果 1-成功 0-失败
     */
    private Integer result;
    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;
    /**
     * 操作ip
     */
    @TableField("create_ip")
    private String createIp;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomId() {
        return customId;
    }

    public void setCustomId(Integer customId) {
        this.customId = customId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SendMailLog{" +
        "id=" + id +
        ", customId=" + customId +
        ", customName=" + customName +
        ", userId=" + userId +
        ", userName=" + userName +
        ", emailAddress=" + emailAddress +
        ", result=" + result +
        ", sendTime=" + sendTime +
        ", createIp=" + createIp +
        "}";
    }
}
