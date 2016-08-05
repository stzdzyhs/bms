package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.ResStrategyMapMapper;
import com.db.bms.entity.ResStrategyMap;
import com.db.bms.service.ResStrategyMapService;

@Service("resStrategyMapService")
public class ResStrategyMapServiceImpl implements ResStrategyMapService{
	
	@Autowired
	private ResStrategyMapMapper resStrategyMapMapper;
	
	@Override
	public void addResStrategyMap(ResStrategyMap resStrategyMap) throws Exception {
		resStrategyMapMapper.addResStrategyMap(resStrategyMap);	
	}

	@Override
	public List<ResStrategyMap> findResStrategyMap(ResStrategyMap resStrategyMap) throws Exception {
		return resStrategyMapMapper.findResStrategyMap(resStrategyMap);
	}

}
