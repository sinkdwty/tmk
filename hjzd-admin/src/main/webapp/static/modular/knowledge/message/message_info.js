/**
 * 初始化短信模板管理详情对话框
 */
var MessageInfoDlg = {
    messageInfoData:{},
    validateFields : {
        message: {
            validators: {
                notEmpty: {
                    message: '短信内容不能为空'
                },
            }
        },
    }
};

/**
 * 清除数据
 */
MessageInfoDlg.clearData = function() {
    this.messageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MessageInfoDlg.set = function(key, val) {
    this.messageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MessageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MessageInfoDlg.close = function() {
    parent.layer.close(window.parent.Message.layerIndex);
}

/**
 * 收集数据
 */
MessageInfoDlg.collectData = function() {
    this
    .set('id')
    .set('message')
    .set('note')
}

/**
 * 验证数据是否为空
 */
MessageInfoDlg.validate = function () {
    $('#messageInfoForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#messageInfoForm').bootstrapValidator('validate');              //触发全部验证
    return $("#messageInfoForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};

/**
 * 提交添加
 */
MessageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    // return false;

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/message/add", function(data){
        if (data.code == "200") {
            Feng.success("添加成功!");
            parent.layui.table.reload("MessageTable");
            MessageInfoDlg.close();
        } else if (data.code == "201") {
            Feng.error(data.message);
        }
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.messageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MessageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/message/update", function(data){
        if (data.code == "200") {
            Feng.success("修改成功!");
            parent.layui.table.reload("MessageTable");
            MessageInfoDlg.close();
        } else if (data.code == "201") {
            Feng.error(data.message);
        }

    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.messageInfoData);
    ajax.start();
}

$(function() {
    //初始化验证
    Feng.initValidator("messageInfoForm", MessageInfoDlg.validateFields);
});
