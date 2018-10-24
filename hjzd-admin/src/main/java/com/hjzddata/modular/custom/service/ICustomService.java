package com.hjzddata.modular.custom.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.custom.model.Custom;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-10
 */
public interface ICustomService extends IService<Custom> {

    List<Map<String, Object>> selectCustomList(Page<Custom> page,Integer userid, String column, String condition, String beginTime, String endTime, String update_beginTime, String update_endTime, Integer status, Integer call_status_id,Integer check_status, Integer type, String orderByField, boolean asc);

    Object batchImport(String fileName, MultipartFile file) throws Exception;

    Custom getByPhone(String phone);

    Integer updateStatusByIds(@Param("ids") String[] ids);

    Integer deleteCustomByIds(String[] ids);

    Integer assignCustom(String[] ids, Integer user_id, Date allocateTime);

    List<Custom> selectAssignCustomList( String column, String condition, String beginTime, String endTime,String update_beginTime,String update_endTime, Integer status, Integer id, Integer limit);

    /**
     * 解析数据表
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    Collection parseExcel(String fileName) throws Exception;

    Integer insertBatchNew(List<Custom> list, Integer productid, String batch_no) throws Exception;

    /**
     * 数据统计
     */
    Collection dataReport(String account, String batch_no, String beginTime, String endTime);
}
