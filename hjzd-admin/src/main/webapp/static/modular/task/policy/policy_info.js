/**
 * 初始化商品管理详情对话框
 */
var PolicyInfoDlg = {
    policyInfoData : {},
    validateFields: {
        policyName: {
            validators: {
                notEmpty: {
                    message: '策略名称不能为空'
                },
            }
        },
        policyKey: {
            validators: {
                notEmpty: {
                    message: '请选择话术内容'
                },
            }
        },
    }
};

/**
 * 清除数据
 */
PolicyInfoDlg.clearData = function() {
    this.policyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PolicyInfoDlg.set = function(key, val) {
    this.policyInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PolicyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PolicyInfoDlg.close = function() {
    parent.layer.close(window.parent.Policy.layerIndex);
}

/**
 * 收集数据
 */
PolicyInfoDlg.collectData = function() {
    this
        .set('id')
        .set('policyName')
        .set('policyKey')
        .set('note')
}

/**
 * 验证数据是否为空
 */
PolicyInfoDlg.validate = function () {
    $('#PolicyInfoForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#PolicyInfoForm').bootstrapValidator('validate');              //触发全部验证
    return $("#PolicyInfoForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};

/**
 * 提交添加
 */
PolicyInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    /*if(!this.policyInfoData.policyKey) {
        alert("请选择话术");
        return;
    }*/

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/policy/add", function(data){
        if (data.code == "200") {
            Feng.success("添加成功!");
            parent.layui.table.reload("policyTable");
            PolicyInfoDlg.close();
        } else if (data.code == "201") {
            Feng.error(data.message);
        }
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.policyInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PolicyInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    /*if(!this.policyInfoData.policyKey) {
        alert("请选择话术");
        return;
    }*/

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/policy/edit", function(data){
        if (data.code == "200") {
            Feng.success("修改成功!");
            parent.layui.table.reload("policyTable");
            PolicyInfoDlg.close();
        } else if (data.code == "201") {
            Feng.error(data.message);
        }

    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.policyInfoData);
    ajax.start();
}

/**
 * 显示话术下拉框
 */
PolicyInfoDlg.selectSpeech = function (){
    if($('#policyKeyHide').css("display") == "none") {
        $('#policyKeyHide').removeClass("hide").show();
        $('#searchSpeech').val("");
        $('#policyKeyHide').find("li").show();
    } else {
        $('#policyKeyHide').hide();
    }
}

/**
 * 初始化
 */
$(function () {
    //初始化验证
    Feng.initValidator("PolicyInfoForm", PolicyInfoDlg.validateFields);

    /* 初始化修改操作时，显示已选择话术 */
    var speechIds = $("#policyKey").val();
    if(speechIds) {
        speechIds = speechIds.split(',');
        for (var i = 0;i < speechIds.length;i++) {
            $('#policyKeyHide').find('li').each(function(){
                if($(this).val() == speechIds[i]) {
                    $('#sortable').append($(this).clone())
                    $(this).addClass('selected')
                }
            });
        }
    }

    /**
     * 话术下拉框 话术上的移入移出操作
     */
    $("#policyKeyHide").find('li').mouseover(function(){
        $(this).css("background","#1E90FF").css("color","#fff");
    }).mouseout(function(){
        if($(this).hasClass('selected')) {
            return false;
        }
        $(this).css("background","#fff").css("color","#555");
    });

    /**
     * 话术的搜索功能
     */
    $("#searchSpeech").on("keyup",function(){
        var search = $(this).val();
        var re = new RegExp(eval('/^(.*)'+search+'(.*)$/'));
        $('#policyKeyHide').find("li").each(function(){
            var te = $(this).text();
            if(re.test(te) == true) {
                $(this).show();
            }else {
                $(this).hide();
            }
        });
    });

    /**
     * 话术弹窗关闭效果
     */
    $("#PolicyInfoForm").on("click",function(e){
        if ($(e.target).hasClass('content') ||$(e.target).hasClass('contentsUl') || $(e.target).attr("id") == "policyKeys" || $(e.target).attr("id") == "searchSpeech") {

        } else {
            $('#policyKeyHide').hide();
        }
    })

    /**
     * 话术的选中和取消选中
     */
    $('#policyKeyHide').find("li").on("click",function(){
        var speechId = $(this).val();
        if ($(this).hasClass('selected')) {
           $(this).removeClass('selected');
           $("#sortable").find('li').each(function () {
               if ($(this).val() == speechId) {
                   $(this).remove();
               }
           })
        } else {
            var li = $(this).clone().css("background","#fff").css("color","#555");
            $(this).addClass('selected');
            $('#sortable').append(li);
        }
        getSpeechIdByClass();
    });

    /**
     * 话术框行拖拽
     */
    $("#sortable").sortable({
        revert: false,
        update: function(event, ui) {
            var ids = '';
            $("#sortable").find("li").each(function () {
                if(ids){
                    ids += "," + $(this).val()
                } else {
                    ids = $(this).val()
                }
            })
            $("#policyKey").val(ids);
         }
    });
});

/**
 * 根据选中状态获取所有选中话术id
 */
function getSpeechIdByClass() {
    $("#policyKey").val("");
    $('#policyKeyHide').find("li").each(function(){
        if($(this).hasClass('selected')) {
            var ids = $("#policyKey").val();
            if(ids) {
                $("#policyKey").val(ids + ',' + $(this).val());
            } else {
                $("#policyKey").val($(this).val());
            }
        }
    });
}
