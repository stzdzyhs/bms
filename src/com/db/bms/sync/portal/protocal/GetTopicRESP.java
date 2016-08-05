
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;


public class GetTopicRESP {

	public String serialNo;
	
	public String systemId;
	
	public Integer totalCount;
	
	public Integer totalPage;
	
	public Integer currentPage;
	
	public String resultCode;
	
	public String resultDesc;
	
	public List<TopicInfo> topicList;

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

	public List<TopicInfo> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<TopicInfo> topicList) {
		this.topicList = topicList;
	}
	
	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		return "GetTopicRESP [serialNo=" + this.serialNo + ", systemId="
		+ this.systemId + ", totalCount=" + this.totalCount + ", totalPage="
		+ this.totalPage + ", currentPage="
		+ this.currentPage + ", resultCode="
		+ this.resultCode + ", resultDesc="
		+ this.resultDesc + ", topicList="
		+ (this.topicList != null ? this.topicList.size() : 0) + "]";
	}
 
}
