package com.db.bms.entity;

/**
 * column-article map
 */
public class ColumnArticleMap extends BaseModel {

	public Long columnArticleNo; // pk

	public Long columnNo;
	public Column column;
	
	public Long articleNo;
	public Article article;
	
	private Long createdBy;
	
	// extion fields, column operator
	public Operator operator;
	
	public Long getColumnArticleNo() {
		return columnArticleNo;
	}

	public void setColumnArticleNo(Long columnArticleNo) {
		this.columnArticleNo = columnArticleNo;
	}

	public Long getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Long columnNo) {
		this.columnNo = columnNo;
	}

	public Long getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(Long articleNo) {
		this.articleNo = articleNo;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public void setDefaultNull() {
	}
}
