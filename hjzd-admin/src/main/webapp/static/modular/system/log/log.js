/**
 * 日志管理初始化
 */
var OptLog = {
    id: "managerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function()
{
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    var laydate = layui.laydate;
    OptLog.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#managerTable'
        , height: 440
        , url: Feng.ctxPath + "/log/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '日志类型', field: 'logtype', align: 'center'},
            {title: '日志名称', field: 'logname', align: 'center'},
            // {title: '用户名称', field: 'userName', align: 'center', valign: 'middle', sortable: true, sortName: 'userid'},
            {title: '操作人', field: 'userName', align: 'center', },
            // {title: '类名', field: 'classname', align: 'center'},
            // {title: '方法名', field: 'method', align: 'center'},
            {title: '时间', field: 'createtime', align: 'center', sort: true},
            {title: '具体消息', field: 'message', align: 'center'},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation'}
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
        range: false
    });

    laydate.render({
        elem: '#endTime',    // 注册渲染时间日期
        range: false
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            // 执行重载
            table.reload('managerTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    userName: $("#userName").val(),
                    logName: $("#logName").val(),
                    beginTime: $("#beginTime").val(),
                    endTime: $("#endTime").val(),
                    logType: $("#logType").val(),
                }
            });
        }
    };
    $('.btn-search').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });

    // 重置输入内容
    $("#searchReset").click(function(){
        $('input').val("");
        $('select').val("");
        return false;
    });

    // 监听工具条
    table.on('tool(logs)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'detail'){
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var ajax = new $ax(Feng.ctxPath + "/log/detail/" + id, function (data) {
                Feng.infoDetail("日志详情", data.regularMessage);
            }, function (data) {
                Feng.error("获取详情失败!");
            });
            ajax.start();
        }
        return false;
    });

})

/**
 * 清空日志
 */
OptLog.delLog = function () {
    Feng.confirm("是否清空所有日志?",function(){
        var ajax = Feng.baseAjax("/log/delLog","清空日志");
        ajax.start();
        OptLog.table.reload('managerTable');
    });
}

// /**
//  * 初始化表格的列
//  */
// OptLog.initColumn = function () {
//     return [
//         {field: 'selectItem', radio: true},
//         {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
//         {title: '日志类型', field: 'logtype', align: 'center', valign: 'middle', sortable: true},
//         {title: '日志名称', field: 'logname', align: 'center', valign: 'middle', sortable: true},
//         // {title: '用户名称', field: 'userName', align: 'center', valign: 'middle', sortable: true, sortName: 'userid'},
//         {title: '用户名称', field: 'userName', align: 'center', valign: 'middle'},
//         {title: '类名', field: 'classname', align: 'center', valign: 'middle', sortable: true},
//         {title: '方法名', field: 'method', align: 'center', valign: 'middle', sortable: true},
//         {title: '时间', field: 'createtime', align: 'center', valign: 'middle', sortable: true},
//         {title: '具体消息', field: 'message', align: 'center', valign: 'middle', sortable: true}];
// };
//
// /**
//  * 检查是否选中
//  */
// OptLog.check = function () {
//     var selected = $('#' + this.id).bootstrapTable('getSelections');
//     if(selected.length == 0){
//         Feng.info("请先选中表格中的某一记录！");
//         return false;
//     }else{
//         OptLog.seItem = selected[0];
//         return true;
//     }
// };
//
// /**
//  * 查看日志详情
//  */
// OptLog.detail = function () {
//     if (this.check()) {
//         var ajax = new $ax(Feng.ctxPath + "/log/detail/" + this.seItem.id, function (data) {
//             Feng.infoDetail("日志详情", data.regularMessage);
//         }, function (data) {
//             Feng.error("获取详情失败!");
//         });
//         ajax.start();
//     }
// };
//
//

//
// /**
//  * 查询表单提交参数对象
//  * @returns {{}}
//  */
// OptLog.formParams = function() {
//     var queryData = {};
//     queryData['userName'] = $("#userName").val();
//     queryData['logName'] = $("#logName").val();
//     queryData['beginTime'] = $("#beginTime").val();
//     queryData['endTime'] = $("#endTime").val();
//     queryData['logType'] = $("#logType").val();
//
//     return queryData;
// }
//
// /**
//  * 搜索条件重置
//  */
// OptLog.reset = function () {
//     $('input').val("");
//     $('select').val("");
// }
//
// /**
//  * 查询日志列表
//  */
// OptLog.search = function () {
//
//     OptLog.table.refresh({query: OptLog.formParams()});
// };
//
// $(function () {
//     var defaultColunms = OptLog.initColumn();
//     var table = new BSTable(OptLog.id, "/log/list", defaultColunms);
//     table.setPaginationType("server");
//     table.setQueryParams(OptLog.formParams());
//     OptLog.table = table.init();
// });
