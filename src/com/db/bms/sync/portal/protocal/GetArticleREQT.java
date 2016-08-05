
package com.db.bms.sync.portal.protocal;

import com.db.bms.utils.StringUtils;


public class GetArticleREQT {

	private String serialNo;
	
	private String systemId;
	
	private String columnNo;
	
	private String articleNo;
	
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

	public String getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(String columnNo) {
		this.columnNo = columnNo;
	}

	public String getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
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
		return "GetArticleREQT [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ", columnId=" + this.columnNo + ", articleId=" + this.articleNo + ", startPage="
				+ this.startPage + ", pageSize=" + this.pageSize + "]";
	}

	public GetArticleRESP checkData() {
		GetArticleRESP response = new GetArticleRESP();
		response.setSerialNo(this.getSerialNo());
		response.setSystemId(this.getSystemId());
		response.setColumnNo(this.columnNo);
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
		
		if (StringUtils.isEmpty(this.columnNo)) {
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
