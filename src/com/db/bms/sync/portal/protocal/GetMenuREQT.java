
package com.db.bms.sync.portal.protocal;

import com.db.bms.utils.StringUtils;


public class GetMenuREQT {

	private String serialNo;

	private String systemId;

	private String topicId;

	private String menuId;

	private Integer startPage;

	private Integer pageSize;

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

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "GetMenuREQT [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ", topicId=" + this.topicId
				+ ", menuId=" + this.menuId + ", startPage=" + this.startPage
				+ ", pageSize=" + this.pageSize + "]";
	}

	public GetMenuRESP checkData() {
		GetMenuRESP response = new GetMenuRESP();
		response.setSerialNo(this.getSerialNo());
		response.setSystemId(this.getSystemId());
		response.setTopicId(this.topicId);
		response.setTotalCount(0);
		response.setTotalPage(0);
		response.setCurrentPage(1);
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Check data successfully.");

		if (StringUtils.isEmpty(this.serialNo)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [serialNo] parameter is invalid.");
			return response;
		}

		if (StringUtils.isEmpty(this.systemId)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [SystemId] parameter is invalid.");
			return response;
		}

		if (StringUtils.isEmpty(this.topicId)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [topicId] parameter is invalid.");
			return response;
		}

		if (this.startPage == null || this.startPage <= 0) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [startPage] parameter is invalid.");
			return response;
		}

		if (this.pageSize == null || this.pageSize <= 0) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [pageSize] parameter is invalid.");
			return response;
		}

		return response;

	}

}
