
var webUploader = WebUploader.create({
    auto : true,
    pick : {
        id : "#btn-pick",
        multiple : false
    },
    accept : {
        title : "选择文件",
        extensions : "xls,xlsx",
        mimeTypes : "*"
    },
    swf : Feng.ctxPath + "/static/js/plugins/webuploader/Uploader.swf",
    disableGlobalDnd : true,
    duplicate : true,
    server : Feng.ctxPath + "/upload/file",
    fileSingleSizeLimit : 100 * 1024 * 1024,
    fileNumLimit: 1
});

webUploader.on("uploadError", function(file) {
    layer.msg("上传出错！", {icon: 2});
});

webUploader.on("error", function( handler ) {
    if (handler === "Q_TYPE_DENIED") {
        layer.msg("请选择正确的文件格式！", {icon: 2});
    } else if(handler === "Q_EXCEED_NUM_LIMIT"){
        layer.msg("已选择文件！", {icon: 2});
    } else if(handler === "F_EXCEED_SIZE"){
        layer.msg("文件太大了！", {icon: 2});
    } else if (handler == "F_DUPLICATE") {
        layer.msg("文件选择重复！", {icon: 2});
    } else {
        layer.msg("上传错误！", {icon: 2});
    }
});

webUploader.on("uploadSuccess", function(file, res ) {
    if (res.code == 200) {
        $("#" + file.id).find("input").val(res.data)
    } else {
        layer.msg("上传错误！", {icon: 2});
    }
});

webUploader.on("fileQueued", function(file ) {
    $("#fileList").html("<div id='" + file.id+ "'><i class='fa fa-file'></i> " + file.name + " " +
        "<input name='filelink[]' class='filelink' value='' type='hidden'><i class='fa fa-trash-o file-delete' style='color: #ff2222;cursor:pointer'></i></div>");
    handleFile(file.source.source);
});

$("#upload").on("click", function () {
    $(".webuploader-element-invisible").trigger("click");
});

$(document).on("click", ".file-delete", function () {
    $(this).parent().remove();
    webUploader.reset();
});

$("#import").on("click", function () {
    webUploader.reset();
    var filelink = $('.filelink').val();

    if (filelink == undefined || filelink == null) {
        layer.msg("请先选择文件！", {icon: 2});
        return false;
    }
    layer.load(1);
    $.ajax({
        url: Feng.ctxPath + "/ticket_my/ticket_import",
        type: 'post',
        dataType: 'json',
        data: {filelink: filelink},
        success: function (res) {
            layer.closeAll();
            if (res.code == 200) {
                $('.filelink').val("");
                layer.confirm("总记录：" + res.data.totals + "条；成功：" + res.data.success + "条；失败：" + res.data.error + "条；", function(index){
                    layer.close(index);
                });
            } else {
                layer.msg(res.message, {icon: 2});
            }
        },
        error: function (e) {

        }
    })

});


function handleFile(file) {
    var reader = new FileReader();
    reader.onload = function (e) {
        var data = e.target.result;
        var workbook;
        var arr = fixdata(data);

        workbook = XLSX.read(btoa(arr), {type: 'base64'});

        var result = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[workbook.SheetNames[0]], {header:"A"});
        var errors = '';
        if (result.length >= 2) {
            if (result.length > 1001) {
                errors += '一次导入工单条数最多为1000条\n';
            } else {
                if (
                    result[0].A != '工单标题'
                    || result[0].B != '工单描述'
                    || result[0].C != '客户姓名'
                    || result[0].D != '客户手机'
                    || result[0].E != '状态'
                    || result[0].F != '优先级'
                    || result[0].G != '受理坐席组'
                    || result[0].H != '受理坐席'
                    || result[0].I != '标签'
                    || result[0].J != '关注者'
                ) {
                    errors = '表格头部错误';
                } else {
                    result.map(function (obj, index) {
                        if (index > 0) {
                            if (
                                obj.A == undefined || obj.A == ''
                                || obj.B == undefined ||  obj.B == ''
                                || obj.C == undefined ||  obj.C == ''
                                || obj.D == undefined ||  obj.D == ''
                                || obj.E == undefined ||  obj.E == ''
                                || obj.F == undefined ||  obj.F == ''
                            ) {
                                errors += index + '，';
                            }
                        }
                    });
                    if (errors != '') {
                        errors = "第" + errors + "条数据（<font style='color: #ff2222'>工单标题、工单描述、客户姓名、客户手机、状态、优先级</font>）不能为空。";
                    }
                }
            }
        } else {
            errors = "数据表不能为空";
        }

        if (errors == '') {
            layer.tips('点击上传', '#import');
            $("#import").prop("disabled", false).removeClass("layui-btn-disabled");
        } else {
            $("#import").prop("disabled", true).addClass("layui-btn-disabled");
            layer.confirm(errors, function(index){
                layer.close(index);
            });
        }
    };
    reader.readAsArrayBuffer(file);
}