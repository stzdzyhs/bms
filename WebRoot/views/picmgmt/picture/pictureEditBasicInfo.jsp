<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改图片信息</title>
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

// ok func callback
var fnOk = null;

$(document).ready(function(){
	
	$.formValidator.initConfig({mode:"AutoTip",formID:"fmPictureEditBasicInfo",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/picmgmt/picture/pictureUpdateBasicInfo.action",
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
					art.dialog.alert("保存成功！", fnOk);
				}
				else{
					art.dialog.alert("保存失败！");
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#picName").formValidator({onFocus:"请输入图片名称",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"图片名称前后不能带空格，请确认"},min:1,onError:"图片名称不能为空，请确认"}).defaultPassed();	
	$("#videoTime").formValidator({empty:true,onFocus:"视频时间只支持正整数，请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"视频时间只支持正整数，请确认"}).defaultPassed();

	//异步检查resNo是否可用
	$("#resNo").formValidator({onFocus:"请输入数字且不为空",onCorrect:"&nbsp;"}).inputValidator({min:1,max:30,onError:"编号是数字且不为空，请确认"})
		.regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"编号只支持正整数且不为空，请确认"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/picmgmt/picture/isResNoUnique.action?id=${picture.id}",
		success : function(data){
			eval("var rsobj = "+data+";");
			if(isResultSucc(rsobj)){
				return true;
			}
			else{
				return false;
			}
		},
		buttons: $("#button"),
		error: function(jqXHR, textStatus, errorThrown){art.dialog.alert("服务器没有返回数据，可能服务器忙，请重试"+errorThrown);},
		onError : "该图片编号已存在，请更换板块ID",
		onWait : "正在对图片编号进行合法性校验，请稍候..."
	}).defaultPassed();

});

// this dialog function, the parent will call this.
// ok: a ok funtion callback
function saveBasicInfo(ok) {
	fnOk = ok;
	$("#fmPictureEditBasicInfo").submit();
}

</script>
</head>

<body>
<div class="title"><h2>图片信息</h2></div>

<div>
<form id="fmPictureEditBasicInfo" name="fmPictureEditBasicInfo">
<input type="hidden" name="id" id="id" value="${picture.id}" />
<fieldset><legend>&nbsp;基本信息</legend>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td class="tdBlue" width="10%">图片名称</td>
    <td class="tdBlue" width="20%">
		<input name="picName" id="picName" maxlength="${album.picNameLen != null and album.picNameLen < 30 ? album.picNameLen : 30 }" value="${picture.picName}" >
	</td>
	<td class="tdBlue" width="20%">
		<div id="picNameTip"></div>
	</td>
    <td class="tdBlue" width="10%">图片标签</td>
    <td class="tdBlue" width="40%">
    	<input name="picLabel" id="picLabel" maxlength="100" value="${picture.picLabel}" >
	</td>
</tr>
  
</table>
</fieldset>

<div>
<fieldset><legend>&nbsp;详细信息</legend>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
    <td class="tdBlue"  width="10%">图片作者</td>
    <td class="tdBlue"  width="40%">
		<input name="picAuthor" id="picAuthor" maxlength="30" value="${picture.picAuthor}">
	</td>
    <td class="tdBlue" width="10%">图片来源</td>
    <td class="tdBlue" width="40%">
    	<input name="picSource" id="picSource" maxlength="30" value="${picture.picSource}">
    </td>
</tr>
<tr>
    <td class="tdBlue" width="10%">参与投票</td>
    <td class="tdBlue" width="40%">
    	<select id="voteFlag" name="voteFlag" class="form130px"  >
			<c:forEach items="${pictureVoteFlagMap}" var="m" >
			   <option value="${m.key }" <c:if test="${picture.voteFlag==m.key }"> selected="selected" </c:if>>${m.value }</option>
			</c:forEach> 
		</select>
	</td>
	<td class="tdBlue" width="10%">图片简介</td>
    <td class="tdBlue" width="40%">
      	<textarea rows="3" cols="40" onkeyup="this.value = this.value.slice(0, ${album.picDescLen != null and album.picDescLen < 300 ? album.picDescLen : 300 })" name="picDesc">${picture.picDesc}</textarea>
	</td>
</tr>
</table>
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	    <td class="tdBlue" width="10%">图片编号</td>
	    <td class="tdBlue" width="20%">
	    	<input name="resNo" id="resNo" maxlength="30" value="${picture.resNo}">
		</td>
		<td class="tdBlue" width="20%">
			<div id="resNoTip"></div>
		</td>
	
		<td class="tdBlue" width="10%">&nbsp;</td>
	    <td class="tdBlue" width="40%">
	      	&nbsp;
		</td>
	</tr>
</table>
</fieldset>
</div>
</form>
</div>

</body>

</html>
