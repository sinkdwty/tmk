package com.hjzddata.modular.knowledge.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.config.properties.HjzdProperties;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.annotion.BussinessLog;
import com.hjzddata.core.common.constant.dictmap.KnowledgeDict;
import com.hjzddata.core.common.constant.factory.PageFactory;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.support.HttpKit;
import com.hjzddata.core.util.DateUtil;
import com.hjzddata.core.util.DocConverter;
import com.hjzddata.core.util.RedisUtil;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.knowledge.warpper.KnowledgeWarpper;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.service.IDictService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.hjzddata.core.log.LogObjectHolder;
import com.hjzddata.modular.knowledge.model.Knowledge;
import com.hjzddata.modular.knowledge.service.IKnowledgeService;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 知识库管理控制器
 *
 * @author fengshuonan
 * @Date 2018-07-10 17:19:11
 */
@Controller
@RequestMapping("/knowledge")
public class KnowledgeController extends BaseController {

    private String PREFIX = "/knowledge/knowledge/";

    @Autowired
    private IKnowledgeService knowledgeService;

    @Autowired
    IDictService iDictService;

    @Autowired
    private HjzdProperties hjzdProperties;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 跳转到知识库管理首页
     */
    @RequestMapping("")
    public String index(Model model) {
        //根据code查询字典数据
        List<Dict> list = iDictService.selectByParentCode("knowledge");
        model.addAttribute("category", list);
        System.out.println(model);
        return PREFIX + "knowledge.html";
    }

    /**
     * 跳转到添加知识库管理
     */
    @RequestMapping("/knowledge_add")
    public String knowledgeAdd() {
        return PREFIX + "knowledge_add.html";
    }

    /**
     * 跳转到修改知识库管理
     */
    @RequestMapping("/knowledge_update")
    public String knowledgeUpdate(@RequestParam("id") Integer knowledgeId, @RequestParam("option") String option, Model model) {
        Knowledge knowledge = knowledgeService.selectById(knowledgeId);
        knowledge.setContent(StringEscapeUtils.unescapeHtml(knowledge.getContent()));
        model.addAttribute("item",knowledge);
        LogObjectHolder.me().set(knowledge);

        if (option.equals("detail")) {
            return PREFIX + "knowledge_detail.html";
        } else {
            return PREFIX + "knowledge_edit.html";
        }
    }

