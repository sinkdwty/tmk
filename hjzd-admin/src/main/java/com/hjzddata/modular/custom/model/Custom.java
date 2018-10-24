package com.hjzddata.modular.custom.model;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author hjzd
 * @since 2018-07-31
 */
@TableName("hj_custom")
public class Custom extends Model<Custom> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 客户姓名
     */
    @TableField("custom_name")
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
     * 客户公司
     */
    private String company;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 负责人
     */
    private String principal;
    /**
     * 导入人
     */
    @TableField("import_name")
    private String importName;
    /**
     * 导入人id
     */
    @TableField("import_id")
    private Integer importId;
    /**
     * 标签
     */
    private String label;
    /**
     * 客户来源
     */
    @TableField("custom_source")
    private String customSource;
    /**
     * 首次联系时间
     */
    @TableField("first_contact_time")
    private Date firstContactTime;

    @TableField("last_contact_time")
    private Date lastContactTime;
    /**
     * 联系次数
     */
    @TableField("contact_times")
    private Integer contactTimes;
    /**
     * 致电结果码
     */
    @TableField("call_status_name")
    private String callStatusName;
    /**
     * 致电结果id
     */
    @TableField("call_status_id")
    private Integer callStatusId;
    /**
     * 电话小结
     */
    @TableField("call_note")
    private String callNote;
    /**
     * 客户备注
     */
    private String note;
    /**
     * 分配坐席id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 所属公司id
     */
    @TableField("company_id")
    private Integer companyId;
    /**
     * 状态： 0-导入中 1-未分配 2-已分配 3-结束 4-回收 5-不可用
     */
    private Integer status;
    /**
     * 分配时间
     */
    @TableField("allocate_time")
    private Date allocateTime;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

    /**
     * 是否删除
     * @return
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;

    /**
     * 预约时间
     */
    @TableField("reserve_time")
    private Date reserveTime;

    /**
     * 项目id
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 安庆基地客户其他字段
     */
    @TableField("aq_custom")
    private String aqCustom;

    @TableField("lt_checker")
    private Integer ltChecker;

    @TableField("lt_check_date")
    private Date ltCheckDate;

    @TableField("lt_check_status")
    private Integer ltCheckStatus;

    @TableField("lt_check_note")
    private String ltCheckNote;

    public String getAqCustom() {
        return aqCustom;
    }

    public void setAqCustom(String aqCustom) {
        this.aqCustom = aqCustom;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCustomSource() {
        return customSource;
    }

    public void setCustomSource(String customSource) {
        this.customSource = customSource;
    }

    public Date getFirstContactTime() {
        return firstContactTime;
    }

    public void setFirstContactTime(Date firstContactTime) {
        this.firstContactTime = firstContactTime;
    }

    public Integer getContactTimes() {
        return contactTimes;
    }

    public void setContactTimes(Integer contactTimes) {
        this.contactTimes = contactTimes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAllocateTime() {
        return allocateTime;
    }

    public void setAllocateTime(Date allocateTime) {
        this.allocateTime = allocateTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }


    public Date getReserveTime() {

        return reserveTime;
    }

    /**  modified by eric 2018-09-14 **/
    public void setReserveTime(Date reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Custom{" +
        "id=" + id +
        ", batchNo=" + batchNo +
        ", customName=" + customName +
        ", phone=" + phone +
        ", email=" + email +
        ", company=" + company +
        ", address=" + address +
        ", principal=" + principal +
        ", label=" + label +
        ", customSource=" + customSource +
        ", firstContactTime=" + firstContactTime +
        ", contactTimes=" + contactTimes +
        ", callStatusName=" + callStatusName +
        ", callStatusId=" + callStatusId +
        ", callNote=" + callNote +
        ", note=" + note +
        ", userId=" + userId +
        ", companyId=" + companyId +
        ", status=" + status +
        ", allocateTime=" + allocateTime +
        ", createdTime=" + createdTime +
        ", updatedTime=" + updatedTime +
        ", reserveTime=" + reserveTime +
        ", productId=" + productId +
        ", importName=" + importName +
        ", importId=" + importId +
        "}";
    }


    public String getCallStatusName() {
        return callStatusName;
    }

    public void setCallStatusName(String callStatusName) {
        this.callStatusName = callStatusName;
    }

    public Integer getCallStatusId() {
        return callStatusId;
    }

    public void setCallStatusId(Integer callStatusId) {
        this.callStatusId = callStatusId;
    }

    public String getCallNote() {
        return callNote;
    }

    public void setCallNote(String callNote) {
        this.callNote = callNote;
    }

    public String getImportName(){ return importName;}

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
    }

    public Date getLtCheckDate() {
        return ltCheckDate;
    }

    public Integer getLtCheckStatus() {
        return ltCheckStatus;
    }

    public Integer getLtChecker() {
        return ltChecker;
    }

    public String getLtCheckNote() {
        return ltCheckNote;
    }

    public void setLtCheckDate(Date ltCheckDate) {
        this.ltCheckDate = ltCheckDate;
    }

    public void setLtChecker(Integer ltChecker) {
        this.ltChecker = ltChecker;
    }

    public void setLtCheckNote(String ltCheckNote) {
        this.ltCheckNote = ltCheckNote;
    }

    public void setLtCheckStatus(Integer ltCheckStatus) {
        this.ltCheckStatus = ltCheckStatus;
    }

    public Date getLastContactTime() {
        return lastContactTime;
    }

    public void setLastContactTime(Date lastContactTime) {
        this.lastContactTime = lastContactTime;
    }
}

