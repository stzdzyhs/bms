<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>特征码列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>
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
	ContentWindow.register(null,null,'selectedId','featureCodeData');
	initWinSelect();
	
	$("#featureCodeVal").keyup(function(){
		var featureCodeVal = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (featureCodeVal != null && featureCodeVal != '' && !resNameReg.test(featureCodeVal)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	$("#featureCodeType").keyup(function(){
		var featureCodeType = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (featureCodeType != null && featureCodeType != '' && !resNameReg.test(featureCodeType)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
})


function initWinSelect(){
	var json = art.dialog.data('featureCodeData');
	if(json!=null && typeof(json)!='undefined' && json!=""){
		var dgData = eval(json);
		var items = dgData.items;
		for(var i in items){
			$("input[name='selectedId'][value='"+items[i].channelNo+"']").attr("checked",true);
		}
	}
}
/*当此页面被用作弹出窗口时,
*获取已选择的社区
*/
function getSelectItems(){
	var json = "", checkNos = "", unCheckNos = "";
	$("input[name='selectedId']").each(function(){
		if(this.checked){
			if(checkNos!=""){
				checkNos += ",";
			}
			checkNos += "{'channelNo':'"+$(this).val()+"', 'channelName':'"+$("#channelName"+$(this).val()).html().trim().replaceAll("'", "&#8242;")
					+"' }";
		}else{
			if(unCheckNos!=""){
				unCheckNos += ",";
			}
			unCheckNos += "{'channelNo':'"+$(this).val()+"'}";
		}
	});
	json = "({ ";
	if(checkNos!=""){
		json += " 'items':["+checkNos+"],";
	}
	if(unCheckNos!=""){
		json += " 'delItems':["+unCheckNos+"]";
	}
	json += "})";
	art.dialog.data('featureCodeData', json);
}
</script>
</head>

<body>
<div class="title">
<h2>特征码列表</h2>
</div>
<form action="${path}/opermgmt/featureCode/featureCodeSelect.action" method="post" name="featureCodeForm" id="featureCodeForm">
<input type="hidden" name="featureCodeNo" id="featureCodeNo" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg">
            <table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">特征码值：</td>
                  <td width="160">
                  	<input id="featureCodeVal" name="featureCodeVal" class="form120px" value="${search.featureCodeVal }" 
                  	onMouseOver="toolTip('特征码ID由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>

                  <%-- <td width="90px" height="30">特征值：</td>
                  <td width="160">
                  	<input id="featureCodeType" name="featureCodeVal" class="form120px" value="${search.featureCodeVal }" 
                  	onMouseOver="toolTip('特征码类型由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()"/>
                  </td> --%>
                  	
                	<td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"/></td>
                </tr>
              </tbody>
            </table>
          </td>
          <td class="searchRight"></td>
        </tr>
        <tr>
          <td class="searchButtomLeft"></td>
          <td class="searchButtom"></td>
          <td class="searchButtoRight"></td>
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
          <th width="5px"><input type="checkbox" id="pDel" onclick="ContentWindow.updateAllChannel(this);checkAll(this, 'selectedId')"></th>
          <th width="*%">特征码值</th>
          <th width="*%">特征码描述</th>
          <th width="*%">特征码类型</th>
<!--           <th width="*%">所属组别</th>
 -->          <th width="*%">创建者</th>
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
		<input type="hidden" name="oldStatus${u.featureCodeNo}" id="oldStatus" value="" />
        <tr>
          <td align="center">
	          <c:if test="${u.featureCodeNo != null && u.featureCodeNo != -1}">
	          	<input type="checkbox" name="selectedId" value="${u.featureCodeVal }" onclick="ContentWindow.updateChannel(this)">
	          </c:if>
          </td>
          <td align="center">${u.featureCodeVal }</td>
          <td align="center">${u.featureCodeDesc}</td>
          <td align="center">
          	<c:if test="${u.featureCodeType==0}">喜好特征</c:if>
          	<c:if test="${u.featureCodeType==1}">地理特征</c:if>
          	<c:if test="${u.featureCodeType==2}">大客户</c:if>
          	<c:if test="${u.featureCodeType==3}">年龄特征</c:if>
          </td>
<%--            <td align="center">${u.featureCodeGroup.groupName}</td>
 --%>          <td align="center">${u.operator.operatorName}</td>
          <td align="center">${u.createTime}</td>
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