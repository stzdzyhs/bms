package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Strategy;

public interface StrategyMapper {

	Long getPrimaryKey() throws Exception;

	/** 
	 * 查询策略
	 */
	List<Strategy> findStrategys(@Param("strategy")Strategy strategy) throws Exception;
	Integer findStrategysCount(@Param("strategy")Strategy strategy) throws Exception;
	
	/**
	 * 通过id查询策略, 并获取策略关联的resource
	 * @param strategy
	 * @return
	 * @throws Exception
	 */
	Strategy findStrategyByNoWithCondition(@Param("strategyNo")Long strategyNo) throws Exception;
	
	/**
	 * 添加策略
	 */
	Integer addStrategy(@Param("strategy")Strategy strategy) throws Exception;
    
	/** 
	 * 修改策略
	 */
	Integer updateStrategy(@Param("strategy")Strategy strategy) throws Exception;
	
	/**
	 * 删除策略
	 */
	void deleteStrategysByNo(@Param("strategyNo")Long[] nos) throws Exception;

	/**
	 * delete strategy all condition 
	 * @param strategyNo
	 * @throws Exception
	 */
	void deleteStrategyAllCondition(@Param("strategyNo")Long strategyNo) throws Exception;
	
	/**
	 * 根据ID查找策略
	 */
	List<Strategy> findStrategyByNos(@Param("strategyNos")List<Long> nos) throws Exception;
	
	/**
	 * 根据单个ID查询
	 */
	Strategy findStrategyByNo(@Param("strategyNo")Long Id) throws Exception;
	
	void updateStrategyStatus(@Param(value = "status")Integer status, @Param(value = "strategyNos")Long[] strategyNos)throws Exception;
}
