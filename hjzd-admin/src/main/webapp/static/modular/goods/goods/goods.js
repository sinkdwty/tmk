/**
 * 商品管理管理初始化
 */
var Goods = {
    id: "managerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    condition:$("#condition").val().trim()
};
layui.use(['laydate', 'laypage', 'layer', 'table', 'laydate'], function()
{
    var laypage = layui.laypage;    // 分页
    var layer = layui.layer;    // 弹层
    var table = layui.table;    // 表格
    Goods.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#managerTable'
        , height: 440
        , url: Feng.ctxPath + "/goods/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center',width:60},
            {title: '商品名称', field: 'goodsName', align: 'center', valign: 'middle',width:110},
            {title: '商品价格', field: 'goodsPrice', align: 'center', valign: 'middle',width:310},
            {title: '商品图片', field: 'picUrl',align: 'center', valign: 'middle',width:80,
                templet: function (value){ // 单元格格式化函数
                    var img = '';
                    if (value.picUrl === "") {
                        img = "";
                    } else if (value.picUrl == undefined) {
                        img = "";
                    } else {
                        img = "<img src='/kaptcha/"+ value.picUrl +"' width='80px' height='40px'/>";
                    }
                    return img;
                }
            },
            {title: '商品链接', field: 'url', align: 'center', valign: 'middle',width:130,
                templet: function (value){ // 单元格格式化函数
                    var text = '';
                    if (value.url == "") {
                        text = "";
                    } else {
                        text = '<a href="'+ value.url +'" target="_blank" style="color: #0d8ddb">'+ value.url +'</a>';
                    }
                    return text;
                }},
            // {title: '商品分类', field: 'categoryNo', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'note', align: 'center', valign: 'middle',width:250},
            {title: '商品备注', field: 'goodsNote',align: 'center', valign: 'middle',width:250},
            {title: '上下架（状态）', field: 'isSale',align: 'center', valign: 'middle',width:119,
                templet: function (value){ // 单元格格式化函数
                    var text = '';
                    if (value.isSale == "1") {
                        text = "<span style=\"color:#1ab394;cursor: pointer\">上架</span>";
                    } else {
                        text = "<span style=\"color: #c00;cursor: pointer\">下架</span>";
                    }
                    return text;
                }
            },
            {title: '创建时间', field: 'createdAt', align: 'center', valign: 'middle',sort: true,width:160},
            {fixed: 'right', title: '操作', align:'center', toolbar:  '#operation',width:180}
        ]],
        where: {
            order: 'desc',condition: Goods.condition
        },    // 定义一个默认排序方式，与java后台对接
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 20, 50, 100, 200, 500],
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            // 执行重载
            table.reload('managerTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    condition: $("#condition").val(),
                    isSale: $("#isSale").val(),
                }
            });
        },
    };
    $('.btn-search').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });

    // 重置输入内容
    $("#searchReset").click(function(){
        $('input').val("");
        $('select').val("3");
        return false;
    });

    // 监听工具条
    table.on('tool(goods)', function(obj){    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data ;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值

        if(layEvent === 'edit'){
            var id = data['id'];    // 获取到当前行的id值
            /* 向服务器发送查看指令 */
            var index = layer.open({
                type: 2,
                title: '商品管理详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/goods/goods_update/' + id
            });
            Goods.layerIndex = index;
        } else if(layEvent === 'delete'){
            var operation = function(){
                var id = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/goods/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('managerTable');
                });
                ajax.set("id", id);
                ajax.start();
            };

            Feng.confirm("是否删除" + data['goodsName'] + "?",operation);
        } else if(layEvent === 'updateIsSale') {
            var operation = function(){
                var id = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/goods/update-isSale", function () {
                    Feng.success("操作成功!");
                    table.reload('managerTable');
                }, function (data) {
                    Feng.error("操作失败!" + data.responseJSON.message + "!");
                    table.reload('managerTable');
                });
                ajax.set("id", id);
                ajax.start();
            };

            Feng.confirm("是否执行该操作?",operation);
        }
        return false;
    });

})

/**
 * 点击添加商品管理
 */
