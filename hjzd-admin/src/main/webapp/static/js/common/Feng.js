var Feng = {
    ctxPath: "",
    addCtx: function (ctx) {
        if (this.ctxPath == "") {
            this.ctxPath = ctx;
        }
    },
    confirm: function (tip, ensure) {//询问框
        parent.layer.confirm(tip, {
            btn: ['确定', '取消']
        }, function (index) {
            ensure();
            parent.layer.close(index);
        }, function (index) {
            parent.layer.close(index);
        });
    },
    log: function (info) {
        console.log(info);
    },
    alert: function (info, iconIndex) {
        parent.layer.msg(info, {
            icon: iconIndex
        });
    },
    info: function (info) {
        Feng.alert(info, 0);
    },
    success: function (info) {
        Feng.alert(info, 1);
    },
    error: function (info) {
        Feng.alert(info, 2);
    },
    infoDetail: function (title, info) {
        var display = "";
        if (typeof info == "string") {
            display = info;
        } else {
            if (info instanceof Array) {
                for (var x in info) {
                    display = display + info[x] + "<br/>";
                }
            } else {
                display = info;
            }
        }
        parent.layer.open({
            title: title,
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['950px', '600px'], //宽高
            content: '<div style="padding: 20px;">' + display + '</div>'
        });
    },
    writeObj: function (obj) {
        var description = "";
        for (var i in obj) {
            var property = obj[i];
            description += i + " = " + property + ",";
        }
        layer.alert(description, {
            skin: 'layui-layer-molv',
            closeBtn: 0
        });
    },
    showInputTree: function (inputId, inputTreeContentId, leftOffset, rightOffset) {
        var onBodyDown = function (event) {
            if (!(event.target.id == "menuBtn" || event.target.id == inputTreeContentId || $(event.target).parents("#" + inputTreeContentId).length > 0)) {
                $("#" + inputTreeContentId).fadeOut("fast");
                $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
            }
        };

        if(leftOffset == undefined && rightOffset == undefined){
            var inputDiv = $("#" + inputId);
            var inputDivOffset = $("#" + inputId).offset();
            $("#" + inputTreeContentId).css({
                left: inputDivOffset.left + "px",
                top: inputDivOffset.top + inputDiv.outerHeight() + "px"
            }).slideDown("fast");
        }else{
            $("#" + inputTreeContentId).css({
                left: leftOffset + "px",
                top: rightOffset + "px"
            }).slideDown("fast");
        }

        $("body").bind("mousedown", onBodyDown);
    },
    baseAjax: function (url, tip) {
        var ajax = new $ax(Feng.ctxPath + url, function (data) {
            Feng.success(tip + "成功!");
        }, function (data) {
            Feng.error(tip + "失败!" + data.responseJSON.message + "!");
        });
        return ajax;
    },
    changeAjax: function (url) {
        return Feng.baseAjax(url, "修改");
    },
    zTreeCheckedNodes: function (zTreeId) {
        var zTree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodes = zTree.getCheckedNodes();
        var ids = "";
        for (var i = 0, l = nodes.length; i < l; i++) {
            ids += "," + nodes[i].id;
        }
        return ids.substring(1);
    },
    eventParseObject: function (event) {//获取点击事件的源对象
        event = event ? event : window.event;
        var obj = event.srcElement ? event.srcElement : event.target;
        return $(obj);
    },
    sessionTimeoutRegistry: function () {
        $.ajaxSetup({
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            complete: function (XMLHttpRequest, textStatus) {
                //通过XMLHttpRequest取得响应头，sessionstatus，
                var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
                if (sessionstatus == "timeout") {
                    //如果超时就处理 ，指定要跳转的页面
                    window.location = Feng.ctxPath + "/global/sessionError";
                }
            }
        });
    },
    initValidator: function(formId,fields){
        $('#' + formId).bootstrapValidator({
            excluded:[":disabled"],//关键配置，表示只对于禁用域不进行验证，其他的表单元素都要验证
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: fields,
            live: 'enabled',
            message: '该字段不能为空'
        });
    },
    underLineToCamel: function (str) {
        var strArr = str.split('_');
        for (var i = 1; i < strArr.length; i++) {
            strArr[i] = strArr[i].charAt(0).toUpperCase() + strArr[i].substring(1);
        }
        var result = strArr.join('');
        return result.charAt(0).toUpperCase() + result.substring(1);
    }
};


layui.use('laydate', function() {
    var laydate = layui.laydate;

    //常规用法
    laydate.render({
        elem: '#birthday',
    });
})

//读取cookie
function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1, c.length);
        }
        if (c.indexOf(nameEQ) == 0) {
            return c.substring(nameEQ.length, c.length);
        }
    };

    return null;
}

//设置cookie
function setCookie(name, value, days) {
    var argc = setCookie.arguments.length;
    var argv = setCookie.arguments;
    var secure = (argc > 5) ? argv[5] : false;
    var expire = new Date();
    if(days==null || days==0) days=0;
    expire.setTime(expire.getTime() + 3600000*24*days);
    document.cookie = name + "=" + escape(value) + ("; path=/") + ((secure == true) ? "; secure" : "");
//        document.cookie = name + "=" + escape(value) + ("; path=/") + ((secure == true) ? "; secure" : "") + ";expires="+expire.toGMTString();
}

