/**
 * 外呼管理管理初始化
 */
var CallConfig = {
    id: "CallConfigTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};


/**
 * 点击添加外呼管理
 */
CallConfig.openAddCallConfig = function () {
    var index = layer.open({
        type: 2,
        title: '添加外呼管理',
        area: ['1000px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/callConfig/callConfig_add'
    });
    CallConfig.layerIndex = index;
};

layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function()
{
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    CallConfig.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#callConfigTable'
        , height: 440
        , url: Feng.ctxPath + "/callConfig/list"    // 数据接口
        , page: false    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 50},
            {title: '公司名称', field: 'base_name', align: 'center',  },
            {title: '外呼系统名称', field: 'callSystemName', align: 'center',  },
            {title: '配置内容', field: 'config', align: 'center',  },
            {title: '最后修改时间', field: 'updated_at', align: 'center', sort: true},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation'}

        ]],
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [1,10, 20, 50, 100, 200, 500],
    });

    // 监听工具条
    table.on('tool(call-config)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'edit'){
            var id = data.id;    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '编辑',
                area: ['1000px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/callConfig/callConfig_update/' + id
            });
            CallConfig.layerIndex = index;

        } else if(layEvent === 'delete'){
            var operation = function(){
                var id = data.id;    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/callConfig/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('callConfigTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('callConfigTable');
                });
                ajax.set("id", id);
                ajax.start();
            };

            Feng.confirm("是否删除该条数据?",operation);

        }
        return false;
    });

})
