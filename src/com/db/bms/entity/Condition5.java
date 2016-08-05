package com.db.bms.entity;

import com.db.bms.utils.StringUtils;


/**
 * condition five
 */
public class Condition5 extends BaseModel {
	
	/**
	 * 区域码
	 */
	public String regionCode;
	/**
	 * 网络id
	 */
	public String networkId;
	/**
	 * 空分组
	 */
	public String tsId;
	/**
	 * CA 卡号
	 */
	public String client;
	/**
	 * 特征码 ，分隔
	 */
	private String featureCode;
	public String[] featureCodeList;
	

	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getTsId() {
		return tsId;
	}
	public void setTsId(String tsId) {
		this.tsId = tsId;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
		if(StringUtils.isEmpty(featureCode)) {
			featureCodeList = featureCode.split(",");
		}
		else {
			featureCodeList = null;
		}
	}
	
	
	@Override
	public void setDefaultNull() {
	}

}
