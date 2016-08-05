package com.db.bms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.db.bms.entity.Log;
import com.db.bms.entity.LogConfig;
import com.db.bms.entity.Operator;

public interface LogService {

    List<Log> findPage(Log condition) throws Exception;

    boolean backUp(LogConfig logConfig,Log search) throws Exception;
    
    void logToDB(HttpServletRequest request, String logDetail, long logTypeNo, boolean result, boolean needAppend)
            throws Exception;
    
	public Integer addLogConfig(LogConfig entity) throws Exception;
	
	public Integer updateLogConfig(LogConfig entity) throws Exception;
	
	public LogConfig findLogConfig() throws Exception;
	
	public Log findLogById(Long logNo) throws Exception;
	
    void logToDB(Operator curOper, String logDetail, long logTypeNo, boolean result, boolean needAppend)
    throws Exception; 
    
    

}