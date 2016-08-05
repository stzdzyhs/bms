package com.db.bms.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;

public class PjAuthorizeUtil {
	private static final Logger log = Logger.getLogger(PjAuthorizeUtil.class);

	public static final String authFileName = "authorize.dat";
	public static String limitDateStr = "";
	public static Long limitDateTime = 0l;
	public static Integer companyNum = 0;

	public static final String overdueTip = "系统授权已过期，无法执行该操作！";
	public static final String companyTip = "运营商数量不能超过授权数量";

	static {
		initAuthInfo();
	}

	public static void initAuthInfo() {
		String classPath = PjAuthorizeUtil.class.getResource("/").getPath()
				.replaceAll("%20", " ");
		try {
			String authorizeInfo = FileUtils.readFileContent(classPath
					+ authFileName);
			if (authorizeInfo != null && !authorizeInfo.isEmpty()) {
				EncryptData en = new EncryptData();
				authorizeInfo = en.decrypt(authorizeInfo);
				if (authorizeInfo.startsWith("db")) {
					String[] array = authorizeInfo.split(";");
					if (array != null && array.length == 4) {
						long curTime = (new Date()).getTime();
						if (Long.parseLong(array[3]) > (curTime + 24*3600000)) {
							log.error("系统日期有误，授权无效");
						} else {
							String[] fmacArray = array[0].substring(5)
									.split("&");
							String[] macArray = HardwareUtil.getMac()
									.split("&");
							boolean correct = true, find = false;
							for (String mac : macArray) {
								if (mac != null && !mac.isEmpty()) {
									find = false;
									for (String fmac : fmacArray) {
										if (mac.equals(fmac)) {
											find = true;
											break;
										}
									}
									if(!find){
										correct = false;
										break;
									}
								}
							}
							if (correct) {
								try {
									limitDateTime = Long.parseLong(array[1]);
									companyNum = Integer.parseInt(array[2]);
									limitDateStr = DateUtil.format(new Date(
											limitDateTime), "yyyy-MM-dd");
									// 更新系统时间
									authorizeInfo = array[0] + ";"
											+ limitDateTime + ";" + companyNum
											+ ";" + curTime;
									String encryptInfo = en
											.encrypt(authorizeInfo);
									FileUtils.newFile(classPath, authFileName,
											encryptInfo);
								} catch (Exception e) {
									limitDateStr = "";
									limitDateTime = 0l;
									companyNum = 10;
								}
							} else {
								log.error("授权服务器信息不一致");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 授权是否过期
	 * 
	 * @return
	 */
	public static boolean isOverdue() {
		boolean overdue = true;
		long curTime = (new Date()).getTime();
		if (curTime < limitDateTime) {
			overdue = false;
		}
		return overdue;
	}

	public static void main(String[] args) throws ParseException {
		EncryptData en = new EncryptData();
		String info = "ipttnkNu+YDhAfE7n19naGSUv1908l7pVHX7TfkLSrfz4mADr9akKJBPZggMGHeW7g2hbY9zE0Jmh2PLfEX3OsObYWV9+X8auAK+NATSzkSvx4MUvhPA6hP3+Kv4sqSz1TTw4gaGGgA5hareCbhx0Q==";
		String authorizeInfo = en.decrypt(info);
		String[] array = authorizeInfo.split(";");
		String limitDate = DateUtil.format(new Date(Long.valueOf(array[1])), "yyyy-MM-dd");
		String currentDateStr = DateUtil.format(new Date(Long.valueOf(array[3])), "yyyy-MM-dd");
		System.out.println(authorizeInfo);
		
		System.out.println(DateUtil.parseLongDate("2015-03-15", "yyyy-MM-dd"));
		System.out.println(en.encrypt("db0-80-86--64-0-8-&0-80-86--64-0-1-&0--32-76-49-31--47-&0--32-76-49-31--47-&;1426348800000;20;1374201672078"));
	}
}
