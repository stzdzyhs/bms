package com.db.bms.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.dao.LogConfigMapper;
import com.db.bms.dao.LogMapper;
import com.db.bms.entity.Log;
import com.db.bms.entity.LogConfig;
import com.db.bms.entity.Operator;
import com.db.bms.entity.LogConfig.LogBackupType;
import com.db.bms.service.LogService;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Environment;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.core.PageUtil;
import com.db.bms.utils.spring.SessionUtil;

/** 
 * <b>功能：</b>用于事物处理<br>
 */

@Service("logService")
public class LogServiceImpl implements LogService {

	private final static Logger logger = Logger.getLogger(LogServiceImpl.class);
	
    @Autowired
    private LogMapper mapper;
    
    @Autowired
    private LogConfigMapper logConfigMapper;

    public LogMapper getMapper() {
        return mapper;
    }

    @Override
    public List<Log> findPage(Log search) throws Exception {
        PageUtil page = search.getPageUtil();
        page.setPaging(true);
        Integer rowCount = this.mapper.findLogCount(search);
        page.setRowCount(rowCount);
        return this.mapper.findLogs(search);
    }

    public boolean backUp(LogConfig logConfig, Log search) throws Exception {
        boolean result = false;
        Log timeSearch = new Log();
        PageUtil page = timeSearch.getPageUtil();
        page.setPaging(false);
        if (search != null){
            timeSearch.setStartDate(search.getStartDate());
            timeSearch.setEndDate(search.getEndDate());
        }
        
		if (logConfig != null && search == null) {
			switch (LogBackupType.getType(logConfig.getLogBackupType())) {
			case A_MONTH_AGO:
				timeSearch.setEndDate(DateUtil.getDateStr(DateUtil.getDate(-1)));
				break;
			case THREE_MONTH_AGO:
				timeSearch.setEndDate(DateUtil.getDateStr(DateUtil.getDate(-3)));
				break;
			case HALF_YEAR_AGO:
				timeSearch.setEndDate(DateUtil.getDateStr(DateUtil.getDate(-6)));
				break;
			case A_YEAR_AGO:
				timeSearch.setEndDate(DateUtil.getDateStr(DateUtil.getDate(-12)));
				break;
			case TWO_YEAR_AGO:
				timeSearch.setEndDate(DateUtil.getDateStr(DateUtil.getDate(-24)));
				break;
			default:
				timeSearch.setEndDate(DateUtil.getDateStr(DateUtil.getDate(-1)));
				break;
			}
		}

        List<Log> rslist = this.mapper.findLogs(timeSearch);
        if (rslist != null && rslist.size() > 0) {
        	
            String path = Environment.FILE_SYSTEM_DIRECTORY + "logbackup";
            String fileName = DateUtil.format(DateUtil.format(rslist.get(rslist.size()-1).getLogTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH.mm.ss") + "_" 
            + DateUtil.format(DateUtil.format(rslist.get(0).getLogTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH.mm.ss") + ".txt";
            StringBuffer content = new StringBuffer();
            content.append("用户账号,日志类型,日志描述,日志时间" + System.getProperty("line.separator"));
            int count = 0;
            for (Log log : rslist) {
                content.append(log.getOperator() == null ? log.getOperatorNo() : log.getOperator().getOperatorName() + "," + log.getLogType().getLogTypeName() + ","
                        + log.getLogDescribe() + "," + log.getLogTime()
                        + System.getProperty("line.separator"));
                if (count != 0 && count % 1000 == 0) {
                    FileUtils.newFile(path, fileName, content, true);
                    content = new StringBuffer();
                }
                count++;
            }
            FileUtils.newFile(path, fileName, content, true);
            // 删除相应记录
            this.mapper.deleteByEntity(timeSearch);
        }
        
        logger.info("History log backup success,[startDate=" + timeSearch.getStartDate() + ",endDate=" + timeSearch.getEndDate() + "].");
        result = true;
        return result;
    }

    @Override
    public void logToDB(HttpServletRequest request, String logDetail, long logTypeNo, boolean result, boolean needAppend)
            throws Exception {
        Log log = new Log();
        log.setLogTime(DateUtil.getCurrentTime());
        Operator operator = SessionUtil.getActiveOperator(request);
        if (operator != null) {
            log.setOperatorNo(operator.getOperatorNo());
            log.setCompanyNo(operator.getCompanyNo());
        }
        else {
            log.setOperatorNo(-1l);
            log.setCompanyNo(-1l);
        }
        log.setLogTypeNo(result ? logTypeNo : LogUtil.LOG_ERROR);
        if (needAppend) {
            logDetail += result ? "成功" : "失败";
        }
        log.setLogDescribe(logDetail);
        this.mapper.insert(log);
    }

	@Override
	public Integer addLogConfig(LogConfig entity) throws Exception {
		return logConfigMapper.addLogConfig(entity);
	}

	@Override
	public Integer updateLogConfig(LogConfig entity) throws Exception {
		return logConfigMapper.updateLogConfig(entity);
	}

	@Override
	public LogConfig findLogConfig() throws Exception {
		LogConfig logConfig = null;
		List<LogConfig> list = logConfigMapper.findLogConfigs();
		if (list != null && list.size() > 0){
			logConfig = list.get(0);
		}
		return logConfig;
	}

	@Override
	public Log findLogById(Long logNo) throws Exception {
		return mapper.findLogById(logNo);
	}

	@Override
	public void logToDB(Operator curOper, String logDetail, long logTypeNo,
			boolean result, boolean needAppend) throws Exception {
        Log log = new Log();
        log.setLogTime(DateUtil.getCurrentTime());
        if (curOper != null) {
            log.setOperatorNo(curOper.getOperatorNo());
            log.setCompanyNo(curOper.getCompanyNo());
        }
        else {
            log.setOperatorNo(-1l);
            log.setCompanyNo(-1l);
        }
        log.setLogTypeNo(result ? logTypeNo : LogUtil.LOG_ERROR);
        if (needAppend) {
            logDetail += result ? "成功" : "失败";
        }
        log.setLogDescribe(logDetail);
        this.mapper.insert(log);
		
	} 
	
}