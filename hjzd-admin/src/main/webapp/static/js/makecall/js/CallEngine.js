$(function () {
    ipaddress = getCookie("cc_ipaddress");
    http_ce = "http://" + ipaddress + ":8088";
    is_calling = 0;//是否在打电话
    call_time = 0;//通话时长（单位：秒）
    hold = false;//呼叫保持

    /**
     * 身份验证
     * @param extnum 分机号
     */
    $.authentication = function (extnum) {
        var json = {'reg_user': extnum, 'scope': 'fsExtenRegStatus'};

        json = JSON.stringify(json);

        $.ajax({
            url: http_ce+'/callengine/http/status/find?json='+json,
            type: 'get',
            dataType: 'json',
            success: function(json) {
                if (json.code == 0) {
                    setCookie('is_call_login',1);
                    setCookie('cc_user', extnum);
                    layer.msg('身份验证成功！');
                    setTimeout(function () {
                        window.parent.layer.closeAll();
                    },1000);
                } else {
                    layer.msg('身份验证失败(请检查您的分机号.)');
                }
            },
            error: function(e) {
                layer.msg('身份验证失败(请检查您的分机号.)');
            },
        });
    }

    /**
     *拨打电话
     * @param target_phone
     * http://10.1.2.8:8088/callengine/http/operation?json={"dest":"018855586798","command":"dial","src":"8014"}
     */
    var getCallStatus = '';//声明一个定时器
    $.makeCall = function (target_phone, extnum) {
        var json = {'dest': target_phone, 'command': 'dial', 'src': extnum};

        json = JSON.stringify(json);

        $.ajax({
            url: http_ce+'/callengine/http/operation?json='+json,
            type: 'get',
            dataType: 'json',
            success: function(json) {
                if (json.code == 0) {
                    is_calling = 1;
                    layer.msg('正在拨打...');
                    //记录通话记录
                    var call_record = {
                        'caseId': $("#custom_id").val(),
                        'customName': $("input[name='custom_name']").val(),
                        'customPhone': $("#dial_telNumber").val() ? $("#dial_telNumber").val() : $('input[name="phone"]').val(),
                        'callAgentNo': extnum,
                    };
                    saveCallLog(call_record);
                    //获取某个坐席当天的实时数据(当前状态，当前状态持续时长，示忙次数，示忙时长)；
                    getCallStatus = setInterval(function () {
                        $.getCallStatus(extnum);    //获取分机状态
                    },1000);
                } else {
                    layer.msg('拨打失败，请稍后重播');
                }
            },
            error: function(e) {
                layer.msg('拨打失败，请稍后重播');
            },
        });
    }

    /**
     * 获取分机呼叫状态
     * @param extnum  分机号
     */
    $.getCallStatus = function (extnum) {
        var json = {"scope":"fsCallStatus",'userId': extnum};

        json = JSON.stringify(json);

        $.ajax({
            url: http_ce+'/callengine/http/status/find?json='+json,
            type: 'get',
            dataType: 'json',
            success: function(json) {
                if (json.code == 0) {
                    var message = json.result.callstate;
                    if (message == 'EARLY') {

                    } else if (message == 'RINGING') {       //--振铃
                        call_time = 0;
                    } else if (message == 'ACTIVE' && json.result.is_bridge == "true") {            //-通话中 is_bridge  true:接通 false：未接通
                        call_time++;
                        $('.callTime').html(format_seconds(call_time));
                    }
                } else {
                    clearInterval(getCallStatus);  //清除定时器
                    //更新通话记录
                    var call_record = {
                        'callSecond': call_time,
                        'id': parseInt($("#custom_call_id").val()),
                    };
                    updateCallLog(call_record);
                    call_time = 0;
                }
            },
            error: function(e) {
                layer.msg('抱歉请求出错');
            },
        });
    }

    /**
     *
     * @param extnum
     */
    $.callHungUp = function (_extnum, _case="沟通结束") {
        var json = {"command":"hangup",'dest': _extnum, 'case':_case};

        json = JSON.stringify(json);

        $.ajax({
            url: http_ce+'/callengine/http/operation?json='+json,
            type: 'get',
            dataType: 'json',
            success: function(json) {
                if (json.code == 0) {
                    is_calling = 0;
                    layer.msg('挂断成功！');
                } else {
                    layer.msg(json.errmsg);
                }
            },
            error: function(e) {
                layer.msg('抱歉请求出错');
            },
        });
    }

    /**
     * 呼叫保持
     * @param target_phone
     */
    $.callHold = function (target_phone) {
        var json = {"command":"hold",'dest': target_phone};

        json = JSON.stringify(json);

        $.ajax({
            url: http_ce+'/callengine/http/operation?json='+json,
            type: 'get',
            dataType: 'json',
            success: function(json) {
                if (json.code == 0) {
                    if (!hold) {
                        layer.msg('保持成功！')
                        hold = true;
                        $('.btn-hold').html('保持中..')
                    } else {
                        layer.msg('已取消保持！')
                        $('.btn-hold').html('保持')
                        hold = false;
                    }
                } else {
                    layer.msg(json.errmsg);
                }
            },
            error: function(e) {
                layer.msg('抱歉请求出错');
            },
        });
    }
})