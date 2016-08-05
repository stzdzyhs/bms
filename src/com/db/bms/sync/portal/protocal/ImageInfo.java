
package com.db.bms.sync.portal.protocal;

import java.util.ArrayList;
import java.util.List;

import com.db.bms.utils.Regexs;
import com.db.bms.utils.RegexsUtils;
import com.db.bms.utils.StringUtils;


public class ImageInfo {

	public Long imageNo;// 主键
	
	public String imageId;

	public String imageTitle;

	public String imageUrl;

	public String imageDesc;

	public String fileMd5;

	public String imageTime;

	public Integer frameNum;

	public String companyId;

	public String areaId;

	public String featureId;
	
	public Integer showOrder;
	
	public String updateTime;
	
	//关联的策略集合
	public List<PublishStrategy> strategyArray;
	

	public ImageInfo() {
		super();
		this.strategyArray = new ArrayList<PublishStrategy>();
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageTitle() {
		return imageTitle;
	}

	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getImageTime() {
		return imageTime;
	}

	public void setImageTime(String imageTime) {
		this.imageTime = imageTime;
	}

	public Integer getFrameNum() {
		return frameNum;
	}

	public void setFrameNum(Integer frameNum) {
		this.frameNum = frameNum;
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

	public List<PublishStrategy> getStrategyArray() {
		return strategyArray;
	}

	public void setStrategyArray(List<PublishStrategy> strategyArray) {
		this.strategyArray = strategyArray;
	}

	public boolean checkData() {

		if (StringUtils.isEmpty(this.imageTitle)) {
			return false;
		}
		if (this.imageTitle.length() > 30) {
			return false;
		}
		if (!RegexsUtils.checkData(this.imageTitle, Regexs.ASSET_TITLE)) {
			return false;
		}

		if (StringUtils.isNotEmpty(this.imageDesc)
				&& this.imageDesc.length() > 300) {
			return false;
		}

		if (StringUtils.isEmpty(this.imageUrl)) {
			return false;
		}
		if (!RegexsUtils.checkData(this.imageUrl, Regexs.URL)) {
			return false;
		}

		if (StringUtils.isEmpty(this.fileMd5)) {
			return false;
		}
		if (this.fileMd5.length() > 200) {
			return false;
		}

		if (StringUtils.isEmpty(this.imageTime)) {
			return false;
		}
		if (!RegexsUtils.checkData(this.imageTime, Regexs.VIDEO_TIME)) {
			return false;
		}

		if (this.frameNum == null) {
			return false;
		}
		if (!RegexsUtils.checkData(String.valueOf(this.frameNum),
				Regexs.FRAME_NUM)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ImageInfo [imageTitle=" + this.imageTitle + ", imageUrl="
				+ this.imageUrl + ", fileMd5=" + this.fileMd5 + ", imageDesc="
				+ this.imageDesc + ", imageTime=" + this.imageTime
				+ ", frameNum=" + this.frameNum + "]";
	}

	public Long getImageNo() {
		return imageNo;
	}

	public void setImageNo(Long no) {
		this.imageNo = no;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
