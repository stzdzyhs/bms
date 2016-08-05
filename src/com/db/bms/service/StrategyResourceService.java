package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.StrategyCondition;

public interface StrategyResourceService {

	/**
	 * find strategy one kind of resource.
	 * search.type and search.strategyId must set !!!
	 * @param search
	 * @return
	 * @throws Exception
	 */
    public List<StrategyCondition> findStrategyResourceEntity(StrategyCondition search) throws Exception;
    
}
