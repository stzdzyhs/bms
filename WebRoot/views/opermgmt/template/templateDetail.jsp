<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模板详情</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
</head>
<body>
  	<table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<tr>
			<td width="40%" class="tdBlue2">模板名称</td>
			<td class="tdBlue">${template.templateName}</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">类型</td>
			<td class="tdBlue">
			             <c:forEach items="${templateTypeMap}" var="m" > 
					<c:if test="${template.type==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">状态</td>
			<td class="tdBlue">
			             <c:forEach items="${templateStatusMap}" var="m" > 
					<c:if test="${template.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">描述</td>
			<td class="tdBlue">${template.templateDesc}</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建用户</td>
			<td class="tdBlue">${template.operator.operatorName}</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建时间</td>
			<td class="tdBlue">${template.createTime}</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">更新时间</td>
			<td class="tdBlue">${template.updateTime}</td>
		</tr>
	</table>
</body>
</html>