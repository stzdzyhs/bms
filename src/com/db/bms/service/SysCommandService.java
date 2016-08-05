package com.db.bms.service;

import java.util.List;

import com.db.bms.dao.CommandMapper;
import com.db.bms.entity.Command;

public interface SysCommandService {

    List<Command> findCommandList(Long level) throws Exception;

    List<Command> getCommandsByCommandNos(String[] commandNos) throws Exception;
    
    List<Command> getCommandsByRoleNo(long roleNo) throws Exception;

}