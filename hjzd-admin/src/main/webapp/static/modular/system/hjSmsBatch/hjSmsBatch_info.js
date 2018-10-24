/**
 * 初始化sms_batch详情对话框
 */
var HjSmsBatchInfoDlg = {
    hjSmsBatchInfoData : {},
    validateFields: {
        smsBatchName: {
            validators:{
               notEmpty:{
                  message: '批次名称不能为空'
               },
            }
        },
        batchDesc: {
            validators:{
                notEmpty:{
                   message: '批次描述不能为空'
                },
            }
        },
        smsContent :{
            validators:{
                notEmpty:{
                   message: '短信内容不能为空'
                },
            }
        }

    }
};

/**
 * 清除数据
 */
HjSmsBatchInfoDlg.clearData = function() {
    this.hjSmsBatchInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HjSmsBatchInfoDlg.set = function(key, val) {
    this.hjSmsBatchInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
HjSmsBatchInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
HjSmsBatchInfoDlg.close = function() {
    parent.layer.close(window.parent.HjSmsBatch.layerIndex);
}

/**
 * 收集数据
 */
HjSmsBatchInfoDlg.collectData = function() {
    this
    .set('id')
    .set('smsBatchName')
    .set('batchDesc')
    .set('smsContent')
    .set('batchCapacity')
    .set('status');
}


/**
 * 验证数据是否为空
 */
HjSmsBatchInfoDlg.validate = function () {
    $('#smsBatchForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#smsBatchForm').bootstrapValidator('validate');
    return $("#smsBatchForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};


/**
 * 提交添加
 */
HjSmsBatchInfoDlg.addSubmit = function() {

    $("#ensure").attr("disabled",true);
    this.clearData();
    this.collectData();

    // if (!this.validate()) {
    //      return;
    // }

    // 上述this.validate() 调用正常的话，下述代码可不用
    var sms_batch_name = $('#smsBatchName'); // 短信批次名称
    if (sms_batch_name.val() == '' || typeof sms_batch_name == "undefined") {
        Feng.error("批次名称不能为空!");
        return false;
    }

    var sms_batch_desc = $('#batchDesc'); //批次描述
    if (sms_batch_desc.val() == '' || typeof sms_batch_desc == "undefined") {
        Feng.error("批次描述不能为空!");
        return false;
    }
    var sms_content = $('#smsContent'); //短信内容
    if ($.trim(sms_content.val()) == '' || typeof sms_content == "undefined") {
        Feng.error("短信内容不能为空!");
        return false;
    }

    var capacity = $('#batchCapacity');// 容量
    if (capacity.val() == '' || typeof capacity == "undefined" || capacity.val() == '-1') {
        Feng.error("请选择容量!");
        return false;
    }

    var status = $('#status'); // 发送类型
    if( status.val() !=='' && status.val() =='-1'){
        Feng.error("请选择发送类型!");
        return false;
    }

    var reserveTime = $('#reserveTime'); // 预约时间

    if (reserveTime.val() != '' && typeof reserveTime != "undefined") {
        this.set('reserveTime',reserveTime.val());
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/hjSmsBatch/add", function(data){
        Feng.success("添加短信批次成功!");
        window.parent.HjSmsBatch.table.reload("HjSmsBatchTable");
        setTimeout(function () {
            window.parent.layer.closeAll()
        },1000)
    },function(data){
        $("#ensure").attr("disabled",false);
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.hjSmsBatchInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
HjSmsBatchInfoDlg.editSubmit = function() {
    $("#ensure").attr("disabled",true);
    this.clearData();
    this.collectData();

    // if(!this.validate()){
    //      return;
    // }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/hjSmsBatch/update", function(data){
        Feng.success("修改成功!");
        window.parent.HjSmsBatch.table.reload("HjSmsBatchTable");
        setTimeout(function () {
            window.parent.layer.closeAll()
        },1000)
    },function(data){
        $("#ensure").attr("disabled",false);
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.hjSmsBatchInfoData);
    ajax.start();
}


layui.use(['form', 'table', 'laydate'], function () {
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;


    laydate.render({
        elem: '#reserveTime', //注册渲染时间日期
        type: 'datetime'
    });

    form.on('select(send-select)', function (res) {
        var value = res.value;
        var valueName = res.elem[res.elem.selectedIndex].text;
        if (valueName.indexOf("预约发送") >= 0) {
            $('.reserveDiv').css('display', 'block');
            $('#reserveTime').attr('lay-verify', 'required');
        } else {
            $('.reserveDiv').css('display', 'none');
            $('#reserveTime').removeAttr('lay-verify');
        }
    });

    form.on('select(SmsModel)',function(data){
        var modelId = data.value;
        var model = $('#SmsModel').find('option[value='+modelId+']').html();
        $('#smsContent').val(model);
    })



});




$(function() {

});
