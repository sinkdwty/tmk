package com.hjzddata.modular.productCallCode.controller;

import com.hjzddata.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.hjzddata.modular.productCallCode.model.ProductCallMap;
import com.hjzddata.modular.productCallCode.service.IProductCallMapService;

/**
 * 项目致电码控制器
 *
 * @author fengshuonan
 * @Date 2018-10-24 13:57:18
 */
@Controller
@RequestMapping("/productCallMap")
public class ProductCallMapController extends BaseController {

    private String PREFIX = "/productCallCode/productCallMap/";

    @Autowired
    private IProductCallMapService productCallMapService;

    /**
     * 跳转到项目致电码首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "productCallMap.html";
    }

    /**
     * 跳转到添加项目致电码
     */
    @RequestMapping("/productCallMap_add")
    public String productCallMapAdd() {
        return PREFIX + "productCallMap_add.html";
    }

    /**
     * 跳转到修改项目致电码
     */
    @RequestMapping("/productCallMap_update/{productCallMapId}")
    public String productCallMapUpdate(@PathVariable Integer productCallMapId, Model model) {
        ProductCallMap productCallMap = productCallMapService.selectById(productCallMapId);
        model.addAttribute("item",productCallMap);
        LogObjectHolder.me().set(productCallMap);
        return PREFIX + "productCallMap_edit.html";
    }

    /**
     * 获取项目致电码列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return productCallMapService.selectList(null);
    }

    /**
     * 新增项目致电码
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ProductCallMap productCallMap) {
        productCallMapService.insert(productCallMap);
        return SUCCESS_TIP;
    }

    /**
     * 删除项目致电码
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer productCallMapId) {
        productCallMapService.deleteById(productCallMapId);
        return SUCCESS_TIP;
    }

    /**
     * 修改项目致电码
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ProductCallMap productCallMap) {
        productCallMapService.updateById(productCallMap);
        return SUCCESS_TIP;
    }

    /**
     * 项目致电码详情
     */
    @RequestMapping(value = "/detail/{productCallMapId}")
    @ResponseBody
    public Object detail(@PathVariable("productCallMapId") Integer productCallMapId) {
        return productCallMapService.selectById(productCallMapId);
    }
}
