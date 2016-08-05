package com.db.bms.sync.portal.protocal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.db.bms.entity.CardRegion;

/**
 * 发布策略对象，用于把专题或版块发布所关联的策略信息传递给分发系统
 *
 */
public class PublishStrategy implements Serializable {

	public String strategyName;
	//区域码集合
	public List<CardRegion> regionCode;
	//网络ID集合
	public List<Long> networkId;
	//空分组号集合
	public List<String> tsId;
	//智能卡号集合
	public List<String> client;
	//条件关系，and/or
	public String andOr;
	//特征码集合
	public List<String> featureId;
	
	
	
	public PublishStrategy() {
		super();
		this.regionCode = new ArrayList<CardRegion>();
		this.networkId = new ArrayList<Long>();
		this.tsId = new ArrayList<String>();
		this.client = new ArrayList<String>();
		this.featureId = new ArrayList<String>();
	}
	
	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	/**
	 * @return the regionCode
	 */
	public List<CardRegion> getRegionCode() {
		return regionCode;
	}
	/**
	 * @param regionCode the regionCode to set
	 */
	public void setRegionCode(List<CardRegion> regionCode) {
		this.regionCode = regionCode;
	}
	/**
	 * @return the networkId
	 */
	public List<Long> getNetworkId() {
		return networkId;
	}
	/**
	 * @param networkId the networkId to set
	 */
	public void setNetworkId(List<Long> networkId) {
		this.networkId = networkId;
	}
	/**
	 * @return the tsId
	 */
	public List<String> getTsId() {
		return tsId;
	}
	/**
	 * @param tsId the tsId to set
	 */
	public void setTsId(List<String> tsId) {
		this.tsId = tsId;
	}
	/**
	 * @return the client
	 */
	public List<String> getClient() {
		return client;
	}
	/**
	 * @param client the client to set
	 */
	public void setClient(List<String> client) {
		this.client = client;
	}
	/**
	 * @return the andOr
	 */
	public String getAndOr() {
		return andOr;
	}
	/**
	 * @param andOr the andOr to set
	 */
	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}
	/**
	 * @return the featureId
	 */
	public List<String> getFeatureId() {
		return featureId;
	}
	/**
	 * @param featureId the featureId to set
	 */
	public void setFeatureId(List<String> featureId) {
		this.featureId = featureId;
	}
	
	public String getNetworkIdStr(){
		StringBuffer buff = new StringBuffer();
		if(this.networkId != null && !this.networkId.isEmpty()){
			for (Long netId : this.networkId) {
				if(buff.length() <= 0){
					buff.append(netId);
				}
				else{
					buff.append(",").append(netId);
				}
			}
		}
		return buff.toString();
	}
	
	public String getRegionCodeStr(){
		StringBuffer buff = new StringBuffer();
		if(this.regionCode != null && !this.regionCode.isEmpty()){
			for (CardRegion regionCode : this.regionCode) {
				if(buff.length() <= 0){
					if(regionCode.getCodeType().intValue() == 0){
						buff.append(regionCode.getRegionCode());
					}
					else{
						buff.append(regionCode.getRegionSectionBegin()).append("-").append(regionCode.getRegionSectionEnd());
					}
				}
				else{
					if(regionCode.getCodeType().intValue() == 0){
						buff.append(",").append(regionCode.getRegionCode());
					}
					else{
						buff.append(",").append(regionCode.getRegionSectionBegin()).append("-").append(regionCode.getRegionSectionEnd());
					}
				}
			}
		}
		return buff.toString();
	}
	
	public String getCardNoStr(){
		StringBuffer buff = new StringBuffer();
		if(this.client != null && !this.client.isEmpty()){
			for (String client : this.client) {
				if(buff.length() <= 0){
					buff.append(client);
				}
				else{
					buff.append(",").append(client);
				}
			}
		}
		return buff.toString();
	}
	
	public String getFeatureIdStr(){
		StringBuffer buff = new StringBuffer();
		if(this.featureId != null && !this.featureId.isEmpty()){
			for (String featureId : this.featureId) {
				if(buff.length() <= 0){
					buff.append(featureId);
				}
				else{
					buff.append(",").append(featureId);
				}
			}
		}
		return buff.toString();
	}
	
	public String getTsIdStr(){
		StringBuffer buff = new StringBuffer();
		if(this.tsId != null && !this.tsId.isEmpty()){
			for (String tsId : this.tsId) {
				if(buff.length() <= 0){
					buff.append(tsId);
				}
				else{
					buff.append(",").append(tsId);
				}
			}
		}
		return buff.toString();
	}
	
	public String getConditionStr(){
		if("and".equalsIgnoreCase(this.andOr)){
			return "与";
		}
		else{
			return "或";
		}
	}
}
