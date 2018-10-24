package com.hjzddata.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.DeptDict;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.modular.system.model.Dept;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.service.IDeptService;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.annotion.Permission;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.node.ZTreeNode;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.ticket.model.TicketNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 */
@Controller
@RequestMapping("/dept")
public class DeptController extends BaseController {

    private String PREFIX = "/system/dept/";

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IDictService iDictService;
    /**
     * 跳转到部门管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dept.html";
    }

    /**
     * 跳转到添加部门
     */
    @RequestMapping("/dept_add")
    public String deptAdd(Model model) {
        List<Dict>  list = iDictService.selectByParentCode("call_system");
        model.addAttribute("callSystem", list);
        return PREFIX + "dept_add.html";
    }

    /**
     * 跳转到修改部门
     */
    @Permission
    @RequestMapping("/dept_update/{deptId}")
    public String deptUpdate(@PathVariable Integer deptId, Model model) {
        Dept dept = deptService.selectById(deptId);
        model.addAttribute(dept);
        model.addAttribute("pName", ConstantFactory.me().getDeptName(dept.getPid()));
        LogObjectHolder.me().set(dept);

        List<Dict>  list = iDictService.selectByParentCode("call_system");
        model.addAttribute("callSystem", list);
        return PREFIX + "dept_edit.html";
    }

    /**
     * 获取部门的tree列表
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.deptService.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 新增部门
     */
    @RequestMapping(value = "/add")
    @Permission
    @ResponseBody
    @BussinessLog(value = "添加公司", key = "simplename", dict = DeptDict.class)
    public Object add(Dept dept) {
        if (ToolUtil.isOneEmpty(dept, dept.getSimplename())) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        Dept dept1 = deptService.selectByName(dept.getSimplename(), dept.getId());
        if (dept1 != null) {
            throw new HjzdException(BizExceptionEnum.DEPT_ALREADY_REG);
        }

        //完善pids,根据pid拿到pid的pids
        deptSetPids(dept);
        dept.setCreatedAt(new Date());
        dept.setUpdatedAt(new Date());
        return this.deptService.insert(dept);
    }

    /**
     * 获取所有部门列表
     */
    @RequestMapping(value = "/list")
    @Permission
    @ResponseBody
    public Object list(String condition) {
        Page<Dept> page = new PageFactory<Dept>().defaultPage();
        EntityWrapper wrapper = new EntityWrapper<TicketNote>();
        if (condition != null) {
            wrapper.where("simplename like '%" + condition + "%' or " + "fullname like '%" + condition + "%'");
        }

        wrapper.orderBy("num", true);
        this.deptService.selectPage(page, wrapper);

        Map<String, Object> list = new HashMap<>();

        list.put("code", 0);
        list.put("count", page.getTotal());
        list.put("data", page.getRecords());

        return list;
    }

    /**
     * 部门详情
     */
    @RequestMapping(value = "/detail/{deptId}")
    @Permission
    @ResponseBody
    public Object detail(@PathVariable("deptId") Integer deptId) {
        return deptService.selectById(deptId);
    }

    /**
     * 修改部门
     */
    @BussinessLog(value = "修改公司", key = "simplename", dict = DeptDict.class)
    @RequestMapping(value = "/update")
    @Permission
    @ResponseBody
    public Object update(Dept dept) {
        if (ToolUtil.isEmpty(dept) || dept.getId() == null) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        Dept dept1 = deptService.selectByName(dept.getSimplename(), dept.getId());
        if (dept1 != null) {
            throw new HjzdException(BizExceptionEnum.DEPT_ALREADY_REG);
        }

        deptSetPids(dept);
        dept.setUpdatedAt(new Date());
        deptService.updateById(dept);
        return SUCCESS_TIP;
    }

    /**
     * 删除部门
     */
    @BussinessLog(value = "删除公司", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public Object delete(@RequestParam Integer deptId) {

        //缓存被删除的部门名称
        LogObjectHolder.me().set(ConstantFactory.me().getDeptName(deptId));

        deptService.deleteDept(deptId);

        return SUCCESS_TIP;
    }

    private void deptSetPids(Dept dept) {
        if (ToolUtil.isEmpty(dept.getPid()) || dept.getPid().equals(0)) {
            dept.setPid(0);
            dept.setPids("[0],");
        } else {
            int pid = dept.getPid();
            Dept temp = deptService.selectById(pid);
            String pids = temp.getPids();
            dept.setPid(pid);
            dept.setPids(pids + "[" + pid + "],");
        }
    }
}
