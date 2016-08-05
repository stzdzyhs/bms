package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CardRegionMapper;
import com.db.bms.dao.InternetMapper;
import com.db.bms.dao.OperatorMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Internet;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.Strategy;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.InternetService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.AreaCodeInfo;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;

 
@Service("internetService")
public class InternetServiceImpl implements InternetService{ 
	
	@Autowired
	private InternetMapper mapper;

	@Autowired
	private CardRegionMapper cardRegionMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private OperatorMapper operatorMapper;
	
	@Autowired
	StrategyService strategyService;

	public InternetMapper getMapper() {
		return mapper;
	}

	@Override
	public Internet findInternetByNo(Long internetNo) throws Exception {
		return mapper.findInternetByNo(internetNo);
	}

	@Override
	public List<Internet> findInternetsByNo(List<Long> internetNos)
			throws Exception {
		return mapper.findInternetsByNo(internetNos);
	}

	@Override
	public Internet findInternetById(String internetId) throws Exception {
		return mapper.findInternetById(internetId);
	}

	@Override
	public List<Internet> findAllInternets(List<Long> distInternetList) throws Exception {
		return mapper.findAllInternets(distInternetList);
	}

	@Override
	public List<Internet> findAllInternets(Operator curOper) throws Exception {
		
		List<Long> distInternetList = new ArrayList<Long>();
		switch (OperatorType.getType(curOper.getType())) {
		case ORDINARY_OPER:
			List<Internet> internetList = mapper.findDistInternetByOperNo(curOper.getOperatorNo());
			if (internetList != null && internetList.size() > 0){
				for (Internet cmpy : internetList){
					distInternetList.add(cmpy.getInternetNo());
				}
			}

			break;
		}
		List<Internet> belongList = mapper.findAllInternets(distInternetList);
		return belongList;
	}

	@Override
	public List<Internet> findInternets(Internet entity,List<Long> distInternetList) throws Exception {
		PageUtil page = entity.getPageUtil();
		int count = mapper.findInternetCount(entity,distInternetList);
		page.setRowCount(count);
		return mapper.findInternets(entity,distInternetList);
	}

	@Override
	public boolean isInternetRepeateIdOrName(Internet search) throws Exception {
		int count = this.mapper.getInternetCountByIdOrName(search);
		return count > 0 ? true : false;
	}

	@Override
	public void saveOrUpdate(Internet internet) throws Exception { 
		if (internet.getInternetNo() == null) { 
			internet.setCreateTime(DateUtil.getCurrentTime());  
			
			Long a = this.mapper.getPrimaryKey();
			internet.setInternetNo(a); 
			
			Long pid = internet.getParentId(); 
			if (pid != null && pid >= 0) { 
				Internet parent = this.mapper.findInternetByNo(internet
						.getParentId()); 
				String path = parent.getPath() + internet.getInternetNo()
						+ Delimiters.DOT; 
				internet.setPath(path); 
			} else {
				String path = internet.getInternetNo() + Delimiters.DOT;
				internet.setPath(path);
			}

			this.mapper.addInternet(internet);
		} else {
			
			this.mapper.updateInternet(internet);
		}

	}

	@Override
	public List<Internet> findInternetsWithSubByNo(List<Long> internetNos)
			throws Exception {
		return mapper.findInternetsWithSubByNo(internetNos);
	}

	@Override
	public void deleteInternets(List<Internet> list) throws Exception { 
		for (Internet internet : list) { 
			if (internet.getInternetNo() != null) { 
				Long internetNo = internet.getInternetNo();  
				mapper.deleteInternet(internetNo); 
			}
		}
	}

	@Override
	public void addInternetCardRegionMap(Long internetNo, Long[] regionIds)
			throws Exception {
		for (Long regionId : regionIds) {
			mapper.addInternetCardRegionMap(internetNo, regionId);
		}
	}

	@Override
	public List<CardRegion> findInternetCardRegions(Long internetNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findInternetRegionCount(internetNo, region);
		page.setRowCount(count);
		return cardRegionMapper.findInternetRegions(internetNo, region);
	}

