package com.db.bms.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Internet extends BaseModel implements Serializable {
     
	/*
	 * 主键ID
	 */
	public Long internetNo;
	/* 
	 * 网络ID
	 */
	public String internetId;
	/* 
	 * 网络名称
	 */
	public String internetName;
	/* 
	 * 网络描述
	 */
	public String internetDescribe;
	/* 
	 * 创建者
	 */
	public Long createBy;
	/* 
	 * 创建时间
	 */
	public String createTime;
	/* 
	 * 父级ID
	 */
	public Long parentId;

	public Internet parent;
	
	public Operator operator;
	
	public Long companyNo;

	public List<CardRegion> cardRegionList; 
	
	//关联的strategy, 可以为null
    public  Strategy strategy;

	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Long getInternetNo() {
		return internetNo;
	}

	public void setInternetNo(Long internetNo) {
		this.internetNo = internetNo;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getInternetId() {
		return internetId;
	}

	public void setInternetId(String internetId) {
		this.internetId = internetId;
	}

	public String getInternetName() {
		return internetName;
	}

	public void setInternetName(String internetName) {
		this.internetName = internetName;
	}

	public String getInternetDescribe() {
		return internetDescribe;
	}

	public void setInternetDescribe(String internetDescribe) {
		this.internetDescribe = internetDescribe;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	} 
	
	public Internet getParent() {
		return parent;
	}

	public void setParent(Internet parent) {
		this.parent = parent;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public List<CardRegion> getCardRegionList() {
		return cardRegionList;
	}

	public void setCardRegionList(List<CardRegion> cardRegionList) {
		this.cardRegionList = cardRegionList;
	} 
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Internet other = (Internet) obj;
		if (internetId == null) {
			if (other.internetId != null)
				return false;
		} else if (!internetId.equals(other.internetId))
			return false;
		if (internetName == null) {
			if (other.internetName != null)
				return false;
		} else if (!internetName.equals(other.internetName))
			return false;
		if (internetNo == null) {
			if (other.internetNo != null)
				return false;
		} else if (!internetNo.equals(other.internetNo))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		return true;
	} 
	
	@Override
	public void setDefaultNull() {
	} 
	 
	public String toString() {
		return JSON.toJSONString(this);
	}

	public Long getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(Long companyNo) {
		this.companyNo = companyNo;
	}
}
