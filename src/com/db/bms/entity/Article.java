package com.db.bms.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Article extends OperatorOwnedEntity implements	Serializable {

	public Long articleNo;

	public String articleId;

	public String articleName;

	public String title;

	public String title2;

	public String introduction;

	public String body;

	public String createTime;
	public String updateTime;

	public Long templateId; // the template
	
	public Integer status2;

	public Template template;

	// columns that this article belongs to.
	public List<Column> columns;

	// pictures in this article
	public List<Picture> pictures;

	// extra param
	public String url;
	public Long columnNo;
	public  Integer showOrder;

	public  Long groupId;
	
	//关联的strategy, 可以为null
    public  Strategy strategy;
    
    private Long allocRes;

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Long getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Long columnNo) {
		this.columnNo = columnNo;
	}

	// 编辑文章正文
	String editArticleBody;

	public String getEditArticleBody() {
		return editArticleBody;
	}

	public void setEditArticleBody(String editArticleBody) {
		this.editArticleBody = editArticleBody;
	}

	@Override
	public void setDefaultNull() {
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public static Article fromString(String s) {
		Article c = JSON.parseObject(s, Article.class);
		return c;
	}

	// -------------------------------------------------------------------------
	// getter and setter
	// -------------------------------------------------------------------------
	public Long getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(Long articleNo) {
		this.articleNo = articleNo;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Picture> getPictures() {
		/*
		 * do NOT check this !!! this will cause mybatis lost some data !!!
		 * spend almost all day to find this !!! if(pictures==null) { return new
		 * ArrayList<Picture>(); }
		 */

		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	
	public Integer getStatus2() {
		return status2;
	}

	public void setStatus2(Integer status2) {
		this.status2 = status2;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}	

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getAllocRes() {
		return allocRes;
	}

	public void setAllocRes(Long allocRes) {
		this.allocRes = allocRes;
	}
	
	public Long getCreatedBy(){
		if(this.operator != null && this.operator.getType().intValue() == 2){
			return this.operator.createBy;
		}
		else{
			return this.operatorNo;
		}
	}

	public static enum ArticleStatus {
		DRAFT(0), AUDITING(1), AUDIT_PASS(2), AUDIT_NO_PASS(3), PUBLISH(4), UNPUBLISH(5);

		private final int index;

		private ArticleStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static ArticleStatus getStatus(int index) {
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
	
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
