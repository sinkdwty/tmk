package com.hjzddata.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.system.warpper.DictWarpper;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.annotion.Permission;
import com.hjzddata.core.common.constant.Const;
import com.hjzddata.core.common.constant.dictmap.DictMap;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典控制器
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private String PREFIX = "/system/dict/";

    @Autowired
    private IDictService dictService;

    /**
     * 跳转到字典管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dict.html";
    }

    /**
     * 跳转到添加字典
     */
    @RequestMapping("/dict_add")
    public String deptAdd() {
        return PREFIX + "dict_add.html";
    }

    /**
     * 跳转到修改字典
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping("/dict_edit/{dictId}")
    public String deptUpdate(@PathVariable Integer dictId, Model model) {
        Dict dict = dictService.selectById(dictId);
        model.addAttribute("dict", dict);
        List<Dict> subDicts = dictService.selectList(new EntityWrapper<Dict>().eq("pid", dictId));
        model.addAttribute("subDicts", subDicts);
        LogObjectHolder.me().set(dict);
        return PREFIX + "dict_edit.html";
    }

    /**
     * 新增字典
     *
     * @param dictValues 格式例如   "1:启用;2:禁用;3:冻结"
     */
    @BussinessLog(value = "添加字典记录", key = "dictName,dictValues", dict = DictMap.class)
    @RequestMapping(value = "/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object add(String dictName, String code, String dictValues) {
        if (ToolUtil.isOneEmpty(dictName, code, dictValues)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        this.dictService.addDict(dictName, code, dictValues);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有字典列表
     */
    @RequestMapping(value = "/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(String condition) {
//        List<Map<String, Object>> dictList = this.dictService.list(condition);

        Page<Dict> page = new PageFactory<Dict>().defaultPage();
        List result = dictService.getDict(page, condition);
        page.setRecords((List<Dict>) new DictWarpper(result).warp());
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",result);
        return list;
    }

    /**
     * 字典详情
     */
    @RequestMapping(value = "/detail/{dictId}")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object detail(@PathVariable("dictId") Integer dictId) {
        return dictService.selectById(dictId);
    }

    /**
     * 修改字典
     */
    @BussinessLog(value = "修改字典", key = "dictName,dictValues", dict = DictMap.class)
    @RequestMapping(value = "/update")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object update(Integer dictId, String dictName, String code, String dictValues) {
        if (ToolUtil.isOneEmpty(dictName)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_DICTNAME);
        }
        if (ToolUtil.isOneEmpty(dictId,code, dictValues)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        dictService.editDict(dictId, dictName, code, dictValues);
        return SUCCESS_TIP;
    }

    /**
     * 删除字典记录
     */
    @BussinessLog(value = "删除字典记录", key = "dictId", dict = DictMap.class)
    @RequestMapping(value = "/delete")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delete(@RequestParam Integer dictId) {

        //缓存被删除的名称
        LogObjectHolder.me().set(ConstantFactory.me().getDictName(dictId));

        this.dictService.delteDict(dictId);
        return SUCCESS_TIP;
    }

}