Goods.openAddGoods = function () {
    var index = layer.open({
        type: 2,        //iframe层
        title: '添加商品',
        area: ['800px','480px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/goods/goods_add'
    });
    this.layerIndex = index;
    return false;
};

Goods.uploadBox = function() {
    var index = layer.open({
        type: 2,        //iframe层
        title: '商品导入',
        area: ['800px','480px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/goods/goods_upload'
    });
    this.layerIndex = index;
    return false;
};

//
// /**
//  * 初始化表格的列
//  */
// Goods.initColumn = function () {
//     return [
//         {field: 'selectItem', radio: true, visible: false},
//         {title: '商品序号', field: 'id', visible: true, align: 'center', valign: 'middle'},
//         {title: '商品名称', field: 'goodsName', visible: true, align: 'center', valign: 'middle'},
//         {title: '商品价格', field: 'goodsPrice', visible: true, align: 'center', valign: 'middle'},
//         {title: '商品图片', field: 'picUrl', visible: true, align: 'center', valign: 'middle',
//             formatter: function (value, row, index){ // 单元格格式化函数
//                 var img = '';
//                 if (value == "") {
//                     img = "<img src='' width='80px' height='40px'/>";
//                 } else {
//                     img = "<img src='/kaptcha/"+ value +"' width='80px' height='40px'/>";
//                 }
//                 return img;
//             }
//         },
//         {title: '商品详情地址', field: 'url', visible: true, align: 'center', valign: 'middle',
//             formatter: function (value, row, index){ // 单元格格式化函数
//                 var text = '';
//                 if (value == "") {
//                     text = "";
//                 } else {
//                     text = '<a href="'+ value +'" target="_blank" style="color: #0d8ddb">'+ value +'</a>';
//                 }
//                 return text;
//             }},
//         // {title: '商品分类', field: 'categoryNo', visible: true, align: 'center', valign: 'middle'},
//         {title: '备注', field: 'note', visible: true, align: 'center', valign: 'middle',width:250},
//         {title: '商品备注', field: 'goodsNote', visible: true, align: 'center', valign: 'middle',width:250},
//         {title: '上下架（状态）', field: 'isSale', visible: true, align: 'center', valign: 'middle',
//             formatter: function (value, row, index){ // 单元格格式化函数
//                 var text = '';
//                 if (value == "1") {
//                     text = "<span style=\"color:#1ab394;cursor: pointer\">上架</span>";
//                 } else {
//                     text = "<span style=\"color: #c00;cursor: pointer\">下架</span>";
//                 }
//                 return text;
//             }
//         },
//         {title: '创建时间', field: 'createdAt', visible: true, align: 'center', valign: 'middle',sortable: true},
//         {title: '操作',field: 'operate',align: 'center',events: "operateEvents",formatter: operateFormatter,width:180}
//     ];
// };
//
// function operateFormatter(value, row, index) {
//     // console.info(row);
//     var isSale_btn = '';
//     if (row.isSale == 1) {
//         isSale_btn = '<button type="button" class="update-isSale btn btn-warning  btn-sm" style="margin-right:5px;">下架</button>';
//     } else {
//         isSale_btn = '<button type="button" class="update-isSale btn btn-success  btn-sm" style="margin-right:5px;">上架</button>';
//     }
//     return [
//         '<button type="button" class="RoleOfB btn btn-primary  btn-sm" style="margin-right:5px;">修改</button>',
//         '<button type="button" class="delete-goods btn btn-primary btn-danger  btn-sm" style="margin-right:5px;">删除</button>',
//         isSale_btn,
//     ].join('');
// }
//
// window.operateEvents = {
//     'click .RoleOfB': function (e, value, row, index) {
//         Goods.openGoodsDetail(row);
//     },
//     'click .delete-goods': function (e, value, row, index) {
//         var id = row.id;
//         if (id != '') {
//             layer.confirm("确定要删除该数据？", {
//                 btn: ['确定', '取消']
//             }, function (index) {
//                 Goods.delete(id);
//                 layer.close(index);
//             }, function (index) {
//                 layer.close(index);
//             });
//         }
//     },
//     'click .update-isSale': function (e, value, row, index) {
//         var id = row.id;
//         if (id != '') {
//             layer.confirm("确定要执行该操作吗？", {
//                 btn: ['确定', '取消']
//             }, function (index) {
//                 Goods.updateIsSale(id);
//                 layer.close(index);
//             }, function (index) {
//                 layer.close(index);
//             });
//         }
//     },
// };
//
// /**
//  * 检查是否选中
//  */
// Goods.check = function () {
//     var selected = $('#' + this.id).bootstrapTable('getSelections');
//     if(selected.length == 0){
//         Feng.info("请先选中表格中的某一记录！");
//         return false;
//     }else{
//         Goods.seItem = selected[0];
//         return true;
//     }
// };
//

//
// /**
//  * 打开查看商品管理详情
//  */
// Goods.openGoodsDetail = function (row) {
//     // if (this.check()) {
//     var id = row.id;
//         var index = layer.open({
//             type: 2,
//             title: '商品管理详情',
//             area: ['800px', '420px'], //宽高
//             fix: false, //不固定
//             maxmin: true,
//             content: Feng.ctxPath + '/goods/goods_update/' + id
//         });
//         this.layerIndex = index;
//     // }
// };
//
// /**
//  * 删除商品管理
//  */
// Goods.delete = function (id) {
//         var ajax = new $ax(Feng.ctxPath + "/goods/delete", function (data) {
//             Feng.success("删除成功!");
//             Goods.table.refresh();
//         }, function (data) {
//             Feng.error("删除失败!" + data.responseJSON.message + "!");
//         });
//         ajax.set("id", id);
//         ajax.start();
// };
//
// /**
//  * 点击更改上下架状态
//  */
// Goods.updateIsSale = function (id) {
//     var ajax = new $ax(Feng.ctxPath + "/goods/update-isSale", function (data) {
//         Feng.success("操作成功!");
//         Goods.table.refresh();
//     }, function (data) {
//         Feng.error("操作失败!" + data.responseJSON.message + "!");
//     });
//     ajax.set("id", id);
//     ajax.start();
// }

//
//
// /**
//  * 查询商品管理列表
//  */
// Goods.search = function () {
//     var queryData = {};
//     queryData['condition'] = $("#condition").val();
//     queryData['isSale'] = $("#isSale").val();
//     Goods.table.refresh({query: queryData});
// };
// /**
//  * 搜索条件重置
//  */
// Goods.reset = function () {
//     $('input').val("");
//     $('select').val("3");
// }
//
// $(function () {
//     var defaultColunms = Goods.initColumn();
//     var table = new BSTable(Goods.id, "/goods/list", defaultColunms);
//     table.setPaginationType("server");
//     Goods.table = table.init();
// });
