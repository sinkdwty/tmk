/**
 * @link         http://www.hejunzongda.com/
 * @author       yaoliang <yaoliang@hfhjzddata.com>
 * @copyright    Copyright &copy; hjzddata.com, 2015 - 2018
 * @description    asterCC呼叫系统模块
 */

// layui.define(['layer', 'configs'], function(exports) {
layui.define(['layer'], function(exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    // var configs = layui.configs; // 获取配置参数
    var astercc_ip = getCookie("cc_ipaddress");//configs.ipaddress;
    var astercc_cip = '';

    var CJI = {
        // 登录
        loginCJI : function (orgidentity, usertype, user, pwdtype, password, callbackFuc)
        {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            // $.getJSON(astercc_cip + '/setevent/loginCJI?callback=?', {
            //     usertype: usertype,
            //     user: user,
            //     orgidentity: orgidentity,
            //     pwdtype: pwdtype,
            //     password: password,
            // }, function (json) {
            //     try {
            //         if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
            //             callbackFuc(json);
            //         }
            //     } catch (e) {
            //         // alert('loginCJI error!');
            //     }
            // });
            $.ajax({
                cache:false,
                url: astercc_cip + '/setevent/loginCJI?callback=?',
                type:'get',
                data:{
                    usertype: usertype,
                    user: user,
                    orgidentity: orgidentity,
                    pwdtype: pwdtype,
                    password: password,
                },
                dataType:'jsonp',
                success:function (json) {
                    try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        // alert('loginCJI error!');
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });
        }

        // 登出
        ,logoutCJI:function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            console.log(astercc_cip+":"+usertype+":"+user+":"+orgidentity +":"+pwdtype +":"+password)

            $.getJSON(astercc_cip + '/setevent/logoutCJI?callback=?', {
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
                pwdtype: pwdtype,
                password: password,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('logoutCJI error!');
                }
            });
        }


        // 队列接口(分机示忙，闲)
        ,queueActionCJI :function(type, usertype, user, orgidentity, list, pwdtype, password, deviceexten, pushevent, callbackFuc, intparam) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            var pjson = type;
            if (typeof(type) != 'object') {
                pjson = {
                    'type': type,
                    'usertype': usertype,
                    'user': user,
                    'orgidentity': orgidentity,
                    'list': list,
                    'pwdtype': pwdtype,
                    'password': password,
                    'deviceexten': deviceexten,
                    'pushevent': pushevent,
                    'intparam': intparam,
                };
            } else {
                if (typeof(pjson.callbackFuc) != 'undefined' && pjson.callbackFuc != '') {
                    callbackFuc = pjson.callbackFuc;
                    delete pjson.callbackFuc;
                }
            }

            $.getJSON(astercc_cip + '/setevent/queueActionCJI?callback=?', pjson, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('queueActionCJI error!');
                }
            });
        }


        // (暂停/继续)服务
        ,queuePauseCJI : function(type, usertype, user, orgidentity, pwdtype, password, pause_reason, pushevent, callbackFuc, dnd, intparam) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            var pjson = type;
            if (typeof(type) != 'object') {
                pjson = {
                    'type': type,
                    'usertype': usertype,
                    'user': user,
                    'orgidentity': orgidentity,
                    'pwdtype': pwdtype,
                    'password': password,
                    'pause_reason': pause_reason,
                    'pushevent': pushevent,
                    'dnd': dnd,
                    'intparam': intparam,
                };
            } else {
                if (typeof(pjson.callbackFuc) != 'undefined' && pjson.callbackFuc != '') {
                    callbackFuc = pjson.callbackFuc;
                    delete pjson.callbackFuc;
                }
            }
            $.getJSON(astercc_cip + '/setevent/queuePauseCJI?callback=?', pjson, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('queuePauseCJI error!');
                }
            });
        }


        // 切换事后模式
        ,acwActionCJI : function(type, usertype, user, orgidentity, pwdtype, password, agent_group_id, pushevent, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            var pjson = type;
            if (typeof(type) != 'object') {
                pjson = {
                    'type': type,
                    'usertype': usertype,
                    'user': user,
                    'orgidentity': orgidentity,
                    'pwdtype': pwdtype,
                    'password': password,
                    'agent_group_id': agent_group_id,
                    'pushevent': pushevent,
                };
            } else {
                if (typeof(pjson.callbackFuc) != 'undefined' && pjson.callbackFuc != '') {
                    callbackFuc = pjson.callbackFuc;
                    delete pjson.callbackFuc;
                }
            }

            $.getJSON(astercc_cip + '/setevent/acwActionCJI?callback=?', pjson, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('acwActionCJI error!');
                }
            });
        }


        // 结束事后
        ,acwOffCJI :function(usertype, user, orgidentity, pwdtype, password, pushevent, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            var pjson = usertype;
            if (typeof(usertype) != 'object') {
                pjson = {
                    'usertype': usertype,
                    'user': user,
                    'orgidentity': orgidentity,
                    'pwdtype': pwdtype,
                    'password': password,
                    'pushevent': pushevent,
                };
            } else {
                if (typeof(pjson.callbackFuc) != 'undefined' && pjson.callbackFuc != '') {
                    callbackFuc = pjson.callbackFuc;
                    delete pjson.callbackFuc;
                }
            }

            $.getJSON(astercc_cip + '/setevent/acwOffCJI?callback=?', pjson, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('acwOffCJI error!');
                }
            });
        }


        // 切换工作模式
        ,workwayActionCJI :function(status, usertype, user, orgidentity, pwdtype, password, agent_group_id, pushevent, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            var pjson = status;
            if (typeof(status) != 'object') {
                pjson = {
                    'status': status,
                    'usertype': usertype,
                    'user': user,
                    'orgidentity': orgidentity,
                    'pwdtype': pwdtype,
                    'password': password,
                    'agent_group_id': agent_group_id,
                    'pushevent': pushevent,
                };
            } else {
                if (typeof(pjson.callbackFuc) != 'undefined' && pjson.callbackFuc != '') {
                    callbackFuc = pjson.callbackFuc;
                    delete pjson.callbackFuc;
                }
            }

            $.getJSON(astercc_cip + '/setevent/workwayActionCJI?callback=?', pjson, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('workwayActionCJI error!');
                }
            });
        }


        // 呼叫接口
        ,makeCallCJI :function(targetdn, targettype, agentgroupid, usertype, user, orgidentity, pwdtype, password, modeltype, model_id, userdata, callbackFuc, agentexten, callerid, callername, trunkidentity, cidtype, ignorerepeat) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.ajax({
                cache:false,
                url: astercc_cip + '/setevent/makeCallCJI?callback=?',
                type:'get',
                data:{
                    targetdn: targetdn,
                    targettype: targettype,
                    agentgroupid: agentgroupid,
                    usertype: usertype,
                    user: user,
                    orgidentity: orgidentity,
                    pwdtype: pwdtype,
                    password: password,
                    modeltype: modeltype,
                    model_id: model_id,
                    userdata: userdata,
                    agentexten: agentexten,
                    callerid: callerid,
                    callername: callername,
                    trunkidentity: trunkidentity,
                    cidtype: cidtype,
                    ignorerepeat: ignorerepeat,
                },
                dataType:'jsonp',
                success:function (json) {
                    try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        alert('makeCallCJI error!');
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });

            // $.getJSON(astercc_cip + '/setevent/makeCallCJI?callback=?', {
            //     targetdn: targetdn,
            //     targettype: targettype,
            //     agentgroupid: agentgroupid,
            //     usertype: usertype,
            //     user: user,
            //     orgidentity: orgidentity,
            //     pwdtype: pwdtype,
            //     password: password,
            //     modeltype: modeltype,
            //     model_id: model_id,
            //     userdata: userdata,
            //     agentexten: agentexten,
            //     callerid: callerid,
            //     callername: callername,
            //     trunkidentity: trunkidentity,
            //     cidtype: cidtype,
            //     ignorerepeat: ignorerepeat,
            // }, function (json) {
            //     try {
            //         if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
            //             callbackFuc(json);
            //         }
            //     } catch (e) {
            //         alert('makeCallCJI error!');
            //     }
            // });
        }

        // 咨询接口
        ,consultCJI : function(targetdn, agentgroupid, consulttype, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/consultCJI?callback=?', {
                targetdn: targetdn,
                agentgroupid: agentgroupid,
                consulttype: consulttype,
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('consultCJI error!');
                }
            });
        }

        // 盲转接口
        ,bTransferCJI : function(targetdn, agentgroupid, consulttype, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/bTransferCJI?callback=?', {
                targetdn: targetdn,
                agentgroupid: agentgroupid,
                consulttype: consulttype,
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('consultCJI error!');
                }
            });
        }

        // 转接接口
        ,transferCJI : function(pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/transferCJI?callback=?', {
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('transferCJI error!');
                }
            });
        }

        // 接回接口
        ,callReturnCJI : function(pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/callReturnCJI?callback=?', {
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('callReturnCJI error!');
                }
            });
        }

        // 会议接口
        ,conferenceCJI : function (pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/conferenceCJI?callback=?', {
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('conferenceCJI error!');
                }
            });
        }

        // 挂断接口
        ,hangupCJI :function (uniqueid, targetagent, target, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;

            $.ajax({
                cache:false,
                url: astercc_cip + '/setevent/hangupCJI?callback=?',
                type:'get',
                data:{
                    uniqueid: uniqueid,
                    targetagent: targetagent,
                    target: target,
                    pwdtype: pwdtype,
                    password: password,
                    usertype: usertype,
                    user: user,
                    orgidentity: orgidentity,
                },
                dataType:'jsonp',
                success:function (json) {
                    try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        alert('hangupCJI error!');
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });
            // $.getJSON(astercc_cip + '/setevent/hangupCJI?callback=?', {
            //     uniqueid: uniqueid,
            //     targetagent: targetagent,
            //     target: target,
            //     pwdtype: pwdtype,
            //     password: password,
            //     usertype: usertype,
            //     user: user,
            //     orgidentity: orgidentity,
            // }, function (json) {
            //     try {
            //         if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
            //             callbackFuc(json);
            //         }
            //     } catch (e) {
            //         // alert('hangupCJI error!');
            //     }
            // });
        }


        // 强插接口
        ,intrudeCJI :function (target, phonenumber, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/intrudeCJI?callback=?', {
                target: target,
                phonenumber: phonenumber,
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('intrudeCJI error!');
                }
            });
        }

        // 监听接口
        ,silentMonitorCJI :function (target, phonenumber, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/silentMonitorCJI?callback=?', {
                target: target,
                phonenumber: phonenumber,
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('silentMonitorCJI error!');
                }
            });
        }

