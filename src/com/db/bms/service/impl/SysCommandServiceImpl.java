package com.db.bms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.CommandMapper;
import com.db.bms.entity.Command;
import com.db.bms.entity.Command.CommandStatus;
import com.db.bms.service.SysCommandService;

/** 
 * <b>功能：</b>用于事物处理<br>
 */

@Service("sysCommandService")
public class SysCommandServiceImpl implements SysCommandService {

    @Autowired
    private CommandMapper mapper;

    public CommandMapper getMapper() {
        return mapper;
    }

    @Override
    public List<Command> findCommandList(Long level) throws Exception {
        Command condition = new Command();
        if (level != null) {
            condition.setCommandLevel(level);
        }
        condition.setStatus(CommandStatus.SUSPEND.getIndex());
        return this.mapper.findAllCommands(condition);
    }

    @Override
    public List<Command> getCommandsByRoleNo(long roleNo) throws Exception {
        return this.mapper.getCommandsByRoleNo(roleNo);
    }

    @Override
    public List<Command> getCommandsByCommandNos(String[] commandNos) throws Exception {
        List<Command> result = new ArrayList<Command>(0);
        if (commandNos == null || commandNos.length == 0) {
            return result;
        }
        return this.mapper.findCommandByIds(commandNos);
    }

}