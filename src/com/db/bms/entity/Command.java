package com.db.bms.entity;

import java.util.List;


public class Command extends BaseModel implements java.io.Serializable  {

    private Long commandNo;

    private String commandId;

    private String commandName;

    private Long commandPriority;

    private String moduleName;

    private String commandDescribe;

    private Long parentId;

    private Long commandLevel;

    private Integer status;

    private Long displayPosition;

    private Long isMulti;
    
    private List<Command> commands;

    /** default constructor */
    public Command() {
    }

    /** full constructor */
    public Command(String commandId, String commandName, Long commandPriority, String moduleName, String commandDescribe) {
        this.commandId = commandId;
        this.commandName = commandName;
        this.commandPriority = commandPriority;
        this.moduleName = moduleName;
        this.commandDescribe = commandDescribe;
    }

    public Long getCommandNo() {
        return this.commandNo;
    }

    public void setCommandNo(Long commandNo) {
        this.commandNo = commandNo;
    }

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public Long getCommandPriority() {
        return this.commandPriority;
    }

    public void setCommandPriority(Long commandPriority) {
        this.commandPriority = commandPriority;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCommandDescribe() {
        return this.commandDescribe;
    }

    public void setCommandDescribe(String commandDescribe) {
        this.commandDescribe = commandDescribe;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCommandLevel() {
        return commandLevel;
    }

    public void setCommandLevel(Long commandLevel) {
        this.commandLevel = commandLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public Long getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(Long displayPosition) {
        this.displayPosition = displayPosition;
    }

    public Long getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Long isMulti) {
        this.isMulti = isMulti;
    }
    
	public static enum CommandStatus {
		NORMAL(0), SUSPEND(1);

		private final int index;

		private CommandStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static CommandStatus getSource(int index) {
			switch (index) {
			case 0:
				return NORMAL;
			case 1:
				return SUSPEND;
			}
			return NORMAL;
		}
	}

    @Override
    public void setDefaultNull() {
        this.status = null;
    }

    public String toString() {
        return com.alibaba.fastjson.JSON.toJSONString(this);
    }
}