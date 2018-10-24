package com.hjzddata.modular.ticket.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.HashMap;

@TableName("hj_ticket")
public class Ticket extends Model<Ticket> {
    private static final long serialVersionUID = 1L;

    /**
     * 工单状态
     */
    public static final Integer STATUS_ACTIVE = 1;  // 开启
    public static final Integer STATUS_RESOLVING = 2;   // 处理中
    public static final Integer STATUS_RESOLVED = 3;    // 已结束
    public static final Integer STATUS_CLOSED = 4;   // 已关闭

    /**
     * 工单优先级
     */
    public static final Integer LEVEL_URGENT = 1; // 紧急
    public static final Integer LEVEL_HIGH = 2;  // 高
    public static final Integer LEVEL_NORMAL = 3; // 标准
    public static final Integer LEVEL_LOW = 4;  // 低

    public static final Integer getStatusByName(String str) {
        HashMap<String, Integer> map = new HashMap(4);
        map.put("开启", 1);
        map.put("解决中", 2);
        map.put("已解决", 3);
        map.put("已关闭", 4);
        return map.get(str);
    }

    public static final String getLevelKeyToName(Integer i) {
        HashMap<Integer, String> map = new HashMap(4);
        map.put(1, "紧急");
        map.put(2, "高");
        map.put(3, "标准");
        map.put(4, "低");
        return map.get(i);
    }


    public static final String getStatusByKeyToName(Integer i) {
        HashMap<Integer, String> map = new HashMap(4);
        map.put(1, "开启");
        map.put(2, "解决中");
        map.put(3, "已解决");
        map.put(4, "已关闭");
        return map.get(i);
    }

    public static final Integer getLevelByName(String str) {
        HashMap<String, Integer> map = new HashMap(4);
        map.put("紧急", 1);
        map.put("高", 2);
        map.put("标准", 3);
        map.put("低", 4);
        return map.get(str);
    }
    /**
     * 主键id
     */
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**
     * 主题
     */
    @TableField("title")
    private String title;
    /**
     * 内容
     */
    @TableField("content")
    private String content;
    /**
     * 附件
     */
    @TableField("accessory")
    private String accessory;
    /**
     * 客户姓名
     */
    @TableField("custom_name")
    private String customName;
    /**
     * 客户姓名
     */
    @TableField("custom_id")
    private Integer customId;
    /**
     *  状态：1-开启 2-解决中 3-已解决 4-已关闭
     */
    @TableField("status")
    private Integer status;
    /**
     * 标签
     */
    @TableField("tags")
    private String tags;
    /**
     * 优先级：1-紧急 2-高 3-标准 4-低
     */
    @TableField("levels")
    private Integer levels;
    /**
     * 来源
     */
    @TableField("ticket_from")
    private String ticketFrom;
    /**
     * 是否删除，1是，0否
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private String createdAt;
    /**
     * 修改时间
     */
    @TableField("updated_at")
    private String updatedAt;
    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Integer createUserId;
    /**
     * 受理人ID
     */
    @TableField("assign_user_id")
    private Integer assignUserId;
    /**
     * 创建人
     */
    @TableField("create_user_name")
    private String createUserName;
    /**
     * 受理人
     */
    @TableField("assign_user_name")
    private String assignUserName;

    /**
     * 受理组ID
     */
    @TableField("assign_dept_id")
    private Integer assignDeptId;
    /**
     * 受理组
     */
    @TableField("assign_dept_name")
    private String assignDeptName;
    /**
     * 关注人IDs
     */
    @TableField("follows")
    private String follows;

    /**
     * 关注人
     */
    @TableField("follow_names")
    private String followNames;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Integer getCustomId() {
        return customId;
    }

    public void setCustomId(Integer customId) {
        this.customId = customId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    public String getTicketFrom() {
        return ticketFrom;
    }

    public void setTicketFrom(String ticketFrom) {
        this.ticketFrom = ticketFrom;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(Integer assignUserId) {
        this.assignUserId = assignUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getAssignUserName() {
        return assignUserName;
    }

    public void setAssignUserName(String assignUserName) {
        this.assignUserName = assignUserName;
    }

    public Integer getAssignDeptId() {
        return assignDeptId;
    }

    public void setAssignDeptId(Integer assignDeptId) {
        this.assignDeptId = assignDeptId;
    }

    public String getAssignDeptName() {
        return assignDeptName;
    }

    public void setAssignDeptName(String assignDeptName) {
        this.assignDeptName = assignDeptName;
    }

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getFollowNames() {
        return followNames;
    }

    public void setFollowNames(String followNames) {
        this.followNames = followNames;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", accessory='" + accessory + '\'' +
                ", customName='" + customName + '\'' +
                ", customId=" + customId +
                ", status=" + status +
                ", tags='" + tags + '\'' +
                ", levels=" + levels +
                ", ticketFrom='" + ticketFrom + '\'' +
                ", isDel=" + isDel +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", createUserId=" + createUserId +
                ", assignUserId=" + assignUserId +
                ", createUserName='" + createUserName + '\'' +
                ", assignUserName='" + assignUserName + '\'' +
                ", assignDeptId=" + assignDeptId +
                ", assignDeptName='" + assignDeptName + '\'' +
                ", follows='" + follows + '\'' +
                ", followNames='" + followNames + '\'' +
                '}';
    }
}
