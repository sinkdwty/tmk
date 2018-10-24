//记录选中的数据:做缓存使用,作为参数传递给后台,
var ids_arr = new Array();

var way = $('input[name="way"]').val();

if (way == 'sendBatchMessage') {     //查询分案
    var customIds = window.parent.ids_arr;
    $("#selectedNum").val(customIds.length);
} else if (way == 'searchBatchMessage') {    //快速分案
    var table_search_count = window.parent.table_search_count;
    $("#selectedNum").val(table_search_count);
}

layui.use(['form', 'table'], function ()
{
    var table = layui.table;
    var form = layui.form;
    //执行一个 table 实例

        column = [[ //表头
            {
                title: '', width: 70,templet: '#radioTpl',visible: true,valign: 'middle', align: 'center'
            },
            {title: '批次名称', field: 'sms_batch_name', visible: true, align: 'center', valign: 'middle',width:140},
            {title: '短信内容', field: 'sms_content', visible: true, align: 'center', valign: 'middle'},
            {title: '批次剩余容量', field: 'left_capacity', visible: true, align: 'center', valign: 'middle',width:115},
            {title: '是否预约', field: 'reserve_time', visible: true, align: 'center', valign: 'middle',width:160},
        ]];

    table.render({
        elem: '#userTable',
        height: 'full-150',
        url: Feng.ctxPath + "/custom/message-batch-list", //数据接口
        page: true, //开启分页
        cols: column,
        limits: [10, 50, 100, 200, 500, 1000],
        request: {
            pageName: 'page', //页码的参数名称，默认：page
            limitName: 'limit' //每页数据量的参数名，默认：limit
        },
    });

    //表格头部按钮操作
    var $ = layui.$, active = {
        search: function() {
            table.reload('userTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    name : $.trim($('#batchName').val())
                },
            });
        },
    };


    $('.search-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    $('.ensure-assign-custom').click(function () {

        var radioCheked = $('input[type="radio"]:checked').val();

        if (radioCheked  == undefined) {
            layer.msg('请选择短信批次',{icon: 5});
        } else {
            var ti = layer.confirm('确定分配用户到短信批次吗？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
                parent.sendMassage(radioCheked,way);
            }, function(){
                layer.close(ti);
                return false;
            });

        }

    });

});