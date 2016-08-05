
package com.db.bms.entity;

import com.db.bms.entity.Topic.CaptureFlag;
import com.db.bms.utils.ConstConfig;
import com.db.bms.utils.Delimiters;
import com.db.bms.utils.StringUtils;


public class Album extends BaseModel {

	public Long albumNo;

	public Integer showOrder;

	public String albumId;

	public String albumName;

	public String albumCover;

	public String checkCode;

	// @see AuditStatus
	public Integer status;

	public String albumLabel;

	public String albumDesc;

	public Long picSize;

	public String picFormat;

	public String picHeight;

	public String picWidth;

	public Integer picNameLen;

	public Integer picDescLen;

	public Integer captureFlag;

	public Long operatorNo;

	public Long companyNo;

	public Long groupId;

	public Long templateId;

	public String createTime;

	public String updateTime;

	public Company company;

	public Operator operator;

	public String picFormatStr;

	public String picFormatPattern;

	public Template template;
	
	public Strategy strategy;
	
	public ResourceAlbumMap resAlbumMap;
	
	private Long allocRes;
	
	private String cmds;

	/**
	 * test if this album is capture album or not.
	 * @return
	 */
	public boolean captureAlbum() {
		if(this.captureFlag!=null && this.captureFlag==CaptureFlag.YES.getIndex()) {
			return true;
		}
		return false;
	}
	
	public Long getAlbumNo() {
		return albumNo;
	}	

	public void setAlbumNo(Long albumNo) {
		this.albumNo = albumNo;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumCover() {
		return albumCover;
	}

	public void setAlbumCover(String albumCover) {
		this.albumCover = albumCover;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAlbumLabel() {
		return albumLabel;
	}

	public void setAlbumLabel(String albumLabel) {
		this.albumLabel = albumLabel;
	}

	public String getAlbumDesc() {
		return albumDesc;
	}

	public void setAlbumDesc(String albumDesc) {
		this.albumDesc = albumDesc;
	}

	public Long getPicSize() {
		return picSize;
	}

	public void setPicSize(Long picSize) {
		this.picSize = picSize;
	}

	public String getPicFormat() {
		return picFormat;
	}

	public void setPicFormat(String picFormat) {
		this.picFormat = picFormat;
		if (StringUtils.isNotEmpty(this.picFormat)) {
			String[] idArr = this.picFormat.split(Delimiters.COMMA);
			StringBuffer buffer = new StringBuffer();
			StringBuffer bufferPattern = new StringBuffer();
			for (int i = 0; i < idArr.length; i++) {
				buffer.append(ConstConfig.pictureFormatMap.get(Integer
						.valueOf(idArr[i])));
				buffer.append(Delimiters.COMMA);

				bufferPattern.append(Delimiters.ASTERISK);
				bufferPattern.append(Delimiters.DOT);
				bufferPattern.append(ConstConfig.pictureFormatMap.get(Integer
						.valueOf(idArr[i])));
				bufferPattern.append(Delimiters.SEMICOLON);
			}
			this.picFormatStr = buffer.substring(0, buffer.length() - 1);
			this.picFormatPattern = bufferPattern.substring(0,
					bufferPattern.length() - 1);
		}
	}

	public Integer getPicNameLen() {
		return picNameLen;
	}

	public void setPicNameLen(Integer picNameLen) {
		this.picNameLen = picNameLen;
	}

	public Integer getPicDescLen() {
		return picDescLen;
	}

	public void setPicDescLen(Integer picDescLen) {
		this.picDescLen = picDescLen;
	}

	public Integer getCaptureFlag() {
		return captureFlag;
	}

	public void setCaptureFlag(Integer captureFlag) {
		this.captureFlag = captureFlag;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(String picHeight) {
		this.picHeight = picHeight;
	}

	public String getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(String picWidth) {
		this.picWidth = picWidth;
	}

	public String getPicFormatStr() {
		return picFormatStr;
	}

	public void setPicFormatStr(String picFormatStr) {
		this.picFormatStr = picFormatStr;

	}

	public String getPicFormatPattern() {
		return picFormatPattern;
	}

	public void setPicFormatPattern(String picFormatPattern) {
		this.picFormatPattern = picFormatPattern;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	/* 
	 @Deprecated
	 	 
	public static enum AlbumStatus {
		DRAFT(0), AUDITING(1), AUDIT_PASS(2), AUDIT_NO_PASS(3), PUBLISH(4), UNPUBLISH(
				5);

		private final int index;

		private AlbumStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static AlbumStatus getStatus(int index) {
			switch (index) {
			case 0:
				return DRAFT;
			case 1:
				return AUDITING;
			case 2:
				return AUDIT_PASS;
			case 3:
				return AUDIT_NO_PASS;
			case 4:
				return PUBLISH;
			case 5:
				return UNPUBLISH;
			}
			return DRAFT;
		}
	}
	*/

	public ResourceAlbumMap getResAlbumMap() {
		return resAlbumMap;
	}

	public void setResAlbumMap(ResourceAlbumMap resAlbumMap) {
		this.resAlbumMap = resAlbumMap;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public void setDefaultNull() {

	}

	public Long getCreatedBy(){
		if(this.operator.getType().intValue() == 2){
			return this.operator.createBy;
		}
		else{
			return this.operatorNo;
		}
	}

	public Long getAllocRes() {
		return allocRes;
	}

	public void setAllocRes(Long allocRes) {
		this.allocRes = allocRes;
	}

	public String getCmds() {
		return cmds;
	}

	public void setCmds(String cmds) {
		this.cmds = cmds;
	}

		
	
}
