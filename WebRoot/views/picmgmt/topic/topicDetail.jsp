<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专题详情</title>
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
<div class="title"><h2>专题信息</h2></div>
<fieldset><legend>&nbsp;基本信息</legend>
 <table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
  <tr>
  	<td class="tdBlue" width="20%" align="right">专题ID</td>
  	<td class="tdBlue" width="30%">${topic.topicId} </td>
  	 <td class="tdBlue" width="20%" align="right">关联模板</td>
    <td class="tdBlue" width="30%">		
    ${topic.template.templateName }	             
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">专题名称</td>
    <td class="tdBlue" width="30%">${topic.topicName} </td>
    <td class="tdBlue" width="20%" align="right">类型</td>
    <td class="tdBlue" width="30%">			             
         <c:forEach items="${topicTypeMap}" var="m" > 
					<c:if test="${topic.type==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
    </td>
  </tr>
  <tr height="80">
    <td class="tdBlue" width="20%" align="right">封面</td>
    <td class="tdBlue" width="30%">
    			       <c:if test="${topic.topicCover != null and topic.topicCover != '' }">
	      		<img src="${path}/${topic.topicCover}" height="50" />
	      	 </c:if>
     </td>
    <td class="tdBlue" width="20%" align="right">封面校验码</td>
    <td class="tdBlue" width="30%">			             
    ${topic.checkCode}
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">状态</td>
    <td class="tdBlue" width="30%">
			             <c:forEach items="${topicStatusMap}" var="m" > 
					<c:if test="${topic.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
     </td>
    <td class="tdBlue" width="20%" align="right">截图标志</td>
    <td class="tdBlue" width="30%">			             
			             <c:forEach items="${captureFlagMap}" var="m" > 
					<c:if test="${topic.captureFlag==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">所属运营商</td>
    <td class="tdBlue" width="30%">
       ${topic.company.companyName} 
     </td>
    <td class="tdBlue" width="20%" align="right">创建用户</td>
    <td class="tdBlue" width="30%">			             
     ${topic.operator.operatorName}
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">创建时间</td>
    <td class="tdBlue" width="30%">
		${topic.createTime}
     </td>
    <td class="tdBlue" width="20%" align="right">更新时间</td>
    <td class="tdBlue" width="30%">			             
     	${topic.updateTime}
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">专题简介</td>
    <td class="tdBlue" colspan="3">
		<c:out value="${topic.topicDesc}"></c:out> 
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
		此专题发布到全网	
	</c:if>
</c:if>
<c:if test="${publish == null}">
	此专题没有发布信息
</c:if>

</body>
</html>