function viewDetail(name,url)
{
    // 获取标识数据
    var dataUrl = url,
        dataIndex = $(this).data('index'),
        menuName = name,
        flag = true;
    if (dataUrl == undefined || $.trim(dataUrl).length == 0)return false;

    // 选项卡菜单已存在
    window.parent.$('.J_menuTab').each(function () {
        if ($(this).data('id') == dataUrl) {
            if (!$(this).hasClass('active')) {
                $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
                // 显示tab对应的内容区
                window.parent.$('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == dataUrl) {
                        $(this).show().siblings('.J_iframe').hide();
                        $(this).attr('src', $(this).attr('src'));
                        return false;
                    }
                });
            }
            flag = false;
            return false;
        }
    });

    // 选项卡菜单不存在
    if (flag) {
        var str = '<a href="javascript:;" class="active J_menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
        window.parent.$('.J_menuTab').removeClass('active');

        // 添加选项卡对应的iframe
        var str1 = '<iframe class="J_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
        window.parent.$('.J_mainContent').find('iframe.J_iframe').hide().parents('.J_mainContent').append(str1);

        //显示loading提示
        var loading = layer.load();

        window.parent.$('.J_mainContent iframe:visible').load(function () {
            //iframe加载完成后隐藏loading提示
            layer.close(loading);
        });
        // 添加选项卡
        window.parent.$('.J_menuTabs .page-tabs-content').append(str);
    }
    return false;
}

/**
 * 外呼系统用户建立websocket连接
 * @param cc_user
 */
function cc_socket(cc_user) {
    var host = window.location.host;
    var url = 'ws://'+ host +'/websocket/' + "cc-" + cc_user;
    var lockReconnect = false;  //避免websocket重复连接
    var websocket = null;
    var recon = 1; //重连次数

    createWebSocket(url);   //连接websocket

    function createWebSocket(url) {
        try{
            if('WebSocket' in window){
                websocket = new WebSocket(url);
            }else if('MozWebSocket' in window){
                websocket = new MozWebSocket(url);
            }else{
                layui.use(['layer'],function(){
                    var layer = layui.layer;
                    layer.alert("您的浏览器不支持websocket协议,建议使用新版谷歌、火狐等浏览器，请勿使用IE10以下浏览器，360浏览器请使用极速模式，不要使用兼容模式！");
                });
            }
            initEventHandle();
        }catch(e){
            reconnect(url);
            // console.log(e);
        }
    }

    function initEventHandle() {
        websocket.onclose = function () {
            reconnect(url);
            console.log("cc-" + cc_user+"连接关闭!"+new Date().toUTCString());
        };
        websocket.onerror = function () {
            reconnect(url);
            // console.log("连接错误!");
        };
        websocket.onopen = function () {
            heartCheck.reset().start();      //心跳检测重置
            console.log("cc-" + cc_user+"连接成功!"+new Date().toUTCString());
        };
        websocket.onmessage = function (event) {    //如果获取到消息，心跳检测重置
            heartCheck.reset().start();      //拿到任何消息都说明当前连接是正常的
            console.log("收到消息啦:" +event.data);
            if(event.data!='pong'){
                var server_data = event.data.split("|");
                switch (server_data[0]) {
                    case "预测外呼":
                        setCookie("temp_custom_id_websocket", server_data[1]);
                        setCookie("luyin_ssessionId", server_data[3]);
                        var url = Feng.ctxPath + '/custom/custom_detail/' + server_data[1];
                        viewDetail(server_data[2], url);
                        break;
                }
            }
        }
    }
    // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function() {
        websocket.close();
    }

    function reconnect(url) {
        if(lockReconnect) return;
        lockReconnect = true;
        var reconnect_time = setTimeout(function () {     //没连接上会一直重连，设置延迟避免请求过多
            if (recon <= 10) {
                createWebSocket(url);
                lockReconnect = false;
                recon++;
            } else {
                clearTimeout(reconnect_time);
            }
        }, 2000);
    }

    //心跳检测
    var heartCheck = {
        timeout: 50000,        //50秒发一次心跳
        timeoutObj: null,
        serverTimeoutObj: null,
        reset: function(){
            clearTimeout(this.timeoutObj);
            clearTimeout(this.serverTimeoutObj);
            return this;
            },
        start: function(){
            var self = this;
            this.timeoutObj = setTimeout(function(){
                //这里发送一个心跳，后端收到后，返回一个心跳消息，
                //onmessage拿到返回的心跳就说明连接正常
                websocket.send("ping|cc-"+cc_user);
                console.log("ping!")
                self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                    websocket.close();     //如果onclose会执行reconnect，我们执行websocket.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                }, self.timeout)
            }, this.timeout)
        }
    }
}


function getCall() {
    $.ajax({
        url : "/get-call-msg",
        type : "post",
        dataType : "json",
        success:function (data) {
            $('.topCallNum',parent.document).html(data.callNum);
            $('.topCallTime',parent.document).html(data.callTime);
        }
    })
}

//Ztree创建带有复选框的树
function createTree(url, treeId) {
    var zTree; //用于保存创建的树节点
    var setting = { //设置
        check: {
            enable: true
        },
        view: {
            dblClickExpand: true,//显示辅助线
            showLine: true,
            selectMulti:true,
            showIcon: false
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pid",
                rootPId: 0
            }
        },
        callback:{
            onCheck: zTreeOnCheck,// 节点被点击时调用
        }
    };
    $.ajax({ //请求数据,创建树
        type: 'GET',
        url: url,
        dataType: "json", //返回的结果为json
        success: function(data) {
            zTree = $.fn.zTree.init($(treeId), setting, data); //创建树
        },
        error: function(data) {
            layer.msg("创建树失败!");
        }
    });

    function zTreeOnCheck(event, treeId, treeNode) {
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        var nodes = treeObj.getCheckedNodes();
        $(".item").remove();
        if( nodes.length > 0){
            $.each(nodes,function(i,val) {
                var name = nodes[i].name;
                $('#items').append("<input class='item' name='items[]' value='"+name+"'>");
            });
        }
    }
}





