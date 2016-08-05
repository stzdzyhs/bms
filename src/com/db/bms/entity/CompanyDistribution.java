package com.db.bms.entity;


public class CompanyDistribution extends BaseModel{

	private Long id;

	private Long companyNo;

	private Long operatorNo;

	private Integer hasDistribution;

	private Long distributeBy;

	private String distributeTime;

	private Operator distributeOper;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(Long companyNo) {
		this.companyNo = companyNo;
	}

	public Long getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}

	public Integer getHasDistribution() {
		return hasDistribution;
	}

	public void setHasDistribution(Integer hasDistribution) {
		this.hasDistribution = hasDistribution;
	}

	public Long getDistributeBy() {
		return distributeBy;
	}

	public void setDistributeBy(Long distributeBy) {
		this.distributeBy = distributeBy;
	}

	public String getDistributeTime() {
		return distributeTime;
	}

	public void setDistributeTime(String distributeTime) {
		this.distributeTime = distributeTime;
	}

	public Operator getDistributeOper() {
		return distributeOper;
	}

	public void setDistributeOper(Operator distributeOper) {
		this.distributeOper = distributeOper;
	}

	@Override
	public void setDefaultNull() {

	}

}
