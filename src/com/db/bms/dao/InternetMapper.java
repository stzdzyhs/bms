package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Internet;
import com.db.bms.entity.Topic;

public interface InternetMapper {
	 
	Long getPrimaryKey() throws Exception; 
	
	Internet findInternetByNo(Long internetNo) throws Exception; 
	
    List<Internet> findInternetsByNo(List<Long> internetNos) throws Exception;
	
    Internet findInternetById(String internetId) throws Exception;
	
	List<Internet> findAllInternets(@Param(value = "distInternetList")List<Long> distInternetList) throws Exception;

	Integer findInternetCount(@Param(value = "internet")Internet entity,@Param(value = "distInternetList")List<Long> distInternetList) throws Exception;

	List<Internet> findInternets(@Param(value = "internet")Internet entity,@Param(value = "distInternetList")List<Long> distInternetList) throws Exception;
	
	Integer getInternetCountByIdOrName(Internet entity) throws Exception;
	
	Integer addInternet(Internet internet) throws Exception;

	Integer updateInternet(Internet internet) throws Exception;

	List<Internet> findInternetsWithSubByNo(List<Long> internetNos)	throws Exception;
	
	Integer deleteInternet(@Param(value="internetNo") Long internetNo) throws Exception;
	
	Integer deleteInternetCardRegionMapByInternetNo(Long internetNo) throws Exception;
	
	Integer deleteInternetCardRegionMaps(@Param(value = "internetNo")Long internetNo, @Param(value = "regionIds")Long[] regionIds) throws Exception;
	
	Integer addInternetCardRegionMap(@Param(value = "internetNo")Long internetNo, @Param(value = "regionId")Long regionId) throws Exception;
	
	Integer findInternetCardRegionMapCountByRegionId(Long[] regionIds) throws Exception;
	
	Integer findInternetCountById(List<String> internetIds) throws Exception;
	
	List<Internet> findDistInternetByOperNo(Long operatorNo) throws Exception;
	
	/**
	 * 查找operator关联internet, 不包括子internet
	 */
	List<Internet> findOperatorInternets(Internet entity) throws Exception;
	// count
	Integer findOperatorInternetsCount(Internet entity) throws Exception;
	
	List<Internet> findInternetByIds(String[] internetIds) throws Exception;
	
	List<Internet> findInternetByParentId(Long parentId) throws Exception;

	
	Integer findStrategyInternetNoSelectCount(@Param(value = "strategyNo")Long strategyNo,@Param(value = "companyNo")Long companyNo,
			@Param(value = "internet")Internet region,@Param("excludeIds") Long[] excludeIds) throws Exception;
	List<Internet> findStrategyInternetNoSelect(@Param(value = "strategyNo")Long strategyNo,@Param(value = "companyNo")Long companyNo, 
			@Param(value = "internet")Internet region,@Param("excludeIds") Long[] excludeIds) throws Exception;
	
}
