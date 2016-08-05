
package com.db.bms.sync.portal.protocal;

import com.alibaba.fastjson.JSON;


public class VideoCutNotifyREQT {

	private String serialNo;

	private String assetId;

	private String assetTitle;

	private String sourceUrl;

	private String userName;

	private String password;

	private Integer width;

	private Integer height;

	private Integer interval;

	private String reportUrl;

	private String injectImageUrl;

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

	public String getAssetTitle() {
		return assetTitle;
	}

	public void setAssetTitle(String assetTitle) {
		this.assetTitle = assetTitle;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getInjectImageUrl() {
		return injectImageUrl;
	}

	public void setInjectImageUrl(String injectImageUrl) {
		this.injectImageUrl = injectImageUrl;
	}

	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		return "VideoCutNotifyREQT [serialNo=" + this.serialNo + ", assetId="
				+ this.assetId + ", assetTitle=" + this.assetTitle
				+ ", sourceUrl=" + this.sourceUrl + ", userName=" + this.userName 
				+ ", password=" + this.password + ", width=" + this.width 
				+ ", interval=" + this.interval + ", reportUrl=" + this.reportUrl 
				+ ", injectImageUrl=" + this.injectImageUrl 
				+ "]";
	}
}
