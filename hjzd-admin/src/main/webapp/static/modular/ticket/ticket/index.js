
layui.use(["table", "element", "layedit", "form", "laydate"], function () {
    var table = layui.table;
    var laydate = layui.laydate;
    var element = layui.element;
    var layedit = layui.layedit;
    var form = layui.form
    var $ = layui.$;

    var statusTpl = function (status) {
        // 1-开启 2-解决中 3-已解决 4-已关闭
        var html = '';

        if (status == 1) {
            html = '<span style="color: #c00;cursor: pointer">开启</span>';
        } else if (status == 2) {
            html = '<span style="color: #00B83F;cursor: pointer">解决中</span>';
        } else if (status == 3) {
            html = '<span style="color: #00b7ee;cursor: pointer">已解决</span>';
        } else if (status == 4) {
            html = '<span style="color: #ccc;cursor: pointer">已关闭</span>';
        } else {
            html = '<span style="color: #c00;cursor: pointer">未知</span>';
        }

        return html;
    };

    var levelsTpl = function (levels) {
        // 优先级：1-紧急 2-高 3-标准 4-低
        var html = '';

        if (levels == 1) {
            html = '<span style="color: #c00;cursor: pointer">紧急</span>';
        } else if (levels == 2) {
            html = '<span style="color: #00B83F;cursor: pointer">高</span>';
        } else if (levels == 3) {
            html = '<span style="color: #00b7ee;cursor: pointer">标准</span>';
        } else if (levels == 4) {
            html = '<span style="color: #ccc;cursor: pointer">低</span>';
        } else {
            html = '<span style="color: #c00;cursor: pointer">未知</span>';
        }

        return html;
    };

    table.render({
        elem: '#ticketTable',
        height: 420,
        url: Feng.ctxPath + "/ticket/list",    // 数据接口
        page: true,    // 开启分页
        size: 'sm',
        method:'post',
        cols: [[    // 表头
            {type:'checkbox'},
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '主题', field: 'title', align: 'center', valign: 'middle'},
            {title: '状态', field: 'status', align: 'center', valign: 'middle', sortable: true,width: 70,
                templet: function (data)
                {
                    return statusTpl( data.status);
                }
            },
            {title: '优先级', field: 'levels', align: 'center', valign: 'middle',width: 100,
                templet: function (data)
                {
                    return levelsTpl(data.levels);
                }
            },
            {title: '渠道', field: 'ticketFrom', align: 'center', valign: 'middle',width: 100},
            {title: '客户', field: 'customName', align: 'center', valign: 'middle',width: 70},
            {title: '受理公司', field: 'assignDeptName', align: 'center', valign: 'middle',width: 160},
            {title: '受理人', field: 'assignUserName', align: 'center', valign: 'middle',width: 110},
            {title: '创建时间', field: 'createdAt', align: 'center', valign: 'middle', sortable: true,width: 150},
            {title: '更新时间', field: 'updatedAt', align: 'center', valign: 'middle', sortable: true,width: 150},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#bar', width: 150}
        ]],
        where: {
            order: 'desc'
        },
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 50, 100, 200, 500],
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
            var title = $('#title');
            var beginTime = $('#beginTime');
            var endTime = $('#endTime');

            if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
                Feng.error("创建时间选择范围不正确!");
                return false;
            }

            // 执行重载
            table.reload('ticketTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                } ,
                where: {
                    column: $("#search_column option:selected").val(),
                    title: title.val(),
                    beginTime: beginTime.val(),
                    endTime: endTime.val(),
                }
            });
        },reset:function () {
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
    // // 重置输入内容
    // $("#searchReset").click(function(){
    //     $(this).parents('.layui-form').find('input').val('');
    //     return false;
    // });
    //监听Tab切换，以改变地址hash值
    element.on('tab(ticketTab)', function(){
       var type = $(this).data('type');
        $(".btn-reset").parents('.layui-form').find('.resetInput input').val('');
        var title = $('#title');
        var beginTime = $('#beginTime');
        var endTime = $('#endTime');

        // 执行重载
        table.reload('ticketTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            } ,
            where: {
                type: type,
                column: $("#search_column option:selected").val(),
                title: title.val(),
                beginTime: beginTime.val(),
                endTime: endTime.val(),
            }
        });
    });
    // 监听工具条
    table.on('tool(ticketTable)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'edit'){
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '编辑工单',
                area: ['100%', '100%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/ticket/ticket_edit?id=' + id

            });
            this.layerIndex = index;

        } else if(layEvent === 'view'){
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '查看工单',
                area: ['100%', '100%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/ticket/ticket_view?id=' + id

            });
            this.layerIndex = index;
        } else if(layEvent === 'delete'){
            var operation = function(){
                var id = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/ticket/ticket_delete", function (res) {
                    if (res.code == 200) {
                        Feng.success("删除成功!");
                    } else {
                        Feng.error("删除失败!");
                    }
                    table.reload('ticketTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('ticketTable');
                });
                ajax.set("id", id);
                ajax.start();
            };

            Feng.confirm("是否删除工单：" + data['title'] + "？", operation);

        }

        return false;
    });
    // 添加工单
    $("#create").click(function(){
        var index = layer.open({
            type: 2,
            title: '添加工单',
            area: ['100%', '100%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/ticket/ticket_add'
        });
        this.layerIndex = index;
        return false;
    });

    // 导入工单
    $("#import").click(function(){
        var index = layer.open({
            type: 2,
            title: '导入工单',
            area: ['60%', '65%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/ticket/ticket_import'
        });
        this.layerIndex = index;
        return false;
    });
});