package com.hjzddata.core.common.constant.factory;

import com.baomidou.mybatisplus.plugins.Page;
import com.hjzddata.core.common.constant.state.Order;
import com.hjzddata.core.support.HttpKit;
import com.hjzddata.core.util.ToolUtil;
import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
public class PageFactory<T> {

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpKit.getRequest();
        int limit = 10;
        int pageNum = 0;
        if (request.getParameter("limit") != null) {
            limit = Integer.valueOf(request.getParameter("limit"));     //每页多少条数据
        }
        if (request.getParameter("page") != null) {
            pageNum = Integer.valueOf(request.getParameter("page"));   //每页的偏移量(本页当前有多少条)
        }

        String sort = request.getParameter("sort");         //排序字段名称
        String order = request.getParameter("order");       //asc或desc(升序或降序)
        if (ToolUtil.isEmpty(sort)) {
            Page<T> page = new Page<>(pageNum, limit);
            page.setOpenSort(false);
            return page;
        } else {
            Page<T> page = new Page<>(pageNum, limit, sort);
            if (Order.ASC.getDes().equals(order)) {
                page.setAsc(true);
            } else {
                page.setAsc(false);
            }
            return page;
        }
    }
}
