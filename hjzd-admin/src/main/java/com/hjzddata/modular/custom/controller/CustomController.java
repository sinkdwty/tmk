package com.hjzddata.modular.custom.controller;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.config.properties.HjzdProperties;
import com.hjzddata.config.properties.SmsProperties;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.CustomDict;

import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.datascope.DataScope;
import com.hjzddata.core.excel.ExcelUtil;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogManager;
import com.hjzddata.core.log.factory.LogTaskFactory;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.shiro.ShiroUser;
import com.hjzddata.core.support.HttpKit;
import com.hjzddata.core.util.*;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.common.model.SendMailUtils;
import com.hjzddata.modular.common.model.SendMsg;
import com.hjzddata.modular.common.model.SmsClientAccessTool;
import com.hjzddata.modular.custom.model.AqCustom;
import com.hjzddata.modular.custom.model.SendMailLog;
import com.hjzddata.modular.custom.model.SendMessageLog;
import com.hjzddata.modular.custom.service.ISendMailLogService;
import com.hjzddata.modular.custom.service.ISendMessageLogService;
import com.hjzddata.modular.custom.warpper.CustomWarpper;
import com.hjzddata.modular.knowledge.model.Knowledge;
import com.hjzddata.modular.knowledge.service.IKnowledgeService;
import com.hjzddata.modular.knowledge.service.IMessageService;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.model.HjSmsBatch;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.system.service.IHjSmsBatchService;
import com.hjzddata.modular.system.service.IUserService;
import com.hjzddata.modular.system.warpper.UserWarpper;

import com.hjzddata.modular.task.model.Batch;
import com.hjzddata.modular.task.model.CallLog;
import com.hjzddata.modular.task.model.Product;
import com.hjzddata.modular.task.service.IBatchService;
import com.hjzddata.modular.task.service.ICallLogService;
import com.hjzddata.modular.task.service.IProductService;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.hjzddata.modular.task.service.IPolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.modular.custom.model.Custom;
import com.hjzddata.modular.custom.service.ICustomService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hjzddata.modular.common.model.SendMailUtils.initJavaMailSender;


/**
 * 客户管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-10 11:35:39
 */
