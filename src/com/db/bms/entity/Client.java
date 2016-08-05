package com.db.bms.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Client extends BaseModel implements Serializable{
	
	/*
	 * 主键
	 */
	public Long clientNo;
	/*
	 * ID
	 */
	public String clientId;
	/*
	 * 名称
	 */
	public String clientName;
	/*
	 * 描述
	 */
	public String clientDescribe;
	
	/*
	 * 创建者
	 */
	public Long createBy;
	/*
	 * 创建时间
	 */
	public String createTime;
	public String updateTime;
	
	/*
	 * 父级Id
	 */
	public Long parentId;
	
	public String path;
	
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

	public Long getClientNo() {
		return clientNo;
	}

	public void setClientNo(Long clientNo) {
		this.clientNo = clientNo;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientDescribe() {
		return clientDescribe;
	}

	public void setClientDescribe(String clientDescribe) {
		this.clientDescribe = clientDescribe;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (clientNo == null) {
			if (other.clientNo != null)
				return false;
		} else if (!clientNo.equals(other.clientNo))
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
