
package com.db.bms.sync.portal.protocal;

import java.util.ArrayList;
import java.util.List;


public class AlbumInfo {

	public Long albumNo; // 主键

	public Integer showOrder;

	public String albumId;

	public String assetId;

	public String albumTitle;

	public String albumCover;

	public String fileMd5;

	public String albumDesc;

	public String companyId;

	public String areaId;

	public String featureId;

	public Integer captureFlag;

	public String templateNo;
	
	public String updateTime;
	
	//关联的策略集合
	public List<PublishStrategy> strategyArray;
	
	public AlbumInfo() {
		super();
		this.strategyArray = new ArrayList<PublishStrategy>();
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public String getAlbumCover() {
		return albumCover;
	}

	public void setAlbumCover(String albumCover) {
		this.albumCover = albumCover;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getAlbumDesc() {
		return albumDesc;
	}

	public void setAlbumDesc(String albumDesc) {
		this.albumDesc = albumDesc;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public Integer getCaptureFlag() {
		return captureFlag;
	}

	public void setCaptureFlag(Integer captureFlag) {
		this.captureFlag = captureFlag;
	}

	public String getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(String templateId) {
		this.templateNo = templateId;
	}

	public List<PublishStrategy> getStrategyArray() {
		return strategyArray;
	}

	public void setStrategyArray(List<PublishStrategy> strategyArray) {
		this.strategyArray = strategyArray;
	}

	public Long getAlbumNo() {
		return albumNo;
	}

	public void setAlbumNo(Long no) {
		this.albumNo = no;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
