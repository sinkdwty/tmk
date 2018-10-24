package com.hjzddata.modular.custom.controller;

import com.hjzddata.config.properties.HjzdProperties;
import com.hjzddata.core.base.controller.BaseController;
import com.hjzddata.core.util.DateUtil;
import com.hjzddata.core.util.ToolUtil;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.custom.service.ICustomService;
import com.hjzddata.modular.system.service.IDictService;
import com.hjzddata.modular.system.service.IUserService;
import com.hjzddata.modular.task.service.IBatchService;
import com.hjzddata.modular.task.service.ICallLogService;
import com.hjzddata.modular.task.service.IProductService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 数据报表控制器
 *
 * @Date 2018-07-10 11:35:39
 */
@Controller
@RequestMapping("/data-report")
public class DataReportController extends BaseController {

    private String PREFIX = "/data-report/data-report/";

    @Autowired
    private ICustomService customService;

    @Autowired
    private IDictService iDictService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICallLogService callLogService;

    @Autowired
    private IProductService productService;

    @Autowired
    private HjzdProperties hjzdProperties;

    @Autowired
    private IBatchService batchService;


    @RequestMapping("")
    public String index() {
        return PREFIX + "data-model.html";
    }

    @RequestMapping("/data-report")
    @ResponseBody
    public Object dataReport(@RequestParam(required = false) String account, @RequestParam(required = false) String batch_no, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime) {
        Collection res = customService.dataReport(account, batch_no, beginTime, endTime);
        /*if (res != null) {
            for (Object item : res) {
                Map entry = (Map)item;

                Double performance_success_rate = entry.get("performance_get_nums")!=null ? (BigDecimal)entry.get("performance_success_nums")/(BigDecimal)entry.get("performance_get_nums") : 0;
                ((Map) item).put("performance_success_rate", performance_success_rate);
            }
        }*/
        return new JsonResult(0,"成功", res);
    }


