package com.hjzddata.modular.custom.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.custom.model.Custom;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hjzd
 * @since 2018-07-10
 */
public interface CustomMapper extends BaseMapper<Custom> {


    List<Map<String, Object>> selectCustomList(@Param("page") Page<Custom> page, @Param("userid") Integer userid, @Param("column") String column,
                                               @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime,
                                               @Param("update_beginTime") String update_beginTime, @Param("update_endTime") String update_endTime, @Param("status") Integer status,
                                               @Param("company") Integer company, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc
    );

    List<Map<String, Object>> selectMyCustomList(@Param("page") Page<Custom> page, @Param("userid") Integer userid, @Param("column") String column,
                                                 @Param("condition") String condition, @Param("beginTime") String beginTime, @Param("endTime") String endTime,
                                                 @Param("update_beginTime") String update_beginTime, @Param("update_endTime") String update_endTime, @Param("status") Integer status, @Param("call_status_id") Integer call_status_id, @Param("check_status") Integer check_status, @Param("type") Integer type,
                                                 @Param("company") Integer company, @Param("product_id") Integer product_id, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc
    );

    void importCustom(Custom custom);

    /**
     * 通过账号获取用户
     */
    Custom getByPhone(@Param("phone") String phone, @Param("company") Integer company);

    Integer updateStatusByIds(@Param("ids") String[] ids);

    Integer deleteCustomByIds(@Param("ids") String[] ids);

    Integer assignCustom(@Param("ids") String[] ids, @Param("user_id") Integer user_id, @Param("allocateTime") Date allocateTime);

    List<Custom> selectAssignCustomList(@Param("userid") Integer userid, @Param("column") String column,
                                        @Param("condition") String condition, @Param("beginTime") String beginTime,
                                        @Param("endTime") String endTime, @Param("update_beginTime") String update_beginTime,
                                        @Param("update_endTime") String update_endTime, @Param("status") Integer status,
                                        @Param("product_id") Integer product_id, @Param("id") Integer id, @Param("limit") Integer limit
    );

    Integer insertBatchNew(List<Custom> list, Integer productid) throws Exception;

    /**
     * 数据统计
     */
    Collection dataReport(@Param("account") String account, @Param("batch_no") String batch_no, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("product_id") Integer product_id, @Param("user_id") List<Integer> user_id, @Param("call_status") String call_status);
}
