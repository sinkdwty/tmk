layui.use(["table", "element", "layedit", "form"], function () {
    var table = layui.table;
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

    var note = layedit.build('note', {
        tool: ['strong','italic','underline' ,'del' ,'|' ,'left','center','right','link','unlink']
    });

    form.on('submit(note)', function(data){
        var content = layedit.getContent(note);

        if (content.length == 0) {
            Feng.error("记录不能为空");
            return false
        }
        $.ajax({
            url: Feng.ctxPath + '/ticket_my/ticket_note',
            type: 'post',
            data: {note: content, ticketId: ticketId},
            dataType: 'json',
            success: function (res) {
                if (res.code == 200) {
                    Feng.success(res.message);
                    table.reload('ticketNoteTable');
                    layedit.setContent(note, "")
                } else {
                    Feng.error(res.message);
                }
            },
            error: function (e) {
                console.log(e)
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });


    table.render({
        elem: '#ticketNoteTable',
        height: 440,
        url: Feng.ctxPath + "/ticket_my/ticket_note_list",    // 数据接口
        page: true,    // 开启分页
        size: 'sm',
        method:'post',
        cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center', width: 60},
            {title: '坐席', field: 'userName', align: 'center', valign: 'middle', width: 100},
            {title: '记录', field: 'note', align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createdAt', align: 'center', valign: 'middle', width: 160},
        ]],
        where: {
            ticketId: ticketId,
            order: 'desc'
        }
    });

    $(".btn-status").on("click", function () {
        var status = $(this).data("status");

        if (status > 0) {
            Feng.confirm("是否修改工单状态", function () {
                $.ajax({
                    url: Feng.ctxPath + '/ticket_my/ticket_status',
                    type: 'post',
                    data: {status: status, ticketId: ticketId},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code == 200) {
                            Feng.success(res.message);
                            table.reload('ticketNoteTable');
                        } else {
                            Feng.error(res.message);
                        }
                    },
                    error: function (e) {}
                });
            });

        } else if (status == -9) {
            Feng.confirm("是否删除工单", function () {
                $.ajax({
                    url: Feng.ctxPath + '/ticket_my/ticket_delete',
                    type: 'post',
                    data: {id: ticketId},
                    dataType: 'json',
                    success: function (res) {
                        if (res.code == 200) {
                            Feng.success(res.message);
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index); //关闭自身
                        } else {
                            Feng.error(res.message);
                        }
                    },
                    error: function (e) {}
                });
            });
        } else if (status == 0) {

        }
    });
});