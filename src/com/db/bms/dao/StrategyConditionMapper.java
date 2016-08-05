package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.StrategyCondition;

/**
 * NOTE: table/param alias: sr   
 */
public interface StrategyConditionMapper {

	/**
	 * find strategy condition entity.
	 * sr.strategyId, sr.type must set
	 * @param sr
	 * @return
	 * @throws Exception
	 */
	public Integer findStrategyConditionEntityCount(@Param("sc")StrategyCondition sr) throws Exception;

	public List<StrategyCondition> findStrategyConditionEntity(@Param("sc")StrategyCondition sr) throws Exception;
	
	
	public void addStrategyCondition(@Param("sc")StrategyCondition sr) throws Exception;

	/**
	 * delete strategy condition by id (primary key).
	 * @param no
	 * @throws Exception
	 */
	public void delStrategyConditionById(@Param("id")Long id) throws Exception;
	/**
	 * 根据策略号删除策略关联的条件，删除策略时调用该方法
	 * @time April.26,2016
	 * @param strategyNos：策略号集合
	 * @throws Exception
	 */
	public void deleteStrategyConditionByStrategyNo(@Param("strategyNos")Long[] strategyNos)throws Exception;
	
	public Integer findStrategyConditionCountByTypeNos(@Param("type")Long type,@Param("typeNos")Long[] typeNos)throws Exception;
	
}
