package com.db.bms.common.file;


public class BatchUploadResult {

	private Integer fileTotal = 0;

	private Integer total = 0;

	private Integer successCount = 0;

	private Integer failCount = 0;

	private Integer resultCode;

	private String desc;
	
	private String failDesc;

	public Integer getFileTotal() {
		return fileTotal;
	}

	public void setFileTotal(Integer fileTotal) {
		this.fileTotal = fileTotal;
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
	
	public void fileTotalIncrement(){
		fileTotal++;
	}

	public void totalIncrement() {
		total++;
	}

	public void successCountIncrement() {
		successCount++;
	}

	public void failCountIncrement() {
		failCount++;
	}


	public String getFailDesc() {
		return failDesc;
	}

	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}

	public void failDescIncrement(String desc){	
		if(this.failDesc == null){
			this.failDesc = "<br>"+desc+"<br>";
		}
		else{
			this.failDesc = this.failDesc + desc+"<br>";
		}
	}

	public enum ResultCode {
		SUCCESS(0), FILE_SIZE_EXCEEDED(-1), NO_CONTENT(-2), INVALID_FILE_TYPE(
				-3), INVALID_CONTENT_TITLE(-4), UPLOAD_FAILED(-5), OTHER(
				99);

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
			    return UPLOAD_FAILED;
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
				return "上传失败！";
			default:
				return "其它错误！";
			}
		}

	}

	public String toJson() {
		return com.alibaba.fastjson.JSON.toJSONString(this);
	}
}
