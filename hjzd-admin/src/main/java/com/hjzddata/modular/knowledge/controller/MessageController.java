package com.hjzddata.modular.knowledge.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.base.tips.Tip;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.annotion.Permission;
import com.hjzddata.core.common.constant.dictmap.MessageDict;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.common.model.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.hjzddata.modular.knowledge.model.Message;
import com.hjzddata.modular.knowledge.service.IMessageService;

import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 短信模板管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-16 16:29:44
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    private String PREFIX = "/knowledge/message/";

    @Autowired
    private IMessageService messageService;

    /**
     * 跳转到短信模板管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "message.html";
    }

    /**
     * 跳转到添加短信模板管理
     */
    @RequestMapping("/to_add")
    public String messageAdd() {
        return PREFIX + "message_add.html";
    }

    /**
     * 跳转到修改短信模板管理
     */
    @RequestMapping("/to_edit/{messageId}")
    public String messageUpdate(@PathVariable Integer messageId, Model model) {
        Message message = messageService.selectById(messageId);
        model.addAttribute("item",message);
        LogObjectHolder.me().set(message);
        return PREFIX + "message_edit.html";
    }

    /**
     * 获取短信模板管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "part",required = false) String part,@RequestParam(value = "startTime",required = false) String startTime,@RequestParam(value = "endTime",required = false) String endTime) {
//        return messageService.selectList(null);
        Page<Message> page = new PageFactory<Message>().defaultPage();
        List result = messageService.selectMessage(page, part, startTime,endTime);
        page.setRecords(result);
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",page.getRecords());
        return list;
    }

    /**
     * 新增短信模板管理
     */
    @BussinessLog(value = "添加短信模板", key = "message", dict = MessageDict.class)
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Message message) {
        /* 判断数据库中是否存在相同的短信模板，如果不存在，则可以创建或更新，否则返回存在相同的短信模板 */
        List result = messageService.messageExistence(message.getMessage(), message.getId());
        if (result.isEmpty()) {
            message.setCreatedAt(new Date());
            message.setUpdatedAt(new Date());
            messageService.insert(message);
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同短信模板，请更换短信内容!", result);
            return jsonResult;
        }

    }

    /**
     * 删除短信模板管理
     */
    @BussinessLog(value = "删除短信模板", key = "messageId", dict = MessageDict.class)
    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public Object delete(@RequestParam Integer messageId) {
        if (ToolUtil.isEmpty(messageId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        messageService.deleteById(messageId);
        return SUCCESS_TIP;
    }

    /**
     * 修改短信模板管理
     */
    @BussinessLog(value = "修改短信模板", key = "id", dict = MessageDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Message message) {
        /* 判断数据库中是否存在相同的短信模板，如果不存在，则可以创建或更新，否则返回存在相同的短信模板 */
        List result = messageService.messageExistence(message.getMessage(), message.getId());
        if (result.isEmpty()) {
            message.setUpdatedAt(new Date());
            messageService.updateById(message);
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同短信模板，请更换短信内容!", result);
            return jsonResult;
        }
    }

    /**
     * 禁用策略
     */
    @BussinessLog(value = "禁用/启用短信模板", key = "messageId", dict = MessageDict.class)
    @RequestMapping("/updateStatus")
    @Permission
    @ResponseBody
    public Tip updateStatus(@RequestParam Integer messageId) {
        if (ToolUtil.isEmpty(messageId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        Message message = this.messageService.selectById(messageId);
        if(message.getStatus() == 1) {
            this.messageService.editStatus(messageId, 0);
        } else {
            this.messageService.editStatus(messageId, 1);
        }

        return SUCCESS_TIP;
    }
    }
