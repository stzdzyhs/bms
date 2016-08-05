<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>栏目列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/checkbox.js"></script>
 <script src="${path}/js/picmgmt/column/column.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#columnName").keyup(function(){
		var columnName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (columnName != null && columnName != '' && !resNameReg.test(columnName)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});
//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='columnIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		
		for (var i=0; i<sets.length; i++){
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus == 1){
				art.dialog.alert("不能直接删除已经启用的栏目！");
				return false;
			}
		}
	} else {
		var aa = document.getElementsByName("columnIds");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus == 1){
			art.dialog.alert("不能直接删除已经启用的栏目！");
			return false;
		}
	}

	var param = id != '' ? "?columnIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/picmgmt/column/columnDelete.action"+ param,
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
	document.getElementById("form1").action = "${path}/picmgmt/column/columnList.action";
	document.getElementById("form1").submit();
}
//进入新增编辑页面
function toEditPage(columnId){
	var oldStatus = $("#oldStatus" + columnId).val();
	if (oldStatus == 1){
		art.dialog.alert("该栏目已经启用，不能编辑！");
		return false;
	}
	
	if(columnId==null || typeof(columnId)=="undefined"){
		columnId = "";
	}
	$("#columnId").val(columnId);
	document.getElementById("form1").action = "${path}/picmgmt/column/columnEdit.action";
	document.getElementById("form1").submit();
}

function detail(columnId){

	art.dialog.open("${path}/picmgmt/column/columnDetail.action?columnId=" + columnId, 
			{
				title: "栏目详情", 
				width: 450,
				height: 300,
				lock: true

			});
}

function columnEnable(status) {
	var columnIds = $("input[name='columnIds']:checked");
	if(columnIds.length<=0){
		if (status == 2){
			art.dialog.alert("请选择需要禁用的选项！");
		}else if (status == 1){
			art.dialog.alert("请选择需要启用的选项！");
		}
		return false;
	}
	
	art.dialog.confirm('你确认该操作？', function(){
			var options = {
				url: "${path}/picmgmt/column/columnEnable.action?status=" + status,
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
					if (status == 2){
						art.dialog.alert("禁用失败！",goBack);
					}else if (status == 1){
						art.dialog.alert("启用失败！",goBack);
					}
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsobj = "+data+";");
					if(rsobj.result=="true"){
						if (status == 2){
							art.dialog.alert("禁用成功！",goBack);
						}else if (status == 1){
							art.dialog.alert("启用成功！",goBack);
						}

					}else{
						if (status == 2){
							art.dialog.alert("禁用失败！",goBack);
						}else if (status == 1){
							art.dialog.alert("启用失败！",goBack);
						}
					}
				}
			};
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}

function albumMgmt(columnId){
	art.dialog.open("${path}/picmgmt/column/columnAlbumSelect.action?columnId=" + columnId, 
			{
				title: "相册管理", 
				width: 980,
				height: 450,
				lock: true

			});
}

</script>
</head>
<body>
<div class="title">
  <h2>栏目列表</h2>
</div>
<form action="${path}/picmgmt/column/columnList.action" method="post" name="form1" id="form1">
<input type="hidden" name="topicId" id="topicId" value="${search.topicId}" />
<input type="hidden" name="columnId" id="columnId" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="100px" height="30">栏目名称：</td>
                  <td width="150px">
                     <input id="columnName" name="columnName" class="form130px" maxlength="30"  value="${search.columnName}" 
onMouseOver="toolTip('栏目名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${columnStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                   <c:if test="${activeOperator.type != 2}">
                  <td width="90px" height="30">创建用户：</td>
                  <td width="160">
                  <select name="operatorNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${operatorList}" var="o" > 
							<option value="${o.operatorNo }" <c:if test="${search.operatorNo==o.operatorNo }"> selected="selected" </c:if>>${o.operatorName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                  </c:if>
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
    <c:set var="album" value="false" />
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
		<c:if test="${item eq 'album'}">   
			<c:set var="album" value="true" />  
		</c:if> 
	</c:forEach>
	<c:if test="${delete}">   
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
	</c:if> 
	<c:if test="${enable}">   
		    <input type="button" class="btn btn80" value="启用" onclick="columnEnable(1)">
	</c:if> 
	<c:if test="${disable}">   
		    <input type="button" class="btn btn80" value="禁用" onclick="columnEnable(2)">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'columnIds')"></th>
          <th width="*%" id="columnNameKey" class="sortRow">栏目名称</th>
          <th width="*%">状态</th>
          <th width="*%">创建用户</th>
          <th width="*%" id="createTime" class="sortRow">创建时间</th>
          <th width="*%">更新时间</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="10">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      	<c:forEach items="${list}" var="c">
      	       <input type="hidden" name="oldStatus${c.id}" id="oldStatus${c.id}" value="${c.status}" />
        <tr>
          <td align="center"><input type="checkbox" name="columnIds" value="${c.id }">
          </td>
          <td align="center">
             <c:choose>
             <c:when test="${fn:length(c.columnName) > 10}">
                 ${fn:substring(c.columnName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${c.columnName }
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:forEach items="${columnStatusMap}" var="m" > 
					<c:if test="${c.status==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
             ${c.operator.operatorName}
          </td>
          <td align="center">${c.createTime}</td>
          <td align="center">${c.updateTime}</td>
          <td class="tdOpera2" align="center">
             <c:if test="${detail}"><a href="javascript:;" onclick="detail('${c.id }')">详情</a></c:if>
             <c:if test="${edit}"><a href="javascript:;" onclick="toEditPage('${c.id }')">编辑</a></c:if>
             <c:if test="${delete}"> <a href="javascript:;" onclick="deleteSets('${c.id }')">删除</a></c:if>
             <c:if test="${album}"> <a href="javascript:;" onclick="albumMgmt('${c.id }')">相册管理</a></c:if>
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
     <jsp:include page="/common/sortRow.jsp" />
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
		    <input type="button" class="btn btn80" value="启用" onclick="columnEnable(1)">
	</c:if> 
	<c:if test="${disable}">   
		    <input type="button" class="btn btn80" value="禁用" onclick="columnEnable(2)">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
