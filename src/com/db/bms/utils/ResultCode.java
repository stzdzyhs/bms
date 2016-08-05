package com.db.bms.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONException;

/**
 * common result code
 */
public class ResultCode {
	public static final int SUCCESS=0;
	public static final int NOT_LOGIN=1;
	public static final int LOGIN_ERROR=2;

	public static final int INVALID_PARAM=10;
	public static final int INVALI_ENCODING=12;
	
	public static final int JSON_STRUCTURE_ERROR=20;
	public static final int JSON_PARSE_ERROR=30;
	public static final int COMMUNICATION_EXCEPTION=40;
	public static final int NOT_FOUND_SYSTEM=50;
	public static final int NO_ACCESS_RIGHTS=60;
	public static final int NOT_FOUND_ASSET_ID=70;
	public static final int DB_ERROR=80;
	public static final int KAPI_ERROR =90;
	
	public static final int FULL_ERROR=100;
	public static final int DUP_ERROR= 110; // duplicated error
	
	public static final int IVOD_ERROR =120;
	public static final int DEFAULT_STB_ERROR=130;
	public static final int REF_ERROR =140; // referenced error

	public static final int WORD_ERROR =150; //生僻字错误

	public static int OTHER=10000;
	
	public static Map<Integer,String> errorMsg = new HashMap<Integer, String>();
	static {
		errorMsg.put(SUCCESS, "");
		errorMsg.put(INVALID_PARAM, "非法参数");
		errorMsg.put(INVALI_ENCODING, "无效编码");
		errorMsg.put(NOT_LOGIN, "没有登陆");
		errorMsg.put(LOGIN_ERROR, "登陆错误");
		errorMsg.put(JSON_STRUCTURE_ERROR, "json错误");
		errorMsg.put(JSON_PARSE_ERROR,"json格式错误");
		errorMsg.put(COMMUNICATION_EXCEPTION,"网络错误");
		errorMsg.put(NOT_FOUND_SYSTEM,"系统错误");
		errorMsg.put(NO_ACCESS_RIGHTS,"权限错误");
		errorMsg.put(NOT_FOUND_ASSET_ID,"资源id错误");
		errorMsg.put(DB_ERROR, "数据错误");
		errorMsg.put(KAPI_ERROR, "后台接口错误");
		errorMsg.put(FULL_ERROR, "溢出错误");
		errorMsg.put(DUP_ERROR, "重复错误");
		errorMsg.put(IVOD_ERROR, "ivod错误");
		errorMsg.put(DEFAULT_STB_ERROR,"缺省机顶盒错误");
		errorMsg.put(REF_ERROR,"引用错误");
		errorMsg.put(WORD_ERROR, "生僻字错误");

		errorMsg.put(OTHER,"其它错误");
	}
	
	public static String getErrorMsg(int code) {
		String s = errorMsg.get(code);
		if(s==null) {
			s = "未知错误";
		}
		return s;
	}
	
	public static void throwResultCodeException(int err) throws ResultCodeException{
		throw new ResultCodeException(err, ResultCode.getErrorMsg(err) );
	}
	
	/**
	 * convert exception to result code
	 * @param e
	 * @return
	 */
	public static <T> Result2<T> convertException(Exception e) {
		Result2<T> ret = new Result2<T>();
		if(e instanceof ResultCodeException) {
			ret.result = ((ResultCodeException)e).result;
			ret.desc = ((ResultCodeException)e).desc;
		}
		else {
			if ( e instanceof IOException) {
				ret.result = ResultCode.COMMUNICATION_EXCEPTION;
				ret.desc = "网络错误";
			}
			else if (e instanceof JSONException) {
				ret.result=ResultCode.JSON_PARSE_ERROR;
				ret.desc = "数据错误";
			}
			else {
				ret.result = ResultCode.OTHER;
				ret.desc = "数据错误";
			}
			//ret.desc = e.getMessage();
		}
		return ret;
	}
	
}
