package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.StrategyCondition;

public interface StrategyConditionService {

	/**
	 * find strategy one kind of condtion.
	 * search.type and search.strategyId must set !!!
	 * @param search
	 * @return
	 * @throws Exception
	 */
    public List<StrategyCondition> findStrategyConditionEntity(StrategyCondition search) throws Exception;   

	/**
	 * delete strategy condition by id (primary key).
	 * @param no
	 * @throws Exception
	 */
	public void delStrategyConditionById(Long id) throws Exception;
	
	public boolean isRefStrategyCondition(Long type,Long[] typeNos) throws Exception;
}
