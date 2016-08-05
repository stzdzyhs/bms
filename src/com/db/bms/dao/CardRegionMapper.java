
package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Topic;


public interface CardRegionMapper {

	CardRegion findRegionById(Long regionId) throws Exception;

	List<CardRegion> findRegionsById(Long[] regionIds) throws Exception;

	CardRegion findRegionByCode(CardRegion region) throws Exception;

	Integer findRegionCount(CardRegion region) throws Exception;

	List<CardRegion> findRegions(CardRegion region) throws Exception;

	Integer getRegionCountByCode(CardRegion region) throws Exception;

	Integer addRegion(CardRegion region) throws Exception;

	Integer updateRegion(CardRegion region) throws Exception;

	Integer deleteRegions(Long[] regionIds) throws Exception;

	List<CardRegion> findAllRegions(CardRegion region) throws Exception;

	Integer getChildRegionCount(Long[] parentIds) throws Exception;

	Integer findCompanyRegionCount(@Param(value = "companyNo") Long companyNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	Integer findInternetRegionCount(@Param(value = "internetNo") Long internetNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	Integer findSpaceRegionCount(@Param(value = "spaceNo") Long spaceNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	Integer findCaNumberRegionCount(@Param(value = "caNumberNo") Long caNumberNo,
			@Param(value = "region") CardRegion region) throws Exception;
	Integer findStrategyRegionCount(@Param(value = "strategyId") Long internetNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	Integer findClientRegionCount(@Param(value = "clientNo") Long clientNo,
			@Param(value = "region") CardRegion region) throws Exception; 

	List<CardRegion> findCompanyRegions(
			@Param(value = "companyNo") Long companyNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findInternetRegions(
			@Param(value = "internetNo") Long internetNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findSpaceRegions(
			@Param(value = "spaceNo") Long spaceNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findCaNumberRegions(
			@Param(value = "caNumberNo") Long caNumberNo,
			@Param(value = "region") CardRegion region) throws Exception;
	List<CardRegion> findStrategyRegions(
			@Param(value = "strategyId") Long strategyId,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findClientRegions(
			@Param(value = "clientNo") Long clientNo,
			@Param(value = "region") CardRegion region) throws Exception; 


	Integer findCompanyRegionNoSelectCount(
			@Param(value = "companyNo") Long companyNo,
			@Param(value = "region") CardRegion region) throws Exception;  
	Integer findInternetRegionNoSelectCount(
			@Param(value = "InternetNo") Long internetNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	Integer findSpaceRegionNoSelectCount(
			@Param(value = "SpaceNo") Long spaceNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	Integer findCaNumberRegionNoSelectCount(
			@Param(value = "CaNumberNo") Long caNumberNo,
			@Param(value = "region") CardRegion region) throws Exception;
	Integer findStrategyRegionNoSelectCount(
			@Param(value = "StrategyId") Long strategyId,
			@Param(value = "region") CardRegion region) throws Exception; 
	Integer findClientRegionNoSelectCount(
			@Param(value = "ClientNo") Long clientId,
			@Param(value = "region") CardRegion region) throws Exception; 

	List<CardRegion> findCompanyRegionsNoSelect(
			@Param(value = "companyNo") Long companyNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findInternetRegionsNoSelect(
			@Param(value = "internetNo") Long internetNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findStrategyRegionsNoSelect(
			@Param(value = "strategyId") Long strategyId,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findClientRegionsNoSelect(
			@Param(value = "clientNo") Long clientId,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findSpaceRegionsNoSelect(
			@Param(value = "spaceNo") Long spaceNo,
			@Param(value = "region") CardRegion region) throws Exception; 
	List<CardRegion> findCaNumberRegionsNoSelect(
			@Param(value = "caNumberNo") Long caNumberNo,
			@Param(value = "region") CardRegion region) throws Exception;

	List<CardRegion> getCardRegionByCompanyNo(Long companyNo);  
	List<CardRegion> getCardRegionByInternetNo(Long internetNo);  
	List<CardRegion> getCardRegionBySpaceNo(Long spaceNo);  
	List<CardRegion> getCardRegionByCaNumberNo(Long caNumberNo);
	List<CardRegion> getCardRegionByStrategyId(Long strategyId);
	List<CardRegion> getCardRegionByClientNo(Long clientNo);
	


	Integer findStrategyCardRegionNoSelectCount(@Param(value = "strategyNo")Long strategyNo, @Param(value = "companyNo")Long companyNo, 
			@Param(value = "region")CardRegion region,@Param("excludeIds") Long[] excludeIds) throws Exception;
	List<CardRegion> findStrategyCardRegionNoSelect(@Param(value = "strategyNo")Long strategyNo, @Param(value = "companyNo")Long companyNo,
			@Param(value = "region")CardRegion region,@Param("excludeIds") Long[] excludeIds) throws Exception;
	
}
