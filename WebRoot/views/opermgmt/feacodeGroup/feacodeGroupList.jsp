<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>特征码分组列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#groupId").keyup(function(){
		var groupId = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (groupId != null && groupId != '' && !resNameReg.test(groupId)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	$("#groupName").keyup(function(){
		var groupName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (groupName != null && groupName != '' && !resNameReg.test(groupName)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});	

//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='groupNos']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
	} else {
		var aa = document.getElementsByName("groupNos");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
	}

	var param = id != '' ? "?groupNos="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: "${path}/opermgmt/featureCodeGroup/groupDelete.action"+param,
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
function goBack() {
	document.getElementById("form1").action = "${path}/opermgmt/featureCodeGroup/featureCodeGroupList.action";
	document.getElementById("form1").submit();
}

//进入新增编辑页面
function toEditPage(groupNo){
	if(groupNo==null || typeof(groupNo)=="undefined"){
		groupNo = "";
	}
	$("#groupNo").val(groupNo)
	document.getElementById("form1").action = "${path}/opermgmt/featureCodeGroup/featureCodeGroupEdit.action";
	document.getElementById("form1").submit();
}

//特征码组详情
function detail(groupNo) {
	art.dialog.open("${path}/opermgmt/featureCodeGroup/feacodeGroupDetail.action?groupNo=" + groupNo, 
		{
			title: "特征码组详情", 
			width: 450,
			height: 300,
			lock: true
		});
}

function showfeatureCode(groupNo){
	art.dialog.open("${path}/opermgmt/featureCodeGroup/showFeatureCodes.action?groupNo=" + groupNo, 
			{
				title: "组内特征码", 
				width: 750,
				height: 450,
				lock: true
			});
}

</script>
</head>

<body>
<div class="title">
<h2>特征码分组列表</h2>
</div>
<form action="${path}/opermgmt/featureCodeGroup/featureCodeGroupList.action" method="post" name="form1" id="form1">
<input type="hidden" name="groupNo" id="groupNo" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg">
            <table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                <td width="90px" height="30">特征码组ID：</td>
                  <td width="160">
                  	<input id="groupId" name="groupId" class="form120px" value="${search.groupId }" 
                  	onMouseOver="toolTip('特征码组ID由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">特征码组名称：</td>
                  <td width="160">
                  	<input id="groupName" name="groupName" class="form120px" value="${search.groupName }" 
                  	onMouseOver="toolTip('特征码组名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  
              	
                	<td><input id="btnQuery" class="btnQuery" name="" type="submit" value="查询"/></td>
                </tr>
              </tbody>
            </table>
          </td>
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

	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
			<input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
			<c:set var="add" value="true" />  
		</c:if> 
		<c:if test="${item eq 'detail'}">
			<c:set var="detail" value="true"/>
		</c:if>
		<c:if test="${item eq 'edit'}">
			<c:set var="edit" value="true"></c:set>
		</c:if>
		<c:if test="${item eq 'delete'}">   
			<c:set var="delete" value="true" />  
		</c:if>  
	</c:forEach>
	<c:if test="${delete}">   
		    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
	</c:if> 
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'groupNos')"></th>
          <th width="*%">分组ID</th>
          <th width="*%">分组名称</th>
          <th width="*%">创建者</th>
          <th width="*%">创建时间</th>
          <th width="*%">操作</th>
        </tr>
      </thead>
      <tbody>
      <c:if test="${list == null || fn:length(list) == 0}">
		<tr>
			<td colspan="8">
				抱歉，没有相关的结果。
			</td>
		</tr>
	  </c:if>
      <c:forEach items="${list}" var="u">
		<input type="hidden" name="oldStatus${u.groupNo}" id="oldStatus" value="" />
        <tr>
          <td align="center">
	          <c:if test="${u.groupNo != null && u.groupNo != -1}">
	          	<input type="checkbox" name="groupNos" value="${u.groupNo }">
	          </c:if>
          </td>
          <td align="center">${u.groupId }</td>
          <td align="center">${u.groupName }</td>
          <td align="center">${u.operator.operatorName}</td> 
          <td align="center">${u.createTime}</td>
          <td class="tdOpera2" align="center">
	         <c:if test="${detail}"><a href="javascript:;" onclick="detail('${u.groupNo}')">详情</a></c:if>
             <c:if test="${edit}"><a href="javascript:;" onclick="toEditPage('${u.groupNo}')">编辑</a></c:if>
             <c:if test="${delete}"><a href="javascript:;" onclick="deleteSets('${u.groupNo}')">删除</a></c:if>
             <a href="javascript:;" onclick="showfeatureCode('${u.groupNo}')">特征码管理</a>
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
	</div>
    <!--对表格数据的操作 end-->
  </div>
	
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>