/**
 * 数据批次管理初始化
 */
var Batch = {
    id: "batchTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
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
        elem: '#batchTable'
        , height: 440
        , url: Feng.ctxPath + "/batch/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '项目名称', field: 'productName', align: 'center'},
            {title: '批次编号', field: 'batch_no', align: 'center'},
            {title: '上传用户', field: 'userName', align: 'center'},
            {title: '总条数', field: 'import_num', align: 'center', width: 200},
            {title: '创建时间', field: 'created_at', align: 'center', width: 150},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation',  width: 150}
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
        reload: function () {
            var batch_no = $('#batch_no');
            var beginTime = $('#beginTime');
            var endTime = $('#endTime');

            if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
                Feng.error("创建时间选择范围不正确!");
                return false;
            }

            // 执行重载
            table.reload('batchTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    batch_no: batch_no.val(),
                    beginTime: beginTime.val(),
                    endTime: endTime.val(),
                }
            });
        }, reset: function () {
            // 重置输入内容
            $(this).parents('.layui-form').find('input').val('');
            return false;

        }
    };
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });


    // 监听工具条
    table.on('tool(batch)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'detail'){
            var batch_no = data['batch_no'];    // 获取到当前行的批次编号
            /* 向服务器发送查看指令 */
            /*var name = "客户管理"; // tab 名称
            var url = Feng.ctxPath + '/custom/list?page=1&limit=10&column=batch_no&condition='+batch_no;
            viewDetail(name,url);*/
        } else if(layEvent === 'download'){
            if(!data.file_path) {
                layer.msg('该批次无上传文件！');
                return false;
            }
            layer.confirm('确定下载文件吗', function(index) {
                window.location.href = Feng.ctxPath + '/upload/download?fileName='+data.file_path;
                layer.closeAll();
            });
        } else if(layEvent === 'export-custom'){
            var index = layer.open({
                type: 2,        //iframe层
                title: '客户导入',
                area: ['80%', '65%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/custom/custom_upload?batch_no='+data.batch_no
            });
            Batch.layerIndex = index;
            return false;
        }

        return false;
    });

})


