package com.db.bms.common.file;


public class UploadResult {

	private boolean result=false;
	
	private String filePath;
	
	private Integer total = 0;

	private Integer successCount = 0;

	private Integer failCount = 0;

	private Integer resultCode;

	private String desc;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void totalIncrement(){
		total ++;
	}
	
	public void successCountIncrement(){
		successCount ++;
	}
	
	public void failCountIncrement(){
		failCount ++;
	}

	public enum ResultCode {
		SUCCESS(0), FILE_SIZE_EXCEEDED(-1),NO_CONTENT(-2),INVALID_FILE_TYPE(
				-3),  INVALID_CONTENT_TITLE(-4),  INVALID_TERMINA_TYPE(-5), OTHER(99);

		private int index;

		private ResultCode(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
		
		public static ResultCode getCode(int index) {
			switch (index) {
			case 0:
				return SUCCESS;
			case -1:
				return FILE_SIZE_EXCEEDED;
			case -2:
				return NO_CONTENT;
			case -3:
				return INVALID_FILE_TYPE;
			case -4:
				return INVALID_CONTENT_TITLE;
			case -5:
				return INVALID_TERMINA_TYPE;
			}
			return OTHER;
		}

		public String getStateDesc() {
			switch (index) {
			case 0:
				return "成功。";
			case -1:
				return "文件大小超过限制！";
			case -2:
				return "文件内容为空！";
			case -3:
				return "文件类型不正确！";
			case -4:
				return "文件内容标题不正确！";
			case -5:
				return "系统未能找到任何终端类型，请添加终端类型！";
			default:
				return "其它错误！";
			}
		}

	}
	
	public String toJson() {
		return com.alibaba.fastjson.JSON.toJSONString(this);
	}
}
