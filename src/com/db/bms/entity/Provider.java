
package com.db.bms.entity;

import com.db.bms.entity.BaseModel;


public class Provider extends BaseModel {

	private Long providerNo;

	private String providerId;

	private String providerName;

	private String createTime;

	private String updateTime;

	public Long getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(Long providerNo) {
		this.providerNo = providerNo;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public void setDefaultNull() {

	}

}
