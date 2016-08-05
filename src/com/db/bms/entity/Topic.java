
package com.db.bms.entity;




public class Topic extends Condition5 implements AuditStatus{

	public Long id;
	
	public String topicId;

	public String topicName;

	public String topicCover;

	public String checkCode;

	public final static int TYPE_GEN_PAGE = 1;
	public final static int TYPE_NOT_GEN = 2;
	public Integer type;

	// @see AuditStatus
	public Integer status;

	public String topicDesc;

	// 0 / 1:
	public Integer captureFlag;

	public Long operatorNo;

	public Long companyNo;

	public Long groupId;

	public Long templateId;

	public String createTime;

	public String updateTime;

	public Operator operator;

	public Company company;

	public Template template;
	
	//关联的strategy, 可以为null
	public  Strategy strategy;
	//由admin分配给某个操作员时所指定的命令ID集合，如果不是分配的，则该集合为空
	private String cmds;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTopicCover() {
		return topicCover;
	}

	public void setTopicCover(String topicCover) {
		this.topicCover = topicCover;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTopicDesc() {
		return topicDesc;
	}

	public void setTopicDesc(String topicDesc) {
		this.topicDesc = topicDesc;
	}

	public Integer getCaptureFlag() {
		return captureFlag;
	}

	public void setCaptureFlag(Integer captureFlag) {
		this.captureFlag = captureFlag;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	/*
	 @deprecate
	public static enum TopicStatus {
		DRAFT(0), AUDITING(1), AUDIT_PASS(2), AUDIT_NO_PASS(3), PUBLISH(4), UNPUBLISH(5);

		private final int index;

		private TopicStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static TopicStatus getStatus(int index) {
			switch (index) {
			case 0:
				return DRAFT;
			case 1:
				return AUDITING;
			case 2:
				return AUDIT_PASS;
			case 3:
				return AUDIT_NO_PASS;
			case 4:
				return PUBLISH;
			case 5:
				return UNPUBLISH;
			}
			return DRAFT;
		}
	}
	*/

	public static enum CaptureFlag {
		NO(0), YES(1);

		private final int index;

		private CaptureFlag(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static CaptureFlag getFlag(int index) {
			switch (index) {
			case 0:
				return NO;
			case 1:
				return YES;
			}
			return NO;
		}
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy v) {
		this.strategy= v;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	@Override
	public void setDefaultNull() {
	}

	public String getCmds() {
		return cmds;
	}

	public void setCmds(String cmds) {
		this.cmds = cmds;
	}

	public Long getCreatedBy(){
		if(this.operator.getType().intValue() == 2){
			return this.operator.createBy;
		}
		else{
			return this.operatorNo;
		}
	}
}
