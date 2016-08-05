package com.db.bms.entity;

public class ResStrategyMap extends BaseModel {
	
	public Long id;
	
	// type constants
	public static final int TYPE_TOPIC=1;
	public static final int TYPE_ALBUM=2;
	public static final int TYPE_MENU=3;
	public static final int TYPE_IMAGE=4;
	public static final int TYPE_COLUMN=5;
	public static final int TYPE_ARTICLE=6;
	
	public Long type;
	
	public Long resId;
	public Long strategyNo;
	public Long operatorId;
	public String createTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public Long getResId() {
		return resId;
	}
	public void setResId(Long resId) {
		this.resId = resId;
	}

	public Long getStrategyNo() {
		return strategyNo;
	}
	public void setStrategyNo(Long strategyId) {
		this.strategyNo = strategyId;
	}

	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public void setDefaultNull() {
	}

}
