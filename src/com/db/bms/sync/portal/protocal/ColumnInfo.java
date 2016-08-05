package com.db.bms.sync.portal.protocal;

import java.util.ArrayList;
import java.util.List;


public class ColumnInfo {
	
	public Long columnNo;// 主键

	public Integer showOrder;

	public String columnId;

	public Long parentColumnNo;

	public String columnTitle;

	public String columnDesc;

	public String columnCover;

	public String fileMd5;

	public String companyId;

	public String areaId;

	public String featureId;

	public Long templateNo;
	
	public String updateTime;
	
	//关联的策略集合
	public List<PublishStrategy> strategyArray;
	
	
	public ColumnInfo() {
		super();
		this.strategyArray = new ArrayList<PublishStrategy>();
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public Long getParentColumnNo() {
		return parentColumnNo;
	}

	public void setParentColumnNo(Long parentId) {
		this.parentColumnNo = parentId;
	}

	public String getColumnTitle() {
		return columnTitle;
	}

	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}

	public String getColumnDesc() {
		return columnDesc;
	}

	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}

	public String getColumnCover() {
		return columnCover;
	}

	public void setColumnCover(String columnCover) {
		this.columnCover = columnCover;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
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

	public Long getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(Long templateNo) {
		this.templateNo = templateNo;
	}

	public List<PublishStrategy> getStrategyArray() {
		return strategyArray;
	}

	public void setStrategyArray(List<PublishStrategy> strategyArray) {
		this.strategyArray = strategyArray;
	}

	public Long getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Long columnNo) {
		this.columnNo = columnNo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