    /**
     * 获取知识库管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "key_word",required = false) String key_word,@RequestParam(value = "column",required = false) String column, @RequestParam(value = "category",required = false) Integer category) {
        Page<Knowledge> page = new PageFactory<Knowledge>().defaultPage();
        List result = knowledgeService.selectKnowledges(page, key_word,column, category);
        page.setRecords((List<Knowledge>) new KnowledgeWarpper(result).warp());
        Map<String, Object> list = new HashMap<>();
        list.put("code",0);
        list.put("count",page.getTotal());
        list.put("data",result);
        return list;
//        List  result = knowledgeService.selectKnowledges(page, key_word, category);
//        page.setRecords(result);
//        return super.packForBT(page);
//        return knowledgeService.selectList(null);
    }

    /**
     * 新增知识库管理
     */
    @BussinessLog(value = "添加知识", key = "id", dict = KnowledgeDict.class)
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Knowledge knowledge) {
        List knowledgeExistenceResult = knowledgeService.knowledgeExistence(knowledge.getName(),knowledge.getId());
        if (knowledgeExistenceResult.isEmpty()) {
//        return knowledge.getName();
            knowledge.setCreatedAt(new Date());
            knowledge.setUpdatedAt(new Date());
            knowledge.setCreatedIp(HttpKit.getIp());
            knowledge.setUpdatedIp(HttpKit.getIp());
            knowledgeService.insert(knowledge);
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同知识名称，请更换知识名称!", knowledgeExistenceResult);
            return jsonResult;
        }

    }

    /**
     * 删除知识库管理
     */
    @BussinessLog(value = "删除知识", key = "id", dict = KnowledgeDict.class)
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer knowledgeId) {
        knowledgeService.deleteById(knowledgeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改知识库管理
     */
    @BussinessLog(value = "编辑知识", key = "id", dict = KnowledgeDict.class)
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Knowledge knowledge) {
        List knowledgeExistenceResult = knowledgeService.knowledgeExistence(knowledge.getName(),knowledge.getId());
        if (knowledgeExistenceResult.isEmpty()) {
            knowledge.setUpdatedAt(new Date());
            knowledge.setUpdatedIp(HttpKit.getIp());
            knowledgeService.updateById(knowledge);
            return SUCCESS_TIP;
        } else {
            JsonResult jsonResult = new JsonResult(201, "已存在相同知识名称，请更换知识名称!", knowledgeExistenceResult);
            return jsonResult;
        }
    }

    /**
     * 知识库管理详情
     */
    @RequestMapping(value = "/detail/{knowledgeId}")
    @ResponseBody
    public Object detail(@PathVariable("knowledgeId") Integer knowledgeId) {
        return knowledgeService.selectById(knowledgeId);
    }

    @RequestMapping(value = "/upload-file")
    @ResponseBody
    public Object uploadfile(@RequestParam("file") MultipartFile file) {
        String file_name = file.getOriginalFilename();
        String suffix = file_name.substring(file_name.lastIndexOf(".") + 1);
        String[] suffix_arr = {"pdf", "doc"};
        if (Arrays.asList(suffix_arr).contains(suffix)) {
            String fileName = DateUtil.getAllTime() + file_name;
            String file_name_new = fileName;
            String path = hjzdProperties.getFileUploadPath();
            try {
                File dest = new File(path + fileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);

                if(suffix.equals("doc")) {
                    //把word文档转化为pdf
                    DocConverter docConverter = new DocConverter(path + fileName);
                    try {
                        docConverter.doc2pdf();
                        file_name_new = StringUtils.substringBeforeLast(fileName, ".") + ".pdf";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //把附件名称存入redis
                redisUtil.hset("knowledgeAccessory", fileName, file_name_new);

                JsonResult jsonResult = new JsonResult(200, "上传成功", fileName);
                return jsonResult;
            } catch (Exception e) {
                throw new HjzdException(BizExceptionEnum.UPLOAD_ERROR);
//            JsonResult jsonResult = new JsonResult(201, "上传失败", fileName);
//            return jsonResult;
            }
        } else {
            JsonResult jsonResult = new JsonResult(201, "文件格式不正确，上传失败", file_name);
            return jsonResult;
        }
    }

    /**
     * 下载附件
     * @param fileName
     * @param request
     * @param response
     */
    @RequestMapping(value = "/download-file")
    public void downloadFileAction(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            //设置文件路径
            File file = new File(hjzdProperties.getFileUploadPath() + fileName);
            fis = new FileInputStream(file);
//            response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
            /*如果文件名有中文的话，进行URL编码，让中文正常显示*/
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            IOUtils.copy(fis,response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 预览pdf文件
     * @param id
     */
    @RequestMapping("/displayPDF/{id}")
    @ResponseBody
    public void displayPDF(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        Knowledge knowledge = knowledgeService.selectById(id);
        String knowledge_accessory_key = knowledge.getAccessory();
        //redis获取对应的附件
        Object knowledge_accessory = redisUtil.hget("knowledgeAccessory", knowledge_accessory_key);
        File file = new File(hjzdProperties.getFileUploadPath() + knowledge_accessory);
        if (file.exists()){
            byte[] data = null;
            try {
                FileInputStream input = new FileInputStream(file);
                data = new byte[input.available()];
                input.read(data);
                response.getOutputStream().write(data);
                input.close();
            } catch (Exception e) {
                System.out.println("pdf文件处理异常：" + e.getMessage());
            }

        } else {
            return ;
        }
    }


}
