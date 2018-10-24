/**
 * 商品管理管理初始化
 */
var Policy = {
    id: "PolicyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
// Policy.initColumn = function () {
//     return [
//         {field: 'selectItem', radio: true, visible: false},
//         {title: '序号', field: 'Number', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
//                 var pageSize=$('#PolicyTable').bootstrapTable('getOptions').pageSize;//通过表的#id 可以得到每页多少条
//                 var pageNumber=$('#PolicyTable').bootstrapTable('getOptions').pageNumber;//通过表的#id 可以得到当前第几页
//                 return Number(pageSize * (pageNumber - 1) + index + 1);
//             }
//         },
//         {title: '策略名称', field: 'policyName', visible: true, align: 'center', valign: 'middle'},
//         {title: '话术id', field: 'policyKey', visible: true, align: 'center', valign: 'middle'},
//         {title: '备注', field: 'note', visible: true, align: 'center', valign: 'middle'},
//         {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle',
//             formatter: function (value, row, index){ // 单元格格式化函数
//                 var text = '';
//                 if (value == "1") {
//                     text = "<span style=\"color:#1ab394;cursor: pointer\">启用</span>";
//                 } else {
//                     text = "<span style=\"color: #c00;cursor: pointer\">禁用</span>";
//                 }
//                 return text;
//             }
//         },
//         {title: '创建时间', field: 'createdAt', visible: true, align: 'center', valign: 'middle'},
//         {title: '操作',field: 'operate',align: 'center',events: "operateEvents",formatter: operateFormatter,width:180}
//     ];
// };
//
// function operateFormatter(value, row, index) {
//     // console.info(row);
//     var status_btn = '';
//     if (row.status == 1) {
//         status_btn = '<button type="button" class="update-status btn btn-warning  btn-sm" style="margin-right:5px;">禁用</button>';
//     } else {
//         status_btn = '<button type="button" class="update-status btn btn-success  btn-sm" style="margin-right:5px;">启用</button>';
//     }
//     return [
//         '<button type="button" class="RoleOfB btn btn-primary  btn-sm" style="margin-right:5px;">修改</button>',
//         '<button type="button" class="delete-policy btn btn-primary btn-danger  btn-sm" style="margin-right:5px;">删除</button>',
//         status_btn,
//     ].join('');
// }
//
// window.operateEvents = {
//     'click .RoleOfB': function (e, value, row, index) {
//         Policy.openPolicyDetail(row);
//     },
//     'click .delete-policy': function (e, value, row, index) {
//         var id = row.id;
//         if (id != '') {
//             layer.confirm("确定要删除该数据？", {
//                 btn: ['确定', '取消']
//             }, function (index) {
//                 Policy.delete(id);
//                 layer.close(index);
//             }, function (index) {
//                 layer.close(index);
//             });
//         }
//     },
//     'click .update-status': function (e, value, row, index) {
//         var id = row.id;
//         if (id != '') {
//             layer.confirm("确定要执行该操作吗？", {
//                 btn: ['确定', '取消']
//             }, function (index) {
//                 Policy.updateStatus(id);
//                 layer.close(index);
//             }, function (index) {
//                 layer.close(index);
//             });
//         }
//     },
// };
//
// /**
//  * 点击添加商品管理
//  */
// Policy.openAddPolicy = function () {
//     var index = layer.open({
//         type: 2,        //iframe层
//         title: '添加策略',
//         area: ['800px','510px'], //宽高
//         fix: false, //不固定
//         maxmin: true,
//         content: Feng.ctxPath + '/policy/policy_add'
//     });
//     this.layerIndex = index;
// };
//
// /**
//  * 打开查看商品管理详情
//  */
// Policy.openPolicyDetail = function (row) {
//     // if (this.check()) {
//     var id = row.id;
//     var index = layer.open({
//         type: 2,
//         title: '修改策略',
//         area: ['800px','510px'], //宽高
//         fix: false, //不固定
//         maxmin: true,
//         content: Feng.ctxPath + '/policy/policy_edit/' + id
//     });
//     this.layerIndex = index;
//     // }
// };
//
// /**
//  * 删除商品管理
//  */
// Policy.delete = function (id) {
//     var ajax = new $ax(Feng.ctxPath + "/policy/delete", function (data) {
//         Feng.success("删除成功!");
//         Policy.table.refresh();
//     }, function (data) {
//         Feng.error("删除失败!" + data.responseJSON.message + "!");
//     });
//     ajax.set("policyId", id);
//     ajax.start();
// };
//
// /**
//  * 点击更改状态
//  */
// Policy.updateStatus = function (id) {
//     var ajax = new $ax(Feng.ctxPath + "/policy/updateStatus", function (data) {
//         Feng.success("操作成功!");
//         Policy.table.refresh();
//     }, function (data) {
//         Feng.error("操作失败!" + data.responseJSON.message + "!");
//     });
//     ajax.set("policyId", id);
//     ajax.start();
// }
//
// /**
//  * 查询商品管理列表
//  */
// Policy.search = function () {
//     var queryData = {};
//     queryData['name'] = $("#name").val();
//     queryData['beginTime'] = $("#beginTime").val();
//     queryData['endTime'] = $("#endTime").val();
//     Policy.table.refresh({query: queryData});
// };
// /**
//  * 搜索条件重置
//  */
// Policy.reset = function () {
//     $('input').val("");
//     $('select').val("3");
// }
//
// /**
//  * 初始化表格
//  */
// $(function () {
//     var defaultColunms = Policy.initColumn();
//     var table = new BSTable(Policy.id, "/policy/list", defaultColunms);
//     table.setPaginationType("server");
//     Policy.table = table.init();
// });
//
// /**
//  * 搜索时间插件
//  */
// layui.use('laydate', function(){
//     var laydate = layui.laydate;
//
//     //执行一个laydate实例
//     laydate.render({
//         elem: '#beginTime' //指定元素
//     });
//     laydate.render({
//         elem: '#endTime' //指定元素
//     });
// });

layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function()
{
    var laydate = layui.laydate;    // 日期
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格

    laydate.render({
        elem: '#beginTime',    // 注册渲染时间日期
        range: false
    });

    laydate.render({
        elem: '#endTime',    // 注册渲染时间日期
        range: false
    });

    // 执行一个 table 实例
    table.render({
        elem: '#policyTable'
        , height: 420
        , url: Feng.ctxPath + "/policy/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '策略名称', field: 'policyName', visible: true, align: 'center', valign: 'middle'},
            {title: '话术id', field: 'policyKey', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'note', visible: true, align: 'center', valign: 'middle'},
            {title: '状态', field: 'status', align: 'center', valign: 'middle', sortable: true,
                templet: function (data)
                {
                    return data.status == '0' ? '<span style="color: #c00;cursor: pointer">禁用</span>' : '<span style="color:#1ab394;cursor: pointer">启用</span>';
                }
            },
            {title: '创建时间', field: 'createdAt', visible: true, align: 'center', valign: 'middle'},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation'}

        ]],
        // initSort: {
        //     field: 'createtime' //排序字段，对应 cols 设定的各字段名
        //     ,type: 'desc' //排序方式 asc: 升序、desc: 降序、null: 默认排序
        // },
        where: {order: 'desc'},    // 定义一个默认排序方式，与java后台对接
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 20, 50, 100, 200, 500],
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            var name = $('#name');
            var beginTime = $('#beginTime');
            var endTime = $('#endTime');

            if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
                Feng.error("创建时间选择范围不正确!");
                return false;
            }

            // 执行重载
            table.reload('policyTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    name: name.val(),
                    beginTime: beginTime.val(),
                    endTime: endTime.val(),
                }
            });
        }
    };
    $('.search-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });

    // 添加
    $("#create-policy").click(function(){
        var index = layer.open({
            type: 2,
            title: '添加策略',
            area: ['40%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/policy/policy_add'
        });
        Policy.layerIndex = index;
        return false;
    });

    // 重置输入内容
    $("#searchReset").click(function(){
        $(this).parents('.layui-form').find('input').val('');
        return false;
    });

    // 监听工具条
    table.on('tool(policy)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'edit'){
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '修改策略',
                area: ['40%', '80%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/policy/policy_edit/' + id

            });
            Policy.layerIndex = index;

        } else if(layEvent === 'delete'){
            var operation = function(){
                var policyId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/policy/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('policyTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('policyTable');
                });
                ajax.set("policyId", policyId);
                ajax.start();
            };

            Feng.confirm("是否删除‘" + data['policyName'] + "’?",operation);

        } else if(layEvent === 'freezeAccount'){
            var operation = function() {
                var policyId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/policy/updateStatus", function (data) {
                    Feng.success("禁用成功!");
                    table.reload('policyTable');
                }, function (data) {
                    Feng.error("禁用失败!" + data.responseJSON.message + "!");
                });
                ajax.set("policyId", policyId);
                ajax.start();
            }
            Feng.confirm("是否禁用‘" + data['policyName'] + "’?",operation);
        } else if(layEvent === 'unfreeze'){
            var operation = function() {
                var policyId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/policy/updateStatus", function (data) {
                    Feng.success("解除禁用成功!");
                    table.reload('policyTable');
                }, function (data) {
                    Feng.error("解除禁用失败!");
                });
                ajax.set("policyId", policyId);
                ajax.start();
            }
            Feng.confirm("是否解除禁用‘" + data['policyName'] + "’?",operation);
        }

        return false;
    });

})

