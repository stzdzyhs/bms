package com.db.bms.entity;


@Deprecated
public class OperatorResource extends BaseModel {
	public Integer         no;
	public String          operatorId; // not operatorNo
	public Integer         resourceId; 
	public String          type; 
	
	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void setDefaultNull() {
	}

}
