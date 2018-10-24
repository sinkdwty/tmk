/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var UserInfoDlg = {
    userInfoData: {},
    validateFields: {
        account: {
            validators: {
                notEmpty: {
                    message: '账户不能为空'
                }
            }
        },
        name: {
            validators: {
                notEmpty: {
                    message: '姓名不能为空'
                }
            }
        },
        citySel: {
            validators: {
                notEmpty: {
                    message: '所属公司不能为空'
                }
            }
        },
        productid: {
            validators: {
                notEmpty: {
                    message: '所属项目不能为空'
                }
            }
        },
        // birthday: {
        //     validators: {
        //         notEmpty: {
        //             message: '请填写出生日期'
        //         }
        //     }
        // },
        password: {
            validators: {
                notEmpty: {
                    message: '密码不能为空'
                },
                identical: {
                    field: 'rePassword',
                    message: '两次密码不一致'
                },
            }
        },
        rePassword: {
            validators: {
                notEmpty: {
                    message: '密码不能为空'
                },
                identical: {
                    field: 'password',
                    message: '两次密码不一致'
                },
            }
        },
        roleid : {
            validators: {
                notEmpty: {
                    message: '角色不能为空'
                },
            }
        },
        phone : {
            validators: {
                regexp: {
                    regexp: /^1[3|4|5|6|7|8]{1}[0-9]{9}$/,
                    message: '请输入正确的手机号码'
                }
            }
        },
        email: {
            validators: {
                stringLength: {
                    min: 0,
                    max: 45,
                    message: '请输入有效的邮件地址'
                },
            }
        },
    }
};

/**
 * 清除数据
 */
UserInfoDlg.clearData = function () {
    this.userInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoDlg.set = function (key, val) {
    this.userInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
UserInfoDlg.close = function () {
    parent.layer.close(window.parent.MgrUser.layerIndex);
};

/**
 * 点击所属公司input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
UserInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#citySel").attr("value", instance.getSelectedVal());
    $("#deptid").attr("value", treeNode.id);

    //查找该公司下面的项目
    var ajax = new $ax(Feng.ctxPath + "/product/selectByDeptId", function (data) {
        if (data.code == 200) {
            if (data.data == '') {
                layer.msg(data.message);
                // $("#productId").hide();
            } else {
                var html = '<option value="">请选择</option>';

                $.each(data.data, function (index, val) {
                    html += '<option value="'+ val.id +'">'+ val.name +'</option>';
                })
                $('#productid').html(html);
                // $("#productId").show();
                renderForm();
            }

        } else {
            layer.msg(data.message);
            // $("#productId").hide();
        }
    }, function (data) {
        // $("#productId").hide();
    });
    ajax.set({'deptId':$.trim($("#deptid").val())});
    ajax.start();

};

//重新渲染表单
function renderForm(){
    layui.use(["form"], function(){
        var form = layui.form;//高版本建议把括号去掉，有的低版本，需要加()
        form.render();
    });
}

/**
 * 显示所属公司选择的树
 *
 * @returns
 */
UserInfoDlg.showDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityOffset = $("#citySel").offset();
    $("#menuContent").css({
        left: cityOffset.left + "px",
        top: cityOffset.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

/**
 * 显示用户详情所属公司选择的树
 *
 * @returns
 */
UserInfoDlg.showInfoDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityPosition = $("#citySel").position();
    $("#menuContent").css({
        left: cityPosition.left + "px",
        top: cityPosition.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

/**
 * 隐藏所属公司选择的树
 */
UserInfoDlg.hideDeptSelectTree = function () {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
};

/**
 * 收集数据
 */
UserInfoDlg.collectData = function () {
    this.set('id').set('account').set('sex').set('password').set('avatar')
        .set('email').set('name').set('birthday').set('rePassword').set('deptid').set('phone').set('productid');
};

/**
 * 验证两个密码是否一致
 */
UserInfoDlg.validatePwd = function () {
    var password = this.get("password");
    var rePassword = this.get("rePassword");
    if (password == rePassword) {
        return true;
    } else {
        return false;
    }
};

/**
 * 验证数据是否为空
 */
UserInfoDlg.validate = function () {
    $('#userInfoForm').data("bootstrapValidator").resetForm();
    $('#userInfoForm').bootstrapValidator('validate');
    return $("#userInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加用户
 */
UserInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    // if (!this.validate()) {
    //     return;
    // }

    var account = $.trim($("#account").val());
    var name = $.trim($("#name").val());
    var password = $.trim($("#password").val());
    var rePassword = $.trim($("#rePassword").val());
    var citySel = $.trim($("#citySel").val());
    var id_arr  = $.trim($("#roleid").val());
    // var id_arr = layui.formSelects.value('roleid', 'val');
    var productid = $("#productid").val();

    if (account == "") {
        layer.msg("账号不能为空!");
        return false;
    }
    if (name == "") {
        layer.msg("姓名不能为空!");
        return false;
    }
    if (password == "") {
        layer.msg("密码不能为空!");
        return false;
    }
    if (rePassword == "") {
        layer.msg("确认密码不能为空!");
        return false;
    }
    if (rePassword == "") {
        layer.msg("确认密码不能为空!");
        return false;
    }
    if (citySel == "") {
        layer.msg("公司不能为空!");
        return false;
    }
    if (productid == "") {
        layer.msg("项目不能为空，请选择所属公司下的项目!");
        return false;
    }
    if(id_arr.length == 0) {
        layer.msg("角色不能为空！");
        return false;
    }

    // this.userInfoData.roleid = id_arr.join(',');
    this.userInfoData.roleid = id_arr;

    if (!this.validatePwd()) {
        Feng.error("两次密码输入不一致");
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgr/add", function (data) {
        window.parent.layer.closeAll();
        Feng.success("添加成功!");
        parent.layui.table.reload('managerTable');
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
UserInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    var id_arr  = $.trim($("#roleid").val());
    // var id_arr = layui.formSelects.value('roleid', 'val');
    if(id_arr.length == 0) {
        layer.msg("角色不能为空！");
        return false;
    }
    // this.userInfoData.roleid = id_arr.join(',');
    this.userInfoData.roleid = id_arr;

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgr/edit", function (data) {
        if (window.parent.MgrUser != undefined) {
            window.parent.layer.closeAll();
            Feng.success("修改成功!");
            parent.layui.table.reload('managerTable');
        } else {
            Feng.success("修改成功!");
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userInfoData);
    ajax.start();
};

/**
 * 修改密码
 */
UserInfoDlg.chPwd = function () {
    var ajax = new $ax(Feng.ctxPath + "/mgr/changePwd", function (data) {
        Feng.success("修改成功!");
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set("oldPwd");
    ajax.set("newPwd");
    ajax.set("rePwd");
    ajax.start();

};

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
            event.target).parents("#menuContent").length > 0)) {
        UserInfoDlg.hideDeptSelectTree();
    }
}

$(function () {
    Feng.initValidator("userInfoForm", UserInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(UserInfoDlg.onClickDept);
    ztree.init();
    instance = ztree;

    //初始化性别选项
    $("#sex").val($("#sexValue").val());

    // 初始化头像上传
    var avatarUp = new $WebUpload("avatar");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init();


});
