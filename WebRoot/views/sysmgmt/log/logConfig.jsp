<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>日志配置</title>
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
<!-- 日期控件 -->
<script src="${path}/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"logConfigForm",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/sysmgmt/log/saveOrUpdateConfig.action",
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
					art.dialog.alert('更新日志配置成功！', function () {
						var win = art.dialog.open.api;
						win.close();
					});
				}else{
					art.dialog.alert("更新日志配置失败！");
				}
			}
		}
	});
	
	$("#logBackupType").formValidator({onFocus:"备份时间不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,onError: "备份时间不能为空,请确认"}).defaultPassed();
	$("#logRunType").formValidator({onFocus:"执行类型不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,onError: "执行类型不能为空,请确认"}).defaultPassed();
	$("#logRunTimestr").formValidator({onFocus:"执行时间不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,onError:"执行时间不能为空,请确认"});
});
</script>
</head>
<body>
<div class="title">
  <h2>日志配置</h2>
</div>
<form id="logConfigForm" name="logConfigForm" method="post" action="">
<input type="hidden" name="logConfigNo" id="logConfigNo" value="${logConfig.logConfigNo }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="140px" class="tdBlue2">备份时间</td>
    <td class="tdBlue">
        <select id="logBackupType" name="logBackupType" class="form130px"  >
        <option value="">--请选择--</option>
    	<c:forEach items="${logBackupTypeMap}" var="u">
			<option value="${u.key}" <c:if test="${u.key==logConfig.logBackupType}"> selected="selected" </c:if>>${u.value}</option>
		</c:forEach>
		</select>
	 </td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">执行类型</td>
    <td class="tdBlue">
        <select id="logRunType" name="logRunType" class="form130px"  >
        <option value="">--请选择--</option>
    	<c:forEach items="${logRunTypeMap}" var="u">
			<option value="${u.key}" <c:if test="${u.key==logConfig.logRunType}"> selected="selected" </c:if>>${u.value}</option>
		</c:forEach>
		</select>
   </td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">执行时间</td>
    <td class="tdBlue">
      <input type="text" name="logRunTimestr" id="logRunTimestr" value="${logConfig.logRunTimestr }" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'H:m:s'})" class="Wdate" /></td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">&nbsp;</td>
    <td class="tdBlue"><span style="color:red;">备份后自动清除相应的日志</span></td>
  </tr>
</table>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" id="btn_submit" style="display:none;" class="btnQuery" />
</div>
</form>
</body>
</html>
