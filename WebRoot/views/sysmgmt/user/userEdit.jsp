<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户信息</title>
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
 <script src="${path}/js/win/win.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("legend").click(function(){
		$(this).next().toggle();
	});
	$.formValidator.initConfig({mode:"AutoTip",formID:"form1",ajaxForm:{
			type : "POST",
			dataType : "html",
			buttons:$("#button"),
			url: "${path}/sysmgmt/user/saveOrUpdateUser.action",
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
					art.dialog.alert("保存成功", goBack);
				}else{
					if (rsobj.desc == 'onlyOneAdmin'){
						art.dialog.alert("同一个运营商只能有一个管理员！");
					}else if (rsobj.desc == 'exceedUsedSpace'){
						art.dialog.alert("不能小于用户已使用空间大小！");
					}else{
						art.dialog.alert("保存失败!" + rsobj.desc);
					}

				}
			}
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});
	//异步检查ID是否可用
	$("#operatorId").formValidator({onFocus:"用户账号长度1-10个字符,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:10,onError:"用户账号长度1-10个字符,请确认"}).regexValidator({regExp:"username",dataType:"enum",onError:"用户账号只支持字母和数字"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/sysmgmt/user/userCheckId.action?operatorNo=${operator.operatorNo }",
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
		onError : "该用户账号已存在，请更换用户账号",
		onWait : "正在对用户账号进行合法性校验，请稍候..."
	}).defaultPassed();
	$("#operatorName").formValidator({onFocus:"用户名称不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"名称前后不能带空格,请确认"},min:1,onError:"用户名称不能为空,请确认"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/sysmgmt/user/userCheckName.action?operatorNo=${operator.operatorNo }",
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
		onError : "该用户名称已存在，请更换用户名称",
		onWait : "正在对用户名称进行合法性校验，请稍候..."
	}).defaultPassed();
	$("#operatorPwd").formValidator({onFocus:"用户密码长度6-16个字符,请确认",onCorrect:"&nbsp;"}).inputValidator({min:6,max:16,onError:"用户密码长度6-16个字符,请确认"});
	$("#operatorPwd2").formValidator({onFocus:"确认密码不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,onError:"确认密码不能为空,请确认"}).compareValidator({desID:"operatorPwd",operateor:"=",onError:"2次密码不一致,请确认"});
	//$("#operatorName").formValidator({onFocus:"用户名称不能为空,请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"名称前后不能带空格,请确认"},min:1,onError:"用户名称不能为空,请确认"});
	$("#operatorEmail").formValidator({empty:true,onFocus:"邮箱长度6-50个字符,请确认",onCorrect:"&nbsp;"}).inputValidator({min:6,max:50,onError:"你输入的邮箱长度非法,请确认"}).regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:"你输入的邮箱格式不正确"});
	$("#operatorTel").formValidator({empty:true,onFocus:"格式例如：0577-88888888或11位手机号码",onCorrect:"&nbsp;"}).regexValidator({regExp:["tel","mobile"],dataType:"enum",onError:"你输入的手机或电话格式不正确"});
	/*$("#operatorEmail").formValidator({onFocus:"为空或符合邮箱格式",onCorrect:"&nbsp;"}).functionValidator({fun:function(val){
			if(val=='')
				return true;
			 	var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
 			if (filter.test(val)) return true;
			else {
				 return('你输入的邮箱格式不正确');
			 }
	}});*/
	
	$("#status_id").formValidator({onFocus:"请选择用户状态",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择用户状态"},min:1,onError:"请选择用户状态"}).defaultPassed();
	$("#type").formValidator({onFocus:"请选择用户类型",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择用户类型"},min:1,onError:"请选择用户类型"}).defaultPassed();
	$("#companyNo").formValidator({onFocus:"请选择用户所属的运营商",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择用户所属的运营商"},min:1,onError:"请选择用户所属的运营商"}).defaultPassed();
	
	$("#totalSize").formValidator({empty:true,onFocus:"可用空间只支持正整数,请确认（单位：GB）",onCorrect:"&nbsp;"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"可用空间只支持正整数,请确认（单位：GB）"}).defaultPassed();

	var type = '${operator.type}';
	if (type == 2){
		$("#distCompanyTr").show();
		$("#totalSizeTr").hide();
	}
	else{
		$("#totalSizeTr").show();
	}
});

function goBack(){
	document.getElementById("form2").action = "${path}/sysmgmt/user/userList.action";
	document.getElementById("form2").submit();
}

function selectRole(){
/* 	var type = '${activeOperator.type}';
	var companyNo;
	if (type == '0' || type == '1'){
	    companyNo = $("#companyNo option:selected").val();
		if (companyNo == null || companyNo == ''){
			art.dialog.alert("请选择所属运营商！");
			return false;
		}
	}else{
		companyNo = '${activeOperator.companyNo}';
	} */

	Win.openWin('${path}/sysmgmt/role/roleSelect.action','请选择角色','channelNo','showChannels',null,null,null,null,'ChannelData', 1040, 440);
}

function selectCompany(){
	var companyNo = $('#companyNo').val();
	if (companyNo == null || companyNo == ''){
		art.dialog.alert("请先选择所属运营商！");
		return false;
	}
	Win.openWin('${path}/opermgmt/company/companySelect.action?companyNo='+companyNo,'请选择运营商','cmpyNo','showCompanys',null,null,null,null,'companyData', 1040, 440);
}


function onCompanyChange(){
	$("#num_showChannels").text("已选角色0个");
	$("#showChannels").html("");
}

function onTypeChange(){
	var type = $("#type option:selected").val();
	if (type == 2) {
		$("#distCompanyTr").show();
		$("#totalSizeTr").hide();
	} else {
		$("#distCompanyTr").hide();
		$("#totalSizeTr").show();
	}
}

</script>
</head>

<body>
<div class="title"><h2>用户信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="operatorNo" id="operatorNo" value="${operator.operatorNo }" />
<input type="hidden" name="createBy" id="createBy" value="${operator.createBy }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">用户账号</td>
    <td class="tdBlue">
      <input name="operatorId" id="operatorId" maxlength="20" value="${operator.operatorId}" >
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">用户密码</td>
    <td class="tdBlue">
      <input type="password" name="operatorPwd" id="operatorPwd" value="${operator.operatorPwd}" maxlength="16" /></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">确认密码</td>
    <td class="tdBlue">
     <input type="password" name="operatorPwd2" id="operatorPwd2" value="${operator.operatorPwd}" maxlength="16" /></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">用户名称</td>
    <td class="tdBlue">
      <input name="operatorName" id="operatorName" maxlength="50" value="${operator.operatorName}" >
      </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">电子邮箱</td>
    <td class="tdBlue">
        <input name="operatorEmail" id="operatorEmail" maxlength="50" value="${operator.operatorEmail}" >
     </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">电话号码</td>
    <td class="tdBlue">
      <input name="operatorTel" id="operatorTel" maxlength="20" value="${operator.operatorTel}" >
      </td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">用户状态</td>
    <td class="tdBlue">
      <select name="status" id="status_id" class="form130px"  >
        <option value="">--请选择--</option>
    	<c:forEach items="${userStatusMap}" var="u">
			<option value="${u.key}" <c:if test="${u.key==operator.status}"> selected="selected" </c:if>>${u.value}</option>
		</c:forEach>
	  </select>
    </td>
  </tr>
    <tr>
    <td width="35%" class="tdBlue2">用户类型</td>
    <td class="tdBlue">
      <select name="type" id="type" class="form130px"  onChange="onTypeChange()">
        <option value="">--请选择--</option>
    	<c:forEach items="${userTypeMap}" var="u">
			<option value="${u.key}" <c:if test="${u.key==operator.type}"> selected="selected" </c:if>>${u.value}</option>
		</c:forEach>
	  </select>
	  </select>
    </td>
  </tr>
    <tr id="totalSizeTr" >
    <td width="35%" class="tdBlue2">可用空间</td>
    <td class="tdBlue">
     <input name="totalSize" id="totalSize" maxlength="4" value="${operator.totalSize == -1 ? '' : operator.totalSize}" size="6">
    </td>
  </tr>
  <c:if test="${activeOperator.type == 0 || activeOperator.type == 1}">
    <tr>
    <td class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
        <select name="companyNo" id="companyNo" class="form130px"  onChange="onCompanyChange()">
              <option value="">--请选择--</option>
			  <c:forEach items="${belongList}" var="u" > 
				<option value="${u.companyNo }" <c:if test="${operator.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
			</c:forEach> 
		</select>
	</td>
  </tr>
  </c:if>
    <c:if test="${activeOperator.type == 2}">
    <tr>
    <td class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
    ${activeOperator.company.companyName }
	</td>
  </tr>
  </c:if>
 <tr id="distCompanyTr" style="display: none;">
    <td class="tdBlue2">分配运营商</td>
	<td class="tdBlue">
		<div style="width: 140px; float: left;">
			<input name="button" type="button" style="width: 70px;"
			class="btnQuery" id="btn_selectCompany" value="请选择"
			onClick="selectCompany()" />
		</div>
		<fieldset style="width: 99%" id="companyFSet">
			<legend id="num_showCompanys">
				<c:if test="${operator.companys == null }">
					已选运营商0个
				</c:if>
				<c:if test="${operator.companys != null }">
					已选运营商${fn:length(operator.companys) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showCompanys" style="display: none">
				<c:forEach items="${operator.companys}" var="cmpy">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${cmpy.companyNo }" name="cmpyNo" id="cmpyNo" /> ${cmpy.companyName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	  </fieldset>
	</td>
  </tr>
  <tr>
    <td class="tdBlue2">分配角色</td>
	<td class="tdBlue">
		<div style="width: 140px; float: left;">
			<input name="button" type="button" style="width: 70px;"
			class="btnQuery" id="btn_selectChannel" value="请选择"
			onClick="selectRole()" />
		</div>
		<fieldset style="width: 99%" id="channelFSet">
			<legend id="num_showChannels">
				<c:if test="${operator.roles == null }">
					已选角色0个
				</c:if>
				<c:if test="${operator.roles != null }">
					已选角色${fn:length(operator.roles) }个
				</c:if>
			</legend>
			<div class="checkboxitems" id="showChannels" style="display: none">
				<c:forEach items="${operator.roles}" var="role">
					<li>
					  <label> 
					    <input type="checkbox" class="checkbox" checked value="${role.roleNo }" name="channelNo" id="channelNo" /> ${role.roleName} 
					 </label>
				   </li>
			   </c:forEach>
			</div>
	  </fieldset>
	</td>
  </tr>
   <tr>
    <td width="35%" class="tdBlue2">用户描述</td>
    <td class="tdBlue">
       <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 500)" name="operatorDescribe">${operator.operatorDescribe}</textarea>
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
<input type="hidden" name="companyNo"  value="${search.companyNo}" />
<input type="hidden" name="operatorId"  value="${search.operatorId}" />
<input type="hidden" name="status"  value="${search.status}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
