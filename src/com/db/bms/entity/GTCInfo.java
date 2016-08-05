
package com.db.bms.entity;

import java.util.List;


public class GTCInfo {

	private String title;

	private String subTitle;

	private String text;

	private String dataId;

	private List<CoverInfo> cover;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public List<CoverInfo> getCover() {
		return cover;
	}

	public void setCover(List<CoverInfo> cover) {
		this.cover = cover;
	}

}
