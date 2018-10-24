package com.hjzddata.modular.task.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.constant.factory.PageFactory;

import com.hjzddata.core.excel.ExcelUtil;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.util.DateUtil;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.custom.model.Custom;
import com.hjzddata.modular.custom.service.ICustomService;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.system.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.hjzddata.modular.task.model.CallLog;
import com.hjzddata.modular.task.service.ICallLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通话日志控制器
 *
 * @author fengshuonan
 * @Date 2018-07-18 09:59:21
 */
@Controller
@RequestMapping("/callLog")
public class CallLogController extends BaseController {

    private String PREFIX = "/task/callLog/";

    @Autowired
    private ICallLogService callLogService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private ICustomService customService;

    /**
     * 跳转到通话日志首页
     */
    @RequestMapping("")
    public String index(Model model) {
        /*Integer dic_id = dictService.selectOne(new EntityWrapper<Dict>().and("name='已接通'")).getId();
        List<Dict> list = dictService.selectList(new EntityWrapper<Dict>().and("pid="+dic_id));*/
        Integer product_id = ShiroKit.getUser().getProductId();
        List<Dict> list = dictService.selectAllByCode(product_id,0, "call_status");
        model.addAttribute("successCode", list);
        return PREFIX + "callLog.html";
    }

    /**
     * 跳转到添加通话日志
     */
    @RequestMapping("/callLog_add")
    public String callLogAdd() {
        return PREFIX + "callLog_add.html";
    }

    /**
     * 跳转到修改通话日志
     */
    @RequestMapping("/callLog_update/{callLogId}")
    public String callLogUpdate(@PathVariable Integer callLogId, Model model) {
        CallLog callLog = callLogService.selectById(callLogId);
        model.addAttribute("item",callLog);
        LogObjectHolder.me().set(callLog);
        return PREFIX + "callLog_edit.html";
    }

    /**
     * 获取通话日志列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) Integer caseId,@RequestParam(required = false) String column,@RequestParam(required = false) String condition,@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime,@RequestParam(required = false) String checkStatus,  @RequestParam(required = false, defaultValue = "-1") Integer call_status_id) {
        Page<CallLog> page = new PageFactory<CallLog>().defaultPage();

        List result = callLogService.selectCallList(page,caseId,column,condition,beginTime,endTime,checkStatus,call_status_id,page.getOrderByField(), page.isAsc());

        Map<String, Object> map = new HashMap<>(10);
        map.put("code",0);
        map.put("count",page.getTotal());
        map.put("data", result);
        return map;

    }

    /**
     * 新增通话日志
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CallLog callLog) {
        callLogService.insert(callLog);
        return SUCCESS_TIP;
    }

    /**
     * 删除通话日志
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer callLogId) {
        callLogService.deleteById(callLogId);
        return SUCCESS_TIP;
    }

    /**
     * 修改通话日志
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CallLog callLog) {
        callLogService.updateById(callLog);
        return SUCCESS_TIP;
    }

    /**
     * @desc Add by eric 2018-09-18 start
     * @param recordId
     * @param checkStatus
     * @return
     */
    @RequestMapping(value="/check-record")
    @ResponseBody
    public Object checkRecord(@RequestParam Integer recordId,@RequestParam Integer checkStatus) {
        CallLog clog  = callLogService.selectById(recordId);
        clog.setCheckStatus(checkStatus);
        clog.setCheckDate(new Date());
        clog.setChecker(ShiroKit.getUser().getId());
        clog.updateById();
        return SUCCESS_TIP;
    }


    /**
     * 通话日志详情
     */
    @RequestMapping(value = "/detail/{callLogId}")
    @ResponseBody
    public Object detail(@PathVariable("callLogId") Integer callLogId) {
        return callLogService.selectById(callLogId);
    }


