package com.db.bms.entity;



import java.io.Serializable;


public class FileInfo extends BaseModel implements Serializable {

	private static final long serialVersionUID = -1771788554166364622L;

	private String name;

	private Integer type;

	private long size;

	private String updateTime;

	private String path;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void setDefaultNull() {
		// TODO Auto-generated method stub

	}

}
