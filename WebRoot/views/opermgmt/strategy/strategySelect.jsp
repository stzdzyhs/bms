<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>策略列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/win/contentWindow.js"></script>
<script src="${path}/js/json.js"></script>
<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
//弹出窗口 begin 
String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
}
/*当此页面被用作弹出窗口时,
*初始化已经被
*选中的社区*/
$(document).ready(function(){
	ContentWindow.register(null,null,'selectedId','strategyData');
	initWinSelect();
	
	$("#strategyName").keyup(function(){
		var strategyName = $(this).val();
		var strategyNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (strategyName != null && strategyName != '' && !strategyNameReg.test(strategyName)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
})


function initWinSelect(){
	var json = art.dialog.data('strategyData');
	if(json!=null && typeof(json)!='undefined' && json!=""){
		var dgData = eval(json);
		var items = dgData.items;
		for(var i in items){
			$("input[name='selectedId'][value='"+items[i].channelNo+"']").attr("checked",true);
		}
	}
}


/**
 * get selected items.
 * return selected items.
 * [{strategyNo:no, strategyName:'name'},....]
 */
function getSelectedItems() {
	var sets = $("input[name='selectedId']:checked");
	if(sets.length<=0){
		art.dialog.alert("请选择策略！");
		return null;
	}
	
	var data = [];
	sets.each(function() {
		var id = $(this).val();
		var d = {strategyNo:id, strategyName: $("#name"+id).text() };
		data.push(d);
	});
	
	return data;	
}

</script>
</head>
<body>
<div class="title">
	<h2>策略列表</h2>
</div>
<!-- selectUrl: e.g: /opermgmt/strategy/publishStrategySelect.action?params... -->
<form action="${path}${selectUrl}" method="post" name="strategyForm" id="strategyForm">
<div class="searchWrap">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
    	<tbody>
        	<tr>
          		<td class="searchLeft"></td>
          		<td class="searchBg">
          			<table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
	              		<tbody>
	                		<tr>
	                  			<td width="90px" height="30">策略名称：</td>
	                  			<td width="160">
	                  				<input id="strategyName" name="strategyName" class="form120px" value="${search.strategyName}"  maxlength="50" 
	                  				onMouseOver="toolTip('策略名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" />
	                  			<td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"></td>
	              	  		</tr>
	              		</tbody>
            		</table>
            	</td>
          		<td class="searchRight"></td>
        	</tr>
      	</tbody>
    </table>
</div>

<!--标题 end-->
<div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <!--对表格数据的操作 end-->
</div>
<!--工具栏 end-->
<div class="tableWrap">
	<table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
    	<thead>
        <tr>
          	<th width="5px">
	          	<c:if test="${multiselect== null || multiselect==true}">
	          		<input type="checkbox" id="pDel" onclick="ContentWindow.updateAllChannel(this);checkAll(this, 'selectedId')">
	          	</c:if>
          	</th>
          	<th width="*%">策略名称</th>
          	<th width="*%">策略状态</th>
          	<th width="*%">创建者</th>
          	<th width="*%">运营商</th>
          	<th width="*%">创建时间</th>
        </tr>
      	</thead>
      	<tbody>
      	<c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="8">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  	</c:if>
      	<c:forEach items="${list}" var="u">
        <tr>
          	<td align="center">
	          	<c:if test="${u.strategyNo != null && u.strategyNo != -1}">
          		 	<c:if test="${multiselect== null || multiselect==true}">
		          		<input type="checkbox" name="selectedId" value="${u.strategyNo }" onclick="ContentWindow.updateChannel(this)">
					</c:if>
          		 	<c:if test="${multiselect!= null && multiselect==false}">
		          		<input type="radio" name="selectedId" value="${u.strategyNo }" onclick="ContentWindow.updateChannel(this)">
					</c:if>
	          	</c:if>
          	</td>
          	<td align="center" id='name${u.strategyNo}'>${u.strategyName}</td>
          	<td align="center">${u.auditStatusName }</td>
          	<td align="center">${u.operator.operatorName }</td>
          	<td align="center">${u.company.companyName}</td>
          	<td align="center">${u.createTime }</td>
        </tr>
      	</c:forEach>
	</tbody>
    </table>
</div>

</form>
<!--表格 end-->
<div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <!--对表格数据的操作 end-->
</div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
