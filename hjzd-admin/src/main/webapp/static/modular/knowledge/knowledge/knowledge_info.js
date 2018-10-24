$('#category').css({'width':''});

/**
 * 初始化知识库管理详情对话框
 */
var KnowledgeInfoDlg = {
    knowledgeInfoData : {},
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '知识名称不能为空'
                },
            }
        },
        category: {
            validators: {
                notEmpty: {
                    message: '知识分类不能为空'
                },
            }
        },
    }
};

/**
 * 验证数据是否为空
 */
KnowledgeInfoDlg.validate = function () {
    $('#knowledgeInfoForm').data("bootstrapValidator").resetForm();     //重置表单所有验证规则
    $('#knowledgeInfoForm').bootstrapValidator('validate');              //触发全部验证
    return $("#knowledgeInfoForm").data('bootstrapValidator').isValid();//获取当前表单验证状态
};

/**
 * 清除数据
 */
KnowledgeInfoDlg.clearData = function() {
    this.knowledgeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
KnowledgeInfoDlg.set = function(key, val) {
    this.knowledgeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
KnowledgeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
KnowledgeInfoDlg.close = function() {
    parent.layer.close(window.parent.Knowledge.layerIndex);
}

/**
 * 收集数据
 */
KnowledgeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('keyWord')
    .set('category')
    .set('status')
    .set('accessory')
}

/**
 * 提交添加
 */
KnowledgeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();
    this.knowledgeInfoData['content'] = UE.getEditor('editor').getContent();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/knowledge/add", function(data){
        if (data.code == "200") {
            Feng.success("添加成功!");
            window.parent.Knowledge.table.reload('managerTable');
            KnowledgeInfoDlg.close();
        } else if (data.code == "201") {
            Feng.error(data.message);
        }

    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.knowledgeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
KnowledgeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    this.knowledgeInfoData['content'] = UE.getEditor('editor').getContent();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/knowledge/update", function(data){
        if (data.code == "200") {
            Feng.success("修改成功!");
            window.parent.Knowledge.table.reload('managerTable');
            KnowledgeInfoDlg.close();
        } else {
            Feng.error(data.message);
        }

    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.knowledgeInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("knowledgeInfoForm", KnowledgeInfoDlg.validateFields);
    if ($('#editor').length > 0) {
        var ue = UE.getEditor('editor', {
            wordCount:true, //开启字数统计
            elementPathEnabled : false,//是否启用元素路径，默认是显示
            maximumWords:10000,       //允许的最大字符数
            initialContent:'请填写知识内容',    //初始化编辑器的内容,也可以通过textarea/script给值，看官网例子
            autoClearinitialContent:true, //是否自动清除编辑器初始内容，注意：如果focus属性设置为true,这个也为真，那么编辑器一上来就会触发导致初始化的内容看不到了
            pasteplain:true,  //是否默认为纯文本粘贴。false为不使用纯文本粘贴，true为使用纯文本粘贴
        });

        ue.ready(function () {//编辑器初始化完成再赋值
            if ($("#content_val").length > 0) {
                var content_val = $('#content_val').html();
                ue.setContent(content_val);  //赋值给UEditor
            }
        });
    }
});


layui.use('upload', function(){
    var upload = layui.upload;
    var layer = layui.layer;    // 弹层

    //执行实例
    var uploadInst = upload.render({
        elem: '#uploadAccessory' //绑定元素
        ,url: Feng.ctxPath + '/knowledge/upload-file' //上传接口
        ,accept: 'file' //允许上传的文件类型
        ,exts: 'pdf|doc'
        ,size:10 * 1024 *1024
        ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            layer.load(); //上传loading
        }
        ,done: function(res){
            if (res.code == 200) {
                layer.closeAll("loading");
                layer.msg(res.message,function () {
                    $('#accessory').val(res.data);
                    // $('#show_file').html(res.data);
                    $('#show_file').html("<span>"+res.data + "</span>" + "&nbsp;&nbsp;&nbsp;"+
                        "<i class='fa fa-trash-o file-delete' style='color: #ff2222;cursor:pointer'  title='删除附件'></i></div>")
                });
            } else {
                layer.msg(res.message,function () {
                    layer.closeAll("loading");
                });
            }
            //上传完毕回调
        }
        ,error: function(){
            layer.msg("上传失败",function () {
                layer.closeAll("loading");
            });
        }
    });
});

/* 点击删除按钮，删除上传的文档*/
$(document).on("click", ".file-delete", function () {
    var that = $(this);
    layer.confirm("确定要删除附件？", function(index){
        that.siblings().remove();
        that.remove();
        $("#accessory").val("");
        layer.close(index);
    });
});

/**
 * 详情页下载附件
 */
$('.download-file').on('click',function () {
    var fileName = $(this).attr("url");
    if (fileName != '') {
        window.location.href = Feng.ctxPath + "/knowledge/download-file?fileName=" + fileName;
    } else {
        layer.msg("该知识暂没上传附件！");
    }
})
