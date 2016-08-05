
package com.db.bms.sync.portal.protocal;



public class VideoCutNotifyRESP {

	private String serialNo;
	
	private String assetId;
	
	private String resultCode;
	
	private String resultDesc;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
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
		return "VideoCutNotifyRESP [serialNo=" + this.serialNo + ", assetId="
		+ this.assetId + ", resultCode=" + this.resultCode + ", resultDesc="
		+ this.resultDesc + "]";
	}
}
