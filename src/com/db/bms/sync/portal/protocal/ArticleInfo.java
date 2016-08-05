
package com.db.bms.sync.portal.protocal;

import java.util.ArrayList;
import java.util.List;


public class ArticleInfo {
	public Long articleNo;// 主键

	public Integer showOrder;

	public String articleId;

	public String title;

	public String subTitle;

	public String introduction;

	public String bodyPath;

	public List<ImageInfo> imageList;

	public String companyId;

	public String areaId;

	public String featureId;

	public Long templateNo;
	
	public String updateTime;
	
	//关联的策略集合
	public List<PublishStrategy> strategyArray;
	
	public ArticleInfo() {
		super();
		this.strategyArray = new ArrayList<PublishStrategy>();
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getBodyPath() {
		return bodyPath;
	}

	public void setBodyPath(String bodyPath) {
		this.bodyPath = bodyPath;
	}

	public List<ImageInfo> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageInfo> imageList) {
		this.imageList = imageList;
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

	public Long getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(Long templateId) {
		this.templateNo = templateId;
	}

	public List<PublishStrategy> getStrategyArray() {
		return strategyArray;
	}

	public void setStrategyArray(List<PublishStrategy> strategyArray) {
		this.strategyArray = strategyArray;
	}

	public Long getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(Long no) {
		this.articleNo = no;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
