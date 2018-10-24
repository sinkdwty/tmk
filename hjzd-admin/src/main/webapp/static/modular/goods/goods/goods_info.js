/**
 * 初始化商品管理详情对话框
 */
var GoodsInfoDlg = {
    goodsInfoData : {},
    validateFields: {
        goodsName: {
            validators: {
                notEmpty: {
                    message: '商品名称不能为空'
                },
            }
        },
        stockNum: {
            message: '请正确填写库存量',
            validators: {
                stringLength: {
                    min: 0,
                    max: 9,
                    message: '请正确填写库存量',
                },
                regexp: {
                    regexp: /^[0-9]+$/,
                }
            }
        },
        goodsPrice:{
            message: '请正确填写商品价格',
            validators: {
                stringLength: {
                    min: 0,
                    max: 10,
                    message: '请正确填写商品价格',
                },
                regexp: {
                    regexp: /^[0-9.]+$/,
                }
            }
        },
        url:{
            message: '请正确填写商品地址',
            validators: {
                stringLength: {
                    min: 0,
                    max: 255,
                    message: '最多包含255个字符',
                },
            }
        },
    }
};

/**
 * 清除数据
 */
GoodsInfoDlg.clearData = function() {
    this.goodsInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
GoodsInfoDlg.set = function(key, val) {
    /*var value = '';
    if ($.trim($("#" + key).val()) != '') {
        value = $("#" + key).val().replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
    }

    if ($.trim(val) != '') {
        val = val.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
    }*/
    this.goodsInfoData[key] = (typeof val == "undefined") ?  $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
GoodsInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
GoodsInfoDlg.close = function() {
    parent.layer.close(window.parent.Goods.layerIndex);
}

/**
 * 收集数据
 */
GoodsInfoDlg.collectData = function() {
    this
        .set('id')
        .set('goodsName')
        .set('goodsPrice')
        .set('stockNum')
        .set('picUrl')
        .set('url')
        .set('batchNo')
        .set('categoryNo')
        .set('note')
        .set('goodsNote')
        .set('isSale')
        .set('isDel')
        .set('createdAt')
        .set('updatedAt');
}

/**
 * 验证数据是否为空
 */
GoodsInfoDlg.validate = function () {
    $('#goodsInfoForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#goodsInfoForm').bootstrapValidator('validate');              //触发全部验证
    return $("#goodsInfoForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};

/**
 * 提交添加
 */
GoodsInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/goods/add", function(data){
        Feng.success("添加成功!");
        window.parent.Goods.table.reload('managerTable');
        GoodsInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.goodsInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
GoodsInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/goods/update", function(data){
        Feng.success("修改成功!");
        window.parent.Goods.table.reload('managerTable');
        GoodsInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.goodsInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("goodsInfoForm", GoodsInfoDlg.validateFields);
    // 初始化商品是否在架
    $("#isSale").val($("#goodsIsSale").val());

    // 初始化商品图片上传
    var goodsPic = new $WebUpload("picUrl");
    goodsPic.setUploadBarId("progressBar");
    goodsPic.init();
});

layui.use('upload', function(){
    var upload = layui.upload;
    var layer = layui.layer;    // 弹层

    //执行实例
    var uploadInst = upload.render({
        elem: '#goodsUpload' //绑定元素
        ,url: Feng.ctxPath + '/goods/upload' //上传接口
        ,accept: 'file' //允许上传的文件类型
        ,size:10 * 1024 *1024
        ,exts:'xls|xlsx'
        ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            layer.load(); //上传loading
        }
        ,done: function(res){
            if (res.code == 200) {
                layer.msg(res.message,function () {
                    GoodsInfoDlg.close();
                    // window.parent.Goods.table.refresh();
                    window.parent.Goods.table.reload('managerTable');
                });
            } else {
                layer.msg(res.message,function () {
                    layer.closeAll("loading");
                });
            }
            //上传完毕回调
        }
        ,error: function(){
            layer.msg("导入异常",function () {
                layer.closeAll("loading");
            });
        }
    });
});