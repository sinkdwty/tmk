package com.hjzddata.modular.task.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IUserService;
import com.hjzddata.modular.task.model.CallLog;
import com.hjzddata.modular.task.dao.CallLogMapper;
import com.hjzddata.modular.task.service.ICallLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 呼叫日志表 服务实现类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-18
 */
@Service
public class CallLogServiceImpl extends ServiceImpl<CallLogMapper, CallLog> implements ICallLogService {

    @Autowired
    private IUserService userService;

    @Override
    public List<Map<String, Object>> selectCallList(Page<CallLog> page, Integer caseId,String column,String condition, String beginTime, String endTime,String checkStatus,Integer call_status_id, String orderByField, boolean asc) {
        Integer user_id = ShiroKit.getUser().getId();
        if (ShiroKit.isAdmin()) {
            return this.baseMapper.selectCallList(page, null,caseId, column, condition, beginTime, endTime,checkStatus, call_status_id,orderByField, asc);
        } else {
            List<Integer> ids = new ArrayList<>();
            if (ShiroKit.hasRole("worker")) {       //坐席角色
                ids.add(user_id);
            } else {
                Integer product_id = ShiroKit.getUser().getProductId();
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.and("productid=" + product_id + " and status=1");
                List<User> res = userService.selectList(entityWrapper);
                ids =res.stream().map(User::getId).collect(Collectors.toList());
            }
            return this.baseMapper.selectCallList(page, ids,caseId, column, condition, beginTime, endTime,checkStatus,call_status_id, orderByField, asc);
        }
    }

    @Override
    public String getCallConfig(Integer userId) {
        return this.baseMapper.getCallConfig(userId);
    }

    @Override
    public List selectLastLogBycase(Integer caseId) {
        return this.baseMapper.selectLastLogBycase(caseId);
    }
}
