/**
 * 初始化媒体管理详情对话框
 */
var MediaInfoDlg = {
    mediaInfoData : {},
    validateFields : {
        name : {
            validators : {
                notEmpty : {
                    message: '媒体名称不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
MediaInfoDlg.clearData = function() {
    this.mediaInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MediaInfoDlg.set = function(key, val) {
    // this.mediaInfoData[key] = (typeof val == "undefined") ? $("#" + key).val().replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];}) : val.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
    this.mediaInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MediaInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MediaInfoDlg.close = function() {
    parent.layer.close(window.parent.Media.layerIndex);
}

/**
 * 收集数据
 */
MediaInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('pic')
    .set('url')
    .set('category')
    .set('introduce')
    .set('tags');
}

/**
 * 验证数据是否为空
 */
MediaInfoDlg.validate = function () {
    $('#mediaInfoForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#mediaInfoForm').bootstrapValidator('validate');              //触发全部验证
    return $("#mediaInfoForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};

/**
 * 提交添加
 */
MediaInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/media/add", function(data){
        Feng.success("添加成功!");
        window.parent.Media.table.refresh();
        MediaInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.mediaInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MediaInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/media/update", function(data){
        Feng.success("修改成功!");
        window.parent.Media.table.refresh();
        MediaInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.mediaInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("mediaInfoForm", MediaInfoDlg.validateFields);
    // 初始化商品图片上传
    var mediaPic = new $WebUpload("pic");
    mediaPic.setUploadBarId("progressBar");
    mediaPic.init();
    /**
     *
     * 媒体标签
     */
    $('#tags').tagsInput({
        'height':'auto', //设置高度
        'width':'auto', //设置宽度
        'interactive':true, //是否允许添加标签，false为阻止
        'defaultText':'添加标签', //默认文字
        'onAddTag':function (tag) {
        }, //增加标签的回调函数
        'onRemoveTag':function (tag) {
        }, //删除标签的回调函数
        'onChange' : function (tag) {
        }, //改变一个标签时的回调函数
        'removeWithBackspace' : true, //是否允许使用退格键删除前面的标签，false为阻止
        'minChars' : 0, //每个标签的小最字符
        'maxChars' : 0, //每个标签的最大字符，如果不设置或者为0，就是无限大
        'placeholderColor' : '#666666' //设置defaultText的颜色
    });
});
