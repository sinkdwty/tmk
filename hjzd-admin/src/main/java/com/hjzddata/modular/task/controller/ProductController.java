package com.hjzddata.modular.task.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.ProductDict;
import com.hjzddata.core.support.HttpKit;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.productCallCode.model.ProductCallMap;
import com.hjzddata.modular.productCallCode.service.IProductCallMapService;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.task.service.IProductService;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.base.tips.Tip;
import com.hjzddata.core.common.annotion.Permission;
import com.hjzddata.core.common.constant.Const;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.task.model.Product;
import com.hjzddata.modular.task.warpper.ProductWarpper;
import net.sf.json.JSONArray;
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
@RequestMapping("/product")
public class ProductController extends BaseController {

    private static String PREFIX = "/task/product/";

    @Autowired
    private IProductService productService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IProductCallMapService productCallMapService;

    /**
     * 跳转到查看项目列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "product.html";
    }

    /**
     * 跳转到新建项目列表的页面
     */
    @RequestMapping("/product_add")
    public String addView() {
        return PREFIX + "product_add.html";
    }

    /**
     * 跳转到编辑项目信息页面
     */
    @Permission
    @RequestMapping("/product_edit/{productId}")
    public String productEdit(@PathVariable Integer productId, Model model) {
        if (ToolUtil.isEmpty(productId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        Product product = this.productService.selectById(productId);
        model.addAttribute(product);
        model.addAttribute("name", product.getName());
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(product.getBaseId()));
        LogObjectHolder.me().set(product);
        return PREFIX + "product_edit.html";
    }

    /**
     * 查询项目列表
     */
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer baseId) {

        Page<OperationLog> page = new PageFactory<OperationLog>().defaultPage();
        List<Map<String, Object>> products = productService.selectProducts(page,null, name, beginTime, endTime, baseId, page.getOrderByField(), page.isAsc());
        page.setRecords((List<OperationLog>) new ProductWarpper(products).warp());
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",page.getRecords());
        return list;
    }

    /**
     * 添加项目
     */
    @BussinessLog(value = "添加项目",  key = "name", dict = ProductDict.class)
    @RequestMapping("/add")
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip add(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        // 判断账号是否重复
        Product theProduct = productService.getByName(product.getName());
        if (theProduct != null) {
            throw new HjzdException(BizExceptionEnum.PRODUCT_ALREADY_REG);
        }

        // 完善账号信息
        product.setCreateAt(new Date());
        product.setUpdateAt(new Date());
        product.setCreateIp(HttpKit.getIp());
        product.setUpdateIp(HttpKit.getIp());

        this.productService.insert(product);
        return SUCCESS_TIP;
    }

    /**
     * 修改项目
     *
     */
    @BussinessLog(value = "修改项目",  key = "id", dict = ProductDict.class)
    @RequestMapping("/edit")
    @ResponseBody
    public Tip edit(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        // 判断账号是否重复
        Product theProduct = productService.getByName(product.getName());
        if (theProduct != null && theProduct.getId() != product.getId()) {
            throw new HjzdException(BizExceptionEnum.PRODUCT_ALREADY_REG);
        }

        // 完善账号信息
        product.setUpdateAt(new Date());
        product.setUpdateIp(HttpKit.getIp());

        this.productService.updateById(product);
        return SUCCESS_TIP;
    }

    /**
     * 删除项目（逻辑删除）
     */
    @BussinessLog(value = "删除项目",  key = "productId", dict = ProductDict.class)
    @RequestMapping("/delete")
    @Permission
    @ResponseBody
    public Tip delete(@RequestParam Integer productId) {
        if (ToolUtil.isEmpty(productId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        this.productService.deleteById(productId);
        return SUCCESS_TIP;
    }



    /**
     * 禁用项目
     */
    @BussinessLog(value = "禁用项目",  key = "id", dict = ProductDict.class)
    @RequestMapping("/freeze")
    @Permission
    @ResponseBody
    public Tip freeze(@RequestParam Integer id) {
        if (ToolUtil.isEmpty(id)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        this.productService.setStatus(id, 0);
        return SUCCESS_TIP;
    }

    /**
     * 解除禁用
     */
    @BussinessLog(value = "解除禁用项目",  key = "id", dict = ProductDict.class)
    @RequestMapping("/unfreeze")
    @Permission
    @ResponseBody
    public Tip unfreeze(@RequestParam Integer id) {
        if (ToolUtil.isEmpty(id)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        this.productService.setStatus(id,1);
        return SUCCESS_TIP;
    }

    @RequestMapping("/selectByDeptId")
    @ResponseBody
    public Object selectByDeptId(@RequestParam Integer deptId) {
        if(deptId == 0) {
            return new JsonResult(201, "抱歉该公司下暂无项目，请先创建项目", new Date());
        }

        EntityWrapper<Product> entityWrapper = new EntityWrapper<Product>();
        entityWrapper.and("base_id =" + deptId + " and is_del = 0");
        List<Product> depts = productService.selectList(entityWrapper);
        if (depts.isEmpty()) {
            return new JsonResult(201, "抱歉该公司下暂无项目，请先创建项目", new Date());
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("code", 200);
            hashMap.put("data", depts);
            return hashMap;
        }
    }

    /*
    设置致电结果码 页面
     */
    @RequestMapping("/set-code")
    public String setCallCode(@RequestParam(value = "productId") Integer productId, Model model) {
        EntityWrapper<ProductCallMap> entityWrapper = new EntityWrapper<>();
        entityWrapper.and("product_id=" + productId);
        List<ProductCallMap> list = productCallMapService.selectList(entityWrapper);
        JSONArray json  =  JSONArray.fromObject(list);
        model.addAttribute("codeList", json);
        model.addAttribute("productId", productId);
        return PREFIX + "set-code.html";
    }

    /**
     * 异步加载致电码
     * @return
     */
    @RequestMapping("/get-all-code")
    @ResponseBody
    public Object getAllCode() {
        //获取致电码用于页面数据渲染
        List<Dict> list = dictService.selectAllByCode(0,-1, "call_status");
        JSONArray json  =  JSONArray.fromObject(list);
        return json;
    }

    /**
     * 保存项目致电码
     * @return
     */
    @RequestMapping("/save-all-code")
    @ResponseBody
    public Object saveAllCode(@RequestParam(value = "items[]") Integer[] items, @RequestParam(value = "productId") Integer productId) {
        /* 清除之前字段 */
       EntityWrapper<ProductCallMap> entityWrapper = new EntityWrapper<>();
       entityWrapper.and("product_id=" + productId);
       productCallMapService.delete(entityWrapper);

        /* 遍历得到批量存储数组 */
        if (items.length > 0) {
            List<ProductCallMap> list = new ArrayList<>();
            for (Integer item : items) {
                ProductCallMap nproductCallMap = new ProductCallMap();
                nproductCallMap.setProductId(productId);
                nproductCallMap.setCallStatusId(item);
                list.add(nproductCallMap);
            }
            Boolean res = productCallMapService.insertBatch(list);
            if (res) {
                return  new JsonResult(200, "设置成功", new Date());
            } else {
                return  new JsonResult(201, "设置失败，请稍后重试！", new Date());
            }
        } else {
            return  new JsonResult(201, "请先选择致电结果！", new Date());
        }
    }

}

