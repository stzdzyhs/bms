package com.db.bms.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class HardwareUtil {

	public static String getMac() {
		StringBuffer buf = new StringBuffer();
		try {
			Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (el.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) el.nextElement();
				byte[] mac = ni.getHardwareAddress();
				if (mac == null || mac.length == 0)
					continue;
				Enumeration<InetAddress> nii = ni.getInetAddresses();
				while (nii.hasMoreElements()) {
					ip = nii.nextElement();
					if (ip instanceof Inet6Address)
						continue;
					if (!ip.isReachable(3000))
						continue;
					for (byte b : mac) {
						buf.append(b + "-");
					}
					buf.append("&");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
	
	public static void main(String[] arg){
		String mac = HardwareUtil.getMac();
		System.out.println("mac:"+mac);
		EncryptData en = new EncryptData();
		String enStr1 = en.encrypt(mac);
		System.out.println("enStr1:"+enStr1);
	}

}
