<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文章正文</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
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

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>

<script type="text/javascript">

var fileName;
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"articleBody",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/textmgmt/article/updateArticle.action",
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
				if(isResultSucc(rsobj)) {
					fileName = rsobj.data;
					art.dialog.alert("文章正文保存成功！", refreshArticleEdit);
				}
				else{
					art.dialog.alert("保存失败 " + rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});
});
	
function onSave() {
	$("#articleBody").submit();
}

function refreshArticleEdit() {
	//刷新编辑页面
	// get parent doc
	var pDoc = window.parent.frames["mainFrame"].document; 
	var x = pDoc.getElementById("txtFileName");
	x.value = (fileName); 
	art.dialog.close();
}

function cancel() {
	art.dialog.alert("您是否要放弃正在编辑的文章正文？" ,function(){
		art.dialog.close();
	});
}

</script>

</head>
<body>

<div>
	<form id="articleBody" name="articleBody" method="post" action="${path}/textmgmt/article/articleEdit.action">
	
	<input type="hidden" id="articleNo"  name="articleNo" id="articleNo" value="${article.articleNo}" />
	<input type="hidden" name="articleId" value="${article.articleId }" />
	<input type="hidden" name="articleName" value="${article.articleName }" />
	<input type="hidden" name="title" value="${article.title }" />
	<input type="hidden" name="title2" value="${article.title2 }" />
	<input type="hidden" name="introduction" value="${article.introduction }" />
	<input type="hidden" name="body" value="${article.body }" />
	<input type="hidden" name="companyNo" value="${article.companyNo }" />
	<input type="hidden" name="showOrder" value="${article.showOrder }" />	
	<input type="hidden" name="templateId" value="${article.templateId }" />
	
	<fieldset>
		<legend>&nbsp;文章正文</legend>
		<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">		
  		<tr height="80">
    		<td class="tdBlue">
      			<textarea id="eaBody" name="editArticleBody" rows="24" cols="85">${article.editArticleBody}</textarea>
   			</td>
   		</tr>
		</table>
		<div style="width:100%; text-align:right; margin-top:10px; margin-right: 10px;">
   			<input value="保存" type="button" class="btnQuery" onclick="onSave()"/>&nbsp;
			<input value="取消" type="button" class="btnQuery" onClick="cancel()"/>
   		</div>
	</fieldset>

</form>

</div>

</body>
</html>