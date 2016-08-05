package com.db.bms.dao;

import java.util.List;

import com.db.bms.entity.ResStrategyMap;

public interface ResStrategyMapMapper {

	/**
	 * 添加资源策略
	 */
	void addResStrategyMap(ResStrategyMap resStrategyMap) throws Exception;
	
	/**
	 * 查询资源策略
	 */
	List<ResStrategyMap> findResStrategyMap(ResStrategyMap resStrategyMap) throws Exception;
	
}
