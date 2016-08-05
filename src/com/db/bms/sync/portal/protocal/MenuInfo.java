
package com.db.bms.sync.portal.protocal;

import java.util.ArrayList;
import java.util.List;


public class MenuInfo {

	public Long menuNo; // 主键
	
	public String menuId;

	public String menuTitle;

	public Integer showOrder;

	public String companyId;

	public String areaId;

	public String featureId;
	//关联的策略集合
	public List<PublishStrategy> strategyArray;
	
	public MenuInfo() {
		super();
		this.strategyArray = new ArrayList<PublishStrategy>();
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

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	/**
	 * @return the strategyArray
	 */
	public List<PublishStrategy> getStrategyArray() {
		return strategyArray;
	}

	/**
	 * @param strategyArray the strategyArray to set
	 */
	public void setStrategyArray(List<PublishStrategy> strategyArray) {
		this.strategyArray = strategyArray;
	}

	public Long getMenuNo() {
		return menuNo;
	}

	public void setMenuNo(Long no) {
		this.menuNo = no;
	}


}
