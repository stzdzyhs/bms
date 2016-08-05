<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>日志详情</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
</head>
<body>
  	<table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<tr>
			<td width="40%" class="tdBlue2">日志类型</td>
			<td class="tdBlue">${log.logType.logTypeName }</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">日志时间</td>
			<td class="tdBlue">${log.logTime }</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">用户账号</td>
			<td class="tdBlue">${log.operator.operatorId }</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">运营商</td>
			<td class="tdBlue">
			  <c:if test="${log.company == null && log.companyNo == -1}">
                                            超级管理员
              </c:if>
              <c:if test="${log.company != null}">
                 ${log.company.companyName}
              </c:if>
			</td>
		</tr>
		<tr>
			<td width="40%" class="tdBlue2">日志描述</td>
			<td class="tdBlue">${log.logDescribe}</td>
		</tr>
	</table>
</body>
</html>