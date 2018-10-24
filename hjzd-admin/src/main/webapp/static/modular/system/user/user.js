/**
 * 系统管理--用户管理的单例对象
 */
var MgrUser = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    deptid:0
};
//
// /**
//  * 初始化表格的列
//  */
// MgrUser.initColumn = function () {
//     var columns = [
//         {field: 'selectItem', radio: true},
//         {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
//         {title: '账号', field: 'account', align: 'center', valign: 'middle', sortable: true},
//         {title: '性别', field: 'sexName', align: 'center', valign: 'middle', sortable: true},
//         {title: '姓名', field: 'name', align: 'center', valign: 'middle'},
//         {title: '角色', field: 'roleName', align: 'center', valign: 'middle'},
//         {title: '部门', field: 'deptName', align: 'center', valign: 'middle'},
//         {title: '邮箱', field: 'email', align: 'center', valign: 'middle'},
//         {title: '电话', field: 'phone', align: 'center', valign: 'middle'},
//         {title: '创建时间', field: 'createtime', align: 'center', valign: 'middle', sortable: true},
//         // {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true}];
//         {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true,
//             formatter:function(value, row, index){
//                 return value;
//             },
//             cellStyle:function(value){
//                 if (value == '启用') {
//                     return {css:{"color": "#1ab394", "cursor" : "pointer"}}
//                 } else {
//                     return {css:{"color": "#c00", "cursor" : "pointer"}}
//                 }
//             }
//
//         }];
//     return columns;
// };

/**
 * 检查是否选中
 */
MgrUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrUser.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加管理员
 */