    /**
     * @throws Exception
     *
     *   根据模板导出excel，  这种方法是 通过给模板中指定的位置赋值，然后重新生成excel，来导出excel的，
     *    而且不会修改模板本身。
     *    感觉不太合适去导出有大量数据的excel。
     *    POIFSFileSystem： poi里面的类，
     *    可以把Excel文件作为数据流来进行传入传出。这里介绍下POIFSFileSystem类，这个类是专门用来读取excel表格的。
     *
     */
    @RequestMapping(value = "/export-report")
    @ResponseBody
    public void exportXls(@RequestParam(required = false) String account,@RequestParam(required = false) String batch_no,@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, HttpServletResponse response) throws Exception {
        Collection list = customService.dataReport(account, batch_no, beginTime, endTime);
        // 读取项目根目录中的模板
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("xls/data-report.xls");

//        File file = new File("E:\\template.xls");   电脑硬盘上的模板,,一般模板都在项目里
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
        HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet sheet = workbook.getSheetAt(0);
        sheet.setForceFormulaRecalculation(true);

        // 根据模板中表达式位置， 修改对应单元格的值
        // 第四行数据
        HSSFRow row = sheet.createRow(3);
//        for (int i = 0; i < list.size(); i++) {
        Integer i = 0;
        for (Object item : list) {
            Map entry = (Map)item;
//            值 = entry.get(‘字段名’);
            row = sheet.createRow(i + 3);

            // 导入对应列的数据
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(i+1);

            HSSFCell cell1 = row.createCell(1);
            String person_account = entry.get("person_account") == null ? "" : entry.get("person_account") + "";
            cell1.setCellValue(person_account);

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(entry.get("data_source") + "");

            HSSFCell cell3 = row.createCell(3);
            String data_batch = entry.get("data_batch") == null ? "" : entry.get("data_batch") + "";
            cell3.setCellValue(data_batch);

            HSSFCell cell4 = row.createCell(4);
            String data_importer = entry.get("data_importer") == null ? "" : entry.get("data_importer") + "";
            cell4.setCellValue(data_importer);

            HSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(entry.get("data_import_time") + "");

            HSSFCell cell6 = row.createCell(6);
            String data_call_num = entry.get("performance_get_nums") == null ? "" : entry.get("performance_get_nums") + "";
            cell6.setCellValue(data_call_num);

            HSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(entry.get("performance_called_nums") == null ? "" : entry.get("performance_called_nums") + "");

            HSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(entry.get("performance_succ_called_nums") == null ? "" : entry.get("performance_succ_called_nums") + "");

            HSSFCell cell9 = row.createCell(9);
            cell9.setCellValue(entry.get("performance_success_nums") == null ? "" : entry.get("performance_success_nums") + "");

            HSSFCell cell10 = row.createCell(10);
            long performance_succ_called_nums = entry.get("performance_succ_called_nums") == null ? 0 : Long.parseLong(entry.get("performance_succ_called_nums").toString());
            long performance_success_nums = entry.get("performance_success_nums") == null ? 0 : Long.parseLong(entry.get("performance_success_nums").toString());
            String performance_success_rate = "";
            if (performance_success_nums == 0) {
                performance_success_rate = "0";
            } else {
                if (performance_succ_called_nums == 0) {
                    performance_success_rate = "0";
                } else {
                    double percent = (double)performance_success_nums / performance_succ_called_nums;
                    DecimalFormat format = new DecimalFormat("0.00%");
                    performance_success_rate = format.format(percent);
                }
            }
            cell10.setCellValue(performance_success_rate);

            HSSFCell cell11 = row.createCell(11);
            String total_call_second = entry.get("total_call_second") == null ? "" : entry.get("total_call_second") + "";
            cell11.setCellValue(total_call_second);

            HSSFCell cell12 = row.createCell(12);
            String performance_callLog_nums = entry.get("performance_callLog_nums") == null ? "" : entry.get("performance_callLog_nums") + "";
            cell12.setCellValue(performance_callLog_nums);

            HSSFCell cell13 = row.createCell(13);
            String data_succ_callLog_num = entry.get("data_succ_callLog_num") == null ? "" : entry.get("data_succ_callLog_num") + "";
            cell13.setCellValue(data_succ_callLog_num);

            HSSFCell cell14 = row.createCell(14);
            String data_fail_call_num = entry.get("data_fail_call_num") == null ? "" : entry.get("data_fail_call_num") + "";
            cell14.setCellValue(data_fail_call_num);

            HSSFCell cell15 = row.createCell(15);
            long performance_callLog_nums_new = entry.get("performance_callLog_nums") == null ? 0 : Long.parseLong(entry.get("performance_callLog_nums").toString());
            long data_succ_callLog_num_new = entry.get("data_succ_callLog_num") == null ? 0 : Long.parseLong(entry.get("data_succ_callLog_num").toString());
            String data_succ_call_rate = "";
            if(performance_callLog_nums_new == 0) {
                data_succ_call_rate = "0";
            } else {
                if(data_succ_callLog_num_new == 0) {
                    data_succ_call_rate = "0";
                } else {
                    Double percent = (double)data_succ_callLog_num_new / performance_callLog_nums_new;
                    DecimalFormat format = new DecimalFormat("0.00%");
                    data_succ_call_rate = format.format(percent);
                }
            }
            cell15.setCellValue(data_succ_call_rate);

            HSSFCell cell16 = row.createCell(16);
            String total_ring_second = entry.get("total_ring_second") == null ? "" : entry.get("total_ring_second") + "";
            cell16.setCellValue(total_ring_second);

            HSSFCell cell17 = row.createCell(17);
            String process_man_hour = entry.get("process_man_hour") == null ? "" : entry.get("process_man_hour") + "";
            cell17.setCellValue(process_man_hour);

            HSSFCell cell18 = row.createCell(18);
            String process_avg_answer = entry.get("process_avg_answer") == null ? "" : entry.get("process_avg_answer") + "";
            cell18.setCellValue(process_avg_answer);

            HSSFCell cell19 = row.createCell(19);
            String process_avg_call = entry.get("process_avg_call") == null ? "" : entry.get("process_avg_call") + "";
            cell19.setCellValue(process_avg_call);

            HSSFCell cell20 = row.createCell(20);
            String process_att = entry.get("process_att") == null ? "" : entry.get("process_att") + "";
            cell20.setCellValue(process_att);

            i++;
        }


        /*HSSFCell cell = sheet.getRow(2).getCell(0);//第3行 第1列， 下标是0开始的
        cell.setCellValue("张三");
        HSSFCell cell2 = sheet.getRow(2).getCell(1);
        cell2.setCellValue(23);
        sheet.getRow(2).getCell(2).setCellValue("180cm");
        sheet.getRow(2).getCell(3).setCellValue("未婚");
        sheet.getRow(2).getCell(4).setCellValue("男");*/
        //修改模板内容导出新模板
        // '/' 分隔符 采用 File.separator 统一表示
        String xls_name = "数据报表"+ DateUtil.getAllTime() +".xls";

        //响应到客户端
        try {
            FileOutputStream out = new FileOutputStream(hjzdProperties.getFileUploadPath() + File.separator+ xls_name);
            workbook.write(out);
            out.flush();
            out.close();
            ToolUtil.setResponseHeader(response, xls_name);
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
