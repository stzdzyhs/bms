
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;


public class GetAlbumRESP {

	private String serialNo;

	private String systemId;

	private String topicNo;

	private Integer totalCount;

	private Integer totalPage;

	private Integer currentPage;

	private String resultCode;

	private String resultDesc;

	private List<AlbumInfo> albumList;

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

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
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

	public List<AlbumInfo> getAlbumList() {
		return albumList;
	}

	public void setAlbumList(List<AlbumInfo> albumList) {
		this.albumList = albumList;
	}

	public String build() {
		String s = JSON.toJSONString(this);
		return s;		
	}

	@Override
	public String toString() {
		return "GetAlbumRESP [serialNo=" + this.serialNo + ", systemId="
				+ this.systemId + ", topicId=" + this.topicNo + ", totalCount="
				+ this.totalCount + ", totalPage=" + this.totalPage
				+ ", currentPage=" + this.currentPage + ", resultCode="
				+ this.resultCode + ", resultDesc=" + this.resultDesc
				+ ", albumList="
				+ (this.albumList != null ? this.albumList.size() : 0) + "]";
	}

}
