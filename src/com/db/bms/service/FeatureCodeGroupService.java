package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.FeatureCodeGroup;

public interface FeatureCodeGroupService {

	List<FeatureCodeGroup> findFeatureCodeGroups(FeatureCodeGroup featureCodeGroup) throws Exception; 
	
	FeatureCodeGroup findFeaGroupByNo(Long groupNo) throws Exception;
	
	FeatureCodeGroup findFeaGroupDetail(Long groupNo) throws Exception;
	
	boolean isRepeatGroupName(FeatureCodeGroup featureCodeGroup)  throws Exception;
	
	boolean isRepeatGroupId(FeatureCodeGroup featureCodeGroup)  throws Exception;
	
	void updateGroup(FeatureCodeGroup featureCodeGroup) throws Exception;
	
	void saveGroup(FeatureCodeGroup featureCodeGroup) throws Exception;
	
	List<FeatureCodeGroup> findGroups(Long[] groupNos) throws Exception;
	
	void deleteGroups(Long[] groupNos) throws Exception;
}
