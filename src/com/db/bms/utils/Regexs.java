
package com.db.bms.utils;


public interface Regexs {

	/** 日期时间 */
	public static final String DATE_TIME  = "^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$";
	
	public static final String DATE_TIME1  = "^(\\d{4})-(0[1-9]{1}|1[0-2])-(0[1-9]{1}|1[0-9]{1}|2[0-9]{1}|3[0-1]{1})\\s([0-1]{1}[0-9]{1}|2[0-3]{1}):([0-5]{1}[0-9]{1}):([0-5]{1}[0-9]{1})$";
	
	public static final String ASSET_ID = "^[A-Za-z]{4}[0-9]{16}$";
	
	/** 频道ID */
	public static final String CHANNEL_ID = "^[1-9]{1}[0-9]{0,3}$";
	
	public static final String DISPLAY_RUN_TIME = "^([0-1]{1}[0-9]{1}|2[0-3]{1}):([0-5]{1}[0-9]{1})";
	
	public static final String RUN_TIME = "^([0-1]{1}[0-9]{1}|2[0-3]{1}):([0-5]{1}[0-9]{1}):([0-5]{1}[0-9]{1})$";
	
	public static final String YEAR = "^\\d{4}$";
	
	public static final String DATE  = "^(\\d{4})-(0[1-9]{1}|1[0-2])-(0[1-9]{1}|1[0-9]{1}|2[0-9]{1}|3[0-1]{1})$";
	
	public static final String EPISODE_ID = "^[1-9]{1}[0-9]{0,3}$";
	
	/** URL */
	public static final String URL = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	
	public static final String VIDEO_TIME = "^\\d{1,2}:\\d{2}:\\d{2}$";
	
	public static final String ASSET_TITLE = "^[0-9A-Za-z_\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
	
	public static final String FRAME_NUM = "^[1-9]{1}[0-9]{0,7}$";
}
