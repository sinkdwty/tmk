package com.hjzddata.modular.productCallCode.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 项目通话状态映射表
 * </p>
 *
 * @author hjzd
 * @since 2018-10-24
 */
@TableName("hj_product_call_map")
public class ProductCallMap extends Model<ProductCallMap> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @TableField("product_id")
    private Integer productId;
    /**
     * 字段ID
     */
    @TableField("call_status_id")
    private Integer callStatusId;


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCallStatusId() {
        return callStatusId;
    }

    public void setCallStatusId(Integer callStatusId) {
        this.callStatusId = callStatusId;
    }

    @Override
    protected Serializable pkVal() {
        return this.productId;
    }

    @Override
    public String toString() {
        return "ProductCallMap{" +
        "productId=" + productId +
        ", callStatusId=" + callStatusId +
        "}";
    }
}
