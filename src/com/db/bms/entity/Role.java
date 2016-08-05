package com.db.bms.entity;

import java.util.List;

public class Role extends BaseModel {

	private Long roleNo;

	private String roleId;

	private String roleName;

	private Integer status;

	private String roleDescribe;

	private Long createBy;

	private Long companyNo;

	private String createTime;

	private List<Command> commands;

	private Operator operator;

	private Company company;

	// Constructors
	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(String roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	/** full constructor */
	public Role(String roleId, String roleName, String roleDescribe) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleDescribe = roleDescribe;
	}

	@Override
	public void setDefaultNull() {
		this.status = null;
	}

	public Long getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(Long roleNo) {
		this.roleNo = roleNo;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRoleDescribe() {
		return roleDescribe;
	}

	public void setRoleDescribe(String roleDescribe) {
		this.roleDescribe = roleDescribe;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
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

	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
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

	public static enum RoleStatus {
		NORMAL(0), SUSPEND(1);

		private final int index;

		private RoleStatus(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

		public static RoleStatus getSource(int index) {
			switch (index) {
			case 0:
				return NORMAL;
			case 1:
				return SUSPEND;
			}
			return NORMAL;
		}
	}
}