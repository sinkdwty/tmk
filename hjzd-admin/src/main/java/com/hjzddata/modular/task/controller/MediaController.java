package com.hjzddata.modular.task.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.MediaDict;
import com.hjzddata.core.common.constant.factory.ConstantFactory;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.core.support.HttpKit;
import com.hjzddata.modular.task.model.Media;
import com.hjzddata.modular.task.service.IMediaService;
import com.hjzddata.modular.task.warpper.MediaWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 媒体管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-05 11:21:40
 */
@Controller
@RequestMapping("/media")
public class MediaController extends BaseController {

    private String PREFIX = "/task/media/";

    @Autowired
    private IMediaService mediaService;

    /**
     * 跳转到媒体管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "media.html";
    }

    /**
     * 跳转到添加媒体管理
     */
    @RequestMapping("/media_add")
    public String mediaAdd() {
        return PREFIX + "media_add.html";
    }

    /**
     * 跳转到修改媒体管理
     */
    @RequestMapping("/media_update/{mediaId}")
    public String mediaUpdate(@PathVariable Integer mediaId, Model model) {
        Media media = mediaService.selectById(mediaId);
        model.addAttribute("item",media);
        LogObjectHolder.me().set(media);
        return PREFIX + "media_edit.html";
    }

    /**
     * 获取媒体管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "category", required = false) Integer category) {
        Page<Media> page = new PageFactory<Media>().defaultPage();
        List<Map<String, Object>> medias = mediaService.selectMedias(page, name, category);
        page.setRecords((List<Media>) new MediaWarpper(medias).warp());
        return super.packForBT(page);
//        return mediaService.selectList(new EntityWrapper<Media>().eq("is_del", "0"));
    }

    /**
     * 新增媒体管理
     */
    @BussinessLog(value = "添加媒体",  key = "name", dict = MediaDict.class)
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Media media) {
        media.setCreatedAt(new Date());
        media.setUpdatedAt(new Date());
        media.setCreatedIp(HttpKit.getIp());
        media.setUpdatedIp(HttpKit.getIp());
        mediaService.insert(media);
        return SUCCESS_TIP;
    }

    /**
     * 删除媒体管理
     */
    @BussinessLog(value = "删除媒体",  key = "name", dict = MediaDict.class)
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer mediaId) {
        //缓存被删除的商品名称
        LogObjectHolder.me().set(ConstantFactory.me().getMediaName(mediaId));
        mediaService.deleteById(mediaId);
        return SUCCESS_TIP;
    }

    /**
     * 修改媒体管理
     */
    @BussinessLog(value = "修改媒体",  key = "id", dict = MediaDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Media media) {
        media.setUpdatedAt(new Date());
        media.setUpdatedIp(HttpKit.getIp());
        mediaService.updateById(media);
        return SUCCESS_TIP;
    }

    /**
     * 媒体管理详情
     */
    @RequestMapping(value = "/detail/{mediaId}")
    @ResponseBody
    public Object detail(@PathVariable("mediaId") Integer mediaId) {
        return mediaService.selectById(mediaId);
    }
}
