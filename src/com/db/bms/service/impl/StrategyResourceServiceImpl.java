package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.db.bms.dao.StrategyResourceMapper;
import com.db.bms.entity.StrategyCondition;
import com.db.bms.service.StrategyResourceService;

public class StrategyResourceServiceImpl implements StrategyResourceService {

	@Autowired
	StrategyResourceMapper strategyResourceMapper; 
	
	/**
	 * find strategy one kind of resource.
	 * search.type and search.strategyId must set !!!
	 * @param search
	 * @return
	 * @throws Exception
	 */
    public List<StrategyCondition> findStrategyResourceEntity(StrategyCondition search) throws Exception {
         int cnt = strategyResourceMapper.findStrategyResourceEntityCount(search);
         search.getPageUtil().setRowCount(cnt);
         List<StrategyCondition> list = strategyResourceMapper.findStrategyResourceEntity(search);  
         return list;
     }
    
}
