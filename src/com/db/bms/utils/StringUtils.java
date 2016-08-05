
package com.db.bms.utils;

import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;


public class StringUtils {

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String formatRegionCode(String regionCode) {
		if (isEmpty(regionCode)) {
			return null;
		}

		if (regionCode.length() >= 4) {
			return regionCode;
		}

		int len = regionCode.length();
		for (int i = 0; i < 4 - len; i++) {
			regionCode = "0" + regionCode;
		}
		return regionCode;
	}

	public static String generateFixPrefixId(String prefix) {
		String assetId = prefix + System.currentTimeMillis();
		StringBuffer randomNum = new StringBuffer(5);
		Random rand = new Random();
		if (assetId.length() < 20) {
			for (int i = 0; i < 20 - assetId.length(); i++) {
				randomNum.append(rand.nextInt(10));
			}
			assetId = assetId + randomNum.toString();
		} else if (assetId.length() > 20) {
			assetId = assetId.substring(0, 20);
		}
		return assetId;
	}
	
	public static String generateAssetId() {
		String assetId = "GT" + System.currentTimeMillis();
		StringBuffer randomNum = new StringBuffer(5);
		Random rand = new Random();
		if (assetId.length() < 20) {
			for (int i = 0; i < 20 - assetId.length(); i++) {
				randomNum.append(rand.nextInt(10));
			}
			assetId = assetId + randomNum.toString();
		} else if (assetId.length() > 20) {
			assetId = assetId.substring(0, 20);
		}
		return assetId;
	}

	/**
	 * remove special chars in the input.(to prevent sql injection)
	 * 
	 * @param sql
	 * @return
	 */
	public static String removeSpecialChars(String sql) {
		if (sql == null) {
			return "";
		}
		sql = sql.replaceAll("[#/,;'$\"\\\\]", "");
		return sql;
	}

	/**
	 * upper first char of name. name.length must > 0
	 * 
	 * @param name
	 * @return
	 */
	public static String upperFirst(String name) {
		String s = name.substring(0, 1);
		name = name.substring(1);
		s = s.toUpperCase();
		s = s + name;
		return s;
	}

	/**
	 * concat object property as string in list.
	 * 
	 * @param list
	 *            - the list
	 * @param cls
	 *            - the element class
	 * @param propertyName
	 *            - the property name
	 * @return - a string, if too long, append ...
	 */
	public static <T> String concatListProperty(List<T> list, Class<T> cls,
			String propertyName) {
		try {
			int s = list.size();
			if (s < 1) {
				return "";
			}

			String mn = "get" + upperFirst(propertyName);

			Method m = cls.getMethod(mn);
			StringBuilder sb = new StringBuilder();

			Object o = m.invoke(list.get(0));
			if (o != null) {
				sb.append(o.toString());
			}

			for (int i = 1; i < s; i++) {
				o = m.invoke(list.get(i));
				if (o != null) {
					if (sb.length() < 100) {
						sb.append(",");
						sb.append(o.toString());
					} else {
						sb.append(",...");
						break;
					}
				}
			}
			return sb.toString();
		} catch (Exception e) {
			return "???";
		}
	}
	
	private static final int GB = 1024 * 1024 * 1024;// 定义GB的计算常量
	private static final int MB = 1024 * 1024;// 定义MB的计算常量
	private static final int KB = 1024;// 定义KB的计算常量

	public static String byteConversionGBMBKB(long kSize) {
		if (kSize / GB >= 1){
			double value = (double)kSize / GB;
			DecimalFormat df = new DecimalFormat("#.##");  
			df.setRoundingMode(RoundingMode.FLOOR);
/*			BigDecimal bd =   new   BigDecimal(value);   
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);  */ 
			return df.format(value) + "G";
		}
		else if (kSize / MB >= 1){
		    double value = (double)kSize / MB;
		    DecimalFormat df = new DecimalFormat("#.##");   
		    df.setRoundingMode(RoundingMode.FLOOR);
/*			BigDecimal bd =   new   BigDecimal(value);   
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);   */
			return df.format(value) + "M";
		}else if (kSize / KB >= 1){
		    double value = (double)kSize / KB;
		    DecimalFormat df = new DecimalFormat("#.##"); 
		    df.setRoundingMode(RoundingMode.FLOOR);
/*			BigDecimal bd =   new   BigDecimal(value);   
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP); */  
			return df.format(value) + "KB";
		}
		
		return kSize + "B";	
	}

}
