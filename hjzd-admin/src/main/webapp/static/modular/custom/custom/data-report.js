
/**
 * 初始化表格的列
 */
layui.use(['form', 'table', 'laydate','element'], function ()
{
    var form = layui.form;
    var table = layui.table;
    var laydate = layui.laydate;    // 日期

    laydate.render({
        elem: '#beginTime',    // 注册渲染时间日期
        type: 'datetime',
    });

    laydate.render({
        elem: '#endTime',    // 注册渲染时间日期
        type: 'datetime',
    });

    //二级表头
    table.render({
        elem: "#dataTable",
        url: '/data-report/data-report',
        cellMinWidth: 80,
        page:false,
        height:440,
        cols:  [[ //标题栏
            {align: 'center', title: '人员属性', colspan: 2}, //colspan即横跨的单元格数，这种情况下不用设置field和width
            {align: 'center', title: '数据模块', colspan: 4},
            {align: 'center', title: '业绩指标', colspan: 11},
            {align: 'center', title: '过程指标', colspan: 4},
        ], [
            {type: 'numbers', width:120,fixed: 'left', title: '组别'},
            {field:'person_account', width:120,fixed:true, title: '坐席工号'},

            {field:'data_source', width:120, title: '数据渠道'},
            {field:'data_batch', width:150, title: '数据批次'},
            {field:'data_importer', width:120, title: '导入人'},
            {field:'data_import_time', width:200, title: '导入时间'},

            {field:'performance_get_nums', width:120, title: '派发名单数'},
            {field:'performance_called_nums', width:120, title: '已拨打名单数'},
            {field:'performance_succ_called_nums', width:120, title: '名单接通数'},
            {field:'performance_success_nums', width:120, title: '成功单'},
            {field:'performance_success_rate', width:145, title: '名单成功率（%）',
                templet: function (data) {
                    if (data.performance_succ_called_nums != null || data.performance_succ_called_nums != 0) {
                        if (data.performance_success_nums == null || data.performance_success_nums == 0) {
                            return 0;
                        } else {
                            return (data.performance_success_nums/data.performance_succ_called_nums* 100).toFixed(2);
                        }
                    } else {
                        return 0;
                    }
                }
            },

            {field:'total_call_second', width:120, title: '总通时（秒）',
                templet: function (data) {
                    if (data.total_call_second == null || data.total_call_second == 0) {
                        return 0;
                    } else {
                        return data.total_call_second;
                    }
                }
            },
            {field:'performance_callLog_nums', width:140, title: '名单拨打总次数'},
            {field:'data_succ_callLog_num', width:100, title: '通次'},
            {field:'data_fail_call_num', width:120, title: '未接通次数'},
            {field:'data_succ_call_rate ', width:145, title: '接通成功率（%）',
                templet: function (data) {
                    if (data.performance_callLog_nums != null || data.performance_callLog_nums != 0) {
                        if (data.data_succ_callLog_num == null || data.data_succ_callLog_num == 0) {
                            return 0;
                        } else {
                            return (data.data_succ_callLog_num/data.performance_callLog_nums* 100).toFixed(2);
                        }
                    } else {
                        return 0;
                    }
                }
            },
            {field:'total_ring_second', width:120, title: '总振铃（秒）',
                templet: function (data) {
                    if (data.total_ring_second == null || data.total_ring_second == undefined) {
                        return 0;
                    } else {
                        return data.total_ring_second;
                    }
                }
            },

            {field:'process_man_hour', width:120, title: '工时利用率'},
            {field:'process_avg_answer', width:120, title: '平均接听量'},
            {field:'process_avg_call', width:120, title: '平均外呼量'},
            {field:'process_att', width:120, title: 'ATT'},
        ]]
    });


    // 搜索
    var $ = layui.$, active = {
        reload: function(){
            var beginTime = $('#beginTime');
            var endTime = $('#endTime');

            if ((beginTime.val() > endTime.val()) || (beginTime.val() == '' && endTime.val() != '')) {
                Feng.error("创建时间选择范围不正确!");
                return false;
            }

            // 执行重载
            table.reload('dataTable', {
                url: '/data-report/data-report',
                page: false,
                where: {
                    beginTime: $('#beginTime').val(),
                    endTime: $('#endTime').val(),
                    account: $.trim($('#account').val()),
                    batch_no: $.trim($('#batch_no').val())
                } //设定异步数据接口的额外参数
                //,height: 300
            });
            return false;
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
});










