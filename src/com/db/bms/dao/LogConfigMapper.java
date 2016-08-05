
package com.db.bms.dao;

import java.util.List;

import com.db.bms.entity.LogConfig;


public interface LogConfigMapper {

	public Integer addLogConfig(LogConfig entity) throws Exception;
	
	public Integer updateLogConfig(LogConfig entity) throws Exception;
	
	public List<LogConfig> findLogConfigs() throws Exception;
}
