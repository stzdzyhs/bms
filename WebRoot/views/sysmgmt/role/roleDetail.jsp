<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色详情</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
</head>
<body>
  	<table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<tr>
			<td width="40%" class="tdBlue2">角色ID</td>
			<td class="tdBlue">${role.roleId }</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">角色名称</td>
			<td class="tdBlue">${role.roleName }</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">状态</td>
			<td class="tdBlue">
			<c:if test="${role.status == 0}">
                                                启用
              </c:if>
             <c:if test="${role.status == 1}">
                                                禁用
             </c:if>
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">运营商</td>
			<td class="tdBlue">
		    <c:if test="${role.company == null && role.companyNo == -1}">
                                               超级管理员
            </c:if>
            <c:if test="${role.company != null}">
                 ${role.company.companyName }
            </c:if>
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建用户</td>
			<td class="tdBlue">
			 <c:if test="${role.operator != null}">
                ${role.operator.operatorId }
             </c:if>
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建时间</td>
			<td class="tdBlue">
			 ${role.createTime }
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">角色描述</td>
			<td class="tdBlue">${role.roleDescribe}</td>
		</tr>
	</table>
</body>
</html>