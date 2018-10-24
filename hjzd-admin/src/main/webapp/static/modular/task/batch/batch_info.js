/**
 * 初始化数据批次详情对话框
 */
var BatchInfoDlg = {
    batchInfoData : {}
};

/**
 * 清除数据
 */
BatchInfoDlg.clearData = function() {
    this.batchInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BatchInfoDlg.set = function(key, val) {
    this.batchInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BatchInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BatchInfoDlg.close = function() {
    parent.layer.close(window.parent.Batch.layerIndex);
}

/**
 * 收集数据
 */
BatchInfoDlg.collectData = function() {
    this
    .set('id')
    .set('productId')
    .set('batchNo')
    .set('userId')
    .set('filePath')
    .set('importNum')
    .set('createdAt')
    .set('groupId')
    .set('isDel');
}

/**
 * 提交添加
 */
BatchInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/batch/add", function(data){
        Feng.success("添加成功!");
        window.parent.Batch.table.refresh();
        BatchInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.batchInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BatchInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/batch/update", function(data){
        Feng.success("修改成功!");
        window.parent.Batch.table.refresh();
        BatchInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.batchInfoData);
    ajax.start();
}

$(function() {

});
