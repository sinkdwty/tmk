package com.hjzddata.modular.custom.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.excel.ExcelLogs;
import com.hjzddata.core.excel.ExcelUtil;
import com.hjzddata.core.shiro.ShiroKit;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.custom.model.Custom;
import com.hjzddata.modular.custom.dao.CustomMapper;
import com.hjzddata.modular.custom.service.ICustomService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjzddata.modular.system.model.Dict;
import com.hjzddata.modular.system.model.User;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.system.service.IUserService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjzd
 * @since 2018-07-10
 */
@Service
//@Transactional(readOnly = true)
public class CustomServiceImpl extends ServiceImpl<CustomMapper, Custom> implements ICustomService {

//    @Autowired
//    CustomMapper customMapper;

    @Autowired
    private ICustomService customService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDictService dictService;


    @Override
    public List<Map<String, Object>> selectCustomList(Page<Custom> page, Integer userid, String column, String condition,String beginTime,String endTime,String update_beginTime, String update_endTime, Integer status, Integer call_status_id,Integer check_status, Integer type, String orderByField, boolean asc) {
        Integer product_id = ShiroKit.getUser().getProductId();
        if (ShiroKit.hasRole("worker")) {       //坐席角色  客户列表展示本公司且属于自己的  和我的客户展示一样
            userid = ShiroKit.getUser().getId();
        }

        if(userid != null && userid > 0) {
            Integer company = ShiroKit.getUser().getDeptId();   //获取当前用户公司id
            return this.baseMapper.selectMyCustomList(page,userid,column,condition,beginTime,endTime,update_beginTime,update_endTime,status,call_status_id,check_status,type,null,product_id,orderByField, asc);
        } else {
            if (ShiroKit.isAdmin()) {
                return this.baseMapper.selectMyCustomList(page, null, column, condition, beginTime, endTime, update_beginTime, update_endTime, status, call_status_id,check_status, type, null,null, orderByField, asc);
            } else {
                return this.baseMapper.selectMyCustomList(page, null, column, condition, beginTime, endTime, update_beginTime, update_endTime, status, call_status_id,check_status,type,null,product_id, orderByField, asc);
            }
        }
    }

    /**
     * 客户导入
     * @param fileName
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public Object batchImport(String fileName, MultipartFile file) throws Exception {

        boolean notNull = false;
        List<Custom> customList = new ArrayList<Custom>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return new JsonResult(201, "格式不正确", new Date());
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }

        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            notNull = true;
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            Custom custom = new Custom();

            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String name = row.getCell(0).getStringCellValue();
            if (name == null || name.isEmpty()) {
                return new JsonResult(201, "导入失败(第" + (r + 1) + "行,客户姓名未填写)", new Date());
            }

            if (row.getCell(1) == null) {
                return new JsonResult(201, "导入失败(第" + (r + 1) + "行,手机号码未填写)", new Date());
            }
            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            String phone = row.getCell(1).getStringCellValue();
            if (!Pattern.compile("^1[0-9]{10}$").matcher(phone).matches()) {
                return new JsonResult(201, "导入失败(第" + (r + 1) + "行,无效的手机号码)", new Date());
            }

            //获取父级菜单的id
            Custom custom1 = new Custom();
            custom1.setPhone(phone);
            Object uniqueData = customService.selectOne(new EntityWrapper<>(custom1));
            if (uniqueData != null) {
                return new JsonResult(201, "导入失败(第" + (r + 1) + "行,手机号码已存在)", new Date());
            }


            if (row.getCell(2) != null) {
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                String email = row.getCell(2).getStringCellValue();
                custom.setEmail(email);
            }
            if (row.getCell(3) != null) {
                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                String address = row.getCell(3).getStringCellValue();
                custom.setAddress(address);
            }
            /*if (row.getCell(4) != null) {
                row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                String company = row.getCell(4).getStringCellValue();
                custom.setCompany(company);
            }*/
            if (row.getCell(4) != null) {
                row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                String custom_source = row.getCell(4).getStringCellValue();
                custom.setCustomSource(custom_source);
            }
            if (row.getCell(5) != null) {
                row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                String label = row.getCell(5).getStringCellValue();
                custom.setLabel(label);
            }
            if (row.getCell(6) != null) {
                row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                String note = row.getCell(6).getStringCellValue();
                custom.setNote(note);
            }

            Integer deptId = ShiroKit.getUser().getDeptId();
            String deptname = ShiroKit.getUser().getDeptName();
            custom.setCompany(deptname);
            custom.setCompanyId(deptId);

