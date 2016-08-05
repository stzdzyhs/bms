
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;


public class GetMenuRESP {

	private String serialNo;

	private String systemId;

	private String topicId;

	private Integer totalCount;

	private Integer totalPage;

	private Integer currentPage;

	private String resultCode;

	private String resultDesc;

	private List<MenuInfo> menuList;

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

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
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

	public List<MenuInfo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuInfo> menuList) {
		this.menuList = menuList;
	}

	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		return "GetMenuRESP [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ", topicId=" + this.topicId + ", totalCount="
				+ this.totalCount + ", totalPage=" + this.totalPage
				+ ", currentPage=" + this.currentPage + ", resultCode="
				+ this.resultCode + ", resultDesc=" + this.resultDesc
				+ ", menuList="
				+ (this.menuList != null ? this.menuList.size() : 0) + "]";
	}
}
