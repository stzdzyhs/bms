package com.db.bms.service;

import java.util.List;

import com.db.bms.entity.Portal;

/**
 * portal service
 */
public interface PortalService {
	
	Portal findPortalById(Long sysNo) throws Exception;
	
	List<Portal> findPortalsById(Long[] sysNos) throws Exception;
	
	Integer findPortalCount(Portal search) throws Exception;
	
	List<Portal> findPortals(Portal search) throws Exception;
	
	boolean isRepeatPortal(Portal search) throws Exception;
	
	void addPortal(Portal portal) throws Exception;
	
	void updatePortal(Portal portal) throws Exception;
	
	void deletePortalsById(Long[] sysNos) throws Exception;
	
	void auditPortal(Integer status, Long[] sysNos) throws Exception;
	
}
