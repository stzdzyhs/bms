<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>生僻字信息</title>
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
<script src="${path}/js/swfupload2.5/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload2.5/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/newUploadHandler0.js" type="text/javascript"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
		type : "POST",
		dataType : "html",
		buttons:$("#button"),
		url: "${path}/textmgmt/wordstock/wordstockSave.action",
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
			//是否登陆超时	
		    ifLoginTimeout(data,goBack);	
			    
			art.dialog.list['broadcastLoading'].close();
			eval("var rsobj = "+data+";");
			if(isResultSucc(rsobj)){
				art.dialog.alert("保存成功！", goBack);
			}
			else{
				art.dialog.alert("保存失败！" + rsobj.desc);
			}
		}
	}});
	
	$("#word").formValidator({onFocus:"生僻字名称不能为空,请确认",onCorrect:"&nbsp;"})
		.inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能带空格,请确认"},min:1, onError:"生僻字不能为空,请确认"})
	    .regexValidator({regExp:"chinese",dataType:"enum",onError:"生僻字只支持中文,请确认"})
		.ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/textmgmt/wordstock/wordstockCheck.action?wordNo=${wordstock.wordNo}",
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
		onError : "该生僻字已存在，请更换生僻字",
		onWait : "正在对生僻字进行合法性校验，请稍候..."
	}).defaultPassed();
	
});

function goBack() {
	document.getElementById("form2").action = "${path}/textmgmt/wordstock/wordstockList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>生僻字信息</h2></div>
<form id="form1" name="form1" method="post">
  <input type="hidden" name="wordNo" id="wordNo" value="${wordstock.wordNo}" />
  <table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">生僻字</td>
    <td class="tdBlue">
      <input name="word" value="${wordstock.word}" id="word" maxlength="1" />
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
<%-- <input type="hidden" name="path"  value="${search.path}" /> --%>
<%-- <input type="hidden" name="columnName"  value="${search.columnName}" /> --%>
<%-- <input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" /> --%>
<%-- <input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" /> --%>
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
