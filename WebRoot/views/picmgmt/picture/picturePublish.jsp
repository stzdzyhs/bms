<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发布图片</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/picmgmt/picture/picture.js"></script>
  <script src="${path}/js/win/win.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
	
});

function publish() {
		art.dialog.confirm('你确认发布操作？', function(){
				var options = {
					url: "${path}/picmgmt/picture/picturePublish.action",
					dataType: 'html',
					beforeSend: function() {
						art.dialog.through({
							id: 'broadcastLoading',
							title: "正在发布图片",
						    content: '<img src="${path}/images/08.gif" />',
						    lock: true
						});
					},
					error: function(a, b) {
						art.dialog.list['broadcastLoading'].close();
						art.dialog.alert("发布失败！");
					},
					success: function(data) {
						art.dialog.list['broadcastLoading'].close();
						eval("var rsobj = "+data+";");
						if(rsobj.result=="true" || rsobj.result=="true"){
							art.dialog.alert('发布成功！', function(){
							artDialog.open.origin.document.getElementById('btnQuery').click();
							}); 

						}else{
							art.dialog.alert("发布失败！");
						}
					}
				};
				jQuery('#form1').ajaxSubmit(options);
			}, function(){
		    art.dialog.tips('你取消了操作！');
		});
	}
	
function goBack(){
	document.getElementById("form1").action = "${path}/picmgmt/picture/pictureList.action";
	document.getElementById("form1").submit();
}

function selectStrategy(){
	Win.openWin('${path}/opermgmt/strategy/publishStrategySelect.action','请选择策略','strategyNos','showStrategies',null,null,null,null,'strategyData', 1040, 440);
}

function selectCompany(){
	Win.openWin('${path}/opermgmt/company/publishCompanySelect.action','请选择运营商','companyIds','showCompanys',null,null,null,null,'companyData', 1040, 440);
}

function selectRegion(){
	Win.openWin('${path}/opermgmt/region/publishCardRegionSelect.action','请选择区域','regionIds','showRegions',null,null,null,null,'regionData', 1040, 440);
}

function selectGroup(){
	Win.openWin('${path}/opermgmt/featureCodeGroup/groupSelect.action','请选择特征码组','featureGroupIds','showGroups',null,null,null,null,'groupData', 1040, 440);
}

function selectFeatureCode(){
	Win.openWin('${path}/opermgmt/featureCode/featureCodeSelect.action','请选择特征码','featureIds','showFeatureCodes',null,null,null,null,'featureCodeData', 1040, 440);
}
</script>
</head>

<body>
<form id="form1" name="form1">
<input type="hidden" name="pictureIds" id="pictureIds" value="${pictureIds }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
   	<td class="tdBlue2">关联策略</td>
	<td class="tdBlue">
		<div style="width: 140px; float: left;">
			<input name="button" type="button" style="width: 70px;"
			class="btnQuery" id="btn_selectStrategy" value="请选择"
			onClick="selectStrategy()" />
		</div>
		<fieldset style="width: 75%" id="strategyFSet">
			<legend id="num_showStrategies">
				<c:if test="${publish.strategies == null }">
					已选0个
				</c:if>
				<c:if test="${publish.strategies != null }">
					已选${fn:length(publish.strategies) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showStrategies" style="display: block">
				<c:forEach items="${publish.strategies}" var="strategy">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${strategy.strategyNo}" name="strategyNos" id="strategyNos" /> ${strategy.strategyName} 
					 </label>
				   </li>
			   	</c:forEach>
			</div>
	  	</fieldset>
	</td>
	</tr>
  <!-- 
  <c:if test="${activeOperator.type == 0 || activeOperator.company.parentId==-1 }">
    <tr>
   <td class="tdBlue2" width="35%">运营商</td>
	<td class="tdBlue">
		<div style="width: 140px; float: left;">
			<input name="button" type="button" style="width: 70px;"
			class="btnQuery" id="btn_selectCompany" value="请选择"
			onClick="selectCompany()" />
		</div>
		<fieldset style="width: 75%" id="companyFSet">
			<legend id="num_showCompanys">
				<c:if test="${publish.companys == null }">
					已选0个
				</c:if>
				<c:if test="${publish.companys != null }">
					已选${fn:length(publish.companys) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showCompanys" style="display: none">
				<c:forEach items="${publish.companys}" var="cmpy">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${cmpy.companyId}" name="companyIds" id="companyIds" /> ${cmpy.companyName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	  </fieldset>
	</td>
</tr>
  </c:if>
  <c:if test="${activeOperator.type != 0 && activeOperator.company.parentId!=-1 }">
    <tr>
   <td class="tdBlue2" width="35%">区域</td>
	<td class="tdBlue">
		<div style="width: 140px; float: left;">
			<input name="button" type="button" style="width: 70px;"
			class="btnQuery" id="btn_selectRegion" value="请选择"
			onClick="selectRegion()" />
		</div>
		<fieldset style="width: 75%" id="regionFSet">
			<legend id="num_showRegions">
				<c:if test="${publish.regions == null }">
					已选0个
				</c:if>
				<c:if test="${publish.regions != null }">
					已选${fn:length(publish.regions) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showRegions" style="display: none">
				<c:forEach items="${publish.regions}" var="region">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${region.id }" name="regionIds" id="regionIds" /> ${region.regionName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	  </fieldset>
	</td>
</tr>
  </c:if>
  <tr>
   <td class="tdBlue2" width="35%">特征码组</td>
	<td class="tdBlue">
		<div style="width: 140px; float: left;">
			<input name="button" type="button" style="width: 70px;"
			class="btnQuery" id="btn_selectGroup" value="请选择"
			onClick="selectGroup()" />
		</div>
		<fieldset style="width: 75%" id="groupFSet">
			<legend id="num_showGroups">
				<c:if test="${publish.featureCodeGroups == null }">
					已选0个
				</c:if>
				<c:if test="${publish.featureCodeGroups != null }">
					已选${fn:length(publish.featureCodeGroups) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showGroups" style="display: none">
				<c:forEach items="${publish.featureCodeGroups}" var="group">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${group.groupNo }" name="featureGroupIds" id="featureGroupIds" /> ${group.groupName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	  </fieldset>
	</td>
</tr>
  <tr>
   <td class="tdBlue2" width="35%">特征码</td>
	<td class="tdBlue">
		<div style="width: 140px; float: left;">
			<input name="button" type="button" style="width: 70px;"
			class="btnQuery" id="btn_selectFeatureCode" value="请选择"
			onClick="selectFeatureCode()" />
		</div>
		<fieldset style="width: 75%" id="featureCodeFSet">
			<legend id="num_showFeatureCodes">
				<c:if test="${publish.featureCodes == null }">
					已选0个
				</c:if>
				<c:if test="${publish.featureCodes != null }">
					已选${fn:length(publish.featureCodes) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showFeatureCodes" style="display: none">
				<c:forEach items="${publish.featureCodes}" var="code">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${code.featureCodeVal }" name="featureIds" id="featureIds" /> ${code.featureCodeVal} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	  </fieldset>
	</td>
</tr>
 -->
</table>
</form>
</body>
</html>
