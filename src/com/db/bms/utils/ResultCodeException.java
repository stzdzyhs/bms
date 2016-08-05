package com.db.bms.utils;

import java.io.IOException;

import org.springframework.dao.UncategorizedDataAccessException;

import com.alibaba.fastjson.JSONException;


public class ResultCodeException extends RuntimeException {
	
	public int result;
	public String desc;
	
	public ResultCodeException() {
	}
	
	public ResultCodeException(int code, String desc) {
		this.result = code;
		this.desc = desc;
	}

	public ResultCodeException(int code) {
		this(code, ResultCode.getErrorMsg(code));
	}
	
	public static ResultCodeException convertException(Exception e) {
		if(e instanceof ResultCodeException) {
			return (ResultCodeException)e;
		}
		ResultCodeException rce = new ResultCodeException();
		if ( e instanceof IOException) {
			rce.result = ResultCode.COMMUNICATION_EXCEPTION;
		}
		else if (e instanceof JSONException) {
			rce.result=ResultCode.JSON_PARSE_ERROR;
		}
		else {
			rce.result = ResultCode.OTHER;
		}
		rce.desc = e.getMessage();

		return rce;
	}

	/**
	 * set result's info according to the exception
	 * @param result
	 * @param e - the exception
	 */
	public static <T> void convertException(Result2<T> result, Exception e) {
		if(e instanceof ResultCodeException) {
			ResultCodeException e1 = (ResultCodeException)e;
			result.result = e1.result;
			result.desc= e1.desc;
		}
		else if ( e instanceof IOException) {
			result.result = ResultCode.COMMUNICATION_EXCEPTION;
		}
		else if (e instanceof JSONException) {
			result.result=ResultCode.JSON_PARSE_ERROR;
		}
		else if (e instanceof UncategorizedDataAccessException) {
			result.result=ResultCode.DB_ERROR;
		}
		else {
			result.result = ResultCode.OTHER;
		}

		if(StringUtils.isEmpty(result.desc)) {
			result.desc = ResultCode.getErrorMsg(result.result);
		}
	}

	/**
	 * to string
	 */
	public String toString() {
		return "ResultCodeException:" + result + ", " + desc;
		/*
		String s ;
		try {
			s = JSONObject.toJSONString(this);
		}
		catch(Exception e) {
			s = String.format("{result:%d , desc'%s'}", ResultCode.JSON_PARSE_ERROR, e.getMessage());
		}
		return s;
		*/
	}
	
	
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
