package com.db.bms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Client;
import com.db.bms.entity.Company;
import com.db.bms.entity.FeatureCode;
import com.db.bms.entity.Space;
import com.db.bms.entity.Strategy;
import com.db.bms.sync.portal.protocal.PublishStrategy;

public interface StrategyService {
     
	/** 
	 * 查询策略
	 */
	List<Strategy> findStrategys(Strategy strategy) throws Exception;

	/**
	 * strategyNo and companyNo,  one must not null
	 * @param strategyNo
	 * @param companyNo
	 * @param program
	 * @param excludeIds
	 * @return
	 * @throws Exception
	 */
	List<CardRegion> findStrategyCardRegionNoSelect(Long strategyNo, Long companyNo, CardRegion program, Long[] excludeIds) throws Exception;
	List<Company> findStrategyCompanyNoSelect(Long strategyNo, Long companyNo, Company company, Long[] excludeIds) throws Exception;
	List<Space> findStrategySpaceNoSelect(Long strategyNo, Long companyNo, Space space, Long[] excludeIds) throws Exception;
	List<FeatureCode> findStrategyFeatureCodeNoSelect(Long strategyNo, Long companyNo, FeatureCode featureCode, Long[] excludeIds) throws Exception;
	List<Client> findStrategyClientNoSelect(Long strategyNo, Long companyNo, Client client, Long[] excludeIds) throws Exception;
	
	/**
	 * 更新策略
	 */
	void updateStrategy(Strategy strategy) throws Exception;
	
	/**
	 * 保存策略
	 * strategy.curOper must set.
	 */
	void saveStrategy(Strategy strategy) throws Exception;
	
	/**
	 * 删除策略
	 */
	void deleteStrategysByNo(Long[] strategyNo) throws Exception;
	
	/**
	 * delete strategy all condition 
	 * @param strategyNo
	 * @throws Exception
	 */
	void deleteStrategyAllCondition(Long strategyNo) throws Exception;
	
	/**
	 * 根据ID数组查找策略
	 */
	List<Strategy> findStrategyByNos(List<Long> strategyNo) throws Exception;
	
	/**
	 * 根据单个ID查询
	 */
	Strategy findStrategyByNo(Long no) throws Exception;

	// must set strategy.strategyId
	Strategy findStrategyByNoWithCondition(Long strategyNo) throws Exception;

	// must set strategy.strategyId
	Strategy getStrategyAllData(Long strategyNo) throws Exception;
	
	
	//Result2<Strategy> findStrategys(Strategy strategy) throws Exception;
	
	/**
	 * 根据策略编号，返回发布策略集合，也就是根据策略编号，获取具体的策略内容集合
	 * @param strategyNoStr
	 * @return
	 */
	List<PublishStrategy> getPublishStrategyByNos(String strategyNoStr);

	void auditStrategy(@Param(value = "status")Integer status, @Param(value = "strategyNos")Long[] strategyNos)throws Exception;
}
