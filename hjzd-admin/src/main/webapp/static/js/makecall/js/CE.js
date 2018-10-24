/**
 * @link         http://www.hejunzongda.com/
 * @author       yaoliang <yaoliang@hfhjzddata.com>
 * @copyright    Copyright &copy; hjzddata.com, 2015 - 2018
 * @description    汉天呼叫系统模块
 */

layui.define(['layer', 'configs'], function(exports) {

    var $ = layui.jquery;
    var layer = layui.layer;
    var configs = layui.configs; // 获取配置参数
    var http = 'http://'+configs.ipaddress+':'+configs.port;
    var http_ce = 'http://'+configs.ipaddress+':8088';

    var CE = {
        /**
         * 登录接口
         * @param empNumber 账号
         * @param exten 分机号
         * @param callbackFuc
         */
        login : function(empNumber, exten, callbackFuc) {
            var data = {'empNumber': empNumber, 'exten': exten};
		
            $.ajax({
                'url': http+'/ecs2/http/bind/bind',
                'dataType': 'jsonp',
                'data': data,
                'timeout': 5000,
                'success': function(json) {					
                    //try {
                        if (typeof(callbackFuc) != 'undefined' && callbackFuc != '') {
                            if (json.code == 100) {
                                callbackFuc({'code': 1, 'message': '登录成功'});
                            } else {
                                callbackFuc({'code': 0, 'message': '登录失败'});
                            }
                        }
                    //} catch (e) {
                    //    throw new Error('请检查相关度网络状况');
                    //}
                },
                'error': function(xhr) {
                    layer.msg('请求出错(请检查相关度网络状况.)');
                },
            });
        }

        /**
         * 登出接口
         * @param empNumber 账号
         * @param exten 分机号
         * @param callbackFuc
         */
        ,logout : function(empNumber, exten, callbackFuc) {
            var data = {'empNumber': empNumber, 'exten': exten};

            $.ajax({
                'url': http+'/ecs2/http/bind/unbind',
                'dataType': 'jsonp',
                'data': data,
                'timeout': 5000,
                'success': function(json) {
                    try {
                        if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                            if (json.code == 100) {
                                callbackFuc({'code': 1, 'message': '退出成功'});
                            } else {
                                callbackFuc({'code': 0, 'message': '退出失败'});
                            }
                        }
                    } catch (e) {
                        throw new Error('请求出错(请检查相关度网络状况.)');
                    }
                },
                'error': function(xhr) {
                    layer.msg('请求出错(请检查相关度网络状况.)');
                },
            });
        }

        /**
         * 获取坐席状态接口
         * @param empNumber 账号
         * @param exten 分机号
         * @param callbackFuc
         */
        ,gentStatus : function(empNumber, exten, callbackFuc) {
            var json = {'exten': exten, 'command': 'findCallStatus'};

            json = JSON.stringify(json);

            $.ajax({
                'url': http_ce+'/callengine/http/status/find?json='+json,
                'type': 'get',
                'dataType': 'json',
                'success': function(json) {
                    try {
                        if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        throw new Error('请求出错(请检查相关度网络状况.)');
                    }
                },
                'error': function(e) {
                    layer.msg('请求出错(请检查相关度网络状况.)');
                },
            });
        }

        /**
         * 拨打电话
         * @param empNumber 账号
         * @param exten 分机号
         * @param phone_number 被叫号码
         * @param model_id 项目id
         * @param ext_field 扩展字段，可以是业务ID
         * @param callbackFuc 回调函数
         */
        ,makeCall : function(empNumber, exten, phone_number, model_id, ext_field, callbackFuc) {
            var data = {'telNumber': phone_number, 'exten': exten, 'projectId': model_id, 'bizId': model_id+'_'+ext_field};
            $.ajax({
                'url': http +'/ecs2/httppopupwindow/popup/dialTelNumber',
                'dataType': 'jsonp',
                'data': data,
                'timeout': 5000,
                'success': function(json) {
                    try {
                        if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                            if (json.callMsg !== 'undefined' && json.callMsg !== '') {
                                callbackFuc({'code': 1, 'message': '拨号成功'});
                            } else {
                                callbackFuc({'code': 0, 'message': '拨号失败'});
                            }
                        }
                    } catch (e) {
                        throw new Error('请求出错(请检查相关度网络状况.');
                    }
                },
                'error': function(xhr) {
                    layer.msg('请求出错(请检查相关度网络状况.)');
                },
            });
        }

        /**
         * 挂断电话
         * @param empNumber 账号
         * @param exten 分机号
         * @param callbackFuc 回调函数
         */
        ,hangup : function(empNumber, exten, callbackFuc) {
            var json = {'dest': exten, 'command': 'hangup'};

            json = JSON.stringify(json);

            $.ajax({
                'url': http_ce+'/callengine/http/operation?json='+json,
                'type': 'get',
                'dataType': 'json',
                'success': function(json) {
                    try {
                        if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        throw new Error('请求出错(请检查相关度网络状况.');
                    }
                },
                'error': function(e) {
                    layer.msg('请求出错(请检查相关度网络状况.)');
                },
            });
        }

        /**
         * 呼叫保持
         * @param empNumber 账号
         * @param exten 分机号
         * @param phone_number  被叫号码
         * @param callbackFuc 回调函数
         */
        , hold : function(empNumber, exten, callbackFuc) {
            this.findCallStatus(empNumber, exten, function(json) {
                var json = {'dest': json.result.calleeNum, 'command': 'hold'};

                json = JSON.stringify(json);

                $.ajax({
                    'url': http_ce+'/callengine/http/operation?json='+json,
                    'type': 'get',
                    'dataType': 'json',
                    'success': function(json) {
                        try {
                            if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                                callbackFuc(json);
                            }
                        } catch (e) {
                            throw new Error('请求出错(请检查相关度网络状况.');
                        }
                    },
                    'error': function(e) {
                        layer.msg('请求出错(请检查相关度网络状况.)');
                    },
                });
            });
        }

        /**
         * 取消呼叫保持
         * @param empNumber 账号
         * @param exten 分机号
         * @param phone_number  被叫号码
         * @param callbackFuc 回调函数
         */
        , resume : function(empNumber, exten, callbackFuc) {
            this.findCallStatus(empNumber, exten, function(json) {
                var json = {'dest': json.result.calleeNum, 'command': 'hold'};

                json = JSON.stringify(json);

                $.ajax({
                    'url': http_ce+'/callengine/http/operation?json='+json,
                    'type': 'get',
                    'dataType': 'json',
                    'success': function(json) {
                        try {
                            if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                                callbackFuc(json);
                            }
                        } catch (e) {
                            throw new Error('请求出错(请检查相关度网络状况.');
                        }
                    },
                    'error': function(e) {
                        layer.msg('请求出错(请检查相关度网络状况.)');
                    },
                });
            });
        }

        /**
         * 转接
         * @param empNumber 账号
         * @param exten 分机号
         * @param transfer_number 转接号码
         * @param dest_number 被叫号码
         * @param target_type 转接类型内线(internal)，外线(external)
         * @param callbackFuc 回调函数
         */
        ,transfer : function(empNumber, exten, transfer_number, dest_number, target_type, callbackFuc) {
            var json = {'transferNum': transfer_number, 'dest': dest_number, 'command': 'attendTransfer'};

            json = JSON.stringify(json);

            $.ajax({
                'url': http_ce+'/callengine/http/operation?json='+json,
                'type': 'get',
                'dataType': 'json',
                'success': function(json) {
                    try {
                        if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        throw new Error('请求出错(请检查相关度网络状况.');
                    }
                },
                'error': function(e) {
                    layer.msg('请求出错(请检查相关度网络状况.)');
                },
            });
        }

        /**
         * 获取单个呼叫状态
         * @param empNumber 账号
         * @param exten 分机号
         */
        ,findCallStatus : function(empNumber, exten, callbackFuc) {
            var json = {'userId': exten, 'scope': 'fsCallStatus'};

            json = JSON.stringify(json);

            $.ajax({
                'url': http_ce+'/callengine/http/status/find?json='+json,
                'type': 'get',
                'dataType': 'json',
                'success': function(json) {
                    try {
                        if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                            callbackFuc(json);
                        }
                    } catch (e) {
                        throw new Error('请求出错(请检查相关度网络状况.');
                    }
                },
                'error': function(e) {
                    layer.msg('请求出错(请检查相关度网络状况.)');
                },
            });
        }

        /**
         * 接听听话
         * @param empNumber 账号
         * @param exten 分机号
         * @param callbackFuc 回调函数
         */
        ,answer : function(user, password, callbackFuc) {
            layer.msg('暂未实现！');
        }

        /**
         * 获取通话记录url
         * @param projectId 项目编号
         * @param bizId 业务编号
         * @param callbackFuc 回调函数
         */
        ,pullCdr : function(projectId, bizId, callbackFuc) {
            var url = http+'/ecs2/http/finalcdr/show?projectId='+projectId+'&bizId='+projectId+'_'+bizId;

            try {
                if (typeof callbackFuc !== 'undefined' && callbackFuc != '') {
                    callbackFuc(url);
                }
            } catch (e) {
                layer.msg('请求出错(请检查相关度网络状况.)');
            }
        }

    };

    exports('CE', CE);
});

