package com.hjzddata.modular.common;

import com.alibaba.fastjson.JSON;
import com.hjzddata.config.UeditorConfig;
import com.hjzddata.config.properties.HjzdProperties;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.modular.common.model.Ueditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Controller
@Transactional
public class UeditorController {
    @Autowired
    private HjzdProperties hjzdProperties;

    //ueditr后端配置
    @ResponseBody
    @RequestMapping(value = "/ueditor",produces="text/html;charset=UTF-8")
    public Object ueditor(@RequestParam("action") String action, MultipartFile upfile, HttpServletRequest request, HttpServletResponse response){
        Ueditor ueditor = new Ueditor();
        if (action != null && action.equals("config")) {
            /*try {
                Resource resource = new ClassPathResource("config.json");
                File file = resource.getFile();
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder;//.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }*/
            return UeditorConfig.UEDITOR_CONFIG;

        } else if (action != null && action.equals("uploadimage") || action.equals("uploadscrawl")) {
            if (upfile != null) {
                // 获取文件名
//                String fileName = upfile.getOriginalFilename();
//                String pathRoot = request.getSession().getServletContext().getRealPath("");
//                String path = pathRoot + "upload\\image\\";
                String path = hjzdProperties.getFileUploadPath();
                String fileName = UUID.randomUUID().toString() + ".jpg";
                try {
                    File dest = new File(path + fileName);
                    if (!dest.getParentFile().exists()) {
                        dest.getParentFile().mkdirs();
                    }
                    upfile.transferTo(dest);
                } catch (Exception e) {
                    throw new HjzdException(BizExceptionEnum.UPLOAD_ERROR);
                }
                ueditor.setState("SUCCESS");
                ueditor.setUrl("/kaptcha/" + fileName);
                ueditor.setTitle(fileName);
                ueditor.setOriginal(fileName);
                return JSON.toJSONString(ueditor);
            } else {
                ueditor.setState("文件为空");
                return JSON.toJSONString(ueditor);
            }
        } else {
            ueditor.setState("不支持该操作");
            return JSON.toJSONString(ueditor);
        }
    }

}
