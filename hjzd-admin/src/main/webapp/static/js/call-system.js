
var orgidentity = getCookie("cc_orgidentity");  // astercc | lianghuapai   团队唯一标识
var usertype = 'agent';  // agent | account  actercc用户名类型
var pwdtype = 'plaintext';  // actercc密码类型

var is_calling = 0;//电话没接通
var call_time = 0;//通话时长（单位：秒）
var ring_time = 0;//振铃时长（单位：秒）
var hold = false;//通话保持状态

//拨打电话
$("#dial, #take-call-0").on("click", function () {
    //判断该用户是否配置外呼系统
    if(getCookie("cc_ipaddress") == null || getCookie("cc_ipaddress") == "") {
        layer.msg('抱歉，请先配置外呼系统在进行外呼任务！');
        return false;
    }

    //判断是否在新增页面拨打电话
    if ($(this).attr("from") == "new_add") {
        var new_phone = $("#phone").val();
        if (new_phone.length != 11 || new_phone == '') {
            layer.msg('客户电话有误');
            return false;
        }
        var customObj = {};
        customObj['phone'] = new_phone;
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/custom/add", function(data){
            $("#custom_id").val(data.data);
        },function(data){

        });
        ajax.set(customObj);
        ajax.start();
    }

    //首先判断是否登录。没有登录则需要先登录
    var is_call_login = getCookie("is_call_login");
    if (is_call_login == '' || is_call_login == null) {
        var url = Feng.ctxPath + "/goods/call-login";
        layer.open({
            title: "登录呼叫系统",
            type: 2,
            area: ['500px', '450px'],
            //点击遮罩关闭
            shadeClose: true,
            content: url,
            scrollbar: false,
        })
    } else {
        ipaddress = getCookie("cc_ipaddress");
        model_id = getCookie("cc_model_id");
        engine = getCookie("cc_engine");
        port = getCookie("cc_port");
        var user = getCookie('cc_user');//'8001';

        var phone = $("#phone").val() ? $("#phone").val() : $('#tele_phone').val();
        if ($(this).attr('id') == 'dial') {
            if (phone.length != 11 || phone == '') {
                layer.msg('客户电话有误');
                return false;
            }
        } else {
            phone = '0' + phone;
            if (phone.length != 12 || phone == '') {
                layer.msg('客户电话有误');
                return false;
            }
        }

        if (engine == 'CJI') {
            layui.use(['layer', 'callCenter'], function () {
                var callCenter = layui.callCenter;

                var exter = 'exter';//exter外呼到号码   inner外呼到坐席
                var agentgroupid = getCookie("cc_agentgroupid");
                var password = getCookie('cc_password');// 'temp123';
                var modeltype = 'Campaign';//外呼营销任务

                var timestamp = new Date().getTime();
                var userData = phone + timestamp;   //用于获取通话唯一标识

                // 目标类型(电话号码[exter],坐席[inner])
                callCenter.makeCall(phone, 'exter', agentgroupid, usertype, user, orgidentity, pwdtype, password, 'Campaign', model_id, userData, function (data) {
                    if (data.code === 1) {
                        is_calling = 1;
                        layer.msg('正在拨打...');
                        //记录通话记录
                        var call_record = {
                            'caseId': $("#custom_id").val(),
                            'customName': $("input[name='custom_name']").val(),
                            'customPhone': $("#phone").val() ? $("#phone").val() : $("#tele_phone").val(),
                            'callAgentNo': user,
                            'callUserdata': userData,
                        };
                        saveCallLog(call_record);

                        //获取某个坐席当天的实时数据(当前状态，当前状态持续时长，示忙次数，示忙时长)；
                        var getCallStatus = setInterval(function () {
                            callCenter.agentRealtime(orgidentity, usertype, user, pwdtype, password, function (data) {
                                var message = data.message.split('|');
                                if (message[2] == 'idle') {      //-空闲
                                    clearInterval(getCallStatus);
                                    //更新通话记录
                                    var call_record = {
                                        'callSecond': call_time,
                                        'ringSecond': ring_time,
                                        'id': parseInt($("#custom_call_id").val()),
                                    };
                                    updateCallLog(call_record);
                                    call_time = 0;
                                    ring_time = 0;
                                    online = 0;
                                    getCall();
                                } else if (message[2] == 'ring') {       //--振铃
                                    call_time = 0;
                                    ring_time = message[3];
                                    online = 1;
                                } else if (message[2] == 'busy') {            //-通话
                                    $('.callTime').html(format_seconds(message[3]));
                                    call_time = message[3];
                                    online = 1;
                                }else if (message[2] == 'logout') {            //-通话
                                    clearInterval(getCallStatus);
                                    online = 0;
                                }
                            });
                        }, 1000);
                    } else {
                        layer.msg('拨打失败！');
                    }
                });
            });
        } else {
            setTimeout(function () {
                $.makeCall(phone, user);
            }, 2000);
        }
    };
})

