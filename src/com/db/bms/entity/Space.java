package com.db.bms.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;


public class Space extends BaseModel implements Serializable{
	/* 
	 * 主键ID
	 */
	public Long spaceNo;
	/* 
	 * 空分组ID
	 */
	public String spaceId;
	/* 
	 * 空分组名称
	 */
	public String spaceName;
	/* 
	 * 空分组描述
	 */
	public String spaceDescribe;
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
	
	public Long companyNo;

	public Space parent;
	
	public Operator operator;

	public List<CardRegion> cardRegionList;

	//关联的strategy, 可以为null
    public  Strategy strategy;
	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Long getSpaceNo() {
		return spaceNo;
	}

	public void setSpaceNo(Long spaceNo) {
		this.spaceNo = spaceNo;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getSpaceDescribe() {
		return spaceDescribe;
	}

	public void setSpaceDescribe(String spaceDescribe) {
		this.spaceDescribe = spaceDescribe;
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

	public Space getParent() {
		return parent;
	}

	public void setParent(Space parent) {
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
		Space other = (Space) obj;
		if (spaceId == null) {
			if (other.spaceId != null)
				return false;
		} else if (!spaceId.equals(other.spaceId))
			return false;
		if (spaceName == null) {
			if (other.spaceName != null)
				return false;
		} else if (!spaceName.equals(other.spaceName))
			return false;
		if (spaceNo == null) {
			if (other.spaceNo != null)
				return false;
		} else if (!spaceNo.equals(other.spaceNo))
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