    /**
     * 获取astercc录音
     * 本方法只适用于astercc呼叫系统
     * @return
     */
    @RequestMapping(value = "/get-record-address")
    @ResponseBody
    public Map<String, String> getRecordAddress(@RequestParam Integer callLogId,@RequestParam(required = false) String way) {

        CallLog call_record = callLogService.selectById(callLogId);

        String callConfig = callLogService.getCallConfig(call_record.getUserId());
//        String callConfig = "10.254.250.4|astercc|2|admin|admin|183.161.30.139:8081";
        //自定义astercc参数 (该参数由后期确定怎么绑定呼叫账号确定)
        String[] configArr = callConfig.split("\\|");
        String callDate = call_record.getCallStartTime() == null ? "" : DateUtil.formatDate(call_record.getCallStartTime(), "yyyy-MM-dd");

        String sessionid = "";

        // 声明返回值
        Map<String, String> result = new HashMap<>();

        if(call_record.getCallSessionid() != null) {
            sessionid = call_record.getCallSessionid();
        } else {
            if(call_record.getCallUserdata() == null){
                result.put("code", "201");
                result.put("data", "暂无录音");
                return result;
            };
            //获取通话记录session_id子串
            //正常返回值"{"result":"success","msg":{"data":[{"sessionid":"a871aad074f18fdd2543c7ad91a393e4",...}],"totalCount":1}}"
            Object session_id = CallLog.getCallSessionId(call_record.getCallUserdata(), configArr);

            // 解析返回值
            // 正常返回值"{"result":"success","msg":{"data":[{"sessionid":"a871aad074f18fdd2543c7ad91a393e4",...}],"totalCount":1}}"
            // 无录音 返回值data数组为空，result=fail

            JSONObject resultobj = new JSONObject().fromObject(session_id); //转为json对象
            if(resultobj.optString("result").equals("success")) {
                JSONArray dataArr = resultobj.optJSONObject("msg").optJSONArray("data");  //获取data的json数组
                if (dataArr.isEmpty()) {
                    result.put("code", "201");
                    result.put("data", "暂无录音");
                    return result;
                } else {
                    JSONObject dataObject = new JSONObject().fromObject(dataArr.optString(0));
                    //获取到sessionid
                    sessionid = dataObject.optString("sessionid");
                }
            }else {
                result.put("code","201");
                result.put("data","暂无录音");
                return result;
            }
        }
        String recordIp = configArr.length == 6 ? configArr[5] : configArr[0];

        //根据sessionid获取录音地址
        String addrs = CallLog.getRecordingAddr(sessionid, callDate, recordIp, configArr[0]);
        //正则判断录音是否存在
        if(!addrs.matches("^(.*)mp3$")) {
            result.put("code","201");
            result.put("data","暂无录音");
            return result;
        }

        if(way != null && way.equals("download")) { //下载
            String fileName = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+"_"+ShiroKit.getUser().getId();
            ShiroKit.setSessionAttr("record_name",fileName);
            saveUrlAs(addrs,"luyin",fileName);
            result.put("code","210");
            return result;
        } else {
            //构造返回值
            result.put("code","200");
            result.put("data",addrs);
            return result;
        }
    }

    @RequestMapping(value = "/download-record")
    @ResponseBody
    public HttpServletResponse download(@RequestParam() String phone,@RequestParam() String cc_user,@RequestParam() String start, HttpServletResponse response) {

        String fileName = ShiroKit.getSessionAttr("record_name");

        if(fileName.equals("") || fileName.equals(null)) {
            response.setHeader("error","暂无录音");
            return response;
        }

        String path = "luyin/"+fileName+".mp3";
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + phone+"_"+cc_user+"_"+start+".mp3");
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            file.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * @功能 下载临时素材接口
     * @param filePath 文件将要保存的目录
     * @param fileName 录音文件名
     * @param url 请求的路径
     * @return
     */
    public File saveUrlAs(String url,String filePath,String fileName){

        File file=new File(filePath);       //判断文件夹是否存在
        if (!file.exists()) {       //如果文件夹不存在，则创建新的的文件夹
             file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try{        // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();      //以Post方式提交表单，默认get方式
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);     // post方式不能使用缓存
            conn.setUseCaches(false);       //连接指定的资源
            conn.connect();     //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);     //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }       //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath+fileName+".mp3");
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);
            byte[] buf = new byte[4096];
            int length = bis.read(buf);     //保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }
        return file;
    }