//呼叫账号登录
$('#call_login_ensure').on('click',function () {
    engine = getCookie("cc_engine");
    var user = $.trim($('input[name="username"]').val());

    if (engine == "CE") {
        $.authentication(user); //身份验证
    } else {
        layui.use(['laypage', 'layer', 'form', 'table', 'element', 'callCenter', 'laydate', 'upload'], function () {
            var layer = layui.layer;
            var callCenter = layui.callCenter;
            var password = $.trim($('input[name="password"]').val());

            callCenter.login(orgidentity, usertype, user, pwdtype, password, function (data) {

                if (data.code == 1) {
                    setCookie('is_call_login', 1);
                    setCookie('cc_user', user);
                    setCookie('cc_password', password);
                    sessionStorage.setItem('cc_user', user);
                    window.parent.layer.msg('登录成功！');
                    window.parent.layer.closeAll();

                    window.parent.parent.location.reload();
                    // callCenter.queueActionCJI(orgidentity, usertype, user, pwdtype, password);
                    window.open('http://'+getCookie("cc_ipaddress"),"_black");

                    /*setTimeout(function () {
                        window.parent.location.reload();
                        window.parent.cc_socket(user);
                        callCenter.queueActionCJI(orgidentity, usertype, user, pwdtype, password);
                        window.open('http://'+getCookie("cc_ipaddress"),"_black");

                        // window.open(getCookie("cc_ipaddress")+"/login/changelogin?account_id=3&agent_id=2&pnum=cca8dd8babd4c9996c8dfee788a49d18&language=cn")
                    }, 1000);*/
                } else {
                    layer.msg('登录失败！', {time: 1000})
                }
            });
        });
    }

});

/* 点击保持 */
$('.btn-hold').on('click',function () {
    var user = getCookie('cc_user');
    var password = getCookie('cc_password');


    if (engine == "CJI") {
        layui.use(['layer', 'callCenter'], function () {
            var callCenter = layui.callCenter;

            if (!hold) {
                callCenter.hold(1, orgidentity, usertype, user, pwdtype, password, function (data) {     //是否静音(0或1)，0代表不静音，1代表静音
                    if (data.code == 1) {
                        layer.msg('保持成功！')
                        hold = true;
                        $('.btn-hold').html('保持中..')
                    } else {
                        layer.msg('无需保持！')
                    }
                });
            } else {
                callCenter.resume(orgidentity, usertype, user, pwdtype, password, function (data) {
                    if (data.code == 1) {
                        layer.msg('已取消保持！')
                        $('.btn-hold').html('保持')
                        hold = false;
                    } else {
                        layer.msg('无需保持！')
                    }
                });
            }
        })
    } else {
        var phone = $("#phone").val() ? $("#phone").val() : $('#tele_phone').val();
        if ($(this).attr('id') == 'dial') {
            if (phone.length != 11 || phone == '') {
                layer.msg('客户电话有误');
                return false;
            }
        } else {
            phone = '0' + phone;
            if (phone.length != 12 || phone == '') {
                layer.msg('客户电话有误');
                return false;
            }
        }
        $.callHold(phone);
    }
});

/**
 * 秒数转换为时分秒
 * @param call_time
 * @returns {string}
 */
function format_seconds(call_time) {
    var connector = ":";
    var hour = parseInt(call_time / 3600) < 10 ? ("0"+parseInt(call_time / 3600)) : parseInt(call_time / 3600);
    var minute = parseInt((call_time % 3600) / 60) < 10 ? ("0"+parseInt((call_time % 3600) / 60)) : parseInt((call_time % 3600) / 60);
    var second = (call_time % 3600) % 60 < 10 ? ("0"+(call_time % 3600) % 60) : (call_time % 3600) % 60;
    return hour + connector + minute + connector + second;
}

/**
 * 保存通话记录
 * @param user
 * @param userData
 */
