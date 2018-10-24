//记录选中的数据:做缓存使用,作为参数传递给后台,
var ids_arr = new Array();
//当前表格中的全部数据:在表格的checkbox全选的时候没有得到数据, 因此用全局存放变量
var table_data_arr = new Array();

var way = $('input[name="way"]').val();
layui.use(['form', 'table'], function ()
{
    var table = layui.table;
    var form = layui.form;
    //执行一个 table 实例
    if(way == 'opListAll' || way == 'fast_batchOp1') {
        column = [[ //表头
                {
                    title: '', width: 100,templet: '#radioTpl', unresize: true
                },
                {
                    field: 'account', title: '坐席账号'
                },  {
                    field: 'name', title: '坐席姓名'
                }
            ]];

    } else {
        column = [[ //表头
                {
                    field: 'id',  title: '',type:'checkbox'
                },
                {
                    field: 'account', title: '坐席账号'
                },  {
                    field: 'name', title: '坐席姓名'
                }
            ]];
    }
    table.render({
        elem: '#userTable',
        height: 'full-150',
        url: Feng.ctxPath + "/custom/assign-custom-list", //数据接口
        page: true, //开启分页
        cols: column,
        limits: [10, 50, 100, 200, 500, 1000],
        request: {
            pageName: 'page', //页码的参数名称，默认：page
            limitName: 'limit' //每页数据量的参数名，默认：limit
        },
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
    table.on('checkbox(table-filter)', function (obj) {
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
        ids_arr = unique_arr(ids_arr);
    });
    function unique_arr(arr){
        var hash=new Array();
        for (var i = 0; i < arr.length; i++) {
            for (var j = i+1; j < arr.length; j++) {
                if(arr[i]===arr[j]){
                    ++i;
                }
            }
            hash.push(arr[i]);
        }
        return hash;
    }

    //表格头部按钮操作
    var $ = layui.$, active = {
        search: function() {
            table.reload('userTable', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    name : $.trim($('#userName').val())
                },
            });
        },
    };


    $('.search-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    $('.ensure-assign-custom').click(function () {
        if(way == 'opListAll' || way == 'fast_batchOp1') {
            var radioCheked = $('input[type="radio"]:checked').val();
            if (radioCheked  == undefined) {
                layer.msg('请选择分案坐席',{icon: 5});
            } else {
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
                parent.allotCase(radioCheked,way);
            }
        } else {
            /*var checkStatus = table.checkStatus('userTable');
            var data = checkStatus.data;
            if(data.length == 0 ) {
                layer.msg('请选择分案坐席',{icon:5});
                return false;
            }
            var selIds = '';
            data.forEach(function (obj) {
                selIds += obj.id + ','
            });

            if (selIds  == '') { */
            if (ids_arr.length  == 0) {
                layer.msg('请选择分案坐席',{icon: 5});
            } else {
                selIds = ids_arr.join(",");
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
                parent.allotCase(selIds,way);
            }
        }
    });

});