
package com.db.bms.entity;


public class ResourceAllocation extends BaseModel {

	private Long id;

	private Integer type;

	private Long resourceId;

	private Long operatorNo;

	private Long allocBy;

	private String allocTime;
	
	private String cmdStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Long getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(Long operatorNo) {
		this.operatorNo = operatorNo;
	}

	public Long getAllocBy() {
		return allocBy;
	}

	public void setAllocBy(Long allocBy) {
		this.allocBy = allocBy;
	}

	public String getAllocTime() {
		return allocTime;
	}

	public void setAllocTime(String allocTime) {
		this.allocTime = allocTime;
	}

	public String getCmdStr() {
		return cmdStr;
	}

	public void setCmdStr(String cmdStr) {
		this.cmdStr = cmdStr;
	}

	public static enum ResourceType {
		TOPIC(1), ALBUM(2), PIC(3), COLUMN(4), ARTICLE(5), FEATURE_CODE(6), FEACODE_GROUP(7);

		private final int index;

		private ResourceType(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static ResourceType getType(int index) {
			switch (index) {
			case 1:
				return TOPIC;
			case 2:
				return ALBUM;
			case 3:
				return PIC;
			case 4:
				return COLUMN;
			case 5:
				return ARTICLE;
			case 6:
				return FEATURE_CODE;
			case 7:
				return FEACODE_GROUP;
			}
			return TOPIC;
		}
	}
	
	@Override
	public void setDefaultNull() {

	}

}
