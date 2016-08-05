package com.db.bms.service.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.SysRoleMapper;
import com.db.bms.entity.Command;
import com.db.bms.entity.Role;
import com.db.bms.service.SysRoleService;
import com.db.bms.utils.core.PageUtil;

/** 
 * <b>功能：</b>用于事物处理<br>
 */

@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper mapper;

    public SysRoleMapper getMapper() {
        return mapper;
    }

    @Override
    public List<Role> getRolesByOperatorNo(Long operatorNo) throws SQLException {
        return mapper.findRolesByOperatorNo(operatorNo);

    }

	@Override
	public List<Role> findRoles(Role role) throws Exception {
        PageUtil page = role.getPageUtil();
        int count = this.mapper.findRoleCount(role);
        page.setRowCount(count);
        return this.mapper.findRoles(role);
	}

    @Override
    public void updateRoleCommands(Role role) throws Exception {
        this.mapper.deleteCommandByRole(role.getRoleNo());
        addRoleCommand(role);
    }

    @Override
    public void deleteCommandByRole(Long roleNo) throws SQLException {
        this.mapper.deleteCommandByRole(roleNo);

    }

    @Override
    public Integer addRoleCommand(Role role) throws Exception {
        int result = 0;
        for (Command command : role.getCommands()) {
            result += getMapper().addRoleCommand(command.getCommandNo(), role.getRoleNo());
        }
        return result;
    }

    @Override
    public boolean isRoleRepeateIdOrName(Role role) throws Exception {
        int count = this.mapper.findRoleCountByIdOrName(role);
        return count > 0 ? true : false;
    }

	@Override
	public List<Role> findAllRoles(Role entity) throws Exception {
		return mapper.findAllRoles(entity);
	}

	@Override
	public Role findRoleById(Object key) throws Exception {
		return mapper.findRoleById(key);
	}

	@Override
	public Integer updateByPrimaryKey(Role t) throws Exception {
		return mapper.updateRole(t);
	}

	@Override
	public Integer addRole(Role t) throws Exception {
		return mapper.addRole(t);
	}

	@Override
	public List<Role> findRolesById(Long[] roleNo) throws Exception {
		return mapper.findRolesById(roleNo);
	}

	@Override
	public void deleteRoles(List<Role> roles) throws Exception {
		Iterator<Role> it = roles.iterator();
		while (it.hasNext()){
			Role role = it.next();
			mapper.deleteCommandByRole(role.getRoleNo());
		    mapper.deleteRoleById(role.getRoleNo());
		}

	}

	@Override
	public Integer getRolesByCompanyNo(Long[] companyNos) throws Exception {
		return mapper.getRolesByCompanyNo(companyNos);
	}

	@Override
	public Integer getRolesByInternetNo(Long[] internetNos) throws Exception {
		return mapper.getRolesByInternetNo(internetNos);
	}}