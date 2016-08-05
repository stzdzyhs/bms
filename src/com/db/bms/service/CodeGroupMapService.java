package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.FeacodeGroupMap;
import com.db.bms.entity.FeatureCode;

public interface CodeGroupMapService {

	List<FeacodeGroupMap> findFeacodeGroupMap(FeatureCode featureCode) throws Exception;
	
}
