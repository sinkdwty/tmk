layui.use(['form', 'layedit', 'laydate', 'element'], function(){
    var form = layui.form
        ,layer = layui.layer;

    form.on('select(SmsModel)',function(data){
        var modelId = data.value;

        var model = $('#SmsModel').find('option[value='+modelId+']').html();

        $('#message').val(model);

    })

    //监听提交
    form.on('submit(demo1)', function(data){

        var message = data.field.message.trim();
        if(message == '') {
            layer.msg('短信内容不能为空！')
            return false;
        }
        if(way == "sendMoreMessage") {
            var url = Feng.ctxPath +"/custom/sendMoreMessage/";
            var id = window.parent.ids_arr.join(',');
        } else if(way == "sendSearchMessage") {
            var url = Feng.ctxPath +"/custom/sendSearchMessage/";
            var id = true;
        } else {
            var url = Feng.ctxPath +"/custom/sendMessage/";
            var id = $("#case_id").val();
        }
        if(!id) {
            layer.msg('页面错误！');
            return false;
        }
        $(this).attr('disabled', true);
        var index = layer.msg('发送中', {
            icon: 16
            ,shade: 0.01
        });
        $.ajax({
            url: url,
            type: 'POST', //GET
            async: true,    //或false,是否异步
            data: {'customId':id,'message':message},
            dataType: 'json',    //返回的数据格式：json/xml/html/script/jsonp/text
            beforeSend: function (xhr) {    //请求相应之前方法
            },
            success: function (data) {    //请求成功
                layer.close(index)
                //弹出返回信息
                if(data.code == '1') {
                    layer.msg(data.message);
                    setTimeout(function () {
                        window.parent.layer.closeAll();
                    },1000);
                } else {
                    $('.send-btn').attr('disabled', false);
                    window.parent.layer.msg('发送失败！<br>'+data.message)
                }

            },
            error: function () {
                layer.close(index)
                layer.msg("发送失败！");
            }
        })
    });

});
