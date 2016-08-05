package com.db.bms.entity;

/**
 * audit status
 */
public interface AuditStatus {
	
	public static final int DRAFT = 0;
	public static final int AUDITING=(1);
	public static final int AUDIT_PASS=(2);
	public static final int AUDIT_NO_PASS=(3);
	public static final int PUBLISH=(4);
	public static final int UNPUBLISH=(5);
	
}
