package com.db.bms.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;


public class Strategy extends BaseModel implements Serializable, AuditStatus {

	/*
	 * @Deprecated, 
	 * @see AuditStatus
	public static final int STATUS_DRAFT=0;
    public static final int STATUS_SUBMIT=1; //"正在审核");
    public static final int STATUS_PASS=2; // "审核通过");
    public static final int STATUS_FAIL=3; //, "审核不通过");
    public static final int STATUS_ENABLE=4; //  "启用");
    public static final int STATUS_DISABLE=5; // "禁用");
    */
	
	//...............................................................................................
	
	// PK
    public Long strategyNo;
	/* 
	 * 策略id
	 */
	public String strategyId;  
	/* 
	 * 策略名称
	 */
	public String strategyName;   

	/*
	 * 审核状态, 
	 * @see AuditStatus
	 */
	public Integer auditStatus; 

	/* 
	 * 所属运营商
	 */
	public Long companyNo;
	/* 
	 * 创建用户
	 */
	public Long createBy; 
	/* 
	 * 创建时间
	 */
	public String createTime; 
	/* 
	 * 更新时间
	 */
	public String updateTime;
	
	public static final int FORM_AND=1;
	public static final int FORM_OR=2;
	/* 
	 * 策略形式
	 */
	public Integer strategyForm; 
	
	public Company company;
	
	public Long operatorNo;
	
	public Internet parent;
	
    public Operator operator;

    // 关联的resource

    public List<StrategyCondition> conditionList = null;
    
    public List<StrategyCondition> cardRegionList;
	public List<StrategyCondition> featureCodeList;
	public List<StrategyCondition> spaceList;
	public List<StrategyCondition> companyList;	
	public List<StrategyCondition> clientList;
	
	public void setData(Integer type, List<StrategyCondition> map) {
		if (type==null) {
			throw new ResultCodeException(ResultCode.INVALID_PARAM, "");
		}
		if(type ==StrategyCondition.TYPE_CARD_REGION) {
			this.cardRegionList = map;
		}
		else if(type==StrategyCondition.TYPE_FEATURE_CODE) {
			this.featureCodeList = map;
		}
		else if(type==StrategyCondition.TYPE_COMPANY) {
			this.companyList = map;
		}
		else if(type==StrategyCondition.TYPE_SPACE) {
			this.spaceList = map;
		}
		else if(type==StrategyCondition.TYPE_CLIENT) {
			this.clientList = map;
		}		
		else {
			throw new ResultCodeException(ResultCode.INVALID_PARAM, "type:" + type);
		}
	}

	private String spaceName;

	
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

    
	public List<StrategyCondition> getClientList() {
		return clientList;
	}
	public void setClientList(List<StrategyCondition> clientList) {
		this.clientList = clientList;
	}
	public String toString() {
		return JSON.toJSONString(this);
	}

	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Internet getParent() {
		return parent;
	}
	public void setParent(Internet parent) {
		this.parent = parent;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
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
	
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
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
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getStrategyForm() {
		return strategyForm;
	}
	public void setStrategyForm(Integer strategyForm) {
		this.strategyForm = strategyForm;
	}
	
	public String getStrategyFormName(){
		if(this.strategyForm == 1){
			return "与";
		}
		else{
			return "或";
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Strategy other = (Strategy) obj;
		if (strategyName == null) {
			if (other.strategyName != null)
				return false;
		} else if (!strategyName.equals(other.strategyName))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		
		return true;
	} 
	
	public static enum AuditStatus {
		DRAFT(0), AUDITING(1), AUDIT_PASS(2), AUDIT_NO_PASS(3), ENABLE(4), DISABLE(5);

		private final int index;

		private AuditStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static AuditStatus getStatus(int index) {
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
				return ENABLE;
			case 5:
				return DISABLE;
			}
			return DRAFT;
		}
	}
	
	@Override
	public void setDefaultNull() {
	}

	public List<StrategyCondition> getCardRegionList() {
		return cardRegionList;
	}

	public void setCardRegionList(List<StrategyCondition> cardRegionList) {
		this.cardRegionList = cardRegionList;
	}

	public List<StrategyCondition> getFeatureCodeList() {
		return featureCodeList;
	}

	public void setFeatureCodeList(List<StrategyCondition> featureCodeList) {
		this.featureCodeList = featureCodeList;
	}

	public List<StrategyCondition> getSpaceList() {
		return spaceList;
	}

	public void setSpaceList(List<StrategyCondition> spaceList) {
		this.spaceList = spaceList;
	}

	public List<StrategyCondition> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<StrategyCondition> v) {
		this.conditionList = v;
	}

	public Long getStrategyNo() {
		return strategyNo;
	}
	public void setStrategyNo(Long strategyNo) {
		this.strategyNo = strategyNo;
	}
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	public String getStrategyId() {
		return this.strategyId;
	}
	public List<StrategyCondition> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<StrategyCondition> companyList) {
		this.companyList = companyList;
	}
	
	public String getAuditStatusName(){
		if(this.auditStatus == 0){
			return "编辑";
		}
		else if(this.auditStatus == 1){
			return "提交审核";
		}
		else if(this.auditStatus == 2){
			return "审核通过";
		}
		else if(this.auditStatus == 3){
			return "审核不通过";
		}
		else if(this.auditStatus == 4){
			return "启用";
		}
		else{
			return "禁用";
		}
	}
}
