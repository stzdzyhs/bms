package com.db.bms.controller.sysmgmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.bms.cache.SysSchedulerEngine;
import com.db.bms.entity.Log;
import com.db.bms.entity.LogConfig;
import com.db.bms.entity.Operator;
import com.db.bms.entity.LogConfig.LogRunType;
import com.db.bms.job.LogBackupJob;
import com.db.bms.service.LogService;
import com.db.bms.service.OperatorService;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.DateUtil;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.FileUtils;
import com.db.bms.utils.LogUtil;
import com.db.bms.utils.spring.SessionUtil;

@RequestMapping("sysmgmt/log")
@Controller
public class LogController {

	private final static Logger logger = Logger.getLogger(LogController.class);
	
    @Autowired
    private OperatorService operatorService;
    
    @Autowired
    private LogService logService;
    
    @Autowired
    private SysSchedulerEngine sysSchedulerEngine;
    
    /**
     * 获得日志列表
     */
    @RequestMapping(value = "/logList.action")
    public String logList(HttpServletRequest request, ModelMap modelMap, Log search) throws Exception {
    	Operator curOper = SessionUtil.getActiveOperator(request);

        String rootPath = request.getSession(true).getServletContext().getRealPath("/");
        String relatePath = rootPath + "logbackup/";
        File file = new File(relatePath);
        if (!file.exists()) {
            file.mkdir();
        }
        Map<String, String> logFilesList = FileUtils.findFileMap(relatePath);
        modelMap.put("logFilesList", logFilesList);

        List<Log> list = this.logService.findPage(search);
        modelMap.put("list", list);
        modelMap.put("pageUtil", search.getPageUtil());//分页页面时要写这个
        modelMap.put("search", search);
        modelMap.put("operatorList", this.operatorService.findAllOperators(curOper));
        return "/sysmgmt/log/logList";
    }

    @RequestMapping(value = "/backUp.action", method = RequestMethod.POST)
    @ResponseBody
    public String backUp(HttpServletRequest request,Log search) throws Exception {
        boolean result = false;
        try {
            result = this.logService.backUp(null, search);
            logService.logToDB(request, "手动备份日志", LogUtil.LOG_INFO, result, true);
        }
        catch (Exception e) {
        	logger.error("Manual backup log exception occurred, cause by:{}", e);
            result = false;
            logService.logToDB(request, "手动备份日志", LogUtil.LOG_INFO, result, true);
        }
        return "{result: '" + result + "', desc : ''}";

    }
    
    @RequestMapping(value = "/download.action")
    public String download(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
    	response.setContentType("text/html;charset=utf-8");
    	request.setCharacterEncoding("UTF-8");
    	BufferedInputStream bis = null;
    	BufferedOutputStream bos = null;
    	String ctxPath = request.getSession().getServletContext().getRealPath("/") + File.separator + "logbackup" + File.separator;
    	String downloadPath = ctxPath + fileName; 
    	
    	try{
        	long length = new File(downloadPath).length();
            response.setContentType("application/x-msdownload;");  
            response.setHeader("Content-disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"");  
            response.setHeader("Content-Length", String.valueOf(length));  
            bis = new BufferedInputStream(new FileInputStream(downloadPath));  
            bos = new BufferedOutputStream(response.getOutputStream());  
            byte[] buff = new byte[2048];  
            int len;  
            while (-1 != (len = bis.read(buff, 0, buff.length))) {  
                bos.write(buff, 0, len);  
            }  
            logService.logToDB(request, "下载历史日志文件【" + fileName + "】", LogUtil.LOG_INFO, true, true);
    	}catch (Exception e){
    		 logService.logToDB(request, "下载历史日志文件【" + fileName + "】", LogUtil.LOG_INFO, false, true);
    		 logger.error("Download history Log file exception occurred, cause by:{}", e);
    	}finally{
    		if (bis != null){
                bis.close();  
    		}

            if (bos != null){
                bos.close(); 
            } 
    	}
        return null;
    }
    
    @RequestMapping(value = "/logConfigDetail.action", method = RequestMethod.GET)
    public String logConfigDetail(HttpServletRequest request, ModelMap modelMap) throws Exception {
    	LogConfig logConfig = logService.findLogConfig();
    	modelMap.put("logConfig", logConfig);
    	modelMap.put("logBackupTypeMap", ConstConfig.logBackupTypeMap);
    	modelMap.put("logRunTypeMap", ConstConfig.logRunTypeMap);
    	return "/sysmgmt/log/logConfig";
    }
    
    @RequestMapping(value = "/saveOrUpdateConfig.action", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrUpdateConfig(HttpServletRequest request, LogConfig logConfig) throws Exception{
        boolean result = false;
        String logStr = "";
        try {

            if (logConfig != null) {
				String cronExpression = "0 15 4 L * ?"; // 每月最后一天的4点15分触发
				if (logConfig.getLogRunTimestr() != null && logConfig.getLogRunTimestr().length() > 0) {
					logConfig.setLogRunTime(DateUtil.getHMS(logConfig.getLogRunTimestr()));
				} else {
					logConfig.setLogRunTime(0l);
				}
				logConfig.setLogCreateTime(new Date().getTime());
				
				String[] times = logConfig.getLogRunTimestr().split(Delimiters.COLON);
				cronExpression = times[2] + " " + times[1] + " " + times[0];
				switch (LogRunType.getType(logConfig.getLogRunType())) {
				case EVERY_DAY:
					cronExpression += " * * ?";
					break;
				case EVERY_WEEK:
					cronExpression += " ? * SUN";// 每周的星期天
					break;
				case EVERY_MONTH:
					cronExpression += " L * ?";// 每月的最后一天
					break;
				default:
					cronExpression = "0 15 4 L * ?"; // 每月最后一天的4点15分触发
					break;
				}
				logConfig.setCronExpression(cronExpression);
				
				if (logConfig.getLogConfigNo() != null && logConfig.getLogConfigNo() > 0) {
					logStr = "更新日志配置";
					logService.updateLogConfig(logConfig);
					// 更改时间配置
					sysSchedulerEngine.removeJob(sysSchedulerEngine.getJobName());
				} else {
					logStr = "添加日志配置";
					logService.addLogConfig(logConfig);
				}
				
				// 添加日志备份调度
				LogBackupJob job = new LogBackupJob();
				sysSchedulerEngine.addJob(sysSchedulerEngine.getJobName(), job, logConfig.getCronExpression());
				result = true;
			    logService.logToDB(request, logStr, LogUtil.LOG_INFO, result, true);
            }
        }
        catch (Exception e) {
        	 logService.logToDB(request, logStr, LogUtil.LOG_INFO, result, true);
        	 if (logConfig.getLogConfigNo() != null && logConfig.getLogConfigNo() > 0) {
            	 logger.error("Update the log configuration exception occurred, cause by:{}", e);
        	 }else{
            	 logger.error("Add the log configuration exception occurred, cause by:{}", e);
        	 }

        }
        return "{result: '" + result + "', desc : ''}";
    }
    
    @RequestMapping(value = "/detail.action")
    public String detail(HttpServletRequest request, ModelMap modelMap, Long logNo) throws Exception {
    	Log log = logService.findLogById(logNo);
    	modelMap.put("log", log);
    	return "/sysmgmt/log/logDetail";
    } 

}
