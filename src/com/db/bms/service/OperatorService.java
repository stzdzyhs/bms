package com.db.bms.service;

import java.io.File;
import java.util.List;

import com.db.bms.entity.Operator;

public interface OperatorService {

	List<Operator> findOperatorsPage(Operator search) throws Exception;

	List<Operator> findOperatorsWithRole(Operator entity) throws Exception;

	void saveOrUpdateUser(Operator operator, Long[] companyNos) throws Exception;

	boolean isOperatorRepeateIdOrName(Operator operator) throws Exception;

	List<Operator> findOperatorsById(List<Long> userNos) throws Exception;

	void deleteUsers(List<Operator> list) throws Exception;

	public Integer findOperatorCountByRoleNo(Long[] roleNos) throws Exception;

	void deleteOperatorRole(Long operatorNo) throws Exception;

	void addOperatorRole(Long operatorNo, Long roleNo) throws Exception;

	int getOperatorsByCompanyNo(Long[] rtId) throws Exception; 
	
	int getOperatorsByInternetNo(Long[] rtId) throws Exception;

	Operator findOperatorById(Object key) throws Exception;

	List<Operator> findAllOperators(Operator entity) throws Exception;

	public boolean updatePwd(Operator t) throws Exception;

	List<Operator> findOtherOperators(Operator condition) throws Exception;

	// 查找当前用户所在机构的所有下属机构的用户
	List<Operator> findAllJuniorOperators(Long id);
	
	void calculateUsedSpace(Operator curOper, File file, boolean add) throws Exception;
	
	List<Operator> findResourceAllocOperators(Operator oper, Integer type, Long resourceId)
	throws Exception;

	List<Operator> findResourceNoAllocOperators(Operator oper, Integer type, Long resourceId)
	throws Exception;
	
	boolean validateUsedSpace(Operator curOper, long size) throws Exception; 
	 
	/*Operator findOperatorCommand(long operatorNo) throws Exception; 
	*/
	Operator findResourceCommand(String resourceName) throws Exception;  
	
	//查询用户可用资源
    List<Long> findResourceId(String operatorId,String type) throws Exception; 
    
    //查询有该资源权限的用户
    List<String> findResourceOperator(Long resourceId,String type) throws Exception;
    
    //添加用户资源
    void addOperatorResource(String operatorId,Long topicId,String type) throws Exception; 
    
    //查询父级
    Operator findOperator(Long createBy) throws Exception;
    
    //根据ID查询 
    Long findOperatorId(String operatorId) throws Exception;

}