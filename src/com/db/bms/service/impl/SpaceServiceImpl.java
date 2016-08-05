package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CardRegionMapper;
import com.db.bms.dao.OperatorMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.dao.SpaceMapper;
import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.Space;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.SpaceService;
import com.db.bms.service.StrategyService;
import com.db.bms.sync.portal.protocal.AreaCodeInfo;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;
import com.db.bms.utils.ArrayUtils;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.ResultCode;
import com.db.bms.utils.ResultCodeException;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;
 
  
@Service("SpaceService")
public class SpaceServiceImpl implements SpaceService{
      
	@Autowired
	private SpaceMapper spaceMapper;

	@Autowired
	private CardRegionMapper cardRegionMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private OperatorMapper operatorMapper;
	
	@Autowired
	StrategyService strategyService;

	public SpaceMapper getMapper() {
		return spaceMapper;
	}

	@Override
	public Space findSpaceByNo(Long spaceNo) throws Exception {
		return spaceMapper.findSpaceByNo(spaceNo);
	}

	@Override
	public List<Space> findSpacesByNo(List<Long> spaceNos)
			throws Exception {
		return spaceMapper.findSpacesByNo(spaceNos);
	}

	@Override
	public Space findSpaceById(String spaceId) throws Exception {
		return spaceMapper.findSpaceById(spaceId);
	}

	@Override
	public List<Space> findAllSpaces(List<Long> distSpaceList) throws Exception {
		return spaceMapper.findAllSpaces(distSpaceList);
	}

	@Override
	public List<Space> findAllSpaces(Operator curOper) throws Exception {
		
		List<Long> distSpaceList = new ArrayList<Long>();
		switch (OperatorType.getType(curOper.getType())) {
		case ORDINARY_OPER:
			List<Space> spaceList = spaceMapper.findDistSpaceByOperNo(curOper.getOperatorNo());
			if (spaceList != null && spaceList.size() > 0){
				for (Space cmpy : spaceList){
					distSpaceList.add(cmpy.getSpaceNo());
				}
			}

			break;
		default:
			break;
		}
		List<Space> belongList = spaceMapper.findAllSpaces(distSpaceList);
		return belongList;
	}

	@Override
	public List<Space> findSpaces(Space entity,List<Long> distSpaceList) throws Exception {
		PageUtil page = entity.getPageUtil();
		int count = spaceMapper.findSpaceCount(entity,distSpaceList);
		page.setRowCount(count);
		return spaceMapper.findSpaces(entity,distSpaceList);
	}

	@Override
	public boolean isSpaceRepeateIdOrName(Space search) throws Exception {
		int count = this.spaceMapper.getSpaceCountByIdOrName(search);
		return count > 0 ? true : false;
	}

	@Override
	public void saveOrUpdate(Space space) throws Exception { 
		if (space.getSpaceNo() == null) { 
			space.setCreateTime(DateUtil.getCurrentTime());  
			
			Long a = this.spaceMapper.getPrimaryKey();
			space.setSpaceNo(a); 
			
			Long pid = space.getParentId(); 
			if (pid != null && pid >= 0) { 
				Space parent = this.spaceMapper.findSpaceByNo(space.getParentId()); 
				String path = parent.getPath() + space.getSpaceNo()	+ Delimiters.DOT; 
				space.setPath(path); 
			} else {
				String path = space.getSpaceNo() + Delimiters.DOT;
				space.setPath(path);
			}
			this.spaceMapper.addSpace(space);
		} else {
			
			this.spaceMapper.updateSpace(space);
		}

	}

	@Override
	public List<Space> findSpacesWithSubByNo(List<Long> spaceNos)
			throws Exception {
		return spaceMapper.findSpacesWithSubByNo(spaceNos);
	}

