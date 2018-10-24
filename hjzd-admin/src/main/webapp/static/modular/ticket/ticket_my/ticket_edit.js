layui.use(["element", "layedit", "form"], function () {
    var formSelects = layui.formSelects;
    var element = layui.element;
    var layedit = layui.layedit;
    var form = layui.form;
    var $ = layui.$;

    //构建一个默认的编辑器
    layedit.set({
        uploadImage: {
            url: Feng.ctxPath + '/upload/img' //接口url
        }
    });

    var index = layedit.build('content', {
        tool: ['strong','italic','underline' ,'del' ,'|' ,'left','center','right','link','unlink']
    });

    form.on('select(assignDeptId)', function(data){
        if (assignDeptId != data.value) {
            $("#customName").val('');
            $("#customId").val('');
            $("#assignUserId").val('');
            $("#assignUserName").val('');
            assignDeptId = data.value;
        }
        $("#assignDeptName").val($("#assignDeptId").find("option:selected").text());
    });

    form.on('submit(edit)', function(data){
        data.field.accessory = '';
        data.field.content = layedit.getContent(index);
        $(".accessorys").each(function(){
                data.field.accessory += $(this).val() + ",";
            }
        );

        $.ajax({
            url: Feng.ctxPath + '/ticket_my/ticket_save',
            type: 'post',
            data: data.field,
            dataType: 'json',
            success: function (res) {
                if (res.code == 200) {
                    Feng.success(res.message);
                    parent.layui.table.reload('ticketTable');
                    table.reload('ticketNoteTable');
                } else {
                    Feng.error(res.message);
                }
            },
            error: function (e) {
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
});

var webUploader = WebUploader.create({
    auto : true,
    pick : {
        id : "#btn-pick",
        multiple : true
    },
    accept : {
        title : "选择文件",
        extensions : "*",
        mimeTypes : "*"
    },
    swf : Feng.ctxPath + "/static/js/plugins/webuploader/Uploader.swf",
    disableGlobalDnd : true,
    duplicate : true,
    server : Feng.ctxPath + "/upload/file",
    fileSingleSizeLimit : 100 * 1024 * 1024,
    fileNumLimit: 10
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
    layer.closeAll();
});

webUploader.on("fileQueued", function(file ) {
    layer.load(1);
    $("#fileList").append("<div id='" + file.id+ "'><i class='fa fa-file'></i> " + file.name + " " +
        "<input name='accessorys[]' class='accessorys' value='' type='hidden'>" +
        "<i class='fa fa-download file-view' style='color: #00B83F;cursor:pointer' title='下载附件'></i> " +
        "<i class='fa fa-trash-o file-delete' style='color: #ff2222;cursor:pointer'  title='删除附件'></i></div>")
});

$("#file").on("click", function () {
    $(".webuploader-element-invisible").trigger("click");
});

$(document).on("click", ".file-delete", function () {
    var that = $(this);
    layer.confirm("确定要删除附件？", function(index){
        that.parent().remove();
        layer.close(index);
    });
});

$(document).on("click", ".file-view", function () {
    var that = $(this);
    layer.confirm("确定下载附件？", function(index){
        var input = that.parent().find("input");
        window.open(Feng.ctxPath + '/upload/download?fileName=' + input.val());
        layer.close(index);
    });
});

initFiles();

function initFiles() {
    var accessorys = accessory.split(',');

    for (var i in accessorys) {
        if (accessorys[i].length > 1) {
            var names = (accessorys[i]).split('/');
            $("#fileList").append("<div id='" + i + "'><i class='fa fa-file'></i> " + names[1] + " " +
                "<input name='accessorys[]' class='accessorys' value='" + accessorys[i] +"' type='hidden'>" +
                "<i class='fa fa-download file-view' style='color: #00B83F;cursor:pointer' title='下载附件'></i> " +
                "<i class='fa fa-trash-o file-delete' style='color: #ff2222;cursor:pointer' title='删除附件'></i></div>")
        }
    }
}

$('#tags').tagsInput({defaultText: '', width: '100%'});

$('#selectCustomId').on('click', function () {
    var assignDeptId = $("#assignDeptId").val();
    if (assignDeptId == '') {
        layer.msg("请先选择所属公司", {icon: 2});
        return false;
    }
    layer.open({
        type: 2,
        title: '选择客户',
        area: ['80%', '80%'],
        fix: true,
        maxmin: true,
        shade: 0,
        content: Feng.ctxPath + "/ticket_my/select_custom?deptId=" + assignDeptId
    });
    return false;
});

$('#selectUserId').on('click', function () {
    var assignDeptId = $("#assignDeptId").val();
    if (assignDeptId == '') {
        layer.msg("请先选择所属公司", {icon: 2});
        return false;
    }
    layer.open({
        type: 2,
        title: '选择受理人',
        area: ['80%', '80%'],
        fix: true,
        maxmin: true,
        shade: 0,
        content: Feng.ctxPath + "/ticket_my/select_user?deptId=" + assignDeptId
    });
    return false;
});

$('#selectFollows').on('click', function () {
    var assignDeptId = $("#assignDeptId").val();
    var follows = $("#follows").val();
    if (assignDeptId == '') {
        layer.msg("请先选择所属公司", {icon: 2});
        return false;
    }
    layer.open({
        type: 2,
        title: '选择关注人',
        area: ['80%', '80%'],
        fix: true,
        maxmin: true,
        shade: 0,
        content: Feng.ctxPath + "/ticket_my/select_follows?follows=" + follows + "&deptId=" + assignDeptId
    });
    return false;
});