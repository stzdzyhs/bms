package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Operator;
import com.db.bms.entity.Space;

public interface SpaceMapper {
  
    Long getPrimaryKey() throws Exception; 
	
    Space findSpaceByNo(Long spaceNo) throws Exception; 
	
    List<Space> findSpacesByNo(List<Long> spaceNos) throws Exception;
	
    Space findSpaceById(String spaceId) throws Exception;
	
	List<Space> findAllSpaces(@Param(value = "distSpaceList")List<Long> distSpaceList) throws Exception;

	Integer findSpaceCount(@Param(value = "space")Space entity,@Param(value = "distSpaceList")List<Long> distSpaceList) throws Exception;

	List<Space> findSpaces(@Param(value = "space")Space entity,@Param(value = "distSpaceList")List<Long> distSpaceList) throws Exception;
	
	Integer getSpaceCountByIdOrName(Space entity) throws Exception;
	
	Integer addSpace(Space space) throws Exception;

	Integer updateSpace(Space space) throws Exception;

	List<Space> findSpacesWithSubByNo(List<Long> spaceNos) throws Exception;
	
	Integer deleteSpace(@Param(value="spaceNo") Long spaceNo) throws Exception;
	
	/**
	 * test if operator has write permission with the space list.
	 * @param curOper
	 * @param spaceList
	 * @return -  !=0:not permission,  0:has permission
	 */
	Integer hasWritePermission(@Param(value="curOper")Operator curOper, @Param(value="spaceList")List<Space> spaceList);
	
	Integer deleteSpaceCardRegionMapBySpaceNo(Long spaceNo) throws Exception;
	
	Integer deleteSpaceCardRegionMaps(@Param(value = "spaceNo")Long spaceNo, @Param(value = "regionIds")Long[] regionIds) throws Exception;
	
	Integer addSpaceCardRegionMap(@Param(value = "spaceNo")Long spaceNo, @Param(value = "regionId")Long regionId) throws Exception;
	
	Integer findSpaceCardRegionMapCountByRegionId(Long[] regionIds) throws Exception;
	
	Integer findSpaceCountById(List<String> spaceIds) throws Exception;
	
	List<Space> findDistSpaceByOperNo(Long operatorNo) throws Exception;
	
	/**
	 * 查找operator关联Space, 不包括子Space
	 */
	List<Space> findOperatorSpaces(Space entity) throws Exception;
	// count
	Integer findOperatorSpacesCount(Space entity) throws Exception;
	
	List<Space> findSpaceByIds(String[] spaceIds) throws Exception;
	
	List<Space> findSpaceByParentId(Long parentId) throws Exception;

	Integer findStrategySpaceNoSelectCount(@Param(value = "strategyNo")Long strategyNo,@Param(value = "companyNo")Long companyNo, 
			@Param(value = "space")Space space,@Param("excludeIds") Long[] excludeIds) throws Exception;	
	List<Space> findStrategySpaceNoSelect(@Param(value = "strategyNo")Long strategyNo, @Param(value = "companyNo")Long companyNo,
			@Param(value = "space")Space space,@Param("excludeIds") Long[] excludeIds) throws Exception;
}