            custom.setCustomName(name);
            custom.setPhone(phone);
            custom.setCreatedTime(d);
            custom.setUpdatedTime(d);
            if (ShiroKit.hasRole("worker")) {       //坐席新增的自动分配给自己
                Integer userid = ShiroKit.getUser().getId();
                custom.setUserId(userid);
                custom.setStatus(2);
            }
            customList.add(custom);
        }

        for (Custom customResord : customList) {
//            customMapper.importCustom(customResord);
            customService.insert(customResord);
        }

        return new JsonResult(200, "导入成功", new Date());

    }

    /**
     * 根据项目名称获取项目数据
     * @param name
     * @return
     */
    @Override
    public Custom getByPhone(String phone) {
        Integer company = ShiroKit.getUser().getDeptId();
        return this.baseMapper.getByPhone(phone, company);
    }

    @Override
    public Integer updateStatusByIds(String[] ids) {
        return this.baseMapper.updateStatusByIds(ids);
    }

    @Override
    public Integer deleteCustomByIds(String[] ids) {
        return this.baseMapper.deleteCustomByIds(ids);
    }

    @Override
    public Integer assignCustom(String[] ids, Integer user_id, Date allocateTime){
        return this.baseMapper.assignCustom(ids, user_id, allocateTime);
    }

    @Override
    public List<Custom> selectAssignCustomList(String column, String condition,String beginTime,String endTime,String update_beginTime,String update_endTime ,Integer status, Integer id, Integer limit) {
        Integer product_id = ShiroKit.getUser().getProductId();
        Integer userid = null;
        if (ShiroKit.hasRole("worker")) {       //坐席角色  客户列表展示本公司且属于自己的  和我的客户展示一样
            userid = ShiroKit.getUser().getId();
        }

        if(userid != null && userid > 0) {
            Integer company = ShiroKit.getUser().getDeptId();   //获取当前用户公司id
            return this.baseMapper.selectAssignCustomList(userid,column,condition,beginTime,endTime,update_beginTime,update_endTime,status,product_id,id,limit);
        } else {
            if (ShiroKit.isAdmin()) {
                return this.baseMapper.selectAssignCustomList(userid, column, condition, beginTime, endTime, update_beginTime, update_endTime, status,null,id,limit);
            } else {
                return this.baseMapper.selectAssignCustomList(userid, column, condition, beginTime, endTime, update_beginTime, update_endTime, status,product_id,id,limit);
            }
        }
//        return this.baseMapper.selectAssignCustomList(column,condition,beginTime,endTime,update_beginTime,update_endTime,status);
    }

    @Override
    public Collection parseExcel(String fileName) throws Exception {
        File f = new File(fileName);
        InputStream inputStream= new FileInputStream(f);

        ExcelLogs logs = new ExcelLogs();
        Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);

        return importExcel;
    }

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionTemplate getSqlSessionTemplate()
    {
        return sqlSessionTemplate;
    }

    @Override
    public Integer insertBatchNew(List<Custom> members, Integer productid, String batch_no) throws Exception {
        // TODO Auto-generated method stub
        int result = 1;
        SqlSession batchSqlSession = null;
        try {
            batchSqlSession = this.getSqlSessionTemplate()
                    .getSqlSessionFactory()
                    .openSession(ExecutorType.BATCH, false);// 获取批量方式的sqlsession
            int batchCount = 5000;// 每批commit的个数
            int batchLastIndex = batchCount;// 每批最后一个的下标
            for (int index = 0; index < members.size();) {
                if (batchLastIndex >= members.size()) {
                    batchLastIndex = members.size();
                    result = result * batchSqlSession.insert("com.hjzddata.modular.custom.dao.CustomMapper.insertBatchNew",members.subList(index, batchLastIndex));
                    batchSqlSession.commit();
                    System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
                    break;// 数据插入完毕，退出循环
                } else {
                    result = result * batchSqlSession.insert("com.hjzddata.modular.custom.dao.CustomMapper.insertBatchNew",members.subList(index, batchLastIndex));
                    batchSqlSession.commit();
                    System.out.println("index:" + index+ " batchLastIndex:" + batchLastIndex);
                    index = batchLastIndex;// 设置下一批下标
                    batchLastIndex = index + (batchCount - 1);
                }
            }
            batchSqlSession.commit();
        }
        finally {
            batchSqlSession.close();
        }
        return result;
    }

    /**
     * 数据统计
     */
    @Override
    public Collection dataReport(String account, String batch_no, String beginTime, String endTime) {
        Integer product_id = ShiroKit.getUser().getProductId();
        Integer user_id = ShiroKit.getUser().getId();

        Integer dic_id = dictService.selectOne(new EntityWrapper<Dict>().and("name='已接通'")).getId();
        List<Dict> list = dictService.selectList(new EntityWrapper<Dict>().and("pid="+dic_id));
        String call_status = "0,";
        if (list != null) {
            for(Dict item : list) {
                call_status += item.getId() + ",";
            }
        }

        if (ShiroKit.isAdmin()) {
            return this.baseMapper.dataReport(account,batch_no,beginTime, endTime, null, null, call_status);
        } else {
            List<Integer> ids = new ArrayList<>();
            if (ShiroKit.hasRole("worker")) {
                ids.add(user_id);
            } else {         //除坐席角色外  展示整个项目的
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.and("productid=" + product_id + " and status=1");
                List<User> res = userService.selectList(entityWrapper);
                ids =res.stream().map(User::getId).collect(Collectors.toList());
            }
            return this.baseMapper.dataReport(account,batch_no,beginTime, endTime, product_id, ids, call_status);
        }
    }
}
