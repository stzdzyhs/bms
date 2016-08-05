<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智能卡区域信息</title>
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
 <script src="${path}/js/win/win.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/opermgmt/region/saveOrUpdateCardRegion.action",
			beforeSend: function() {
				var regionCodeReg = /^[0-9]{1,4}$/;
				var type = $("#type option:selected").val();
				var parentId = $("#parentId option:selected").val();
				var codeType = $("#codeType option:selected").val();
				if (type == 0){
					
					if (parentId == null || parentId == ''){
						art.dialog.alert("请选择所属分段！");
						return false;
					}
					
					if (codeType == 0){
						var regionCode = $("#regionCode").val();
						if (!regionCodeReg.test(regionCode)){		
							art.dialog.alert("区域码必须为1-4个数字,请确认！");
							return false;
						}
						
						if (regionCode == 0){
							art.dialog.alert("区域码不能为" + regionCode + "！");
							return false;
						}
						
						var parentRegionSectionBegin = $("#regionSectionBegin" + parentId).val();
						var parentRegionSectionEnd = $("#regionSectionEnd" + parentId).val();
						var parentRegionSectionBeginNum = parseInt(parentRegionSectionBegin);
						var parentRegionSectionEndNum = parseInt(parentRegionSectionEnd);
						if (regionCode < parentRegionSectionBeginNum || regionCode > parentRegionSectionEndNum){
							art.dialog.alert("区域码必须在" + parentRegionSectionBegin + "-" + parentRegionSectionEnd + "范围之内！");
							return false;
						}
					}else if (codeType == 1){
						var regionSectionBegin = $("#regionSectionBegin").val();
						var regionSectionEnd = $("#regionSectionEnd").val();
						if (!regionCodeReg.test(regionSectionBegin)){		
							art.dialog.alert("分段起始值必须为4个数字,请确认！");
							return false;
						}
						
						if (!regionCodeReg.test(regionSectionEnd)){		
							art.dialog.alert("分段终止值必须为4个数字,请确认）！");
							return false;
						}

						if (regionSectionBegin == 0){
							art.dialog.alert("分段起始值不能为" + regionSectionBegin + "！");
							return false;
						}
						
						if (regionSectionEnd == 0){
							art.dialog.alert("分段终止值不能为" + regionSectionEnd + "！");
							return false;
						}

						if (parseInt(regionSectionBegin) > parseInt(regionSectionBegin)){
							art.dialog.alert("分段起始值不能大于分段终止值！");
							return false;
						}
						
						var parentRegionSectionBegin = $("#regionSectionBegin" + parentId).val();
						var parentRegionSectionEnd = $("#regionSectionEnd" + parentId).val();
						var parentRegionSectionBeginNum = parseInt(parentRegionSectionBegin);
						var parentRegionSectionEndNum = parseInt(parentRegionSectionEnd);
						if (regionSectionBegin < parentRegionSectionBeginNum || regionSectionBegin > parentRegionSectionEndNum){
							art.dialog.alert("分段起始值必须在" + parentRegionSectionBegin + "-" + parentRegionSectionEnd + "范围之内！");
							return false;
						}
						
						if (regionSectionEnd < parentRegionSectionBeginNum || regionSectionEnd > parentRegionSectionEndNum){
							art.dialog.alert("分段终止值必须在" + parentRegionSectionBegin + "-" + parentRegionSectionEnd + "范围之内！");
							return false;
						}
					}
				}else if (type == 1){
					if (codeType == 0){
						var regionCode = $("#regionCode").val();
						if (!regionCodeReg.test(regionCode)){		
							art.dialog.alert("区域码必须为1-4个数字,请确认！");
							return false;
						}
						
						if (regionCode == 0){
							art.dialog.alert("区域码不能为" + regionCode + "！");
							return false;
						}
					}else if (codeType == 1){
						var regionSectionBegin = $("#regionSectionBegin").val();
						var regionSectionEnd = $("#regionSectionEnd").val();
						if (!regionCodeReg.test(regionSectionBegin)){		
							art.dialog.alert("分段起始值必须为1-4个数字,请确认！");
							return false;
						}
						
						if (!regionCodeReg.test(regionSectionEnd)){		
							art.dialog.alert("分段终止值必须为1-4个数字,请确认！");
							return false;
						}
						
						if (regionSectionBegin == 0){
							art.dialog.alert("分段起始值不能为" + regionSectionBegin + "！");
							return false;
						}
						
						if (regionSectionEnd == 0){
							art.dialog.alert("分段终止值不能为" + regionSectionEnd + "！");
							return false;
						}
						
						if (parseInt(regionSectionBegin) > parseInt(regionSectionBegin)){
							art.dialog.alert("分段起始值不能大于分段终止值！");
							return false;
						}
						
					}
				}
				
				art.dialog.through({
					id: 'broadcastLoading',
					title: false,
				    content: '<img src="${path}/images/08.gif" />',
				    lock: true
				});
			},
			error: function(a, b) {
				art.dialog.list['broadcastLoading'].close();
				art.dialog.alert("保存失败！");
				return false;
			},
			success: function(data) {
				art.dialog.list['broadcastLoading'].close();
				eval("var rsobj = "+data+";");
				if(rsobj.result=="true"){
					art.dialog.alert("保存成功！", goBack);
				}else{
					if (rsobj.desc == "repeatCode"){
						art.dialog.alert("区域码已经被使用，请重新输入！");
					}else if (rsobj.desc == "repeatName"){
						art.dialog.alert("区域名称已经被使用，请重新输入！");
					}else{
						art.dialog.alert("保存失败！" + rsobj.desc);
					} 
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});
	
	$("#regionName").formValidator({onFocus:"区域名称不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:30,onError:"区域名称不能为空，请确认"}).regexValidator({regExp:"resname",dataType:"enum",onError:"区域名称由中文、大小写英文字母、数字、以及下划线组成"}).defaultPassed();
	
	$("#type").formValidator({onFocus:"类型不能为空,请选择",onCorrect:"&nbsp;",tipID:"type_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"类型不能为空,请选择"},min:1,onError:"类型不能为空,请选择"}).defaultPassed();
	$("#codeType").formValidator({onFocus:"区域码类型不能为空,请选择",onCorrect:"&nbsp;",tipID:"codeType_tip"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"区域码类型不能为空,请选择"},min:1,onError:"区域码类型不能为空,请选择"}).defaultPassed();

	onTypeChange();
	onCodeTypeChange();
});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/region/cardRegionList.action";
	document.getElementById("form2").submit();
}

