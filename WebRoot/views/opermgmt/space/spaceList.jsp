<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>空分组列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/common.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
$(document).ready(function(){ 
	
	$("#spaceName").keyup(function(){
		var spaceName = $(this).val();
		var spaceNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (spaceName != null && spaceName != '' && !spaceNameReg.test(spaceName)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	}); 
	
	$("#spaceId").keyup(function(){
		var spaceId = $(this).val();
		var spaceIdReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (spaceId != null && spaceId != '' && !spaceIdReg.test(spaceId)){		
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});


//提交表单
function deleteSets(id) {
    var activeOpNo = "${activeOperator.operatorNo}";
	var type = ${activeOperator.type};
    
	var url="${path}/opermgmt/space/spaceDelete.action";
	if (id == '') {
		var sets = $("input[name='rtId']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		/*
		for (var i=0; i<sets.length; i++){
			var operatorNo = $("#oldOperatorNo" + sets[i].value).val();
			if (activeOpNo != operatorNo){
				art.dialog.alert("您没有权限删除其他人添加的空分组！");
				return false;
			}
		}
		*/
	} else {
		url="${path}/opermgmt/space/spaceDelete.action?rtId="+id;
		var rtId = document.getElementsByName("rtId");
		for (var i=0; i<rtId.length; i++){
			rtId[i].checked = false;
		}
		/*	
		var operatorNo = $("#oldOperatorNo" + id).val();
		if (activeOpNo != operatorNo){
				art.dialog.alert("您没有权限删除其他人添加的空分组！");
				return false;
		}
		*/
	}
	
	/* 普通用户可以删除自己创建的空分组
	if (type == 2){
		art.dialog.alert("您没有删除空分组的权限！");
		return false;
	}
	*/
	
	art.dialog.confirm('你确认删除操作？', function(){
			var options = {
				url: url,
				dataType: 'json',
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
				success: function(rsobj) {
					art.dialog.list['broadcastLoading'].close();
					if(isResultSucc(rsobj)){
						art.dialog.alert("删除成功！", goBack);
					}else{
						if (rsobj.desc == 'reference') {
							art.dialog.alert("该空分组被策略引用，不能删除！");
						}else {
							art.dialog.alert("删除失败:" + rsobj.desc);
						}
					}
				}
			};
			jQuery('#spaceForm').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('您取消了操作！');
	});
}

function goBack(){
	document.getElementById("spaceForm").action = "${path}/opermgmt/space/spaceList.action";
	document.getElementById("spaceForm").submit();
}
//进入新增编辑页面
function toEditPage(id){
	var type = ${activeOperator.type};
	if (type == 2){
		var activeOpNo = "${activeOperator.operatorNo}";
		var operatorNo = $("#oldOperatorNo" + id).val();
		if (typeof(id)!='undefined' &&activeOpNo != operatorNo){
			art.dialog.alert("您没有权限编辑其他人添加的空分组！");
			return false;
		}
	}
	if(id==null || typeof(id)=="undefined"){
		id = "";
	}
	$("#id").val(id);
	document.getElementById("spaceForm").action = "${path}/opermgmt/space/spaceEdit.action";
	document.getElementById("spaceForm").submit();
}

function detail(id){

	art.dialog.open("${path}/opermgmt/space/spaceDetail.action?spaceNo=" + id, 
			{
				title: "空分组详情", 
				width: 450,
				height: 400,
				lock: true

			});
}

function regionMgmt(spaceNo){
	art.dialog.open("${path}/opermgmt/space/spaceCardRegionSelect.action?spaceNo=" + spaceNo, 
			{
				title: "区域管理", 
				width: 980,
				height: 450,
				lock: true

			});
}
</script>
</head>
<body>
<div class="title">
  <h2>空分组列表</h2>
</div>
<form action="${path}/opermgmt/space/spaceList.action" method="post" name="spaceForm" id="spaceForm">
<input type="hidden" name="id" id="id" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg"><table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody> 
                <tr> 
                <td width="90px" height="30">空分组ID：</td>
                  <td width="160">
                  	<input id="spaceId" name="spaceId" class="form120px" value="${search.spaceId}"   maxlength="50" 
                  	onMouseOver="toolTip('空分组ID大小写英文字母、数字组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                
                  <td width="90px" height="30">空分组名称：</td>
                  <td width="160">
                  	<input id="spaceName" name="spaceName" class="form120px" value="${search.spaceName}"   maxlength="50" 
                  	onMouseOver="toolTip('空分组名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
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
    <c:set var="dist" value="false" />
    <c:set var="region" value="false" />
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
		<c:if test="${item eq 'region'}">   
			<c:set var="region" value="true" />  
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
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'rtId')"></th>
          <th width="*%">空分组ID</th>
          <th width="*%">空分组名称</th>
          <th width="*%">空分组描述</th>
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
      	<input type="hidden" name="oldOperatorNo${u.spaceNo}" id="oldOperatorNo${u.spaceNo}" value="${u.operator.operatorNo}" disabled="disabled"/>
        <tr>
          <td align="center">
	          <c:if test="${u.spaceNo != null && u.spaceNo != -1}">
	          	<input type="checkbox" name="rtId" value="${u.spaceNo }">
	          </c:if>
          </td>
          <td align="center">${u.spaceId }</td>
          <td align="center">${u.spaceName }</td>
          <td align="center">${u.spaceDescribe }</td>
          <td align="center">
             <c:if test="${u.operator != null }">
                ${u.operator.operatorName }
             </c:if>
          </td>
          <td align="center">${u.createTime }</td>
          <td class="tdOpera2" align="center">
          <c:if test="${detail}"><a href='javascript:;' onclick='detail(${u.spaceNo})'>详情</a></c:if>
          <c:if test="${edit}"><a href="javascript:;" onclick="toEditPage('${u.spaceNo }')">编辑</a></c:if>
          <!-- comment out by MiaoJun on Apri, 23 2016. cancel the relationship between company with region
          <c:if test="${region && u.parentId != -1}"> <a href="javascript:;" onclick="regionMgmt('${u.spaceNo}')">区域管理</a></c:if>
          -->
          <c:if test="${delete}">
             <a href="javascript:;" onclick="deleteSets('${u.spaceNo }')">删除</a>
         </c:if>
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