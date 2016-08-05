package com.db.bms.utils.core;

import java.util.ArrayList;
import java.util.List;

/** 
 * <b>Application name:</b>分页实现<br>
 * <b>Application describing:</b> <br>
 * <b>Date:</b>2014-7-9<br>
 * @version $Revision$
 */
public class PageUtil {

    private Integer pageId = 1; // 当前页

    private Integer rowCount = 0; // 总行数

    private Integer pageSize = 10; // 页大小

    private Integer pageCount = 0; // 总页数

    private Integer pageOffset = 0;// 当前页起始记录

    private Integer pageTail = 0; // 当前页到达的记录

    private String queryCondition; //自定义条件

    private String andCondition; // 条件

    private String orderByCondition; // 排序

    private boolean paging = false; //默认分页

    private boolean like = false; //默认模糊查询

    private boolean hasPreviousPage = false; //是否存在上一页

    private boolean hasNextPage = false; //是否存在下一页

    private List<Integer> pageList;
    
    private List<?> rslist;

    public void setPageInfo(Integer pageSize, Integer pageId) {
    	if(pageSize==null) pageSize=10;
    	if(pageId==null) pageId = 1;
    	
    	if(pageSize<1) {
    		throw new IllegalArgumentException("pageSize错误");
    	}
    	if(pageId<1) {
    		throw new IllegalArgumentException("pageId错误");
    	}
    	
    	this.pageSize = pageSize;
    	this.pageId = pageId; // pageId is from 1
    	
    	// set pageOffset accordingly!
    	this.pageOffset = (pageId-1)*pageSize; 
    }

    public boolean getLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean getPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public void splitPageInstance() {
        if (null == pageSize || pageSize < 1) {
            pageSize = 10;
        }
        // 总页数=(总记录数+每页行数-1)/每页行数
        pageCount = (rowCount + pageSize - 1) / pageSize;
        // 当前页大于总页数
        if (this.pageId == null) {
            this.pageId = 1;
        }
        if (pageId > pageCount) {
            pageId = pageCount;
        }
        // 防止 pageOffset 小于 0
        pageOffset = ((pageId - 1) * pageSize);
        if (pageOffset < 0)
            pageOffset = 0;
        if (this.pageId >= this.pageCount) {
            this.hasNextPage = false;
        }
        else {
            this.hasNextPage = true;
        }

        if ((this.pageId <= 1) || (this.pageCount == 0)) {
            this.hasPreviousPage = false;
        }
        else {
            this.hasPreviousPage = true;
        }
    }

    public String getLimit() {
        return " limit " + pageOffset + "," + pageSize;
    }

    public Integer getLastRowCount() {
        return pageOffset + pageSize;
    }

    public String getAndCondition() {
        return andCondition == null ? "" : " AND " + andCondition;
    }

    public String getOrderByCondition() {
        return orderByCondition == null ? "" : " order by " + orderByCondition;
    }

    public String getAllConditionAndLimit() {
        return (getQueryCondition() == null ? "" : getQueryCondition()) + getAndCondition() + getOrderByCondition()
                + getLimit();
    }

    // GET AND SET

    public Integer getPageId() {
        return pageId;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public void setAndCondition(String andCondition) {
        this.andCondition = andCondition;
    }

    public void addAndCondition(String andCondition) {
        if (this.andCondition == null || this.andCondition.isEmpty()) {
            this.andCondition = andCondition;
            return;
        }
        this.andCondition += " AND " + andCondition;
    }

    public void setOrderByCondition(String orderByCondition) {
        this.orderByCondition = orderByCondition;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
        splitPageInstance();
    }

    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageTail() {
        return pageTail;
    }

    public void setPageTail(Integer pageTail) {
        this.pageTail = pageTail;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<Integer> getPageList() {
        pageList = new ArrayList<Integer>(0);
        int count = 1;
        int total = pageCount;
        if (pageCount > 1000) {
            if ((pageId - 500) <= 0)
                count = 1;
            else
                count = pageId - 500;
            if ((pageId + 500) > pageCount) {
                total = pageCount;
            }
            else
                total = pageId + 500;
        }
        for (int i = count; i <= total; i++) {
            pageList.add(i);
        }
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }
    
	public void setRslist(List<?> rslist) {
		this.rslist = rslist;
	}
    
	public List<?> getPageRslist() {
		int lastRowCount = this.getLastRowCount() > this.rowCount ? this.rowCount : this.getLastRowCount();
		if (rslist==null) {
			return null;
		}
		return rslist.subList(this.pageOffset, lastRowCount);
	}
	
	public static int DEF_PAGE_SIZE=10;
	
	/**
	 * calculate total page
	 * @param totalCount
	 * @param pageSize
	 * @return
	 */
	public static int calTotalPage(int totalCount, Integer pageSize) {
		if (pageSize==null) {
			pageSize = DEF_PAGE_SIZE;
		}

		int totalPage = totalCount/pageSize;	
		if(totalCount%pageSize!=0) {
			totalPage++;
		}
		return totalPage;
	}
	

    public static void main(String[] args) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageId(2);
        pageUtil.setPageSize(10);
        pageUtil.setRowCount(10);
        System.out.println(pageUtil.getLimit());
        System.out.println(pageUtil.getPageCount());
    }
}
