/**
 * 短信发送
 */
$("#send-sms").on("click",function(){

    // layer.msg("暂时无法使用短信功能！");
    // return false;

    var customId = $("#customId").val();
    var url = Feng.ctxPath +"/custom/to_sendMessage/" + customId;

    layer.open({
        title: "发送短信",
        type: 2,
        area: ['800px', '350px'],
        //点击遮罩关闭
        shadeClose: true,
        content: url,

        scrollbar: false,
    })
});
layui.use(['form', 'table', 'laydate'], function () {
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;



    laydate.render({
        elem: '#reserveTime' //注册渲染时间日期
        ,type: 'datetime'
    });

    form.on('select(call-select-detail)', function (res) {
        var value = res.value;
        var valueName = res.elem[res.elem.selectedIndex].text;
        if (valueName.indexOf("预约联系") >= 0) {
            $('.call_status_Div').css('display','none');
            $('.reserveDiv').css('display', 'block');
            $('#reserveTime').attr('lay-verify', 'required');
            $('#call_status_content').removeAttr('lay-verify');
        } else {
            $('.call_status_Div').css('display','block');
            $('.reserveDiv').css('display', 'none');
            $('#reserveTime').removeAttr('lay-verify');
            $('#call_status_content').attr('lay-verify','required');
        }
        if (value == '') {
            $("#call_status_content").html('');
            form.render();
        } else {
            $.ajax({
                url:  Feng.ctxPath + "/custom/get_call_status" ,
                method: "post",
                data: {value: value},
                success: function (data)
                {
                    var result = "<option value=''>请选择</option>";
                    if (data) {
                        $.each(data.data, function (idx, obj) {
                            result +="<option value='"+obj.id+"'>"+obj.name+"</option>";
                        });
                    }
                    $("#call_status_content").html(result);
                    form.render();
                }
            })
        }
    });

    form.on('select(call-again-detail)', function (res) {
        var valueName = res.elem[res.elem.selectedIndex].text;
        var value = res.value;
        if (valueName.indexOf("预约联系") >= 0) {
            $('.reserveDiv').css('display', 'block');
            $('#reserveTime').attr('lay-verify', 'required');
        } else {
            $('.reserveDiv').css('display', 'none');
            $('#reserveTime').removeAttr('lay-verify');
        }
    });

    form.on('submit(submitSave)', function(data){
        // add by eric 2018-09-26 start
        var call_status_content = ""; // ----> call_status_id
        if ($('#call_status_top option:selected').text() =="预约联系") {
            call_status_content = $('#call_status_top').val();
        } else {
            if ($('#call_status_top option:selected').val() =="-1") {
                call_status_content = $('#call_status_top').val();
            }else{
                call_status_content = $('#call_status_content').val()
            }
        }

        if(call_status_content == ''){
            Feng.error("请选择致电结果二级类型!");
            return false;
        }
        data.field.call_status_id = call_status_content;

        // add by eric 2018-09-26 end
        data.field.custom_name = $("input[name='custom_name']").val();
        $.ajax({
            url: Feng.ctxPath + '/custom/save-call-status',
            type: 'POST',
            data: data.field,
            dataType: 'JSON',
            success: function (data) {           //成功回调
                change_status(2)
                if (data.code == 200) {
                    layer.msg(data.message,function () {
                        //table.reload('callLogTable');
                        // window.location.reload();

                        closepage();
                    });
                } else {
                    layer.msg("操作失败");
                }
            }
        });
        return false;
    });



    $('.next-case').on('click', function () {
        var customId = $("#customId").val();
        $.ajax({
            url: Feng.ctxPath + '/custom/next-custom',
            type: 'POST',
            data: {"id":customId},
            dataType: 'JSON',
            success: function (data) {           //成功回调
                if (data.code == 200) {
                    var url = Feng.ctxPath + '/custom/custom_detail/' + data.id;
                    viewDetail(data.name, url);
                } else {
                    layer.msg("当前客户已经是最后一位！");
                }
            }
        })
    })


});

layui.use(['form', 'laypage', 'layer', 'table', 'laydate'], function()
{
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    var form = layui.form;    // 表格

    // 执行一个 table 实例
    table.render({
        elem: '#operateLogTable'
        , height: 220
        , url: Feng.ctxPath + "/log/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '日志类型', field: 'logtype', align: 'center',width: 100},
            {title: '日志名称', field: 'logname', align: 'center',width: 150},
            //{title: '用户名称', field: 'userName', align: 'center', valign: 'middle', sortable: true, sortName: 'userid'},
            {title: '操作人', field: 'userName', align: 'center',width: 100 },
            // {title: '类名', field: 'classname', align: 'center'},
            // {title: '方法名', field: 'method', align: 'center'},
            {title: '时间', field: 'createtime', align: 'center', sort: true,width: 200},
            {title: '具体消息', field: 'message', align: 'center'},
        ]],
        where: {order: 'desc',customId:$("#custom_id").val() !='' ? $("#custom_id").val() : ''},    // 定义一个默认排序方式，与java后台对接
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 20, 50, 100, 200, 500],
    });

});

/**
 * 修改坐席状态
 * @param type 1 暂停呼叫   2 继续呼叫
 */
function change_status(type){
    var orgidentity = getCookie("cc_orgidentity");  // astercc | lianghuapai   团队唯一标识
    var usertype = 'agent';  // agent | account  actercc用户名类型
    var pwdtype = 'plaintext';  // actercc密码类型
    var user = getCookie('cc_user');//'8001';
    var password = getCookie('cc_password');// 'temp123';

    var data = {
        'type': type,
        'usertype': usertype,
        'user': user,
        'orgidentity': orgidentity,
        'pwdtype': pwdtype,
        'password': password,
        'pause_reason': "rest",
        'dnd': '',
        'pushevent': '',
        'intparam': '',
    };

    var url = 'http' + '://' + getCookie("cc_ipaddress") + '/setevent/queuePauseCJI?callback=?';

    $.ajax({
        cache:false,
        url: url,
        type:'get',
        data:data,
        dataType:'jsonp',
        success:function (data) {

        }
    })
}

/**
 * 关闭当前详情iframe页面
 */
function closepage() {
    var phone = $('input[name=phone]').val()+" ";
    parent.$(".J_menuTab").each(function(){
        if ( $(this).text() == phone){
            $(this).find('i').click();
        }
    });
}
