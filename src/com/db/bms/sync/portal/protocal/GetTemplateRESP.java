
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;


public class GetTemplateRESP {

	private String serialNo;

	private String systemId;

	private String templateId;

	private Integer totalCount;

	private Integer totalPage;

	private Integer currentPage;

	private String resultCode;

	private String resultDesc;

	private List<TemplateInfo> templateList;

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

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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

	public List<TemplateInfo> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<TemplateInfo> templateList) {
		this.templateList = templateList;
	}
	
	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		return "GetTemplateRESP [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ", templateId=" + this.templateId + ", totalCount="
				+ this.totalCount + ", totalPage=" + this.totalPage
				+ ", currentPage=" + this.currentPage + ", resultCode="
				+ this.resultCode + ", resultDesc=" + this.resultDesc
				+ ", templateList="
				+ (this.templateList != null ? this.templateList.size() : 0) + "]";
	}
}
