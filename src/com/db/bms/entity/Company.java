package com.db.bms.entity;

import java.util.List;

import com.db.bms.entity.SystemConfig;

public class Company extends BaseModel implements java.io.Serializable {

	private Long companyNo;

	private String companyId;

	private String companyName;

	private String companyDescribe;

	private Long createBy;

	private String createTime;

	private Long parentId;

	private Company parent;
	
	private Operator operator;

	private List<CardRegion> cardRegionList;
	
	private List<SystemConfig> systemConfigs;
	
	
	public List<SystemConfig> getSystemConfigs() {
		return systemConfigs;
	}

	public void setSystemConfigs(List<SystemConfig> systemConfigs) {
		this.systemConfigs = systemConfigs;
	}

	public Long getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(Long companyNo) {
		this.companyNo = companyNo;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDescribe() {
		return companyDescribe;
	}

	public void setCompanyDescribe(String companyDescribe) {
		this.companyDescribe = companyDescribe;
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

	public Company getParent() {
		return parent;
	}

	public void setParent(Company parent) {
		this.parent = parent;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (companyNo == null) {
			if (other.companyNo != null)
				return false;
		} else if (!companyNo.equals(other.companyNo))
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
	
	

	public List<CardRegion> getCardRegionList() {
		return cardRegionList;
	}

	public void setCardRegionList(List<CardRegion> cardRegionList) {
		this.cardRegionList = cardRegionList;
	}

	@Override
	public void setDefaultNull() {

	}

	public String toString() {
		return com.alibaba.fastjson.JSON.toJSONString(this);
	}
	
	
}