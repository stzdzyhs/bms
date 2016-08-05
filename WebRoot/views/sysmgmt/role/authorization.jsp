<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>资源授权</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/permission.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
	$("#selectAll").click(function () {//全选
		if (document.getElementById("selectAll").checked) {
			$("#options :checkbox").each(function () {
				document.getElementById($(this).attr("id")).checked = true;
			});
		} else {
			$("#options :checkbox").each(function () {
				document.getElementById($(this).attr("id")).checked = false;
			});
		}
	});
});

function submitForm() {
	$("#form1").ajaxSubmit({
		type : "POST",
		dataType : "html",
		url: "${path}/sysmgmt/role/saveAuthorization.action",
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
			var rsobj = eval("("+data+")");
			if(rsobj.result=="true"){
				art.dialog.alert("保存成功！", goBack);
			}else{
				art.dialog.alert("保存失败！");
			}
		}
	});
}

function goBack(){
	document.getElementById("form2").action = "${path}/sysmgmt/role/roleList.action";
	document.getElementById("form2").submit();
}
</script>
</head>
<body>
<form id="form1" name="form1" method="post" action="">
<input type="hidden" name="roleNo" id="roleNo" value="${role.roleNo }" />
<div class="title">
  <h2>资源授权</h2>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td>
  <div id="widget-docs" class="ui-widget ui-widget-content ui-corner-all">
    <div id="options" class="searchWrap">
      <div class="toggle-docs-links">
      <label for="selectAll"><input type="checkbox" class="checkbox" id="selectAll" />全选</label>
      <a href="#" class="toggle-docs-detail">显示</a></div>
      <ul class="options-list">
      	<!-- position type -->
		<li id="option-value" class="option">
          <div class="option-header">
            <h3 class="option-name"><a href="#option-value">B类开机应用位置</a></h3>
            <dl>
              <dt class="option-default-label">请选择下面的类型:</dt>
            </dl>
          </div>
          <div class="option-examples">
          	<c:forEach items="${positionTypeList}" var="p" varStatus="st" >
            			<dl class="option-examples-list">
              				<dd>
	               				 <label for="position_${p.typeValue }">
	               				 <input type="checkbox" class="checkbox" id="position_${p.typeValue }" name="positionTypeNos" value="${p.positionTypeNo }" 
		                			<c:forEach items="${role.positonTypes}" var="r"  >
		                				<c:if test="${r.positionTypeNo == p.positionTypeNo }"> checked </c:if>
		                			</c:forEach>
	                 			/>${p.typeName }</label>
              				</dd>
            			</dl>
          	</c:forEach>
          </div>
        </li>
      </ul>
    </div>
  </div>
  <div style="width:100%; text-align:center; margin-top:10px; float:left;">
    <input value="保存" type="button" class="btnQuery" onclick="submitForm()" />
	&nbsp;&nbsp;
	<input value="取消" type="button" class="btnQuery" onClick="goBack()" />
  </div>
</td>
</tr>
</table>
</form>
<form id="form2" name="form2" method="post">
<!-- 缓存查询条件 start -->
<input type="hidden" name="roleId"  value="${search.roleId}" />
<input type="hidden" name="roleName"  value="${search.roleName}" />
<input type="hidden" name="status"  value="${search.status}" />
<input type="hidden" name="pageUtil.pageSize"  value="${search.pageUtil.pageSize}" />
<input type="hidden" name="pageUtil.pageId"  value="${search.pageUtil.pageId}" />
<!-- 缓存查询条件 end -->
</form>
</body>
</html>
