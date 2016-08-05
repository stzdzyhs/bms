package com.db.bms.dao;

import java.util.List;

import com.db.bms.entity.Log;
import com.db.bms.entity.Operator;

public interface LogMapper {

	public Integer findLogCount(Log model) throws Exception;

	public List<Log> findLogs(Log model) throws Exception;

	public Integer deleteByEntity(Log entity) throws Exception;

	public Integer insert(Log t) throws Exception;
	
	public Log findLogById(Long logNo) throws Exception; 
	
}