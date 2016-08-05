<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>模板列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	$("#templateName").keyup(function(){
		var templateName = $(this).val();
		var templateNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (templateName != null && templateName != '' && !templateNameReg.test(templateName)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});

//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='templateIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		
		for (var i=0; i<sets.length; i++){
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus == 1){
				art.dialog.alert("不能直接删除已经启用的模板！");
				return false;
			}
		}
		
	} else {
		var aa = document.getElementsByName("templateIds");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus == 1){
			art.dialog.alert("不能直接删除已经启用的模板！");
			return false;
		}
	}
	
	var param = id != '' ? "?templateIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/opermgmt/template/templateDelete.action"+param,
				dataType: 'html',
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
					art.dialog.alert("删除失败！");
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(rsobj.result=="true"){
						art.dialog.alert("删除成功！", goBack);
					}else{
						art.dialog.alert("删除失败！");
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/opermgmt/template/templateList.action";
	document.getElementById("form1").submit();
}
//进入新增编辑页面
function toEditPage(templateId){
	var oldStatus = $("#oldStatus" + templateId).val();
	if (oldStatus == 1){
		art.dialog.alert("不能编辑已经启用的模板！");
		return false;
	}
	if(templateId==null || typeof(templateId)=="undefined"){
		templateId = "";
	}
	$("#templateId").val(templateId);
	document.getElementById("form1").action = "${path}/opermgmt/template/templateEdit.action";
	document.getElementById("form1").submit();
}

function detail(templateId){

	art.dialog.open("${path}/opermgmt/template/templateDetail.action?templateId=" + templateId, 
			{
				title: "模板详情", 
				width: 450,
				height: 300,
				lock: true

			});
}

function templateAudit(status) {
	var templateIds = $("input[name='templateIds']:checked");
	if(templateId.length<=0){
		if (status == 1){
			art.dialog.alert("请选择需要启用的选项！");
		}else if (status == 2){
			art.dialog.alert("请选择需要禁用的选项！");
		}
		return false;
	}
	
	for (var i=0; i<templateIds.length; i++){
		var oldStatus = $("#oldStatus" + templateIds[i].value).val();
		if (status == 1){
			if (oldStatus == 1){
				art.dialog.alert("不能重复发布模板！");
				return false;
			}

		}else if (status == 2){
			if (oldStatus == 2){
				art.dialog.alert("不能重复禁用模板！");
				return false;
			}
		}
	}
	art.dialog.confirm('你确认该操作？', function(){
			var options = {
				url: "${path}/opermgmt/template/templateAudit.action?status=" + status,
				dataType: 'html',
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
					if (status == 1){
						art.dialog.alert("启用失败！",goBack);
					}else if (status == 2){
						art.dialog.alert("禁用失败！",goBack);
					}
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(rsobj.result=="true"){
						if (status == 1){
							art.dialog.alert("启用成功！",goBack);
						}else if (status == 2){
							art.dialog.alert("禁用成功！",goBack);
						}

					}else{
						if (status == 1){
							art.dialog.alert("启用失败！",goBack);
						}else if (status == 2){
							if (rsobj.desc == 'isReferenced'){
								art.dialog.alert("模板已经被引用，不能禁用！",goBack);
							}else{
								art.dialog.alert("禁用失败！",goBack);
							}

						}
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}
</script>
</head>
<body>
<div class="title">
  <h2>模板列表</h2>
</div>
<form action="${path}/opermgmt/template/templateList.action" method="post" name="form1" id="form1">
<input type="hidden" name="templateId" id="templateId" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="100px" height="30">模板名称</td>
                  <td width="150px">
                     <input id="templateName" name="templateName" class="form130px" value="${search.templateName}"  maxlength="30" 
                      onMouseOver="toolTip('系统名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">类型：</td>
                  <td width="160">
                  <select name="type" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${templateTypeMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.type==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${templateStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  <td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"></td>
                </tr>
              </tbody>
            </table></td>
          <td class="searchRight"></td>
        </tr>
        <tr>
          <td class="searchButtomLeft"></td>
          <td class="searchButtom"></td>
          <td class="searchButtoRight"></td>
        </tr>
      </tbody>
    </table>
  </div>
  <!--标题 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <div class="operation">
    <c:set var="add" value="false" />
    <c:set var="delete" value="false" />
    <c:set var="edit" value="false" />
    <c:set var="detail" value="false" />
    <c:set var="enable" value="false" />
    <c:set var="disable" value="false" />
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
			 <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
			 <c:set var="add" value="true" />  
		</c:if> 
		<c:if test="${item eq 'delete'}">   
			<c:set var="delete" value="true" />  
		</c:if> 
		<c:if test="${item eq 'edit'}">   
			<c:set var="edit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'detail'}">   
			<c:set var="detail" value="true" />  
		</c:if> 
		<c:if test="${item eq 'enable'}">   
			<c:set var="enable" value="true" />  
		</c:if> 
		<c:if test="${item eq 'disable'}">   
			<c:set var="disable" value="true" />  
		</c:if> 
	</c:forEach>
	<c:if test="${delete}">   
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
	</c:if> 
	<c:if test="${enable}">   
		    <input type="button" class="btn btn80" value="启用" onclick="templateAudit(1)">
	</c:if> 
	<c:if test="${disable}">   
		    <input type="button" class="btn btn80" value="禁用" onclick="templateAudit(2)">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'templateIds')"></th>
          <th width="*%">模板名称</th>
          <th width="*%">类型</th>
          <th width="*%">状态</th>
          <th width="*%">模板文件</th>
          <th width="*%">创建用户</th>
          <th width="*%">创建时间</th>
          <th width="*%">更新时间</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="9">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      	<c:forEach items="${list}" var="t">
      	       <input type="hidden" name="oldStatus${t.id}" id="oldStatus${t.id}" value="${t.status}" />
        <tr>
          <td align="center"><input type="checkbox" name="templateIds" value="${t.id}">
          </td>
          <td align="center">
             <c:choose>
             <c:when test="${fn:length(t.templateName) > 10}">
                 ${fn:substring(t.templateName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${t.templateName }
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:forEach items="${templateTypeMap}" var="m" > 
					<c:if test="${t.type==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
             <c:forEach items="${templateStatusMap}" var="m" > 
					<c:if test="${t.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
                       <c:choose>
             <c:when test="${fn:length(t.fileName) > 30}">
                 <c:out value="${fn:substring(t.fileName, 0, 30)}"></c:out>...
             </c:when>
             <c:otherwise>
                <c:out value="${t.fileName}"></c:out>
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             ${t.operator.operatorName}
          </td>
          <td align="center">${t.createTime}</td>
          <td align="center">${t.updateTime}</td>
          <td class="tdOpera2" align="center">
             <c:if test="${detail}"><a href="javascript:;" onclick="detail('${t.id}')">详情</a></c:if>
             <c:if test="${edit && t.status !=1}"><a href="javascript:;" onclick="toEditPage('${t.id}')">编辑</a></c:if>
             <c:if test="${delete && t.status !=1}"> <a href="javascript:;" onclick="deleteSets('${t.id}')">删除</a></c:if>
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
  </form>
  <!--表格 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <div class="operation">
    <c:if test="${add}">
      <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
    </c:if>
    <c:if test="${delete}">
      <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
    </c:if>
	<c:if test="${enable}">   
		    <input type="button" class="btn btn80" value="启用" onclick="templateAudit(1)">
	</c:if> 
	<c:if test="${disable}">   
		    <input type="button" class="btn btn80" value="禁用" onclick="templateAudit(2)">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>