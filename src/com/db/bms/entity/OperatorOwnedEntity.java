package com.db.bms.entity;

import com.db.bms.utils.ConstConfig;

/**
 * the base class of those entities owned by operator
 */
public abstract class OperatorOwnedEntity extends BaseModel {
	public Long operatorNo;
	public Operator operator;
	
	public Long companyNo;
	public Company company;
	
	// @see AuditStatus
	public Integer status;

	/**
	 * get status name
	 * @return
	 */
	public String getStatusName() {
		String s = getStatusName(this.status);
		return s;
	}
	
	public static String getStatusName(Integer status) {
		String s = ConstConfig.pictureStatusMap.get(status);
		if(s ==null) {
			return "未知状态";
		}
		return s;
	}


	public Long getOperatorNo()	{
		return operatorNo;
	}
	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}	
	
	public Long getCompanyNo() {
		return companyNo;
	}
	
	public void setCompanyNo(Long companyNo) {
		// for -1 company, treat as null company ! otherwise, foreign key constraint will error !
		if(companyNo!=null && companyNo==-1) {
			this.companyNo= null;
		}
		else {
			this.companyNo = companyNo;
		}
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public boolean isPublished() {
		if(status!=null && status==ConstConfig.STATUS_PUBLISH) {
			return true;
		}
		return false;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public boolean equals(Object o) {
		if(o==this) {
			return true;
		}
		OperatorOwnedEntity ooe = (OperatorOwnedEntity)o;
		
		if(this.operatorNo!=null) {
			if(!this.operatorNo.equals(ooe.operatorNo)) {
				return false;
			}
		}
		else {
			if(ooe.operatorNo!=null) {
				return false;
			}
		}
		
		if(this.companyNo!=null) {
			if(!this.companyNo.equals(ooe.companyNo)) {
				return false;
			}
		}
		else {
			if(ooe.companyNo!=null) {
				return false;
			}
		}
		
		return true;		
	}

}
