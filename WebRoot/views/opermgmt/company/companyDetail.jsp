<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>运营商详情</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
</head>
<body>
  	<table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<tr>
			<td width="40%" class="tdBlue2">运营商网络ID</td>
			<td class="tdBlue">${company.companyId }</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">运营商名称</td>
			<td class="tdBlue">${company.companyName }</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">所属运营商</td>
			<td class="tdBlue">
			<c:if test="${company.parentId == -1}">
                                                运营中心
              </c:if>
             <c:if test="${company.parent != null && company.parentId != -1}">
                  ${company.parent.companyName }
             </c:if>
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建者</td>
			<td class="tdBlue">
			 <c:if test="${company.operator != null }">
                ${company.operator.operatorName }
             </c:if>
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建时间</td>
			<td class="tdBlue">
			 ${company.createTime }
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">运营商描述</td>
			<td class="tdBlue"><c:out value="${company.companyDescribe}"></c:out> </td>
		</tr>
	</table>
</body>
</html>