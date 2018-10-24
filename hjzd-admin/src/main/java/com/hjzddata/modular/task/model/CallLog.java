package com.hjzddata.modular.task.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hjzddata.core.support.HttpKit;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 呼叫日志表
 * </p>
 *
 * @author hjzd
 * @since 2018-07-18
 */
@TableName("hj_call_log")
public class CallLog extends Model<CallLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 案件id
     */
    @TableField("case_id")
    private Integer caseId;
    /**
     * 客户姓名
     */
    @TableField("custom_name")
    private String customName;
    /**
     * 客户手机号码
     */
    @TableField("custom_phone")
    private String customPhone;
    /**
     * 案件所属坐席id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 坐席姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 呼叫状态id
     */
    @TableField("call_status_id")
    private Integer callStatusId;
    /**
     * 呼叫状态名称
     */
    @TableField("call_status_name")
    private String callStatusName;
    /**
     * 呼叫开始时间
     */
    @TableField("call_start_time")
    private Date callStartTime;
    /**
     * 呼叫结束时间
     */
    @TableField("call_end_time")
    private Date callEndTime;
    /**
     * 本次呼叫总秒数
     */
    @TableField("call_second")
    private Integer callSecond;
    /**
     * 本次振铃总秒数
     */
    @TableField("ring_second")
    private Integer ringSecond;
    /**
     * 分机号
     */
    @TableField("call_agent_no")
    private String callAgentNo;
    /**
     * 录音sessionId
     */
    @TableField("call_sessionid")
    private String callSessionid;
    /**
     * 录音唯一标识
     */
    @TableField("call_userdata")
    private String callUserdata;
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
     * 创建ip
     */
    @TableField("created_ip")
    private String createdIp;

    @TableField("check_status")
    private Integer checkStatus;

    @TableField("check_note")
    private String checkNote;

    public String getCheckNote() {
        return checkNote;
    }

    public void setCheckNote(String checkNote) {
        this.checkNote = checkNote;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Integer getChecker() {
        return checker;
    }

    public void setChecker(Integer checker) {
        this.checker = checker;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    @TableField("checker")

    private Integer checker;

    @TableField("check_date")
    private Date checkDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getCustomPhone() {
        return customPhone;
    }

    public void setCustomPhone(String customPhone) {
        this.customPhone = customPhone;
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

    public Integer getCallStatusId() {
        return callStatusId;
    }

    public void setCallStatusId(Integer callStatusId) {
        this.callStatusId = callStatusId;
    }

    public String getCallStatusName() {
        return callStatusName;
    }

    public void setCallStatusName(String callStatusName) {
        this.callStatusName = callStatusName;
    }

    public Date getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Date callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Date getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Date callEndTime) {
        this.callEndTime = callEndTime;
    }

    public Integer getCallSecond() {
        return callSecond;
    }

    public void setCallSecond(Integer callSecond) {
        this.callSecond = callSecond;
    }

    public Integer getRingSecond() {
        return ringSecond;
    }

    public void setRingSecond(Integer ringSecond) {
        this.ringSecond = ringSecond;
    }

    public String getCallAgentNo() {
        return callAgentNo;
    }

    public void setCallAgentNo(String callAgentNo) {
        this.callAgentNo = callAgentNo;
    }

    public String getCallSessionid() {
        return callSessionid;
    }

    public void setCallSessionid(String callSessionid) {
        this.callSessionid = callSessionid;
    }

    public String getCallUserdata() {
        return callUserdata;
    }

    public void setCallUserdata(String callUserdata) {
        this.callUserdata = callUserdata;
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

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CallLog{" +
        "id=" + id +
        ", caseId=" + caseId +
        ", customName=" + customName +
        ", customPhone=" + customPhone +
        ", userId=" + userId +
        ", userName=" + userName +
        ", callStatusId=" + callStatusId +
        ", callStatusName=" + callStatusName +
        ", callStartTime=" + callStartTime +
        ", callEndTime=" + callEndTime +
        ", callSecond=" + callSecond +
        ", ringSecond=" + ringSecond +
        ", callAgentNo=" + callAgentNo +
        ", callSessionid=" + callSessionid +
        ", callUserdata=" + callUserdata +
        ", note=" + note +
        ", createdAt=" + createdAt +
        ", createdIp=" + createdIp +
        ", checkNote=" + checkNote +
        "}";
    }

    /**
     * 获取通话记录session_id
     */
    public static String getCallSessionId(String callUserdata, String[] callConfig) {
        // 处理传值
        String recordIp = callConfig.length == 6 ? callConfig[5] : callConfig[0];
        String orgidentity = callConfig[1];
        String user = callConfig[3];
        String pwd = callConfig[4];
        // 设置参数
        Map<String, String> parameters = new HashMap<>();
        parameters.put("EVENT", "GetCdr");
        parameters.put("orgidentity", orgidentity);
        parameters.put("user", user);
        parameters.put("password", pwd);
        parameters.put("pwdtype", "plaintext");
        parameters.put("id", "0");
        parameters.put("limit", "10");
        parameters.put("userdata", callUserdata);
        String url = "http://" + recordIp + "/asterccinterfaces";
//        String parameters = "EVENT=GetCdr&orgidentity=" + orgidentity + "&user=" + user + "&password="+pwd+"&pwdtype=plaintext&id=0&limit=10&userdata="+callUserdata;
        String result = HttpKit.sendGet(url, parameters);


        return result;
    }

    /**
     * 获取录音地址
     * @retrun 返回完整录音路径
     */
    public static String getRecordingAddr(String callSessionid, String callDate, String servierIp ,String ip) {
        Map<String, String> param = new HashMap<>();
        param.put("EVENT", "GetMonitor");
        param.put("sessionid", callSessionid);
        param.put("calldate", callDate);
        param.put("mp3", "yes");

        String url = "http://" + servierIp + "/asterccinterfaces";

        String result = HttpKit.sendGet(url, param);

        String[] addr = result.split("\\|Retuen\\|");

        return "http://" + ip + "/" + addr[2];

    }
}
