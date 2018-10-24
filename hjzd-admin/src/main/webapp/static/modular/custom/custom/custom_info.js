/**
 * 初始化客户管理详情对话框
 */
var CustomInfoDlg = {
    customInfoData : {},
    validateFields: {

        phone: {
            validators: {
                notEmpty: {
                    message: '手机号码不能为空'
                },
                regexp: {
                    regexp: /^[1][3,4,5,7,8][0-9]{9}$/,
                    message: '请正确填写手机号码'
                },

                // threshold :  11,

                // remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                //     url: Feng.ctxPath + "/custom/unique-phone",//验证地址
                //     message: '手机号码已存在',//提示消息
                //     delay :  2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                //     type: 'POST'//请求方式
                //     /**自定义提交数据，默认值提交当前input value
                //      *  data: function(validator) {
                //                return {
                //                    password: $('[name="passwordNameAttributeInYourForm"]').val(),
                //                    whatever: $('[name="whateverNameAttributeInYourForm"]').val()
                //                };
                //             }
                //      */
                //

                // },
            }
        },
       /**
        customName: {
            validators: {
                notEmpty: {
                    message: '客户姓名不能为空'
                },
            }
        },

        email:{
            validators:{
                notEmpty:{
                    message: '邮箱不能为空'
                },
                regexp: {
                    regexp: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                    message: '请正确填写邮箱'
                },
            }
        }, **/

        /**
        sex:{
            validators:{
                notEmpty:{
                    message: '请选择性别'
                }
            }
        },

        customEducationBackground:{
            validator:{
                notEmpty:{
                    message: '请选择客户学历'
                }
            }
        } **/
    }
};



/**
 * 清除数据
 */
CustomInfoDlg.clearData = function() {
    this.customInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomInfoDlg.set = function(key, val) {
    this.customInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomInfoDlg.get = function(key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
CustomInfoDlg.close = function() {

    if (window.parent.Custom == undefined) {
        var that = $(window.parent.document).find('a[data-id="/custom/custom_add"] i');
        var parentDiv = $(window.parent.document).find('.J_mainContent');
        var closeTabId = $(that).parents('.J_menuTab').data('id');
        var currentWidth = $(that).parents('.J_menuTab').width();
        // 当前元素处于活动状态
        if ($(that).parents('.J_menuTab').hasClass('active')) {

            // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
            if ($(that).parents('.J_menuTab').next('.J_menuTab').size()) {

                var activeId = $(that).parents('.J_menuTab').next('.J_menuTab:eq(0)').data('id');
                //highLightMenuItem(activeId);  //高亮对应的tab菜单
                $(that).parents('.J_menuTab').next('.J_menuTab:eq(0)').addClass('active');

                $(parentDiv).find('.J_iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });

                var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                if (marginLeftVal < 0) {
                    $('.page-tabs-content').animate({
                        marginLeft: (marginLeftVal + currentWidth) + 'px'
                    }, "fast");
                }

                //  移除当前选项卡
                $(that).parents('.J_menuTab').remove();

                // 移除tab对应的内容区
                $(parentDiv).find('.J_iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }

            // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
            if ($(that).parents('.J_menuTab').prev('.J_menuTab').size()) {
                var activeId = $(that).parents('.J_menuTab').prev('.J_menuTab:last').data('id');
                $(that).parents('.J_menuTab').prev('.J_menuTab:last').addClass('active');
                $(parentDiv).find('.J_iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });

                //  移除当前选项卡
                $(that).parents('.J_menuTab').remove();

                // 移除tab对应的内容区
                $(parentDiv).find('.J_iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
                // highLightMenuItem(activeId);//高亮对应的tab菜单
            }
        }
        // 当前元素不处于活动状态
        else {
            //  移除当前选项卡
            $(that).parents('.J_menuTab').remove();

            // 移除相应tab对应的内容区
            $(parentDiv).find('.J_iframe').each(function () {
                if ($(this).data('id') == closeTabId) {
                    $(this).remove();
                    return false;
                }
            });
            //scrollToTab($('.J_menuTab.active'));
        }
        return false;
    } else {
        parent.layer.close(window.parent.Custom.layerIndex);
    }



};

// 关闭选项卡菜单



/**
 * 收集数据
 */
CustomInfoDlg.collectData = function() {
    this
    .set('id')
    .set('customName')
    .set('phone')
    .set('email')
    .set('company')
    .set('address')
    .set('principal')
    .set('label')
    .set('customSource')
    .set('firstContactTime')
    .set('contactTimes')
    .set('note')
    .set('createdTime')
    .set('updatedTime')
    .set('productId');
};


/**
 * 处理 我的客户-新增字段拆分  add  by eric 2018-09-13
 */
CustomInfoDlg.collectDataForMyCustom = function() {
    this
        .set('phone')
        .set('customName')
        .set('email')
        .set('sex')
        .set('customEducationBackground')
        .set('graduateSchool')
        .set('jobCity')
        .set('companyName')
        .set('jobType')
        .set('cardBank')
        .set('applyCardType')
        .set('dataChannel')
        .set('dataBatch')
        .set('otherCustomers')
        .set('havingOtherCard')
        .set('cardUseTime')
        .set('isCorporateShareholders')
        .set('convenientTime')
        .set('note')
        .set('call_status_top')
        .set('call_status_content')
        .set('reserveTime')
        .set('phoneNote');
};



/**
 * 验证数据是否为空
 */
CustomInfoDlg.validate = function () {
    $('#customForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#customForm').bootstrapValidator('validate');
    return $("#customForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};
/**
 * 提交添加
 */
CustomInfoDlg.addSubmit = function() {
    this.clearData();

    // add by eric 2018-09-26 start

    var call_status_content = ""; // ----> call_status_id
    if ($('#call_status_top option:selected').text() =="预约联系") {
        call_status_content = $('#call_status_top').val();
    } else {
        if ($('#call_status_top option:selected').val() =="-1") {
            call_status_content = $('#call_status_top').val();
        }else{
            call_status_content = $('#call_status_id').val()
        }
    }

    // add by eric 2018-09-26 end

    // modified by eric 2018-09-14 start
    this.collectDataForMyCustom();
    // this.set('call_status_content',call_status_content);
    this.customInfoData.call_status_content = call_status_content;
    if (!this.validate()) {
         return;
    }
    // modified by eric 2018-09-14 end
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/custom/add", function(data){
        Feng.success("添加成功!");
        // window.parent.Custom.table.reload('CustomTable');
        //CustomInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CustomInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    // console.log(this.validate());
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/custom/update", function(data){
        Feng.success("修改成功!");
        window.parent.Custom.table.reload('CustomTable');
        CustomInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customInfoData);
    ajax.start();
};

$(function() {
    Feng.initValidator("customForm", CustomInfoDlg.validateFields);

});

$(function() {
    layui.use('upload', function(){
        var upload = layui.upload;
        var layer = layui.layer;    // 弹层

        //执行实例
        var uploadInst = upload.render({
            elem: '#customUpload' //绑定元素
            ,url: Feng.ctxPath + '/custom/upload' //上传接口
            ,accept: 'file' //允许上传的文件类型
            ,size:10 * 1024 *1024
            ,exts:'xls|xlsx'
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                layer.load(); //上传loading
            }
            ,done: function(res){
                if (res.code == 200) {
                    layer.msg(res.message,function () {
                        CustomInfoDlg.close();
                        // window.parent.Custom.table.refresh();
                        window.parent.Custom.table.reload('CustomTable');
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

});
