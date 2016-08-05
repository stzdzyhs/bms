package com.db.bms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.CardRegion;
import com.db.bms.entity.Company;
import com.db.bms.entity.Operator;
import com.db.bms.sync.portal.protocal.GetAreaCodeREQT;
import com.db.bms.sync.portal.protocal.GetAreaCodeRESP;

public interface CompanyService {

	Company findCompanyByNo(Long companyNo) throws Exception;
	
	List<Company> findCompanysByNo(List<Long> companyNos) throws Exception;

	Company findCompanyById(String companyId) throws Exception;
    
    List<Company> findAllCompanys(List<Long> distCompanyList) throws Exception;
    
    List<Company> findAllCompanys(Operator curOper) throws Exception;
    
	public List<Company> findCompanys(Company entity,List<Long> distCompanyList) throws Exception;
	
    boolean isCompanyRepeateIdOrName(Company search) throws Exception;

    void saveOrUpdate(Company company) throws Exception;

    List<Company> findCompanysWithSubByNo(List<Long> companyNos) throws Exception;

    void deleteCompanys(List<Company> list) throws Exception;
	
	void deleteCompanyCardRegionMaps(Long companyNo, Long[] regionIds) throws Exception;
	
	void addCompanyCardRegionMap(Long companyNo, Long[] regionIds) throws Exception;
	
	List<CardRegion> findCompanyCardRegions(Long companyNo, CardRegion region) throws Exception;
	
	List<CardRegion> findCompanyCardRegionsNoSelect(Long companyNo, CardRegion region) throws Exception;
	
	boolean isCompanyReferenceCardRegion(Long[] regionIds) throws Exception;
	
	Integer findCompanyCountById(List<String> companyIds) throws Exception;
	
	List<Company> findDistCompanyByOperNo(Long operatorNo) throws Exception;
	
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
	List<Company> findStrategyCompanyNoSelect(Long strategyNo,
			Long companyNo, Company company,Long[] excludeIds) throws Exception;
	
	/**
	 * search company and its children company.
	 * @param companyNo - the companyNo.
	 * @param company - search filter
	 * @param excludeIds - can be null.
	 * @return
	 * @throws Exception
	 */
	List<Company> findCompanyAndChildrenByNo(@Param("companyNo")Long companyNo, @Param("company")Company company, 
			@Param("excludeIds")Long[] excludeIds) throws Exception;
	
	/**
	 * 查找operator关联company,如果admin, 返回全部
	 * 和findAllCompanys()区别：返回结果不包括子company
	 */
	List<Company> findOperatorCompanys(Company entity) throws Exception;
	
	GetAreaCodeRESP getAreaCodeList(GetAreaCodeREQT request) throws Exception;
	
	List<Company> findCompanyByParentId(Long parentId) throws Exception; 
	
}
