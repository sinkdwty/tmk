/**
 * @link         http://www.hejunzongda.com/
 * @author       yaoliang <yaoliang@hfhjzddata.com>
 * @copyright    Copyright &copy; hjzddata.com, 2015 - 2018
 * @description    呼叫系统模块
 */

// layui.define(['layer', 'configs', 'CJI', 'CE'], function(exports) {
layui.define(['layer', 'CJI', 'CE'], function(exports) {
    var $ = layui.jquery, CJI = layui.CJI, CE = layui.CE;
    var callCenter = {};
    var orgidentity = 'fs';
    var usertype = 'agent';
    var pwdtype = 'plaintext';
    // var configs = layui.configs; // 获取配置参数
    var engine = getCookie("cc_engine");//configs.engine;

    callCenter = {

        /**
         * 登录接口
         * @param orgidentity 团队唯一标识
         * @param usertype 用户类型 agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 分机密码
         * @param callbackFuc 回调函数
         */
        login : function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.loginCJI(orgidentity, usertype, user, pwdtype, password, function( loginJson) {
                    callbackFuc(loginJson)
                    /*CJI.queueActionCJI(1, usertype, user, orgidentity, '', pwdtype, password, '', 'no', function(queueJson) {
                        // console.log(queueJson);
                        // callbackFuc(queueJson)

                    });*/
                });

            } else {
                CE.login(user, password, function(json) {
                    //try {
                    if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                        callbackFuc(json);
                    }
                    //} catch (e) {

                    //}
                });
            }
        },

        queueActionCJI : function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.queueActionCJI(1, usertype, user, orgidentity, '', pwdtype, password, '', 'no', function(queueJson) {
                    // callbackFuc(queueJson)
                });
            } else {

            }
        },

        /**
         * 登出接口
         * @param orgidentity 团队唯一标识
         * @param usertype 用户类型 agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 分机密码
         * @param callbackFuc 回调函数
         */
        logout : function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.logoutCJI(orgidentity, usertype, user, pwdtype, password, function(logoutJson) {
                    CJI.queueActionCJI(2, usertype, user, orgidentity, '', pwdtype, password, '', 'no', function(queueJson) {
                        console.log(CJI_result_format(queueJson));
                        // callbackFuc(queueJson)
                    });
                    callbackFuc(CJI_result_format(logoutJson));
                });
            } else {
                CE.logout(user, password, function(json) {
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {}
                });
            }
        },

        /**
         * 获取坐席状态接口
         * @param orgidentity 团队唯一标识
         * @param usertype 用户类型 agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 分机密码
         * @param callbackFuc 回调函数
         */
        agentStatus : function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.agentStatusCJI(orgidentity, usertype, user, pwdtype, password, callbackFuc);
            } else {
                CE.agentStatus(user, password, function(json) {
                    json = CE_result_format(json);
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },
        /**
         * 获取坐席实时数据接口
         * @param orgidentity 团队唯一标识
         * @param usertype 用户类型 agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 分机密码
         * @param callbackFuc 回调函数
         */
        agentRealtime : function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.agentRealtimeCJI(orgidentity, usertype, user, pwdtype, password, callbackFuc);
            } else {
                CE.agentStatus(user, password, function(json) {
                    json = CE_result_format(json);
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 获取坐席呼叫接口
         * @param orgidentity
         * @param usertype
         * @param user
         * @param pwdtype
         * @param password
         * @param callbackFuc
         */
        findCallStatus : function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.agentStatusCJI(orgidentity, usertype, user, pwdtype, password, function(agentStatus) {
                    console.log(agentStatus);
                    agentStatus = CJI_result_format(agentStatus);
                    if (agentStatus.code ===1 ) {
                        var status = agentStatus.status;
                        var statusArr = status.split(',');
                        for (var i=0; i<statusArr.length; i++ ) {
                            var arr = statusArr[i].split('-');
                            agentStatus.status = arr[1];
                        }
                    }
                    callbackFuc(agentStatus);
                });
            } else {
                CE.findCallStatus(user, password, function(json) {
                    //json = CE_result_format(json);
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 拨打电话
         * @param targetdn 被叫号码
         * @param targettype 被叫类型 目标类型(电话号码[exter],坐席[inner])
         * @param agentgroupid 坐席组id
         * @param usertype 用户类型 agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param orgidentity 团队唯一标识
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 分机密码
         * @param modeltype 业务类型(业务应用[BusinessApp],外呼营销[Campaign],虚拟中心[Virtualcustomer],呼入客服[Customerservice])
         * @param model_id 业务ID
         * @param userdata 业务ID
         * @param callbackFuc  回调函数
         */
        makeCall : function(targetdn, targettype, agentgroupid, usertype, user, orgidentity, pwdtype, password, modeltype, model_id, userdata, callbackFuc) {
            if (engine=='CJI') {
                // CJI.queueActionCJI(1, usertype, user, orgidentity, '', pwdtype, password, '', 'no', function(queueJson) {
                    CJI.makeCallCJI(targetdn, targettype, agentgroupid, usertype, user, orgidentity, pwdtype, password, modeltype, model_id, userdata, function (mackJson) {
                        callbackFuc(mackJson);
                    });
                // })
            } else {
                CE.makeCall(user, password, targetdn, model_id, userdata, function(json) {
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 挂断电话
         * @param uniqueid 要挂断通话的uniqueid 列表，多个uniqueid 以逗号分割。  非必填项，可以为空。必填情况请参照target 参数。
         * @param targetagent 目标坐席工号，要操作哪个坐席的通话。
         uniqueid 为空时，此项必填。
         uniqueid 不为空时，此项可为空。
         如果挂断监听，强插，密语中的坐席组长(target=groupadmin)，
         此处需要填写被监听，强插，密语的坐席工号。
         如2227 组长监听1986，欲挂断2227，此处需要填写1986
         * @param target 挂机对象(channel,agent,caller,consult,all)
         channel 必须传uniqueid，挂断相应uniqueid 的通话
         agent 查出目标坐席并挂断
         caller 挂断客户
         consult 如果传了uniqueid 则只挂uniqueid 相应的咨询方，如果
         没传，就按从旧到新的顺序挂断所有咨询方的通话
         all 挂断目标坐席相关所有通话，如果被监听，强插，密语同时挂
         断组长通话
         groupadmin 当目标坐席被监听，强插，密语时，用于挂断组长的
         通话，即结束组长的监听，强插，密语状态。
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 分机密码
         * @param usertype 用户类型 agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param orgidentity 团队唯一标识
         * @param callbackFuc 回调函数
         */
        hangup : function(uniqueid, targetagent, target, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            if (engine=='CJI') {
                CJI.hangupCJI(uniqueid, targetagent, target, pwdtype, password, usertype, user, orgidentity, callbackFuc);
            } else {
                CE.hangup(user, password, function(json) {
                    json = CE_result_format(json);
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 呼叫保持
         * @param silence 是否静音(0 或1)，0 代表不静音，1 代表静
         * @param orgidentity 团队唯一标识
         * @param usertype  agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 密码
         * @param callbackFuc 回调函数
         */
        hold : function(silence, orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.holdCJI(silence, orgidentity, usertype, user, pwdtype, password, callbackFuc);
            } else {
                CE.hold(user, password, function(json) {
                    json = CE_result_format(json);
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 取消呼叫保持
         * @param orgidentity 团队唯一标识
         * @param usertype agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password  密码
         * @param callbackFuc  回调函数
         */
        resume :function(orgidentity, usertype, user, pwdtype, password, callbackFuc) {
            if (engine=='CJI') {
                CJI.resumeCJI(orgidentity, usertype, user, pwdtype, password, callbackFuc);
            } else {
                CE.resume(user, password, function(json) {
                    json = CE_result_format(json);
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 转接
         * @param targetdn 内线转接填写坐席工号 / 外线转接填写目标号码
         * @param agentgroupid 内线转接的坐席(被咨询的坐席)所属坐席组id, 外线转接时填0
         * @param consulttype 转接类型，内线转接(internal)，外线转接(external)
         * @param pwdtype 密码类型 明文(plaintext) / MD5 加密(md5)
         * @param password 密码
         * @param usertype agent(坐席) / account(帐号)
         * @param user 坐席工号 / 用户名
         * @param orgidentity 组织标识
         * @param callbackFuc 回调函数
         */
        transfer : function(targetdn, agentgroupid, consulttype, pwdtype, password, usertype, user, orgidentity, callbackFuc) {
            if (engine=='CJI') {
                CJI.consultCJI(targetdn, agentgroupid, consulttype, pwdtype, password, usertype, user, orgidentity, function(json) {
                    if (json.code == 1) {
                        CJI.transferCJI(pwdtype, password, usertype, user, orgidentity, callbackFuc);
                    }
                });
            } else {
                CE.transfer(user, password, function(json) {
                    json = CE_result_format(json);
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 接听听话
         * @param user 分机号
         * @param password 分机密码
         * @param callbackFuc 回调函数
         */
        answer : function(user, password, callbackFuc) {
            layer.msg('暂未实现！');
        },

        /**
         * 获取话单接口
         * @param orgidentity 团队唯一标识，用于鉴权
         * @param user 鉴权帐号
         * @param password 鉴权帐号的密码
         * @param pwdtype 密码类型  明文(plaintext) / MD5加密(md5)
         * @param id 正整数，大于等于0，默认0，增量参数，用于数据去重
         * @param limit 正整数，大于等于1，默认10，最大不能超过5000
         * @param model_id
         * @param userdata 可缺省，查询呼叫记录中符合的userdata字段值
         * @param callbackFuc
         */
        pullCdr: function pullCdr(orgidentity, user, password, pwdtype, id, limit, model_id, userdata, callbackFuc) {
            if (engine=='CJI') {
                userdata = model_id != ''? model_id: userdata;
                CJI.pullCdrCJI(orgidentity, user, password, pwdtype, id, limit, userdata, function(json) {
                    // var json = {"result":"success","msg":{"data":[{"id":66,"calldate":"2017-09-29 13:19:51","answertime":"0000-00-00 00:00:00","endtime":"2017-09-29 13:19:51","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"0","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"b51f4bf5f0a164b80a984d8378c48022","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_b51f4bf5f0a164b80a984d8378c48022","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":67,"calldate":"2017-09-29 13:20:21","answertime":"0000-00-00 00:00:00","endtime":"2017-09-29 13:20:23","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"2","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"b8b9a2a0f8c766876613fd4a21c2b2ca","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_b8b9a2a0f8c766876613fd4a21c2b2ca","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":68,"calldate":"2017-09-30 09:29:32","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 09:29:46","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"14","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"e1d0dd0fa843a4ab1194a836eb2a9336","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_e1d0dd0fa843a4ab1194a836eb2a9336","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":70,"calldate":"2017-09-30 09:34:17","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 09:34:17","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"0","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"bb31405c32ff963216b609f2eafd2819","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_bb31405c32ff963216b609f2eafd2819","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":71,"calldate":"2017-09-30 09:34:41","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 09:35:04","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"23","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"0de3565aa29e7e92b7ea4767a58c0150","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_0de3565aa29e7e92b7ea4767a58c0150","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":76,"calldate":"2017-09-30 09:52:27","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 09:52:29","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"2","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"9861cef0bfaee803287e3f44a2674975","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_9861cef0bfaee803287e3f44a2674975","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":77,"calldate":"2017-09-30 09:55:15","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 09:55:17","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"2","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"9f9d848c1e1fd435dfbf0243b0fae07e","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_9f9d848c1e1fd435dfbf0243b0fae07e","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":78,"calldate":"2017-09-30 09:55:43","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 09:55:45","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"2","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"7c917faf5b54dacd474a99eede981e0d","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_7c917faf5b54dacd474a99eede981e0d","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":79,"calldate":"2017-09-30 09:57:24","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 09:57:27","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"3","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"45fc9d857ad638e0ff8949a4cfa6de67","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_45fc9d857ad638e0ff8949a4cfa6de67","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":80,"calldate":"2017-09-30 10:07:14","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 10:07:27","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"13","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"0","sessionid":"ce3b7130faea4d04146adfd3c27a9d17","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_ce3b7130faea4d04146adfd3c27a9d17","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":83,"calldate":"2017-09-30 10:19:52","answertime":"2017-09-30 10:20:22","endtime":"2017-09-30 10:20:25","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"33","billsec":"3","disposition":"ANSWERED","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"3","sessionid":"054db2d8faea80fc8d0e5412de17d94e","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"66039233","memo":"","hangupstatus":"","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_054db2d8faea80fc8d0e5412de17d94e","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":84,"calldate":"2017-09-30 10:20:31","answertime":"2017-09-30 10:20:43","endtime":"2017-09-30 10:20:52","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"21","billsec":"9","disposition":"ANSWERED","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"3","sessionid":"e5626bc15d19ebbb5f6dc8dbfd1425ec","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"66039233","memo":"","hangupstatus":"","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_e5626bc15d19ebbb5f6dc8dbfd1425ec","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":85,"calldate":"2017-09-30 10:26:33","answertime":"2017-09-30 10:26:45","endtime":"2017-09-30 10:27:06","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"33","billsec":"21","disposition":"ANSWERED","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"3","sessionid":"e702159546e910bfd1679b12c458cb74","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"66039233","memo":"","hangupstatus":"","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_e702159546e910bfd1679b12c458cb74","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":86,"calldate":"2017-09-30 10:27:09","answertime":"0000-00-00 00:00:00","endtime":"2017-09-30 10:27:21","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"12","billsec":"0","disposition":"NO ANSWER","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"3","sessionid":"57d6b6490fb92f4f80f01f5af197887c","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"66039233","memo":"","hangupstatus":"AGENT HANGUP","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_57d6b6490fb92f4f80f01f5af197887c","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"},{"id":87,"calldate":"2017-09-30 10:27:29","answertime":"2017-09-30 10:27:42","endtime":"2017-09-30 10:28:02","calleridnum":"6004","dst":"15395083865","didnumber":"","duration":"33","billsec":"20","disposition":"ANSWERED","agentno":"6000","calltype":"DIALOUT","team_id":"1","agent_group_id":"1","account_id":"2","trunk_id":"3","sessionid":"66faa2f04b4f0a3af6929ec7c85c8ba1","pdsessionid":"","rec_result":"","calleridname":"","act_callerid":"66039233","memo":"","hangupstatus":"","productserial":"cc20170819000718149","uniqueid":"cc20170819000718149_66faa2f04b4f0a3af6929ec7c85c8ba1","ivrduration":"0","ivrduration":"0","queuewaitsec":"0","userdata":"1|1"}],"totalCount":15}}
                    for (var o in json.msg.data) {
                        if (json.msg.data[o].billsec > 0) {
                            console.log(CJI.getMonitorCJI(json.msg.data[o].sessionid, (json.msg.data[o].calldate).substring(0, 10), function(json) {
                                callbackFuc(json);
                            }, true));
                        }
                    }
                });
            } else {
                CE.pullCdr(model_id, userdata, function(url) {
                    try {
                        if (typeof (callbackFuc) !== 'undefined' && callbackFuc != '') {
                            callbackFuc(url);
                        }
                    } catch (e) {

                    }
                });
            }
        },

        /**
         * 发送短信
         * @param user 分机号
         * @param password 分机密码
         * @param smd 短信内容
         * @param callbackFuc 回调函数
         */
        sendSms : function(user, password, smd, callbackFuc) {
            layer.msg('暂未实现！');
        },

    };

    var CJI_msg = new Array();
    CJI_msg.BackMsg_01 = 'BackMsg_01'; CJI_msg.BackMsg_02='组织标识错误或不存在'; CJI_msg.BackMsg_03='工号不存在或密码错误'; CJI_msg.BackMsg_04='登录成功'; CJI_msg.BackMsg_05='坐席所属帐号被禁用'; CJI_msg.BackMsg_06='坐席所属帐号不存在'; CJI_msg.BackMsg_07='密码错误'; CJI_msg.BackMsg_08='登出成功'; CJI_msg.BackMsg_09='坐席工号不存在[坐席工号]'; CJI_msg.BackMsg_10='切换队列状态失败!'; CJI_msg.BackMsg_11='坐席组'; CJI_msg.BackMsg_12='不存在此座席'; CJI_msg.BackMsg_13='变量'; CJI_msg.BackMsg_14='切换队列状态成功!'; CJI_msg.BackMsg_15='帐号不存在'; CJI_msg.BackMsg_16='呼叫失败'; CJI_msg.BackMsg_17='呼叫失败'; CJI_msg.BackMsg_18='呼叫失败'; CJI_msg.BackMsg_19='呼叫成功'; CJI_msg.BackMsg_20='呼叫失败'; CJI_msg.BackMsg_21='咨询操作失败!'; CJI_msg.BackMsg_22='请不要重复申请拨号!'; CJI_msg.BackMsg_23='坐席暂未设置分机信息'; CJI_msg.BackMsg_24='未找到坐席'; CJI_msg.BackMsg_25='预咨询的坐席未签入'; CJI_msg.BackMsg_26='创建咨询方数据失败!'; CJI_msg.BackMsg_27='咨询操作成功!'; CJI_msg.BackMsg_28='参数错误!'; CJI_msg.BackMsg_29='转接失败'; CJI_msg.BackMsg_30='坐席组不存在'; CJI_msg.BackMsg_31='转接成功'; CJI_msg.BackMsg_32='接回操作成功'; CJI_msg.BackMsg_33='接回操作失败'; CJI_msg.BackMsg_34='会议操作成功'; CJI_msg.BackMsg_35='会议操作失败!'; CJI_msg.BackMsg_36='挂机失败'; CJI_msg.BackMsg_37='挂机成功'; CJI_msg.BackMsg_38='当前坐席无匹配的帐号'; CJI_msg.BackMsg_39='强插成功'; CJI_msg.BackMsg_40='强插失败!'; CJI_msg.BackMsg_41='监听成功!'; CJI_msg.BackMsg_42='监听失败!'; CJI_msg.BackMsg_43='获取成功'; CJI_msg.BackMsg_44='获取失败'; CJI_msg.BackMsg_45='当前话务不能强拆'; CJI_msg.BackMsg_46='强拆成功!'; CJI_msg.BackMsg_47='强拆失败!'; CJI_msg.BackMsg_48='密语成功!'; CJI_msg.BackMsg_49='密语失败!'; CJI_msg.BackMsg_50='当前团队下无此坐席组'; CJI_msg.BackMsg_51='通话暂停，操作失败!'; CJI_msg.BackMsg_52='通话暂停，操作成功!'; CJI_msg.BackMsg_53='话务继续'; CJI_msg.BackMsg_54='话务继续'; CJI_msg.BackMsg_55='坐席工号错误或不存在'; CJI_msg.BackMsg_56='不能咨询'; CJI_msg.BackMsg_57='暂无坐席组可签入'; CJI_msg.BackMsg_58='未签入任何坐席组'; CJI_msg.BackMsg_59='话后模式切换成功'; CJI_msg.BackMsg_60='坐席组未签入或组内不存在此坐席'; CJI_msg.BackMsg_61='结束话后成功'; CJI_msg.BackMsg_62='工作模式切换成功'; CJI_msg.BackMsg_63='用'; CJI_msg.BackMsg_64='通话'; CJI_msg.BackMsg_65='该坐席不是坐席组长'; CJI_msg.BackMsg_66='坐席已经处于暂停状态'; CJI_msg.BackMsg_67='外拨营销任'; CJI_msg.BackMsg_68='不是坐席组长'; CJI_msg.BackMsg_69='预拨号数据导入完毕'; CJI_msg.BackMsg_70='modeltype'; CJI_msg.BackMsg_71='context'; CJI_msg.BackMsg_72='source'; CJI_msg.BackMsg_73='外呼营销任务不存在'; CJI_msg.BackMsg_74='文件标题头与数据表结构不符.'; CJI_msg.BackMsg_75='context'; CJI_msg.BackMsg_76='文件中缺少必填字段]='; CJI_msg.BackMsg_77='重置客户状态成功'; CJI_msg.BackMsg_78='更新客户数据成功'; CJI_msg.BackMsg_79='清空归属坐席成功'; CJI_msg.BackMsg_80='插入预拨号列表成功'; CJI_msg.BackMsg_81='数据重复'; CJI_msg.BackMsg_82='电话号码重复'; CJI_msg.BackMsg_83='新数据导入客户包成功，并且插入预拨号列表成功'; CJI_msg.BackMsg_84='新数据导入客户包成功'; CJI_msg.BackMsg_85='数据存储错误'; CJI_msg.BackMsg_86='仅支持'; CJI_msg.BackMsg_87='导入任务创建失败'; CJI_msg.BackMsg_88='文件上传失败'; CJI_msg.BackMsg_89='远程数据文件获取失败'; CJI_msg.BackMsg_404='目标文件不存在'; CJI_msg.BackMsg_90='坐席未处于话后状态'; CJI_msg.BackMsg_91='工作模式切换失败，坐席组未签入或不存在此坐席组或坐席不属于此坐席组。'; CJI_msg.BackMsg_92='无相关通话'; CJI_msg.BackMsg_93='坐席组或团队内无任何坐席'; CJI_msg.BackMsg_94='无相关状态的坐席'; CJI_msg.BackMsg_95='当前处于咨询状态，无法再发起咨询通话'; CJI_msg.BackMsg_96='无法进行通话暂停或恢复。不是坐席与客户单独通话状态。'; CJI_msg.BackMsg_97='dtmf'; CJI_msg.BackMsg_98='发送'; CJI_msg.BackMsg_99='发送'; CJI_msg.BackMsg_100='内核连接失败'; CJI_msg.BackMsg_101='varname'; CJI_msg.BackMsg_102='设置随路数据成功'; CJI_msg.BackMsg_103='设置随路数据失败'; CJI_msg.BackMsg_104='咨询中不允许转'; CJI_msg.BackMsg_105='会议中不允许释放转'; CJI_msg.BackMsg_106='主'; CJI_msg.BackMsg_107='IVR'; CJI_msg.BackMsg_108='坐席转'; CJI_msg.BackMsg_109='坐席转'; CJI_msg.BackMsg_110='分机不存在'; CJI_msg.BackMsg_111='需签出后，再进行分机设置'; CJI_msg.BackMsg_112='分机设置完毕'; CJI_msg.BackMsg_113='帐号数据创建失败'; CJI_msg.BackMsg_114='坐席数据创建失败'; CJI_msg.BackMsg_115='超出坐席授权数'; CJI_msg.BackMsg_116='当前服务器授权错误'; CJI_msg.BackMsg_117='App'; CJI_msg.BackMsg_118='坐席未设置分机信息'; CJI_msg.BackMsg_119='无法获取分机注册状态'; CJI_msg['BackMsg_120[ip]']='已向'; CJI_msg.BackMsg_121='此分机号码已被其它签入坐席占用'; CJI_msg['BackMsg_122[type]']='不支持此'; CJI_msg.BackMsg_123='坐席不属于任何坐席组'; CJI_msg.BackMsg_124='座席数据删除成功';

    var CJI_result_format = function(json) {
        json.message = CJI_msg[json.message];
        return json;
    };

    var CE_result_format = function(json) {
        json.code = json.code+1;
        json.message = (json.msg!=''&& json.msg!=undefined) ?json.msg: json.errmsg;
        return json;
    };

    exports('callCenter', callCenter);
});


/**
 *
 * @param fmt
 * @returns {*}
 */
Date.prototype.format = function (fmt) { //author: meizz
    var o = {
        "M+" : this.getMonth()+1, //月份
        "d+" : this.getDate(), //日
        "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
        "H+" : this.getHours(), //小时
        "m+" : this.getMinutes(), //分
        "s+" : this.getSeconds(), //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S" : this.getMilliseconds() //毫秒
    };
    var week = {
        "0" : "/u65e5",
        "1" : "/u4e00",
        "2" : "/u4e8c",
        "3" : "/u4e09",
        "4" : "/u56db",
        "5" : "/u4e94",
        "6" : "/u516d"
    };
    if(/(y+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    if(/(E+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);
    }
    for(var k in o){
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
};