	public static void checkWritePermission(SpaceMapper spaceMapper, Operator operator, List<Space> list)  throws Exception {
		if(operator==null) {
			throw new ResultCodeException(ResultCode.NO_ACCESS_RIGHTS, "null operator");
		}
		if(ArrayUtils.getSize(list)<1) {
			return;
		}
		
		Integer ret;
		if(operator.type == Operator.TYPE_SUPER_ADMIN) {
		}
		else if(operator.type == Operator.TYPE_COMPANY_ADMIN) {
			ret = spaceMapper.hasWritePermission(operator, list);
			if(ret!=0) {
				// show which space has no permission
				List<Space> list2 = new ArrayList<Space>();
				for(Space s:list) {
					list2.clear();
					list2.add(s);
					ret = spaceMapper.hasWritePermission(operator, list2);
					if(ret!=0) {
						throw new ResultCodeException(ResultCode.NO_ACCESS_RIGHTS, "没有操作空分组 " + s.spaceName + " 的权限");		
					}
				}
				throw new ResultCodeException(ResultCode.NO_ACCESS_RIGHTS, "没有操作空分组的权限");		
			}
		}
		else if(operator.type == Operator.TYPE_ORDINARY_OPER) {
			for(Space s:list) {
				if(!operator.operatorNo.equals(s.createBy)) {
					throw new ResultCodeException(ResultCode.NO_ACCESS_RIGHTS, "没有操作空分组 " + s.spaceName + " 的权限");		
				}
			}
		}
		else {
			throw new ResultCodeException(ResultCode.NO_ACCESS_RIGHTS, "invalid operator");
		}
	}
	
	@Override
	public void checkWritePermission(Operator operator, List<Space> list) throws Exception {
		checkWritePermission(this.spaceMapper, operator, list);
	}
	
	@Override
	public void deleteSpaces(Operator operator, List<Space> list) throws Exception {
		checkWritePermission(operator, list);
		for (Space space : list) { 
			if (space.getSpaceNo() != null) { 
				Long spaceNo = space.getSpaceNo();  
				spaceMapper.deleteSpace(spaceNo); 
			}
		}
	}

	@Override
	public void addSpaceCardRegionMap(Long spaceNo, Long[] regionIds)
			throws Exception {
		for (Long regionId : regionIds) {
			spaceMapper.addSpaceCardRegionMap(spaceNo, regionId);
		}
	}

	@Override
	public List<CardRegion> findSpaceCardRegions(Long spaceNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findSpaceRegionCount(spaceNo, region);
		page.setRowCount(count);
		return cardRegionMapper.findSpaceRegions(spaceNo, region);
	}

	@Override
	public List<CardRegion> findSpaceCardRegionsNoSelect(Long spaceNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findSpaceRegionNoSelectCount(spaceNo,
				region);
		page.setRowCount(count);
		return cardRegionMapper.findSpaceRegionsNoSelect(spaceNo, region);
	}

	@Override
	public void deleteSpaceCardRegionMaps(Long spaceNo, Long[] regionIds)
			throws Exception {
		spaceMapper.deleteSpaceCardRegionMaps(spaceNo, regionIds);

	}

	@Override
	public boolean isSpaceReferenceCardRegion(Long[] regionIds)
			throws Exception {
		int count = spaceMapper.findSpaceCardRegionMapCountByRegionId(regionIds);
		return count > 0 ? true : false;
	}

	@Override
	public Integer findSpaceCountById(List<String> spaceIds)
			throws Exception {
		return spaceMapper.findSpaceCountById(spaceIds);
	}
	
	@Override
	public List<Space> findDistSpaceByOperNo(Long operatorNo) throws Exception {
		return spaceMapper.findDistSpaceByOperNo(operatorNo);
	}

	@Override
	public List<Space> findOperatorSpaces(Space space) throws Exception {
		PageUtil page = space.getPageUtil();
		if (page.getPaging()) {
			int ret = this.spaceMapper.findOperatorSpacesCount(space);
			page.setRowCount(ret);
		}
		List<Space> ret = this.spaceMapper.findOperatorSpaces(space);
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
	    if (StringUtils.isNotEmpty(request.getSpaceId())){
	    	Space space = spaceMapper.findSpaceById(request.getSpaceId());
	        AreaCodeInfo areaCode = convertSpace(space);
	        areaCodeList.add(areaCode);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
	    
	    Space search = new Space();
		PageUtil page = search.getPageUtil();
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		int totalCount = spaceMapper.findSpaceCount(search, null);
		page.setRowCount(totalCount);
		List<Space> spaceList = spaceMapper.findSpaces(search, null);
		Iterator<Space> it = spaceList.iterator();
		while (it.hasNext()){
			Space space = it.next();
			AreaCodeInfo areaCode = convertSpace(space);
			areaCodeList.add(areaCode);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private AreaCodeInfo convertSpace(Space space){
		AreaCodeInfo areaCode = new AreaCodeInfo();
		areaCode.setSpaceId(space.getSpaceId());
		if (space.getCardRegionList() != null && space.getCardRegionList().size() > 0){
			StringBuffer buffer = new StringBuffer();
			for (CardRegion region : space.getCardRegionList()){
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
	public List<Space> findSpaceByParentId(Long parentId) throws Exception {
		return spaceMapper.findSpaceByParentId(parentId);
	}
	
}
