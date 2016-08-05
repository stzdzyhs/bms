
package com.db.bms.sync.portal.protocal;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;


public class TopicInfo {

	public Long topicNo; // 主键
	
	public String topicId;

	public String topicTitle;

	public String topicDesc;

	public Integer topicType;

	public String topicCover;

	public String fileMd5;
	//是否是视频截取专题，1为视频截取专题，否则不是
	public Integer captureFlag;
	//模板no
	public String templateNo;
	
	public String updateTime;

	public List<PublishStrategy> strategyArray;	
	
	public TopicInfo() {
		super();
		this.strategyArray = new ArrayList<PublishStrategy>();
	}

	public String toString() {
		String s = JSON.toJSONString(this);
		return s;
	}
	
	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public String getTopicDesc() {
		return topicDesc;
	}

	public void setTopicDesc(String topicDesc) {
		this.topicDesc = topicDesc;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public String getTopicCover() {
		return topicCover;
	}

	public void setTopicCover(String topicCover) {
		this.topicCover = topicCover;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public Integer getCaptureFlag() {
		return captureFlag;
	}

	public void setCaptureFlag(Integer captureFlag) {
		this.captureFlag = captureFlag;
	}

	public String getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(String templateId) {
		this.templateNo = templateId;
	}

	/**
	 * @return the strategyArray
	 */
	public List<PublishStrategy> getStrategyArray() {
		return strategyArray;
	}

	/**
	 * @param strategyArray the strategyArray to set
	 */
	public void setStrategyArray(List<PublishStrategy> strategyArray) {
		this.strategyArray = strategyArray;
	}

	public Long getTopicNo() {
		return topicNo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public void setTopicNo(Long no) {
		this.topicNo = no;
	}

}
