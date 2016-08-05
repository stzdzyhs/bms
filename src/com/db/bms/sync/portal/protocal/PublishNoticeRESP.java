
package com.db.bms.sync.portal.protocal;



public class PublishNoticeRESP {

	private String serialNo;
	
	private String systemId;
	
	private String resultCode;
	
	private String resultDesc;

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
	
	@Override
	public String toString() {
		return "PublishNoticeRESP [serialNo=" + this.serialNo + ", systemId="
		+ this.systemId + ", resultCode=" + this.resultCode + ", resultDesc="
		+ this.resultDesc + "]";
	}
	
	

}
