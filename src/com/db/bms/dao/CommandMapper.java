package com.db.bms.dao;

import java.util.List;

import com.db.bms.entity.Command;

public interface CommandMapper {
	
	public List<Command> getCommandsByRoleNo(long roleNo) throws Exception;

	public List<Command> findAllCommands(Command model) throws Exception;

	public List<Command> findCommandByIds(Object[] key) throws Exception;
}
