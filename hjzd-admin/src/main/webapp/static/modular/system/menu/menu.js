/**
 * 角色管理的单例
 */
var Menu = {
    id: "menuTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};


layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate','treeGrid'], function()
{

    var laydate = layui.laydate;    // 日期
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    var treeGrid = layui.treeGrid; //很重要

    // 执行一个 table 实例
    var treeTable = treeGrid.render({
        elem: '#menuTable'
        ,treeId:'code'//树形id字段名称
        ,treeUpId:'pcode'//树形父id字段名称
        ,treeShowName:'name'//以树形式显示的字段
        , url: Feng.ctxPath + "/menu/list"    // 数据接口
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60,sortable: true},
            {title: '菜单名称', field: 'name', align: 'center'},
            {title: '菜单编号', field: 'code', align: 'center'},
            {title: '菜单父编号', field: 'pcode', align: 'center'},
            {title: '请求地址', field: 'url', align: 'center'},
            {title: '排序', field: 'num', align: 'center',sortable: true,width:70},
            {title: '层级', field: 'levels', align: 'center',sortable: true, width:70},
            {title: '是否是菜单', field: 'isMenuName', align: 'center', width:100},
            // {title: '状态', field: 'statusName', align: 'center', sortable: true,},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation', width: 120}
        ]]
        ,page:false,
        // initSort: {
        //     field: 'createtime' //排序字段，对应 cols 设定的各字段名
        //     ,type: 'desc' //排序方式 asc: 升序、desc: 降序、null: 默认排序
        // },
        where: {order: 'desc'},    // 定义一个默认排序方式，与java后台对接
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            var name = $('#menuName');
            var levels = $('#menuLevels');
            var search_column = $('#search_column');

            // 执行重载
            treeGrid.reload('menuTable', {
                // page: {
                //     curr: 1 //重新从第 1 页开始
                // },
                where: {
                    menuName: name.val(),
                    level: levels.val(),
                    column: search_column.val(),
                }
            });
        },
    };
    $('.search-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    /* 监听工具条 */
    treeGrid.on('tool(demo)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;    // 获取当前行的数据
        var layevent = obj.event;    // 获取lay-event对应的事假名

        if (layevent === 'edit') {    // 判断如果是编辑（修改），则进行如下操作
            var id = data['id'];
            var index = layer.open({
                type : 2,
                title : '修改菜单',
                area : ['70%', '75%'],
                fix : false,
                maxmin : true,
                content : Feng.ctxPath + '/menu/menu_edit/' + id,
            });
            this.layerIndex = index
        } else if (layevent === 'delete') {    // 判断说如果是删除，则进行如下操作
            var id = data['id'];
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + '/menu/remove', function (data) {
                    Feng.success('删除成功！');
                    window.location.reload()
                }, function (data) {
                    Feng.error('删除失败！' + data.responseJson.message + '!');
                })
                ajax.set('menuId', id);
                ajax.start();
            };
            Feng.confirm('是否删除该菜单？' ,operation);
        }
    });
});

// 重置输入内容
$("#searchReset").click(function(){
    $(this).parents('.layui-form').find('input').val('');
});

/* 点击添加 */
$("#create-menu").click(function () {
    var index= layer.open({
        type : 2,
        title : '添加菜单',
        area : ['70%', '75%'],
        fix : false,
        maxmin : true,
        content : Feng.ctxPath + '/menu/menu_add' ,
    });
    this.layerIndex = index;
});

/**
 * 检查是否选中
 */
Menu.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Menu.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加菜单
 */
Menu.openAddMenu = function () {
    var index = layer.open({
        type: 2,
        title: '添加菜单',
        area: ['830px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/menu/menu_add'
    });
    this.layerIndex = index;
};

/**
 * 点击修改
 */
Menu.openChangeMenu = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改菜单',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/menu/menu_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
Menu.delMenu = function () {
    if (this.check()) {

        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/menu/remove", function (data) {
                Feng.success("删除成功!");
                Menu.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("menuId", Menu.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除该菜单?", operation);
    }
};

/**
 * 搜索
 */
// Menu.search = function () {
//     var queryData = {};
//
//     queryData['menuName'] = $("#menuName").val();
//     queryData['level'] = $("#level").val();
//
//     Menu.table.refresh({query: queryData});
// }

// $(function () {
//     var defaultColunms = Menu.initColumn();
//     var table = new BSTreeTable(Menu.id, "/menu/list", defaultColunms);
//     table.setExpandColumn(2);
//     table.setIdField("id");
//     table.setCodeField("code");
//     table.setParentCodeField("pcode");
//     table.setExpandAll(true);
//     table.init();
//     Menu.table = table;
// });
