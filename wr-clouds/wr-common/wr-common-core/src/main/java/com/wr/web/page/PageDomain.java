package com.wr.web.page;

/**
 * 分页数据
 *
 * @author wr
 */
public class PageDomain
{
    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 分页参数合理化 */
    private Boolean reasonable = true;


    public Integer getPageCurrent()
    {
        return pageNum;
    }

    public void setPageCurrent(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public Boolean getReasonable()
    {
        if (reasonable == null)
        {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    public void setReasonable(Boolean reasonable)
    {
        this.reasonable = reasonable;
    }
}
