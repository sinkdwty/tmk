/**
 * @link         http://www.hejunzongda.com/
 * @author       yaoliang <yaoliang@hfhjzddata.com>
 * @copyright    Copyright &copy; hjzddata.com, 2015 - 2018
 * @description    呼叫系统配置模块
 */

layui.define(['layer'], function(exports) {
    exports('configs', {
        'ipaddress':getCookie("cc_ipaddress"), // 呼叫系统地址
        'port':getCookie("cc_port"), // 端口
        'engine':getCookie("cc_engine"), // 选择呼叫版本 CE 汉天 CJI astercc
        'model_id': getCookie("cc_model_id"), // 项目ID
        'agentgroupid': getCookie("cc_agentgroupid"), // 项目唯一标识
    });
});