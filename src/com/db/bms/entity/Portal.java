package com.db.bms.entity;

public class Portal extends BaseModel {

	private Long sysNo;

	private String sysId;

	private String sysName;

	private String sysUrl;

	private Integer status;

	private Long operatorNo;

	private Long companyNo;

	private String createTime;

	private String updateTime;

	private Operator operator;

	private Company company;

	public Long getSysNo() {
		return sysNo;
	}

	public void setSysNo(Long sysNo) {
		this.sysNo = sysNo;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysUrl() {
		return sysUrl;
	}

	public void setSysUrl(String sysUrl) {
		this.sysUrl = sysUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}

	public Long getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(Long companyNo) {
		this.companyNo = companyNo;
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
	
	public static enum PortalStatus {
		DRAFT(0),  ENABLE(1), DISABLE(
				2);

		private final int index;

		private PortalStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static PortalStatus getStatus(int index) {
			switch (index) {
			case 0:
				return DRAFT;
			case 1:
				return ENABLE;
			case 2:
				return DISABLE;
			}
			return DRAFT;
		}
	}

	@Override
	public void setDefaultNull() {

	}

}
