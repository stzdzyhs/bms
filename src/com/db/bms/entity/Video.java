
package com.db.bms.entity;


public class Video extends BaseModel {

	private Long id;

	private String videoName;

	private String assetId;

	private String sourceUrl;

	private Integer status;

	private String userName;

	private String password;

	private Integer width;

	private Integer height;

	private Integer interval;

	private Long operatorNo;

	private Long companyNo;

	private Long groupId;

	private String createTime;

	private String updateTime;

	private String sendTime;

	private String originResult;

	private String failReason;

	private Operator operator;

	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getOriginResult() {
		return originResult;
	}

	public void setOriginResult(String originResult) {
		this.originResult = originResult;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
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

	@Override
	public void setDefaultNull() {

	}

	public static enum VideoStatus {
		DRAFT(0), COMMITTING(1), COMMIT_SUCCESS(2), COMMIT_FAILED(3), DOWNLOADING(
				4), DOWNLOAD_FAILED(5), CAPTURING(6), CAPTURE_FAILED(7), INJECTING(
				8), CAPTURE_FINISHED(9), INJECT_FAILED(10);

		private final int index;

		private VideoStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static VideoStatus getStatus(int index) {
			switch (index) {
			case 0:
				return DRAFT;
			case 1:
				return COMMITTING;
			case 2:
				return COMMIT_SUCCESS;
			case 3:
				return COMMIT_FAILED;
			case 4:
				return DOWNLOADING;
			case 5:
				return DOWNLOAD_FAILED;
			case 6:
				return CAPTURING;
			case 7:
				return CAPTURE_FAILED;
			case 8:
				return INJECTING;
			case 9:
				return CAPTURE_FINISHED;
			case 10:
				return INJECT_FAILED;
			}
			return DRAFT;
		}
	}
}
