
package com.db.bms.entity;



public class Template extends BaseModel {

	private Long id;

	private String templateName;

	private Integer type;

	private Integer status;

	private String fileName;

	private String filePath;

	private String templateDesc;

	private Long operatorNo;

	private Long companyNo;

	private String createTime;

	private String updateTime;

	private Operator operator;

	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	public Long getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}

	public Long getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(Long companyNo) {
		this.companyNo = companyNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public static enum TemplateType {
		TOPIC(1), ALBUM(2), PICTURE(3), COLUMN(4), ARTICLE(5);

		private final int index;

		private TemplateType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static TemplateType getType(int index) {
			switch (index) {
			case 1:
				return TOPIC;
			case 2:
				return ALBUM;
			case 3:
				return PICTURE;
			case 4:
				return COLUMN;
			case 5:
				return ARTICLE;
			}
			return TOPIC;
		}
	}

	public static enum TemplateStatus {
		DRAFT(0), ENABLE(1), DISABLE(2);

		private final int index;

		private TemplateStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static TemplateStatus getStatus(int index) {
			switch (index) {
			case 0:
				return DRAFT;
			case 1:
				return ENABLE;
			case 2:
				return DISABLE;
			}
			return DRAFT;
		}
	}

	public static enum ComponentType {
		backGround("backGround"), pagetitle("pagetitle"),backUrl("backUrl"), buttonItem("buttonItem"), GTC("GTC"), PicList("PicList")
		,Text("Text");

		private final String name;

		private ComponentType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static ComponentType getType(String name) {
			if (name.indexOf("backGround") != -1){
				return backGround;
			}else if (name.indexOf("pagetitle") != -1){
				return pagetitle;
			}else if (name.indexOf("backUrl") != -1){
				return backUrl;
			}else if (name.indexOf("buttonItem") != -1){
				return buttonItem;
			}else if (name.indexOf("GTC") != -1){
				return GTC;
			}else if (name.indexOf("PicList") != -1){
				return PicList;
			}else if (name.indexOf("Text") != -1){
				return Text;
			}
			
			return backGround;
		}
	}

	@Override
	public void setDefaultNull() {

	}

}
