
package com.db.bms.entity;

import com.db.bms.utils.ConstConfig;


public class Picture extends BaseModel {

	public Long id;

	public Integer showOrder;

	public String picName;

	public String picPath;

	public String checkCode;

	public Integer status;

	public String picLabel;

	public String picDesc;

	public String picAuthor;

	public String picSource;

	public Integer voteFlag = 0;

	public String videoTime;

	public Integer frameNum;

	// this pic is belong to which album, albumNo and articleNo should not both
	// null.
	public Long albumNo;
	public Album album;
	
	// this pic is belong to which article
	public Long articleNo;
	public Article article;

	// the res number in the article, used by template to format data
	public Integer resNo;

	public Long operatorNo;

	public Long companyNo;

	public String createTime;

	public String updateTime;

	public Company company;

	public Operator operator;
	
	//added by MiaoJun on 2016-06-08
	public String pictureId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	static String[] IMAGE_EXT = new String[] { ".gif", ".jpg", ".png" };

	/**
	 * is a image or not
	 * @return
	 */
	public boolean isImage() {
		if (picPath == null) {
			return false;
		}
		String pp = picPath.toLowerCase();
		for (int i = 0; i < IMAGE_EXT.length; i++) {
			if (pp.endsWith(IMAGE_EXT[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get pic short name. not more than 10.
	 * 
	 * @return
	 */
	public String getShortPicName() {
		if (picName != null && picName.length() > 10) {
			picName = picName.substring(0, 10);
			return picName + "...";
		}
		return picName;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	/**
	 * get picPath2 according to this picture is belong to album or article.
	 * since album and article pictures saved in different location! 
	 * @return
	 */
	public String getPicPath2() {
		if(this.articleNo!=null) { // an article picture
			return "/textmgmt/article/view/" + this.picPath;
		}
		return this.picPath;
	}
	

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getStatusName() {
		String s = ConstConfig.albumStatusMap.get(status);
		if (s == null) {
			return "未知状态";
		}
		return s;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPicLabel() {
		return picLabel;
	}

	public void setPicLabel(String picLabel) {
		this.picLabel = picLabel;
	}

	public String getPicDesc() {
		return picDesc;
	}

	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}

	public String getPicAuthor() {
		return picAuthor;
	}

	public void setPicAuthor(String picAuthor) {
		this.picAuthor = picAuthor;
	}

	public String getPicSource() {
		return picSource;
	}

	public void setPicSource(String picSource) {
		this.picSource = picSource;
	}

	/**
	 * get vote flag name
	 * 
	 * @return
	 */
	public String getVoteFlagName() {
		String s = ConstConfig.pictureVoteFlagMap.get(this.voteFlag);
		if (s == null) {
			s = "?";
		}
		return s;
	}

	public Integer getVoteFlag() {
		return voteFlag;
	}

	public void setVoteFlag(Integer voteFlag) {
		this.voteFlag = voteFlag;
	}

	public String getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}

	public Integer getFrameNum() {
		return frameNum;
	}

	public void setFrameNum(Integer frameNum) {
		this.frameNum = frameNum;
	}

	public Long getAlbumNo() {
		return albumNo;
	}

	public void setAlbumNo(Long albumNo) {
		this.albumNo = albumNo;
	}

	public Long getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(Long articleNo) {
		this.articleNo = articleNo;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Integer getResNo() {
		return resNo;
	}

	public void setResNo(Integer resNo) {
		this.resNo = resNo;
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

	public static enum PictureStatus {
		DRAFT(0), AUDITING(1), AUDIT_PASS(2), AUDIT_NO_PASS(3), PUBLISH(4), UNPUBLISH(
				5);

		private final int index;

		private PictureStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static PictureStatus getStatus(int index) {
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
	
	public boolean isArticlePic() {
		if(this.articleNo!=null) { // an article picture
			return true;
		}
		return false;
	}

	@Override
	public void setDefaultNull() {

	}

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public Long getCreatedBy(){
		if(this.operator == null){
			System.out.println("invalid op:"+this.operatorNo);
		}
		if(this.operator != null && this.operator.getType().intValue() == 2){
			return this.operator.getCreateBy();
		}
		else{
			return this.operatorNo;
		}
	}
	
}
