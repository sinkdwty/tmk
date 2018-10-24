package com.hjzddata.modular.goods.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.common.model.JsonResult;
import com.hjzddata.modular.goods.model.Goods;
import com.hjzddata.modular.goods.dao.GoodsMapper;
import com.hjzddata.modular.goods.service.IGoodsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjzd123
 * @since 2018-07-02
 */
@Service
@Transactional(readOnly = true)
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<Map<String, Object>> getGoods(Page<Goods> page, String goodsname, Integer isSale, String orderByField, boolean asc) {
        return this.baseMapper.getGoods(page, goodsname, isSale, orderByField, asc);
    }

    /**
     * 商品导入
     * @param fileName
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public Object batchImport(String fileName, MultipartFile file) throws Exception {

        boolean notNull = false;
        List<Goods> goodsList = new ArrayList<Goods>();
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
        System.out.println(sheet.getLastRowNum());
        if (sheet.getLastRowNum() != 0) {
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                System.out.println(sheet.getLastRowNum());
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                Goods goods = new Goods();

                if (row.getCell(0) == null) {
                    return new JsonResult(201, "导入失败(第" + (r + 1) + "行,商品名称未填写)", new Date());
                }

                if (row.getCell(1) == null) {
                    return new JsonResult(201, "导入失败(第" + (r + 1) + "行,商品价格未填写)", new Date());
                }

                if (row.getCell(2) == null) {
                    return new JsonResult(201, "导入失败(第" + (r + 1) + "行,库存未填写)", new Date());
                }
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                String name = row.getCell(0).getStringCellValue();

                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                String goodsPrice = row.getCell(1).getStringCellValue();

                /* 类型转换，判断商品价格是否为正数 */
                if (Integer.valueOf(goodsPrice).intValue() < 0) {
                    return new JsonResult(201, "导入失败(第" + (r + 1) + "行,商品价格请填写正数)", new Date());
                }

                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                String stockNum = row.getCell(2).getStringCellValue();
                if (!Pattern.compile("^[-\\+]?[\\d]*$").matcher(stockNum).matches()) {
                    return new JsonResult(201, "导入失败(第" + (r + 1) + "行,无效的库存)", new Date());
                }

                /* 类型转换，判断商品价格是否为正数 */
                if (Integer.valueOf(stockNum).intValue() < 0) {
                    return new JsonResult(201, "导入失败(第" + (r + 1) + "行,库存量请填写正数)", new Date());
                }

                if (row.getCell(3) != null) {
                    row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                    String url = row.getCell(3).getStringCellValue();
                    goods.setUrl(url);
                }


                if (row.getCell(4) != null) {
                    row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                    String note = row.getCell(4).getStringCellValue();
                    goods.setNote(note);
                }

                if (row.getCell(5) != null) {
                    row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                    String goodsNote = row.getCell(5).getStringCellValue();
                    goods.setGoodsNote(goodsNote);
                }

                goods.setGoodsName(name);
                goods.setGoodsPrice(goodsPrice);
                goods.setStockNum(Integer.parseInt(stockNum));
                goods.setCreatedAt(d);
                goods.setUpdatedAt(d);
                goods.setBatchNo(sdf.format(d));
                goodsList.add(goods);
            }

            for (Goods goodResord : goodsList) {
                goodsMapper.importGoods(goodResord);
            }

            return new JsonResult(200, "导入成功", new Date());
        } else {
            return new JsonResult(201, "表格中没有数据，请检查表格内是否有数据", new Date());
        }
    }


}
