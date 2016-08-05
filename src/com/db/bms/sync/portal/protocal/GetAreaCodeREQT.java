
package com.db.bms.sync.portal.protocal;

import com.db.bms.utils.StringUtils;


public class GetAreaCodeREQT {

	private String serialNo;

	private String systemId;

	private String companyId; 
	
	private String internetId;  
	
	private String clientId;
	 
	private String spaceId;


	private Integer startPage;

	private Integer pageSize;
 
	

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSpaceId() {
		return spaceId;
	}
	
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
    
	public String getInternetId() {
		return internetId;
	}

	public void setInternetId(String internetId) {
		this.internetId = internetId;
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
		return "GetAreaCodeREQT [serialNo=" + this.serialNo + ", systemId="
		+ this.systemId + ", companyId=" + this.companyId + ", startPage="
		+ this.startPage + ", pageSize=" + this.pageSize + "]";
	}
	
	public GetAreaCodeRESP checkData() {
		GetAreaCodeRESP response = new GetAreaCodeRESP();
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
