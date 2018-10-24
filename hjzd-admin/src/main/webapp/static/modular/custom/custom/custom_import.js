//选择文件
$("#upload").on("click", function () {
    $(".webuploader-element-invisible").trigger("click");
});

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

// 文件上传过程中创建进度条实时显示。
webUploader.on( 'uploadProgress', function( file, percentage ) {
    var $li = $( '#'+file.id ),
        $percent = $li.find('.progress .progress-bar');

    // 避免重复创建
    if ( !$percent.length ) {
        $percent = $('<div class="progress progress-striped active">' +
            '<div class="progress-bar" role="progressbar" style="width: 0%">' +
            '</div>' +
            '</div>').appendTo( $li ).find('.progress-bar');
    }

    $li.find('p.state').text('上传中');

    $percent.css( 'width', percentage * 100 + '%' );
});

webUploader.on("uploadSuccess", function(file, res ) {
    if (res.code == 200) {
        $("#fileList").html("<div id='" + file.id+ "'><i class='fa fa-file'></i> " + file.name + " " +
            "<input name='filelink[]' class='filelink' value='' type='hidden'><i class='fa fa-trash-o file-delete' style='color: #ff2222;cursor:pointer'></i></div>");
        $("#import").prop("disabled", false).removeClass("layui-btn-disabled");
        $("#" + file.id).find("input").val(res.data)
    } else {
        layer.msg("上传错误！", {icon: 2});
    }
});

//当有文件被添加进队列的时候
webUploader.on("fileQueued", function(file ) {
    $("#fileList").html( '<div id="' + file.id + '" class="item">' +
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
        '</div>' );
    // handleFile(file.source.source);
});

$(document).on("click", ".file-delete", function () {
    $(this).parent().remove();
    webUploader.reset();
});

$("#import").on("click", function () {
    webUploader.reset();
    var filelink = $('.filelink').val();
    var productid = $('#productid').val();

    if (productid == '') {
        layer.msg("请先选择项目！", {icon: 2});
        return false;
    }

    if (filelink == undefined || filelink == null) {
        layer.msg("请先选择文件！", {icon: 2});
        return false;
    }
    layer.load(1);
    $.ajax({
        url: Feng.ctxPath + "/custom/customer_import",
        type: 'post',
        dataType: 'json',
        data: {filelink: filelink, productid:productid, batch_no:$('#batch_no').val()},
        success: function (res) {
            layer.closeAll();
            if (res.code == 200) {
                $(".file-delete").trigger("click");
                $('.filelink').val("");
                layer.msg(res.message,function () {
                    window.parent.close();
                    // window.parent.Custom.table.refresh();
                    window.parent.Custom.table.reload('CustomTable');
                });
                /*layer.confirm("导入成功", function(index){
                    layer.close(index);
                });*/
            } else {
                layer.msg(res.message, {icon: 2});
            }
        },
        error: function (e) {

        }
    })

});

//黑名单上传
$("#black_import").on("click", function () {
    webUploader.reset();
    var filelink = $('.filelink').val();
    var productid = $('#productid').val();

    if (productid == '') {
        layer.msg("请先选择项目！", {icon: 2});
        return false;
    }

    if (filelink == undefined || filelink == null) {
        layer.msg("请先选择文件！", {icon: 2});
        return false;
    }
    layer.load(1);
    $.ajax({
        url: Feng.ctxPath + "/custom/customer_black_import",
        type: 'post',
        dataType: 'json',
        data: {filelink: filelink, productid:productid},
        success: function (res) {
            layer.closeAll();
            if (res.code == 200) {
                $(".file-delete").trigger("click");
                $('.filelink').val("");
                layer.msg(res.message,function () {
                    window.parent.close();
                    // window.parent.Custom.table.refresh();
                    window.parent.Custom.table.reload('CustomTable');
                });
                /*layer.confirm("导入成功", function(index){
                    layer.close(index);
                });*/
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
        // console.info(typeof (result));console.info(result);return false;
        console.table(result);return false;
        var errors = '';
        var lines = result.length;
        var phone_reg = /^1[0-9]{10}$/;
        var phone_arr = new Array();
        var errors1 = '';
        var errors2 = '';
        var errors3 = '';
        var errors_index1 = '';
        var errors_index2 = '';
        var errors_index3 = '';
        if (lines >= 2) {
            if (
                result[0].A != '客户姓名'
                || result[0].B != '手机号码'
                || result[0].C != '邮箱'
                || result[0].D != '地址'
                || result[0].E != '客户来源'
                || result[0].F != '标签'
                || result[0].G != '备注'
            ) {
                errors = '表格头部错误\n';
            } else {
                result.map(function (obj, index) {
                    if (index > 0) {
                        if (
                            obj.A == undefined || obj.A == ''
                            || obj.B == undefined ||  obj.B == ''
                        ) {
                            errors_index1 += index + '，';
                        } else if (!phone_reg.test(obj.B)) {
                            errors_index2 += index + '，';
                        } else {
                            if ($.inArray(obj.B, phone_arr) == -1) {
                                phone_arr.push(obj.B);
                            } else {
                                errors_index3 += index + '，';
                            }
                        }
                    }
                });
                if (errors1 != '') {
                    errors += "第" + errors_index + "条数据（<font style='color: #ff2222'>客户姓名、手机号码</font>）不能为空。\n";
                }
                if (errors2 != '') {
                    errors += "第" + errors_index + "条数据（<font style='color: #ff2222'>手机号码</font>）格式不正确。\n";
                }
                if (errors3 != '') {
                    errors += "第" + errors_index + "条数据（<font style='color: #ff2222'>手机号码</font>）在本表格中已经存在，请重新整理再上传。\n";
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
                $(".file-delete").trigger("click");
                $('.filelink').val("");
            });
        }
    };
    reader.readAsArrayBuffer(file);
}