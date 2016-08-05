package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Portal;

public interface PortalMapper {
	
	Portal findPortalBySysId(@Param(value = "sysId")String sysId) throws Exception;
	
	Portal findPortalById(Long sysNo) throws Exception;
	
	List<Portal> findPortalsById(Long[] sysNos) throws Exception;
	
	Integer findPortalCount(Portal search) throws Exception;
	
	List<Portal> findPortals(Portal search) throws Exception;
	
	Integer getPortalCountByIdOrName(Portal search) throws Exception;
	
	Integer addPortal(Portal portal) throws Exception;
	
	Integer updatePortal(Portal portal) throws Exception;
	
	Integer deletePortalsById(Long[] sysNos) throws Exception;
	
	Integer updatePortalStatus(@Param(value = "status")Integer status, @Param(value = "sysNos")Long[] sysNos, @Param(value = "updateTime")String updateTime) throws Exception;
}
