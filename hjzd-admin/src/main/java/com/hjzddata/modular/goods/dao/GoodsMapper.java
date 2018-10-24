package com.hjzddata.modular.goods.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.modular.goods.model.Goods;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjzd123
 * @since 2018-07-02
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取登录日志
     */
    List<Map<String, Object>> getGoods(@Param("page") Page<Goods> page, @Param("goodsname") String goodsname, @Param("isSale") Integer isSale, @Param("orderByField") String orderByField, @Param("isAsc") boolean isAsc);

    void importGoods(Goods goods);
}