    /**
     * 导出通话日志
     *
     * @return
     */
    @RequestMapping(value = "/export-callLog")
    @ResponseBody
    public void export(@RequestParam(required = false) String column,@RequestParam(required = false) String condition,@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String checkStatus, @RequestParam(required = false, defaultValue = "-1") Integer call_status_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityWrapper<CallLog> entityWrapper = new EntityWrapper<>();
        if ((!condition.isEmpty() && condition != null) && (!column.isEmpty() && column != null)) {
            entityWrapper.and(column + " like '%"+ condition +"%'");
        }
        if ((!beginTime.isEmpty() && beginTime != null) && (!endTime.isEmpty() && endTime != null)) {
            entityWrapper.and("created_at between '"+ beginTime+" 00:00:00" +"' and '"+ endTime+" 23:59:59" +"'");
        }
        if((!checkStatus.isEmpty() && checkStatus!=null)){
           entityWrapper.and("check_status ="+checkStatus);
        }
        if(call_status_id != -1) {
            entityWrapper.and("call_status_id =" + call_status_id);
        }
        if (ShiroKit.hasRole("manager")) {       //项目经理角色  展示整个项目的
            if (!ShiroKit.isAdmin()) {       //如果项目经理没有管理员权限
                Integer product_id = ShiroKit.getUser().getProductId();
                EntityWrapper<User> UserWrapper = new EntityWrapper<>();
                UserWrapper.and("productid=" + product_id + " and status=1");
                List<User> res = userService.selectList(UserWrapper);
                List ids = res.stream().map(User::getId).collect(Collectors.toList());
                String user_id = org.apache.commons.lang.StringUtils.join(ids.toArray(), ",");
                entityWrapper.in("user_id", user_id);
            }
        } else if (ShiroKit.hasRole("worker")) {       //坐席角色  展示自己的
            String user_id = ShiroKit.getUser().getId()+"";
            entityWrapper.in("user_id", user_id);
        }
        List<CallLog> result = callLogService.selectList(entityWrapper);
        //excel标题
        String[] title = {"客户姓名", "手机号码", "致电结果", "呼叫开始时间", "呼叫结束时间", "本次呼叫总秒数", "分机号","坐席姓名","电话小结","质检状态","质检备注"};

        //excel文件名
        String fileName = "通话记录表" + DateUtil.getAllTime() + ".xls";

        //sheet名
        String sheetName = "通话记录表";

        String[][] content = new String[result.size()][];

        for (int i = 0; i < result.size(); i++) {
            content[i] = new String[title.length];
            CallLog obj = result.get(i);
            content[i][0] = obj.getCustomName();
            content[i][1] = obj.getCustomPhone();
            content[i][2] = obj.getCallStatusName();
            content[i][3] = obj.getCallStartTime() == null ? "" : DateUtil.formatDate(obj.getCallStartTime(), "yyyy-MM-dd HH:mm:ss");
            content[i][4] = obj.getCallEndTime() == null ? "" : DateUtil.formatDate(obj.getCallEndTime(), "yyyy-MM-dd HH:mm:ss");
            content[i][5] = obj.getCallSecond() + "";
            content[i][6] = obj.getCallAgentNo();
            content[i][7] = obj.getUserName();
            content[i][8] = obj.getNote();
            content[i][9] = (obj.getCheckStatus() == null || obj.getCheckStatus() == 0) ? "未质检" : obj.getCheckStatus() == 1 ? "质检通过" : "质检未通过";
            content[i][10] = obj.getCheckNote();
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            ToolUtil.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 录音质检
     * @author chenhexiang 2018-09-20
     */
    @RequestMapping("/check-status/{recordId}")
    public String checkStatus(@PathVariable Integer recordId, Model model) {
        model.addAttribute("recordId", recordId);
        return PREFIX + "check-status.html";
    }

    @RequestMapping(value="/do-check-status")
    @ResponseBody
//    @BussinessLog(value = "修改录音质检", key = "id", dict = CallLogDict.class)
    public Object doCheckStatus(CallLog callLog) {
        Integer id = ShiroKit.getUser().getId();
        Date date = new Date();
        callLog.setCheckDate(date);
        callLog.setChecker(id);

        // add by eric 2018-10-10 start
        CallLog tclog = callLogService.selectById(callLog.getId()); // 临时使用
        Custom custom = new Custom();
        custom.setLtCheckDate(date);
        custom.setLtChecker(id);
        custom.setLtCheckNote(callLog.getCheckNote());
        custom.setLtCheckStatus(callLog.getCheckStatus());

        EntityWrapper<Custom> wrapper = new EntityWrapper<Custom>();
        /*StringBuffer bff = new StringBuffer();
        if(tclog.getCustomName()!=null && !tclog.getCustomName().equals("")){
            bff.append("custom_name = '"+tclog.getCustomName()+"' ");
        }else{
            bff.append("custom_name is NULL ");
        }
        wrapper.and(bff.toString()).and(" phone= '" + tclog.getCustomPhone()+ "'");*/
        wrapper.and(" phone= '" + tclog.getCustomPhone()+ "'");         //edited by chx 2018/10/12
        customService.update(custom,wrapper);

        // add by eric 2018-10-10 end

        if (callLogService.updateById(callLog)) {
            return SUCCESS_TIP;
        } else {
            return new JsonResult(201, "网络出错，质检失败，请稍后重试！", new Date());
        }
    }
}
