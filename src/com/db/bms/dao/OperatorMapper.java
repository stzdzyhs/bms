package com.db.bms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Operator;

public interface OperatorMapper {

	Long getPrimaryKey() throws Exception;

	List<Operator> findAllOperators(Operator operator) throws Exception;

	Integer findOperatorCount(@Param(value = "oper") Operator oper)
			throws Exception;

	List<Operator> findOperators(@Param(value = "oper") Operator oper)
			throws Exception;

	List<Operator> findOperatorsWithRole(Operator entity) throws Exception;

	Integer findOperatorCountByRoleNo(Long[] roleNos) throws Exception;

	void deleteOperatorRole(Long operatorNo) throws Exception;

	void addOperatorRole(@Param(value = "operatorNo") Long operatorNo,
			@Param(value = "roleNo") Long roleNo) throws Exception;

	int getOperatorsByCompanyNo(Long[] rtId) throws Exception; 
	int getOperatorsByInternetNo(Long[] rtId) throws Exception;

	Integer updateOperator(Operator t) throws Exception;

	Integer addOperator(Operator t) throws Exception;

	List<Operator> findOperatorsById(List<Long> userNos) throws Exception;

	Integer deleteOperator(Object key) throws Exception;

	Operator findOperatorById(Object key) throws Exception;

	Integer getOperatorCountByIdOrName(Operator entity) throws Exception;

	Integer updatePwd(Operator t) throws Exception;

	List<Operator> findOtherOperators(Operator entity);

	Integer findOtherCountOperators(Operator entity);

	List<Operator> selectBySql(@Param(value = "sql") String sql)
			throws Exception;
	
	Integer setUsedSize(@Param(value = "operatorNo")Long operatorNo, @Param(value = "usedSize")Long usedSize) throws Exception;
	
	Integer addOperatorCompany(@Param(value = "operatorNo")Long operatorNo, @Param(value = "companyNo")Long companyNo) throws Exception;
	
	Integer deleteOperatorCompany(@Param(value = "operatorNo")Long operatorNo) throws Exception;
	
	Integer deleteOperatorCompanyByCompanyNo(@Param(value = "companyNo")Long companyNo) throws Exception;
	
	Integer findResourceAllocOperatorCount(@Param(value = "oper") Operator oper, @Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId)
	throws Exception;

	List<Operator> findResourceAllocOperators(@Param(value = "oper") Operator oper, @Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId)
	throws Exception;

	Integer findResourceNoAllocOperatorCount(@Param(value = "oper") Operator oper, @Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId)
	throws Exception;

	List<Operator> findResourceNoAllocOperators(@Param(value = "oper") Operator oper, @Param(value = "type")Integer type, @Param(value = "resourceId")Long resourceId)
	throws Exception; 
	
	/*Operator findOperatorCommand(long operatorNo) throws Exception; */
	 
	Long findResourceCommand(long resourceNo) throws Exception; 
    
    //查询用户可用资源
    List<Long> findResourceId(@Param(value = "operatorId")String operatorId,@Param(value = "type")String type) throws Exception;
    
    //查询有该资源权限的用户
    List<String> findResourceOperator(@Param(value = "resourceId")Long resourceId,@Param(value = "type")String type) throws Exception;
    
    //添加用户资源
    void addOperatorResource(@Param(value = "operatorId") String operatorId,@Param(value = "resourceId") Long topicId,@Param(value = "type") String type) throws Exception;
    
    //查询父级
    Operator findOperator(Object operatorNo) throws Exception; 
    
    //根据ID查询 
    Long findOperatorId(@Param(value = "operatorId") String operatorId) throws Exception;
    
    Integer findOperatorResourceCount(@Param(value = "operatorId")String operatorId,@Param(value = "resourceId")Long resourceId,@Param(value = "type")String type)throws Exception;
}