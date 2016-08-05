
package com.db.bms.sync.portal.protocal;


public class TemplateInfo {

	private String templateNo;

	private String templateName;

	private Integer type;

	private String filePath;

	public String getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(String templateId) {
		this.templateNo = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
