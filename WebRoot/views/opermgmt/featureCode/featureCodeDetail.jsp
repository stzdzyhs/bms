<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特征码详情</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
</head>
<body>
  	<table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<tr>
			<td width="40%" class="tdBlue2">特征码值</td>
			<td class="tdBlue"><c:out value="${featureCode.featureCodeVal}"></c:out> </td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">特征码类型</td>
			<td class="tdBlue">
			    <c:forEach items="${featureCodeMap}" var="m" > 
					<c:if test="${featureCode.featureCodeType==m.key }"> ${m.value }</c:if>
		    	</c:forEach>
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">特征码描述</td>
			<td class="tdBlue">${featureCode.featureCodeDesc}</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">状态</td>
			<td class="tdBlue">
				<c:if test="${featureCode.status==0}">禁用</c:if>
	          	<c:if test="${featureCode.status==1}">启用</c:if>
          	</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建用户</td>
			<td class="tdBlue">${featureCode.operator.operatorName}</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">创建时间</td>
			<td class="tdBlue">${featureCode.createTime}</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">修改时间</td>
			<td class="tdBlue">${featureCode.updateTime}</td>
		</tr>

	</table>
</body>
</html>