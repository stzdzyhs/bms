
package com.db.bms.sync.portal.protocal;


public class Poster {

	private Long operatorNo;

	private String posterUrl;

	private String localFile;

	public Long getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getLocalFile() {
		return localFile;
	}

	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}

	@Override
	public String toString() {
		return "Poster[posterUrl=" + this.posterUrl + ", localFile="
				+ this.localFile + ",operatorNo=" + operatorNo + "]";
	}
}
