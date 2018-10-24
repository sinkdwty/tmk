@/*
    时间查询条件标签的参数说明:

    name : 查询条件的名称
    isTime : 日期是否带有小时和分钟(true/false)
    pattern : 日期的正则表达式(例如:"YYYY-MM-DD")
@*/
<div class="col-sm-2" style="padding-right: 0;">
<div class="input-group">
    <div class="input-group-btn">
        <button data-toggle="dropdown" class="btn btn-white dropdown-toggle"
                type="button">${name}
        </button>
    </div>
    <input type="text" class="form-control layer-date"
           onclick="laydate({istime: ${isTime}, format: '${pattern}'})" id="beginTime"/>
</div>
</div>
<div class="col-sm-2" style="padding-left: 0;">
    <div class="input-group">
        <div class="input-group-btn">
            <button class="btn btn-white dropdown-toggle" style="border: none;"
                    type="button">-
            </button>
        </div>
        <input type="text" class="form-control layer-date"
               onclick="laydate({istime: ${isTime}, format: '${pattern}'})" id="endTime"/>
    </div>
</div>