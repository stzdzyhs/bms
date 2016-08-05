package com.db.bms.entity;

import com.alibaba.fastjson.JSON;

public class Column extends OperatorOwnedEntity implements java.io.Serializable {

	public Long columnNo;

	public String columnId;

	public String columnName;

	public String columnDesc;

	public String createTime;
	
	public String updateTime;

	public Long parentNo;

	public Column parent;

	public String cover;

	public Operator operator;

	// extra param
	public Long articleNo;
	
	public Integer showOrder;

	public Long templateId;

	public Template template;

	public Long groupId;
	
	//关联的strategy, 可以为null
	public  Strategy strategy;
	
	public Integer level;
	//added by MiaoJun on 2016-07-09
	private Long allocRes;
	
	private String cmds;

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public static Column fromString(String s) {
		Column c = JSON.parseObject(s, Column.class);
		return c;
	}

	// -------------------------------------------------------------------------
	// getter and setter
	// -------------------------------------------------------------------------

	public Long getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(Long columnNo) {
		this.columnNo = columnNo;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * get parent column name
	 * 
	 * @return
	 */
	public String getParentColumnName() {
		String ret;
		if (parent != null) {
			ret = parent.getColumnName();
		} else {
			ret = "";
		}
		return ret;
	}

	public String getColumnDesc() {
		return columnDesc;
	}

	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
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

	public Long getParentNo() {
		return parentNo;
	}

	public void setParentNo(Long parentNo) {
		this.parentNo = parentNo;
	}

	public Column getParent() {
		return parent;
	}

	public void setParent(Column parent) {
		this.parent = parent;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public void setDefaultNull() {
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		Column c = (Column) o;

		if (this.columnNo != null) {
			if (!this.columnNo.equals(c.columnNo)) {
				return false;
			}
		} else {
			if (c.columnNo != null) {
				return false;
			}
		}

		if (this.columnId != null) {
			if (!this.columnId.equals(c.columnId)) {
				return false;
			}
		} else {
			if (c.columnId != null) {
				return false;
			}
		}

		if (this.columnName != null) {
			if (!this.columnName.equals(c.columnName)) {
				return false;
			}
		} else {
			if (c.columnName != null) {
				return false;
			}
		}

		if (this.columnDesc != null) {
			if (!this.columnDesc.equals(c.columnDesc)) {
				return false;
			}
		} else {
			if (c.columnDesc != null) {
				return false;
			}
		}

		if (this.createTime != null) {
			if (!this.createTime.equals(c.createTime)) {
				return false;
			}
		} else {
			if (c.createTime != null) {
				return false;
			}
		}
		if (this.parentNo != null) {
			if (!this.parentNo.equals(c.parentNo)) {
				return false;
			}
		} else {
			if (c.parentNo != null) {
				return false;
			}
		}

		/*
		 * no check status, operatorNo, companyNo if(this.status!=null) {
		 * if(!this.status.equals(c.status)) { return false; } } else { if
		 * (c.status!=null) { return false; } }
		 * 
		 * boolean t = super.equals(o);
		 */

		return true;
	}

	public Long getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(Long articleNo) {
		this.articleNo = articleNo;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