function onTypeChange()
{
	var type = $("#type option:selected").val();
	if (type == ''){
		$("#parentTr").hide();
		$("#parentId").val(null);
	}else if (type == 0) {
		$("#parentTr").show();
	}else if(type == 1){
		$("#parentTr").hide();
		$("#parentId").val(null);

	}
}

function onCodeTypeChange()
{
	var codeType = $("#codeType option:selected").val();
	if (codeType == ''){
		$("#regionCodeTr").hide();
		$("#regionSectionBeginTr").hide();
		$("#regionSectionEndTr").hide();
	 	$("#regionCode").val(null);
	 	$("#regionSectionBegin").val(null);
	 	$("#regionSectionEnd").val(null); 
	}else if (codeType == 0) {
		$("#regionCodeTr").show();
		$("#regionSectionBeginTr").hide();
		$("#regionSectionEndTr").hide();
	 	$("#regionSectionBegin").val(null);
	 	$("#regionSectionEnd").val(null); 
	} else if (codeType == 1) {
		$("#regionCodeTr").hide();
		$("#regionSectionBeginTr").show();
		$("#regionSectionEndTr").show();
	 	$("#regionCode").val(null);
	}
	
}
</script>
</head>

<body>
<div class="title"><h2>智能卡区域信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${region.id}" />
<input type="hidden" name="createTime" id="createTime" value="${region.createTime }" />
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">名称</td>
    <td class="tdBlue">
      <input name="regionName" id="regionName" maxlength="50" value="${region.regionName}" >
      <div id="regionName_tip" style="width:60%; float:right"></div>
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">类型</td>
    <td class="tdBlue">
        <select name="type" class="form130px"  id="type" onChange="onTypeChange()">
             <option value="">--请选择--</option>
			<c:forEach items="${cardRegionTypeMap}" var="t" > 
				<option value="${t.key }" <c:if test="${region.type==t.key }"> selected="selected" </c:if>>${t.value }</option>
			</c:forEach> 
		</select>
      <div id="type_tip" style="width:60%; float:right"></div>
     </td>
  </tr>
  <tr id="parentTr" style="display: none;">
    <td width="35%" class="tdBlue2">所属分段</td>
    <td class="tdBlue">
    	    <c:forEach items="${parentList}" var="p" > 
    	        <c:if test="${p.codeType == 0 }">
    	        <input type="hidden" name="regionSectionBegin${p.id}" id="regionSectionBegin${p.id}" value="${p.regionCode}" />
				<input type="hidden" name="regionSectionEnd${p.id}" id="regionSectionEnd${p.id}" value="${p.regionCode}" />
    	        </c:if>
    	        <c:if test="${p.codeType == 1 }">
    	        <input type="hidden" name="regionSectionBegin${p.id}" id="regionSectionBegin${p.id}" value="${p.regionSectionBegin}" />
				<input type="hidden" name="regionSectionEnd${p.id}" id="regionSectionEnd${p.id}" value="${p.regionSectionEnd}" />
    	        </c:if>
			</c:forEach>  
        <select name="parentId" class="form130px"  id="parentId">
             <option value="">--请选择--</option>
			<c:forEach items="${parentList}" var="p" > 
				<option value="${p.id }" <c:if test="${region.parentId==p.id }"> selected="selected" </c:if>>${p.regionName }</option>
			</c:forEach>  
		</select>
      <div id="parentId_tip" style="width:60%; float:right"></div>
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">区域码类型</td>
    <td class="tdBlue">
        <select name="codeType" class="form130px"  id="codeType" onChange="onCodeTypeChange()">
             <option value="">--请选择--</option>
			<c:forEach items="${cardRegionCodeTypeMap}" var="t" > 
				<option value="${t.key }" <c:if test="${region.codeType==t.key }"> selected="selected" </c:if>>${t.value }</option>
			</c:forEach> 
		</select>
		<div id="codeType_tip" style="width:60%; float:right"></div>
     </td>
  </tr>
  <tr id="regionCodeTr" style="display: none;">
    <td width="35%" class="tdBlue2">区域码</td>
    <td class="tdBlue">
         <input name="regionCode" id="regionCode" maxlength="4" value="${region.regionCode}" ></input>
         <div id="regionCode_tip" style="width:60%; float:right"></div>
     </td>
  </tr>
  <tr id="regionSectionBeginTr" style="display: none;">
    <td width="35%" class="tdBlue2">分段起始值</td>
    <td class="tdBlue">
         <input name="regionSectionBegin" id="regionSectionBegin" maxlength="4" value="${region.regionSectionBegin}" >
         <div id="regionSectionBegin_tip" style="width:60%; float:right"></div>
     </td>
  </tr>
  <tr id="regionSectionEndTr" style="display: none;">
    <td width="35%" class="tdBlue2">分段终止值</td>
    <td class="tdBlue">
         <input name="regionSectionEnd" id="regionSectionEnd" maxlength="4" value="${region.regionSectionEnd}" >
         <div id="regionSectionEnd_tip" style="width:60%; float:right"></div>
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">描述</td>
    <td class="tdBlue">
     <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 256)" name="depict">${region.depict}</textarea>
     </td>
  </tr>
</table>

<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>
<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="regionName"  value="${search.regionName}" />
<input type="hidden" name="type"  value="${search.type}" />
<input type="hidden" name="codeType"  value="${search.codeType}" />
<input type="hidden" name="regionCode"  value="${search.regionCode}" />
<input type="hidden" name="regionSectionBegin"  value="${search.regionSectionBegin}" />
<input type="hidden" name="regionSectionEnd"  value="${search.regionSectionEnd}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
