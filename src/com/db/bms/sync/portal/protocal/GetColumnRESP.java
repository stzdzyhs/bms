
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;


public class GetColumnRESP {

	private String serialNo;
	
	private String systemId;
	
	private Integer totalCount;
	
	private Integer totalPage;
	
	private Integer currentPage;
	
	private String resultCode;
	
	private String resultDesc;
	
	private List<ColumnInfo> columnList;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
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

	public List<ColumnInfo> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnInfo> columnList) {
		this.columnList = columnList;
	}
	
	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		String ret = JSON.toJSONString(this);
		return this.getClass().getName() + " " + ret;
	}
 
}
