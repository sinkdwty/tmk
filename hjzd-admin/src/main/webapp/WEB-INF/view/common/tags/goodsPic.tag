@/*
商品图片参数的说明:
name : 名称
id : 图片的id
@*/
<div class="form-group">
    <label class="col-sm-3 control-label">${name}</label>
    <div class="col-sm-4">
        <div id="${id}PreId">
            <div>
                      @if(isEmpty(avatarImg)){
                <img width="" height=""
                      src=""></div>
            @}else{
            <img width="100px" height="100px"
            src="${ctxPath}/kaptcha/${avatarImg}"></div>
        @}
    </div>
    <div class="layui-btn-sm upload-btn layui-col-sm1" id="${id}BtnId" style="padding-top: 5px;"><i class="fa fa-upload"></i>&nbsp;点击上传</div>
</div>

<input type="hidden" id="${id}" value="${avatarImg!}"/>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
<div class="hr-line-dashed"></div>
@}


