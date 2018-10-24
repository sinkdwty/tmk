/**
 * 初始化公司详情对话框
 */
var DeptInfoDlg = {
    deptInfoData : {},
    zTreeInstance : null
};

/**
 * 清除数据
 */
DeptInfoDlg.clearData = function() {
    this.deptInfoData = {};
}

/**
 * 关闭此对话框
 */
DeptInfoDlg.close = function() {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}

/**
 * 点击公司ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
DeptInfoDlg.onClickDept = function(e, treeId, treeNode) {
    $("#pName").attr("value", DeptInfoDlg.zTreeInstance.getSelectedVal());
    $("#pid").attr("value", treeNode.id);
}

/**
 * 显示公司选择的树
 *
 * @returns
 */
DeptInfoDlg.showDeptSelectTree = function() {
    var pName = $("#pName");
    var pNameOffset = $("#pName").offset();
    $("#parentDeptMenu").css({
        left : pNameOffset.left + "px",
        top : pNameOffset.top + pName.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}

/**
 * 隐藏公司选择的树
 */
DeptInfoDlg.hideDeptSelectTree = function() {
    $("#parentDeptMenu").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "parentDeptMenu" || $(
            event.target).parents("#parentDeptMenu").length > 0)) {
        DeptInfoDlg.hideDeptSelectTree();
    }
}

$(function() {
    //Feng.initValidator("deptInfoForm", DeptInfoDlg.validateFields);

    var ztree = new $ZTree("parentDeptMenuTree", "/dept/tree");
    ztree.bindOnClick(DeptInfoDlg.onClickDept);
    ztree.init();
    DeptInfoDlg.zTreeInstance = ztree;
});


layui.use('form', function(){
    var form = layui.form;

    form.on('submit(add)', function(data) {
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/dept/add", function(data) {
            window.parent.layer.closeAll();
            Feng.success("添加成功!");
            parent.layui.table.reload('deptManagerTable');
        }, function(data) {
            Feng.error("添加失败!" + data.responseJSON.message + "!");
        });
        ajax.set(data.field);
        ajax.start();
        return false;
    });

    form.on('submit(edit)', function(data) {
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/dept/update", function(data){
            window.parent.layer.closeAll();
            Feng.success("修改成功!");
            parent.layui.table.reload('deptManagerTable');
        }, function(data) {
            Feng.error("修改失败!" + data.responseJSON.message + "!");
        });
        ajax.set(data.field);
        ajax.start();
        return false;
    });
});