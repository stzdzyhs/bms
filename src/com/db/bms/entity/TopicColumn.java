
package com.db.bms.entity;


public class TopicColumn extends BaseModel {

	private Long id;

	private Integer showOrder;

	private String columnName;

	private Integer status;

	private Long topicId;

	private Long operatorNo;

	private Long companyNo;

	private Long groupId;

	private String createTime;

	private String updateTime;

	private Operator operator;

	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public static enum ColumnStatus {
		DRAFT(0), ENABLE(1), DISABLE(2), ;

		private final int index;

		private ColumnStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static ColumnStatus getStatus(int index) {
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
