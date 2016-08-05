<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>FTP服务器信息</title>
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

<script type="text/javascript">
$(document).ready(function(){
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/opermgmt/ftp/saveOrUpdateFtpServer.action",
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

 	$("#ip").formValidator({onFocus:"例如：172.16.201.18",onCorrect:"&nbsp;"}).regexValidator({regExp:"ip4",dataType:"enum",onError:"服务器地址格式不正确"});
	$("#port").formValidator({onFocus:"端口取值范围1-65535,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:65535,type:"value",onError:"端口取值范围1-65535,请确认"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"请输入整数"});
	$("#userName").formValidator({onFocus:"用户名不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"用户名前后不能带空格，请确认"},min:1,onError:"用户名不能为空，请确认"}).defaultPassed();
	$("#password").formValidator({onFocus:"密码不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码前后不能带空格，请确认"},min:1,onError:"密码不能为空，请确认"}).defaultPassed();
	
	var type = ${activeOperator.type};
	if (type == 0){
		$("#companyNo").formValidator({onFocus:"请选择角色所属运营商",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择角色所属运营商"},min:1,onError:"请选择角色所属运营商"}).defaultPassed();
	}
	
	
});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/ftp/ftpServerList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>FTP服务器信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="id" id="id" value="${ftpServer.id }" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${ftpServer.operatorNo }" />
<input type="hidden" name="createTime" id="createTime" value="${ftpServer.createTime }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">服务器地址</td>
    <td class="tdBlue">
      <input name="ip" id="ip" maxlength="50" value="${ftpServer.ip}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">端口</td>
    <td class="tdBlue">
      <input name="port" id="port" maxlength="10" value="${ftpServer.port}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">用户名</td>
    <td class="tdBlue">
      <input name="userName" id="userName" maxlength="50" value="${ftpServer.userName}" >
	</td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">密码</td>
    <td class="tdBlue">
      <input name="password" id="password" maxlength="50" value="${ftpServer.password}" >
	</td>
  </tr>
 <c:if test="${activeOperator.type == 0}">
  <tr>
    <td width="35%" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
         <select id="companyNo" name="companyNo" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${companyList}" var="u" > 
			   <option value="${u.companyNo }" <c:if test="${ftpServer.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
			</c:forEach> 
		</select>
      </td>
  </tr>
 </c:if>
</table>
<div style="width:100%; text-align:center; margin-top:10px;">
<input value="保存" type="submit" class="btnQuery" />
&nbsp;&nbsp;
<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
</div>
</form>
<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="companyNo" id="companyNo" value="${search.companyNo }" />
<input type="hidden" name="operatorNo"  value="${search.operatorNo}" />
<input type="hidden" name="ip"  value="${search.ip}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
