package com.db.bms.entity;

import com.alibaba.fastjson.JSON;

public class FeatureCode  extends BaseModel{
	
	private Long featureCodeNo;
	private String featureCodeVal;
	private String featureCodeType;
	private String featureCodeDesc;
	private Long operatorNo;
	private String createTime;
	private String updateTime;
	
	public static final int STATUS_DISABLED = 0;
	public static final int STATUS_ENABLED = 1;
	private Integer status;
	
	private Long groupId; //用于权限查询
	
	private Long groupNo;
		
	private FeatureCodeGroup featureCodeGroup;

	private Operator operator;
	
	private FeacodeGroupMap feacodeGroupMap;
	
	//关联的strategy, 可以为null
    public  Strategy strategy;
	
    
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public FeacodeGroupMap getFeacodeGroupMap() {
		return feacodeGroupMap;
	}

	public void setFeacodeGroupMap(FeacodeGroupMap FeacodeGroupMap) {
		this.feacodeGroupMap = FeacodeGroupMap;
	}
	public FeatureCodeGroup getFeatureCodeGroup() {
		return featureCodeGroup;
	}
	
	public void setFeatureCodeGroup(FeatureCodeGroup featureCodeGroup) {
		this.featureCodeGroup = featureCodeGroup;
	}

	@Override
	public void setDefaultNull() {
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	//----Getter and Setter----
	public Long getFeatureCodeNo() {
		return featureCodeNo;
	}

	public void setFeatureCodeNo(Long featureCodeNo) {
		this.featureCodeNo = featureCodeNo;
	}

	public String getFeatureCodeVal() {
		return featureCodeVal;
	}

	public void setFeatureCodeVal(String featureCodeVal) {
		this.featureCodeVal = featureCodeVal;
	}

	public String getFeatureCodeType() {
		return featureCodeType;
	}

	public void setFeatureCodeType(String featureCodeType) {
		this.featureCodeType = featureCodeType;
	}

	public String getFeatureCodeDesc() {
		return featureCodeDesc;
	}

	public void setFeatureCodeDesc(String featureCodeDesc) {
		this.featureCodeDesc = featureCodeDesc;
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

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}



	public static enum FeatureCodeStatus {
		ENABLE(1), DISABLE(0);

		private final int index;

		private FeatureCodeStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static FeatureCodeStatus getStatus(int index) {
			switch (index) {
			case 0:
				return DISABLE;
			case 1:
				return ENABLE;
			}
			return DISABLE;
		}
	}

}
