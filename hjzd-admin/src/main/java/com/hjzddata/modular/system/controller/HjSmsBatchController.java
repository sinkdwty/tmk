package com.hjzddata.modular.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.config.properties.SmsProperties;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.util.XmlToolUtil;
import com.hjzddata.modular.common.model.SmsClientAccessTool;
import com.hjzddata.modular.knowledge.service.IMessageService;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.system.warpper.HjSmsBatchWarpper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.hjzddata.modular.system.model.HjSmsBatch;
import com.hjzddata.modular.system.service.IHjSmsBatchService;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.hjzddata.modular.custom.controller.CustomController.regex;

/**
 * sms_batch控制器
 *
 * @author fengshuonan
 * @Date 2018-09-27 11:54:29
 */
@Controller
@RequestMapping("/hjSmsBatch")
public class HjSmsBatchController extends BaseController {

    private String PREFIX = "/system/hjSmsBatch/";

    @Autowired
    private IHjSmsBatchService hjSmsBatchService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private SmsProperties smsProperties;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 跳转到sms_batch首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "hjSmsBatch.html";
    }

    /**
     * 跳转到添加sms_batch
     */
    @RequestMapping("/hjSmsBatch_add")
    public String hjSmsBatchAdd(Model model) {
        model.addAttribute("selectList", messageService.messageList());
        return PREFIX + "hjSmsBatch_add.html";
    }

    /**
     * 跳转到修改sms_batch
     */
    @RequestMapping("/hjSmsBatch_update/{hjSmsBatchId}")
    public String hjSmsBatchUpdate(@PathVariable Integer hjSmsBatchId, Model model) {
        Integer dic_id = dictService.selectOne(new EntityWrapper<Dict>().and("code='sms_batch_status'")).getId();
        List<Dict> list = dictService.selectList(new EntityWrapper<Dict>().and("pid=" + dic_id));
        model.addAttribute("smsCodes", list);


        HjSmsBatch hjSmsBatch = hjSmsBatchService.selectById(hjSmsBatchId);
        model.addAttribute("item", hjSmsBatch);
        LogObjectHolder.me().set(hjSmsBatch);
        return PREFIX + "hjSmsBatch_edit.html";
    }

    /**
     * 获取sms_batch列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String column, @RequestParam(required = false) String condition, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false, defaultValue = "-1") Integer status) {
        Page<HjSmsBatch> page = new PageFactory<HjSmsBatch>().defaultPage();
        List listSms = hjSmsBatchService.selectHjSmsBatchList(page, -1, column, condition, beginTime, endTime, status,"", page.getOrderByField(), page.isAsc());
        page.setRecords((List<HjSmsBatch>) new HjSmsBatchWarpper(listSms).warp());

        Map<String, Object> map = new HashMap<>(10);
        map.put("code", 0);
        map.put("count", page.getTotal());
        map.put("data", listSms);
        return map;
    }

    /**
     * 新增sms_batch
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(HjSmsBatch hjSmsBatch) {
        if (null == hjSmsBatch.getStatus()) {
            hjSmsBatch.setStatus(0); //  默认添加为待发送
        }
        hjSmsBatch.setUserid(ShiroKit.getUser().getId());
        hjSmsBatch.setCreateAt(new Date());
        hjSmsBatchService.insert(hjSmsBatch);
        return SUCCESS_TIP;
    }

    /**
     * 新增短信批次 ()
     */
    @RequestMapping(value = "/sms_batch_add")
    @ResponseBody
    public Object sms_batch_add(HjSmsBatch hjSmsBatch) {
        hjSmsBatchService.insert(hjSmsBatch);
        return SUCCESS_TIP;
    }

    /**
     * 删除sms_batch
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer hjSmsBatchId) {
        hjSmsBatchService.deleteById(hjSmsBatchId);
        return SUCCESS_TIP;
    }

    /**
     * 修改sms_batch
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(HjSmsBatch hjSmsBatch) {
        hjSmsBatchService.updateById(hjSmsBatch);
        return SUCCESS_TIP;
    }

    /**
     * sms_batch详情
     */
    @RequestMapping(value = "/detail/{hjSmsBatchId}")
    @ResponseBody
    public Object detail(@PathVariable("hjSmsBatchId") Integer hjSmsBatchId) {
        return hjSmsBatchService.selectById(hjSmsBatchId);
    }

    @RequestMapping(value = "/query_detail/{hjSmsBatchId}")
    @ResponseBody
    public Object queryDetailList(@PathVariable("hjSmsBatchId") Integer hjSmsBatchId) {
        Map<String, Object> map = new HashMap<>(10);

        HjSmsBatch hjSmsBatch = hjSmsBatchService.selectById(hjSmsBatchId);
        String result_detail = hjSmsBatch.getResultDetail();
        if (result_detail == null || "".equals(result_detail)) { // 明细未生成，稍后重试
            map.put("code", 1);
            map.put("message", "明细未生成，稍后重试");

            List errList = new ArrayList();
            HashMap hashMap1 = new HashMap();
            hashMap1.put("mobile", "");
            hashMap1.put("taskid", "");
            hashMap1.put("status", "明细未生成，稍后重试");
            hashMap1.put("receivetime", "");
            hashMap1.put("errorcode", "");
            errList.add(hashMap1);

            map.put("data", errList);
        } else {
            // 先按照失败报文解析
            if (log.isInfoEnabled()) {
                log.info("从数据库获取-------------ResultDetail----->" + result_detail);
            }
            List list = XmlToolUtil.isExistsNode(result_detail, "/returnsms/errorstatus");
            if (list != null && list.size() > 0) { // 如果存在表示是失败报文
                Map hashMap = XmlToolUtil.getElementAsMap((Element) list.get(0)); // 固定取头一组结果文件
                List errList = new ArrayList();
                HashMap hashMap1 = new HashMap();
                hashMap1.put("mobile", "");
                hashMap1.put("taskid", "");
                hashMap1.put("status", hashMap.get("remark"));
                hashMap1.put("receivertime", "");
                hashMap1.put("errorcode", "");
                errList.add(hashMap1);

                map.put("data", errList);
                map.put("message", hashMap.get("remark"));
            } else {
                // 接口乱，有两套返回异常报文，特此解析
                List st = XmlToolUtil.isExistsNode(result_detail, "/returnsms/returnstatus");
                if (log.isInfoEnabled()) {
                    log.info("-------------------处理失败报文[returnsms/returnstatus]-------->" + (st != null ? st.size():0));
                }
                if (st != null && st.size() > 0) {
                    Map hashMap = XmlToolUtil.getElementAsMap((Element) st.get(0)); // 固定取头一组结果文件
                    List errList = new ArrayList();
                    HashMap hashMap1 = new HashMap();
                    hashMap1.put("mobile", "00000000000");
                    hashMap1.put("taskid", hashMap.get("taskID"));
                    hashMap1.put("status", hashMap.get("message"));
                    hashMap1.put("receivertime", "");
                    hashMap1.put("errorcode", "");
                    errList.add(hashMap1);

                    map.put("data", errList);
                    map.put("message", hashMap.get("message"));
                } else {
                    List results_db = new ArrayList();
                    List suc_list = XmlToolUtil.isExistsNode(result_detail, "/returnsms/statusbox");
                    if(suc_list != null) {
                        for (int i = 0; i < suc_list.size(); i++) {
                            Map hashMap = XmlToolUtil.getElementAsMap((Element) suc_list.get(i));
                            results_db.add(hashMap);
                        }
                    }

                    map.put("data", results_db);
                    map.put("message", "明细查询成功");
                }
            }
            map.put("code", 0);
        }
        return map;
    }

    /**
     * action === query
     *
     * @param hjSmsBatchId
     * @param model
     * @return
     */
    @RequestMapping(value = "/send_detail/{hjSmsBatchId}")
    public String sendDetailQuery(@PathVariable("hjSmsBatchId") Integer hjSmsBatchId, Model model) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式

        // 查询短信批次结果信息
        HjSmsBatch hjSmsBatch = hjSmsBatchService.selectById(hjSmsBatchId);

        String result_detail = hjSmsBatch.getResultDetail();
        if (result_detail == null || "".equals(result_detail)) { // 如果未取回明细结果，则调用接口查询
            SmsClientAccessTool client = SmsClientAccessTool.getInstance();
            String uid = smsProperties.getUid();
            String user = smsProperties.getUser();
            String key = smsProperties.getKey();
            String sendUrl = smsProperties.getAppend_url();
            String backEncodType = smsProperties.getCode();

            String sendParam = "action=query&userid=" + uid + "&account=" + user + "&password=" + key;

            //UTF发送
            String ret_msg = null;
            try {
                ret_msg = client.sendPost(sendUrl, sendParam, backEncodType);
            } catch (Exception ex) {
                // 调用接口异常，异常处理
            }

            // 此处针对返回进行异常-报文处理,都存入数据库，加载展示是解析，解析是要区别是否是有效XML字符串
            hjSmsBatch.setResultDetail(ret_msg); // 返回为XML
            hjSmsBatchService.updateById(hjSmsBatch);
            if (log.isInfoEnabled()) {
                log.info("查询短信返回：-------------ResultDetail----->" + hjSmsBatch.getResultDetail());
            }
        }
        model.addAttribute("smsBatchId", hjSmsBatchId);
        return PREFIX + "hjSmsBatch_detail.html";
    }


    @RequestMapping(value = "/now_send")
    @ResponseBody
    public Object nowSend(@RequestParam("batchSmsId") Integer hjSmsBatchId) {
        // 获取批次信息
        HjSmsBatch smsBatch = hjSmsBatchService.selectById(hjSmsBatchId);

        String phones = smsBatch.getPhones();
        String message = smsBatch.getSmsContent();

        String reserveTime = smsBatch.getReserve_time() != null ? smsBatch.getReserve_time() : "";// 获取预约时间,如有非空，则表示预约发送；目前接口支持预约发送

        SmsClientAccessTool client = SmsClientAccessTool.getInstance();
        String uid = smsProperties.getUid();
        String user = smsProperties.getUser();
        String key = smsProperties.getKey();
        String sendUrl = smsProperties.getUrl();
        String backEncodType = smsProperties.getCode();
        Integer code = 0;
        String msg = "发送失败!";
        String ret_msg = null;

        try {
            String sendParam = "action=send&userid=" + uid + "&account=" + user + "&password=" + key + "&mobile=" + phones + "&content=" + message.replaceAll(" ", "") + "&sendTime=" + reserveTime + "&extno=";
            try {
                ret_msg = client.sendPost(sendUrl, sendParam, backEncodType);
            } catch (Exception ex) { // 调用接口异常，异常处理
                smsBatch.setStatus(4);
                smsBatch.setSendNote("调用批次短信服务失败，可审核后重发");
            }

            if (ret_msg != null) {
                String note = regex(ret_msg, "message");
                String taskId = regex(ret_msg, "taskID");
                String status = regex(ret_msg, "returnstatus");
                if (status.equals("Faild")) {
                    smsBatch.setStatus(4);     // 发送失败
                    smsBatch.setSendNote(note);
                    smsBatch.setResultText(ret_msg);
                    smsBatch.setRetTaskId(taskId);
                    code = 0;
                    msg = "发送失败!<br>" + note;
                } else if (status.equals("Success")) {
                    smsBatch.setStatus(3);     // 发送成功
                    smsBatch.setSendNote(note);
                    smsBatch.setResultText(ret_msg);
                    smsBatch.setRetTaskId(taskId);
                    code = 1;
                    msg = "发送成功！<br>成功发送" + regex(ret_msg, "successCounts") + "条";
                } else {
                    code = 1;
                    msg = "调用批次短信服务失败，可审核后重发";
                }
                System.out.println("发送短信返回：-------------ResultText----->" + smsBatch.getResultText());
                hjSmsBatchService.updateById(smsBatch);
            }
        } catch (Exception ex) {
            code = 1;
            msg = "调用批次短信服务失败，可审核后重发";
        }
        HashMap map = new HashMap();
        map.put("code", code);
        map.put("data", msg);
        return map;
    }
}
