package com.db.bms.service;

import java.util.List;



import com.db.bms.entity.FeacodeGroupMap;

public interface FeacodeGroupMapService {
	
	List<FeacodeGroupMap> findFeacodeGroupMaps(FeacodeGroupMap FeacodeGroupMap) throws Exception;
	
	List<FeacodeGroupMap> findFeacodesOutGroup(FeacodeGroupMap FeacodeGroupMap) throws Exception;
	
	void add(Long groupNo,Long[] featureCodeNos) throws Exception;
	
	void deleteInGroupMap(Long groupNo,Long[] featureCodeNos) throws Exception;
	
	void deleteGroupNos(Long[] groupNos) throws Exception;
	
	void featureCodeNos(Long[] feaNos) throws Exception;
	
	List<FeacodeGroupMap> findAllFeacodeGroupMaps(
			FeacodeGroupMap FeacodeGroupMap) throws Exception;
}
