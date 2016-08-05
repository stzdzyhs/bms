
package com.db.bms.sync.portal.protocal;

import com.db.bms.utils.StringUtils;


public class GetImageREQT {

	private String serialNo;

	private String systemId;
	//added by MiaoJun on April.25,2016
	// this param is not used any more
	private String topicNo;

	private String albumNo;

	private String imageNo;

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

	public String getAlbumNo() {
		return albumNo;
	}

	public String getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}

	public void setAlbumNo(String albumNo) {
		this.albumNo = albumNo;
	}

	public String getImageNo() {
		return imageNo;
	}

	public void setImageNo(String imageNo) {
		this.imageNo = imageNo;
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
		return "GetImageREQT [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ",topicId="+this.topicNo +", albumId="
				+ this.albumNo + ", imageId=" + this.imageNo + ", startPage=" + this.startPage
				+ ", pageSize=" + this.pageSize + "]";
	}

	public GetImageRESP checkData() {
		GetImageRESP response = new GetImageRESP();
		response.setSerialNo(this.getSerialNo());
		response.setSystemId(this.getSystemId());
		response.setTopicNo(topicNo);
		response.setAlbumNo(this.albumNo);
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
		
		/* 
		if (StringUtils.isEmpty(this.topicNo)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [topicId] parameter is invalid.");
			return response;
		}
		*/

		if (StringUtils.isEmpty(this.albumNo)) {
			response.setResultCode(CommonResultCode.INVALID_PARAM.getResultCode());
			response.setResultDesc("The [albumId] parameter is invalid.");
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

		return response;

	}
}
