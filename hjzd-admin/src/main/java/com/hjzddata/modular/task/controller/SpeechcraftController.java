package com.hjzddata.modular.task.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.SpeechcraftDict;
import com.hjzddata.modular.task.service.ISpeechcraftService;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.base.tips.Tip;
import com.hjzddata.core.common.annotion.Permission;
import com.hjzddata.core.common.constant.Const;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.task.model.Speechcraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 项目管理控制器
 */
@Controller
@RequestMapping("/speechcraft")
public class SpeechcraftController extends BaseController {

    private static String PREFIX = "/task/speechcraft/";

    @Autowired
    private ISpeechcraftService speechcraftService;

    /**
     * 跳转到查看项目列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "speechcraft.html";
    }

    /**
     * 跳转到新建项目列表的页面
     */
    @RequestMapping("/to_add")
    public String addView() {
        return PREFIX + "speechcraft_add.html";
    }

    /**
     * 跳转到编辑项目信息页面
     */
    @Permission
    @RequestMapping("/to_edit/{speechId}")
    public String policyEdit(@PathVariable Integer speechId, Model model) {
        if (ToolUtil.isEmpty(speechId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        Speechcraft speechcraft = this.speechcraftService.selectById(speechId);
        model.addAttribute(speechcraft);
        model.addAttribute("contents", speechcraft.getContents());
        model.addAttribute("note", speechcraft.getNote());
        LogObjectHolder.me().set(speechcraft);
        return PREFIX + "speechcraft_edit.html";
    }

    /**
     * 查询项目列表
     */
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime) {

        Page<Speechcraft> page = new PageFactory<Speechcraft>().defaultPage();
        List result = speechcraftService.selectSpeechs(page, name, beginTime, endTime, page.getOrderByField(), page.isAsc());
        page.setRecords(result);
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",page.getRecords());
        return list;
    }

    /**
     * 添加话术
     */
    @BussinessLog(value = "添加话术", key = "contents", dict = SpeechcraftDict.class)
    @RequestMapping("/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip add(@Valid Speechcraft speech, BindingResult result) {
        if (result.hasErrors()) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

//        // 判断账号是否重复
//        Policy thePolicy = speechcraftService.getByName(policy.getPolicyName());
//        if (thePolicy != null) {
//            throw new HjzdException(BizExceptionEnum.PRODUCT_ALREADY_REG);
//        }

        // 完善账号信息
        speech.setCreatedAt(new Date());
        speech.setUpdatedAt(new Date());

        this.speechcraftService.insert(speech);
        return SUCCESS_TIP;
    }

    /**
     * 修改话术
     *
     */
    @BussinessLog(value = "修改话术", key = "id", dict = SpeechcraftDict.class)
    @RequestMapping("/edit")
    @ResponseBody
    public Tip edit(@Valid Speechcraft speech, BindingResult result) {
        if (result.hasErrors()) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        // 完善策略信息
        speech.setUpdatedAt(new Date());

        this.speechcraftService.updateById(speech);
        return SUCCESS_TIP;
    }

    /**
     * 删除话术（逻辑删除）
     */
    @BussinessLog(value = "删除话术", key = "speechId", dict = SpeechcraftDict.class)
    @RequestMapping("/delete")
    @Permission
    @ResponseBody
    public Tip delete(@RequestParam Integer speechId) {
        if (ToolUtil.isEmpty(speechId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        this.speechcraftService.deleteById(speechId);
        return SUCCESS_TIP;
    }

    /**
     * 禁用/启用话术
     */
    @BussinessLog(value = "禁用/启用话术", key = "speechId", dict = SpeechcraftDict.class)
    @RequestMapping("/updateStatus")
    @Permission
    @ResponseBody
    public Tip updateStatus(@RequestParam Integer speechId) {
        if (ToolUtil.isEmpty(speechId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        Speechcraft speech = this.speechcraftService.selectById(speechId);
        if(speech.getStatus() == 1) {
            this.speechcraftService.setStatus(speechId, 0);
        } else {
            this.speechcraftService.setStatus(speechId, 1);
        }

        return SUCCESS_TIP;
    }

    /**
     * 获取部门的tree列表
     */
    @RequestMapping(value = "/selectList")
    @ResponseBody
    public List<Map> selectList() {
        List<Map> selectList = this.speechcraftService.selectList();
        return selectList;
    }
}

