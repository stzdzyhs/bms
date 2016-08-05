package com.db.bms.entity;

public class FeacodeGroupMap extends BaseModel{

	private Long feacodeGroupMapNo;
	private Long featureCodeNo;
	private Long groupNo;
	
	private FeatureCode featureCode;
	private String featureCodeVal;
	
	public String getFeatureCodeVal() {
		return featureCodeVal;
	}

	public void setFeatureCodeVal(String featureCodeVal) {
		this.featureCodeVal = featureCodeVal;
	}

	public FeatureCode getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(FeatureCode featureCode) {
		this.featureCode = featureCode;
	}

	public Long getFeacodeGroupMapNo() {
		return feacodeGroupMapNo;
	}

	public void setFeacodeGroupMapNo(Long FeacodeGroupMapNo) {
		this.feacodeGroupMapNo = FeacodeGroupMapNo;
	}

	public Long getFeatureCodeNo() {
		return featureCodeNo;
	}

	public void setFeatureCodeNo(Long featureCodeNo) {
		this.featureCodeNo = featureCodeNo;
	}

	public Long getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}

	@Override
	public void setDefaultNull() {
	}

}