function saveCallLog(call_record) {
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/goods/save-call-log", function(data){
        // Feng.success("添加成功!");
        $('#custom_call_id').val(data.data);

    },function(data){
        // Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(call_record);
    ajax.start();
}

/**
 * 更新通话记录
 * @param call_record
 */
function updateCallLog(call_record) {
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/goods/update-call-log", function(data){
        // Feng.success("更新成功!");

    },function(data){
        // Feng.error("更新成功!" + data.responseJSON.message + "!");
    });
    ajax.set(call_record);
    ajax.start();

    /*$.ajax({
        url:  Feng.ctxPath + "/goods/save-call-log",
        type: "POST",
        data: {'call_record': JSON.stringify(call_record)},
        dataType: "JSON",
        success: function (res) {
            if(res.code == 1) {
                var DayCallNum = $('.DayCallNum').html();
                var MonthCallNum = $('.MonthCallNum').html();
                $('.DayCallNum').html(Number(DayCallNum) + 1)
                $('.MonthCallNum').html(Number(MonthCallNum) + 1)
                table.reload('callLogTable');
                $('#logid').val(res.logid);
            }
        }
    })*/
}

/* 点击挂断 */
$('.btn-hangup').on('click',function () {
    // if (is_calling ==1) {
        var user = getCookie('cc_user');
        var password = getCookie('cc_password');
        ipaddress = getCookie("cc_ipaddress");
        model_id = getCookie("cc_model_id");
        engine = getCookie("cc_engine");
        port = getCookie("cc_port");

        if (engine == "CJI") {
            layui.use(['layer', 'callCenter'], function () {
                var callCenter = layui.callCenter;

                callCenter.hangup('', user, 'all', pwdtype, password, usertype, user, orgidentity, function (data) {
                    if (data.code == 1) {
                        is_calling = 0;
                        layer.msg('挂断成功！');
                        /*//记录挂断日志
                        $.ajax({
                            url: "<?= \yii\helpers\Url::toRoute(['task-data/hangup-log'])?>",
                            type: 'POST',
                            data: {'case_no':caseNo},
                            dataType: 'JSON',
                            success:function (data) {           //成功回调

                            }
                        });*/
                    } else {
                        layer.msg('无需挂断！');
                    }
                });
            })
        } else {
            $.callHungUp(user);
        }
    /*} else {        //没有接听的电话
        return false;
    }*/
});

/**
 * 登出
 */
layui.use(['layer', 'callCenter'], function () {
    var callCenter = layui.callCenter;
    $('#logout').on('click',function () {
        var user = getCookie('cc_user');
        var password = getCookie('cc_password');
        callCenter.logout(orgidentity, usertype, user, pwdtype, password, function(data) {
            console.log(data);
            if (data.code == 1) {
                setCookie('cc_user', '');
                setCookie('cc_password', '');
                setCookie('is_call_login', '');
                layer.msg(data.msg)
            }
        });
    });
});
/**
 * 获取呼叫系统配置
 */
$('#callSystemId').on('change',function () {
    var call_system = $('#callSystemId').val();
    var call_system_text = $('#callSystemId').find("option:selected").text();

    if (call_system == '' || call_system == null) {

    } else {
        $.ajax({
            url: Feng.ctxPath + "/goods/call-system",
            type : "POST",
            data : {'call_system' : call_system},
            dataType: 'json',
            success: function (data) {
                if (call_system_text == "汉天") {
                    $('.div-password').hide();

                    ipaddress = data[0];
                    model_id = 1;
                    engine = 'CE';//CJI
                    port = 5070;//5060;
                    dynamicLoadJs("/static/js/makecall/js/CallEngine.js");
                } else {
                    $('.div-password').show();

                    ipaddress = data[0];
                    model_id = 1;
                    engine = 'CJI';//CJI
                    port = 29999;//5060;
                    dynamicLoadJs("/static/js/makecall/js/configs.js");
                    dynamicLoadJs("/static/js/makecall/js/callCenter.js");
                    dynamicLoadJs("/static/js/makecall/js/CJI.js");
                    layui.config({
                        base: "/static/js/makecall/js/", // 模块所在的目录
                        debug: true, //开发模式
                    });
                }
                setCookie('cc_ipaddress', ipaddress);
                setCookie('cc_model_id', model_id);
                setCookie('cc_engine', engine);
                setCookie('cc_port', port);
            },
            error: function () {

            }
        })
    }
})

