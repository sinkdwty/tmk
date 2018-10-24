/**
 * 客户管理管理初始化
 */
var Custom = {
    id: "CustomTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

//记录选中的数据:做缓存使用,作为参数传递给后台,
var ids_arr = new Array();
//当前表格中的全部数据:在表格的checkbox全选的时候没有得到数据, 因此用全局存放变量
var table_data_arr = new Array();

/**
 * 初始化表格的列
 */
layui.use(['form', 'table', 'laydate','element'], function ()
{
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;    // 日期
    var element = layui.element;
    Custom.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#CustomTable'
        , height: 420
        , url: Feng.ctxPath + "/custom/my-custom-list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        , method:'get',
        cols:[[
            {type:'checkbox'},
            {title: '序号', type: 'numbers', align:'center',width:60},
            {title: '客户姓名', field: 'customName',  align: 'center', valign: 'middle',width:120},
            {title: '手机号码', field: 'phone',  align: 'center', valign: 'middle',width:120,
                templet:function (value) {
                    var len = value.phone;
                    let ruten = len.substring(3,7); //提取字符串下标之间的字符。
                    return len.replace(ruten,'****'); //字符串中用字符替换另外字符，或替换一个与正则表达式匹配的子串。

                }
            },
            {title: '客户状态', field: 'customStatus',  align: 'center', valign: 'middle',width:90},
            // {title: '所属公司', field: 'company',  align: 'center', valign: 'middle',width:160},
            {title: '所属坐席', field: 'username',  align: 'center', valign: 'middle',width:80},
            // {title: '联系地址', field: 'address',  align: 'center', valign: 'middle',width:200},
            // {title: '负责人', field: 'principal',  align: 'center', valign: 'middle',width:100},
            {title: '标签', field: 'label',  align: 'center', valign: 'middle',width:80},
            {title: '客户来源', field: 'customSource',  align: 'center', valign: 'middle',width:120},
            {title: '首次联系时间', field: 'firstContactTime',  align: 'center', valign: 'middle',width:160},
            {title: '联系次数', field: 'contactTimes',  align: 'center', valign: 'middle',width:75},
            {title: '客户备注', field: 'note',  align: 'center', valign: 'middle',width:160},
            {title: '电话小结', field: 'callNote',  align: 'center', valign: 'middle',width:160},
            {title: '批次编号', field: 'batchNo',  align: 'center', valign: 'middle',sortable: true,width:160},
            {title: '创建时间', field: 'createdTime',  align: 'center', valign: 'middle',sortable: true,width:160},
            {title: '更新时间', field: 'updatedTime',  align: 'center', valign: 'middle',sortable: true,width:160},
            {fixed: 'right', title: '操作', align:'center', toolbar:'#operation',class:"exportClass",width:210}

        ]],
        where: {order: 'desc'},    // 定义一个默认排序方式，与java后台对接
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 20, 50, 100, 200, 500],
        done: function(res, curr, count){
            //设置全部数据到全局变量
            table_data_arr = res.data;

            //在缓存中找到id ,然后设置data表格中的选中状态
            //循环所有数据，找出对应关系，设置checkbox选中状态
            for (var i = 0; i < res.data.length; i++) {
                for (var j = 0; j < ids_arr.length; j++) {
                    //数据id和要勾选的id相同时checkbox选中
                    if (res.data[i].id == ids_arr[j]) {
                        //这里才是真正的有效勾选
                        res.data[i]["LAY_CHECKED"] = 'true';
                        //找到对应数据改变勾选样式，呈现出选中效果
                        var index = res.data[i]['LAY_TABLE_INDEX'];

                        $('.layui-table-view tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
                        $('tr[data-index=' + index + '] .layui-form-checkbox').addClass('layui-form-checked');
                    }
                }
            }
        }

    });

    //复选框选中监听,将选中的id 设置到缓存数组,或者删除缓存数组
    table.on('checkbox(custom)', function (obj) {
        if(obj.checked==true){
            if(obj.type=='one'){
                ids_arr.push(obj.data.id);
            }else{
                // ids.splice(0,ids.length);//清空数组
                // checked_phones.splice(0,checked_phones.length);//清空数组
                for(var i=0;i<table_data_arr.length;i++){
                    ids_arr.push(table_data_arr[i].id);
                }
            }
        }else{
            if(obj.type=='one'){
                for(var i=0;i<ids_arr.length;i++){
                    if(ids_arr[i]==obj.data.id){
                        ids_arr.splice(i, 1);
                    }
                }
            }else{
                for(var i=0;i<ids_arr.length;i++){
                    for(var j=0;j<table_data_arr.length;j++){
                        if(ids_arr[i]==table_data_arr[j].id){
                            ids_arr.splice(i, 1);
                        }
                    }
                }
            }
        }
    });

    laydate.render({
        elem: '#beginTime',    // 注册渲染时间日期
        type: 'datetime',
        range: false
    });

    laydate.render({
        elem: '#endTime',    // 注册渲染时间日期
        type: 'datetime',
        range: false
    });

    laydate.render({
        elem: '#update_beginTime',    // 注册渲染时间日期
        type: 'datetime',
        range: false
    });

    laydate.render({
        elem: '#update_endTime',    // 注册渲染时间日期
        type: 'datetime',
        range: false
    });


    form.on('select(call-select)', function (res) {
        var value = res.value;
        var valueName = res.elem[res.elem.selectedIndex].text;
        if (valueName.indexOf("预约联系") >= 0) {
            $('.call_status_Div').css('display', 'none');
            $('#call_status_id').val(value);
            if (value == '') {
                $("#call_status_id").html('');
                form.render();
            }
        } else {
            $('.call_status_Div').css('display', 'initial');

            $.ajax({
                url:  Feng.ctxPath + "/custom/get_call_status" ,
                method: "post",
                data: {value: value},
                success: function (data)
                {
                    var result = "<option value=''>请选择</option>";
                    if (data) {
                        $.each(data.data, function (idx, obj) {
                            result +="<option value='"+obj.id+"'>"+obj.name+"</option>";
                        });
                    }
                    $("#call_status_id").html(result);
                    form.render();
                }
            })
        }
    });

    form.on('select(call-again)', function (res) {
        var valueName = res.elem[res.elem.selectedIndex].text;
        var value = res.value;
    });

    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            var condition = $('#condition');
            var beginTime = $('#beginTime');
            var endTime = $('#endTime');
            var update_beginTime = $('#update_beginTime');
            var update_endTime = $('#update_endTime');
            var call_status_id = "";
            if ($('#call_status_top option:selected').text() =="预约联系") {
                call_status_id = $('#call_status_top').val();
            } else {
                if ($('#call_status_top option:selected').val() =="-1") {
                   call_status_id = $('#call_status_top').val();
                }else{
                   call_status_id = $('#call_status_id').val()
                }
            }
            // add by eric 2019-10-10 start 增加质检状态筛选
            var check_status = $('#check_status');

            if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
                Feng.error("创建时间选择范围不正确!");
                return false;
            }

            if ((update_beginTime.val() > update_endTime.val()) || (update_beginTime.val() == '' && update_endTime.val() != '')) {
                Feng.error("更新时间选择范围不正确!");
                return false;
            }
            if(call_status_id == ''){ // add by eric 2018-09-26 start
               Feng.error("请选择致电结果二级类型!");
               return false;
            }

            // 执行重载
            table.reload('CustomTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    condition: condition.val(),
                    beginTime: beginTime.val(),
                    endTime: endTime.val(),
                    update_beginTime: update_beginTime.val(),
                    update_endTime: update_endTime.val(),
                    column: $("#search_column option:selected").val(),
                    status: $('#status').val(),
                    call_status_id:call_status_id,
                    check_status: check_status.val()
                }
            });
        },
        parseTable:function () {

            var checkStatus = table.checkStatus('CustomTable'),
                data = checkStatus.data;
            if (data.length > 0) {

                var resultData = new Array();
                    resultData[0] = [
                        "客户姓名", "手机号码", "邮箱", "客户公司", "联系地址", "负责人", "标签",
                        "客户来源", "首次联系时间", "联系次数", "客户备注", "创建时间", "更新时间"
                    ],

                    $.each(data, function (idx, obj) {
                        resultData[idx + 1] = [
                            obj.customName, obj.phone, obj.email, obj.company, obj.address, obj.principal, obj.label,
                            obj.customSource, obj.firstContactTime, obj.contactTimes, obj.note, obj.createdTime, obj.updatedTime
                        ];
                    });

                var wb = XLSX.utils.book_new();
                var ws = XLSX.utils.aoa_to_sheet(resultData);

                ws['!cols'] = [
                    {wch: 10},
                    {wch: 18},
                    {wch: 20},
                    {wch: 25},
                    {wch: 30},
                    {wch: 10},
                    {wch: 20},
                    {wch: 10},
                    {wch: 20},
                    {wch: 10},
                    {wch: 30},
                    {wch: 20},
                    {wch: 20},
                ];

                XLSX.utils.book_append_sheet(wb, ws, "Sheet1");
                /* Trigger Download with `writeFile` */
                XLSX.writeFile(wb, "客户导出.xlsx", {compression: true});
            } else {
                layer.msg("请勾选导出数据");
            }

        },reset:function () {
            // 重置输入内容
                $(this).parents('.layui-form').find('.resetInput input').val('');
                $('select').val('-1');
                return false;

        }
    };
    $('.search-btn,.export-btn,.reset-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';

        return false;
    });

    // add by eric 2018-09-13  弹框改成tab 显示 start
    $("#create-custom").on('click',function () {
        var name = "添加客户"; // tab 名称
        var url = Feng.ctxPath + '/custom/custom_add';
        viewDetail(name,url);

        /**
        var index = layer.open({
            type: 2,
            title: '添加客户',
            area: ['80%', '70%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/custom/custom_add'
        });
        Custom.layerIndex = index;
        return false;
         */
        // add by eric 2018-09-13  弹框改成tab 显示 end
    });


    $("#upload-custom").on('click',function () {
        var index = layer.open({
            type: 2,        //iframe层
            title: '客户导入',
            area: ['80%', '67%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/custom/custom_upload'
        });
        Custom.layerIndex = index;
        return false;
    });

    /**
     * 监听Tab切换，以改变地址
     * 2018-09-21  chenhexiang
     */
    element.on('tab(customTab)', function() {
        var type = $(this).data('type');
        $(".reset-btn").parents(".layui-form").find(".resetInput input").val('');

        // 执行重载
        table.reload('CustomTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                type: type,
            }
        });
    });

    table.on('tool(custom)', function(obj) {    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值
        if(layEvent === 'edit') {
            var id = data['id'];
            var index = layer.open({
                type: 2,
                title: '编辑客户',
                area: ['80%', '70%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/custom/custom_update/' + id
            });
            Custom.layerIndex = index;
        }else if (layEvent === 'addCustom') {  // add by eric 2018-09-13 start
            var name = "添加客户"; // tab 名称
            var url = Feng.ctxPath + '/custom/custom_add';
            viewDetail(name,url);
            // add by eric 2018-09-13 start
        } else if(layEvent === 'delete'){
            var operation = function(){
                var id = data['id'];    // 获取到当前行的id值
                var ajax = new $ax(Feng.ctxPath + "/custom/delete", function () {
                    Feng.success("删除成功!");
                    table.reload('CustomTable');
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                    table.reload('CustomTable');
                });
                ajax.set("customId", id);
                ajax.start();
            };

            Feng.confirm("是否删除" + data['customName'] + "?",operation);
        } else if(layEvent === 'openCustom') {
            var id = data['id'];
            var title = data['customName'] ? data['customName'] : data['phone'].substr(0,3)+"****"+data['phone'].substr(7);
            var url = Feng.ctxPath + '/custom/custom_detail/' + id;
            viewDetail(title, url);
            /*var name = data['customName'];
            var id = data['id'];
            var index = layer.open({
                type: 2,
                title: name,
                area: ['100%', '100%'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/custom/custom_detail/' + id
            });
            Custom.layerIndex = index;*/
        } else if(layEvent === 'send_mail') {
            var name = data['customName'];
            var id = data['id'];
            var index = layer.prompt({title: '请输入需要发送的邮箱(<span style="color: red;">多账户请用英文逗号“,”隔开</span>):', btn: ['发送', '取消'], formType: 0}, function(pass, index){
                var re = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
                if(!re.test(pass)) {
                    alert("输入邮箱格式有误！")
                    return false;
                } else {
                    var ajax = new $ax(Feng.ctxPath + "/custom/sendMail", function (data) {
                        Feng.success("转发成功!");
                    }, function (data) {
                        Feng.error("转发失败!" + data.responseJSON.message + "!");
                    });
                    ajax.set("customId",id);
                    ajax.set("mailId",pass);
                    ajax.start();

                    layer.close(index);
                }
            });
            Custom.layerIndex = index;
        }

        return false;

    });

    //导出客户
    $('#export-custom').on("click", function () {
        var condition = $("#condition").val();
        var beginTime = $('#beginTime').val();
        var endTime = $('#endTime').val();
        var column = $("#search_column option:selected").val();
        // var status = $('#status').val();
        var update_beginTime = $('#update_beginTime').val();
        var update_endTime = $('#update_endTime').val();
        var type = $('.search-btn').data('type');
        console.info(type);return false;
        window.location.href=Feng.ctxPath + "/custom/export-custom" + "?column="+column+"&condition="+condition+"&beginTime="+beginTime+"&endTime="+endTime+
            "&update_beginTime="+update_beginTime+"&update_endTime="+update_endTime+"&type="+type;
        return false;
    })

});





