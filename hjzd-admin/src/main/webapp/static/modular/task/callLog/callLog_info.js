/**
 * 初始化通话日志详情对话框
 */
var CallLogInfoDlg = {
    callLogInfoData : {}
};

/**
 * 清除数据
 */
CallLogInfoDlg.clearData = function() {
    this.callLogInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CallLogInfoDlg.set = function(key, val) {
    this.callLogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CallLogInfoDlg.get = function(key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
CallLogInfoDlg.close = function() {
    parent.layer.close(window.parent.CallLog.layerIndex);
};

/**
 * 收集数据
 */
CallLogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('caseId')
    .set('customName')
    .set('customPhone')
    .set('userId')
    .set('userName')
    .set('callStatusId')
    .set('callStatusName')
    .set('callStartTime')
    .set('callEndTime')
    .set('callSecond')
    .set('callAgentNo')
    .set('callSessionid')
    .set('callUserdata')
    .set('note')
    .set('createdAt')
    .set('createdIp');
};

/**
 * 提交添加
 */
CallLogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/callLog/add", function(data){
        Feng.success("添加成功!");
        window.parent.CallLog.table.refresh();
        CallLogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.callLogInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
CallLogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/callLog/update", function(data){
        Feng.success("修改成功!");
        window.parent.CallLog.table.refresh();
        CallLogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.callLogInfoData);
    ajax.start();
};

