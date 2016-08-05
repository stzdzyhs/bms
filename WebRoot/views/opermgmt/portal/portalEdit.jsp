<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Portal信息</title>
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
			url: "${path}/opermgmt/portal/saveOrUpdatePortal.action",
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

	$("#sysId").formValidator({onFocus:"系统ID由英文字母组成,请确认",onCorrect:"&nbsp;"}).regexValidator({regExp:"letter",dataType:"enum",onError:"系统ID格式不正确，请确认"})
    .ajaxValidator({
    type : "POST",
	dataType : "html",
	async : true,
	url : "${path}/opermgmt/portal/checkData.action?sysNo=${portal.sysNo}",
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
	onError : "该系统ID已存在，请更换系统ID",
	onWait : "正在对系统ID进行合法性校验，请稍候..."
}).defaultPassed();

$("#sysName").formValidator({onFocus:"系统名称不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:30,onError:"系统名称不能为空，请确认"}).regexValidator({regExp:"resname",dataType:"enum",onError:"系统名称由中文、大小写英文字母、数字、以及下划线组成"})
.ajaxValidator({
type : "POST",
dataType : "html",
async : true,
url : "${path}/opermgmt/portal/checkData.action?sysNo=${portal.sysNo}",
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
onError : "该系统名称已存在，请更换系统名称",
onWait : "正在对系统名称进行合法性校验，请稍候..."
}).defaultPassed();

$("#sysUrl").formValidator({onFocus:"系统URL不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"系统URL前后不能带空格，请确认"},min:1,onError:"系统URL不能为空，请确认"}).defaultPassed();

	var type = ${activeOperator.type};
	if (type == 0){
		$("#companyNo").formValidator({onFocus:"请选择角色所属运营商",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"请选择角色所属运营商"},min:1,onError:"请选择角色所属运营商"}).defaultPassed();
	}
	

});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/portal/portalList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>Portal信息</h2></div>
<form id="form1" name="form1">
<input type="hidden" name="sysNo" id="sysNo" value="${portal.sysNo}" />
<input type="hidden" name="operatorNo" id="operatorNo" value="${portal.operatorNo }" />
<input type="hidden" name="createTime" id="createTime" value="${portal.createTime }" />
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">系统ID</td>
    <td class="tdBlue">
      <input name="sysId" id="sysId" maxlength="20" value="${portal.sysId}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">系统名称</td>
    <td class="tdBlue">
      <input name="sysName" id="sysName" maxlength="20" value="${portal.sysName}" ></td>
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">系统URL</td>
    <td class="tdBlue">
    <input name="sysUrl" id="sysUrl" maxlength="200" value="${portal.sysUrl}" ></td>
  </tr>
 <c:if test="${activeOperator.type == 0}">
  <tr>
    <td width="35%" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
         <select id="companyNo" name="companyNo" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${companyList}" var="u" > 
			   <option value="${u.companyNo }" <c:if test="${portal.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
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
<input type="hidden" name="sysId"  value="${search.sysId}" />
<input type="hidden" name="sysName"  value="${search.sysName}" />
<input type="hidden" name="status"  value="${search.status}" />
<input type="hidden" name="companyNo"  value="${search.companyNo}" />
<input type="hidden" name="operatorNo"  value="${search.operatorNo}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
