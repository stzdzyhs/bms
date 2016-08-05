
package com.db.bms.entity;


public class LogConfig extends BaseModel {

	private Long logConfigNo;

	private Integer logBackupType;

	private Integer logRunType;

	private Long logRunTime;

	private String logRunTimestr;

	private Long logCreateTime;

	private String cronExpression;

	public Long getLogConfigNo() {
		return logConfigNo;
	}

	public void setLogConfigNo(Long logConfigNo) {
		this.logConfigNo = logConfigNo;
	}

	public Integer getLogBackupType() {
		return logBackupType;
	}

	public void setLogBackupType(Integer logBackupType) {
		this.logBackupType = logBackupType;
	}

	public Integer getLogRunType() {
		return logRunType;
	}

	public void setLogRunType(Integer logRunType) {
		this.logRunType = logRunType;
	}

	public Long getLogRunTime() {
		return logRunTime;
	}

	public void setLogRunTime(Long logRunTime) {
		this.logRunTime = logRunTime;
	}

	public String getLogRunTimestr() {
		return logRunTimestr;
	}

	public void setLogRunTimestr(String logRunTimestr) {
		this.logRunTimestr = logRunTimestr;
	}

	public Long getLogCreateTime() {
		return logCreateTime;
	}

	public void setLogCreateTime(Long logCreateTime) {
		this.logCreateTime = logCreateTime;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public static enum LogBackupType {
		A_MONTH_AGO(1), THREE_MONTH_AGO(2), HALF_YEAR_AGO(3), A_YEAR_AGO(4), TWO_YEAR_AGO(
				5);

		private final int index;

		private LogBackupType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static LogBackupType getType(int index) {
			switch (index) {
			case 1:
				return A_MONTH_AGO;
			case 2:
				return THREE_MONTH_AGO;
			case 3:
				return HALF_YEAR_AGO;
			case 4:
				return A_YEAR_AGO;
			case 5:
				return TWO_YEAR_AGO;
			}
			return A_MONTH_AGO;
		}
	}

	public static enum LogRunType {
		EVERY_DAY(1), EVERY_WEEK(2), EVERY_MONTH(3);

		private final int index;

		private LogRunType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static LogRunType getType(int index) {
			switch (index) {
			case 1:
				return EVERY_DAY;
			case 2:
				return EVERY_WEEK;
			case 3:
				return EVERY_MONTH;
			}
			return EVERY_DAY;
		}
	}

	@Override
	public void setDefaultNull() {

	}

}
