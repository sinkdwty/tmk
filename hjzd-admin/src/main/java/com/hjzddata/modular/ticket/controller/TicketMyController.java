package com.hjzddata.modular.ticket.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.config.properties.HjzdProperties;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.shiro.ShiroUser;
import com.hjzddata.core.util.DateUtil;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.custom.model.Custom;
import com.hjzddata.modular.custom.service.ICustomService;
import com.hjzddata.modular.system.model.Dept;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IDeptService;
import com.hjzddata.modular.system.service.IUserService;
import com.hjzddata.modular.ticket.model.Ticket;
import com.hjzddata.modular.ticket.model.TicketNote;
import com.hjzddata.modular.ticket.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 工单管理
 *
 * @author yaoliang <yaoliang@hjzddata.com>
 * @since 2018年8月2日14:13:41
 */
@Controller
@RequestMapping("/ticket_my")
public class TicketMyController extends BaseController {
    private String PREFIX = "/ticket/ticket_my/";

    @Autowired
    private ITicketService ticketService;

    @Autowired
    private ICustomService customService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private HjzdProperties hjzdProperties;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return PREFIX + "index.html";
    }

    /**
     * 列表查询
     *
     * @param title
     * @param beginTime
     * @param endTime
     * @param type
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object ticketlist(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String column,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer type
    ) {
        Integer assignUserId;
        if (type != null && type != 2) {
            assignUserId = ShiroKit.getUser().getId();
        } else {
            assignUserId = null;
        }

        Page<Ticket> page = new PageFactory<Ticket>().defaultPage();
        List tickets = ticketService.selectPage(page, type, title,column, beginTime, endTime, assignUserId, page.getOrderByField(), page.isAsc());

        HashMap<String, Object> result = new HashMap<>(3);

        result.put("data", tickets);
        result.put("count", page.getTotal());
        result.put("code", 0);

        return result;
    }

    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/ticket_edit")
    public Object ticketEdit(@RequestParam(required = true) Integer id, Model model) {
        Ticket ticket = ticketService.selectById(id);
        if (ToolUtil.isEmpty(ticket)) {
            return "/404.html";
        }

        if (ticket.getCreateUserId() == null || !ticket.getCreateUserId().equals(ShiroKit.getUser().getId()) ) {
            return "/404.html";
        }

        model.addAttribute("ticket", ticket);
        List depts = deptService.selectList(new EntityWrapper<Dept>().eq("id", ShiroKit.getUser().getDeptId()));
        model.addAttribute("depts", depts);
        return PREFIX + "ticket_edit.html";
    }

    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/ticket_view")
    public Object ticketView(@RequestParam(required = true) Integer id, Model model) {
        Ticket ticket = ticketService.selectById(id);
        if (ToolUtil.isEmpty(ticket)) {
            return "/404.html";
        }

        if (ticket.getCreateUserId() == null || !ticket.getCreateUserId().equals(ShiroKit.getUser().getId()) ) {
            return "/404.html";
        }
        List depts = deptService.selectList(new EntityWrapper<Dept>());
        model.addAttribute("depts", depts);
        model.addAttribute("ticket", ticket);
        return PREFIX + "ticket_view.html";
    }

    /**
     * 工单记录
     *
     * @param ticketId
     * @param note
     * @return
     */
    @RequestMapping("/ticket_note")
    @ResponseBody
    public Object ticketNote(@RequestParam(required = true) Integer ticketId, @RequestParam(required = true) String note) {
        if (note != null && ticketId != null) {
            TicketNote ticketNote = new TicketNote();
            ticketNote.setUserName(ShiroKit.getUser().getName());
            ticketNote.setUserId(ShiroKit.getUser().getId());
            ticketNote.setCreatedAt(DateUtil.getTime());
            ticketNote.setType(TicketNote.TYPE_NOTE);
            ticketNote.setNote(note);
            ticketNote.setTicketId(ticketId);
            ticketNote.insert();
            return  new JsonResult(200, "操作成功", null);
        }

        return  new JsonResult(201, "操作失败", null);
    }

    /**
     * 修改状态
     *
     * @param ticketId
     * @param status
     * @return
     */
    @RequestMapping("/ticket_status")
    @ResponseBody
    @Transactional
    public Object ticketStatus(@RequestParam(required = true) Integer ticketId, @RequestParam(required = true) Integer status) {
        if (status != null && ticketId != null) {
            Ticket ticket = this.ticketService.selectById(ticketId);

            if (ticket != null) {
                String statusName = Ticket.getStatusByKeyToName(ticket.getStatus());
                ticket.setStatus(status);
                ticket.setUpdatedAt(DateUtil.getTime());
                ticket.updateById();

                String note = "状态: " + statusName + "---->" + Ticket.getStatusByKeyToName(status);
                TicketNote ticketNote = new TicketNote();
                ticketNote.setUserName(ShiroKit.getUser().getName());
                ticketNote.setUserId(ShiroKit.getUser().getId());
                ticketNote.setCreatedAt(DateUtil.getTime());
                ticketNote.setType(TicketNote.TYPE_NOTE);
                ticketNote.setNote(note);
                ticketNote.setTicketId(ticketId);
                if(ticketNote.insert()) {
                    return new JsonResult(200, "操作成功", null);
                }
            }
        }
        return new JsonResult(201, "操作失败", null);
    }

    /**
     * 列表查询
     * @return
     */
    @RequestMapping("/ticket_note_list")
    @ResponseBody
    public Object ticketNotelist(@RequestParam(required = false) Integer type, @RequestParam(required = true) Integer ticketId) {
        EntityWrapper wrapper = new EntityWrapper<TicketNote>();
        wrapper.eq("ticket_id", ticketId);
        wrapper.orderBy("id", false);
        if (type != null) {
            wrapper.eq("type", type);
        }
        TicketNote ticketNote = new TicketNote();
        HashMap<String, Object> result = new HashMap<>(3);
        try {
            Page<TicketNote> page = new PageFactory<TicketNote>().defaultPage();
            ticketNote.selectPage(page, wrapper);

            result.put("data", page.getRecords());
            result.put("code", 0);
            result.put("count", page.getTotal());
        } catch (Exception e) {
            result.put("data", null);
            result.put("code", 0);
            result.put("count", 0);
        }
        return result;
    }

    @RequestMapping("/ticket_add")
    public Object ticketAdd(Model model) {
        List depts = deptService.selectList(new EntityWrapper<Dept>().eq("id", ShiroKit.getUser().getDeptId()));
        model.addAttribute("depts", depts);
        return PREFIX + "ticket_add.html";
    }

    /**
     * 保存
     *
     * @param ticket
     * @return
     */
    @RequestMapping("/ticket_save")
    @ResponseBody
    @Transactional
    public Object ticketSave(Ticket ticket) {
        if (ticket.getAssignUserId() == null) {
            ticket.setAssignUserName("");
            ticket.setAssignUserId(null);
        }

        if (ticket.getCustomId() == null) {
            ticket.setCustomName("");
            ticket.setCustomId(null);
        }

        Boolean res = false;
        if (ticket.getId() != null) {
            Ticket ticketOld = ticketService.selectById(ticket.getId());

            if (ticketOld != null) {
                String note = "";
                TicketNote ticketNote = new TicketNote();
                if (ticketOld.getTitle() != null && !ticketOld.getTitle().equals(ticket.getTitle())) {
                    note += "标题: " + ticketOld.getTitle() + "---->" + ticket.getTitle() + "\n";
                }
                //if (!ticketOld.getContent().equals(ticket.getContent())) {
                // note += "描述: " + ticketOld.getContent() + "---->" + ticket.getContent()+ "\n";
                //}
                if (ticketOld.getCustomId() != null && !ticketOld.getCustomId().equals(ticket.getCustomId())) {
                    note += "客户名称: " + ticketOld.getCustomName() + "---->" + ticket.getCustomName()+ "\n";
                }
                if (ticketOld.getStatus() != null && !ticketOld.getStatus().equals(ticket.getStatus())) {
                    String statusName = Ticket.getStatusByKeyToName(ticket.getStatus());
                    note += "状态: " + statusName + "---->" + Ticket.getStatusByKeyToName(ticket.getStatus())+ "\n";
                }
                if (ticketOld.getLevels() != null && !ticketOld.getLevels().equals(ticket.getLevels())) {
                    String levelName = Ticket.getLevelKeyToName(ticket.getLevels());
                    note += "优先级: " + levelName + "---->" + Ticket.getLevelKeyToName(ticket.getLevels())+ "\n";
                }
                if (ticketOld.getTags() != null && !ticketOld.getTags().equals(ticket.getTags())) {
                    note += "标签: " + ticketOld.getTags() + "---->" + ticket.getTags()+ "\n";
                }
                if (ticketOld.getAssignUserId() != null && !ticketOld.getAssignUserId().equals(ticket.getAssignUserId())) {
                    note += "受理人: " + ticketOld.getAssignUserName() + "---->" + ticket.getAssignUserName()+ "\n";
                }
                if (ticketOld.getAssignDeptId() != null && !ticketOld.getAssignDeptId().equals(ticket.getAssignDeptId())) {
                    note += "受理部门: " + ticketOld.getAssignDeptName() + "---->" + ticket.getAssignDeptName()+ "\n";
                }
                if (ticketOld.getAccessory() != null && !ticketOld.getAccessory().equals(ticket.getAccessory())) {
                    String oldAccessory = "";
                    String accessory = "";
                    if (ticketOld.getAccessory() != null) {
                        oldAccessory = getAccessoryToSimpleFileName(ticket.getAccessory());
                    }
                    if (ticket.getAccessory() != null) {
                        accessory = getAccessoryToSimpleFileName(ticket.getAccessory());
                    }
                    note += "附件: " + oldAccessory + "---->" + accessory + "\n";
                }
                if (ticketOld.getFollows() != null && !ticketOld.getFollows().equals(ticket.getFollows())) {
                    note += "关注人: " + ticketOld.getFollowNames() + "---->" + ticket.getFollowNames() + "\n";
                }
                if (note != null) {
                    ticketNote.setUserName(ShiroKit.getUser().getName());
                    ticketNote.setUserId(ShiroKit.getUser().getId());
                    ticketNote.setCreatedAt(DateUtil.getTime());
                    ticketNote.setType(TicketNote.TYPE_NOTE);
                    ticketNote.setNote(note.trim());
                    ticketNote.setTicketId(ticket.getId());
                    ticketNote.insert();
                }
            }
            ticket.setUpdatedAt(DateUtil.getTime());
            res = ticket.updateById();
        } else {
            ticket.setCreateUserName(ShiroKit.getUser().getName());
            ticket.setCreateUserId(ShiroKit.getUser().getId());
            ticket.setCreatedAt(DateUtil.getTime());
            ticket.setUpdatedAt(DateUtil.getTime());
            ticket.setTicketFrom("手动录入");
            res = ticket.insert();
        }
        if (res) {
            return  new JsonResult(200, "操作成功", ticket);
        }
        return  new JsonResult(201, "操作失败", ticket);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/ticket_delete")
    @ResponseBody
    public Object ticketDelete(@RequestParam(required = true) Integer id) {
        if (ticketService.deleteById(id)) {
            return  new JsonResult(200, "删除成功", null);
        }

        return new JsonResult(201, "删除失败", null);
    }

    /**
     * 导入页面
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/ticket_import")
    public String ticketImport() {
        return  PREFIX + "ticket_import.html";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/ticket_import")
    @ResponseBody
    public Object ticketImport(@RequestParam(required = true) String filelink) {
        try {
            ShiroUser shiroUser = ShiroKit.getUser();
            Collection<Map> list = ticketService.parseExcel(hjzdProperties.getFileUploadPath() + filelink);
            List<Ticket> tickets = new ArrayList();
            EntityWrapper<Custom> customEntityWrapper = new EntityWrapper<Custom>();
            EntityWrapper<User> userEntityWrapper = new EntityWrapper<User>();
            EntityWrapper<Dept> deptEntityWrapper = new EntityWrapper<Dept>();
            Integer errorNum = 0;
            Integer successNum = 0;
            Integer totals = 0;
            for (Map map : list) {
                System.out.println(map);
                Ticket ticket = new Ticket();

                ticket.setCreateUserName(shiroUser.getName());
                ticket.setCreateUserId(shiroUser.getId());

                Wrapper<Custom> customWrapper = customEntityWrapper.clone();

                if ( map.get("客户姓名") != null &&  map.get("客户手机") != null) {
                    customWrapper.where("custom_name='" + map.get("客户姓名") + "' AND phone='" + map.get("客户手机") + "'");
                    Custom custom = customService.selectOne(customWrapper);

                    if (custom != null) {
                        ticket.setCustomName(custom.getCustomName());
                        ticket.setCustomId(custom.getId());
                        if (map.get("受理坐席") != null) {
                            Wrapper<User> userWrapper = userEntityWrapper.clone();
                            userWrapper.where("account = '" + map.get("受理坐席") + "'");
                            User user = userService.selectOne(userWrapper);
                            ticket.setAssignUserName(user.getName());
                            ticket.setAssignUserId(user.getId());

                            Integer deptid = user.getDeptid();
                            Dept dept = deptService.selectById(deptid);
                            ticket.setAssignDeptName(dept.getFullname());
                            ticket.setAssignDeptId(dept.getId());
                        } else if(map.get("受理坐席组") != null) {
                            Wrapper<Dept> deptWrapper = deptEntityWrapper.clone();
                            deptWrapper.where("fullname = '" + map.get("受理坐席组") + "'");
                            Dept dept  = deptService.selectOne(deptWrapper);
                            ticket.setAssignDeptName(dept.getFullname());
                            ticket.setAssignDeptId(dept.getId());
                        }

                        ticket.setTicketFrom("系统导入");
                        ticket.setTitle((String) map.get("工单标题"));
                        ticket.setContent((String) map.get("工单描述"));
                        ticket.setFollows((String) map.get("关注者"));
                        ticket.setTags((String) map.get("标签"));
                        ticket.setStatus(Ticket.getStatusByName((String) map.get("状态")));
                        ticket.setLevels(Ticket.getLevelByName((String) map.get("优先级")));
                        ticket.setUpdatedAt(DateUtil.getTime());
                        ticket.setCreatedAt(DateUtil.getTime());
                        System.out.println("-----------");
                        System.out.println(ticket);
                        tickets.add(ticket);
                        successNum ++;
                    } else {
                        errorNum ++;
                        return new JsonResult(201, "操作失败：系统不存在客户 " + map.get("客户姓名") + " - 手机号 " + map.get("客户手机"), null);
                    }
                } else {
                    errorNum ++;
                    return new JsonResult(201, "操作失败：第" + (totals + 1) + "记录，客户姓名+客户手机不能为空", null);
                }
                totals ++;
            }
            if (tickets.size() > 0) {
                if (ticketService.insertBatch(tickets)) {
                    HashMap map = new HashMap(3);
                    map.put("success", successNum);
                    map.put("error", errorNum);
                    map.put("totals", totals);
                    return new JsonResult(200, "操作成功", map);
                } else {
                    return new JsonResult(201, "操作失败", null);
                }
            } else {
                return new JsonResult(201, "操作失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(201, "操作失败", null);
        }
    }

    /**
     * 获取用户
     *
     * @param keyword
     * @return
     */
    @RequestMapping("/user_search")
    @ResponseBody
    public Object userSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String follows,
            @RequestParam(required = false) String deptId
    ) {
        HashMap<String, Object> result = new HashMap<>(3);
        EntityWrapper wrapper = new EntityWrapper<User>();
        if (keyword != null) {
            wrapper.like("name", keyword);
        }
        if (deptId != null) {
            wrapper.eq("deptid", deptId);
        }
        Page<User> page = new PageFactory<User>().defaultPage();
        userService.selectPage(page, wrapper);
        List<User> records = page.getRecords();
        List data = new ArrayList();
        String[] followsArray = new String[0];
        if (follows != null) {
            followsArray = follows.split(",");
        }

        for (User u : records) {
            HashMap<String, Object> map = new HashMap<>(3);
            map.put("name", u.getName());
            map.put("value", u.getId());
            if (followsArray.length > 0) {
                if (Arrays.asList(followsArray).contains(u.getId().toString())) {
                    map.put("LAY_CHECKED", true);
                }
            }

            data.add(map);
        }
        result.put("data", data);
        result.put("count", page.getTotal());
        result.put("code", 0);
        return result;
    }

    /**
     * 获取客户
     *
     * @param keyword
     * @return
     */
    @RequestMapping("/custom_search")
    @ResponseBody
    public Object customSearch(@RequestParam(required = false) String keyword, @RequestParam(required = false) String deptId) {
        HashMap<String, Object> result = new HashMap<>(3);
        EntityWrapper wrapper = new EntityWrapper<Custom>();
        if (keyword != null) {
            wrapper.like("custom_name", keyword);
        }
        if (deptId != null) {
            wrapper.eq("company_id", deptId);
        }
        Page<Custom> page = new PageFactory<Custom>().defaultPage();
        customService.selectPage(page, wrapper);
        List<Custom> records = page.getRecords();
        List data = new ArrayList();
        for (Custom u : records) {
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("name", u.getCustomName());
            map.put("value", u.getId());
            data.add(map);
        }
        result.put("data", data);
        result.put("count", page.getTotal());
        result.put("code", 0);
        return result;
    }

    /**
     * 选择客户页面
     *
     * @return
     */
    @RequestMapping("/select_custom")
    public String selectCustom(@RequestParam(required = false) String deptId, Model model) {
        model.addAttribute("deptId", deptId);
        return PREFIX + "select_custom.html";
    }

    /**
     * 选择用户页面
     *
     * @return
     */
    @RequestMapping("/select_user")
    public String selectUser(@RequestParam(required = false) String deptId, Model model) {
        model.addAttribute("deptId", deptId);
        return PREFIX + "select_user.html";
    }

    /**
     * 选择关注人页面
     *
     * @return
     */
    @RequestMapping("/select_follows")
    public String selectFollows(@RequestParam(required = false) String deptId, @RequestParam(required = false) String follows, Model model) {
        model.addAttribute("deptId", deptId);
        model.addAttribute("follows", follows);
        return PREFIX + "select_follows.html";
    }

    private String getAccessoryToSimpleFileName(String str) {
        String accessory = "";
        if (str != null) {
            String[] ticketAccessorys = str.split(",");
            if (ticketAccessorys.length > 0) {
                for(int i = 0; i < ticketAccessorys.length; i++) {
                    if (ticketAccessorys[i] != null) {
                        String[] fileNames = ticketAccessorys[i].split("/");
                        accessory += fileNames[fileNames.length-1] + ",";
                    }
                }
            }
        }
        return  accessory;
    }
}
