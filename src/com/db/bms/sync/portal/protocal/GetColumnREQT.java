package com.db.bms.sync.portal.protocal;

import com.alibaba.fastjson.JSON;
import com.db.bms.utils.StringUtils;


public class GetColumnREQT {

	private String serialNo;

	private String systemId;

	private String columnNo;

	private Integer startPage;

	private Integer pageSize;

	public static GetColumnREQT fromJsonString(String js) {
		GetColumnREQT req = JSON.parseObject(js, GetColumnREQT.class);
		return req;
	}

	public String build() {
		String ret = JSON.toJSONString(this);
		return ret;
	}

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

	public String getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(String columnNo) {
		this.columnNo = columnNo;
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
		String ret = JSON.toJSONString(this);
		return "GetColumnREQT " + ret;
	}

	public GetColumnRESP checkData(GetColumnRESP response) {
		response.setSerialNo(this.getSerialNo());
		response.setSystemId(this.getSystemId());
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
