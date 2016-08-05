<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文章详情</title>
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
<div class="title"><h2>文章信息</h2></div>
<fieldset><legend>&nbsp;基本信息</legend>
 <table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
  <tr>
  	<td class="tdBlue" width="20%" align="right">文章ID</td>
    <td class="tdBlue" width="30%">${article.articleId} </td>
    <td class="tdBlue" width="10%" align="right">显示顺序</td>
    <td class="tdBlue" width="40%">${article.showOrder}</td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">文章名称</td>
    <td class="tdBlue" width="30%">${article.articleName}</td>
    <td class="tdBlue" width="20%" align="right">标题</td>
    <td class="tdBlue" width="30%">${article.title}</td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">副标题</td>
    <td class="tdBlue" width="30%">${article.title2}</td>
    <td class="tdBlue" width="20%" align="right">关联模板</td>
    <td class="tdBlue" width="30%">${article.template.templateName }</td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">所属运营商</td>
    <td class="tdBlue" width="30%">${article.company.companyName}</td>
    <td class="tdBlue" width="20%" align="right">创建用户</td>
    <td class="tdBlue" width="30%">${article.operator.operatorName}</td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">创建时间</td>
    <td class="tdBlue" width="30%">${article.createTime }</td>
    <td class="tdBlue" width="20%" align="right">简介</td>
    <td class="tdBlue" width="30%">
         <c:out value="${article.introduction}"></c:out> 
     </td>
  </tr>
 </table> 
</fieldset>

<c:if test="${publish != null}">
<c:forEach items="${publish}" var="p">
	<fieldset><legend>&nbsp;文章发布到版块 ${p.column.columnId} 的关联策略&nbsp;</legend>
		<c:if test="${p.publishStrategy!= null}">
			<c:forEach items="${p.publishStrategy}" var="ps">
				<div>策略名称: ${ps.strategyName}</div>						
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
			</c:forEach>		
		</c:if>
		<c:if test="${p.publishStrategy== null}">
			继承使用版块的策略
		</c:if>
	</fieldset>
</c:forEach>	
</c:if>
<c:if test="${publish == null}">
	此文章没有发布信息
</c:if>


</body>
</html>
