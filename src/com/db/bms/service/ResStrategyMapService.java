package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.ResStrategyMap;

public interface ResStrategyMapService {

    void addResStrategyMap(ResStrategyMap resStrategyMap) throws Exception;
	
	List<ResStrategyMap> findResStrategyMap(ResStrategyMap resStrategyMap) throws Exception;
	
}
