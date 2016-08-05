
package com.db.bms.sync.portal.protocal;

import com.alibaba.fastjson.JSON;


public class InjectVideoImageRESP {

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

	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		String s = JSON.toJSONString(this);
		return s;
		/*
		return "InjectVideoImageRESP [serialNo=" + this.serialNo + ", assetId="
				+ this.assetId + ", resultCode=" + this.resultCode
				+ ", resultDesc=" + this.resultDesc + "]";
		*/
	}

}
