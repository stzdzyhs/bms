package com.db.bms.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayUtils {
	/**
	 * 转换ids数组为逗号分隔字符串
	 * @param ids
	 * @return
	 */
	public static String getIdsStr(Object[] ids){
		String str = "";
		if (ids!=null && ids.length>0) {
			for (int i = 0; i < ids.length; i++) {
				if (ids[i]!=null&&!ids[i].toString().trim().equals("")) {
					str +=ids[i] + ",";
				}
			}
			if(str.length()>1)
			    str = str.substring(0, str.length()-1);
		}
		return str;
	}

	public static Set<String> getIdSet(String ids){
		Set<String> set = new HashSet<String>();
		if (ids!=null&&!ids.trim().equals("")) {
			String[] idArray = ids.trim().split(",");
			for (int i = 0; i < idArray.length; i++) {
				set.add(idArray[i]);
			}
		}
		return set;
	}
	
	/**
	 * , string to Long[].  
	 * @param ids
	 * @return
	 */
	public static Long[] getIdArray(String ids){
		ArrayList<Long> set = new ArrayList<Long>();
		if (ids!=null&&!ids.trim().equals("")) {
			String[] idArray = ids.trim().split(",");
			for (int i = 0; i < idArray.length; i++) {
				set.add(Long.parseLong(idArray[i]));
			}
		}
		return set.toArray(new Long[]{});
	}
	
	
	public static Set<Long> getLongSet(String ids){
	    Set<Long> set = new HashSet<Long>();
	    if (ids!=null&&!ids.trim().equals("")) {
	        String[] idArray = ids.trim().split(",");
	        for (int i = 0; i < idArray.length; i++) {
	            set.add(Long.parseLong(idArray[i]));
	        }
	    }
	    return set;
	}
	
	/**
	 * {将字符串倒序输出}如“12,36” output "36,12"
	 * 用于数据库中读出来的字符串倒序输出了
	 * @param ids
	 * @return
	 * @author: wanghaotao
	 */
	public static String getInvertedStr(String ids){
	    String[] idsArray = ids.split(",");
	    StringBuffer ret = new StringBuffer(0);
	    for(int i=idsArray.length-1;i>=0;i--){
	        if(ret.length()>0)
	            ret.append(",");
	        ret.append(idsArray[i]);
	    }
	    return ret.toString();
	}
	
	public  static Long[] toLongArray(Object[] ids){
	    Long[] ret = new Long[ids.length];
	    int i=0;
        for(Object str:ids){
            try {
                ret[i++] = Long.parseLong(str.toString());
            }
            catch (NumberFormatException e) {
            }
        }
	    return ret;
	}
	public  static Integer[] toIntegerArray(Object[] ids){
	    Integer[] ret = new Integer[ids.length];
	    int i=0;
	    for(Object str:ids){
	        try {
	            ret[i++] = Integer.parseInt(str.toString());
	        }
	        catch (NumberFormatException e) {
	        }
	    }
	    return ret;
	}
	public  static String[] toStringArray(Object[] ids){
	    String[] ret = new String[ids.length];
	    int i=0;
	    for(Object str:ids){
	        try {
	            ret[i++] = str.toString();
	        }
	        catch (NumberFormatException e) {
	        }
	    }
	    return ret;
	}
	
	/**
	 * print list
	 * @param list
	 */
	public static <T> void printList(List<T> list, String msg) {
		if(list!=null) {
			System.out.println(msg + " size:" + list.size());
			for(T t:list) {
				System.out.println(t);
				//System.out.print(", ");
			}
		}
		else {
			System.out.println(msg + " NULL");
		}
		System.out.println("");
	}
	
	public static <K,V> void printMap(Map<K,V> map, String msg) {
		if(map!=null) {
			System.out.println(msg + " size:" + map.size());
			Set<K> ks = map.keySet();
			for(K k:ks) {
				System.out.println(k + " " + map.get(k));
			}
		}
		else {
			System.out.println(msg + " NULL");
		}
		System.out.println("");
	}
	
	public static <T> int getSize(List<T> list) {
		if(list==null) {
			return 0;
		}
		return list.size();
	}
	
	public static <T> boolean isEmpty(List<T> list) {
		if(list==null || list.size()==0) {
			return true;
		}
		return false;
	}
		
	public static <T> boolean isNotEmpty(List<T> list) {
		if(list==null || list.size()==0) {
			return false;
		}
		return true;
	}
}
