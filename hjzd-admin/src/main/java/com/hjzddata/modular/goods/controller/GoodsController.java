package com.hjzddata.modular.goods.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.GoodsDict;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.support.HttpKit;
import com.hjzddata.core.util.WebSocketServer;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.custom.model.Custom;
import com.hjzddata.modular.custom.service.ICustomService;
import com.hjzddata.modular.goods.service.IGoodsService;

import com.hjzddata.modular.goods.model.Goods;

import com.hjzddata.modular.system.model.CallConfig;
import com.hjzddata.modular.system.model.Dept;
import com.hjzddata.modular.system.service.ICallConfigService;
import com.hjzddata.modular.system.service.IDeptService;
import com.hjzddata.modular.task.model.CallLog;
import com.hjzddata.modular.task.service.ICallLogService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理控制器
 */
@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController {

    private String PREFIX = "/goods/goods/";

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ICallConfigService callConfigService;

    @Autowired
    private ICallLogService callLogService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private ICustomService customService;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 跳转到商品管理首页
     */
    @RequestMapping()
    public String index(@RequestParam(value = "condition", required = false) String condition, Model model) {
        model.addAttribute("condition",condition);
        return PREFIX + "goods.html";
    }


    /**
     * 跳转到添加商品管理
     */
    @RequestMapping("/goods_upload")
    public String goodsUpload() {
        return PREFIX + "goods_upload.html";
    }


    /**
     * 商品导入
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadfile(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            Object resultJson = goodsService.batchImport(fileName, file);
            return resultJson;
        } catch (Exception e) {
            System.out.println( e);
            return new JsonResult(201, "表格格式出现问题，导入失败！", new Date());
        }

    }


    @RequestMapping(path = "/download-tpl", method = RequestMethod.GET)
    public void downloadTpl(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //ClassLoader classLoader = getClass().getClassLoader();
        //URL url = classLoader.getResource("xls/" + fileName);

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("xls/" + fileName);
        //File file = new File(url.getFile());
        String exportFileName = "商品导入模板.xls";
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
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public String uploadFile(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception{
//        if(file==null)
//            return  "上传文件不能为空";
//        String fileName = file.getOriginalFilename();
//        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
//            return "上传文件格式错误，请上传后缀为.xls或.xlsx的文件";
//        }
//
//        String filePath = request.getSession().getServletContext().getRealPath("upload/");
//        String path = filePath+fileName;
//        try {
//            File targetFile = new File(filePath);
//            if(!targetFile.exists()){
//                targetFile.mkdirs();
//            }
//            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
//            out.write(file.getBytes());
//            out.flush();
//            out.close();
//            goodsService.batchImport(fileName, file);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "上传失败";
//        }
//        return "SUCCESS";
//    }


    /**
     * 跳转到添加商品管理
     */
    @RequestMapping("/goods_add")
    public String goodsAdd() {
        return PREFIX + "goods_add.html";
    }

    /**
     * 跳转到修改商品管理
     */
    @RequestMapping("/goods_update/{goodsId}")
    public String goodsUpdate(@PathVariable Integer goodsId, Model model) {
        Goods goods = goodsService.selectById(goodsId);
        model.addAttribute("item",goods);
        LogObjectHolder.me().set(goods);
        return PREFIX + "goods_edit.html";
    }

    /**
     * 获取商品管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, Integer isSale) {
        Page<Goods> page = new PageFactory<Goods>().defaultPage();
        /*List result = goodsService.getGoods(page, condition, isSale, page.getOrderByField(), page.isAsc());
        page.setRecords(result);
        return super.packForBT(page);*/
        List result = goodsService.getGoods(page, condition, isSale, page.getOrderByField(), page.isAsc());
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",result);
        return list;
    }

    /**
     * 新增商品管理
     */
    @BussinessLog(value = "添加商品",  key = "goodsName", dict = GoodsDict.class)
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Object add(Goods goods) {
        goods.setCreatedAt(new Date());
        goods.setUpdatedAt(new Date());
        goodsService.insert(goods);
        return SUCCESS_TIP;
    }

    /**
     * 删除商品管理
     */
    @BussinessLog(value = "删除商品",  key = "id", dict = GoodsDict.class)
    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Object delete(@RequestParam Integer id) {
        //缓存被删除的商品名称
        LogObjectHolder.me().set(ConstantFactory.me().getGoodsName(id));
        goodsService.deleteById(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改商品上下架状态
     * @param id
     * @return
     */
    @BussinessLog(value = "修改商品上下架状态",  key = "id", dict = GoodsDict.class)
    @RequestMapping(value = "/update-isSale", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Object updateIsSale(@RequestParam Integer id) {
        Goods goods = goodsService.selectById(id);
        Integer isSale = (goods.getIsSale() + 1) % 2;   //取余
        goods.setIsSale(isSale);
        goodsService.updateById(goods);
        return SUCCESS_TIP;
    }

    /**
     * 修改商品管理
     */
    @BussinessLog(value = "修改商品",  key = "id", dict = GoodsDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Object update(Goods goods) {
        goods.setUpdatedAt(new Date());
        goodsService.updateById(goods);
        return SUCCESS_TIP;
    }

    /**
     * 商品管理详情
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Object detail(@PathVariable("goodsId") Integer goodsId) {
        return goodsService.selectById(goodsId);
    }

    /**
     * 跳转到外呼测试页面
     */
    @RequestMapping("/call")
    public String call() {
        return PREFIX + "call.html";
    }

    /**
     * 跳转到外呼工作台
     */
//    @RequestMapping("/to-cc")
////    public String toCc() {
////        return PREFIX + "login-cc.html";
////    }

    /**
     * 跳转到外呼测试页面
     */
    @RequestMapping("/call-login")
    public String callLogin(Model model) {
        Integer baseId = ShiroKit.getUser().getDeptId();
        String call_system_name = "";
        if (baseId != null) {
            Dept dept = deptService.selectById(baseId);
            Integer call_system = dept.getCallSystemId();
            call_system_name = ConstantFactory.me().getCallSystemName((call_system));
        /*CallConfig callConfig = callConfigService.getcallConfig(call_system, baseId);
        String config = callConfig.getConfig();
        String[] configArr = config.split("\\|");
        String cc_ipaddress, cc_model_id, cc_engine, cc_port;
        if (call_system_name.equals("汉天")) {
            cc_ipaddress = configArr[0];
            cc_model_id = "1";
            cc_engine = "CE";
            cc_port = "5070";
        } else {
            cc_ipaddress = configArr[0];
            cc_model_id = "1";
            cc_engine = "CJI";
            cc_port = "5060";
        }
        Cookie cookie_cc_ipaddress = new Cookie("cc_ipaddress",cc_ipaddress);
        Cookie cookie_cc_model_id = new Cookie("cc_model_id",cc_model_id);
        Cookie cookie_cc_engine = new Cookie("cc_engine",cc_engine);
        Cookie cookie_cc_port = new Cookie("cc_port",cc_port);
        cookie_cc_ipaddress.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
        cookie_cc_model_id.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
        cookie_cc_engine.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
        cookie_cc_port.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
        getHttpServletResponse().addCookie(cookie_cc_ipaddress);
        getHttpServletResponse().addCookie(cookie_cc_model_id);
        getHttpServletResponse().addCookie(cookie_cc_engine);
        getHttpServletResponse().addCookie(cookie_cc_port);*/
        }
        model.addAttribute("call_system_name", call_system_name);
        return PREFIX + "call-login.html";
    }

    @RequestMapping("/call-system")
    @ResponseBody
//    public Object callSystem(@RequestParam("call_system") Integer call_system) {
    public Object callSystem() {
        Integer baseId = ShiroKit.getUser().getDeptId();
        Dept dept = deptService.selectById(baseId);
        Integer call_system = dept.getCallSystemId();

        CallConfig callConfig = callConfigService.getcallConfig(call_system, baseId);
        String config = callConfig.getConfig();
        String[] configArr = config.split("\\|");

        /*Cookie callConfigCookie = new Cookie("callConfigCookie",config);
        callConfigCookie.setMaxAge(-1);//0立即删除 -1浏览器关闭删除
        getHttpServletResponse().addCookie(callConfigCookie);*/

        return configArr;
    }

    /**
     * 添加通话记录
     * @param call_record
     * @return
     */
    @RequestMapping("/save-call-log")
    @ResponseBody
    @Transactional(readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    public Object saveCallLog(CallLog call_record) {
        call_record.setUserId(ShiroKit.getUser().id);
        call_record.setUserName(ShiroKit.getUser().name);
        call_record.setCreatedAt(new Date());
        call_record.setCreatedIp(HttpKit.getIp());
        call_record.setCallStartTime(new Date());
        callLogService.insert(call_record);

        //更新客户联系次数 +1  ；更新客户表首次联系时间
        Integer custom_id = call_record.getCaseId();
        Custom custom = customService.selectById(custom_id);
        custom.setContactTimes(custom.getContactTimes()+1);
        if (custom.getFirstContactTime() == null) {
            custom.setFirstContactTime(new Date());
        }
        customService.updateById(custom);

        Integer call_log_id = call_record.getId();
        JsonResult jsonResult = new JsonResult(200,"通话记录添加成功", call_log_id);
        return jsonResult;
    }

    /**
     * 更新通话记录
     * @param call_record
     * @return
     */
    @RequestMapping("/update-call-log")
    @ResponseBody
    public Object updateCallLog(CallLog call_record) throws IOException{
        call_record.setCallEndTime(new Date());
        callLogService.updateById(call_record);
        JsonResult jsonResult = new JsonResult(200,"通话记录更新成功", call_record.getId());
//        webSocketServer.sendtoUser("更新通话日志", ShiroKit.getUser().getId()+"");
        return jsonResult;
    }

    /**
     * 录音
     * @return
     */
    /*@RequestMapping("/get-record-address")
    @ResponseBody
    public Object getRecordAddress() {
        call_record = callLogService.selectById(call_record.getId());
        Cookie[] cookies = getHttpServletRequest().getCookies();
        String callConfigCookie = "";
        for (Cookie cookie : cookies) {
            switch(cookie.getName()){
                case "callConfigCookie":
                    callConfigCookie = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
        String[] configArr = callConfigCookie.split("\\|");
        //获取通话记录session_id
        Object session_id = CallLog.getCallSessionId(call_record.getCallUserdata(), configArr);
        return SUCCESS_TIP;
    }*/






}
