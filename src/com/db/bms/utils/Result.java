package com.db.bms.utils;

/**
 * operation result
 */
public class Result {

	public boolean result = false;

	public Object data=null; // the data

	public String desc = null;

	
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public boolean isResult() {
		return result;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
