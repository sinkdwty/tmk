/**
 * 字典管理初始化
 */
var Dict = {
    id: "managerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function () {
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    Dict.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#managerTable'
        , height: 440
        , url: Feng.ctxPath + "/dict/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        , method: 'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align: 'center', width: 60},
            {title: '名称', field: 'name', align: 'center', valign: 'middle'},
            {title: '详情', field: 'detail', align: 'center', valign: 'middle'},
            {title: '备注', field: 'tips', align: 'center', valign: 'middle'},
            {fixed: 'right', title: '操作', align: 'center', toolbar: '#operation'}
        ]],
        where: {order: 'desc'},    // 定义一个默认排序方式，与java后台对接
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 20, 50, 100, 200, 500],
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function () {
            // 执行重载
            table.reload('managerTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    condition: $("#condition").val()
                }
            });
        }, reset: function () {
            // 重置输入内容
            $('#condition').val('');
            return false;

        }, add: function () {
            var index = layer.open({
                type: 2,
                title: '添加字典',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/dict/dict_add'
            });
            Dict.layerIndex = index;
        }
    };
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });


    // 监听工具条
    table.on('tool(dict)', function (obj) {    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值
        var id = data['id'];    // 获取到当前行的id值

        if (layEvent === 'edit') {
            var index = layer.open({
                type: 2,
                title: '编辑字典',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/dict/dict_edit/' + id
            });
            Dict.layerIndex = index;
        } else if (layEvent === 'delete') {
            var operation = function(){
                var ajax = new $ax(Feng.ctxPath + "/dict/delete", function (data) {
                    Feng.success("删除成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('managerTable');
                });
                ajax.set("dictId", id);
                ajax.start();
            };

            Feng.confirm("是否删除" + data['name'] + "?", operation);
        }
        return false;
    });

});
