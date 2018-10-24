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
//设置查询结果总条数
var table_search_count;

var curr_page = 1;

/**
 * 初始化表格的列
 */
layui.use(['form', 'table', 'laydate','element'], function ()
{
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;    // 日期
    Custom.table = table;

    // 执行一个 table 实例
    table.render({
        elem: '#CustomTable'
        , height: 420
        , url: Feng.ctxPath + "/custom/list"    // 数据接口
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
            {title: '客户状态', field: 'customStatus',  align: 'center', valign: 'middle',width:120},
            // {title: '客户公司', field: 'company',  align: 'center', valign: 'middle',width:160},
            {title: '所属坐席', field: 'username',  align: 'center', valign: 'middle',width:120},
            // {title: '联系地址', field: 'address',  align: 'center', valign: 'middle',width:200},
            // {title: '负责人', field: 'principal',  align: 'center', valign: 'middle',width:100},
            {title: '标签', field: 'label',  align: 'center', valign: 'middle',width:160},
            {title: '客户来源', field: 'customSource',  align: 'center', valign: 'middle',width:120},
            {title: '首次联系时间', field: 'firstContactTime',  align: 'center', valign: 'middle',width:160},
            {title: '联系次数', field: 'contactTimes',  align: 'center', valign: 'middle',width:90},
            {title: '客户备注', field: 'note',  align: 'center', valign: 'middle',width:160},
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
        limits: [10, 20, 50, 100,200,500],
        done: function(res, curr, count){
            //设置全部数据到全局变量
            table_data_arr = res.data;

            //设置查询结果总条数
            table_search_count = count;

            curr_page = curr;

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
            $('.reserveDiv').css('display', 'block');
            $('#call_status_id').val(value);
            if (value == '') {
                $("#call_status_id").html('');
                form.render();
            }
        } else {
            $('.call_status_Div').css('display', 'initial');
            $('.reserveDiv').css('display', 'none');
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
                    isCall: $('#isCall').val(),
                    call_status_id: call_status_id
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

    // add by eric 2018-09-13  若要改弹框改成tab 显示，此处不用
    $("#create-custom").on('click',function () {
        var name = "添加客户"; // tab 名称
        var url = Feng.ctxPath + '/custom/custom_add';
        viewDetail(name,url);
        // add by eric 2018-09-14 start
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
         **/
        // add by eric 2018-09-14 end
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


    table.on('tool(custom)', function(obj) {    // 注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;   // 获得当前行数据
        var layEvent = obj.event;    // 获得 lay-event 对应的值
        if(layEvent === 'edit'){
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
        } else if(layEvent === 'openCustom') {  //客户详情页
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
        } else if (layEvent === 'addCustom') { // 客户新增页 add by eric 2018-09-13 start
            var name = "添加客户"; // tab 名称
            var url = Feng.ctxPath + '/custom/custom_add';
            viewDetail(name,url);
            // 客户新增页 add by eric 2018-09-13 end
        } else if(layEvent === 'send_mail') {
            localStorage.setItem('send',1);
            var name = data['customName'];
            var id = data['id'];
            var index = layer.prompt({title: '请输入需要发送的邮箱(<span style="color: red;">多账户请用英文逗号“,”隔开</span>):', btn: ['发送', '取消'], formType: 0,shift:-1}, function(pass, index){
                var re = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
                var i = layer.load(2);
                $(".layui-layer-btn0").addClass("layui-btn-disabled");
                if(localStorage.getItem('send') == 0) {
                    layer.close(i);
                    return false;
                }
                // return false;
                setTimeout(function () {
                    if(!re.test(pass)) {
                        layer.close(i);
                        alert("输入邮箱格式有误！");
                        return false;
                    } else {
                        localStorage.setItem('send',0);
                        var ajax = new $ax(Feng.ctxPath + "/custom/sendMail", function (data) {
                            layer.close(i);
                            Feng.success("转发成功!");
                        }, function (data) {
                            layer.close(i);
                            Feng.error("转发失败!" + data.responseJSON.message + "!");
                        });
                        ajax.set("customId",id);
                        ajax.set("mailId",pass);
                        ajax.start();
                        layer.close(index);
                    }
                },50)
            });
            Custom.layerIndex = index;
        }

        return false;

    });

    /* 点击案件操作按钮 */
    $('.case_button').on('click', function () {
        var settingIds = '';
        var url = Feng.ctxPath + "/custom/recycle" ;
        var del_url = Feng.ctxPath + "/custom/batchDel";
        var _this = $(this);
        console.log(ids_arr)
        if (ids_arr.length == 0) {
            layer.msg('请选择要操作客户！', {icon: 5});
            return false;
        }
        settingIds = ids_arr.join(',');
        $(this).attr('disabled', true).addClass('disabled');
        /* 回收案件 */
        if ($(this).hasClass('recycle')) {
            layer.confirm('确定回收选中的案件吗？', function (index) {
                layer.close(index);
                var _index = layer.load(1);
                $.post(url, {ids: ids_arr}, function (res) {
                    layer.close(_index);
                    if (res.code == '200') {
                        ids_arr.splice(0,ids_arr.length);//清空数组
                        layer.msg(res.message, {icon: 1}, function () {
                            table.reload('CustomTable');
                        });
                    } else {
                        layer.msg(res.message, {icon: 5});
                    }
                });
            });
            // 恢复按钮点击
            _this.removeAttr('disabled').removeClass('disabled');
        } else if ($(this).hasClass('del_case')) {
            layer.confirm('确定删除选中的案件吗?（此操作将删除案件下所有关联数据）', {icon: 3, title: '删除案件'}, function (index) {
                layer.close(index);
                var _index = layer.load(1);
                $.post(del_url, {ids: ids_arr}, function (res) {
                    layer.close(_index);
                    if (res.code == '200') {
                        ids_arr.splice(0,ids_arr.length);//清空数组
                        layer.msg(res.message, {icon: 1}, function () {
                            table.reload('CustomTable');
                        });
                    } else {
                        layer.msg(res.message, {icon: 5});
                    }
                });
            });
            // 恢复按钮点击
            _this.removeAttr('disabled').removeClass('disabled');
        } else if ($(this).hasClass('fast_batchOp1')) {    //快速分案
            var way = 'fast_batchOp1';    //设置标识，按钮calss

            assign_custom_html(_this, way);
        } else if ($(this).hasClass('fast_batchAuto')) {    //快速自动分案
            var way = 'fast_batchAuto';    //设置标识，按钮calss

            assign_custom_html(_this, way);
        } else if ($(this).hasClass('sendMsg')) {
            var url = Feng.ctxPath +"/custom/to_sendMoreMessage/";
            _this.removeAttr('disabled').removeClass('disabled');
            layer.open({
                title: "发送短信",
                type: 2,
                area: ['90%', '95%'],
                //点击遮罩关闭
                shadeClose: true,
                content: url,

                scrollbar: false,
            })
        } else if ($(this).hasClass('batchMsg')) {
            var url = Feng.ctxPath +"/custom/to_sendBatchMessage?way=sendBatchMessage";
            _this.removeAttr('disabled').removeClass('disabled');
            layer.open({
                title: "批次短信",
                type: 2,
                area: ['90%', '95%'],
                //点击遮罩关闭
                shadeClose: true,
                content: url,

                scrollbar: false,
            })
        }
    });

    /* 查询分案操作按钮 */
    $('.search_case_button').on('click', function () {
        var _this = $(this);
        if (table_data_arr.length == 0) {
            layer.msg('查询结果为空无需操作！', {icon: 5});
            return false;
        }
        if ($(this).hasClass('searchFast')) {       //查询结果快速分案
            var way = "opListAll"; //设置标识，按钮calss
            assign_custom_html(_this, way);
        } else if ($(this).hasClass('searchAuto')) {        //查询结果自动分案
            var way = "fast_batchOp2";    //设置标识，按钮calss
            /* 以坐席和小组进行分类 */
            assign_custom_html(_this, way)
        }else if ($(this).hasClass('searchSend')) {        //查询结果自动分案
            var url = Feng.ctxPath +"/custom/to_sendSearchMessage/";
            _this.removeAttr('disabled').removeClass('disabled');
            layer.open({
                title: "发送短信",
                type: 2,
                area: ['800px', '350px'],
                //点击遮罩关闭
                shadeClose: true,
                content: url,

                scrollbar: false,
            })
        } else if ($(this).hasClass('searchBatchMsg')) {        //查询结果自动分案
            var url = Feng.ctxPath +"/custom/to_sendBatchMessage?way=searchBatchMessage";
            _this.removeAttr('disabled').removeClass('disabled');
            layer.open({
                title: "批次短信",
                type: 2,
                area: ['90%', '95%'],
                //点击遮罩关闭
                shadeClose: true,
                content: url,

                scrollbar: false,
            })
        }
    })

    /**
     * 分案弹出框
     * @param _this
     * @param way
     * @returns {*}
     */
    function assign_custom_html(_this, way) {
        var satr = layer.msg('请选择分案方式', {
            time: 0 //不自动关闭
            ,shade:0.3
            , btn: ['坐席', '取消']
            , yes: function (index) {
                _this.removeAttr('disabled').removeClass('disabled');
                layer.close(satr)
                layer.open({  //弹出详细页面
                    type: 2,
                    title: '坐席列表',
                    skin: 'demo-class',
                    area: ['50%', '70%'],
                    tips: 2,
                    closeBtn: 1,
                    content: Feng.ctxPath + "/custom/assign-custom" + '?way='+way, //这里content是一个普通的String
                    cancel: function (index, layero) {
                        layer.closeAll('page'); //关闭所有页面层

                    }
                });
            }, btn2: function () {
                _this.removeAttr('disabled').removeClass('disabled');
                layer.closeAll(); //关闭所有页面层
            }
        });
        return satr;
    }

    //分案方法
    window.allotCase = function (id, way) {
        var url = Feng.ctxPath + "/custom/assign-custom-ensure";
        var allData = {};
        if (way == 'opListAll' || way == 'fast_batchOp2') {     //查询分案
            allData = {user_id: id, way: way};
        } else {    //快速分案
            if (way == 'fast_batchAuto') {  //快速自动分案
                var ids_length = ids_arr.length;
                var userIds_length = id.split(",").length;
                if (ids_length < userIds_length) {
                    layer.alert("案件数量少于分配坐席数量，请重新分配！");
                    return false;
                }
            }
            allData = {ids: ids_arr, user_id: id, way: way};
        }

        var _index = layer.load(1);
        $.post(url, allData, function (res) {
            layer.close(_index);
            if (res.code == '200') {
                layer.msg(res.message, {icon: 1}, function () {
                    ids_arr.splice(0,ids_arr.length);//清空数组
                    table.reload('CustomTable');
                });
            } else {
                layer.alert("案件数量少于分配坐席数量，请重新分配！");
                // layer.msg(res.message, {icon: 5});
            }
        });
    }

    //短信方法
    window.sendMassage = function (id, way) {
        var url = Feng.ctxPath + "/custom/assign-message-batch";
        var allData = {};
        if (way == 'sendBatchMessage') {     //查询分案
            allData = {ids: ids_arr.join(','),batch_id: id, way: way};
        } else if (way == 'searchBatchMessage') {    //快速分案
            allData = { batch_id: id, way: way};
        }

        var _index = layer.load(1);
        $.post(url, allData, function (res) {
            layer.close(_index);
            if (res.code == '1') {
                layer.msg(res.msg, {icon: 1}, function () {
                    ids_arr.splice(0,ids_arr.length);//清空数组
                    table.reload('CustomTable');
                });
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        });
    }

    $('#customNote').blur(function () {
        var note = $(this).val().trim();
        var customId = $("#customId").val();

        var old_note = $('#old_note').val();
        if (note == old_note) {
            return false;
        }
        if (customId > 0) {
            var index = layer.load(2);
            $.ajax({
                url: Feng.ctxPath + '/custom/handle_custom',
                method: "post",
                dataType: 'json',
                data: {'note': note, 'id': customId},
                success: function (res) {
                    layer.close(index);
                    layer.msg(res.message);
                    if (res.code == 200) {
                        $('#old_note').val(note);
                        table.reload('operateLogTable');
                    }
                }
            })
        } else {
            layer.msg("系统异常");
        }
    });


    //标签
    $('#customTags').tagsInput({
        'height': '30px', //设置高度
        'width': 'auto', //设置宽度
        'interactive': true, //是否允许添加标签，false为阻止
        'defaultText': '请添加标签', //默认文字
        'onAddTag': function (tag) {
            updateCustomtag();

        }, //增加标签的回调函数
        'onRemoveTag': function (tag) {
            updateCustomtag();
        }, //删除标签的回调函数
        'onChange': function (tag) {

        }, //改变一个标签时的回调函数
        'removeWithBackspace': true, //是否允许使用退格键删除前面的标签，false为阻止
        'minChars': 0, //每个标签的小最字符
        'maxChars': 0, //每个标签的最大字符，如果不设置或者为0，就是无限大
        'placeholderColor': '#666666' //设置defaultText的颜色
    });


    function updateCustomtag() {
        var label = $('#customTags').val();
        var customId = $("#customId").val();

        $.ajax({
            url: Feng.ctxPath + '/custom/handle_custom',
            type: 'POST',
            data: {'label': label, 'id': customId},
            dataType: 'JSON',
            success: function (data) {           //成功回调
                if (data.code == 200) {
                    layer.msg(data.message);
                    table.reload('operateLogTable');
                }
            }
        });
    }

    // $(".tag span").on('click',function () {
    var layer_index = '';
    $(document).on('click', '.tag span', function () {
        layer.close(layer_index);
        var value = $(this).text().trim();

        layer_index = layer.open({
            type: 2,
            title: '商品',
            area: ['50%', '100%'],
            fix: true,
            maxmin: true,
            offset: 'r',
            shade: 0,
            content: Feng.ctxPath + '/goods?condition='+value
        });
        return false;
    })

});



/**
 * layui
 */
layui.use(['form', 'table', 'laydate','element'], function ()
{
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;
    var element = layui.element;


    // 执行一个 table 实例
    table.render({
        elem: '#callLogTable'
        , height: 220
        , url: Feng.ctxPath + "/callLog/list"    // 数据接口
        , page: true    // 开启分页
        , size: 'sm'
        ,method:'post'
        , cols: [[    // 表头
            {title: '序号', type: 'numbers', align:'center',width:60},
            {title: '坐席姓名', field: 'userName',  align: 'center', valign: 'middle',width:100},
            {title: '客户姓名', field: 'customName',  align: 'center', valign: 'middle',width:80},
            {title: '手机号码', field: 'customPhone',  align: 'center', valign: 'middle',width:120,
                templet:function (value) {
                    var len = value.customPhone;
                    var ruten = len.substring(3,7); //提取字符串下标之间的字符。
                    return len.replace(ruten,'****'); //字符串中用字符替换另外字符，或替换一个与正则表达式匹配的子串。

                }
            },
            {title: '致电结果', field: 'callStatusName',  align: 'center', valign: 'middle',width:150},
            {title: '呼叫开始时间', field: 'callStartTime',  align: 'center', valign: 'middle',width:150},
            {title: '呼叫结束时间', field: 'callEndTime',  align: 'center', valign: 'middle',width:150},
            {title: '本次呼叫总秒数', field: 'callSecond',  align: 'center', valign: 'middle',width:120},
            {title: '分机号', field: 'callAgentNo',  align: 'center', valign: 'middle',width:80},
            {title: '电话小结', field: 'note',  align: 'center', valign: 'middle',width:150},
            {title: '创建时间', field: 'createdAt',  align: 'center', valign: 'middle',width:150},

        ]],
        // initSort: {
        //     field: 'createtime' //排序字段，对应 cols 设定的各字段名
        //     ,type: 'desc' //排序方式 asc: 升序、desc: 降序、null: 默认排序
        // },
        where: {order: 'desc',caseId:$("#customId").val()},    // 定义一个默认排序方式，与java后台对接
        request: {
            pageName: 'page',    // 页码的参数名称，默认：page
            limitName: 'limit'    // 每页数据量的参数名，默认：limit
        },
        limits: [10, 20, 50, 100, 200, 500],

    });


    laydate.render({
        elem: '#reserveTime' //注册渲染时间日期
        ,type: 'datetime'
    });

    (function(){
        /**
         * 搜索条件显示隐藏
         *
         * @param    {boolean}  mark        标志性布尔值，用于切换显示隐藏
         * @param    {string}   clickdom    需要点击的dom元素
         * @param    {string}   dom         需要绑定的dom元素父类
         * @param    {number}   queryWidth  媒体查询的宽度
         * @returns  obj
         *
         */
        function loadMore (obj){
            var marker = obj.mark;
            queryWidth();
            $(obj.clickdom).on('click', function(){
                if(!marker) {
                    var conut = $('.height-container .layui-col-xs12').length;
                    if(  $(window).width() >= 768 && $(window).width() < 992 ) {
                        var finalHeight = Math.ceil(conut / 2) * 46;
                        $('.height-container').css({
                            transition: 'all .3s',
                            height: finalHeight + 'px'
                        })
                    } else if ( $(window).width() < 768 ) {
                        var finalHeight = conut * 46;
                        $('.height-container').css({
                            transition: 'all .3s',
                            height: finalHeight + 'px'
                        })
                    }

                    setTimeout(function(){
                        $( obj.dom + ' .need-hide').css("cssText","display: block!important");
                    },300);

                    $(this).find('i').css({
                        transition: 'all .5s',
                        display: 'inline-block',
                        transform:'rotate(180deg)'
                    });
                    marker = true;
                } else {
                    queryWidth();
                    $( obj.dom + ' .need-hide').css("cssText","display: none!important");
                    $(this).find('i').css({
                        transition: 'all .5s',
                        display: 'inline-block',
                        transform:'rotate(0deg)'
                    });
                    marker = false;
                }
            });
            $(window).resize(function(){
                if($(window).width() >= obj.queryWidth) {
                    $( obj.dom + ' .need-hide').removeAttr('style');
                    $( obj.clickdom ).find('i').removeAttr('style');
                    $( '.height-container' ).removeAttr('style');
                    marker = false;
                }
                if(  $(window).width() >= 768 && $(window).width() < 992 ) {
                    if(marker == true) {
                        $('.load-more').trigger('click');
                    } else {
                        $('.height-container').css({
                            height: '46px'
                        })
                    }
                } else if ( $(window).width() < 768 ) {
                    if(marker == true) {
                        $('.load-more').trigger('click');
                    }
                }
            })
        }

        function queryWidth(){
            if(  $(window).width() >= 768 && $(window).width() < 992 ) {
                $('.height-container').css({
                    height: '46px'
                })
            } else if ( $(window).width() < 768 ) {
                $('.height-container').css({
                    height: '92px'
                })
            }
        }

        /*
        *  弹框iframe逻辑执行后重新渲染表格
        *
        *  参数 ID 即为基础参数id对应的值
        *  参数 options 即为各项基础参数
        *
        */
        function randerTable(id,option){
            parent.layui.table.reload(id,option);
        }

        /*-----电话详情页input Ajax改变基本信息-----*/
        function editUserInfo(url,caseNo,callback){
            var oldData = '';
            var _time = null;
            var clickEvent = function(){
                var that = this;
                clearTimeout(_time);
                _time = setTimeout(function(){
                    if($(that).find('input').val() == '') {
                        return;
                    }
                    // $(that).parent().siblings('.hover-div').show();
                }, 200);
            }
            $('.layui-form').on('click','i.close-tip', function(){
                $(this).parent().hide();
            });
            $('.db-container').on('click', clickEvent);
            $('.db-container').on('dblclick', function(){
                if ($(this).find('input').attr('name') == "phone") {
                    layer.msg("抱歉，暂不支持修改手机号码！");
                    return false;
                }
                $('.db-container').unbind('click');
                clearTimeout(_time);
                var t = ($(this).find('input').val()).trim();
                oldData = t;
                var domInput = $(this).find('input');
                domInput.removeAttr('readonly');
                domInput.css({
                    'border':'1px solid #e6e6e6'
                });
                domInput.val("");
                domInput.focus().val(t);
            });
            $('.db-container input').on('blur', function(){

                /* 过滤单击离开事件 */
                if($(this).attr('readonly') == 'readonly') {
                    return false;
                }
                $('.db-container').on('click', clickEvent);
                var newData = ($(this).val()).trim();
                $(this).css('border', 0);
                $(this).attr('readonly', 'readonly');
                if(newData == oldData) {
                    return;
                }
                var index = layer.load(2);
                var upurl = url;
                var data = {};
                var that = $(this);
                data.name = $(this).attr('name');
                data.value = $(this).val();
                data.case_no = caseNo;
                $.post(upurl, data, function(res){
                    layer.close(index);
                    if(res.code == '200') {
                        layer.msg(res.message);
                        that.attr('title',data.value);
                        that.attr('value',data.value);
                        callback && callback(res);
                    } else {
                        layer.msg(res.message);
                    }
                }, 'json');
            });
        }
        return CommonApi = {
            loadMore: loadMore,
            randerTable: randerTable,
            editUserInfo : editUserInfo
        }
    })();


    /*-----修改表单input内容参数-----*/
    var caseNo = $("#customId").val();
    var editUrl = Feng.ctxPath + "/custom/edit-nx-custom"    // 数据接口

    /*-----修改表单input内容-----*/
    CommonApi.editUserInfo(editUrl, caseNo, function(res){
        form.render();
    });

    $('.base-info-container').on('click',function(e) {
        // e.preventDefault();
        var saveSelect = function () {
            $.ajax({
                url: editUrl,
                method: "post",
                dataType: 'json',
                data: data,
                success: function (res) {
                    layer.close(index);
                    layer.msg(res.message);
                    successfunction();
                }
            })
        }
        //客户学历
        if (!$('.education-select').hasClass('isShow')) {
            var inputTarget = $(e.target).parent().parent().parent();
            if (inputTarget.hasClass('education-select')) {
                return false;
            }
            var education = $('select[name="education"]').val();

            if (!education) {
                $('.education-select').addClass('isShow');
                $('.education').removeClass('isShow');
                return false;
            }
            var data = {name: 'customEducationBackground', 'value': education, 'case_no': caseNo};
            var successfunction = function () {
                $('.education-select').addClass('isShow');
                $('.education').removeClass('isShow').find('input').val(education);
            }
            index = layer.load(2);
            saveSelect();
        }

        //客户性别
        if (!$('.sex-select').hasClass('isShow')) {
            var inputTarget = $(e.target).parent().parent().parent();
            if (inputTarget.hasClass('sex-select')) {
                return false;
            }
            var sex = $('select[name="sex"]').val();

            if (!sex) {
                $('.sex-select').addClass('isShow');
                $('.sex').removeClass('isShow');
                return false;
            }
            var data = {name: 'sex', 'value': sex, 'case_no': caseNo};
            var successfunction = function () {
                $('.sex-select').addClass('isShow');
                $('.sex').removeClass('isShow').find('input').val(sex);
            }
            index = layer.load(2);
            saveSelect();
        }

        //是否持有他行卡
        if (!$('.having-other-card-select').hasClass('isShow')) {
            var inputTarget = $(e.target).parent().parent().parent();
            if (inputTarget.hasClass('having-other-card-select')) {
                return false;
            }
            var havingOtherCard = $('select[name="havingOtherCard"]').val();

            if (!havingOtherCard) {
                $('.having-other-card-select').addClass('isShow');
                $('.having-other-card').removeClass('isShow');
                return false;
            }
            var data = {name: 'havingOtherCard', 'value': havingOtherCard, 'case_no': caseNo};
            var successfunction = function () {
                $('.having-other-card-select').addClass('isShow');
                $('.having-other-card').removeClass('isShow').find('input').val(havingOtherCard);
            }
            index = layer.load(2);
            saveSelect();
        }

        //卡片使用时间
        if (!$('.card-use-time-select').hasClass('isShow')) {
            var inputTarget = $(e.target).parent().parent().parent();
            if (inputTarget.hasClass('card-use-time-select')) {
                return false;
            }
            var cardUseTime = $('select[name="cardUseTime"]').val();

            if (!cardUseTime) {
                $('.card-use-time-select').addClass('isShow');
                $('.card-use-time').removeClass('isShow');
                return false;
            }
            var data = {name: 'cardUseTime', 'value': cardUseTime, 'case_no': caseNo};
            var successfunction = function () {
                $('.card-use-time-select').addClass('isShow');
                $('.card-use-time').removeClass('isShow').find('input').val(cardUseTime);
            }
            index = layer.load(2);
            saveSelect();
        }

        //是否企业主股东
        if (!$('.is-corporat-select').hasClass('isShow')) {
            var inputTarget = $(e.target).parent().parent().parent();
            if (inputTarget.hasClass('is-corporat-select')) {
                return false;
            }
            var isCorporateShareholders = $('select[name="isCorporateShareholders"]').val();

            if (!isCorporateShareholders) {
                $('.is-corporat-select').addClass('isShow');
                $('.is-corporat').removeClass('isShow');
                return false;
            }
            var data = {name: 'isCorporateShareholders', 'value': isCorporateShareholders, 'case_no': caseNo};
            var successfunction = function () {
                $('.is-corporat-select').addClass('isShow');
                $('.is-corporat').removeClass('isShow').find('input').val(isCorporateShareholders);
            }
            index = layer.load(2);
            saveSelect();
        }
    });

    //客户学历
    $('.education').on('dblclick',function(){
        $(this).addClass('isShow');
        $('.education-select').removeClass('isShow')
    });
    //客户性别
    $('.sex').on('dblclick',function(){
        $(this).addClass('isShow');
        $('.sex-select').removeClass('isShow')
    });
    //是否持有他行卡
    $('.having-other-card').on('dblclick',function(){
        $(this).addClass('isShow');
        $('.having-other-card-select').removeClass('isShow')
    });
    //卡片使用时间
    $('.card-use-time').on('dblclick',function(){
        $(this).addClass('isShow');
        $('.card-use-time-select').removeClass('isShow')
    });
    //是否企业主股东
    $('.is-corporat').on('dblclick',function(){
        $(this).addClass('isShow');
        $('.is-corporat-select').removeClass('isShow')
    });

    //导出客户
    $('#export-custom').on("click", function () {
        var page = curr_page;
        var limit = 10000;
        var temp_page = parseInt(table_search_count/limit) + 1;
        if(temp_page<page) {
            layer.alert("数据导出每次10000条，总页数为"+temp_page+"，导出页选择错误！");
            return false;
        }

        var condition = $("#condition").val();
        var beginTime = $('#beginTime').val();
        var endTime = $('#endTime').val();
        var column = $("#search_column option:selected").val();
        var status = $('#status').val();
        var update_beginTime = $('#update_beginTime').val();
        var update_endTime = $('#update_endTime').val();
        var call_status_id = -1;
        var isCall = $("#isCall").val() != null ? $("#isCall").val():0;
        if ($('#call_status_top option:selected').text() =="预约联系") {
            call_status_id = $('#call_status_top').val();
        } else {
            if ($('#call_status_top option:selected').val() =="-1") {
                call_status_id = $('#call_status_top').val();
            }else{
                call_status_id = $('#call_status_id').val()
            }
        }

        window.location.href=Feng.ctxPath + "/custom/export-custom" + "?column="+column+"&condition="+condition+"&beginTime="+beginTime+"&endTime="+endTime+
            "&status="+status+"&isCall="+isCall+"&update_beginTime="+update_beginTime+"&update_endTime="+update_endTime+"&call_status_id="+call_status_id+"&page="+page+"&limit="+limit;
        return false;
    })

    //黑名单处理
    $('#blackList').on("click", function () {
        var index = layer.open({
            type: 2,        //iframe层
            title: '黑名单数据处理',
            area: ['80%', '67%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/custom/custom_black_list'
        });
        Custom.layerIndex = index;
        return false;
    })

});










