<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>栏目信息</title>
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
<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/picmgmt/column/saveOrUpdateColumn.action",
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
				if(rsobj.result=="true"){
					art.dialog.alert("保存成功！", goBack);
				}else{
					art.dialog.alert("保存失败！" + rsobj.desc);
				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});

	$("#showOrder").formValidator({onFocus:"显示顺序只支持正整数且不能为0,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"^[1-9]{1}[0-9]*$",onError:"显示顺序只支持正整数且不能为0"}).defaultPassed();
	$("#columnName").formValidator({onFocus:"栏目名称不能为空，请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"resname",dataType:"enum",onError:"栏目名称由中文、大小写英文字母、数字、以及下划线组成"}).defaultPassed();
});

function goBack(){
	document.getElementById("form2").action = "${path}/picmgmt/column/columnList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>栏目信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${column.id }" />
<input type="hidden" name="topicId" id="topicId" value="${topic.id }" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${column.operatorNo }" />
<input type="hidden" name="companyNo" id="companyNo" value="${column.companyNo}" />
<input type="hidden" name="createTime" id="createTime" value="${column.createTime }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">显示顺序</td>
    <td class="tdBlue">
      <input name="showOrder" id="showOrder" maxlength="8" value="${column.showOrder == null ? 100 : column.showOrder}" size="5">
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">栏目名称</td>
    <td class="tdBlue">
      <input name="columnName" id="columnName" maxlength="30" value="${column.columnName}" ></td>
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
<input type="hidden" name="columnName" id="columnName" value="${search.columnName}" />
<input type="hidden" name="topicId"  value="${topic.id}" />
<input type="hidden" name="status" id="status" value="${search.status}" />
<input type="hidden" name="operatorNo"  value="${search.operatorNo}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
