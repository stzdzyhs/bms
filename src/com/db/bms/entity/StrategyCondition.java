package com.db.bms.entity;

/**
 * table strategyResource table
 */
public class StrategyCondition extends BaseModel {

	public Long id;

	public Long strategyNo;

	public static final int TYPE_CARD_REGION=1;
	public static final int TYPE_COMPANY=2;
	//public static final int TYPE_INTERNET=2; // internet is changed to company.
	public static final int TYPE_SPACE=3;
	public static final int TYPE_FEATURE_CODE=4;
	public static final int TYPE_CLIENT=5;
	
	public Integer conditionType;
	public Long conditionId;
	
	public CardRegion cardRegion;
	//public Internet internet;
	public Company company;
	public Space space;
	public FeatureCode featureCode;
	public Client client;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStrategyNo() {
		return strategyNo;
	}
	public void setStrategyNo(Long strategyNo) {
		this.strategyNo = strategyNo;
	}
	public Integer getConditionType() {
		return conditionType;
	}
	public void setConditionType(Integer v) {
		this.conditionType = v;
	}
	public Long getConditionId() {
		return conditionId;
	}
	public void setConditionId(Long v) {
		this.conditionId = v;
	}
	@Override
	public void setDefaultNull() {
	}
	
	public CardRegion getCardRegion() {
		return cardRegion;
	}
	public void setCardRegion(CardRegion cardRegion) {
		this.cardRegion = cardRegion;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Space getSpace() {
		return space;
	}
	public void setSpace(Space space) {
		this.space = space;
	}
	public FeatureCode getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(FeatureCode featureCode) {
		this.featureCode = featureCode;
	}

	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}

}
