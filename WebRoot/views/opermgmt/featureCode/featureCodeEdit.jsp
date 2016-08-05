<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Portal信息</title>
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
<script src="${path}/js/swfupload/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/plugins/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/myhandlers.js" type="text/javascript"></script>

<script src="${path}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/opermgmt/featureCode/saveOrUpdateFeatureCode.action",
			beforeSend: function() {
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
				if(isResultSucc(rsobj)){
					art.dialog.alert("保存成功！", goBack);
				}else{
					art.dialog.alert("保存失败！"+ rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#featureCodeVal").formValidator({onFocus:"特征码值支持英文、数字和下划线",onCorrect:"&nbsp;"})
		.regexValidator({regExp:"username",dataType:"enum",onError:"特征码值格式不正确，请确认"})
    	.ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/opermgmt/featureCode/checkfeatureCodeVal.action",
		data:$("#featureCodeVal").val,
		success : function(data){
			eval("var rsobj = "+data+";");
			if(rsobj.result=="true"){
				return true;
			}else{
				return false;
			}
		},
		buttons: $("#button"),
		error: function(jqXHR, textStatus, errorThrown){art.dialog.alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);},
		onError : "该特征码值已存在",
		onWait : "正在对特征码值进行合法性校验，请稍候..."
	}).defaultPassed();

});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/featureCode/featureCodeList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>特征码信息</h2></div>
<form id="form2" name="form2" method="post">

</form>
<form id="form1" name="form1">
<input type="hidden" name="featureCodeNo" id="featureCodeNo" value="${featureCode.featureCodeNo}" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${featureCode.operatorNo }" />
<input type="hidden" name="createTime" id="createTime" value="${featureCode.createTime }" /> 
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td width="35%" class="tdBlue2">特征码值</td>
    <td class="tdBlue">
      <input name="featureCodeVal" id="featureCodeVal" maxlength="20" value="${featureCode.featureCodeVal}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">特征码类型</td>
    <td class="tdBlue">
    	<select name="featureCodeType" id="featureCodeType">
    		<option value="">--请选择--</option>
    		<option value="0" <c:if test="${featureCode.featureCodeType=='0'}">selected</c:if>>喜好特征</option>
    		<option value="1" <c:if test="${featureCode.featureCodeType=='1'}">selected</c:if>>地理位置</option>
    		<option value="2" <c:if test="${featureCode.featureCodeType=='2'}">selected</c:if>>大客户</option>
    		<option value="3" <c:if test="${featureCode.featureCodeType=='3'}">selected</c:if>>年龄特征</option>
    	</select>    
    </td>
  </tr>
  <tr>
    <td class="tdBlue2" width="10%">特征码描述</td>
      <td class="tdBlue" width="40%">
      	<textarea id="featureCodeDesc" name="featureCodeDesc" rows="3" cols="50" onkeyup="this.value = this.value.slice(0, 1000)">${featureCode.featureCodeDesc}</textarea>
      </td>
  </tr>
  
</table>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>

</body>
</html>
