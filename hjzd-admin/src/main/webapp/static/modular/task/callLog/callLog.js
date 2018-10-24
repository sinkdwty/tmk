/**
 * 通话日志管理初始化
 */
var CallLog = {
    id: "CallLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
layui.use(['form', 'table', 'laydate','element','layer'], function () {
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;
    var element = layui.element;
    var layer = layui.layer;
    CallLog.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#CallLogTable'
        , height: 420
        , url: Feng.ctxPath + "/callLog/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        , method: 'post'
        , cols: [[
            {title: '序号', type: 'numbers', align:'center',width:50},
            {title: '坐席姓名', field: 'userName', visible: true, align: 'center', valign: 'middle',width:100},
            {title: '客户姓名', field: 'customName', visible: true, align: 'center', valign: 'middle',width:80},
            {title: '手机号码', field: 'customPhone', visible: true, align: 'center', valign: 'middle',width:108,
                templet:function (value) {
                    if (value.customPhone != null && value.customPhone != "") {
                        var len = value.customPhone;
                        let ruten = len.substring(3, 7); //提取字符串下标之间的字符。
                        if($(".hasRole").val() == "worker") {   //坐席角色手机号脱敏
                            return len.replace(ruten, '****'); //字符串中用字符替换另外字符，或替换一个与正则表达式匹配的子串。
                        } else {        //其他角色手机号不需要脱敏
                            return len;
                        }
                    } else {
                        return "";
                    }
                }
            },
            {title: '致电结果', field: 'callStatusName', visible: true, align: 'center', valign: 'middle',width:90},
            {title: '呼叫开始时间', field: 'callStartTime', visible: true, align: 'center', valign: 'middle',width:145},
            {title: '呼叫结束时间', field: 'callEndTime', visible: true, align: 'center', valign: 'middle',width:145},
            {title: '本次呼叫总秒数', field: 'callSecond', visible: true, align: 'center', valign: 'middle',width:125},
            {title: '分机号', field: 'callAgentNo', visible: true, align: 'center', valign: 'middle',width:80},
            {title: '电话小结', field: 'note', visible: true, align: 'center', valign: 'middle',width:150},
            {title: '创建时间', field: 'createdAt', visible: true, align: 'center', valign: 'middle',width:145},
            {title: '质检状态', field: 'checkStatus', visible: true, align: 'center', valign: 'middle',width:100,
                templet: function (data)
                {
                    return data.checkStatus == '1' ? '<span style="color: #1ab394;cursor: pointer;font-weight: bold;">质检通过</span>' : data.checkStatus == '2' ? '<span style="color:#c00;cursor: pointer">质检不通过</span>' : '<span style="color:#CCCCCC;cursor: pointer;font-weight: bold;">未质检</span>';
                }

            },
            {title: '质检备注', field: 'checkNote', visible: true, align: 'center', valign: 'middle',width:145},
            {fixed: 'right', title: '操作', align:'center', toolbar:'#operation',class:"exportClass",width:270}
        ]],
        where: {order: 'desc'},    // 定义一个默认排序方式，与java后台对接
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 20, 50, 100, 200, 500],
    });
    laydate.render({
        elem: '#beginTime',    // 注册渲染时间日期
        type: 'datetime',
        range: false
    });

    laydate.render({
        elem: '#endTime',    // 注册渲染时间日期
        type: 'datetime',
        range: false
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            var beginTime = $('#beginTime');
            var endTime = $('#endTime');
            if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
                Feng.error("创建时间选择范围不正确!");
                return false;
            }
            // 执行重载
            table.reload('CallLogTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    condition: $("#condition").val(),
                    beginTime: $('#beginTime').val(),
                    endTime: $('#endTime').val(),
                    checkStatus: $('#check_status').val(), // add by eric 2018-09-18
                    column: $("#search_column option:selected").val(),
                    call_status_id: $('#call_status_id').val()
                }
            });
        },reset:function () {
            // 重置输入内容
            $(this).parents('.layui-form').find('.resetInput input').val('');
            $(this).parents('.layui-form').find('.resetInput select').val('-1');
            return false;

        },export:function () {
            var condition = $("#condition").val();
            var beginTime = $('#beginTime').val();
            var endTime = $('#endTime').val();
            var checkStatus =  $('#check_status').val();
            var column = $("#search_column option:selected").val();
            var call_status_id = $('#call_status_id').val();
            window.location.href=Feng.ctxPath + "/callLog/export-callLog" + "?column="+column+"&condition="+condition+"&beginTime="+beginTime+"&endTime="+endTime+"&checkStatus="+checkStatus+"&call_status_id="+call_status_id;
            return false;

        }
    };
    $('.btn-search,.btn-reset').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });

    table.on('tool(CallLogTable)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        // var calldate = data.callStartTime.slice(0,10);
        var call_userdata = data.callUserdata;
        var user_id = data.userId;
        var callLogId = data.id;

        if (layEvent === 'openCustom' || layEvent === 'download') { //播放录音
            $(".play").attr("disabled",true);
            $(".download").attr("disabled",true);
            var tishi = layer.load(0);
            $.ajax({
                url:Feng.ctxPath + "/callLog/get-record-address",
                method:"post",
                dataType:"json",
                // data:{calldate:calldate,call_userdata:call_userdata,user_id:user_id},
                data:{callLogId:callLogId,way:layEvent},
                success:function (res) {
                    $(".play").attr("disabled",false);
                    $(".download").attr("disabled",false);
                    layer.close(tishi)
                    if(res.code == '200') {

                        if(layEvent === 'openCustom') {
                            layer.open({
                                title: false, //不显示标题
                                type: 1,
                                //点击遮罩关闭
                                shadeClose: true,
                                content: '<audio src="'+ res.data +'" autoplay preload loop="loop" controls="controls">您的浏览器不支持该功能，请使用其他浏览器</audio>',

                            })
                        }
                        else {
                            $('#download').attr("download",res.data);
                            $("#download").append("<span></span>");
                            $("#download span").click();
                        }

                    }  else if(res.code == '210') {
                        $('#download').attr("href", Feng.ctxPath + "/callLog/download-record?phone="+data.customPhone+"&cc_user="+data.callAgentNo+"&start="+data.createdAt);
                        $("#download span").click();
                    }else {
                        layer.msg(res.data);
                    }

                },
                error:function(e){
                    $(".play").attr("disabled",false);
                    $(".download").attr("disabled",false);
                }
            });


        }else if (layEvent === 'checkCaseLog') { // add by eric 2018-09-18 审核案例 start
            /*var operation = function(checks){
                var recordId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/callLog/check-record", function () {
                    Feng.success("操作成功!");
                    table.reload('CallLogTable');
                }, function (data) {
                    Feng.error("操作失败!" + data.responseJSON.message + "!");
                    table.reload('CallLogTable');
                });
                ajax.set("recordId", recordId);
                ajax.set("checkStatus",checks);
                ajax.start();
            };
            layer.confirm('是否质检通过?',{
               btn: ['通过','不通过']
            },function (index) {
               operation(1);
               layer.close(index);
            },function (index) {
               operation(2);
               layer.close(index);
            });*/
            // add by eric 2018-09-18 审核案例 end
            var recordId = data['id'];    // 获取到当前行的id值
            var url = Feng.ctxPath +"/callLog/check-status/" + recordId;

            layer.open({
                title: "录音质检",
                type: 2,
                area: ['800px', '350px'],
                //点击遮罩关闭
                shadeClose: true,
                content: url,
                scrollbar: false,
            })
        } else if (layEvent === 'custom-detail') {
            var id = data['caseId'];
            var title = data['customName'] ? data['customName'] : data['customPhone'].substr(0,3)+"****"+data['customPhone'].substr(7);
            var url = Feng.ctxPath + '/custom/custom_detail/' + id;
            viewDetail(title, url);
        } else{

        }
    })
});


/**
 * 检查是否选中
 */
CallLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        CallLog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加通话日志
 */
CallLog.openAddCallLog = function () {
    var index = layer.open({
        type: 2,
        title: '添加通话日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/callLog/callLog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看通话日志详情
 */
CallLog.openCallLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '通话日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/callLog/callLog_update/' + CallLog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除通话日志
 */
CallLog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/callLog/delete", function (data) {
            Feng.success("删除成功!");
            CallLog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("callLogId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询通话日志列表
 */
CallLog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    CallLog.table.refresh({query: queryData});
};




