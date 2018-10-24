package com.hjzddata.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.config.properties.HjzdProperties;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.UserDict;
import com.hjzddata.modular.system.dao.UserMapper;
import com.hjzddata.modular.system.factory.UserFactory;
import com.hjzddata.modular.system.model.OperationLog;
import com.hjzddata.modular.system.model.Role;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IRoleService;
import com.hjzddata.modular.system.service.IUserService;
import com.hjzddata.modular.system.warpper.RoleWarpper;
import com.hjzddata.modular.system.warpper.UserWarpper;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.base.tips.Tip;
import com.hjzddata.core.common.annotion.Permission;
import com.hjzddata.core.common.constant.Const;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.common.constant.state.ManagerStatus;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.datascope.DataScope;
import com.hjzddata.core.db.Db;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.shiro.ShiroUser;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.system.transfer.UserDto;
import com.hjzddata.modular.task.model.Product;
import com.hjzddata.modular.task.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.io.File;
import java.util.*;

/**
 * 系统管理员控制器
 */
@Controller
@RequestMapping("/mgr")
public class UserMgrController extends BaseController {

    private static String PREFIX = "/system/user/";

    @Autowired
    private HjzdProperties hjzdProperties;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IProductService productService;

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "user.html";
    }

    /**
     * 跳转到查看管理员列表的页面
     */
    @RequestMapping("/user_add")
    public String addView(Model model) {
        ShiroUser shiroUser = ShiroKit.getUser();
        // add by eric 2018-09-20 start
        EntityWrapper entityWrapper = new EntityWrapper<Role>();
        List roles = null;

        // 获取登陆者的角色情况，如果是项目经理角色，则在新增是，无需录入选择新增用户的所属项目和公司
        if (ShiroKit.hasRole("manager") || ShiroKit.hasRole("group_leader")) { // 如果是以项目经理/组长角色添加下属用户
           model.addAttribute("deptId",shiroUser.getDeptId());
           model.addAttribute("productId",shiroUser.getProductId());
           roles = roleService.selectList(entityWrapper.and("id>"+shiroUser.getRoleList().get(0)));
        }else{ // 如果是管理员创建用户，允许创建管理员
           model.addAttribute("deptId",null);
           model.addAttribute("productId",null);
           roles = roleService.selectList(entityWrapper.and("id>="+shiroUser.getRoleList().get(0)));
        }
        model.addAttribute("roleList", roles);
        // add by eric 2018-09-20 end
        return PREFIX + "user_add.html";
    }

    /**
     * 跳转到角色分配页面
     */
    //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
    @Permission
    @RequestMapping("/role_assign/{userId}")
    public String roleAssign(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = (User) Db.create(UserMapper.class).selectOneByCon("id", userId);
        model.addAttribute("userId", userId);
        model.addAttribute("userAccount", user.getAccount());
        return PREFIX + "user_roleassign.html";
    }

    /**
     * 跳转到编辑管理员页面
     */
    @Permission
    @RequestMapping("/user_edit/{userId}")
    public String userEdit(@PathVariable Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        model.addAttribute(user);

        // add by eric 2018-09-20 start
        model.addAttribute("roleId", user.getRoleid());
        // add by eric 2018-09-20 end

        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));

        // add by eric 2018-09-21 start
        ShiroUser shiroUser = ShiroKit.getUser();
        if (ShiroKit.hasRole("manager") || ShiroKit.hasRole("group_leader")) { // 如果是以项目经理/组长角色添加下属用户
           // 获取登陆者的角色情况，如果是项目经理/组长角色，则在修改页面，不能修改所属项目和公司
           model.addAttribute("deptId",shiroUser.getDeptId());
           model.addAttribute("productId",shiroUser.getProductId());
        }else{
           model.addAttribute("deptId",null);
           model.addAttribute("productId",null);
        }
        EntityWrapper entityWrapper = new EntityWrapper<Role>();
        if(shiroUser.getRoleList().size()>0){
            String [] arr = user.getRoleid().split(",");
            String user_roleId = null;
            if(arr.length >=1){
               user_roleId = arr[0];
            }else{
               user_roleId = user.getRoleid();
            }
            if(shiroUser.getRoleList().get(0) == Integer.parseInt(user_roleId)){
                entityWrapper.and("id>="+user_roleId);
            }else{
                entityWrapper.and("id>"+shiroUser.getRoleList().get(0));
            }
        }
        List roles = roleService.selectList(entityWrapper);
        model.addAttribute("roleList", roles);
        // add by eric 2018-09-21 end

        entityWrapper = new EntityWrapper<Product>();
        entityWrapper.and("base_id =" + user.getDeptid() + " and is_del = 0");
        List<Product> products = productService.selectList(entityWrapper);
        model.addAttribute("products", products);
        LogObjectHolder.me().set(user);
        return PREFIX + "user_edit.html";
    }

    /**
     * 跳转到查看用户详情页面
     */
    @RequestMapping("/user_info")
    public String userInfo(Model model) {
        Integer userId = ShiroKit.getUser().getId();
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = this.userService.selectById(userId);
        model.addAttribute(user);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
        LogObjectHolder.me().set(user);
        return PREFIX + "user_view.html";
    }

    /**
     * 跳转到修改密码界面
     */
    @RequestMapping("/user_chpwd")
    public String chPwd() {
        return PREFIX + "user_chpwd.html";
    }

    /**
     * 修改当前用户的密码
     */
    @RequestMapping("/changePwd")
    @ResponseBody
    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
        if (!newPwd.equals(rePwd)) {
            throw new HjzdException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
        }
        Integer userId = ShiroKit.getUser().getId();
        User user = userService.selectById(userId);
        String oldMd5 = ShiroKit.md5(oldPwd, user.getSalt());
        if (user.getPassword().equals(oldMd5)) {
            String newMd5 = ShiroKit.md5(newPwd, user.getSalt());
            user.setPassword(newMd5);
            user.updateById();
            return SUCCESS_TIP;
        } else {
            throw new HjzdException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * 查询管理员列表
     */
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String name, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) Integer deptid) {
        Page<User> page = new PageFactory<User>().defaultPage();
        if (ShiroKit.isAdmin()) {
            List<Map<String, Object>> users = userService.selectUsers(page, null, name, beginTime, endTime, deptid, page.getOrderByField(), page.isAsc());
            page.setRecords((List<User>) new UserWarpper(users).warp());
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            List<Map<String, Object>> users = userService.selectUsers(page, dataScope, name, beginTime, endTime, deptid, page.getOrderByField(), page.isAsc());
            page.setRecords((List<User>) new UserWarpper(users).warp());
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("code",0);
        map.put("count",page.getTotal());
        map.put("data",page.getRecords());
        return map;
    }

    /**
     * 添加管理员
     */
    @RequestMapping("/add")
    @BussinessLog(value = "添加用户", key = "account", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip add(@Valid UserDto user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> err = new HashMap<String, String>();
            List<FieldError> list = result.getFieldErrors();
            FieldError error = null;
            for (int i = 0; i < list.size(); i++) {
                error = list.get(i);
                err.put(error.getField(), error.getDefaultMessage());
            }

            System.out.println(err);
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }

        // 判断账号是否重复
        User theUser = userService.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new HjzdException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // add by eric 2018-09-20 start
        // 请求字段：
        // 获取当前登陆用户信息
        ShiroUser shiroUser = ShiroKit.getUser();
        if (ShiroKit.hasRole("manager") || ShiroKit.hasRole("group_leader")) { // 如果是以项目经理/角色添加下属用户
            shiroUser.getDeptId();
            shiroUser.getProductId();
            user.setDeptid(shiroUser.getDeptId());
            user.setProductid(shiroUser.getProductId());
        }
        // add by eric 2018-09-20 end

        // 完善账号信息
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
        user.setStatus(ManagerStatus.OK.getCode());
        user.setCreatetime(new Date());

        this.userService.insert(UserFactory.createUser(user));
        return SUCCESS_TIP;
    }

    /**
     * 修改管理员
     *
     * @throws NoPermissionException
     */
    @RequestMapping("/edit")
    @BussinessLog(value = "修改用户", key = "account", dict = UserDict.class)
    @ResponseBody
    public Tip edit(@Valid UserDto user, BindingResult result) throws NoPermissionException {
        if (result.hasErrors()) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
            this.userService.updateById(UserFactory.createUser(user));
            return SUCCESS_TIP;
        } else {
            assertAuth(user.getId());
            ShiroUser shiroUser = ShiroKit.getUser();
            // add by eric 2018-09-21 start
            // 修改用户信息，关键域（所属项目、所属公司）根据权限来处理

            // (2) 登陆者为项目经理角色
            // 可以修改同个项目，所属公司，某个用户信息(包括本人)
            if (ShiroKit.hasRole("manager") || ShiroKit.hasRole("group_leader")) { // 如果是以项目经理/组长角色添加下属用户
                if(shiroUser.getProductId() != user.getProductid()){ // 属于不同项目，不允许修改，只可看
                    throw new HjzdException(BizExceptionEnum.CANT_UPDATE_PRIV);
                }else{
                   this.userService.updateById(UserFactory.createUser(user));
                   return SUCCESS_TIP;
                }
            }else {
               // (3) 登陆者为坐席操作人员角色
               if(ShiroKit.hasRole("worker")){
                    throw new HjzdException(BizExceptionEnum.CANT_UPDATE_PRIV);
               }else if (ShiroKit.hasRole("administrator")){
                   // (1) 登陆者为系统管理员权限,目前管理员可以任意分配
                   this.userService.updateById(UserFactory.createUser(user));
                   return SUCCESS_TIP;
               }else { // 其他角色
                   throw new HjzdException(BizExceptionEnum.NO_PERMITION);
               }
            }
            // add by eric 2018-09-21 end
        }
    }

    /**
     * 删除管理员（逻辑删除）
     */
    @RequestMapping("/delete")
    @BussinessLog(value = "删除用户", key = "userId", dict = UserDict.class)
    @Permission
    @ResponseBody
    public Tip delete(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能删除超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new HjzdException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        //缓存被删除的用户名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(userId));

        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.DELETED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 查看管理员详情
     */
    @RequestMapping("/view/{userId}")
    @ResponseBody
    public User view(@PathVariable Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        return this.userService.selectById(userId);
    }

    /**
     * 重置管理员的密码
     */
    @BussinessLog(value = "重置用户密码", key = "userId", dict = UserDict.class)
    @RequestMapping("/reset")
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip reset(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        User user = this.userService.selectById(userId);
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, user.getSalt()));
        this.userService.updateById(user);
        return SUCCESS_TIP;
    }

    /**
     * 冻结用户
     */
    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
    @RequestMapping("/freeze")
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip freeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能冻结超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new HjzdException(BizExceptionEnum.CANT_FREEZE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.FREEZED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 解除冻结用户
     */
    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
    @RequestMapping("/unfreeze")
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip unfreeze(@RequestParam Integer userId) {
        if (ToolUtil.isEmpty(userId)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        assertAuth(userId);
        this.userService.setStatus(userId, ManagerStatus.OK.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 分配角色
     */
    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
    @RequestMapping("/setRole")
//    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip setRole(@RequestParam("userId") Integer userId, @RequestParam("roleIds") String roleIds) {
        if (ToolUtil.isOneEmpty(userId, roleIds)) {
            throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (userId.equals(Const.ADMIN_ID)) {
            throw new HjzdException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        assertAuth(userId);
        this.userService.setRoles(userId, roleIds);
        return SUCCESS_TIP;
    }

    /**
     * 上传图片(上传到项目的webapp/static/img)
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {
        String pictureName = UUID.randomUUID().toString() + ".jpg";
        try {
            String fileSavePath = hjzdProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new HjzdException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return pictureName;
    }

    /**
     * 判断当前登录的用户是否有操作这个用户的权限
     */
    private void assertAuth(Integer userId) {
        if (ShiroKit.isAdmin()) {
            return;
        }
        List<Integer> deptDataScope = ShiroKit.getDeptDataScope();
        User user = this.userService.selectById(userId);
        Integer deptid = user.getDeptid();
        if (deptDataScope.contains(deptid)) {
            return;
        } else {
            throw new HjzdException(BizExceptionEnum.NO_PERMITION);
        }

    }
}
