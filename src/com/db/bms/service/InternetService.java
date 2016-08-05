package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Internet;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Topic;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;

 
public interface InternetService {
 
	Internet findInternetByNo(Long internetNo) throws Exception;
	
	List<Internet> findInternetsByNo(List<Long> internetNos) throws Exception;

	Internet findInternetById(String internetId) throws Exception;
    
    List<Internet> findAllInternets(List<Long> distInternetList) throws Exception;
    
    List<Internet> findAllInternets(Operator curOper) throws Exception;
    
	public List<Internet> findInternets(Internet entity,List<Long> distInternetList) throws Exception;
	
    boolean isInternetRepeateIdOrName(Internet search) throws Exception;

    void saveOrUpdate(Internet internet) throws Exception;

    List<Internet> findInternetsWithSubByNo(List<Long> internetNos) throws Exception;

    void deleteInternets(List<Internet> list) throws Exception;
	
	void deleteInternetCardRegionMaps(Long internetNo, Long[] regionIds) throws Exception;
	
	void addInternetCardRegionMap(Long internetNo, Long[] regionIds) throws Exception;
	
	List<CardRegion> findInternetCardRegions(Long internetNo, CardRegion region) throws Exception;
	
	List<CardRegion> findInternetCardRegionsNoSelect(Long internetNo, CardRegion region) throws Exception;
	
	boolean isInternetReferenceCardRegion(Long[] regionIds) throws Exception;
	
	Integer findInternetCountById(List<String> internetIds) throws Exception;
	
	List<Internet> findDistInternetByOperNo(Long operatorNo) throws Exception;
	

	/**
	 * 查找operator关联internet,如果admin, 返回全部
	 * 和findAllInternets()区别：返回结果不包括子internet
	 */
	List<Internet> findOperatorInternets(Internet entity) throws Exception;
	
	GetAreaCodeRESP getAreaCodeList(GetAreaCodeREQT request) throws Exception;
	
	List<Internet> findInternetByParentId(Long parentId) throws Exception;
	
}
