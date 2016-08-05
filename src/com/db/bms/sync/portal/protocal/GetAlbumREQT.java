
package com.db.bms.sync.portal.protocal;

import com.db.bms.utils.StringUtils;


public class GetAlbumREQT {

	public String serialNo;

	public String systemId;

	public String topicNo;

	public String albumNo;

	public static final int TYPE_TOPIC=0;
	public static final int TYPE_MENU=1;
	public Integer type;

	public Integer startPage;

	public Integer pageSize;

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

	public String getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(String topicId) {
		this.topicNo = topicId;
	}

	public String getAlbumNo() {
		return albumNo;
	}

	public void setAlbumNo(String albumId) {
		this.albumNo = albumId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
		return "GetAlbumREQT [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ", topicId=" + this.topicNo + ", albumId="
				+ this.albumNo + ", type=" + this.type + ", startPage="
				+ this.startPage + ", pageSize=" + this.pageSize + "]";
	}

	public GetAlbumRESP checkData() {
		GetAlbumRESP response = new GetAlbumRESP();
		response.setSerialNo(this.getSerialNo());
		response.setSystemId(this.getSystemId());
		response.setTopicNo(this.topicNo);
		response.setTotalCount(0);
		response.setTotalPage(0);
		response.setCurrentPage(1);
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Check data successfully.");

		if (StringUtils.isEmpty(this.serialNo)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [serialNo] parameter is invalid.");
			return response;
		}

		if (StringUtils.isEmpty(this.systemId)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [SystemId] parameter is invalid.");
			return response;
		}

		if (StringUtils.isEmpty(this.topicNo)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [topicNo] parameter is invalid.");
			return response;
		}

		if (this.startPage == null || this.startPage <= 0) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [startPage] parameter is invalid.");
			return response;
		}

		if (this.pageSize == null || this.pageSize <= 0) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [pageSize] parameter is invalid.");
			return response;
		}

		if (this.type != 0 && this.type != 1) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [type] parameter is invalid.");
			return response;
		}

		return response;

	}

}
