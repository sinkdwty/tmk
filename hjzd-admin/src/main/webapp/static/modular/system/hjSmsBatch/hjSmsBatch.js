/**
 * sms_batch管理初始化
 */
var HjSmsBatch = {
    id: "HjSmsBatchTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};


/**
 * 初始化表格的列
 */
layui.use(['form','table','laydate','element'],function () {
    var form = layui.form;
    var table= layui.table;
    var laydate = layui.laydate; // 日期
    HjSmsBatch.table = table;

    table.render({
        elem: '#HjSmsBatchTable',
        height: 440,
        url: Feng.ctxPath + "/hjSmsBatch/list", // 数据接口
        page: true , // 开启分页
        size: 'sm',
        method: 'get',
        cols:[[
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '批次名称', field: 'sms_batch_name', visible: true, align: 'center', valign: 'middle'},
            {title: '批次描述', field: 'batch_desc', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'create_at', visible: true, align: 'center', valign: 'middle',width:160},
            {title: '短信内容', field: 'sms_content', visible: true, align: 'center', valign: 'middle'},
            {title: '容量', field: 'batch_capacity', visible: true, align: 'center', valign: 'middle',width:80},
            {title: '剩余容量', field: 'left_capacity', visible: true, align: 'center', valign: 'middle',width:100},
            {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle',
                templet: function (data)
                {
                    return data.status == '0' ? '<span style="color: #1ab394;cursor: pointer;font-weight: bold;">待发送</span>' : data.status == '1' ? '<span style="color:#1ab394;cursor: pointer">预约待发送</span>' : data.status == '2' ? '<span style="color:#c00;cursor: pointer">已发送</span>' : data.status == '3' ? '<span style="color:#c00;cursor: pointer">处理成功</span>' : data.status == '4' ? '<span style="color:#c00;cursor: pointer">处理失败</span>' : '<span style="color:#CCCCCC;cursor: pointer;font-weight: bold;">可重发</span>';
                }

            },
            {title: '结果描述', field: 'send_note', visible: true, align: 'center', valign: 'middle'},
            {title: '成功数', field: 'success_sms', visible: true, align: 'center', valign: 'middle',width:80},
            {title: '失败数', field: 'failure_sms', visible: true, align: 'center', valign: 'middle',width:80},
            {title: '批次结果', field: 'result_text', visible: true, align: 'center', valign: 'middle'},
            {title: '批次明细', field: 'result_detail', visible: true, align: 'center', valign: 'middle'},
            {fixed: 'right', title: '操作', align:'center', toolbar:'#operation',class:"exportClass",width:250}
        ]],
        where: {order: 'desc'},
        request: {
            pageName: 'page',
            limitName: 'limit'
        },
        limits: [10,20,50,100,200,500],
    });


    laydate.render({
        elem: '#beginTime',    // 注册渲染时间日期
        type: 'date',
        range: false
    });

    laydate.render({
        elem: '#endTime',    // 注册渲染时间日期
        type: 'date',
        range: false
    });





// 搜索
var $ = layui.$, active = {
    reload: function(){
        var condition = $('#condition');
        var beginTime = $('#beginTime');
        var endTime = $('#endTime');

        if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
            Feng.error("创建时间选择范围不正确!");
            return false;
        }

        // 执行重载
        table.reload('HjSmsBatchTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            ,where: {
                condition: condition.val(),
                beginTime: beginTime.val(),
                endTime: endTime.val(),
                column: $("#search_column option:selected").val(),
                status: $('#status').val()
            }
        });
    },

    reset:function () {
        // 重置输入内容
        $(this).parents('.layui-form').find('.resetInput input').val('');
        $('select').val('-1');
        return false;
    }
};
    $('.search-btn,.export-btn,.reset-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
        return false;
    });

    $("#create-sms-batch").on('click',function () { // 添加短信批次
        var name = "添加短信批次"; // tab 名称
        var url = Feng.ctxPath + '/hjSmsBatch/hjSmsBatch_add';
        popDetail(name,url);
    });

    table.on('tool(smsbatch)', function(obj) {    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值
        if(layEvent === 'order_send'){ // 预约发送
            var id = data['id'];
            var index = layer.open({
                type: 2,
                title: '短信批次预约设置',
                area: ['80%', '70%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/hjSmsBatch/order_send/' + id
            });
            HjSmsBatch.layerIndex = index;
        } else if(layEvent === 'now_send'){ // 发送短信
            var status = data['status'];
            if(status =='2'){
                Feng.error("该批次已经发送，不允许重发！");
                return false;
            }else if (status == '3'){
                Feng.error("该批次已发送成功，不允许重发！");
                return false;
            }else if (status == "4"){
                Feng.error("该批次短信处理失败，可查证后修改状态后重发");
                return false;
            }else{
                var operation = function(){
                    var id = data['id'];    // 获取到当前行的id值
                    var ajax = new $ax(Feng.ctxPath + "/hjSmsBatch/now_send", function () {
                        table.reload('HjSmsBatchTable');
                    }, function (data) {
                        Feng.error("发送失败!" + data.responseJSON.message + "!");
                        table.reload('HjSmsBatchTable');
                    });
                    ajax.set("batchSmsId", id);
                    ajax.start();
                };
                Feng.confirm("是否立即发送" + data['sms_batch_name'] + "?",operation);
            }
        } else if(layEvent === 'send_detail') {  //结果查看
            var id = data['id'];
            var title = data['sms_batch_name'];
            var url = Feng.ctxPath + '/hjSmsBatch/send_detail/' + id;
            var status = data['status'];
            var result_detail = data['result_detail']; // 结果明细

            if (status == '0' || status == '1'){ //还未发送，请先发送批次
                Feng.success("该批次短信未发送，无法查询！");
                return false;
            }
            var index = layer.open({
                type: 2,
                title: '结果明细',
                area: ['80%', '70%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/hjSmsBatch/send_detail/' + id
            });
            HjSmsBatch.layerIndex = index;
        } else if(layEvent === 'edit_sms') {     //编辑
            var id = data['id'];
            var title = "短信批次修改";
            // var title = data['sms_batch_name'];
            // 此处判断 批次状态，为2，3是不允许修改！
            var status = data['status'];
            if(status =='2'){
               Feng.error("该批次已经发送，不允许修改！");
               return false;
            }else if (status == '3'){
               Feng.error("该批次已发送成功，不允许修改！");
               return false;
            }else if (status == '0' || status == '1'){
                var url = Feng.ctxPath + '/hjSmsBatch/hjSmsBatch_update/' + id;
                popDetail(title, url);
            }else if (status == "4"){ // 该批次短信处理失败，可查证后确认是否重发！
                var url = Feng.ctxPath + '/hjSmsBatch/hjSmsBatch_update/' + id;
                popDetail(title, url);
            }else{
               console.info("状态未知，不处理");
            }
        }
        return false;
    });

});

function popDetail(title,url) {
    var index = layer.open({
        type: 2,
        title: title,
        area: ['80%', '70%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: url
    });
}


/**
 * 检查是否选中
 */
HjSmsBatch.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        HjSmsBatch.seItem = selected[0];
        return true;
    }
};



/**
 * 打开查看sms_batch详情
 */


/**
 * 删除sms_batch
 */
HjSmsBatch.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/hjSmsBatch/delete", function (data) {
            Feng.success("删除成功!");
            HjSmsBatch.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("hjSmsBatchId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询sms_batch列表
 */
HjSmsBatch.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    HjSmsBatch.table.refresh({query: queryData});
};