
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.db.bms.entity.PortalPublishNotice;


public class PublishNoticeREQT {

	private String serialNo;

	private String systemId;

	private List<PortalPublishNotice> resourceList;

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

	public List<PortalPublishNotice> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<PortalPublishNotice> resourceList) {
		this.resourceList = resourceList;
	}

	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		return "PublishNoticeREQT [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ", resourceList=" + this.resourceList.size() + "]";
	}
}
