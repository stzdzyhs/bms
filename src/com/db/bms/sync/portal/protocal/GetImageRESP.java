
package com.db.bms.sync.portal.protocal;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.db.bms.utils.core.PageUtil;


public class GetImageRESP {

	private String serialNo;
	
	private String systemId;
	
	//added by MiaoJun on April.25,2016
	private String topicNo;
	
	private String albumNo;
	
	private Integer totalCount;
	
	private Integer totalPage;
	
	private Integer currentPage;
	
	private String resultCode;
	
	private String resultDesc;
	
	private List<ImageInfo> imageList;

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

	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}

	public String getAlbumNo() {
		return albumNo;
	}

	public void setAlbumNo(String albumNo) {
		this.albumNo = albumNo;
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

	public List<ImageInfo> getImageList() {
		return imageList;
	}

	/**
	 * set image list
	 * @param imageList - the page data
	 * @param total - total
	 * @param pageSize - page size
	 */
	public void setImageList(List<ImageInfo> imageList, Integer total, Integer pageSize) {
		this.imageList = imageList;

		this.totalCount = total;
		this.totalPage = PageUtil.calTotalPage(this.totalCount, pageSize);		
	}
	
	public String build() {
		String s = JSON.toJSONString(this);
		return s;
	}

	@Override
	public String toString() {
		return "GetImageRESP [serialNo=" + this.serialNo + ", systemId="
		+ this.systemId + ",topicId="+this.topicNo + ", albumId=" + this.albumNo
		+ ", totalCount=" + this.totalCount + ", totalPage="
		+ this.totalPage + ", currentPage="
		+ this.currentPage + ", resultCode="
		+ this.resultCode + ", resultDesc="
		+ this.resultDesc + ", imageList="
		+ (this.imageList != null ? this.imageList.size() : 0) + "]";
	}


}
