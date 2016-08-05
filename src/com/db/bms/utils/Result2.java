package com.db.bms.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * operation result
 */
public class Result2<T> implements Serializable {
	/**
	 * data set total count
	 */
	public Integer total = 0;
	
	/**
	 * list data (rows)
	 */
	public List<T> rows = null; // the data

	/**
	 * other associated data, not list.
	 */
	public Object data=null;
	
	/**
	 * result code
	 */
	public Integer result = 0;
	
	/**
	 * result description
	 */
	public String desc = null;

	@Override
	public String toString() {
		String s = JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
		return s;
	}

	
	/**
	 * return rows size
	 * @return
	 */
	public int rowsSize() {
		if (rows!=null) {
			return rows.size();
		}
		return 0;
	}
	
	public void add(T t) {
		if (rows==null) {
			this.rows = new ArrayList<T>();
		}
		this.rows.add(t);
	}
	public void clear() {
		if (rows!=null) {
			this.rows.clear();
		}
	}
	
	// getter and setter
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}	
}