@Controller
@RequestMapping("/custom")
public class CustomController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String PREFIX = "/custom/custom/";

    @Autowired
    private ICustomService customService;

    @Autowired
    private IDictService iDictService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private  ISendMailLogService mailLogService;

    @Autowired
    private ISendMessageLogService messageLogService;

    @Autowired
    private IKnowledgeService knowledgeService;

    @Autowired
    private IPolicyService policyService;

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICallLogService callLogService;

    @Autowired
    private IProductService productService;

    @Autowired
    private HjzdProperties hjzdProperties;

    @Autowired
    private IBatchService batchService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IHjSmsBatchService hjSmsBatchService;
    /**
     * 跳转到客户管理首页
     */
    @RequestMapping("")
    public String index(Model model) {
        // add by eric 2018-09-28 start
//        List<Dict> list = dictService.selectList(new EntityWrapper<Dict>().and("code='call_status'"));
        Integer product_id = ShiroKit.getUser().getProductId();
        List<Dict> list = dictService.selectAllByCode(product_id,0, "call_status");
        model.addAttribute("successCode", list);
        // add by eric 2018-09-28 start
        return PREFIX + "custom.html";
    }


    /**
     * 跳转到添加客户管理
     */
    @RequestMapping("/custom_add")
    public String customAdd(Model model) {
        //查找项目
        ShiroUser shiroUser = ShiroKit.getUser();
        EntityWrapper<Product> entityWrapper = new EntityWrapper<>();
        if (ShiroKit.isAdmin()) {

        } else {
            entityWrapper.and("id =" + shiroUser.getProductId());
        }
        List<Product> products = productService.selectList(entityWrapper);
        model.addAttribute("products", products);


        // add by eric 2018-09-14 start
//        List callList = iDictService.selectByCode("call_status");
        Integer product_id = ShiroKit.getUser().getProductId();
        List<Dict> list = dictService.selectAllByCode(product_id,0, "call_status");
        model.addAttribute("callList", list);
        // add by eric 2018-09-14 end

        return PREFIX + "custom_add.html";
    }
    /**
     * 跳转到批量上传
     */
    @RequestMapping("/custom_upload")
    public String customUpload(@RequestParam(required = false, defaultValue = "") String batch_no, Model model) {
        //查找项目
        ShiroUser shiroUser = ShiroKit.getUser();
        EntityWrapper<Product> entityWrapper = new EntityWrapper<>();
        if (ShiroKit.isAdmin()) {

        } else {
            entityWrapper.and("id =" + shiroUser.getProductId());
        }
        List<Product> products = productService.selectList(entityWrapper);
        model.addAttribute("products", products);
        model.addAttribute("batch_no", batch_no);
        return PREFIX + "custom_upload.html";
    }
    /**
     * 跳转到修改客户管理
     */
    @RequestMapping("/custom_update/{customId}")
    public String customUpdate(@PathVariable Integer customId, Model model) {
        Custom custom = customService.selectById(customId);
        model.addAttribute("item",custom);

        //查找项目
        ShiroUser shiroUser = ShiroKit.getUser();
        EntityWrapper<Product> entityWrapper = new EntityWrapper<>();
        if (ShiroKit.isAdmin()) {

        } else {
            entityWrapper.and("id =" + shiroUser.getProductId());
        }
        List<Product> products = productService.selectList(entityWrapper);
        model.addAttribute("products", products);
        LogObjectHolder.me().set(custom);
        return PREFIX + "custom_edit.html";
    }


    /**
     * 跳转到详情页面
     *
     */
    @RequestMapping("/custom_detail/{viewId}")
    public String customDetail(@PathVariable Integer viewId, Model model, HttpServletRequest request, HttpServletResponse response) {
        Custom custom = customService.selectById(viewId);
        model.addAttribute("item",custom);

        /*
         *预测外呼
         *给坐席生成相应的通话日志 并把该案件分配给自己  edited by chenhexiang  20180926
         */
        Integer callLog_id = 0;
        Cookie[] cookies = request.getCookies();
        String token = "";
        String sessionID = "";
        for (Cookie cookie : cookies) {
            switch(cookie.getName()){
                case "temp_custom_id_websocket":
                    token = cookie.getValue();
                    break;
                case "luyin_ssessionId":
                    sessionID = cookie.getValue();
                default:
                    break;
            }
        }
        if (token != null && !token.equals("")) {
            custom.setUserId(ShiroKit.getUser().getId());
            custom.setAllocateTime(new Date());
            custom.setStatus(2);
            custom.setContactTimes(custom.getContactTimes()+1);
            if (custom.getFirstContactTime() == null) {
                custom.setFirstContactTime(new Date());
            }
            customService.updateById(custom);

            CallLog callLog = new CallLog();
            callLog.setCaseId(viewId);
            callLog.setCustomName(custom.getCustomName());
            callLog.setCustomPhone(custom.getPhone());
            callLog.setUserId(ShiroKit.getUser().getId());
            callLog.setUserName(ShiroKit.getUser().getName());
            callLog.setCreatedAt(new Date());
            callLog.setCreatedIp(HttpKit.getIp());
            callLog.setCallStartTime(new Date());
            callLog.setCallSessionid(sessionID);
            callLogService.insert(callLog);
            callLog_id = callLog.getId();

            Cookie newCookie=new Cookie("temp_custom_id_websocket",null); //假如要删除名称为username的Cookie
            newCookie.setMaxAge(0); //立即删除
            newCookie.setPath("/");
            response.addCookie(newCookie);

            LogManager.me().executeLog(LogTaskFactory.bussinessLog(ShiroKit.getUser().getId(), "预测外呼打开详情页", "Custom.class", "/custom_detail" ,0,"客户id="+viewId));
        } else {
            LogManager.me().executeLog(LogTaskFactory.bussinessLog(ShiroKit.getUser().getId(), "正常打开详情页", "Custom.class", "/custom_detail" ,0,"客户id="+viewId));
        }
        model.addAttribute("callLog_id", callLog_id);

        if (custom.getAqCustom() == null) {
            AqCustom aqCustomNew = new AqCustom();
            model.addAttribute("aqItem",aqCustomNew);
        } else {
            String aqCustom = custom.getAqCustom();
            JSONObject obj = new JSONObject().fromObject(aqCustom);//将json字符串转换为json对象
            AqCustom aqCustomNew = (AqCustom) JSONObject.toBean(obj,AqCustom.class);//将建json对象转换为Person对象
            model.addAttribute("aqItem",aqCustomNew);
        }
        LogObjectHolder.me().set(custom);


//        List callList = iDictService.selectByCode("call_status");
        Integer product_id = ShiroKit.getUser().getProductId();
        List<Dict> list = dictService.selectAllByCode(product_id,0, "call_status");
        model.addAttribute("callList", list);
        if (custom.getLabel() != null) {
            model.addAttribute("label", custom.getLabel().split(","));
        } else {
            model.addAttribute("label", new String[0]);
        }

        //今日拨打，往月拨打，短信条数统计
        Date getDayBegin = DateFormatUtil.getDayBegin();
        Date getDayEnd = DateFormatUtil.getDayEnd();
        Date getBeginDayOfMonth = DateFormatUtil.getBeginDayOfMonth();
        Date getEndDayOfMonth = DateFormatUtil.getEndDayOfMonth();
        Integer todatCallTimes = callLogService.selectCount(Condition.create()
                .setSqlSelect("sum(id)")
                .eq("case_id", viewId)
                .between("created_at", getDayBegin, getDayEnd));
        Integer monthCallTimes = callLogService.selectCount(Condition.create()
                .setSqlSelect("sum(id)")
                .eq("case_id", viewId)
                .between("created_at", getBeginDayOfMonth, getEndDayOfMonth));
        model.addAttribute("todatCallTimes", todatCallTimes);
        model.addAttribute("monthCallTimes", monthCallTimes);

        return PREFIX + "detail.html";
    }

    @RequestMapping(value = "/next-custom")
    @ResponseBody
    public Object nextCustom(@RequestParam(required = true) Integer id) {
        String column = ShiroKit.getSessionAttr("column");
        String condition = ShiroKit.getSessionAttr("condition");
        String beginTime = ShiroKit.getSessionAttr("beginTime");
        String endTime = ShiroKit.getSessionAttr("endTime");
        Integer status = ShiroKit.getSessionAttr("status");
        String update_beginTime = ShiroKit.getSessionAttr("update_beginTime");
        String update_endTime = ShiroKit.getSessionAttr("update_endTime");
        List<Custom> result = customService.selectAssignCustomList( column,condition,beginTime,endTime,update_beginTime,update_endTime,status,id,1);

        HashMap map = new HashMap();
        if (result.size()>0) {
            String name = "";
            if (result.get(0).getCustomName() != null && !result.get(0).getCustomName().equals("")){
                name = result.get(0).getCustomName();
            } else {
                name = result.get(0).getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            }
            map.put("code", 200);
            map.put("id", result.get(0).getId());
            map.put("name", name);
        } else {
            map.put("code", 201);
        }
        return map;
    }

    /**
     * 获取客户管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String column, @RequestParam(required = false) String condition,@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String update_beginTime, @RequestParam(required = false) String update_endTime, @RequestParam(required = false, defaultValue = "0") Integer status, @RequestParam(required = false, defaultValue = "0") Integer isCall,  @RequestParam(required = false, defaultValue = "-1") Integer call_status_id) {
        ShiroKit.setSessionAttr("column",column);
        ShiroKit.setSessionAttr("condition",condition);
        ShiroKit.setSessionAttr("beginTime",beginTime);
        ShiroKit.setSessionAttr("endTime",endTime);
        ShiroKit.setSessionAttr("status",status);
        ShiroKit.setSessionAttr("update_beginTime",update_beginTime);
        ShiroKit.setSessionAttr("update_endTime",update_endTime);
        ShiroKit.setSessionAttr("call_status_id",call_status_id);
        ShiroKit.setSessionAttr("isCall",isCall);

        Page<Custom> page = new PageFactory<Custom>().defaultPage();
        List result = customService.selectCustomList(page,null,column,condition,beginTime,endTime,update_beginTime,update_endTime,status,call_status_id,null,isCall,page.getOrderByField(), page.isAsc());
        page.setRecords((List<Custom>) new CustomWarpper(result).warp());
        Map<String, Object> map = new HashMap<>(10);
        map.put("code",0);
        map.put("count",page.getTotal());
        map.put("data", result);
        return map;
    }

    /**
     * 新增客户管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(AqCustom custom) {
        Custom ncustom = new Custom();
        JSONObject json = JSONObject.fromObject(custom);//将java对象转换为json对象
        String aqCustomStr = json.toString();//将json对象转换为字符串
        ncustom.setAqCustom(aqCustomStr);
        ncustom.setPhone(custom.getPhone());
        ncustom.setEmail(custom.getEmail());//邮箱
        String dictName = "";
        if(custom.getCall_status_content() != null) {
            Dict dict = iDictService.selectById(custom.getCall_status_content());
            dictName = dict.getName();
        }

        Integer deptId = ShiroKit.getUser().getDeptId();
        String deptname = ShiroKit.getUser().getDeptName();
        if(null!= custom.getCustomName()){
            ncustom.setCustomName(custom.getCustomName());
        }
        ncustom.setCompany(deptname);
        ncustom.setCompanyId(deptId);
        ncustom.setCreatedTime(new Date());
        ncustom.setUpdatedTime(new Date());
        ncustom.setImportId(ShiroKit.getUser().getId());
        ncustom.setImportName(ShiroKit.getUser().getName());

        Integer proId = ShiroKit.getUser().getProductId();  //客户所属项目
        ncustom.setProductId(proId);
        if (ShiroKit.hasRole("worker")) {       //坐席新增的自动分配给自己
            Integer userid = ShiroKit.getUser().getId();
            ncustom.setUserId(userid);
            ncustom.setStatus(2);
        }
        if(null!=custom.getCall_status_content()){
            // call_status_id
            ncustom.setFirstContactTime(new Date());
            ncustom.setLastContactTime(new Date());
            ncustom.setCallStatusId(custom.getCall_status_content());
            ncustom.setCallStatusName(dictName);
            ncustom.setContactTimes(1);
        }

        if(null!= custom.getReserveTime()){ // 预约时间
            try{
                ncustom.setReserveTime(new SimpleDateFormat("yyyy-mm-dd  HH:mm:ss").parse(custom.getReserveTime()));
            }catch (Exception ex){
                ncustom.setReserveTime(new Date());
            }
        }

        if(null!= custom.getPhoneNote()){
            ncustom.setCallNote(custom.getPhoneNote());
        }
        ncustom.setUpdatedTime(new Date());

        // add by eric 2018-09-14 start
        // desc : 页面逻辑调整，新增客户页，允许呼叫，然后可以选择是否保存客户，因此在呼叫时
        // 同时生成初始化客户信息：字段参考（手机号）
        // 后续修改请情况下，若手机号不变，则进行字段的修改
        // 否则进行新增
        Custom theCustom = customService.getByPhone(ncustom.getPhone()); // 号码存在
        if (theCustom != null && theCustom.getId() != ncustom.getId()) { // 做修改
            ncustom.setId(theCustom.getId());
           customService.updateById(ncustom);
        }else{ // 新增
            customService.insert(ncustom); // add by eric 2018-09-13 end
        }

        //添加通话记录 edited by chx 2018-10-19
        CallLog callLog = new CallLog();
        callLog.setCaseId(ncustom.getId());
        callLog.setCustomName(ncustom.getCustomName());
        callLog.setCustomPhone(ncustom.getPhone());
        callLog.setCallStatusId(ncustom.getCallStatusId());
        callLog.setCallStatusName(dictName);
        callLog.setUserId(ShiroKit.getUser().getId());
        callLog.setUserName(ShiroKit.getUser().getName());
        callLog.setCreatedAt(new Date());
        callLog.setCreatedIp(HttpKit.getIp());
        callLogService.insert(callLog);

        return new JsonResult(200, "数据添加成功", ncustom.getId());
    }

    /**
     * 删除客户管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer customId) {
        Custom custom = customService.selectById(customId);
        custom.setStatus(5);
        custom.setIsDel(1);
        customService.updateById(custom);
        return SUCCESS_TIP;
    }

    /**
     * 修改客户管理
     */
    @BussinessLog(value = "修改客户",  key = "id", dict = CustomDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Custom custom) {
        // 判断电话是否重复
        Custom theCustom = customService.getByPhone(custom.getPhone());
        if (theCustom != null && (!theCustom.getId().equals(custom.getId()))) {
            throw new HjzdException(BizExceptionEnum.CUSTOM_ALREADY_REG);
        }
        custom.setUpdatedTime(new Date());
        customService.updateById(custom);
        return SUCCESS_TIP;
    }

    /**
     * 客户管理详情
     */
    @RequestMapping(value = "/detail/{customId}")
    @ResponseBody
    public Object detail(@PathVariable("customId") Integer customId) {
        return customService.selectById(customId);
    }


    /**
     *
     * @param note
     * @param label
     * @param id
     * @return
     */
    @BussinessLog(value = "修改客户备注和标签",  key = "id", dict = CustomDict.class)
    @RequestMapping(value="/handle_custom",method = RequestMethod.POST)
    @ResponseBody
    public Object handleCustom(@RequestParam(required = false) String note,@RequestParam(required = false) String label, @RequestParam Integer id ) {
        Custom custom = customService.selectById(id);
        if (note != null) {
            custom.setNote(note);
        } else {
            custom.setLabel(label);
        }
        custom.setUpdatedTime(new Date());
        customService.updateById(custom);
        return SUCCESS_TIP;
    }


    /**
     * 客户批量回收
     */
    @RequestMapping(value = "/recycle")
    @ResponseBody
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public Object recycle(@RequestParam(value = "ids[]") String[] ids) {
        /*String[] ids_arr = ids.split(",");
        for (String id : ids_arr) {
            // 判断电话是否重复
            Custom theCustom = customService.selectById(id);
            theCustom.setStatus(4);
            theCustom.setUserId(null);
            theCustom.setAllocateTime(null);
            customService.updateById(theCustom);
        }*/
        customService.updateStatusByIds(ids);
        return SUCCESS_TIP;
    }

    /**
     * 客户批量删除 暂没删除通话记录
     */
    @RequestMapping(value = "/batchDel")
    @ResponseBody
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public Object batchDel(@RequestParam(value = "ids[]") String[] ids) {
        customService.deleteCustomByIds(ids);
        return SUCCESS_TIP;
    }

    /**
     *快速分案
     */
    @RequestMapping(value = "/assign-custom")
    public Object assignCustom(@RequestParam(value = "way") String way, Model model) {
        model.addAttribute("way", way);
        return PREFIX + "user-table.html";
    }

    /**
     *快速分案坐席
     */
    @RequestMapping(value = "/assign-custom-list")
    @ResponseBody
    public Object assignCustomList(@RequestParam(value = "name", required = false) String name, Model model) {
        //查找当前用户所属公司
        Integer deptid = ShiroKit.getUser().getDeptId();
        DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
        Page<User> page = new PageFactory<User>().defaultPage();
        List<Map<String, Object>> users = userService.selectUsers(page, dataScope, name, "", "", deptid, page.getOrderByField(), page.isAsc());
        page.setRecords((List<User>) new UserWarpper(users).warp());
        Map<String, Object> map = new HashMap<>(10);
        map.put("code",0);
        map.put("count",page.getTotal());
        map.put("data", users);
        return map;
    }

    /**
     * 分案操作
     */
    @RequestMapping(value = "/assign-custom-ensure")
    @ResponseBody
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public Object assignCustomEnsure(@RequestParam(value = "ids[]", required = false) String[] ids, @RequestParam(value = "user_id") String user_id, @RequestParam("way") String way) {
        Date allocate_time = new Date();
        if (way.equals("opListAll") || way.equals("fast_batchOp2")) {     //查询分案
            String column = ShiroKit.getSessionAttr("column");
            String condition = ShiroKit.getSessionAttr("condition");
            String beginTime = ShiroKit.getSessionAttr("beginTime");
            String endTime = ShiroKit.getSessionAttr("endTime");
            Integer status = ShiroKit.getSessionAttr("status");
            String update_beginTime = ShiroKit.getSessionAttr("update_beginTime");
            String update_endTime = ShiroKit.getSessionAttr("update_endTime");
            List<Custom> result = customService.selectAssignCustomList(column,condition,beginTime,endTime,update_beginTime,update_endTime,status,null,null);

            if (result != null && !result.isEmpty()) {
                String[] userIdArr = user_id.split(",");
                if (result.size() < userIdArr.length) {
                    return new JsonResult(201, "案件数量少于分配坐席数量，请重新分配！", new Date());
                }

                ArrayList<String> list = new ArrayList<>();

                for(int i=0; i<result.size(); i++) {
                    Custom custom = (Custom) result.get(i);
                    list.add(custom.getId()+"");
                }
                List<List<String>> avg_list = ToolUtil.averageAssign(list, userIdArr.length);

                for (int i=0; i<userIdArr.length; i++) {
                    Integer arr_length = avg_list.get(i).size();
                    String[] array = new String[arr_length];
                    customService.assignCustom(avg_list.get(i).toArray(array), Integer.parseInt(userIdArr[i]), allocate_time);
                }
            } else {
                return new JsonResult(201, "查询结果为空无需操作！", new Date());
//                throw new HjzdException(BizExceptionEnum.CUSTOM_List_REG);
            }
        } else if (way.equals("fast_batchAuto")) {     //快速自动分案
            if ( ids != null && ids.length > 0) {
                String[] userIdArr = user_id.split(",");
                List<String> ids_list = Arrays.asList(ids);    //把数组转换为list
                List<List<String>> list = ToolUtil.averageAssign(ids_list, userIdArr.length);//把list均分成n个list

                for (int i=0; i<userIdArr.length; i++) {    //随机分案
                    Integer arr_length = list.get(i).size();
                    String[] array = new String[arr_length];
                    customService.assignCustom(list.get(i).toArray(array), Integer.parseInt(userIdArr[i]), allocate_time);
                }
            } else {
                return new JsonResult(201, "查询结果为空无需操作！", new Date());
            }
        } else {        //快速分案
            customService.assignCustom(ids, Integer.parseInt(user_id), allocate_time);
        }

        return SUCCESS_TIP;
    }

    /* 保存致电结果
     * @param call_status_id
     * @param case_id
     * @param reserve_time
     * @param call_note
     * @return
     */
    @BussinessLog(value = "保存致电结果",  key = "case_id", dict = CustomDict.class)
    @RequestMapping(value="/save-call-status",method = RequestMethod.POST)
    @ResponseBody
    public Object saveCallStatus(
            @RequestParam Integer call_status_id,
            @RequestParam Integer case_id,
            @RequestParam(required = false) String reserve_time ,
            @RequestParam(required = false) Integer custom_case_id ,
            @RequestParam(required = false) String custom_name ,
            @RequestParam(required = false) String custom_phone ,
            @RequestParam(required = false) String call_note
    ) {

        try {
            /* 修改客户致电结果码 */
            Dict dict = iDictService.selectById(call_status_id);
            Custom custom = customService.selectById(case_id);
            custom.setCallStatusId(call_status_id);
            custom.setCallStatusName(dict.getName());
            if (reserve_time != null && !reserve_time.equals("")) {
                // add by eric 2018-09-14  start
                try{
                    custom.setReserveTime(new SimpleDateFormat("yyyy-mm-dd  HH:mm:ss").parse(reserve_time));
                }catch (Exception ex){
                    custom.setReserveTime(new Date());
                }
                // add by eric 2018-09-14  end
            }
            if (call_note != null  && !call_note.equals("")) {
                custom.setCallNote(call_note);
            }

            custom.setUpdatedTime(new Date());
            custom.setLastContactTime(new Date());
            if (custom.getFirstContactTime() == null) {
                custom.setFirstContactTime(new Date());
            }


            if (custom_case_id != null && custom_case_id > 0) {
                CallLog  callLog = callLogService.selectById(custom_case_id);
                callLog.setCallStatusId(call_status_id);
                callLog.setCallStatusName(dict.getName());
                if (call_note != null  && !call_note.equals("")) {
                    callLog.setNote(call_note);
                }
                callLogService.updateById(callLog);
            } else {
                CallLog callLog = new CallLog();
                callLog.setCaseId(case_id);
                callLog.setCustomName(custom_name);
                callLog.setCustomPhone(custom_phone);
                callLog.setCallStatusId(call_status_id);
                callLog.setCallStatusName(dict.getName());
                callLog.setUserId(ShiroKit.getUser().getId());
                callLog.setUserName(ShiroKit.getUser().getName());
                if (call_note != null  && !call_note.equals("")) {
                    callLog.setNote(call_note);
                }
                callLog.setCreatedAt(new Date());
                callLog.setCreatedIp(HttpKit.getIp());

                callLogService.insert(callLog);

                //只是在新生成通话日志时，联系次数+1  手拨或自拨时不需要增加
                Integer ContactTimesSum = custom.getContactTimes() + 1;
                custom.setContactTimes(ContactTimesSum);
            }

            customService.updateById(custom);
        } catch (Exception e) {
            return new JsonResult(201, e.getMessage(), new Date());
        }
        return SUCCESS_TIP;
    }


    /**
     * 获取二级致电结果
     * @param value
     * @return
     */
    @RequestMapping(value="/get_call_status",method = RequestMethod.POST)
    @ResponseBody
    public Object getCallStatus(@RequestParam Integer value) {
//        List dict = iDictService.selectByPid(value);
        Integer product_id = ShiroKit.getUser().getProductId();
        List<Dict> dict = dictService.selectAllByCode(product_id, value, "call_status");
        Map<String, Object> map = new HashMap<>();
        map.put("code" , 200);
        map.put("data", dict);
        return  map;
    }


    /**
     * 客户导入
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadfile(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            Object resultJson = customService.batchImport(fileName, file);
            return resultJson;
        } catch (Exception e) {
            System.out.println( e);
            return new JsonResult(201, "导入失败", new Date());
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/customer_import")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Object customerImport(@RequestParam(required = true) String filelink, @RequestParam(required = true) Integer productid, @RequestParam(required = false, defaultValue = "") String batch_no) {
        Batch nbatch = new Batch();
        if (batch_no.equals("")) {
            //如果从客户管理或者我的客户导入，同一项目一天只可导入一次
            Date begin = DateFormatUtil.getDayBegin();
            Date end = DateFormatUtil.getDayEnd();
            Batch batch = batchService.selectOne(new EntityWrapper<Batch>().and("product_id= " + productid + " and created_at between '" + begin + "' and '"+end+"'").last("LIMIT 0,1").orderBy("created_at desc"));
            if (batch != null) {
                return new JsonResult(201, "抱歉，今天已经生成了一个批次，该入口不能导入数据！请前往数据批次进行导入！", new Date());
            }
            batch_no = "九格" + DateUtil.formatDate(new Date(), "yyyMMdd") + "/01";
        } else {
            String[] batch_arr = batch_no.split("/");
            Batch batch = batchService.selectOne(new EntityWrapper<Batch>().and("batch_no like '" + batch_arr[0] + "%'").last("LIMIT 0,1").orderBy("created_at desc"));
            String[] batch_arr_new = batch.getBatchNo().split("/");
            Integer num = Integer.parseInt(batch_arr_new[1]) + 1;
            String num_str = "";
            if (num <= 10) {
                num_str = "0" + num;
            } else {
                num_str = num + "";
            }
            batch_no = batch_arr[0] + "/" + num_str;
        }
        try {
            ShiroUser shiroUser = ShiroKit.getUser();
            long startTime=System.currentTimeMillis();   //获取开始时间
            Integer deptId = shiroUser.getDeptId();
            String deptname = shiroUser.getDeptName();
            String file_path = hjzdProperties.getFileUploadPath() + filelink;
            Collection<Map> list = customService.parseExcel(file_path);

         //插入批次
            nbatch.setBatchNo(batch_no);
            nbatch.setProductId(productid);
            nbatch.setFilePath(filelink);
            nbatch.setUserId(shiroUser.getId());
            nbatch.setCreatedAt(new Date());
            nbatch.setImportNum(list.size());
            batchService.insert(nbatch);

            List<Custom> new_list = new ArrayList();
            int r = 1;
            for (Map map : list) {
                //判断手机号是否存在(redis)
//                Custom uniqueData = customService.selectOne(new EntityWrapper<Custom>().and("phone=" + map.get("手机号码")));
                /*Object uniqueData = redisUtil.hget("product_phone", productid+""+map.get("手机号码"));
                if (uniqueData != null) {
                    if (nbatch.getId() != null && nbatch.getId() > 0) {
                        batchService.deleteById(nbatch.getId());
                    }
                    return new JsonResult(201, "导入失败(第" + (r + 1) + "行,手机号码已存在)", new Date());
                }*/

                Map new_map = new HashMap();
                for(Object map_key: map.keySet()){
                    String new_key = "";
                    switch (map_key.toString()) {
                        case "客户姓名" :
                            new_key = "customName";
                            break;
                        case "手机号码" :
                            new_key = "phone";
                            break;
                        case "邮箱" :
                            new_key = "email";
                            break;
                        case "地址" :
                            new_key = "address";
                            break;
                        case "手机号码归属地" :
                            new_key = "address";
                            break;
                        case "客户来源" :
                            new_key = "customSource";
                            break;
                        case "标签" :
                            new_key = "label";
                            break;
                        case "备注" :
                            new_key = "note";
                            break;
                    }

                    new_map.put(new_key, map.get(map_key));
                    new_map.put("company", deptname);
                    new_map.put("companyId", deptId);
                    new_map.put("createdTime", DateUtil.getAllTime());
                    new_map.put("updatedTime", DateUtil.getAllTime());
                    new_map.put("productId", productid);
                    new_map.put("batchNo", batch_no);
                    Integer userid = ShiroKit.getUser().getId();
                    String userName = ShiroKit.getUser().getName();
                    new_map.put("importId", userid);
                    new_map.put("importName", userName);

                    if (ShiroKit.hasRole("worker")) {       //坐席新增的自动分配给自己
                        new_map.put("userId", userid);
                        new_map.put("status", 2);
                    } else {
                        new_map.put("userId", null);
                        new_map.put("status", 0);
                    }
                }
                Object customer = ToolUtil.parseMap2Object(new_map, Custom.class);
                new_list.add((Custom) customer);
                r++;
            }

            customService.insertBatchNew(new_list, productid, batch_no);
            long endTime=System.currentTimeMillis(); //获取结束时间

            //把数据存入redis
            /*for (Custom item : new_list) {
                //把手机号存入redis
                redisUtil.hset("product_phone", productid+item.getPhone(), batch_no);
            }*/

            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
            return new JsonResult(200,"导入成功","导入成功");
        } catch (Exception e) {
            return new JsonResult(201,"导入失败",e);
        }
    }


    /**
     *
     * @param fileName
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(path = "/download-tpl", method = RequestMethod.GET)
    public void downloadTpl(@RequestParam("fileName") String fileName, @RequestParam(value = "type", required = false) String type, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //ClassLoader classLoader = getClass().getClassLoader();
        //URL url = classLoader.getResource("xls/" + fileName);

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("xls/" + fileName);
        //File file = new File(url.getFile());
        String exportFileName = "客户导入模板.xls";
        if (type.equals("ticket")) {
            exportFileName = "工单导入模板.xls";
        } else if (type.equals("blacklist")) {
            exportFileName = "黑名单导入模板.xls";
        }
        exportFileName = new String(exportFileName.getBytes(), "ISO-8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + exportFileName);
        response.setCharacterEncoding("GBK");
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(is);
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                ToolUtil.closeObject(os);
                ToolUtil.closeObject(bis);
            }catch (Exception ex){
                // 此处可写日志
            }
            bis = null;
        }
    }

    @RequestMapping(path = "/knowledge", method = RequestMethod.GET)
    public Object knowledge() {
        return PREFIX + "knowledge.html";
    }

    @RequestMapping(path = "/knowledge", method = RequestMethod.POST)
    @ResponseBody
    public Object knowledgeSearch(@RequestParam(required = false) String keyword) {
        HashMap<String, Object> result = new HashMap<>(3);
        if (keyword != null && keyword.length() >= 1) {
            List<Knowledge> knowledgeList = knowledgeService.selectList(
                    new EntityWrapper<Knowledge>().setSqlSelect("id").where("status = 1 and (name like '%" + keyword +"%' or key_word like '%" + keyword + "%')")
            );

            /* 搜索的是策略 */
