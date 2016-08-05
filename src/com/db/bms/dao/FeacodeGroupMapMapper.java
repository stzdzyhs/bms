package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.FeacodeGroupMap;

public interface FeacodeGroupMapMapper {
	
	Integer getFeatureCodeGroupsCount(@Param("feacodeGroupMap") FeacodeGroupMap FeacodeGroupMap);

	List<FeacodeGroupMap> getFeacodeGroupMaps(@Param("feacodeGroupMap") FeacodeGroupMap FeacodeGroupMap);
	
	Integer getFeacodeOutGroupsCount(@Param("feacodeGroupMap") FeacodeGroupMap FeacodeGroupMap);

	List<FeacodeGroupMap> getFeacodeOutGroups(@Param("feacodeGroupMap") FeacodeGroupMap FeacodeGroupMap);
	
	Long getPrimaryKey() throws Exception;
	
	Integer addFeacodeGroupMap(@Param("feacodeGroupMap")FeacodeGroupMap fea);
	
	void delete(@Param("groupNo")Long groupNo, @Param("featureCodeNos")Long[] featureCodeNos);
	
	void deleteGroupNos(@Param("groupNos") Long[] groupNos);
	
	void deleteFeaNos(@Param("feaNos") Long[] feaNos);
	
}
