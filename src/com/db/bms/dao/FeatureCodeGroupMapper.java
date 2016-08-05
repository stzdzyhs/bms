package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.FeatureCodeGroup;

public interface FeatureCodeGroupMapper {

	Long getPrimaryKey() throws Exception;
	
	List<FeatureCodeGroup> getFeatureCodeGroups(@Param("fcg")FeatureCodeGroup featureCodeGroup) throws Exception; 
	
	Integer getFeatureCodeGroupsCount(@Param("fcg")FeatureCodeGroup featureCodeGroup) throws Exception;

	FeatureCodeGroup getFeaGroupByNo(Long groupNo);
	
	FeatureCodeGroup getFeaGroupDetail(Long groupNo);
	
	Integer checkGroupName(@Param("fcg")FeatureCodeGroup featureCodeGroup);
	
	Integer checkGroupId(@Param("fcg")FeatureCodeGroup featureCodeGroup);
	
	void updateGroup(@Param("fcg")FeatureCodeGroup featureCodeGroup);
	
	void addGroup(@Param("fcg")FeatureCodeGroup featureCodeGroup);
	
	List<FeatureCodeGroup> getGroups (Long[] groupNos);
	
	void deleteGroups(Long[] groupNos);
}
