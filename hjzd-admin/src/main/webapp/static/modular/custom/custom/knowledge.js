layui.use(["table", "form",], function () {
    var table = layui.table;
    var $ = layui.$;

    $(".btn-search").on("click", function () {
        var keyword = $("#keyword").val();
        // 执行重载
        table.reload('knowledgeTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                keyword: keyword
            }
        });

    });

    table.render({
        elem: '#knowledgeTable',
        height: 440,
        url: Feng.ctxPath + "/custom/knowledge",
        page: true,
        size: 'sm',
        method:'post',
        cols: [[
            {title: '序号', type: 'numbers', align:'center', width: 50},
            {title: '操作', align:'center', toolbar:  '#bar', width: 70},
            {title: '知识名称', field: 'name', align: 'center', valign: 'middle',  width: 100},
            // {title: '知识内容', field: 'content', align: 'center', valign: 'middle', width: 300},
            {title: '创建时间', field: 'createdAt', align: 'center', valign: 'middle', width: 150},
        ]],
        where: {
            order: 'desc'
        },
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        }
    });

    // 监听工具条
    table.on('tool(knowledgeTable)', function(obj) {    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if (layEvent === 'view') {
            var id = data['id'];    // 获取到当前行的id值

            var index = layer.open({
                type: 1,
                title: '查看',
                area: ['90%', '90%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content:  "<div>知识名称: </div>" +
                "<div style='padding: 10px'>"+ data['name'] + "</div>" +
                "<div>知识内容:</div>" +
                "<div style='padding: 10px'>"+data['content']+"</div>" +
                "<div>关键字:</div>" +
                "<div style='padding: 10px'>"+ data['keyWord']+"</div>"
            });
            this.layerIndex = index;
        }
    });
});