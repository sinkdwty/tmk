/**
 * 公司管理初始化
 */
var Dept = {
    id: "DeptTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', align: 'center', valign: 'middle',width:'60px'},
        {title: '公司简称', field: 'simplename', align: 'center', valign: 'middle', sortable: true},
        {title: '公司全称', field: 'fullname', align: 'center', valign: 'middle', sortable: true},
        {title: '排序', field: 'num', align: 'center', valign: 'middle', sortable: true},
        {title: '备注', field: 'tips', align: 'center', valign: 'middle', sortable: true}];
};

/**
 * 检查是否选中
 */
Dept.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Dept.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加公司
 */
Dept.openAddDept = function () {
    var index = layer.open({
        type: 2,
        title: '添加公司',
        area: ['800px', '480px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/dept/dept_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看公司详情
 */
Dept.openDeptDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '公司详情',
            area: ['800px', '480px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/dept/dept_update/' + Dept.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除公司
 */
Dept.delete = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/dept/delete", function () {
                Feng.success("删除成功!");
                Dept.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("deptId",Dept.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除该公司?", operation);
    }
};

/**
 * 查询公司列表
 */
Dept.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Dept.table.refresh({query: queryData});
};

// $(function () {
//     var defaultColunms = Dept.initColumn();
//     var table = new BSTreeTable(Dept.id, "/dept/list", defaultColunms);
//     table.setExpandColumn(2);
//     table.setIdField("id");
//     table.setCodeField("id");
//     table.setParentCodeField("pid");
//     table.setExpandAll(true);
//     table.init();
//     Dept.table = table;
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
        elem: '#deptManagerTable'
        , height: 400
        , url: Feng.ctxPath + "/dept/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '公司简称', field: 'simplename', align: 'center', valign: 'middle', width: 150},
            {title: '公司全称', field: 'fullname', align: 'center', valign: 'middle', width: 200},
            {title: '排序', field: 'num', align: 'center', valign: 'middle', width: 60},
            {title: '备注', field: 'tips', align: 'center', valign: 'middle'},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation', width: 100}

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


            // 执行重载
            table.reload('deptManagerTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    condition: name.val(),
                }
            });
        },
    };
    $('.search-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    // 添加公司
    $("#create-dept").click(function(){
        var index = layer.open({
            type: 2,
            title: '添加公司',
            area: ['55%', '95%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/dept/dept_add'
        });
        this.layerIndex = index;
    });

    // 重置输入内容
    $("#searchReset").click(function(){
        $(this).parents('.layui-form').find('input').val('');
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
                title: '编辑公司',
                area: ['75%', '85%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/dept/dept_update/' + id

            });
            this.layerIndex = index;

        } else if(layEvent === 'delete'){
            var operation = function(){
                var deptId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/dept/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('deptManagerTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('deptManagerTable');
                });
                ajax.set("deptId",deptId);
                ajax.start();
            };

            Feng.confirm("是否刪除该公司?", operation);
        }
    });

    //查询条件显示隐藏功能
    // CommonApi.loadMore({
    //     mark: false,
    //     clickdom: '.load-more',
    //     dom: '.layui-form',
    //     queryWidth: 992
    // });

})