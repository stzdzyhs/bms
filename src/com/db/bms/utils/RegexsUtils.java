
package com.db.bms.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexsUtils {

	public static boolean checkData(String data, String regexs){
		Pattern p = Pattern.compile(regexs);
		Matcher m = p.matcher(data);
		if (!m.matches()) {
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args){
		//boolean flag = checkData("02:08:02","^([0-1]{1}[0-9]{1}|2[0-3]{1}):([0-5]{1}[0-9]{1}):([0-5]{1}[0-9]{1})$");
		boolean flag = checkData("name.jpg","^*.jpg$");
		System.out.println(flag);
	}
}
