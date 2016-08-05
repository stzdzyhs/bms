package com.db.bms.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CompanyMapper;
import com.db.bms.dao.OperatorMapper;
import com.db.bms.entity.Operator;
import com.db.bms.entity.Role;
import com.db.bms.entity.Operator.OperatorType;
import com.db.bms.service.OperatorService;
import com.db.bms.utils.core.PageUtil;

/**
 * <b>功能：</b>用于事物处理<br>
 */

@Service("operatorService")
public class OperatorServiceImpl implements OperatorService {

	@Autowired
	private OperatorMapper mapper;

	@Autowired
	private CompanyMapper companyMapper;

	public OperatorMapper getMapper() {
		return mapper;
	}

	@Override
	public List<Operator> findOperatorsWithRole(Operator entity)
			throws Exception {
		return this.mapper.findOperatorsWithRole(entity);
	}

	@Override
	public List<Operator> findOperatorsPage(Operator search) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = this.mapper.findOperatorCount(search);
		page.setRowCount(count);
		return this.mapper.findOperators(search);
	}

	@Override
	public Integer findOperatorCountByRoleNo(Long[] roleNos) throws Exception {
		return this.mapper.findOperatorCountByRoleNo(roleNos);
	}

	@Override
	public void saveOrUpdateUser(Operator operator, Long[] companyNos) throws Exception {
		Long operatorNo = null;
		if (operator.getOperatorNo() != null) {
			deleteOperatorRole(operator.getOperatorNo());
			this.mapper.deleteOperatorCompany(operator.getOperatorNo());
			this.mapper.updateOperator(operator);
			operatorNo = operator.getOperatorNo();
		} else {

			operatorNo = mapper.getPrimaryKey();
			operator.setOperatorNo(operatorNo);
			operator.setUsedSize(0L);
			this.mapper.addOperator(operator);
		}

		List<Role> roles = operator.getRoles();
		if (roles != null) {
			for (Role role : roles) {
				this.addOperatorRole(operatorNo, role.getRoleNo());
			}
		}
		
		if (OperatorType.getType(operator.getType()) == OperatorType.ORDINARY_OPER){
			if (companyNos != null && companyNos.length > 0){
				for (Long companyNo : companyNos){
					this.mapper.addOperatorCompany(operator.getOperatorNo(), companyNo);
				}
			}
		}
	}

	public void insertOperatorCompanyBath() {

	}

	@Override
	public boolean isOperatorRepeateIdOrName(Operator operator)
			throws Exception {
		int count = mapper.getOperatorCountByIdOrName(operator);
		return count > 0 ? true : false;
	}

	@Override
	public void deleteOperatorRole(Long operatorNo) throws Exception {
		this.mapper.deleteOperatorRole(operatorNo);
	}

	@Override
	public void addOperatorRole(Long operatorNo, Long roleNo) throws Exception {
		this.mapper.addOperatorRole(operatorNo, roleNo);
	}

	@Override
	public List<Operator> findOperatorsById(List<Long> userNos)
			throws Exception {
		return this.mapper.findOperatorsById(userNos);
	}

	@Override
	public void deleteUsers(List<Operator> list) throws Exception {
		for (Operator operator : list) {
			this.deleteOperatorRole(operator.getOperatorNo());
			this.mapper.deleteOperatorCompany(operator.getOperatorNo());
			this.mapper.deleteOperator(operator);
		}

	}

	@Override
	public int getOperatorsByCompanyNo(Long[] rtId) throws Exception {
		return this.mapper.getOperatorsByCompanyNo(rtId);
	} 
	 
	@Override
	public int getOperatorsByInternetNo(Long[] rtId) throws Exception {
		return this.mapper.getOperatorsByInternetNo(rtId);
	}

	@Override
	public Operator findOperatorById(Object key) throws Exception {
		return mapper.findOperatorById(key);
	}

	@Override
	public List<Operator> findAllOperators(Operator curOper) throws Exception {
		Operator search = new Operator();
		search.setCurOper(curOper);
		List<Operator> list = null;
		switch (OperatorType.getType(curOper.getType())) {
		case SUPER_ADMIN:
			list = this.mapper.findAllOperators(search);
			break;
		case COMPANY_ADMIN:
		case ORDINARY_OPER:
			search.setCreateBy(curOper.getOperatorNo());
			list = this.mapper.findAllOperators(search);
			break;
		}
		return list;
	}

	@Override
	public boolean updatePwd(Operator t) throws Exception {

		int count = mapper.updatePwd(t);
		return count > 0 ? true : false;
	}

	// 查询处理本人和超级管理员的用户
	public List<Operator> findOtherOperators(Operator search) throws Exception {
		PageUtil page = search.getPageUtil();
		int count = this.mapper.findOtherCountOperators(search);
		page.setRowCount(count);
		return mapper.findOtherOperators(search);
	}

	@Override
	public List<Operator> findAllJuniorOperators(Long id) {
		String sql = "select * from sys_operator t where t.operator_no in  (select company_no from bus_company start with company_no ="
				+ id + " connect by prior  company_no= parent_id)";
		List<Operator> list = null;
		try {
			list = this.mapper.selectBySql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void calculateUsedSpace(Operator curOper, File file, boolean add) throws Exception {
		Operator mgOp = null;
		if(curOper.getType() == 2){
			mgOp = this.findOperator(curOper.getCreateBy());
		}
		else{
			mgOp = curOper;
		}
	    if (mgOp.getTotalSize() != -1){
	    	if (file.exists()){
			    long totalSize = mgOp.getTotalSize() * 1024* 1024 * 1024;
			    long usedSize = mgOp.getUsedSize();
		    	if (add){
				    usedSize = usedSize + file.length();
				    if (usedSize > totalSize){
				    	usedSize = totalSize;
				    }
		    	}else{
		    	    usedSize = usedSize - file.length();
		    	    if (usedSize < 0){
		    	    	usedSize = 0;
		    	    }
		    	}
			    mapper.setUsedSize(mgOp.getOperatorNo(), usedSize);
	    	}

	    }
	}

	@Override
	public List<Operator> findResourceAllocOperators(Operator search,
			Integer type, Long resourceId) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = this.mapper.findResourceAllocOperatorCount(search, type, resourceId);
		page.setRowCount(count);
		return this.mapper.findResourceAllocOperators(search, type, resourceId);
	}

	@Override
	public List<Operator> findResourceNoAllocOperators(Operator search,
			Integer type, Long resourceId) throws Exception {
		PageUtil page = search.getPageUtil();
		page.setPaging(true);
		int count = this.mapper.findResourceNoAllocOperatorCount(search, type, resourceId);
		page.setRowCount(count);
		return this.mapper.findResourceNoAllocOperators(search, type, resourceId);
	}

	@Override
	public boolean validateUsedSpace(Operator curOper, long size)
			throws Exception {
		Operator mgOp = null;
		if(curOper.getType() == 2){
			mgOp = this.findOperator(curOper.getCreateBy());
		}
		else{
			mgOp = curOper;
		}
		if (mgOp.getTotalSize() != -1){
		    long totalSize = mgOp.getTotalSize() * 1024* 1024 * 1024;
		    long usedSize = mgOp.getUsedSize();
		    if ((usedSize + size) > totalSize){
		    	return false;
		    }
		}
		return true;
	}

	@Override
	public Operator findResourceCommand(String resourceName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> findResourceId(String operatorId, String type)
			throws Exception {
		return mapper.findResourceId(operatorId,type);
	}

	@Override
	public List<String> findResourceOperator(Long resourceId, String type)
			throws Exception {
		return mapper.findResourceOperator(resourceId, type);
	}

	@Override
	public void addOperatorResource(String operatorId, Long topicId, String type)
			throws Exception {
		if(this.mapper.findOperatorResourceCount(operatorId, topicId, type)<=0){
			mapper.addOperatorResource(operatorId, topicId, type);	
		}
	}

	@Override
	public Operator findOperator(Long createBy) throws Exception {
		return mapper.findOperator(createBy);
	}

	@Override
	public Long findOperatorId(String operatorId) throws Exception {
		return mapper.findOperatorId(operatorId);
	}
    
	/*@Override
	public Operator findOperatorCommand(long operatorNo) throws Exception { 
		return mapper.findOperatorCommand(operatorNo);
	}
	@Override
	public Operator findResourceCommand(String resourceName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	*/
     
	/** 
	 * 查询用户可用资源
	@Override
	public List<Long> findResourceId(String operatorId,String type) throws Exception {
		System.out.println(operatorId+"-99999999999999999999999999999999999999999999");
		return mapper.findResourceId(operatorId,type);
	}
	 */ 
    
	/** 
	 * 查询有该资源权限的用户
	@Override
	public List<String> findResourceOperator(Long resourceId, String type) throws Exception {
		return mapper.findResourceOperator(resourceId, type);
	}
	 */
    
	/** 
	 * 添加用户资源
	@Override
	public void addOperatorResource(String operatorId, Long topicId, String type) throws Exception {
		 mapper.addOperatorResource(operatorId, topicId, type);		
	}
	 */
    
	/** 
	 * 查询父级
	@Override
	public Operator findOperator(Long createBy) throws Exception {
		return mapper.findOperator(createBy);
	}
	 */
    
	/** 
	 * 根据ID查询
	@Override
	public Long findOperatorId(String operatorId) throws Exception {
		return mapper.findOperatorId(operatorId);
	}
	 */
	
}
