package com.db.bms.sync.portal.protocal;

import com.alibaba.fastjson.JSON;
import com.db.bms.utils.StringUtils;

public class GetPageREQT {

	private String serialNo;

	private String systemId;

	private String columnId;

	private String pageId;

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

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
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

	public static GetPageREQT fromJsonString(String js) {
		GetPageREQT req = JSON.parseObject(js, GetPageREQT.class);
		return req;
	}

	public String build() {
		String ret = JSON.toJSONString(this);
		return ret;
	}

	public GetPageRESP checkData(GetPageRESP response) {
		response.setSerialNo(this.getSerialNo());
		response.setSystemId(this.getSystemId());
		response.setTotalCount(0);
		response.setTotalPage(0);
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

		if (StringUtils.isEmpty(this.columnId)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM
					.getResultCode());
			response.setResultDesc("The [columnId] parameter is invalid.");
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
