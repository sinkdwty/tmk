package com.hjzddata.modular.custom.model;

/**
 * <p>
 * 宁夏客户信息字段
 * </p>
 *
 * @author hjzd
 * @since 2018-07-31
 */
public class AqCustom {

    private static final long serialVersionUID = 1L;

    /**
     * 客户姓名
     */
    private String customName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String sex;
    /**
     * 工作城市
     */
    private String jobCity;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 工作类型
     */
    private String jobType;
    /**
     * 持卡行
     */
    private String cardBank;
    /**
     * 客户学历
     */
    private String customEducationBackground;
    /**
     * 毕业院校
     */
    private String graduateSchool;
    /**
     * 申请卡种
     */
    private String applyCardType;
    /**
     * 数据渠道
     */
    private String dataChannel;
    /**
     * 数据批次
     */
    private String dataBatch;
    /**
     * 他有客群
     */
    private String otherCustomers;
    /**
     * 是否持有他行卡
     */
    private String havingOtherCard;
    /**
     * 卡片使用时间
     */
    private String cardUseTime;
    /**
     * 是否企业主股东
     */
    private String isCorporateShareholders;
    /**
     * 方便联系时间
     */
    private String convenientTime;

    // add by eric 2018-9-14 start

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    /**
     * 预约时间
     */
    private String reserveTime;


    /**
     * 联系次数
     */
    private Integer contactTimes;

    public String getCall_status_top() {
        return call_status_top;
    }

    public void setCall_status_top(String call_status_top) {
        this.call_status_top = call_status_top;
    }

    public Integer getCall_status_content() {
        return call_status_content;
    }

    public void setCall_status_content(Integer call_status_content) {
        this.call_status_content = call_status_content;
    }

    public String getPhoneNote() {
        return phoneNote;
    }

    public void setPhoneNote(String phoneNote) {
        this.phoneNote = phoneNote;
    }

    /**
     * 致电结果码
     */
    private String call_status_top;

    /**
     * 致电结果id
     */
    private Integer call_status_content;

    /**
     * 电话小结
     */
    private String phoneNote;

    public Integer getContactTimes() {
        return contactTimes;
    }

    public void setContactTimes(Integer contactTimes) {
        this.contactTimes = contactTimes;
    }

// add by eric 2018-9-14 end

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJobCity() {
        return jobCity;
    }

    public void setJobCity(String jobCity) {
        this.jobCity = jobCity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public String getCustomEducationBackground() {
        return customEducationBackground;
    }

    public void setCustomEducationBackground(String cunstomEducationBackground) {
        this.customEducationBackground = cunstomEducationBackground;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getApplyCardType() {
        return applyCardType;
    }

    public void setApplyCardType(String applyCardType) {
        this.applyCardType = applyCardType;
    }

    public String getDataChannel() {
        return dataChannel;
    }

    public void setDataChannel(String dataChannel) {
        this.dataChannel = dataChannel;
    }

    public String getDataBatch() {
        return dataBatch;
    }

    public void setDataBatch(String dataBatch) {
        this.dataBatch = dataBatch;
    }

    public String getOtherCustomers() {
        return otherCustomers;
    }

    public void setOtherCustomers(String otherCustomers) {
        this.otherCustomers = otherCustomers;
    }

    public String getHavingOtherCard() {
        return havingOtherCard;
    }

    public void setHavingOtherCard(String havingOtherCard) {
        this.havingOtherCard = havingOtherCard;
    }

    public String getCardUseTime() {
        return cardUseTime;
    }

    public void setCardUseTime(String cardUseTime) {
        this.cardUseTime = cardUseTime;
    }

    public String getIsCorporateShareholders() {
        return isCorporateShareholders;
    }

    public void setIsCorporateShareholders(String isCorporateShareholders) {
        this.isCorporateShareholders = isCorporateShareholders;
    }

    public String getConvenientTime() {
        return convenientTime;
    }

    public void setConvenientTime(String convenientTime) {
        this.convenientTime = convenientTime;
    }

    @Override
    public String toString() {
        return "NxCustom{" +
        ", customName=" + customName +
        ", phone=" + phone +
        ", sex=" + sex +
        ", jobCity=" + jobCity +
        ", companyName=" + companyName +
        ", jobType=" + jobType +
        ", cardBank=" + cardBank +
        ", customEducationBackground=" + customEducationBackground +
        ", graduateSchool=" + graduateSchool +
        ", applyCardType=" + applyCardType +
        ", dataChannel=" + dataChannel +
        ", dataBatch=" + dataBatch +
        ", otherCustomers=" + otherCustomers +
        ", havingOtherCard=" + havingOtherCard +
        ", cardUseTime=" + cardUseTime +
        ", isCorporateShareholders=" + isCorporateShareholders +
        ", convenientTime=" + convenientTime +
                ",callStatusId=" + call_status_content+
                ",callStatusName="+ call_status_top+
                ",reserveTime= "+ reserveTime +
                ",callNote="+ this.phoneNote+
        "}";
    }
}
