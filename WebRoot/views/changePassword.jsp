<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>修改基本资料</title>
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

<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/changePwd.action",
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
					art.dialog.alert('修改密码成功，请退出重新登录！', function () {
						var win = art.dialog.open.api;
						win.close();
					});
				}else{
					art.dialog.alert("修改密码失败，请确保原密码正确！");
				}
			}
		}
	});
	$("#oldPassword").formValidator({onFocus:"原密码不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:16,onError:"原密码不能为空,请确认"});
	$("#newPassword").formValidator({onFocus:"新密码长度6-16个字符,请确认",onCorrect:"&nbsp;"}).inputValidator({min:6,max:16,onError:"新密码长度6-16个字符,请确认"});
	$("#newPassword2").formValidator({onFocus:"确认密码不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,onError:"确认密码不能为空,请确认"}).compareValidator({desID:"newPassword",operateor:"=",onError:"2次密码不一致,请确认"});
	$("#operatorName").formValidator({onFocus:"用户名称不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"名称前后不能带空格,请确认"},min:1,onError:"用户名称不能为空,请确认"});
	$("#operatorEmail").formValidator({empty:true,onFocus:"邮箱长度6-100个字符,请确认",onCorrect:"&nbsp;"}).inputValidator({min:6,max:100,onError:"你输入的邮箱长度非法,请确认"}).regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:"你输入的邮箱格式不正确"});
	$("#operatorTel").formValidator({empty:true,onFocus:"格式例如：0577-88888888或11位手机号码",onCorrect:"&nbsp;"}).regexValidator({regExp:["tel","mobile"],dataType:"enum",onError:"你输入的手机或电话格式不正确"});
	
});
</script>
</head>
<body>
<div class="title">
  <h2>修改基本资料</h2>
</div>
<form id="form1" name="form1" method="post" action="">
<input type="hidden" name="operatorNo" id="operatorNo" value="${activeOperator.operatorNo }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
<c:if test="${activeOperator.type != 0}">
  <tr>
    <td width="140px" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
    ${activeOperator.company.companyName}
     </td>
  </tr>
</c:if>
  <tr>
    <td width="140px" class="tdBlue2">空间信息(已用/总)</td>
    <td class="tdBlue">
         <c:if test="${operator.totalSize != -1 }">
       <c:choose>
        <c:when test="${operator.usedSize == 0 }">
         ${operator.totalSize }G
       </c:when>
       <c:otherwise>
            ${operator.usedSizeFmt } / ${operator.totalSize }G
       </c:otherwise>
       </c:choose>
     </c:if>
     <c:if test="${operator.totalSize == -1 }">
       不限制
     </c:if>
    </td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">原密码</td>
    <td class="tdBlue">
      <div style="width:130px; float:left;"><input type="password" name="oldPassword" id="oldPassword" /></div><div id="oldPasswordTip" style="width:250px; float:left;"></div></td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">新密码</td>
    <td class="tdBlue">
      <div style="width:130px; float:left;"><input type="password" name="newPassword" id="newPassword" /></div><div id="newPasswordTip" style="width:250px; float:left;"></div></td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">确认密码</td>
    <td class="tdBlue">
      <div style="width:130px; float:left;"><input type="password" name="newPassword2" id="newPassword2" /></div><div id="newPassword2Tip" style="width:250px; float:left;"></div></td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">用户名称</td>
    <td class="tdBlue">
      <div style="width:130px; float:left;"><input type="text" name="operatorName" id="operatorName" value="${activeOperator.operatorName }" maxlength="50" /></div><div id="operatorNameTip" style="width:250px; float:left;"></div></td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">电子邮箱</td>
    <td class="tdBlue">
      <div style="width:130px; float:left;"><input type="text" name="operatorEmail" id="operatorEmail" value="${activeOperator.operatorEmail }" maxlength="100" /></div><div id="operatorEmailTip" style="width:250px; float:left;"></div></td>
  </tr>
  <tr>
    <td width="140px" class="tdBlue2">电话号码</td>
    <td class="tdBlue">
      <div style="width:130px; float:left;"><input type="text" name="operatorTel" id="operatorTel" value="${activeOperator.operatorTel }" maxlength="20" /></div><div id="operatorTelTip" style="width:250px; float:left;"></div></td>
  </tr>
</table>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" id="btn_submit" style="display:none;" class="btnQuery" />
</div>
</form>
</body>
</html>
