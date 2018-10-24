package com.hjzddata.modular.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.NoticeMap;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.system.model.Notice;
import com.hjzddata.modular.system.service.INoticeService;
import com.hjzddata.modular.system.warpper.NoticeWrapper;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.core.util.ToolUtil;
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
 * 通知控制器
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    private String PREFIX = "/system/notice/";

    @Autowired
    private INoticeService noticeService;

    /**
     * 跳转到通知列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "notice.html";
    }

    /**
     * 跳转到添加通知
     */
    @RequestMapping("/notice_add")
    public String noticeAdd() {
        return PREFIX + "notice_add.html";
    }

    /**
     * 跳转到修改通知
     */
    @RequestMapping("/notice_update/{noticeId}")
    public String noticeUpdate(@PathVariable Integer noticeId, Model model) {
        Notice notice = this.noticeService.selectById(noticeId);
        model.addAttribute("notice",notice);
        LogObjectHolder.me().set(notice);
        return PREFIX + "notice_edit.html";
    }

    /**
     * 跳转到首页通知
     */
    @RequestMapping("/hello")
    public String hello() {
        List<Map<String, Object>> notices = noticeService.list(null);
        super.setAttr("noticeList",notices);
        return "/blackboard.html";
    }

    /**
     * 获取通知列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
//        List<Map<String, Object>> list = this.noticeService.list(condition);
//        return super.warpObject(new NoticeWrapper(list));
        Page<Notice> page = new PageFactory<Notice>().defaultPage();
        List result = noticeService.getNotice(page, condition);
        page.setRecords((List<Notice>) new NoticeWrapper(result).warp());
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",result);
        return list;
    }

    /**
     * 新增通知
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增通知",key = "title",dict = NoticeMap.class)
    public Object add(Notice notice) {
        List result = noticeService.noticeExistence(notice.getId(), notice.getContent(),notice.getTitle());
        if (result.isEmpty()) {
            if (ToolUtil.isOneEmpty(notice, notice.getTitle(), notice.getContent())) {
                throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
            }
            notice.setCreater(ShiroKit.getUser().getId());
            notice.setCreatetime(new Date());
            notice.insert();
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同的通知内容，请更换通知内容!", result);
            return jsonResult;
        }
    }

    /**
     * 删除通知
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除通知",key = "noticeId",dict = NoticeMap.class)
    public Object delete(@RequestParam Integer noticeId) {

        //缓存通知名称
        LogObjectHolder.me().set(ConstantFactory.me().getNoticeTitle(noticeId));

        this.noticeService.deleteById(noticeId);

        return SUCCESS_TIP;
    }

    /**
     * 修改通知
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改通知",key = "title",dict = NoticeMap.class)
    public Object update(Notice notice) {
        List result = noticeService.noticeExistence(notice.getId(), notice.getContent(), notice.getTitle());
        if (result.isEmpty()) {
            if (ToolUtil.isOneEmpty(notice, notice.getId(), notice.getTitle(), notice.getContent())) {
                throw new HjzdException(BizExceptionEnum.REQUEST_NULL);
            }
            Notice old = this.noticeService.selectById(notice.getId());
            old.setTitle(notice.getTitle());
            old.setContent(notice.getContent());
            old.updateById();
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同的通知内容，请更换通知内容!", result);
            return jsonResult;
        }
    }

}