//            if (knowledgeList != null) {
//                boolean isnull = true;
//                EntityWrapper<Policy> policyEntityWrapper = new EntityWrapper<>();
//                for (Knowledge kd : knowledgeList) {
//                    System.out.println(kd);
//                    if (kd.getId() != null) {
//                        isnull = false;
//                        policyEntityWrapper.or("FIND_IN_SET('" + kd.getId() + "', policy_key)");
//                    }
//                }
//                if (isnull == false) {
//                    Page<Policy> page = new PageFactory<Policy>().defaultPage();
//                    policyService.selectPage(page, policyEntityWrapper);
//
//                    result.put("data", page.getRecords());
//                    result.put("count", page.getTotal());
//                    result.put("code", 0);
//                    return  result;
//                }
//            }

            /* 现改为以知识库搜索 */
            if (knowledgeList != null) {
                boolean isnull = true;
                EntityWrapper<Knowledge> knowledgeEntityWrapper = new EntityWrapper<>();
                for (Knowledge kd : knowledgeList) {
                    System.out.println(kd);
                    if (kd.getId() != null) {
                        isnull = false;
                        knowledgeEntityWrapper.and("status = 1 and (name like '%" + keyword +"%' or key_word like '%" + keyword + "%')");
                    }
                }
                if (isnull == false) {
                    Page<Knowledge> page = new PageFactory<Knowledge>().defaultPage();
                    knowledgeService.selectPage(page, knowledgeEntityWrapper);

                    result.put("data", page.getRecords());
                    result.put("count", page.getTotal());
                    result.put("code", 0);
                    return  result;
                }
            }


        }
        result.put("data", null);
        result.put("count", 0);
        result.put("code", 0);
        return  result;
    }

