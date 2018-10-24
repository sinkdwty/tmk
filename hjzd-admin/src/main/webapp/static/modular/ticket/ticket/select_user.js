
layui.use(["table", "element", "layedit", "form"], function () {
    var table = layui.table;
    var element = layui.element;
    var layedit = layui.layedit;
    var form = layui.form;
    var $ = layui.$;

    table.render({
        elem: '#userTable',
        height: 440,
        url: Feng.ctxPath + "/ticket/user_search",    // 数据接口
        page: true,    // 开启分页
        size: 'sm',
        method:'post',
        cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '姓名', toolbar:  '#bar'},
        ]],
        where: {
            deptId: deptId,
            order: 'desc'
        },
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 50, 100, 200, 500],
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            var keyword = $('#keyword');
            // 执行重载
            table.reload('userTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                } ,
                where: {
                    keyword: keyword.val(),
                    deptId: deptId
                }
            });
        },
        reset:function () {
            // 重置输入内容
            $(this).parents('.layui-form').find('.resetInput input').val('');
            return false;
        }
    };

    $('.btn-search,.btn-reset').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });

    form.on('radio(select)', function(data){
        var assignUserName = $(data.elem).attr("title");
        var assignUserId = $(data.elem).val();

        window.parent.$("#assignUserId").val(assignUserId);
        window.parent.$("#assignUserName").val(assignUserName);

        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });
});