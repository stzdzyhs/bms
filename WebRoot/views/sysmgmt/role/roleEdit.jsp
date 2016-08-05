<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色信息</title>
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
			url: "${path}/sysmgmt/role/roleSave.action",
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
	//异步检查ID是否可用
	$("#roleId").formValidator({onCorrect:"&nbsp;"}).inputValidator({min:1,max:20,onError:"角色ID长度1-20个字符,请确认"}).regexValidator({regExp:"username",dataType:"enum",onError:"角色ID只支持字母和数字"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/sysmgmt/role/roleCheckId.action?roleNo=${role.roleNo }",//要把roleNo放进去，form表单下的hidden数据post不上去
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
		onError : "该角色ID已存在，请更换角色ID",
		onWait : "正在对角色ID进行合法性校验，请稍候..."
	}).defaultPassed();
	
	$("#roleName").formValidator({onFocus:"角色名称不能为空，请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"resname",dataType:"enum",onError:"角色名称由中文、大小写英文字母、数字、以及下划线组成"})
		    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/sysmgmt/role/roleCheckName.action?roleNo=${role.roleNo }",//要把roleNo放进去，form表单下的hidden数据post不上去
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
		onError : "该角色名称已存在，请更换角色名称",
		onWait : "正在对角色名称进行合法性校验，请稍候..."
	}).defaultPassed();
	
	$("#status").formValidator({onFocus:"请选择角色状态",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择角色状态"},min:1,onError:"请选择角色状态"}).defaultPassed();
	var type = ${activeOperator.type};
	if (type == 0 || type == 1){
		$("#companyNo").formValidator({onFocus:"请选择角色所属运营商",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择角色所属运营商"},min:1,onError:"请选择角色所属运营商"}).defaultPassed();
	}
	
	
});

function goBack(){
	document.getElementById("form2").action = "${path}/sysmgmt/role/roleList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>角色信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="roleNo" id="roleNo" value="${role.roleNo }" />
<input type="hidden" name="createBy" id="createBy" value="${role.createBy }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">角色ID</td>
    <td class="tdBlue">
      <input name="roleId" id="roleId" maxlength="20" value="${ role.roleId}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">角色名称</td>
    <td class="tdBlue">
      <input name="roleName" id="roleName" maxlength="50" value="${ role.roleName}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">状态</td>
    <td class="tdBlue">
       <select name="status" id="status" class="form130px"  >
	        <option value="">--请选择--</option>
	        <option value="0" <c:if test="${role.status==0 }"> selected="selected" </c:if>>启用</option>
	        <option value="1" <c:if test="${role.status==1 }"> selected="selected" </c:if>>禁用</option>
	  </select>
	</td>
  </tr>
 <c:if test="${activeOperator.type == 0 || activeOperator.type == 1}">
  <tr>
    <td width="35%" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
         <select id="companyNo" name="companyNo" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${belongList}" var="u" > 
			   <option value="${u.companyNo }" <c:if test="${role.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
			</c:forEach> 
		</select>
      </td>
  </tr>
 </c:if>
  <tr>
    <td width="35%" class="tdBlue2">角色描述</td>
    <td class="tdBlue">
        <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 500)" name="roleDescribe">${role.roleDescribe}</textarea>
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
<input type="hidden" name="companyNo" id="companyNo" value="${search.companyNo }" />
<input type="hidden" name="roleId"  value="${search.roleId}" />
<input type="hidden" name="roleName"  value="${search.roleName}" />
<input type="hidden" name="status"  value="${search.status}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