//    /**
//     * 验证手机号码是否重复
//     * @param phone
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(path = "/unique-phone", method = RequestMethod.POST)
//    public Object uniquePhone(@RequestParam("phone") String phone) {
//        Map<String,Boolean> map = new HashMap<String,Boolean>();
//        Custom custom = new Custom();
//        custom.setPhone(phone);
//        Object uniqueData = customService.selectOne(new EntityWrapper<>(custom));
//        if (uniqueData != null) {
//            map.put("valid", false);
//        } else {
//            map.put("valid", true);
//        }
//
//        return map;
//    }


    /**
     * 短信界面跳转
     * @param customId
     * @param model
     * @return
     */
    @RequestMapping(value = "/to_sendMessage/{customId}")
    public String to_sendMessage(@PathVariable Integer customId, Model model) {
        Custom custom = this.customService.selectById(customId);
        model.addAttribute(custom);
        model.addAttribute("selectList",messageService.messageList());
        model.addAttribute("way","single");
        LogObjectHolder.me().set(custom);
        return PREFIX + "send_message.html";
    }

    /**
     * 短信界面跳转
     * @param model
     * @return
     */
    @RequestMapping(value = "/to_sendMoreMessage")
    public String to_sendMoreMessage(Model model) {
        model.addAttribute("way","sendMoreMessage");
        model.addAttribute("selectList",messageService.messageList());
        return PREFIX + "send_message.html";
    }

    /**
     * 短信界面跳转
     * @param model
     * @return
     */
    @RequestMapping(value = "/to_sendSearchMessage")
    public String to_sendSearchMessage(Model model) {
        model.addAttribute("way","sendSearchMessage");
        model.addAttribute("selectList",messageService.messageList());
        return PREFIX + "send_message.html";
    }

    /**
     * 短信批次界面跳转
     * @param model
     * @return
     */
    @RequestMapping(value = "/to_sendBatchMessage")
    public String to_sendBatchMessage(@RequestParam("way") String way,Model model) {
//        List<HjSmsBatch> list = hjSmsBatchService.selectList(new EntityWrapper<HjSmsBatch>().and("status in (0,1,5)"));
        model.addAttribute("way",way);
//        model.addAttribute("selectList",list);
        return PREFIX + "batchMessage.html";
    }

    /**
     *快速分案坐席
     */
    @RequestMapping(value = "/message-batch-list")
    @ResponseBody
    public Object messageBatchList(@RequestParam(value = "name", required = false) String name, Model model) {
        Page<HjSmsBatch> page = new PageFactory<HjSmsBatch>().defaultPage();
        List listSms = hjSmsBatchService.selectHjSmsBatchList(page,-1,"sms_batch_name",name,"","",-1,"msg", page.getOrderByField(), page.isAsc());
        Map<String, Object> map = new HashMap<>(10);
        map.put("code",0);
        map.put("count",page.getTotal());
        map.put("data", listSms);
        return map;
    }

    /**
     * 添加客户到短信批次
     *
     */
    @RequestMapping(value = "/assign-message-batch")
    @ResponseBody
    public Object assignMessageBatch(@RequestParam(value = "ids", required = false) String ids,@RequestParam("batch_id") String batch_id,@RequestParam("way") String way){
        StringBuffer mobileArr = new StringBuffer();
        Integer newPhonesNum = 0;
        Map<String, Object> map = new HashMap<>(10);

        if(way.equals("sendBatchMessage")) {//选中添加
            List<Custom> customs = this.customService.selectList(new EntityWrapper<Custom>().setSqlSelect("phone").where("id in("+ids+")"));
            if(customs == null) {
                map.put("code",0);
                map.put("msg","页面错误！");
                return map;
            }

            for (Custom custom : customs) {
                mobileArr.append(","+custom.getPhone());
            }
            newPhonesNum = customs.size();
        } else if(way.equals("searchBatchMessage")) {//查询添加
            String column = ShiroKit.getSessionAttr("column");
            String condition = ShiroKit.getSessionAttr("condition");
            String beginTime = ShiroKit.getSessionAttr("beginTime");
            String endTime = ShiroKit.getSessionAttr("endTime");
            Integer status = ShiroKit.getSessionAttr("status");
            String update_beginTime = ShiroKit.getSessionAttr("update_beginTime");
            String update_endTime = ShiroKit.getSessionAttr("update_endTime");
            List<Custom> customs = customService.selectAssignCustomList(column,condition,beginTime,endTime,update_beginTime,update_endTime,status,null,null);

            if(customs == null) {
                map.put("code",0);
                map.put("msg","页面错误！");
                return map;
            }

            for (Custom custom : customs) {
                mobileArr.append(","+custom.getPhone());
            }
            newPhonesNum = customs.size();
        }
        String mobiles = mobileArr.toString();

        HjSmsBatch hjSmsBatch = hjSmsBatchService.selectById(batch_id);

        String phones = hjSmsBatch.getPhones();
        Integer leftCapacity = hjSmsBatch.getLeftCapacity();

        if(phones.length() == 0) {
            mobiles = mobiles.substring(1,mobileArr.length());
        }

        if(batch_id != null && mobiles != "") {
            hjSmsBatch.setPhones(phones+mobiles);
            hjSmsBatch.setLeftCapacity(leftCapacity-newPhonesNum > 0 ? leftCapacity-newPhonesNum:0 );
            hjSmsBatchService.updateById(hjSmsBatch);
            map.put("code",1);
            map.put("msg","提交成功！");

        } else {
            map.put("code",0);
            map.put("msg","提交失败！");
        }

        return map;
    }


    /**
     * 发送短信
     */
    @ResponseBody
    @RequestMapping(value = "sendMessage")
    public Object sendMessage(@RequestParam("customId") int customId, @RequestParam("message") String message) {
        Custom custom = this.customService.selectById(customId);
        String phone = custom.getPhone();

        SendMsg client = SendMsg.getInstance();

        String uid = smsProperties.getUid();

        String key = smsProperties.getKey();

        //UTF发送
        int result = client.sendMsgUtf8(message, phone, uid, key);

        Integer code;
        String msg;
        if(result>0){
            code = 1;
            msg = "发送成功！发送条数："+result+"条";
        }else{
            code = 0;
            msg = client.getErrorMsg(result);
        }

        // 记录日志
        sendMessageLog(custom.getCustomName(), customId, message, code);

        return new JsonResult(code,msg, new Date());
    }

    /**
     * 群发短信
     */
    @ResponseBody
    @RequestMapping(value = "sendMoreMessage")
    public Object sendMoreMessage(@RequestParam("customId") String customIds, @RequestParam("message") String message) {
        List<Custom> phones = this.customService.selectList(new EntityWrapper<Custom>().setSqlSelect("phone").where("id in("+customIds+")"));
        StringBuffer mobileArr = new StringBuffer();
        for (Custom custom : phones) {
            mobileArr.append(custom.getPhone()+",");
        }

        String mobiles = mobileArr.toString().substring(0,mobileArr.length() - 1);

        Object result = doSendMassage(phones.size(), mobiles, message);

        return result;
    }
    /**
     * 查询结果群发短信
     */
    @ResponseBody
    @RequestMapping(value = "sendSearchMessage")
    public Object sendSearchMessage(@RequestParam("message") String message) {
        String column = ShiroKit.getSessionAttr("column");
        String condition = ShiroKit.getSessionAttr("condition");
        String beginTime = ShiroKit.getSessionAttr("beginTime");
        String endTime = ShiroKit.getSessionAttr("endTime");
        Integer status = ShiroKit.getSessionAttr("status");
        String update_beginTime = ShiroKit.getSessionAttr("update_beginTime");
        String update_endTime = ShiroKit.getSessionAttr("update_endTime");
        List<Custom> customs = customService.selectAssignCustomList(column,condition,beginTime,endTime,update_beginTime,update_endTime,status,null,null);
        StringBuffer mobileArr = new StringBuffer();
        for (Custom custom : customs) {
            mobileArr.append(custom.getPhone()+",");
        }

        String mobiles = mobileArr.toString().substring(0,mobileArr.length() - 1);

        Object result = doSendMassage(customs.size(),mobiles,message);

        return result;

    }

    public Object doSendMassage(Integer num,String mobiles, String message) {

        String uid = smsProperties.getUid();

        String user = smsProperties.getUser();

        String key = smsProperties.getKey();

        String sendUrl = smsProperties.getUrl();

        String backEncodType = smsProperties.getCode();

        Integer volume = Integer.parseInt(smsProperties.getVolume());


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式

        String timestamp = df.format(new Date());

        HjSmsBatch hjSmsBatch = new HjSmsBatch();

        hjSmsBatch.setSmsBatchName(df.format(new Date())); // 短信批次id
        hjSmsBatch.setBatchDesc(""); // 批次描述
        hjSmsBatch.setSmsContent(message); // 短信内容
        hjSmsBatch.setBatchCapacity(volume); // 批次容量
        hjSmsBatch.setLeftCapacity(volume-num > 0 ? volume-num:0); // 剩余容量
        hjSmsBatch.setStatus(0); // 待发送
        hjSmsBatch.setUserid(ShiroKit.getUser().getId()); // 操作人id
        hjSmsBatch.setCreateAt(new Date()); //创建时间
        hjSmsBatch.setPhones(mobiles);
        hjSmsBatchService.insert(hjSmsBatch);

        SmsClientAccessTool client = SmsClientAccessTool.getInstance();

        // 接口参数http://123.57.51.191:8888/sms.aspx
        String sendParam = "action=send&userid="+uid+"&account="+user+"&password="+key+"&mobile="+mobiles+"&content="+message.replaceAll(" ","")+"&sendTime=&extno=";
        //接口参数http://123.57.51.191:8888/v2sms.aspx
//        String sendParam = "action=send&userid="+uid+"&timestamp="+timestamp+"&sign="+MD5Util.encrypt(user+key+timestamp,backEncodType,true)+"&mobile="+mobiles+"&content="+message.replaceAll(" ","")+"&sendTime=&extno=";

        //UTF发送
        String result = client.sendPost(sendUrl,sendParam, backEncodType);

        Integer code = 0;
        String msg = "发送失败!";

        if(result != null) {
            if(regex(result,"returnstatus").equals("Faild")) {
                hjSmsBatch.setStatus(4); // 发送失败
                hjSmsBatch.setResultText(result);
                hjSmsBatch.setRetTaskId(regex(result,"taskID"));
                hjSmsBatchService.updateById(hjSmsBatch);
                code = 0;
                msg = regex(result,"message");
            } else if( regex(result,"returnstatus").equals("Success") ) {
                hjSmsBatch.setStatus(3); // 发送成功
                hjSmsBatch.setResultText(result);
                hjSmsBatch.setRetTaskId(regex(result,"taskID"));

                hjSmsBatchService.updateById(hjSmsBatch);

                code = 1;
                msg = "发送成功！<br>成功发送"+regex(result,"successCounts")+"条";
            }

        }

        return new JsonResult(code, msg, new Date());
    }


    /**		 
     * 获取指定标签中的内容		 
     * @param xml 传入的xml字符串		 
     * @param label  指定的标签中的内容		 
     */
    public static String regex(String xml,String label) {
        String context = "";		//正则表达式
        String rgex = "<"+label+">(.*?)</"+label+">";
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式    	       
        Matcher m = pattern.matcher(xml); //匹配的有多个	       
        List<String> list = new ArrayList<String>();
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }//只要匹配的第一个	       
        if(list.size()>0){
            context = list.get(0);
        }
        return context;
    }

    /**
     * 邮件转发
     */
    @ResponseBody
    @RequestMapping(value = "sendMail")
    public Object sendMail(@RequestParam("customId") int customId, @RequestParam("mailId") String mailId) {
        Custom custom = this.customService.selectById(customId);
        SendMailUtils mail = new SendMailUtils();
        JavaMailSenderImpl sender = initJavaMailSender();
        String[] ss = {mailId};
        String text = "客户姓名:"+custom.getCustomName()+
                "<br>手机号码:"+custom.getPhone()+
                "<br>邮箱:"+custom.getEmail()+
                "<br>客户公司:"+custom.getCompany()+
                "<br>联系地址:"+custom.getAddress()+
                "<br>负责人:"+custom.getPrincipal()+
                "<br>标签:"+custom.getLabel()+
                "<br>客户来源:"+custom.getCustomSource()+
                "<br>首次联系时间:"+custom.getFirstContactTime()+
                "<br>联系次数:"+custom.getContactTimes()+
                "<br>客户备注:"+custom.getNote()+
                "<br>创建时间:"+DateUtil.getTime(custom.getCreatedTime())+
                "<br>预约时间:"+custom.getReserveTime();
        Integer code = mail.sendTextWithHtml(sender, ss,"电销系统客户转发",text);

        // 记录日志
        sendMailLog(custom.getCustomName(), customId, mailId, code);
        String result;
        if (code == 1) {
            result = "发送成功！";
        } else {
            result = "发送失败！";
        }

        return new JsonResult(code, result, new Date());
    }

    /**
     * 邮件发送日志
     * @param customName
     * @param custom_id
     * @param mailId
     * @param result
     */
    public void sendMailLog(String customName,Integer custom_id,String mailId, Integer result) {

        SendMailLog log = new SendMailLog();
        log.setCreateIp(HttpKit.getIp());
        log.setCustomId(custom_id);
        log.setCustomName(customName);
        log.setUserId(ShiroKit.getUser().getId());
        log.setUserName(ShiroKit.getUser().getName());
        log.setEmailAddress(mailId);
        log.setResult(result);
        log.setSendTime(new Date());

        this.mailLogService.insert(log);
    }

    /**
     * 短息发送日志
     * @param customName
     * @param custom_id
     * @param message
     * @param code
     */
    public void sendMessageLog(String customName,Integer custom_id,String message, Integer code) {

        SendMessageLog log = new SendMessageLog();

        log.setCreateIp(HttpKit.getIp());
        log.setCustomId(custom_id);
        log.setCustomName(customName);
        log.setUserId(ShiroKit.getUser().getId());
        log.setUserName(ShiroKit.getUser().getName());
        log.setResult(code);
        log.setMessage(message);
        log.setSendTime(new Date());

        this.messageLogService.insert(log);
    }

    /**
     * 跳转到我的客户首页
     */
    @RequestMapping("/my_custom")
    public String myCustom(Model model) {
        // add by eric 2018-09-25 start
//        List<Dict> list = dictService.selectList(new EntityWrapper<Dict>().and("code='call_status'"));
        Integer product_id = ShiroKit.getUser().getProductId();
        List<Dict> list = dictService.selectAllByCode(product_id,0, "call_status");
        model.addAttribute("successCode", list);
        // add by eric 2018-09-25 end

        return PREFIX + "my_custom.html";
    }

    /**
     * 获取客户管理列表
     */
    @RequestMapping(value = "/my-custom-list")
    @ResponseBody
    public Object myCustomList(@RequestParam(required = false) String column, @RequestParam(required = false) String condition,@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime,@RequestParam(required = false) String update_beginTime, @RequestParam(required = false) String update_endTime,  @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer call_status_id, @RequestParam(required = false) Integer check_status,@RequestParam(required = true, defaultValue = "1") Integer type) {
        Page<Custom> page = new PageFactory<Custom>().defaultPage();
        Integer userid = ShiroKit.getUser().getId();
        List result = customService.selectCustomList(page,userid,column,condition,beginTime,endTime,update_beginTime,update_endTime,status,call_status_id,check_status, type, page.getOrderByField(), page.isAsc());
        page.setRecords((List<Custom>) new CustomWarpper(result).warp());
        Map<String, Object> map = new HashMap<>(10);
        map.put("code",0);
        map.put("count",page.getTotal());
        map.put("data", result);
        return map;
    }

    @RequestMapping(value = "/edit-nx-custom")
    @ResponseBody
    public Object editNxCustom(@RequestParam(required = true) String name, @RequestParam(required = true) String value, @RequestParam(required = true) Integer case_no) {
        if (name.equals("")) {
            return new JsonResult(201, "抱歉缺少相关参数，请重新执行操作！", new Date());
        }

        Custom custom = customService.selectById(case_no);
        AqCustom aqCustomNew = new AqCustom();
        if (custom.getAqCustom() == null) {

        } else {
            String aqCustom = custom.getAqCustom();
            JSONObject obj = new JSONObject().fromObject(aqCustom);//将json字符串转换为json对象
            aqCustomNew = (AqCustom) JSONObject.toBean(obj,AqCustom.class);//将建json对象转换为Person对象
        }

        switch (name) {
            case "sex" :
                aqCustomNew.setSex(value);
                break;
            case "jobCity" :
                aqCustomNew.setJobCity(value);
                break;
            case "companyName" :
                aqCustomNew.setCompanyName(value);
                break;
            case "jobType" :
                aqCustomNew.setJobType(value);
                break;
            case "cardBank" :
                aqCustomNew.setCardBank(value);
                break;
            case "customEducationBackground" :
                aqCustomNew.setCustomEducationBackground(value);
                break;
            case "graduateSchool" :
                aqCustomNew.setGraduateSchool(value);
                break;
            case "applyCardType" :
                aqCustomNew.setApplyCardType(value);
                break;
            case "dataChannel" :
                aqCustomNew.setDataChannel(value);
                break;
            case "dataBatch" :
                aqCustomNew.setDataBatch(value);
                break;
            case "otherCustomers" :
                aqCustomNew.setOtherCustomers(value);
                break;
            case "havingOtherCard" :
                aqCustomNew.setHavingOtherCard(value);
                break;
            case "cardUseTime" :
                aqCustomNew.setCardUseTime(value);
                break;
            case "isCorporateShareholders" :
                aqCustomNew.setIsCorporateShareholders(value);
                break;
            case "convenientTime" :
                aqCustomNew.setConvenientTime(value);
                break;
            case "custom_name" :
                custom.setCustomName(value);
                break;
            case "email" :
                custom.setEmail(value);
                break;
            case "custom_source" :
                custom.setCustomSource(value);
                break;
            case "address" :
                custom.setAddress(value);
                break;
            case "first_contact_time" :
                Date first_contact_time = DateUtil.parseTime(value);
                custom.setFirstContactTime(first_contact_time);
                break;

        }

        JSONObject json = JSONObject.fromObject(aqCustomNew);//将java对象转换为json对象
        String aqCustomStr = json.toString();//将json对象转换为字符串
        custom.setAqCustom(aqCustomStr);
        customService.updateById(custom);
        return new JsonResult(200, "修改成功！", new Date());
    }


    /**
     * 导出客户
     *
     * @return
     */
    @RequestMapping(value = "/export-custom")
    @ResponseBody
    public void export(@RequestParam(required = false) String column,@RequestParam(required = false) String condition,@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime,@RequestParam(required = false) String update_beginTime, @RequestParam(required = false) String update_endTime, @RequestParam(required = false, defaultValue = "-1") Integer status, @RequestParam(required = false, defaultValue = "0") Integer isCall,  @RequestParam(required = false, defaultValue = "-1") Integer call_status_id,@RequestParam(required = false, defaultValue = "1") Integer page,@RequestParam(required = false, defaultValue = "10000") Integer limit, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        List<Custom> result = customService.selectAssignCustomList(column,condition,beginTime,endTime,update_beginTime,update_endTime,status);
        EntityWrapper<Custom> entityWrapper = new EntityWrapper<>();
        Integer product_id = ShiroKit.getUser().getProductId();
        entityWrapper.and("product_id="+product_id);

        if ((!condition.isEmpty() && condition != null) && (!column.isEmpty() && column != null)) {
            if (column.equals("name")) {
                List<Integer> ids = new ArrayList<>();
                List<User> users = userService.selectList(new EntityWrapper<User>().and("name like '%"+ condition +"%' and productid=" + product_id + " "));
                ids =users.stream().map(User::getId).collect(Collectors.toList());
                entityWrapper.in("user_id", ids);
            } else {
                entityWrapper.and(column + " like '%"+ condition +"%'");
            }
        }
        if ((!beginTime.isEmpty() && beginTime != null) && (!endTime.isEmpty() && endTime != null)) {
            entityWrapper.and("created_at between '"+ beginTime +"' and '"+ endTime +"'");
        }
        if ((!update_beginTime.isEmpty() && update_beginTime != null) && (!update_endTime.isEmpty() && update_endTime != null)) {
            entityWrapper.and("updated_time between '"+ update_beginTime +"' and '"+ update_endTime +"'");
        }
        if (status >= 0 && status != null) {
            entityWrapper.and("status =" + status);
        }

        if(isCall != null) {
            if(isCall == 1) {
                entityWrapper.and("contact_times = 0");
            } else if(isCall == 2) {
                entityWrapper.and("contact_times > 0");
            }
        }
        if (ShiroKit.hasRole("worker")) {       //坐席角色  客户列表展示本公司且属于自己的  和我的客户展示一样
            Integer userid = ShiroKit.getUser().getId();
            entityWrapper.and("user_id =" + userid);
        }
        if(call_status_id != -1) {
            entityWrapper.and("call_status_id =" + call_status_id);
        }
        entityWrapper.orderBy("created_time DESC");

        List<Custom> result = customService.selectPage(
                new Page<Custom>(page, limit),
                entityWrapper
        ).getRecords();
//        List<Custom> result = customService.selectList(entityWrapper);
        //excel标题
        String[] title = {"处理状态","数据状态","组别","坐席工号", "客户姓名", "性别", "手机号码", "城市", "工作城市", "公司名称","工作类型","是否持有他行卡","持卡行","卡片使用时间","申请卡种","是否企业主股东","客户学历", "毕业院校", "导入人","导入时间","方便联系时间","备注","数据渠道","数据批次","他有客群","首次外呼时间", "提交时间","总拨打次数","拨打结果","质检时间","质检工号"};

//        String[] title = {"坐席工号", "客户姓名", "性别", "手机号码", "工作城市","公司名称","工作类型","是否持有他行卡","持卡行","卡片使用时间","是否企业主股东",
//                "客户学历", "毕业院校", "方便练习时间", "申请卡种", "备注","拨打日期","数据渠道","数据批次","他有客群"};

        //excel文件名
        String fileName = "客户表" + DateUtil.getAllTime() + ".xls";
        System.out.println(2);
        //sheet名
        String sheetName = "客户表";

        String[][] content = new String[result.size()][];

        for (int i = 0; i < result.size(); i++) {
            content[i] = new String[title.length];
            Custom obj = result.get(i);

            AqCustom aqCustomNew = new AqCustom();
            if (obj.getAqCustom() == null) {

            } else {
                String aqCustom = obj.getAqCustom();
                JSONObject jsonObj = new JSONObject().fromObject(aqCustom);//将json字符串转换为json对象
                aqCustomNew = (AqCustom) JSONObject.toBean(jsonObj,AqCustom.class);//将建json对象转换为Person对象
            }

            content[i][0] = obj.getLtCheckStatus() != null && obj.getLtCheckStatus() > 0 ? (obj.getLtCheckStatus() == 1?"质检通过":"质检不通过") :"未质检";    //处理状态
            content[i][1] = obj.getCallStatusName();    //数据状态
            content[i][2] = "";    //组别
            content[i][3] = ConstantFactory.me().getUserAccountById(obj.getUserId());    //坐席工号
            content[i][4] = obj.getCustomName();    //客户姓名
            content[i][5] = aqCustomNew.getSex();    //性别
            content[i][6] = obj.getPhone();     //手机号
            content[i][7] = obj.getAddress();     //城市，手机号码归属地
            content[i][8] = aqCustomNew.getJobCity();    //工作城市
            content[i][9] = aqCustomNew.getCompanyName();    //公司名称
            content[i][10] = aqCustomNew.getJobType();    //工作类型
            content[i][11] = aqCustomNew.getHavingOtherCard();    //是否持有他行卡
            content[i][12] = aqCustomNew.getCardBank();    //持卡行
            content[i][13] = aqCustomNew.getCardUseTime();    //卡片使用时间
            content[i][14] = aqCustomNew.getApplyCardType();    //申请卡种
            content[i][15] = aqCustomNew.getIsCorporateShareholders();    //是否企业主股东
            content[i][16] = aqCustomNew.getCustomEducationBackground();     //客户学历
            content[i][17] = aqCustomNew.getGraduateSchool();    //毕业院校
            content[i][18] = obj.getImportName();    //导入人
            content[i][19] = obj.getCreatedTime() == null ? "" : DateUtil.formatDate(obj.getCreatedTime(), "yyyy-MM-dd HH:mm:ss");    //导入时间
            content[i][20] = aqCustomNew.getConvenientTime();    //方便联系时间
            content[i][21] = obj.getNote();    //备注
            content[i][22] = aqCustomNew.getDataChannel();    //数据渠道
            content[i][23] = obj.getBatchNo();    //数据批次
            content[i][24] = aqCustomNew.getOtherCustomers();    //他有客群
            content[i][25] = obj.getFirstContactTime() == null ? "" : DateUtil.formatDate(obj.getFirstContactTime(), "yyyy-MM-dd HH:mm:ss");    //首次外呼时间
            content[i][26] = obj.getLastContactTime() == null ? DateUtil.formatDate(obj.getUpdatedTime(), "yyyy-MM-dd HH:mm:ss") : DateUtil.formatDate(obj.getLastContactTime(), "yyyy-MM-dd HH:mm:ss");    //提交时间  客户单提交时间不存在则用更新时间
            content[i][27] = Integer.toString(obj.getContactTimes());    //总拨打次数
            content[i][28] = obj.getCallNote();    //拨打结果
            content[i][29] = obj.getLtCheckDate() == null ? "" : DateUtil.formatDate(obj.getLtCheckDate(), "yyyy-MM-dd HH:mm:ss");    //质检时间
            content[i][30] = ConstantFactory.me().getUserAccountById(obj.getLtChecker());    //质检工号
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        OutputStream os = null;
        try {
            ToolUtil.setResponseHeader(response, fileName);
            os = response.getOutputStream();
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                ToolUtil.closeObject(os);
            }catch (Exception ex){
               // 此处可写日志
            }
        }
    }

    /**
     * 黑名单数据处理
     */
    @RequestMapping(value = "/custom_black_list")
    public String customBackList (Model model) {
        //查找项目
        ShiroUser shiroUser = ShiroKit.getUser();
        EntityWrapper<Product> entityWrapper = new EntityWrapper<>();
        if (ShiroKit.isAdmin()) {

        } else {
            entityWrapper.and("id =" + shiroUser.getProductId());
        }
        List<Product> products = productService.selectList(entityWrapper);
        model.addAttribute("products", products);
        return PREFIX + "custom_black_list.html";
    }


    @RequestMapping(method = RequestMethod.POST, path = "/customer_black_import")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Object customerBlackImport(@RequestParam(required = true) String filelink, @RequestParam(required = true) Integer productid) {
        try {
            long startTime=System.currentTimeMillis();   //获取开始时间
            String file_path = hjzdProperties.getFileUploadPath() + filelink;
            Collection<Map> list = customService.parseExcel(file_path);

            int r = 0;
            ArrayList<String> ids = new ArrayList<>();
            for (Map map : list) {
                for(Object map_key: map.keySet()){
                    //查询手机号存在性
                    Custom black_custom = customService.selectOne(new EntityWrapper<Custom>().and("is_del = 0 and product_id="+productid+" and phone="+map.get(map_key).toString()));
                    if (black_custom != null) {
                        ids.add(black_custom.getId().toString());
                        r++;
                    }
                }
            }
            if (ids.size() > 0) {
                int size=ids.size();
                String[] array=new String[size];
                for(int i=0;i<ids.size();i++){
                    array[i]=ids.get(i);
                }
                customService.deleteCustomByIds(array);
            }
            long endTime=System.currentTimeMillis(); //获取结束时间

            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
            return new JsonResult(200,"该批黑名单已禁用","黑名单已禁用");
        } catch (Exception e) {
            return new JsonResult(201,"该批黑名单禁用失败",e);
        }
    }
}
