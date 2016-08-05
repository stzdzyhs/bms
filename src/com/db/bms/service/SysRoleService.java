package com.db.bms.service;

import java.sql.SQLException;
import java.util.List;

import com.db.bms.entity.Role;

public interface SysRoleService {

	List<Role> findRoles(Role role) throws Exception;

	void updateRoleCommands(Role role) throws Exception;

	public Integer addRoleCommand(Role role) throws Exception;

	boolean isRoleRepeateIdOrName(Role role) throws Exception;

	List<Role> getRolesByOperatorNo(Long operatorNo) throws SQLException;

	void deleteCommandByRole(Long roleNo) throws SQLException;

	List<Role> findAllRoles(Role entity) throws Exception;

	Role findRoleById(Object key) throws Exception;

	Integer updateByPrimaryKey(Role t) throws Exception;

	Integer getRolesByCompanyNo(Long[] companyNos) throws Exception; 
	Integer getRolesByInternetNo(Long[] internetNos) throws Exception;

	Integer addRole(Role t) throws Exception;

	List<Role> findRolesById(Long[] roleNo) throws Exception;

	void deleteRoles(List<Role> roles) throws Exception;
}