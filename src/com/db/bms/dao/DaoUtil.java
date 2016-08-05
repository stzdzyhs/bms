package com.db.bms.dao;

import com.db.bms.entity.Album;
import com.db.bms.entity.BaseModel;

public class DaoUtil {
	
	/**
	 * bm.sortKey, e.g: album.albumNo, changed to : album.album_no 
	 * @param bm
	 */
	public static void processSort(BaseModel bm, String defSortField, String defSortType) {
		if(bm.sortKey!=null) {
			String b1,s;
			int idx = bm.sortKey.indexOf(".");
			if(idx==-1) {
				b1 = "";
				s = bm.sortKey;
			}
			else {
				b1 = bm.sortKey.substring(0,idx+1);
				s = bm.sortKey.substring(idx+1);
			}
			
			String ret = "";
			int begin = 0;
			char[] cs = s.toCharArray();
			s = s.toLowerCase();
			
			int i;
			for(i=1;i<cs.length;i++) {
				if (Character.isUpperCase(cs[i])) {
					if(ret.length()>1) {
						ret = ret + "_";
					}
					ret = ret + s.substring(begin, i);
					begin = i;
				}
			}
			if(begin<i) {
				if(ret.length()>1) {
					ret = ret + "_";
				}
				ret = ret + s.substring(begin, i);
			}
	
			bm.sortKey = b1 + ret;
		}
		else {
			bm.sortKey = defSortField;
		}
		
		if(bm.sortKey==null) {
			bm.sortType = "desc";
		}
	}
	
	public static void main(String[] args) {
		Album a = new Album();
		a.sortKey = "album.albumNo";
		//processSort(a);
		System.out.println(a.sortKey);
	}
}
