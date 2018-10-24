/**
 * 角色管理的单例
 */
var Role = {
    id: "roleTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
// Role.initColumn = function () {
//     var columns = [
//         {field: 'selectItem', radio: true},
//         {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
//         {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
//         {title: '上级角色', field: 'pName', align: 'center', valign: 'middle', sortable: true},
//         {title: '所在部门', field: 'deptName', align: 'center', valign: 'middle', sortable: true},
//         {title: '别名', field: 'tips', align: 'center', valign: 'middle', sortable: true}]
//     return columns;
// };


/**
 * 检查是否选中
 */
Role.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Role.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加管理员
 */
Role.openAddRole = function () {
    var index = layer.open({
        type: 2,
        title: '添加角色',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/role/role_add'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 */
Role.openChangeRole = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改角色',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/role/role_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除角色
 */
Role.delRole = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/role/remove", function () {
                Feng.success("删除成功!");
                Role.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("roleId", Role.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否删除角色 " + Role.seItem.name + "?",operation);
    }
};

/**
 * 权限配置
 */
Role.assign = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '权限配置',
            area: ['300px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/role/role_assign/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 搜索角色
 */
Role.search = function () {
    var queryData = {};
    queryData['roleName'] = $("#roleName").val();
    Role.table.refresh({query: queryData});
}

// $(function () {
//     var defaultColunms = Role.initColumn();
//     var table = new BSTable(Role.id, "/role/list", defaultColunms);
//     table.setPaginationType("client");
//     table.init();
//     Role.table = table;
// });

layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function()
{
    var laydate = layui.laydate;    // 日期
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格

    // 执行一个 table 实例
    table.render({
        elem: '#roleTable'
        , height: 440
        , url: Feng.ctxPath + "/role/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        // ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '角色名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
            {title: '上级角色', field: 'pName', align: 'center', valign: 'middle', sortable: true},
            {title: '部门名称', field: 'deptName', align: 'center', valign: 'middle', sortable: true},
            {title: '别名', field: 'tips', align: 'center', valign: 'middle', sortable: true},
            {title: '序号', field: 'num', align: 'center', valign: 'middle', sortable: true},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation', width: 260}
        ]],
        // initSort: {
        //     field: 'createtime' //排序字段，对应 cols 设定的各字段名
        //     ,type: 'desc' //排序方式 asc: 升序、desc: 降序、null: 默认排序
        // },

        request: {
            pageName: 'page',     // 页码的参数名称，默认：page
            limitName: 'limit',    // 每页数据量的参数名，默认：limit
        },
        where: {order: 'desc'},    // 定义一个默认排序方式，与java后台对接

        limits: [10, 20, 50, 100, 200, 500],
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            var name = $('#role_name');
            // 执行重载
            table.reload('roleTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    roleName: name.val(),
                }
            });
        },
    };

    $('.search-btn').on('click', function ()
    {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    // 重置输入内容
    $("#searchReset").click( function ()
    {
        $(this).parents('.layui-form').find('input').val('');
    });

    // 添加角色
    $("#create-role").click(function ()
    {
        var index = layer.open({
            type: 2,
            title: '添加角色',
            area: ['75%', '80%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/role/role_add'
        });
        this.layerIndex = index;
    });

    // 监听工具条
    table.on('tool(demo)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'edit'){
            var seItem = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '修改角色',
                area: ['75%', '80%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/role/role_edit/' + seItem
            });
            this.layerIndex = index;

        } else if(layEvent === 'delete') {
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/role/remove", function () {
                    Feng.success("删除成功!");
                    table.reload('roleTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                });
                ajax.set("roleId", data.id);
                ajax.start();
            };

            Feng.confirm("是否删除角色 " + data.name + "?",operation);

        } else if(layEvent === 'assign'){
            var seItem = data['id'];    // 获取到当前行的id值
            var index = layer.open({
                type: 2,
                title: '权限配置',
                area: ['75%', '70%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/role/role_assign/' + seItem
            });
            this.layerIndex = index;
        }
    });
})
