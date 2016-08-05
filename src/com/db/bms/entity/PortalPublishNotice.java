
package com.db.bms.entity;

import com.alibaba.fastjson.JSON;



public class PortalPublishNotice {

	public static final int ACT_UNPUBLISH=0;
	public static final int ACT_PUBLISH=1;
	public Integer actionType;

	//@see EntityType
	public Integer parentType;
	public Long parentId;

	//@see EntityType
	public Integer resourceType;
	public Long resourceId;
	
	public String toString() {
		String s = JSON.toJSONString(this);
		return s;
	}

	public Integer getParentType() {
		return parentType;
	}

	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public Integer getResourceType() {
		return resourceType;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

}