	@Override
	public List<CardRegion> findInternetCardRegionsNoSelect(Long internetNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findInternetRegionNoSelectCount(internetNo,
				region);
		page.setRowCount(count);
		return cardRegionMapper.findInternetRegionsNoSelect(internetNo, region);
	}

	@Override
	public void deleteInternetCardRegionMaps(Long internetNo, Long[] regionIds)
			throws Exception {
		mapper.deleteInternetCardRegionMaps(internetNo, regionIds);

	}

	@Override
	public boolean isInternetReferenceCardRegion(Long[] regionIds)
			throws Exception {
		int count = mapper.findInternetCardRegionMapCountByRegionId(regionIds);
		return count > 0 ? true : false;
	}

	@Override
	public Integer findInternetCountById(List<String> internetIds)
			throws Exception {
		return mapper.findInternetCountById(internetIds);
	}
	
	@Override
	public List<Internet> findDistInternetByOperNo(Long operatorNo)
			throws Exception {
		return mapper.findDistInternetByOperNo(operatorNo);
	}


	@Override
	public List<Internet> findOperatorInternets(Internet internet) throws Exception {
		PageUtil page = internet.getPageUtil();
		if (page.getPaging()) {
			int ret = this.mapper.findOperatorInternetsCount(internet);
			page.setRowCount(ret);
		}
		List<Internet> ret = this.mapper.findOperatorInternets(internet);
		return ret;
	}

	@Override
	public GetAreaCodeRESP getAreaCodeList(GetAreaCodeREQT request)
			throws Exception {
		GetAreaCodeRESP response = new GetAreaCodeRESP();
		response.setSerialNo(request.getSerialNo());
		response.setSystemId(request.getSystemId());
		response.setResultCode(CommonResultCode.SUCCESS.getResultCode());
		response.setResultDesc("Operation is successful.");
		List<AreaCodeInfo> areaCodeList = new ArrayList<AreaCodeInfo>();
		response.setAreaCodeList(areaCodeList);
		
		Portal portal = portalMapper.findPortalBySysId(request.getSystemId());
	    if (portal == null){
			response.setResultCode(CommonResultCode.NOT_FOUND_SYSTEM.getResultCode());
			response.setResultDesc("Could not find the system.");
			return response;
	    }
	    
	    if (PortalStatus.getStatus(portal.getStatus()) != PortalStatus.ENABLE){
			response.setResultCode(CommonResultCode.NO_ACCESS_RIGHTS.getResultCode());
			response.setResultDesc("No access rights.");
			return response;
	    }
		
	    //查询指定运营商
	    if (StringUtils.isNotEmpty(request.getInternetId())){
	    	Internet internet = mapper.findInternetById(request.getInternetId());
	        AreaCodeInfo areaCode = convertInternet(internet);
	        areaCodeList.add(areaCode);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
	    
	    Internet search = new Internet();
		PageUtil page = search.getPageUtil();
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		int totalCount = mapper.findInternetCount(search, null);
		page.setRowCount(totalCount);
		List<Internet> internetList = mapper.findInternets(search, null);
		Iterator<Internet> it = internetList.iterator();
		while (it.hasNext()){
			Internet internet = it.next();
			AreaCodeInfo areaCode = convertInternet(internet);
			areaCodeList.add(areaCode);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private AreaCodeInfo convertInternet(Internet internet){
		AreaCodeInfo areaCode = new AreaCodeInfo();
		areaCode.setInternetId(internet.getInternetId());
		if (internet.getCardRegionList() != null && internet.getCardRegionList().size() > 0){
			StringBuffer buffer = new StringBuffer();
			for (CardRegion region : internet.getCardRegionList()){
				switch (RegionCodeType.getType(region.getCodeType())){
				case REGION:
					buffer.append(region.getRegionCode());
					break;
				case SECTION:
					buffer.append(region.getRegionSectionBegin()).append(Delimiters.WHIFFLETREE).append(region.getRegionSectionEnd());
					break;
				}
				buffer.append(Delimiters.COMMA);

			}
			
			areaCode.setAreaId(buffer.substring(0, buffer.length() - 1));
		}
		
		return areaCode;
	}

	@Override
	public List<Internet> findInternetByParentId(Long parentId) throws Exception {
		return mapper.findInternetByParentId(parentId);
	}
    
	
}
