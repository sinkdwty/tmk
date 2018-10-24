package com.hjzddata.modular.task.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.PolicyDict;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.task.service.IPolicyService;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.base.tips.Tip;
import com.hjzddata.core.common.annotion.Permission;
import com.hjzddata.core.common.constant.Const;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.task.model.Policy;
import com.hjzddata.modular.task.service.ISpeechcraftService;
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
@RequestMapping("/policy")
public class PolicyController extends BaseController {

    private static String PREFIX = "/task/policy/";

    @Autowired
    private IPolicyService policyService;

    @Autowired
    private ISpeechcraftService speechcraftService;

    /**
     * 跳转到查看项目列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "policy.html";
    }

    /**
     * 跳转到新建项目列表的页面
     */
    @RequestMapping("/policy_add")
    public String addView(Model model) {
        model.addAttribute("selectList",speechcraftService.selectList());
        return PREFIX + "policy_add.html";
    }

    /**
     * 跳转到编辑项目信息页面
     */
    @Permission
    @RequestMapping("/policy_edit/{policyId}")
    public String policyEdit(@PathVariable Integer policyId, Model model) {
        if (ToolUtil.isEmpty(policyId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        Policy policy = this.policyService.selectById(policyId);
        model.addAttribute(policy);
        model.addAttribute("selectList",speechcraftService.selectList());
        LogObjectHolder.me().set(policy);
        return PREFIX + "policy_edit.html";
    }

    /**
     * 查询项目列表
     */
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime) {

        Page<Policy> page = new PageFactory<Policy>().defaultPage();
        List result = policyService.selectPolicies(page, name, beginTime, endTime, page.getOrderByField(), page.isAsc());
        page.setRecords(result);
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",page.getRecords());
        return list;
    }

    /**
     * 添加策略
     */
    @BussinessLog(value = "添加策略", key = "policyName", dict = PolicyDict.class)
    @RequestMapping("/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object add(@Valid Policy policy, BindingResult result) {
        if (result.hasErrors()) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

//        // 判断账号是否重复
//        Policy thePolicy = policyService.getByName(policy.getPolicyName());
//        if (thePolicy != null) {
//            throw new HjzdException(BizExceptionEnum.PRODUCT_ALREADY_REG);
//        }

        /* 判断是否存在相同的策略，如果不存在相同的策略则可以添加该策略，如果存在则不能添加策略 */
        List resultExistence = policyService.policyExistence(policy.getPolicyName(),policy.getId());
        if (resultExistence.isEmpty()) {
            // 完善账号信息
            policy.setCreatedAt(new Date());
            policy.setUpdatedAt(new Date());

            this.policyService.insert(policy);
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同策略，请更换策略名称!", resultExistence);
            return jsonResult;
        }

    }

    /**
     * 修改策略
     *
     */
    @BussinessLog(value = "修改策略", key = "id", dict = PolicyDict.class)
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Policy policy, BindingResult result) {
        if (result.hasErrors()) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

//        // 判断策略是否重复
//        Policy thePolicy = policyService.getByName(policy.getPolicyName());
//        if (thePolicy != null && thePolicy.getId() != policy.getId()) {
//            throw new HjzdException(BizExceptionEnum.PRODUCT_ALREADY_REG);
//        }

        /* 判断是否存在相同的策略，如果不存在相同的策略则可以添加该策略，如果存在则不能添加策略 */
        List resultExistence = policyService.policyExistence(policy.getPolicyName(),policy.getId());

        if (resultExistence.isEmpty()) {
            // 完善策略信息
            policy.setUpdatedAt(new Date());

            this.policyService.updateById(policy);
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同策略，请更换策略名称!", resultExistence);
            return jsonResult;
        }



    }

    /**
     * 删除策略（逻辑删除）
     */
    @BussinessLog(value = "删除策略", key = "policyId", dict = PolicyDict.class)
    @RequestMapping("/delete")
    @Permission
    @ResponseBody
    public Tip delete(@RequestParam Integer policyId) {
        if (ToolUtil.isEmpty(policyId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        this.policyService.deleteById(policyId);
        return SUCCESS_TIP;
    }

    /**
     * 禁用策略
     */
    @BussinessLog(value = "禁用/启用策略", key = "policyId", dict = PolicyDict.class)
    @RequestMapping("/updateStatus")
    @Permission
    @ResponseBody
    public Tip updateStatus(@RequestParam Integer policyId) {
        if (ToolUtil.isEmpty(policyId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        Policy policy = this.policyService.selectById(policyId);
        if(policy.getStatus() == 1) {
            this.policyService.setStatus(policyId, 0);
        } else {
            this.policyService.setStatus(policyId, 1);
        }

        return SUCCESS_TIP;
    }
}

