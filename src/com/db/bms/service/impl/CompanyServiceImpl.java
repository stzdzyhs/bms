package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CardRegionMapper;
import com.db.bms.dao.CompanyMapper;
import com.db.bms.dao.OperatorMapper;
import com.db.bms.dao.PortalMapper;
import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Company;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Portal;
import com.db.bms.entity.CardRegion.RegionCodeType;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.entity.Portal.PortalStatus;
import com.db.bms.service.CompanyService;
import com.db.bms.sync.portal.protocal.AreaCodeInfo;
import com.db.bms.sync.portal.protocal.CommonResultCode;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.StringUtils;
import com.db.bms.utils.core.PageUtil;

/**
 * <b>功能：</b>用于事物处理<br>
 */

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyMapper mapper;

	@Autowired
	private CardRegionMapper cardRegionMapper;
	
	@Autowired
	private PortalMapper portalMapper;
	
	@Autowired
	private OperatorMapper operatorMapper;

	public CompanyMapper getMapper() {
		return mapper;
	}

	@Override
	public Company findCompanyByNo(Long companyNo) throws Exception {
		return mapper.findCompanyByNo(companyNo);
	}

	@Override
	public List<Company> findCompanysByNo(List<Long> companyNos)
			throws Exception {
		return mapper.findCompanysByNo(companyNos);
	}

	@Override
	public Company findCompanyById(String companyId) throws Exception {
		return mapper.findCompanyById(companyId);
	}

	@Override
	public List<Company> findAllCompanys(List<Long> distCompanyList) throws Exception {
		return mapper.findAllCompanys(distCompanyList);
	}

	@Override
	public List<Company> findAllCompanys(Operator curOper) throws Exception {
		List<Company> belongList = null;
		List<Long> distComanyList = new ArrayList<Long>();
		switch (OperatorType.getType(curOper.getType())) {
		case SUPER_ADMIN:
			belongList = mapper.findAllCompanys(distComanyList);
			break;
		case COMPANY_ADMIN:
		case ORDINARY_OPER:
			Company company = new Company();
			belongList = mapper.findCompanyAndChildrenByNo(curOper.getCompanyNo(),company,null);			
			break;
		}	
		return belongList;
	}

	@Override
	public List<Company> findCompanys(Company entity,List<Long> distCompanyList) throws Exception {
		PageUtil page = entity.getPageUtil();
		int count = mapper.findCompanyCount(entity,distCompanyList);
		page.setRowCount(count);
		return mapper.findCompanys(entity,distCompanyList);
	}

	@Override
	public boolean isCompanyRepeateIdOrName(Company search) throws Exception {
		int count = this.mapper.getCompanyCountByIdOrName(search);
		return count > 0 ? true : false;
	}

	@Override
	public void saveOrUpdate(Company company) throws Exception {
		if (company.getCompanyNo() == null) {
			company.setCreateTime(DateUtil.getCurrentTime());
			company.setCompanyNo(this.mapper.getPrimaryKey());

			Long pid = company.getParentId();
			if (pid != null && pid >= 0) {
				Company parent = this.mapper.findCompanyByNo(company
						.getParentId());
				String path = parent.getPath() + company.getCompanyNo()
						+ Delimiters.DOT;
				company.setPath(path);
			} else {
				String path = company.getCompanyNo() + Delimiters.DOT;
				company.setPath(path);
			}

			this.mapper.addCompany(company);
		} else {
			if (company.getParentId() != -1) {
				Company parent = this.mapper.findCompanyByNo(company
						.getParentId());
				String path = parent.getPath() + company.getCompanyNo()
						+ Delimiters.DOT;
				company.setPath(path);
			}
			this.mapper.updateCompany(company);
		}

	}

	@Override
	public List<Company> findCompanysWithSubByNo(List<Long> companyNos)
			throws Exception {
		return mapper.findCompanysWithSubByNo(companyNos);
	}

	@Override
	public void deleteCompanys(List<Company> list) throws Exception {
		for (Company company : list) {
			if (company.getCompanyNo() != null) {
				operatorMapper.deleteOperatorCompanyByCompanyNo(company.getCompanyNo());
				mapper.deleteCompanyCardRegionMapByCompanyNo(company
						.getCompanyNo());
				mapper.deleteCompany(company);

			}
		}
	}

	@Override
	public void addCompanyCardRegionMap(Long companyNo, Long[] regionIds)
			throws Exception {
		for (Long regionId : regionIds) {
			mapper.addCompanyCardRegionMap(companyNo, regionId);
		}
	}

	@Override
	public List<CardRegion> findCompanyCardRegions(Long companyNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findCompanyRegionCount(companyNo, region);
		page.setRowCount(count);
		return cardRegionMapper.findCompanyRegions(companyNo, region);
	}

	@Override
	public List<CardRegion> findCompanyCardRegionsNoSelect(Long companyNo,
			CardRegion region) throws Exception {
		PageUtil page = region.getPageUtil();
		int count = cardRegionMapper.findCompanyRegionNoSelectCount(companyNo,
				region);
		page.setRowCount(count);
		return cardRegionMapper.findCompanyRegionsNoSelect(companyNo, region);
	}

	@Override
	public void deleteCompanyCardRegionMaps(Long companyNo, Long[] regionIds)
			throws Exception {
		mapper.deleteCompanyCardRegionMaps(companyNo, regionIds);

	}

	@Override
	public boolean isCompanyReferenceCardRegion(Long[] regionIds)
			throws Exception {
		int count = mapper.findCompanyCardRegionMapCountByRegionId(regionIds);
		return count > 0 ? true : false;
	}

	@Override
	public Integer findCompanyCountById(List<String> companyIds)
			throws Exception {
		return mapper.findCompanyCountById(companyIds);
	}
	
	@Override
	public List<Company> findDistCompanyByOperNo(Long operatorNo)
			throws Exception {
		return mapper.findDistCompanyByOperNo(operatorNo);
	}


	@Override
	public List<Company> findOperatorCompanys(Company company) throws Exception {
		PageUtil page = company.getPageUtil();
		if (page.getPaging()) {
			int ret = this.mapper.findOperatorCompanysCount(company);
			page.setRowCount(ret);
		}
		List<Company> ret = this.mapper.findOperatorCompanys(company);
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
	    if (StringUtils.isNotEmpty(request.getCompanyId())){
	    	Company company = mapper.findCompanyById(request.getCompanyId());
	        AreaCodeInfo areaCode = convertCompany(company);
	        areaCodeList.add(areaCode);
			response.setTotalCount(1);
			response.setTotalPage(1);
			response.setCurrentPage(1);
			return response;
	    }
	    
		Company search = new Company();
		PageUtil page = search.getPageUtil();
		page.setPageSize(request.getPageSize());
		page.setPageId(request.getStartPage());
		int totalCount = mapper.findCompanyCount(search, null);
		page.setRowCount(totalCount);
		List<Company> companyList = mapper.findCompanys(search, null);
		Iterator<Company> it = companyList.iterator();
		while (it.hasNext()){
			Company company = it.next();
			AreaCodeInfo areaCode = convertCompany(company);
			areaCodeList.add(areaCode);
		}
		
		response.setTotalCount(totalCount);
		response.setTotalPage(page.getPageCount());
		response.setCurrentPage(page.getPageId());
		return response;
	}
	
	private AreaCodeInfo convertCompany(Company company){
		AreaCodeInfo areaCode = new AreaCodeInfo();
		areaCode.setCompanyId(company.getCompanyId());
		if (company.getCardRegionList() != null && company.getCardRegionList().size() > 0){
			StringBuffer buffer = new StringBuffer();
			for (CardRegion region : company.getCardRegionList()){
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
	public List<Company> findCompanyByParentId(Long parentId) throws Exception {
		return mapper.findCompanyByParentId(parentId);
	}
    
	
	/**
	 * 查找strategy可以关联的company.
	 * 如果strategyNo为null,companyNo不能为null. 
	 * @param strategyNo
	 * @param companyNo
	 * @param company - 过滤条件
	 * @param excludeIds - 排除ids, 可以为null
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Company> findStrategyCompanyNoSelect(Long strategyNo, Long companyNo, 
			Company company, Long[] excludeIds) throws Exception {
		PageUtil pu = company.getPageUtil();
		int cnt = mapper.findStrategyCompanyNoSelectCount(strategyNo, companyNo, company, excludeIds);
		pu.setRowCount(cnt);
		List<Company> list = mapper.findStrategyCompanyNoSelect(strategyNo, companyNo, company, excludeIds);
		return list;
	}
	
	/*.....................................................................................................*/
	
	/**
	 * search company and its children company.
	 * @param companyNo - the companyNo.
	 * @param company - search filter
	 * @param excludeIds - can be null.
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Company> findCompanyAndChildrenByNo(Long companyNo, Company company, 
			Long[] excludeIds) throws Exception {
		
		PageUtil pu = company.getPageUtil();
		int cnt = mapper.findCompanyAndChildrenByNoCount(companyNo, company, excludeIds);
		pu.setRowCount(cnt);
		List<Company> list = mapper.findCompanyAndChildrenByNo(companyNo, company, excludeIds);
		return list;
	}
	
}
