package com.hjzddata.modular.system.controller;

import com.hjzddata.config.properties.HjzdProperties;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.common.exception.BizExceptionEnum;
import com.hjzddata.core.exception.HjzdException;
import com.hjzddata.core.util.DateUtil;
import com.hjzddata.modular.common.model.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 文件上传控制器
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

    private static String PREFIX = "/upload/user/";

    @Autowired
    private HjzdProperties hjzdProperties;

    /**
     * 上传图片(上传到项目的webapp/static/img)
     */
    @RequestMapping(method = RequestMethod.POST, path = "/img")
    @ResponseBody
    public Object img(@RequestPart("file") MultipartFile picture) {
        String pictureName = UUID.randomUUID().toString() + ".jpg";
        try {
            String fileSavePath = hjzdProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new HjzdException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return new JsonResult(200, "上传成功", pictureName);
    }

    /**
     * 上传文件
     */
    @RequestMapping(method = RequestMethod.POST, path = "/file")
    @ResponseBody
    public Object file(@RequestPart("file") MultipartFile multipartFile) {
        String ext = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String filePath = "upload";//UUID.randomUUID().toString();
        String fileName = DateUtil.getAllTime() + "_" + multipartFile.getOriginalFilename();

        File file = new File(hjzdProperties.getFileUploadPath() + "/" + filePath);
        //如果文件夹不存在则创建
        if  (!file .exists()  && !file .isDirectory()) {
            file.mkdir();
        }
        System.out.println(file.getPath());
        try {
            multipartFile.transferTo(new File(file.getPath() + "/" +  fileName));
        } catch (Exception e) {
            throw new HjzdException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return new JsonResult(200, "上传成功", filePath + "/" + fileName);
    }

    /**
     * 下载已上传文件
     *
     * @param fileName
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public String downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        if (fileName != null) {
            File file = new File(hjzdProperties.getFileUploadPath() + "/" +fileName);
            if (file.exists()) {
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                // 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes(), "ISO-8859-1"));
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
