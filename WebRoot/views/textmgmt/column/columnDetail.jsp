<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>版块详情</title>
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
<div class="title"><h2>版块信息</h2></div>
<fieldset><legend>&nbsp;基本信息</legend>
 <table class="tableCommon tdBorWhite" width="100%" border="0"
		cellspacing="0" cellpadding="0">
  <tr>
    <td class="tdBlue" width="20%" align="right">版块ID</td>
    <td class="tdBlue" width="30%">${column.columnId} </td>
    <td class="tdBlue" width="20%" align="right">版块名称</td>
    <td class="tdBlue" width="30%">			             
       ${column.columnName}
    </td>
  </tr>
  <tr height="80">
    <td class="tdBlue" width="20%" align="right">封面</td>
    <td class="tdBlue" width="30%">
				${u.cover}
				<c:if test="${column.cover!=null}">
			    	<img src="${path}/${column.cover}" style='width:64px; height:64px' id='toolTipImg'/>
				</c:if>
     </td>
    <td class="tdBlue" width="20%" align="right">所属版块</td>
    <td class="tdBlue" width="30%">			             
    ${column.parentColumnName}
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">关联模板</td>
    <td class="tdBlue" width="30%">
      ${column.template.templateName }	    
     </td>
    <td class="tdBlue" width="20%" align="right">所属运营商</td>
    <td class="tdBlue" width="30%">			             
      ${column.company.companyName}
    </td>
  </tr>
  <tr>
    <td class="tdBlue" width="20%" align="right">创建用户</td>
    <td class="tdBlue" width="30%">
${column.operator.operatorName}
     </td>
    <td class="tdBlue" width="20%" align="right">创建时间</td>
    <td class="tdBlue" width="30%">			             
   ${column.createTime }
    </td>
  </tr>
  <tr>
  	<td class="tdBlue" width="20%" align="right">更新时间</td>
    <td class="tdBlue" width="30%">			             
   ${column.updateTime }
    </td>
    <td class="tdBlue" width="20%" align="right">版块描述</td>
    <td class="tdBlue" width="30%">		
    <c:out value="${column.columnDesc}"></c:out> 	             
    </td>
        <td class="tdBlue" width="20%"></td>
    <td class="tdBlue" width="30%">

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
		此版块发布到全网	
	</c:if>
</c:if>
<c:if test="${publish == null}">
	此版块没有发布信息
</c:if>

</body>
</html>
