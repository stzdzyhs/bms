package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.StrategyCondition;

/**
 * NOTE: table/param alias: sr   
 */
public interface StrategyResourceMapper {

	/**
	 * find strategy resoruce entity.
	 * sr.strategyId, sr.type must set
	 * @param sr
	 * @return
	 * @throws Exception
	 */
	public Integer findStrategyResourceEntityCount(@Param("sr")StrategyCondition sr) throws Exception;

	public List<StrategyCondition> findStrategyResourceEntity(@Param("sr")StrategyCondition sr) throws Exception;
}
