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
        elem: '#productTable'
        , height: 420
        , url: Feng.ctxPath + "/product/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '项目名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
            {title: '公司', field: 'deptName', align: 'center', valign: 'middle', sortable: true, width: 200},
            {title: '创建时间', field: 'createAt', align: 'center', valign: 'middle', sort: true, width: 150},
            {title: '状态', field: 'status', align: 'center', valign: 'middle', sortable: true, width: 100,
                templet: function (data)
                {
                    return data.status == '0' ? '<span style="color: #c00;cursor: pointer">禁用</span>' : '<span style="color:#1ab394;cursor: pointer">启用</span>';
                }
            },
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation',  width: 200}

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
            table.reload('productTable', {
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
    $("#create-product").click(function(){
        var index = layer.open({
            type: 2,
            title: '添加项目',
            area: ['40%', '55%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/product/product_add'
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
    table.on('tool(product)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'edit'){
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '更新项目',
                area: ['40%', '55%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/product/product_edit/' + id

            });
            this.layerIndex = index;

        } else if(layEvent === 'delete'){
            var operation = function(){
                var productId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/product/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('productTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('productTable');
                });
                ajax.set("productId", productId);
                ajax.start();
            };

            Feng.confirm("是否删除项目" + data['name'] + "?",operation);

        } else if(layEvent === 'freezeAccount'){
            var operation = function () {
                var productId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/product/freeze", function (data) {
                    Feng.success("禁用成功!");
                    table.reload('productTable');
                }, function (data) {
                    Feng.error("禁用失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", productId);
                ajax.start();
            }
            Feng.confirm("是否禁用该项目?",operation);
        } else if(layEvent === 'unfreeze'){
            var operation = function () {
                var productId = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/product/unfreeze", function (data) {
                    Feng.success("解除禁用成功!");
                    table.reload('productTable');
                }, function (data) {
                    Feng.error("解除禁用失败!");
                });
                ajax.set("id", productId);
                ajax.start();
            }
            Feng.confirm("是否启用该项目?",operation);
        } else if(layEvent === 'callCode'){
            var productId = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var url =  Feng.ctxPath + '/product/set-code?productId=' + productId;
            layer.open({
                type:2,
                content: url,
                area:["30%","80%"]
            })
        }

        return false;
    });

})


