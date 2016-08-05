package com.db.bms.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.bms.entity.Role;


public interface SysRoleMapper {

    public List<Role> findRolesByOperatorNo(Long operatorNo) throws SQLException;

    public void deleteCommandByRole(Long roleNo) throws SQLException;
    
    public Integer findRoleCount(@Param(value = "role")Role role) throws Exception;
    
    public List<Role> findRoles(@Param(value = "role")Role role) throws Exception;
    
    public Integer addRoleCommand(@Param(value = "otherKey")
    	    Object otherTableKey, @Param(value = "mainKey")
    	    Object primaryKey) throws SQLException;
    
    public List<Role> findAllRoles(Role entity) throws Exception;
    
    public Role findRoleById(Object key) throws Exception;
    
    public Integer updateRole(Role t) throws Exception;
    
    public Integer addRole(Role t) throws Exception;
    
    public List<Role> findRolesById(Long[] roleNo) throws Exception;
    
    public Integer deleteRoleById(Long roleNo) throws Exception;
    
    public Integer findRoleCountByIdOrName(Role entity) throws Exception;
    
    Integer getRolesByCompanyNo(Long[] companyNos) throws Exception; 
    
    Integer getRolesByInternetNo(Long[] internetNos) throws Exception;
}