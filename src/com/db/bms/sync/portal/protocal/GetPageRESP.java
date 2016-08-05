
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;


public class GetPageRESP {

	private String systemId;

	private String serialNo;

	private String columnId;

	private String columnTitle;

	private String columnDesc;

	private String columnCover;

	private String fileMd5;

	private Integer totalCount;

	private Integer totalPage;

	private Integer currentPage;

	private String resultCode;

	private String resultDesc;

	private List<PageInfo> pageList;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getColumnTitle() {
		return columnTitle;
	}

	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}

	public String getColumnDesc() {
		return columnDesc;
	}

	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}

	public String getColumnCover() {
		return columnCover;
	}

	public void setColumnCover(String columnCover) {
		this.columnCover = columnCover;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public List<PageInfo> getPageList() {
		return pageList;
	}

	public void setPageList(List<PageInfo> pageList) {
		this.pageList = pageList;
	}

	public String build() {
		String ret = JSON.toJSONString(this);
		return ret;
	}

	@Override
	public String toString() {
		String ret = JSON.toJSONString(this);
		return this.getClass().getName() + " " + ret;
	}

}
