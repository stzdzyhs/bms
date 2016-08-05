package com.db.bms.entity;


public class Log extends BaseModel {

	private Long logNo;

	private Long operatorNo;

	private Operator operator;

	private Long logTypeNo;

	private LogType logType;

	private String logDescribe;

	private String logTime;

	private Long companyNo;

	private Company company; 
	
	// 辅助查询条件
	private String startDate;

	private String endDate;

	public Long getLogNo() {

		return this.logNo;

	}

	public void setLogNo(Long logNo) {

		this.logNo = logNo;

	}

	public Long getOperatorNo() {

		return this.operatorNo;

	}

	public void setOperatorNo(Long operatorNo) {

		this.operatorNo = operatorNo;

	}

	public Long getLogTypeNo() {

		return this.logTypeNo;

	}

	public void setLogTypeNo(Long logTypeNo) {

		this.logTypeNo = logTypeNo;

	}

	public String getLogDescribe() {

		return this.logDescribe;

	}

	public void setLogDescribe(String logDescribe) {

		this.logDescribe = logDescribe;

	}

	public String getLogTime() {

		return this.logTime;

	}

	public void setLogTime(String logTime) {

		this.logTime = logTime;

	}

	public Long getCompanyNo() {

		return this.companyNo;

	}

	public void setCompanyNo(Long companyNo) {

		this.companyNo = companyNo;

	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	public String getStartDate() {
		return startDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
     
	@Override
	public void setDefaultNull() {
		// TODO Auto-generated method stub

	}

}