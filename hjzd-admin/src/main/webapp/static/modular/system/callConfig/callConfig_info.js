/**
 * 初始化外呼管理详情对话框
 */
var CallConfigInfoDlg = {
    callConfigInfoData : {},
    validateFields: {
        baseName: {
            validators: {
                notEmpty: {
                    message: '基地名称不能为空'
                },
            }
        },
        callSystemId: {
            validators: {
                notEmpty: {
                    message: '外呼系统不能为空'
                },
            }
        },
        config: {
            validators: {
                notEmpty: {
                    message: '外呼配置不能为空'
                },
            }
        },
    }
};

/**
 * 清除数据
 */
CallConfigInfoDlg.clearData = function() {
    this.callConfigInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CallConfigInfoDlg.set = function(key, val) {
    this.callConfigInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CallConfigInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CallConfigInfoDlg.close = function() {
    parent.layer.close(window.parent.CallConfig.layerIndex);
}

/**
 * 收集数据
 */
CallConfigInfoDlg.collectData = function() {
    this
    .set('id')
    .set('baseName')
    .set('baseId')
    .set('callSystemId')
    .set('config')
    .set('createdAt')
    .set('updatedAt')
    .set('isDel');
}

/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
CallConfigInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#baseName").attr("value", instance.getSelectedVal());
    $("#baseId").attr("value", treeNode.id);
};

/**
 * 显示部门选择的树
 *
 * @returns
 */
CallConfigInfoDlg.showDeptSelectTree = function () {
    var cityObj = $("#baseName");
    var cityOffset = $("#baseName").offset();
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
CallConfigInfoDlg.showInfoDeptSelectTree = function () {
    var cityObj = $("#baseName");
    var cityPosition = $("#baseName").position();
    $("#menuContent").css({
        left: cityPosition.left + "px",
        top: cityPosition.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

/**
 * 隐藏部门选择的树
 */
CallConfigInfoDlg.hideDeptSelectTree = function () {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
};

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
        event.target).parents("#menuContent").length > 0)) {
        CallConfigInfoDlg.hideDeptSelectTree();
    }
}

/**
 * 验证数据是否为空
 */
CallConfigInfoDlg.validate = function () {
    $('#callConfigForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#callConfigForm').bootstrapValidator('validate');              //触发全部验证
    return $("#callConfigForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};

/**
 * 提交添加
 */
CallConfigInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/callConfig/add", function(data){
        Feng.success("添加成功!");
        window.parent.CallConfig.table.reload('callConfigTable');
        CallConfigInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.callConfigInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CallConfigInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/callConfig/update", function(data){
        Feng.success("修改成功!");
        window.parent.CallConfig.table.reload('callConfigTable');
        CallConfigInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.callConfigInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("callConfigForm", CallConfigInfoDlg.validateFields);

    //初始化部门选择框
    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(CallConfigInfoDlg.onClickDept);
    ztree.init();
    instance = ztree;
});
