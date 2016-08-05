<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.db.bms.utils.*" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>策略详细信息1</title>
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<script type='text/javascript' src="${path}/js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/css/themes/redmond/jquery-ui.css" />
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/win/win.js" type="text/javascript"></script>
 
<script src="${path}/js/swfupload/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/plugins/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/myhandlers.js" type="text/javascript"></script>

<script language="javascript" src="${path}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${path}/js/win/win.js" type="text/javascript"></script>
<script src="${path}/js/selectlist.js" type="text/javascript"></script>

<!-- 多选日期控件 -->
<script type='text/javascript' src="${path}/js/kitJs/kit.js"></script>
<!--[if IE]>
<script type='text/javascript' src="${path}/js/kitJs/ieFix.js"></script>
<![endif]-->
<script type='text/javascript' src="${path}/js/kitJs/array.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/date.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/dom.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/selector.js"></script>
<script type='text/javascript' src="${path}/js/kitJs/datepicker.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/js/kitJs/datepicker.css" />

<script type='text/javascript' src="${path}/js/multiselect/jquery.multiselect.js"></script>
<link type="text/css" rel="stylesheet" href="${path}/js/multiselect/jquery.multiselect.css" />

<script type='text/javascript' src="${path}/js/common.js"></script>
<script type='text/javascript' src="${path}/js/bms.js"></script>

<script type="text/javascript">

var cnt = 0;

function p1() {
	cnt ++;
	console.log(cnt + " active ele: " + document.activeElement + " id:" + document.activeElement.id);
}
$(document).ready(function(){
	keyDownClose();
	//setInterval(p1, 1000);
});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/strategy/strategyList.action";
	document.getElementById("form2").submit();
}

</script>
</head>

<body style="padding:15px;" id='ebody'>
<div class="title"><h2>策略详细信息</h2></div>

<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  	<tr>
    	<td class="tdBlue" width="20%" align="right">策略名称</td>
    	<td class="tdBlue" width="80%" colspan="3">
			${strategy.strategyName}
     	</td>  
	</tr>
	<tr>
		<td class="tdBlue" width="20%" align="right">条件关系</td>
		<td class="tdBlue" width="30%">
			${strategy.strategyFormName}
     	</td> 
		<td class="tdBlue" width="20%" align="right">所属运营商</td>
    	<td class="tdBlue" width="30%">
    		${strategy.company.companyName}
    	</td>
	</tr>
</table>
</fieldset>

<c:if test="${not empty(strategy.companyList)}" >
<div>
	<fieldset><legend>&nbsp;关联运营商条件</legend>
	<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
		<c:forEach items="${strategy.companyList}" var="c">
		<tr>
	    	<td width="35%" class="tdBlue2">运营商				
			</td>
			<td class="tdBlue">
				<a onclick="showCompanyDetailDlg(${c.company.companyNo});">
					${c.company.companyName}
				</a>
			</td>
		</tr>
		</c:forEach>
	</table>
	</fieldset>
</div>
</c:if>

<c:if test="${not empty(strategy.cardRegionList)}" >
<div>
	<fieldset><legend>&nbsp;关联区域码条件</legend>
	<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
		<c:forEach items="${strategy.cardRegionList}" var="c">
		<tr>
	    	<td width="35%" class="tdBlue2">区域码				
			</td>
			<td class="tdBlue">
				<a onclick="showCardRegionDetailDlg(${c.cardRegion.id});">
					${c.cardRegion.regionName}
				</a>
			</td>
		</tr>
		</c:forEach>
	</table>
	</fieldset>
</div>
</c:if>

<c:if test="${not empty(strategy.featureCodeList)}" >
<div>
	<fieldset><legend>&nbsp;关联特征码条件</legend>
	<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
		<c:forEach items="${strategy.featureCodeList}" var="c">
		<tr>
	    	<td width="35%" class="tdBlue2">特征码				
			</td>
			<td class="tdBlue">
				<a onclick="showFeatureCodeDetailDlg(${c.featureCode.featureCodeNo});">
					${c.featureCode.featureCodeVal}
				</a>
			</td>
		</tr>
		</c:forEach>
	</table>
	</fieldset>
</div>
</c:if>

<c:if test="${not empty(strategy.clientList)}" >
<div>
	<fieldset><legend>&nbsp;关联用户条件</legend>
	<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
		<c:forEach items="${strategy.clientList}" var="c">
		<tr>
	    	<td width="35%" class="tdBlue2">用户				
			</td>
			<td class="tdBlue">
				<a onclick="showClientDetailDlg(${c.client.clientNo});">
					${c.client.clientName}
				</a>
			</td>
		</tr>
		</c:forEach>
	</table>
	</fieldset>
</div>
</c:if>

<c:if test="${not empty(strategy.spaceList)}" >
<div>
	<fieldset><legend>&nbsp;关联空分组条件</legend>
	<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
		<c:forEach items="${strategy.spaceList}" var="c">
		<tr>
	    	<td width="35%" class="tdBlue2">空分组				
			</td>
			<td class="tdBlue">
				<a onclick="showSpaceDetailDlg(${c.space.spaceNo});">
					${c.space.spaceName}
				</a>
			</td>
		</tr>
		</c:forEach>
	</table>
	</fieldset>
</div>
</c:if>

</body>
</html>