MgrUser.openAddMgr = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户',
        area: ['55%', '50%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/mgr/user_add'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 * @param userId 管理员id
 */
MgrUser.openChangeUser = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '编辑',
            area: ['75%', '70%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/user_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 点击角色分配
 * @param
 */
MgrUser.roleAssign = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分配角色',
            area: ['75%', '70%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/role_assign/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户
 */
MgrUser.delMgrUser = function () {
    if (this.check()) {

        var operation = function(){
            var userId = MgrUser.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/mgr/delete", function () {
                Feng.success("删除成功!");
                MgrUser.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("userId", userId);
            ajax.start();
        };

        Feng.confirm("是否删除用户" + MgrUser.seItem.account + "?",operation);
    }
};

/**
 * 冻结用户账户
 * @param userId
 */
MgrUser.freezeAccount = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/freeze", function (data) {
            Feng.success("禁用成功!");
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("禁用失败!" + data.responseJSON.message + "!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
};

/**
 * 解除冻结用户账户
 * @param userId
 */
MgrUser.unfreeze = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        var ajax = new $ax(Feng.ctxPath + "/mgr/unfreeze", function (data) {
            Feng.success("启用成功!");
            MgrUser.table.refresh();
        }, function (data) {
            Feng.error("启用失败!");
        });
        ajax.set("userId", userId);
        ajax.start();
    }
}

/**
 * 重置密码
 */
MgrUser.resetPwd = function () {
    if (this.check()) {
        var userId = this.seItem.id;
        parent.layer.confirm('是否重置密码为111111？', {
            btn: ['确定', '取消'],
            shade: false //不显示遮罩
        }, function () {
            var ajax = new $ax(Feng.ctxPath + "/mgr/reset", function (data) {
                Feng.success("重置密码成功!");
            }, function (data) {
                Feng.error("重置密码失败!");
            });
            ajax.set("userId", userId);
            ajax.start();
        });
    }
};

MgrUser.resetSearch = function () {
    $("#name").val("");
    $("#beginTime").val("");
    $("#endTime").val("");

    MgrUser.search();
}

MgrUser.search = function () {
    var queryData = {};

    queryData['deptid'] = MgrUser.deptid;
    queryData['name'] = $("#name").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();

    MgrUser.table.refresh({query: queryData});
}

MgrUser.onClickDept = function (e, treeId, treeNode) {
    MgrUser.deptid = treeNode.id;
    MgrUser.search();
};

// $(function () {
//     var defaultColunms = MgrUser.initColumn();
//     console.log(defaultColunms);return;false;
//     var table = new BSTable("managerTable", "/mgr/list", defaultColunms);
//     table.setPaginationType("client");
//     MgrUser.table = table.init();
//     var ztree = new $ZTree("deptTree", "/dept/tree");
//     ztree.bindOnClick(MgrUser.onClickDept);
//     ztree.init();
// });

layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function()
{
    var laydate = layui.laydate;    // 日期
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    MgrUser.table = table;
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
        elem: '#managerTable'
        , height: 440
        , url: Feng.ctxPath + "/mgr/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            // {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '账号', field: 'account', align: 'center', valign: 'middle', sortable: true},
            {title: '姓名', field: 'name', align: 'center', valign: 'middle',width: 100},
            {title: '角色', field: 'roleName', align: 'center', valign: 'middle',width: 100},
            {title: '所属公司', field: 'deptName', align: 'center', valign: 'middle',width: 100},
            {title: '邮箱', field: 'email', align: 'center', valign: 'middle',width: 160},
            {title: '电话', field: 'phone', align: 'center', valign: 'middle',width: 110},
            // {title: '性别', field: 'sex', align: 'center', valign: 'middle', sortable: true,
            //     templet: function (data)
            //     {
            //         return data.sex == '2' ? '<span style="cursor: pointer">女</span>' : '<span style="cursor: pointer">男</span>';
            //     }},
            {title: '创建时间', field: 'createtime', align: 'center', valign: 'middle', sortable: true,width: 150},
            {title: '状态', field: 'status', align: 'center', valign: 'middle', sortable: true,
                templet: function (data)
                {
                    return data.status == '2' ? '<span style="color: #c00;cursor: pointer">禁用</span>' : '<span style="color:#1ab394;cursor: pointer">启用</span>';
                }
            },
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation', width: 320}

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
            var name = $('#custom_name');
            var beginTime = $('#beginTime');
            var endTime = $('#endTime');

            if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
                Feng.error("时间选择范围不正确!");
                return false;
            }
            // 执行重载
            table.reload('managerTable', {
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

    // 添加用户
    $("#create-user").click(function(){
        var index = layer.open({
            type: 2,
            title: '添加用户',
            area: ['50%', '85%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgr/user_add'
        });
        this.layerIndex = index;
        return false;
    });

    // 重置输入内容
    $("#searchReset").click(function(){
        $(this).parents('.layui-form').find('input').val('');
        return false;
    });

    // 监听工具条
    table.on('tool(demo)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'edit'){
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '修改用户',
                area: ['50%', '75%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/mgr/user_edit/' + id

            });
            this.layerIndex = index;

        } else if(layEvent === 'delete'){
            var operation = function(){
                var userId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/mgr/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('managerTable');
                });
                ajax.set("userId", userId);
                ajax.start();
            };

            Feng.confirm("是否删除用户" + data['name'] + "?",operation);

        } else if(layEvent === 'freezeAccount'){
            var operation = function() {
                var userId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/mgr/freeze", function (data) {
                    Feng.success("禁用成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("禁用失败!" + data.responseJSON.message + "!");
                });
                ajax.set("userId", userId);
                ajax.start();
            }
            Feng.confirm("是否禁用用户" + data['name'] + "?",operation);
        } else if(layEvent === 'unfreeze'){
            var operation = function() {
                var userId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/mgr/unfreeze", function (data) {
                    Feng.success("启用成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("启用失败!");
                });
                ajax.set("userId", userId);
                ajax.start();
            }
            Feng.confirm("是否启用" + data['name'] + "?",operation);
        } else if(layEvent === 'resetPwd'){
            var userId = data['id'];    // 获取到当前行的id值
            parent.layer.confirm('是否重置密码为111111？', {
                btn: ['确定', '取消'],

                shade: false //不显示遮罩
            }, function () {
                var ajax = new $ax(Feng.ctxPath + "/mgr/reset", function (data) {
                    Feng.success("重置密码成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("重置密码失败!");
                });
                ajax.set("userId", userId);
                ajax.start();
            });
        } else if(layEvent === 'setRole'){
            var userId = data['id'];    // 获取到当前行的id值
            var index = layer.open({
                type: 2,
                title: '分配角色',
                area: ['75%', '70%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/mgr/role_assign/' + userId
            });
            MgrUser.layerIndex = index;
        }

        return false;
    });

})


