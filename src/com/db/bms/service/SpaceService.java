package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Space;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;

public interface SpaceService {
 
	Space findSpaceByNo(Long spaceNo) throws Exception;
	
	List<Space> findSpacesByNo(List<Long> spaceNos) throws Exception;

	Space findSpaceById(String spaceId) throws Exception;
    
    List<Space> findAllSpaces(List<Long> distSpaceList) throws Exception;
    
    List<Space> findAllSpaces(Operator curOper) throws Exception;
    
	public List<Space> findSpaces(Space entity,List<Long> distSpaceList) throws Exception;
	
    boolean isSpaceRepeateIdOrName(Space search) throws Exception;

    void saveOrUpdate(Space space) throws Exception;

    List<Space> findSpacesWithSubByNo(List<Long> spaceNos) throws Exception;

    void deleteSpaces(Operator operator, List<Space> list) throws Exception;
	
	void deleteSpaceCardRegionMaps(Long spaceNo, Long[] regionIds) throws Exception;
	
	void addSpaceCardRegionMap(Long spaceNo, Long[] regionIds) throws Exception;
	
	void checkWritePermission(Operator operator, List<Space> list)  throws Exception;
	
	List<CardRegion> findSpaceCardRegions(Long spaceNo, CardRegion region) throws Exception;
	
	List<CardRegion> findSpaceCardRegionsNoSelect(Long spaceNo, CardRegion region) throws Exception;
	
	boolean isSpaceReferenceCardRegion(Long[] regionIds) throws Exception;
	
	Integer findSpaceCountById(List<String> spaceIds) throws Exception;
	
	List<Space> findDistSpaceByOperNo(Long operatorNo) throws Exception;
	

	/**
	 * 查找operator关联Space,如果admin, 返回全部
	 * 和findAllSpaces()区别：返回结果不包括子Space
	 */
	List<Space> findOperatorSpaces(Space entity) throws Exception;
	
	GetAreaCodeRESP getAreaCodeList(GetAreaCodeREQT request) throws Exception;
	
	List<Space> findSpaceByParentId(Long parentId) throws Exception;
	
}
