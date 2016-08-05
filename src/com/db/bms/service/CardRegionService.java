
package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Topic;


public interface CardRegionService {

	
	CardRegion findRegionById(Long regionId) throws Exception;
	
	List<CardRegion> findRegionsById(Long[] regionIds) throws Exception;
	
	CardRegion findRegionByCode(CardRegion region) throws Exception;
	
	List<CardRegion> findRegions(CardRegion region) throws Exception;
	
	boolean isRegionRepeat(CardRegion region) throws Exception;
	
	void addRegion(CardRegion region) throws Exception;
	
	void updateRegion(CardRegion region) throws Exception;
	
	void deleteRegions(Long[] regionIds) throws Exception;
	
	List<CardRegion> findAllRegions(CardRegion region) throws Exception;
	
	boolean isReferenceRegion(Long[] parentIds) throws Exception;
	
}
