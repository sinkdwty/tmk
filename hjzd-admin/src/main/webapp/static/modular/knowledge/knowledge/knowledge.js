/**
 * 知识库管理管理初始化
 */
var Knowledge = {
    id: "managerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function () {
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    Knowledge.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#managerTable'
        , height: 440
        , url: Feng.ctxPath + "/knowledge/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        , method: 'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align: 'center', width: 60},
            {title: '知识名称', field: 'name', align: 'center', valign: 'middle'},
            {title: '关键字', field: 'key_word', align: 'center', valign: 'middle'},
            {title: '知识分类', field: 'knowledgeCategoryName', align: 'center', valign: 'middle'},
            {
                title: '知识状态', field: 'status', align: 'center', valign: 'middle',
                templet: function (value) {
                    if (value.status == true) {
                        return "<span style=\"color:#1ab394;cursor: pointer\">已发布</span>";
                    } else {
                        return "<span style=\"color:#c00;cursor: pointer\">未发布</span>";
                    }
                }
            },
            {title: '最后编辑时间', field: 'updated_at', align: 'center', valign: 'middle'},
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
                    key_word: $("#key_word").val(),
                    category: $("#category option:selected").val(),
                    column: $("#search_column option:selected").val()
                }
            });
        }, reset: function () {
            // 重置输入内容
            $(this).parents('.layui-form').find('.resetInput input').val('');
            return false;

        }, add: function () {
            var index = layer.open({
                type: 2,
                title: '添加知识',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/knowledge/knowledge_add',
                success: function (layero, index) {
                    //在回调方法中的第2个参数“index”表示的是当前弹窗的索引。
                    //通过layer.full方法将窗口放大。
                    layer.full(index);
                }
            });
            Knowledge.layerIndex = index;
        }
    };
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });

    var birtwin = null;
    var birturl = null;
    var birttitle = null;
    // 监听工具条
    table.on('tool(knowledge)', function (obj) {    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if (layEvent === 'edit') {
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '编辑知识',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/knowledge/knowledge_update?id=' + id + '&option=update',
            });
            Knowledge.layerIndex = index;
            layer.full(index);
        } else if (layEvent === 'displayPDF') {
            var id = data['id'];    // 获取到当前行的id值
            var url = "/static/js/plugins/pdf/generic/web/viewer.html?file=/knowledge/displayPDF/" + id;
            // window.open(url);
            birturl = url;
            birttitle = data['name'];
            birtwin = window.open('');
            checkbirt();//start checking
            /*var index = layer.open({
                type: 2,
                title: '知识知识',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: url,
            });
            Knowledge.layerIndex = index;
            layer.full(index);*/
        } else if (layEvent === 'delete') {
            var operation = function () {
                var id = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/knowledge/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('managerTable');
                });
                ajax.set("knowledgeId", id);
                ajax.start();
            };

            Feng.confirm("是否删除" + data['name'] + "?", operation);
        } else if (layEvent === 'detail') {
            var id = data['id'];    // 获取到当前行的id值
            var index = layer.open({
                type: 2,
                title: '知识详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/knowledge/knowledge_update?id=' + id + '&option=detail',
            });
            Knowledge.layerIndex = index;
            layer.full(index);
        }
        return false;
    });

    function checkbirt() {
        if (birtwin.document) {
            birtwin.document.write('<html><head><title>' + birttitle + '</title></head><body height="100%" width="100%"><iframe src="' + birturl + '" height="100%" width="100%"></iframe></body></html>');
        } else {
            //if not loaded yet
            setTimeout(checkbirt, 10);//check in another 10ms
        }
    }
});

