package com.db.bms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.PortalMapper;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.service.PortalService;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.core.PageUtil;


@Service("portalService")
public class PortalServiceImpl implements PortalService {

	@Autowired
	private PortalMapper portalMapper;

	@Override
	public Portal findPortalById(Long sysNo) throws Exception {
		return portalMapper.findPortalById(sysNo);
	}

	@Override
	public List<Portal> findPortalsById(Long[] sysNos) throws Exception {
		return portalMapper.findPortalsById(sysNos);
	}
	
	@Override
	public Integer findPortalCount(Portal search) throws Exception {
		return portalMapper.findPortalCount(search);
	}
	
	@Override
	public List<Portal> findPortals(Portal search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = portalMapper.findPortalCount(search);
		page.setRowCount(count);
		return portalMapper.findPortals(search);
	}

	@Override
	public boolean isRepeatPortal(Portal search) throws Exception {
		int count = portalMapper.getPortalCountByIdOrName(search);
		return count > 0 ? true : false;
	}

	@Override
	public void addPortal(Portal portal) throws Exception {
		portalMapper.addPortal(portal);
	}

	@Override
	public void updatePortal(Portal portal) throws Exception {
		portalMapper.updatePortal(portal);
	}

	@Override
	public void deletePortalsById(Long[] sysNos) throws Exception {
		portalMapper.deletePortalsById(sysNos);
	}

	@Override
	public void auditPortal(Integer status, Long[] sysNos) throws Exception {
		portalMapper.updatePortalStatus(status, sysNos, DateUtil.getCurrentTime());
	}



}
