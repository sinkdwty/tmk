package com.hjzddata.modular.task.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.util.RedisUtil;
import com.hjzddata.core.util.WebSocketServer;
import com.hjzddata.modular.custom.model.Custom;
import com.hjzddata.modular.custom.service.ICustomService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 预测外呼接口控制器
 */
@Controller
@RequestMapping("/api")
public class ApiController extends BaseController
{

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ICustomService customService;

    @Autowired
    private WebSocketServer webSocketServer;


    @RequestMapping("/infoPD")
    @ResponseBody
    public Object infoPD(@Param("phone") String phone, @Param("cc_userName") String cc_userName, @Param("sessionid") String sessionid, HttpServletRequest request) throws IOException {

        Map<String, String> res = new HashMap();
        String callback = request.getParameter("callback"); //不指定函数名默认 callback
        Custom custom = customService.selectOne(new EntityWrapper<Custom>().and("phone="+phone.substring(1, phone.length())).orderBy("id desc"));
        if(custom != null) {
//            String name = (custom.getCustomName().equals(null) || custom.getCustomName().equals("")) ? phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2") : custom.getCustomName();
            String name = phone.substring(1, phone.length()).replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            webSocketServer.sendtoCcUser("预测外呼", "cc-"+ cc_userName, custom.getId()+"", name, sessionid);

            res.put("code","200");
        } else {
            res.put("code","201");
        }

        return callback+ "(" + JSONObject.toJSONString(res) + ")";
    }

}
