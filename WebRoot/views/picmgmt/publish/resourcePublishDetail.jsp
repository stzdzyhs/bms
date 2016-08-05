<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发布详情</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
});

</script>
</head>
<body>
<div class="title"><h2>发布详情</h2></div>
<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
    	<td class="tdBlue" width="20%" align="right">资源名称</td>
    	<td class="tdBlue" width="30%">${publish.resourceName} </td>
    	<td class="tdBlue" width="20%" align="right">资源类型</td>
    	<td class="tdBlue" width="30%"> ${publish.typeName}			             
<%--         	<c:forEach items="${resourceTypeMap}" var="m" >  --%>
<%-- 				<c:if test="${publish.type==m.key }"> ${m.value }</c:if> --%>
<%-- 			</c:forEach>  --%>
    	</td>
  	</tr>
  	<tr>
    	<td class="tdBlue" width="20%" align="right">所属资源名称</td>
    	<td class="tdBlue" width="30%">
        	${publish.parentName}
     	</td>
    	<td class="tdBlue" width="20%" align="right">所属资源类型</td>
    	<td class="tdBlue" width="30%">${publish.parentTypeName}
    				             
<%-- 		<c:forEach items="${resourceTypeMap}" var="m" >  --%>
<%-- 			<c:if test="${publish.parentType==m.key }"> ${m.value }</c:if> --%>
<%-- 		</c:forEach>  --%>
    	</td>
  	</tr>
</table>
</fieldset>

<c:if test="${publish != null}">
 	<c:if test="${publish.publishStrategy != null}">
		<c:forEach items="${publish.publishStrategy}" var="ps">
			<fieldset><legend>&nbsp;关联策略：${ps.strategyName}</legend>
			 <table class="tableCommon tdBorWhite" width="100%" border="0"
					cellspacing="0" cellpadding="0">
			  <tr>
			    <td class="tdBlue" width="20%" align="right">网络ID</td>
			    <td class="tdBlue" width="30%">${ps.networkIdStr}</td>
			    <td class="tdBlue" width="20%" align="right">区域码</td>
			    <td class="tdBlue" width="30%">			             
			      ${ps.regionCodeStr}
			    </td>
			  </tr>
			  <tr>
			    <td class="tdBlue" width="20%" align="right">卡号</td>
			    <td class="tdBlue" width="30%">${ps.cardNoStr}</td>
			    <td class="tdBlue" width="20%" align="right">特征码</td>
			    <td class="tdBlue" width="30%">			             
			      ${ps.featureIdStr}
			    </td>
			  </tr>
			  <tr>
			    <td class="tdBlue" width="20%" align="right">空分组</td>
			    <td class="tdBlue" width="30%">${ps.tsIdStr}</td>
			    <td class="tdBlue" width="20%" align="right">条件关系</td>
			    <td class="tdBlue" width="30%">			             
			      ${ps.conditionStr}
			    </td>
			  </tr>
			 </table>
			</fieldset>
		</c:forEach>
	</c:if>
	<c:if test="${publish.publishStrategy == null}">
		<c:if test="${publish.topest}">
			此${publish.typeName}发布到全网 
		</c:if>
		<c:if test="${publish.topest==false}">
			此${publish.typeName}继承使用${publish.parentTypeName}策略
		</c:if>
	</c:if>
</c:if>
<c:if test="${publish == null}">
	没有发布信息
</c:if>

</body>
</html>