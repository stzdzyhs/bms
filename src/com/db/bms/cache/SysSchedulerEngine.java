package com.db.bms.cache;

import java.text.ParseException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.bms.entity.LogConfig;
import com.db.bms.job.LogBackupJob;
import com.db.bms.service.LogService;

@Service("sysSchedulerEngine")
public class SysSchedulerEngine {
	
	private final static Logger logger = Logger.getLogger(SysSchedulerEngine.class);

	public static String jobGroupName = "jobgroup1";

	public static String triggerGroupName = "trigger1";

	public static String jobName = "job1";
	
	public static String SYS_PARAM_TRIGGER_GROUP_NAME = "sysParamTriggerGroup";
	
	public static String SYS_PARAM_JOB_GROUP_NAME = "sysParamJobGroup";
	
	public static String SYS_PARAM_TRIGGER_NAME = "sysParamTrigger";
	
	public static String SYS_PARAM_JOB_NAME = "sysParamJob";
	
	private SchedulerFactory sf = new StdSchedulerFactory();
	
	@Autowired
    private LogService logService;

	public void setJobGroupName(String jobGroupName) {
		this.jobGroupName = jobGroupName;
	}

	public void setTriggerGroupName(String triggerGroupName) {
		this.triggerGroupName = triggerGroupName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroupName() {
		return jobGroupName;
	}

	public String getTriggerGroupName() {
		return triggerGroupName;
	}

	public String getJobName() {
		return jobName;
	}

	@PostConstruct
	public void init() throws Exception{
		LogConfig logConfig = logService.findLogConfig();
		if (logConfig != null){
			removeJob(getJobName());
			LogBackupJob job = new LogBackupJob();
			addJob(getJobName(), job, logConfig.getCronExpression());
		}
	}

	@PreDestroy
	public void destroy() {
		try {
			Collection<?> ss = sf.getAllSchedulers();
			for(Object s:ss) {
				Scheduler s1 = (Scheduler)s;
				s1.shutdown();
			}
		}
		catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void addJob(String jobName, Job job, String time)
			throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = new JobDetail(jobName, jobGroupName, job.getClass());
		jobDetail.getJobDataMap().put("logService", logService);
		CronTrigger trigger = new CronTrigger(jobName, triggerGroupName);
		trigger.setCronExpression(time);
		sched.scheduleJob(jobDetail, trigger);
		if (!sched.isShutdown()) {
			sched.start();
		}
	}

	public void addJob(String jobName, String jobGroupName, String triggerName,
			String triggerGroupName, Job job, String time)
			throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = new JobDetail(jobName, jobGroupName, job.getClass());
		jobDetail.getJobDataMap().put("logService", logService);
		CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);
		trigger.setCronExpression(time);
		sched.scheduleJob(jobDetail, trigger);
		if (!sched.isShutdown()) {
			sched.start();
		}
	}

	public void modifyJobTime(String jobName, String time) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		Trigger trigger = sched.getTrigger(jobName, triggerGroupName);
		if (trigger != null) {
			CronTrigger ct = (CronTrigger) trigger;
			ct.setCronExpression(time);
			sched.resumeTrigger(jobName, triggerGroupName);
		}
	}

	public void modifyJobTime(String triggerName, String triggerGroupName,
			String time) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		Trigger trigger = sched.getTrigger(triggerName, triggerGroupName);
		if (trigger != null) {
			CronTrigger ct = (CronTrigger) trigger;
			// 修改时间
			ct.setCronExpression(time);
			// 重启触发器
			sched.resumeTrigger(triggerName, triggerGroupName);
		}
	}

	public void removeJob(String jobName) throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		sched.pauseTrigger(jobName, triggerGroupName);// 停止触发器
		sched.unscheduleJob(jobName, triggerGroupName);// 移除触发器
		sched.deleteJob(jobName, jobGroupName);// 删除任务
	}
	
	public void removeJob(String triggerGroupName, String jobGroupName, String jobName) throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		sched.pauseTrigger(jobName, triggerGroupName);// 停止触发器
		sched.unscheduleJob(jobName, triggerGroupName);// 移除触发器
		sched.deleteJob(jobName, jobGroupName);// 删除任务
	}

}
