/**
 * 初始化商品管理详情对话框
 */
var SpeechInfoDlg = {
    speechInfoData : {},
    validateFields: {
        contents: {
            validators: {
                notEmpty: {
                    message: '话术内容不能为空'
                },
            }
        },
    }
};

/**
 * 清除数据
 */
SpeechInfoDlg.clearData = function() {
    this.speechInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SpeechInfoDlg.set = function(key, val) {
    this.speechInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SpeechInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SpeechInfoDlg.close = function() {
    parent.layer.close(window.parent.Speech.layerIndex);
}

/**
 * 收集数据
 */
SpeechInfoDlg.collectData = function() {
    this
        .set('id')
        .set('contents')
        .set('note')
}

/**
 * 验证数据是否为空
 */
SpeechInfoDlg.validate = function () {
    $('#speechInfoForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#speechInfoForm').bootstrapValidator('validate');              //触发全部验证
    return $("#speechInfoForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};

/**
 * 提交添加
 */
SpeechInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/speechcraft/add", function(data){
        Feng.success("添加成功!");
        parent.layui.table.reload("SpeechTable");
        SpeechInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.speechInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
SpeechInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/speechcraft/edit", function(data){
        Feng.success("修改成功!");
        parent.layui.table.reload("SpeechTable");
        SpeechInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.speechInfoData);
    ajax.start();
}

$(function () {
    Feng.initValidator("speechInfoForm", SpeechInfoDlg.validateFields);
});

