package com.hjzddata.modular.system.controller;

import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.CallConfigDict;
import com.hjzddata.modular.system.warpper.CallConfigWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.hjzddata.modular.system.model.CallConfig;
import com.hjzddata.modular.system.service.ICallConfigService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外呼管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-17 13:59:10
 */
@Controller
@RequestMapping("/callConfig")
public class CallConfigController extends BaseController {

    private String PREFIX = "/system/callConfig/";

    @Autowired
    private ICallConfigService callConfigService;

    /**
     * 跳转到外呼管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "callConfig.html";
    }

    /**
     * 跳转到添加外呼管理
     */
    @RequestMapping("/callConfig_add")
    public String callConfigAdd() {
        return PREFIX + "callConfig_add.html";
    }

    /**
     * 跳转到修改外呼管理
     */
    @RequestMapping("/callConfig_update/{callConfigId}")
    public String callConfigUpdate(@PathVariable Integer callConfigId, Model model) {
        CallConfig callConfig = callConfigService.selectById(callConfigId);
        model.addAttribute("item",callConfig);
        LogObjectHolder.me().set(callConfig);
        return PREFIX + "callConfig_edit.html";
    }

    /**
     * 获取外呼管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list() {
        /*List<CallConfig> data = callConfigService.selectList(null);
        Object table = new CallConfigWarpper(data).warp();*/
        List<Map<String, Object>> data = this.callConfigService.list();
        Object tableData = super.warpObject(new CallConfigWarpper(data));
        Map<String, Object> list = new HashMap<>();
        list.put("code", 0);
        list.put("data", tableData);
        return list;
    }

    /**
     * 新增外呼管理
     */
    @BussinessLog(value = "新增外呼配置", key = "baseName", dict = CallConfigDict.class)
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CallConfig callConfig) {
        callConfig.setCreatedAt(new Date());
        callConfig.setUpdatedAt(new Date());
        callConfigService.insert(callConfig);
        return SUCCESS_TIP;
    }

    /**
     * 删除外呼管理
     */
    @BussinessLog(value = "删除外呼配置", key = "id", dict = CallConfigDict.class)
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        callConfigService.deleteById(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改外呼管理
     */
    @BussinessLog(value = "修改外呼配置", key = "id", dict = CallConfigDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CallConfig callConfig) {
        callConfig.setUpdatedAt(new Date());
        callConfigService.updateById(callConfig);
        return SUCCESS_TIP;
    }

    /**
     * 外呼管理详情
     */
    @RequestMapping(value = "/detail/{callConfigId}")
    @ResponseBody
    public Object detail(@PathVariable("callConfigId") Integer callConfigId) {
        return callConfigService.selectById(callConfigId);
    }
}
