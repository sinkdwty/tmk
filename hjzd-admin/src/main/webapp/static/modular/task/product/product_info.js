/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var ProductInfoDlg = {
    ProductInfoDlg: {},
};

/**
 * 清除数据
 */
ProductInfoDlg.clearData = function () {
    this.ProductInfoDlg = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ProductInfoDlg.set = function (key, val) {
    this.ProductInfoDlg[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ProductInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
ProductInfoDlg.close = function () {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
};

/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
ProductInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#citySel").attr("value", instance.getSelectedVal());
    $("#baseId").attr("value", treeNode.id);
};

/**
 * 显示部门选择的树
 *
 * @returns
 */
ProductInfoDlg.showDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityOffset = $("#citySel").offset();
    $("#menuContent").css({
        left: cityOffset.left + "px",
        top: cityOffset.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

/**
 * 显示用户详情部门选择的树
 *
 * @returns
 */
ProductInfoDlg.showInfoDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityPosition = $("#citySel").position();
    $("#menuContent").css({
        left: cityPosition.left + "px",
        top: cityPosition.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

/**
 * 隐藏部门选择的树
 */
ProductInfoDlg.hideDeptSelectTree = function () {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
};

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
        event.target).parents("#menuContent").length > 0)) {
        ProductInfoDlg.hideDeptSelectTree();
    }
}

$(function () {
    Feng.initValidator("productInfoForm", ProductInfoDlg.validateFields);

    var ztree = new $ZTree("tree", "/dept/tree");
    ztree.bindOnClick(ProductInfoDlg.onClickDept);
    ztree.init();
    instance = ztree;

    // 初始化头像上传
    var avatarUp = new $WebUpload("avatar");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init();


});



layui.use('form', function(){
    var form = layui.form;

    form.on('submit(add)', function(data){
        if ($('#citySel').val() == "顶级" || $('#baseId').val() == 0) {
            Feng.error("请正确选择公司");
            return false;
        }
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/product/add", function(data){
            window.parent.layer.closeAll();
            Feng.success("添加成功!");
            parent.layui.table.reload('productTable');
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.message + "!");
        });
        ajax.set(data.field);
        ajax.start();
        return false;
    });

    form.on('submit(edit)', function(data){
        if ($('#citySel').val() == "顶级" || $('#baseId').val() == 0) {
            Feng.error("请正确选择公司");
            return false;
        }
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/product/edit", function(data){
            window.parent.layer.closeAll();
            Feng.success("修改成功!");
            parent.layui.table.reload('productTable');
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.message + "!");
        });
        ajax.set(data.field);
        ajax.start();
        return false;
    });
});