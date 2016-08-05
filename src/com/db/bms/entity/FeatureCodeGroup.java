package com.db.bms.entity;

public class FeatureCodeGroup extends BaseModel {

	public Long groupNo; 
	public String groupId;
	public String groupName;
	public Long operatorNo;
	public String createTime;
	public String updateTime;
	
	//public Long groupId; //权限管理
	// groupId is renamed to gid
	public Long gid; //权限管理
	
	public Operator operator;
	
	// should be List<FeatureCode> !!!;
	//public FeatureCode featureCode;

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}
	
	/*
	public FeatureCode getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(FeatureCode featureCode) {
		this.featureCode = featureCode;
	}
	*/

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	//getter setter
	public Long getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public void setDefaultNull() {
	}

}
