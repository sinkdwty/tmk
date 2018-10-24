/**
 * 媒体管理管理初始化
 */
var Media = {
    id: "MediaTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Media.initColumn = function () {
    return [
        {field: 'selectItem', radio: true, visible: false},
            {title: '序号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '媒体名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '分类', field: 'categoryName', visible: true, align: 'center', valign: 'middle',minwidth:'100px'},
            {title: '媒体图片', field: 'pic', visible: true, align: 'center', valign: 'middle',
                formatter: function (value, row, index){ // 单元格格式化函数
                    var img = '';
                    if (value == "") {
                        img = "<img src='' width='80px' height='40px'/>";
                    } else {
                        img = "<img src='/kaptcha/"+ value +"' width='80px' height='40px'/>";
                    }
                    return img;
                }
            },
            {title: '媒体链接', field: 'url', visible: true, align: 'center', valign: 'middle',
                formatter: function (value, row, index){ // 单元格格式化函数
                    var text = '';
                    if (value == "") {
                        text = "";
                    } else {
                        text = '<a href="'+ value +'" target="_blank" style="color: #0d8ddb">'+ value +'</a>';
                    }
                    return text;
                }
            },
            // {title: '媒体分类', field: 'category', visible: true, align: 'center', valign: 'middle'},
            {title: '媒体简介', field: 'introduce', visible: true, align: 'center', valign: 'middle',width:'250px',
                formatter: function (value, row, index){ // 单元格格式化函数
                    if (value.length > 20) {
                        value = value.substring(0,20) + "...";
                    }
                    return value;
                }
            },
            {title: '创建时间', field: 'createdAt', visible: true, align: 'center', valign: 'middle'},
            {title: '操作',field: 'operate',align: 'center',events: "operateEvents",formatter: operateFormatter,width:180}
    ];
};

function operateFormatter(value, row, index) {
    // console.info(row);
    return [
        '<button type="button" class="update-media btn btn-primary  btn-sm" style="margin-right:5px;">修改</button>',
        '<button type="button" class="delete-media btn btn-primary btn-danger  btn-sm" style="margin-right:5px;">删除</button>',
    ].join('');
}

window.operateEvents = {
    'click .update-media': function (e, value, row, index) {
        Media.openMediaDetail(row);
    },
    'click .delete-media': function (e, value, row, index) {
        var id = row.id;
        if (id != '') {
            layer.confirm("确定要删除该数据？", {
                btn: ['确定', '取消']
            }, function (index) {
                Media.delete(id);
                layer.close(index);
            }, function (index) {
                layer.close(index);
            });
        }
    },
};


/**
 * 点击添加媒体管理
 */
Media.openAddMedia = function () {
    var index = layer.open({
        type: 2,
        title: '添加媒体管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/media/media_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看媒体管理详情
 */
Media.openMediaDetail = function (row) {
        var id = row.id;
        var index = layer.open({
            type: 2,
            title: '媒体管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/media/media_update/' + id
        });
        this.layerIndex = index;
};

/**
 * 删除媒体管理
 */
Media.delete = function (id) {
        var ajax = new $ax(Feng.ctxPath + "/media/delete", function (data) {
            Feng.success("删除成功!");
            Media.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("mediaId",id);
        ajax.start();
};

/**
 * 查询媒体管理列表
 */
Media.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['category'] = $("#category").val();
    Media.table.refresh({query: queryData});
};
/**
 * 搜索条件重置
 */
Media.reset = function () {
    $('input').val("");
    $('select').val("");
}


$(function () {
    var defaultColunms = Media.initColumn();
    var table = new BSTable(Media.id, "/media/list", defaultColunms);
    table.setPaginationType("server");
    Media.table = table.init();
});
