package com.wr.utils;

import com.github.pagehelper.PageHelper;
import com.wr.web.page.PageDomain;
import com.wr.web.page.TableSupport;

/**
 * 分页工具类
 *
 * @author wr
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageCurrent = pageDomain.getPageCurrent();
        Integer pageSize = pageDomain.getPageSize();
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageCurrent, pageSize).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
