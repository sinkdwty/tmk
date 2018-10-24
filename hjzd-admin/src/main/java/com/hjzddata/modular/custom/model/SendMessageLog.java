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
 * 短信发送日志表
 * </p>
 *
 * @author meikupeng
 * @since 2018-07-25
 */
@TableName("hj_send_message_log")
public class SendMessageLog extends Model<SendMessageLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 短息发送日志id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 短信发送客户的id
     */
    @TableField("custom_id")
    private Integer customId;
    /**
     * 短信发送客户姓名
     */
    @TableField("custom_name")
    private String customName;
    /**
     * 短信发送操作用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 短信发送操作用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 发送结果 1-成功 -n-失败(-n指错误码)
     */
    private Integer result;
    /**
     * 发送内容
     */
    private String message;
    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;
    /**
     * 执行操作ip
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

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        return "SendMessageLog{" +
        "id=" + id +
        ", customId=" + customId +
        ", customName=" + customName +
        ", userId=" + userId +
        ", userName=" + userName +
        ", result=" + result +
        ", message=" + message +
        ", sendTime=" + sendTime +
        ", createIp=" + createIp +
        "}";
    }
}