// 强拆接口
        ,forcedReleaseCJI : function (target, phonenumber, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/forcedReleaseCJI?callback=?', {
                target: target,
                phonenumber: phonenumber,
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('forcedReleaseCJI error!');
                }
            });
        }

        // 密语接口
        ,whisperCJI:function (target, phonenumber, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/whisperCJI?callback=?', {
                target: target,
                phonenumber: phonenumber,
                pwdtype: pwdtype,
                password: password,
                usertype: usertype,
                user: user,
                orgidentity: orgidentity,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('whisperCJI error!');
                }
            });
        }

        // 通话暂停接口
        ,holdCJI : function (silence, orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.ajax({
                cache:false,
                url: astercc_cip + '/setevent/holdCJI?callback=?',
                type:'get',
                data:{
                    silence: silence,
                    orgidentity: orgidentity,
                    usertype: usertype,
                    user: user,
                    pwdtype: pwdtype,
                    password: password,
                },
                dataType:'jsonp',
                success:function (json) {
                    try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        alert('holdCJI error!');
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });
            // $.getJSON(astercc_cip + '/setevent/holdCJI?callback=?', {
            //     silence: silence,
            //     orgidentity: orgidentity,
            //     usertype: usertype,
            //     user: user,
            //     pwdtype: pwdtype,
            //     password: password,
            // }, function (json) {
            //     try {
            //         if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
            //             callbackFuc(json);
            //         }
            //     } catch (e) {
            //         // alert('holdCJI error!');
            //     }
            // });
        }

        // 通话继续接口
        ,resumeCJI : function (orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.ajax({
                cache:false,
                url: astercc_cip + '/setevent/resumeCJI?callback=?',
                type:'get',
                data:{
                        orgidentity: orgidentity,
                        usertype: usertype,
                        user: user,
                        pwdtype: pwdtype,
                        password: password,
                },
                dataType:'jsonp',
                success:function (json) {
                    try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        alert('holdCJI error!');
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });
            // $.getJSON(astercc_cip + '/setevent/resumeCJI?callback=?', {
            //     orgidentity: orgidentity,
            //     usertype: usertype,
            //     user: user,
            //     pwdtype: pwdtype,
            //     password: password,
            // }, function (json) {
            //     try {
            //         if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
            //             callbackFuc(json);
            //         }
            //     } catch (e) {
            //         // alert('resumeCJI error!');
            //     }
            // });
        }

        // 获取团队坐席状态
        ,teamStatusCJI: function (orgidentity, usertype, user, pwdtype, password, status, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/teamStatusCJI?callback=?', {
                status: status,
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('teamStatusCJI error!');
                }
            });
        }

        // 获取坐席组状态
        ,agentgroupStatusCJI: function (orgidentity, usertype, user, pwdtype, password, agent_group_id, status, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/agentgroupStatusCJI?callback=?', {
                agent_group_id: agent_group_id,
                status: status,
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('agentgroupStatusCJI error!');
                }
            });
        }

        // 获取坐席状态接口
        ,agentStatusCJI :function (orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.ajax({
                cache:false,
                url: astercc_cip + '/setevent/agentStatusCJI?callback=?',
                type:'get',
                data:{
                    orgidentity: orgidentity,
                    usertype: usertype,
                    user: user,
                    pwdtype: pwdtype,
                    password: password,
                },
                dataType:'jsonp',
                success:function (json) {
                    try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        // alert('agentStatusCJI error!');
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });
            // $.getJSON(astercc_cip + '/setevent/agentStatusCJI?callback=?', {
            //     orgidentity: orgidentity,
            //     usertype: usertype,
            //     user: user,
            //     pwdtype: pwdtype,
            //     password: password,
            // }, function (json) {
            //     try {
            //         if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
            //             callbackFuc(json);
            //         }
            //     } catch (e) {
            //         // alert('agentStatusCJI error!');
            //     }
            // });
        }

        // 预拨号接口
        ,dialerListCJI :function (orgidentity, usertype, user, pwdtype, password, campaignid, phonenum, priority, dialtime, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/dialerListCJI?callback=?', {
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
                campaignid: campaignid,
                phonenum: phonenum,
                priority: priority,
                dialtime: dialtime,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('agentStatusCJI error!');
                }
            });
        }

        // 数据导入接口
        ,importCJI :function (orgidentity, usertype, user, pwdtype, password, modeltype, model_id, source, context, source_user, source_pwd, exetime, delrow, phone_field, priority_field, dialtime_field, emptyagent, resetstatus, dupway, dupdiallist, changepackage, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/importCJI?callback=?', {
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
                modeltype: modeltype,
                model_id: model_id,
                source: source,
                context: context,
                source_user: source_user,
                source_pwd: source_pwd,
                exetime: exetime,
                delrow: delrow,
                phone_field: phone_field,
                priority_field: priority_field,
                dialtime_field: dialtime_field,
                emptyagent: emptyagent,
                resetstatus: resetstatus,
                dupway: dupway,
                dupdiallist: dupdiallist,
                changepackage: changepackage,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('agentStatusCJI error!');
                }
            });
        }

        // 多段录音控制接口（以坐席为对象，当action为start时，以该时间点为起始，录制独立录音文件，action为stop时录音停止）
        ,monitorCtrlCJI : function (action, usertype, user, orgidentity, pwdtype, password, pushevent, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            var pjson = action;
            if (typeof(action) != 'object') {
                pjson = {
                    'action': action,
                    'usertype': usertype,
                    'user': user,
                    'orgidentity': orgidentity,
                    'pwdtype': pwdtype,
                    'password': password,
                    'pushevent': pushevent,
                };
            } else {
                if (typeof(pjson.callbackFuc) != 'undefined' && pjson.callbackFuc != '') {
                    callbackFuc = pjson.callbackFuc;
                    delete pjson.callbackFuc;
                }
            }
            $.getJSON(astercc_cip + '/setevent/monitorCtrlCJI?callback=?', pjson, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('queuePauseCJI error!');
                }
            });
        }


        // 获取录音存放地址
        ,getMonitorCJI : function (sessionid, calldate, callbackFuc, mp3, filename) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/getMonitorCJI?callback=?', {
                sessionid: sessionid,
                calldate: calldate,
                mp3: mp3,
                filename: filename,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('getMonitorCJI error!');
                }
            });
        }

        // 队列中客户数量
        ,queueCustomerNumCJI: function (orgidentity, queuenumber, prio, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/queueCustomerNumCJI?callback=?', {
                orgidentity: orgidentity,
                queuenumber: queuenumber,
                prio: prio,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('queueCustomerNumCJI error!');
                }
            });
        }

        // 获取单一坐席实时数据
        ,agentRealtimeCJI : function (orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            // $.getJSON(astercc_cip + '/setevent/agentRealtimeCJI?callback=?', {
            //     orgidentity: orgidentity,
            //     usertype: usertype,
            //     user: user,
            //     pwdtype: pwdtype,
            //     password: password,
            // }, function (json) {
            //     try {
            //         if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
            //             callbackFuc(json);
            //         }
            //     } catch (e) {
            //         // alert('agentRealtimeCJI error!');
            //     }
            // });
            $.ajax({
                cache:false,
                url: astercc_cip + '/setevent/agentRealtimeCJI?callback=?',
                type:'get',
                data:{
                    orgidentity: orgidentity,
                    usertype: usertype,
                    user: user,
                    pwdtype: pwdtype,
                    password: password,
                },
                dataType:'jsonp',
                success:function (json) {
                    try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        // alert('loginCJI error!');
                    }
                },
                error: function (e) {
                    console.log(e)
                }
            });
        }

        // 获取单一坐席今日在坐席组中的统计数据
        ,agentStatisticDayCJI : function (orgidentity, usertype, user, pwdtype, password, agent_group_id, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/agentStatisticDayCJI?callback=?', {
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
                agent_group_id: agent_group_id,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('agentStatisticDayCJI error!');
                }
            });
        }

        // 发送DTMF
        ,dtmfCJI : function (orgidentity, usertype, user, pwdtype, password, dtmf, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/dtmfCJI?callback=?', {
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
                dtmf: dtmf,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('dtmfCJI error!');
                }
            });
        }

        // 设置随路数据
        ,setvarCJI : function (orgidentity, usertype, user, pwdtype, password, varname, varvalue, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/setvarCJI?callback=?', {
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
                varname: varname,
                varvalue: varvalue,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('setvarCJI error!');
                }
            });
        }

        // 坐席转IVR
        ,agenttoivrCJI :function (orgidentity, usertype, user, pwdtype, password, ivrexten, ivrflow, transfer, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/agenttoivrCJI?callback=?', {
                orgidentity: orgidentity,
                usertype: usertype,
                user: user,
                pwdtype: pwdtype,
                password: password,
                ivrexten: ivrexten,
                ivrflow: ivrflow,
                transfer: transfer,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('setvarCJI error!');
                }
            });
        }


        // 双呼拨号
        ,backcallCJI : function (orgidentity, exten, targetdn, callerid, user, password, pwdtype, userdata, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/backcallCJI?callback=?', {
                orgidentity: orgidentity,
                exten: exten,
                targetdn: targetdn,
                callerid: callerid,
                user: user,
                password: password,
                pwdtype: pwdtype,
                userdata: userdata,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('backcallCJI error!');
                }
            });
        }


        // Q房双呼拨号
        ,qbackcallCJI : function (orgidentity, exten, targetdn, icallerid, xcallerid, user, password, pwdtype, userdata, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/qbackcallCJI?callback=?', {
                orgidentity: orgidentity,
                exten: exten,
                targetdn: targetdn,
                icallerid: icallerid,
                xcallerid: xcallerid,
                user: user,
                password: password,
                pwdtype: pwdtype,
                userdata: userdata,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('backcallCJI error!');
                }
            });
        }


        // 设置分机
        ,setdeviceCJI : function (orgidentity, exten, user, pwdtype, password, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/setdeviceCJI?callback=?', {
                orgidentity: orgidentity,
                exten: exten,
                user: user,
                pwdtype: pwdtype,
                password: password,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('setdeviceCJI error!');
                }
            });
        }


        // 查询坐席信息
        ,getAgentInfoCJI : function (orgidentity, user, pwdtype, password, agentno, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            var pjson = orgidentity;
            if (typeof(orgidentity) != 'object') {
                pjson = {
                    'orgidentity': orgidentity,
                    'user': user,
                    'pwdtype': pwdtype,
                    'password': password,
                    'agentno': agentno,
                };
            } else {
                if (typeof(pjson.callbackFuc) != 'undefined' && pjson.callbackFuc != '') {
                    callbackFuc = pjson.callbackFuc;
                    delete pjson.callbackFuc;
                }
            }
            $.getJSON(astercc_cip + '/setevent/getAgentInfoCJI?callback=?', pjson, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('getAgentInfoCJI error!');
                }
            });
        }


        // yealink 答应接口
        ,yealinkAnswerCJI : function (orgidentity, type, target, phoneuser, phonepwd, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/setevent/yealinkAnswerCJI?callback=?', {
                orgidentity: orgidentity,
                type: type,
                target: target,
                phoneuser: phoneuser,
                phonepwd: phonepwd,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('yealinkAnswerCJI error!');
                }
            });
        }

        /**
         *
         * @param orgidentity
         * @param user
         * @param password
         * @param pwdtype
         * @param id
         * @param limit
         * @param userdata
         * @param callbackFuc
         */
        ,pullCdrCJI : function (orgidentity, user, password, pwdtype, id, limit, userdata, callbackFuc) {
            astercc_cip = (typeof(astercc_is_ssl) != 'undefined' && astercc_is_ssl ? 'https' : 'http') + '://' + astercc_ip;
            $.getJSON(astercc_cip + '/asterccinterfaces?callback=?', {
                EVENT: 'GetCdr',
                orgidentity: orgidentity,
                user: user,
                password: password,
                pwdtype: pwdtype,
                id: id,
                limit: limit,
                userdata: userdata,
            }, function (json) {
                try {
                    if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                } catch (e) {
                    // alert('pullCdrCJI error!');
                }
            });
        }
    };

    exports('CJI', CJI);
});
