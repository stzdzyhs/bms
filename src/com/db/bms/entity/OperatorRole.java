package com.db.bms.entity;

public class OperatorRole extends BaseModel {

	private Long operatorNo;

	private Long roleNo;

	public Long getOperatorNo() {

		return this.operatorNo;

	}

	public void setOperatorNo(Long operatorNo) {

		this.operatorNo = operatorNo;

	}

	public Long getRoleNo() {

		return this.roleNo;

	}

	public void setRoleNo(Long roleNo) {

		this.roleNo = roleNo;

	}

	@Override
	public void setDefaultNull() {

	}

}