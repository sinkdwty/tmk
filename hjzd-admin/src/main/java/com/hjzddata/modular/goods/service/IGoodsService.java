package com.hjzddata.modular.goods.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.goods.model.Goods;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjzd123
 * @since 2018-07-02
 */
public interface IGoodsService extends IService<Goods> {

    List<Map<String, Object>> getGoods(Page<Goods> page, String goodsname, Integer isSale, String orderByField, boolean asc);

    Object batchImport(String fileName, MultipartFile file) throws Exception;

}
