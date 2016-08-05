<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网络信息</title>
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
			url: "${path}/opermgmt/client/clientSave.action",
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
				if(rsobj.result=="true"){
					art.dialog.alert("保存成功！", goBack);
				}else{
					art.dialog.alert("保存失败！" + rsobj.desc);
				}
			}
		}
	});
	
	//异步检查ID是否可用
	$("#client_clientId").formValidator({onFocus:"CA卡ID只支持字母A-F和数字,最长20字符",onCorrect:"&nbsp;"}).
		inputValidator({min:1,max:20,onError:"CA卡ID长度1-20个字符,请确认"}).regexValidator({regExp:"^[A-Fa-f0-9]+$",dataType:"String",onError:"CA卡ID只支持字母A-F和数字,最长20字符"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/opermgmt/client/clientCheck.action?clientNo=${client.clientNo }",
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
		onError : "该CA卡ID已存在，请更换CA卡ID",
		onWait : "正在对CA卡ID进行合法性校验，请稍候..."
	}).defaultPassed();

	$("#client_clientName").formValidator({onFocus:"CA卡名称不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:30,onError:"CA卡名称不能为空，请确认"}).regexValidator({regExp:"resname",dataType:"enum",onError:"CA卡名称由中文、大小写英文字母、数字、以及下划线组成"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/opermgmt/client/clientCheckName.action?clientNo=${client.clientNo }",
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
		onError : "该CA卡名称已存在，请更换CA卡名称",
		onWait : "正在对CA卡名称进行合法性校验，请稍候..."
	}).defaultPassed();
	$("#client_clientDescribe").formValidator({onFocus:"&nbsp;",onCorrect:"&nbsp;"}).inputValidator({min:0,max:500,onErrorMax:"CA卡描述不能超过500个字符,请确认"})
	$("#client_parentId").formValidator({onFocus:"所属运营商不能为空，请选择",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"所属运营商不能为空，请选择"},min:1,onError:"所属运营商不能为空，请选择"});

});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/client/clientList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>CA卡信息</h2></div>
<form id="form1" name="form1" method="post">
<input type="hidden" name="clientNo" id="clientNo" value="${client.clientNo }" />
<c:if test="${client != null && client.parentId == -1 }">
  <input type="hidden" name="parentId" id="parentId" value="${client.parentId }" />
  <input type="hidden" name="path" id="path" value="${client.path }" />
</c:if>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">CA卡ID</td>
    <td class="tdBlue">
      <input name="clientId" value="${client.clientId }" id="client_clientId" maxlength="20" />
     
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">CA卡名称</td>
    <td class="tdBlue">
      <input name="clientName" value="${client.clientName}" id="client_clientName" maxlength="50" />
      </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">CA卡描述</td>
    <td class="tdBlue">
	  <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 500)" name="clientDescribe">${client.clientDescribe}</textarea>
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
<input type="hidden" name="path"  value="${search.path}" />
<input type="hidden" name="clientName"  value="${search.clientName}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>