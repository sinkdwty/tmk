package com.hjzddata.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.code.kaptcha.Constants;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.util.DateFormatUtil;
import com.hjzddata.modular.system.model.CallConfig;
import com.hjzddata.modular.system.model.Dept;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.*;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.exception.InvalidKaptchaException;
import com.hjzddata.core.log.LogManager;
import com.hjzddata.core.log.factory.LogTaskFactory;
import com.hjzddata.core.node.MenuNode;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.shiro.ShiroUser;
import com.hjzddata.core.util.ApiMenuFilter;
import com.hjzddata.core.util.KaptchaUtil;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.system.service.IMenuService;
import com.hjzddata.modular.system.service.IUserService;
import com.hjzddata.modular.task.model.CallLog;
import com.hjzddata.modular.task.service.ICallLogService;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.hjzddata.core.support.HttpKit.getIp;

/**
 * 登录控制器
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICallConfigService callConfigService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private ICallLogService callLogService;

    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        //获取菜单列表
        List<Integer> roleList = ShiroKit.getUser().getRoleList();
        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登录");
            return "/login.html";
        }
        List<MenuNode> menus = menuService.getMenusByRoleIds(roleList);
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        titles = ApiMenuFilter.build(titles);

        model.addAttribute("titles", titles);

        //获取用户头像
        Integer id = ShiroKit.getUser().getId();
        User user = userService.selectById(id);
        String avatar = user.getAvatar();
        model.addAttribute("avatar", avatar);
        model.addAttribute("userid", id);
//加载相应的呼叫系统
        Integer baseId = ShiroKit.getUser().getDeptId();
        Dept dept = deptService.selectById(baseId);
        if (dept != null) {
            Integer call_system = dept.getCallSystemId();
            String call_system_name = ConstantFactory.me().getCallSystemName((call_system));
            CallConfig callConfig = callConfigService.getcallConfig(call_system, baseId);
            if (callConfig != null) {
            String config = callConfig.getConfig();

                String[] configArr = config.split("\\|");
                String cc_ipaddress, cc_model_id, cc_engine, cc_port, cc_agentgroupid, cc_orgidentity;
                if (call_system_name.equals("汉天")) {
                    cc_ipaddress = configArr[0];
                    cc_model_id = "1";
                    cc_engine = "CE";
                    cc_orgidentity = configArr[1];
                    cc_agentgroupid = configArr[2];
//                    cc_port = "5070";
                } else {
                    cc_ipaddress = configArr[0];
                    cc_model_id = "1";
                    cc_engine = "CJI";
                    cc_orgidentity = configArr[1];
                    cc_agentgroupid = configArr[2];
//                    cc_port = "29998";
                }
                Cookie cookie_cc_ipaddress = new Cookie("cc_ipaddress", cc_ipaddress);
                Cookie cookie_cc_model_id = new Cookie("cc_model_id", cc_model_id);
                Cookie cookie_cc_engine = new Cookie("cc_engine", cc_engine);
//                Cookie cookie_cc_port = new Cookie("cc_port", cc_port);
                Cookie cookie_cc_orgidentity = new Cookie("cc_orgidentity", cc_orgidentity);
                Cookie cookie_cc_agentgroupid = new Cookie("cc_agentgroupid", cc_agentgroupid);
                cookie_cc_ipaddress.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
                cookie_cc_model_id.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
                cookie_cc_engine.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
//                cookie_cc_port.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
                cookie_cc_agentgroupid.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
                cookie_cc_orgidentity.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
                getHttpServletResponse().addCookie(cookie_cc_ipaddress);
                getHttpServletResponse().addCookie(cookie_cc_model_id);
                getHttpServletResponse().addCookie(cookie_cc_engine);
//                getHttpServletResponse().addCookie(cookie_cc_port);
                getHttpServletResponse().addCookie(cookie_cc_agentgroupid);
                getHttpServletResponse().addCookie(cookie_cc_orgidentity);
                model.addAttribute("call_system_name", call_system_name);
            }
        }

        //获取用户今日通次和今日通时
       /* Date beginTime = DateFormatUtil.getDayBegin();
        Date endTime = DateFormatUtil.getDayEnd();

        EntityWrapper wrapper = new EntityWrapper<CallLog>();
        wrapper.and("user_id=" + id + " and created_at between '" + beginTime + "' and '" + endTime + "'");
        List<CallLog> callLog = callLogService.selectList(wrapper);
        Integer callNum = callLog.size();
        String callTime = "00:00:00";
        if (callNum > 0 ) {
            Integer callTimeSum = 0;
            for (CallLog callInfo : callLog) {
                callTimeSum += callInfo.getCallSecond();
            }
            callTime = DateFormatUtil.secToTime(callTimeSum);
        }
        model.addAttribute("callNum", callNum);//今日通次
        model.addAttribute("callTime", callTime);//今日通时*/

        return "/index.html";
    }

    @RequestMapping(value = "/get-call-msg", method = RequestMethod.POST)
    @ResponseBody
    public Object callMsg() {
        Integer id = ShiroKit.getUser().getId();
        //获取用户今日通次和今日通时
        Date beginTime = DateFormatUtil.getDayBegin();
        Date endTime = DateFormatUtil.getDayEnd();
        EntityWrapper wrapper = new EntityWrapper<CallLog>();
        wrapper.and("user_id=" + id + " and created_at between '" + beginTime + "' and '" + endTime + "'");
        List<CallLog> callLog = callLogService.selectList(wrapper);
        Integer callNum = callLog.size();
        String callTime = "00:00:00";
        if (callNum > 0 ) {
            Integer callTimeSum = 0;
            for (CallLog callInfo : callLog) {
                callTimeSum += callInfo.getCallSecond();
            }
            callTime = DateFormatUtil.secToTime(callTimeSum);
        }
        HashMap rel = new HashMap();
        rel.put("callNum", callNum);
        rel.put("callTime", callTime);
        return rel;
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali() {

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");

        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return REDIRECT + "/";
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUser().getId(), getIp()));
        ShiroKit.getSubject().logout();
        // 获取Cookies数组
        Cookie[] cookies = request.getCookies();


        // 迭代查找并清除Cookie
        for (Cookie cookie: cookies) {
            //将cookie.setMaxAge(0)表示删除cookie.
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return REDIRECT + "/login";
    }
}
