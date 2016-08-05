<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>运营商信息</title>
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
			url: "${path}/opermgmt/company/companySave.action",
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
	$("#company_companyId").formValidator({onFocus:"运营商网络ID只支持正整数",onCorrect:"&nbsp;"}).inputValidator({min:1,max:5,onError:"运营商网络ID长度1-5位数字,请确认"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"运营商网络ID只支持正整数"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/opermgmt/company/companyCheck.action?companyNo=${company.companyNo }",
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
		onError : "该运营商网络ID已存在，请更换运营商网络ID",
		onWait : "正在对运营商网络ID进行合法性校验，请稍候..."
	}).defaultPassed();
	$("#company_companyName").formValidator({onFocus:"运营商名称不能为空，请确认",onCorrect:"&nbsp;"}).inputValidator({min:1,max:30,onError:"运营商名称不能为空，请确认"}).regexValidator({regExp:"resname",dataType:"enum",onError:"运营商名称由中文、大小写英文字母、数字、以及下划线组成"})
	    .ajaxValidator({
	    type : "POST",
		dataType : "html",
		async : true,
		url : "${path}/opermgmt/company/companyCheckName.action?companyNo=${company.companyNo }",
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
		onError : "该运营商名称已存在，请更换运营商名称",
		onWait : "正在对运营商名称进行合法性校验，请稍候..."
	}).defaultPassed();
	$("#company_companyDescribe").formValidator({onFocus:"&nbsp;",onCorrect:"&nbsp;"}).inputValidator({min:0,max:500,onErrorMax:"运营商描述不能超过500个字符,请确认"})
	$("#company_parentId").formValidator({onFocus:"所属运营商不能为空，请选择",onCorrect:"&nbsp;"}).inputValidator({empty:{leftEmpty:false,rightEmpty:false,emptyError:"所属运营商不能为空，请选择"},min:1,onError:"所属运营商不能为空，请选择"});

});

function goBack(){
	document.getElementById("form2").action = "${path}/opermgmt/company/companyList.action";
	document.getElementById("form2").submit();
}
</script>
</head>

<body>
<div class="title"><h2>运营商信息</h2></div>
<form id="form1" name="form1" method="post">
<input type="hidden" name="companyNo" id="companyNo" value="${company.companyNo }" />
<input type="hidden" name="createBy" id="createBy" value="${company.createBy }" />
<c:if test="${company != null && company.parentId == -1 }">
  <input type="hidden" name="parentId" id="parentId" value="${company.parentId }" />
  <input type="hidden" name="path" id="path" value="${company.path }" />
</c:if>
<table class="tableCommon tdBorWhite " width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="35%" class="tdBlue2">运营商网络ID</td>
    <td class="tdBlue">
      <input name="companyId" value="${company.companyId }" id="company_companyId" maxlength="20" />
     
  </tr>
  <tr>
    <td width="35%" class="tdBlue2">运营商名称</td>
    <td class="tdBlue">
      <input name="companyName" value="${company.companyName}" id="company_companyName" maxlength="50" />
      </td>
  </tr>
  <c:if test="${company != null && company.parentId != -1 }">
  <tr>
    <td width="35%" class="tdBlue2">所属运营商</td>
    <td class="tdBlue">
         <select id="company_parentId" name="parentId" class="form130px"  >
               <option value="">--请选择--</option>
			<c:forEach items="${belongList}" var="u" > 
			   <option value="${u.companyNo }" <c:if test="${company.parentId==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
			</c:forEach> 
		 </select>
      </td>
  </tr>
 </c:if>
  <tr>
    <td width="35%" class="tdBlue2">运营商描述</td>
    <td class="tdBlue">
	  <textarea rows="8" cols="80" onkeyup="this.value = this.value.slice(0, 500)" name="companyDescribe">${company.companyDescribe}</textarea>
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
<input type="hidden" name="companyName"  value="${search.companyName}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>