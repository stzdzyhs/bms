package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Company;

public interface CompanyMapper {

	Long getPrimaryKey() throws Exception;

	Company findCompanyByNo(Long companyNo) throws Exception;
	
	List<Company> findCompanysByNo(List<Long> companyNos) throws Exception;
	
	Company findCompanyById(String companyId) throws Exception;
	
	List<Company> findAllCompanys(@Param(value = "distCompanyList")List<Long> distCompanyList) throws Exception;

	Integer findCompanyCount(@Param(value = "cmpy")Company entity,@Param(value = "distCompanyList")List<Long> distCompanyList) throws Exception;

	List<Company> findCompanys(@Param(value = "cmpy")Company entity,@Param(value = "distCompanyList")List<Long> distCompanyList) throws Exception;
	
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
	Integer findCompanyAndChildrenByNoCount(@Param("companyNo")Long companyNo, @Param("company")Company company, 
			@Param("excludeIds")Long[] excludeIds) throws Exception;
	
	Integer getCompanyCountByIdOrName(Company entity) throws Exception;
	
	Integer addCompany(Company company) throws Exception;

	Integer updateCompany(Company company) throws Exception;

	List<Company> findCompanysWithSubByNo(List<Long> companyNos) throws Exception;
	
	Integer deleteCompany(Object key) throws Exception;
	
	Integer deleteCompanyCardRegionMapByCompanyNo(Long companyNo) throws Exception;
	
	Integer deleteCompanyCardRegionMaps(@Param(value = "companyNo")Long companyNo, @Param(value = "regionIds")Long[] regionIds) throws Exception;
	
	Integer addCompanyCardRegionMap(@Param(value = "companyNo")Long companyNo, @Param(value = "regionId")Long regionId) throws Exception;
	
	Integer findCompanyCardRegionMapCountByRegionId(Long[] regionIds) throws Exception;
	
	Integer findCompanyCountById(List<String> companyIds) throws Exception;
	
	List<Company> findDistCompanyByOperNo(Long operatorNo) throws Exception;
	
	/**
	 * 查找operator关联company, 不包括子company
	 */
	List<Company> findOperatorCompanys(Company entity) throws Exception;
	// count
	Integer findOperatorCompanysCount(Company entity) throws Exception;
	
	List<Company> findCompanyByIds(String[] companyIds) throws Exception;
	
	List<Company> findCompanyByParentId(Long parentId) throws Exception;
	
	Integer findStrategyCompanyNoSelectCount(@Param(value = "strategyNo")Long strategyNo,
			@Param(value = "companyNo")Long companyNo,
			@Param(value = "company")Company company,@Param("excludeIds")Long[] excludeIds) throws Exception;
	List<Company> findStrategyCompanyNoSelect(@Param(value = "strategyNo")Long strategyNo,
			@Param(value = "companyNo")Long companyNo,
			@Param(value = "company")Company company,@Param("excludeIds") Long[] excludeIds) throws Exception;
	
	/*.....................................................................................................*/
	Integer findCompanysCount1(@Param(value = "company")Company entity) throws Exception;
	
	List<Company> findCompanys1(@Param(value = "company")Company entity) throws Exception;
	
	List<Company> findAllCompanysg(@Param(value = "company") Company entity,
			@Param(value = "list") List<Long> companyNoList) throws Exception;
	
	List<Company> findAllCompanysgg(@Param(value = "company") Company entity,
			@Param(value = "list") List<Long> companyNoList) throws Exception;
	
	Company findCompanyByNog(Long companyNo) throws Exception;
	
	List<Company> findAllCompanys(@Param(value = "company") Company entity,
			@Param(value = "list") List<Long> companyNoList) throws Exception;

}
