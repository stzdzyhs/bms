
package com.db.bms.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import com.db.bms.entity.LogConfig;
import com.db.bms.service.LogService;


public class LogBackupJob implements StatefulJob {

	private final static Logger logger = Logger.getLogger(LogBackupJob.class);

	private LogService logService;

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.debug("Begin to run Automatic backup the history log task...");
			logService = (LogService) context.getJobDetail().getJobDataMap().get("logService");
			LogConfig logConfig = logService.findLogConfig();
			if (logConfig != null) {
				logService.backUp(logConfig, null);
			}
			logger.debug("History log backup task to run over.");
		} catch (Exception e) {
			logger.error(
					"Automatic backup history log task exception occurred, cause by:{}",
					e);
		}
	}

}
