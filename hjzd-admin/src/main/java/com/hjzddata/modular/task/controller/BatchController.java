package com.hjzddata.modular.task.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.modular.task.warpper.BatchWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.hjzddata.modular.task.model.Batch;
import com.hjzddata.modular.task.service.IBatchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据批次控制器
 *
 * @author fengshuonan
 * @Date 2018-09-17 14:57:32
 */
@Controller
@RequestMapping("/batch")
public class BatchController extends BaseController {

    private String PREFIX = "/task/batch/";

    @Autowired
    private IBatchService batchService;

    /**
     * 跳转到数据批次首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "batch.html";
    }

    /**
     * 跳转到添加数据批次
     */
    @RequestMapping("/batch_add")
    public String batchAdd() {
        return PREFIX + "batch_add.html";
    }

    /**
     * 跳转到修改数据批次
     */
    @RequestMapping("/batch_update/{batchId}")
    public String batchUpdate(@PathVariable Integer batchId, Model model) {
        Batch batch = batchService.selectById(batchId);
        model.addAttribute("item",batch);
        LogObjectHolder.me().set(batch);
        return PREFIX + "batch_edit.html";
    }

    /**
     * 获取数据批次列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String batch_no, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime) {
        Page<Batch> page = new PageFactory<Batch>().defaultPage();
        List result = batchService.selectBatchs(page, batch_no, beginTime,endTime);
        page.setRecords((List<Batch>) new BatchWarpper(result).warp());
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",result);
        return list;
    }

    /**
     * 新增数据批次
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Batch batch) {
        batchService.insert(batch);
        return SUCCESS_TIP;
    }

    /**
     * 删除数据批次
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer batchId) {
        batchService.deleteById(batchId);
        return SUCCESS_TIP;
    }

    /**
     * 修改数据批次
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Batch batch) {
        batchService.updateById(batch);
        return SUCCESS_TIP;
    }

    /**
     * 数据批次详情
     */
    @RequestMapping(value = "/detail/{batchId}")
    @ResponseBody
    public Object detail(@PathVariable("batchId") Integer batchId) {
        return batchService.selectById(batchId);
    }
}
