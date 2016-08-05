
package com.db.bms.entity;

import java.util.List;


public class LogResult {

	private long filePointerOffset;

	private List<String> lines;

	public long getFilePointerOffset() {
		return filePointerOffset;
	}

	public void setFilePointerOffset(long filePointerOffset) {
		this.filePointerOffset = filePointerOffset;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

}
