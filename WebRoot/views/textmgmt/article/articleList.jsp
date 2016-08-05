<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>文章列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>
<script src="${path}/js/textmgmt/article/article.js" type="text/javascript"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/picmgmt/publish/publish.js"></script>
<script src="${path}/js/const.js"></script>
<script src="${path}/js/opermgmt/template/template.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	$("#articleId").keyup(function(){
		var articleId = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (articleId != null && articleId != '' && !resNameReg.test(articleId)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	$("#articleName").keyup(function(){
		var articleName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (articleName != null && articleName != '' && !resNameReg.test(articleName)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	$("#title").keyup(function(){
		var title = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (title != null && title != '' && !resNameReg.test(title)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
});

//提交表单
function deleteSets(id) {
	var url="${path}/textmgmt/article/deleteArticle.action";
	var i;
	if (id == '') {
		var sets = $("input[name='rtId']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}

		for(i=0;i<sets.length;i++) {
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
				art.dialog.alert("不能删除该状态的文章！");
				return false;
			}
			var allocRes = $("#allocRes" + sets[i].value).val();
			if(allocRes == 1){
				art.dialog.alert("无权操作分配的文章！");
				return false;
			}
		}		
	}
	else {
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
			art.dialog.alert("不能删除该状态的文章！");
			return false;
		}
		var allocRes = $("#allocRes" + id).val();
		if(allocRes == 1){
			art.dialog.alert("无权操作分配的文章！");
			return false;
		}
		url="${path}/textmgmt/article/deleteArticle.action?rtId="+id;
		var rtId = document.getElementsByName("rtId");
		for (var i=0; i<rtId.length; i++) {
			rtId[i].checked = false;
		}
	}
	art.dialog.confirm('你确认删除操作？', function(){
		var options = {
			url: url,
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
				if(isResultSucc(rsobj)){
					art.dialog.alert("删除成功！", goBack);
				}
				else{
					art.dialog.alert("删除失败! " + rsobj.desc);
					/*
					if (rsobj.desc == 'operator') {
						art.dialog.alert("该文章或者所包含的下属文章被操作员引用，不能删除！");
					}
					else if (rsobj.desc == 'role') {
						art.dialog.alert("该文章或者所包含的下属文章被角色引用，不能删除！");
					}
					else {
						art.dialog.alert("删除失败！");
					}
					*/
				}
			}
		};
		jQuery('#articleForm').ajaxSubmit(options);
	}, function(){
		art.dialog.tips('您取消了操作！');
	});
}

function goBack(){
	document.getElementById("articleForm").action = "${path}/textmgmt/article/articleList.action";
	document.getElementById("articleForm").submit();
}

//进入新增编辑页面
function toEditPage(id) {
	var url;
	url = "${path}/textmgmt/article/articleNew.action";
	if(id!=null && typeof(id)!="undefined") {
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus != STATUS_EDIT && oldStatus != STATUS_FAIL){
			art.dialog.alert("不能编辑该状态的文章！");
			return false;
		}
		
		url = "${path}/textmgmt/article/articleEdit.action?articleNo="+id;
	}
	//$("#articleNo").val(id);
	document.getElementById("articleForm").action = url;
	document.getElementById("articleForm").submit();
}

function detail(id){
	art.dialog.open("${path}/textmgmt/article/articleDetail.action?articleNo=" + id, {
		title: "文章详情", 
		width: 750,
		height: 450,
		lock: true
	});
}

var auditUrl = "${path}/textmgmt/article/auditArticle.action";

function columnManage(articleNo) {
	art.dialog.open("${path}/textmgmt/article/articleColumnSelect.action?articleNo=" + articleNo, {
		title: "节目管理", 
		width: 980,
		height: 450,
		lock: true
	});
}

function regionMgmt(articleNo){
	art.dialog.open("${path}/textmgmt/article/articleCardRegionSelect.action?articleNo=" + articleNo, {
		title: "区域管理", 
		width: 980,
		height: 450,
		lock: true
	});
}

function allocMgmt(resourceId,type,createOperNo){
	
	art.dialog.open("${path}/sysmgmt/user/resourceOperAllocList.action?resourceId=" + resourceId + "&type=" + type + "&createOperNo=" + createOperNo, 
			{
				title: "分配管理", 
				width: 980,
				height: 450,
				lock: true

			});
}
</script>
</head>
<body>
<div class="title">
<h2>文章列表</h2>
</div>
<form action="${path}/textmgmt/article/articleList.action" method="post" name="articleForm" id="articleForm">
<input type="hidden" name="id" id="id" value="" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg">
            <table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">文章ID：</td>
                  <td width="160">
                  	<input id="articleId" name="articleId" class="form120px" value="${search.articleId}" 
                  	onMouseOver="toolTip('文章ID由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">文章名称：</td>
                  <td width="160">
                  	<input id="articleName" name="articleName" class="form120px" value="${search.articleName}" 
                  	onMouseOver="toolTip('文章名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  	
                  <td width="90px" height="30">文章标题：</td>
                  <td width="160">
                  	<input id="title" name="title" class="form120px" value="${search.title}" 
                  	onMouseOver="toolTip('文章标题由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  </td>
                  <td width="90px" height="30">状态：</td>
                  <td width="160">
                  <select name="status" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${articleStatusMap}" var="t" > 
							<option value="${t.key}" <c:if test="${search.status==t.key}"> selected="selected" </c:if>>${t.value }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                </tr>
                <tr>
                  <td width="90px" height="30">所属运营商：</td>
                  <td width="160">
                  <select name="companyNo" class="form130px"  >
                  		<option value="">--请选择--</option>
					  	<c:forEach items="${companyList}" var="u" > 
							<option value="${u.companyNo }" <c:if test="${search.companyNo==u.companyNo }"> selected="selected" </c:if>>${u.companyName }</option>
				 		</c:forEach> 
				  </select>
                  </td>
                   <c:if test="${activeOperator.type != 2 }">
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
    <c:set var="column" value="false" />
    <c:set var="dist" value="false" />
    <c:set var="region" value="false" />
    
    <c:set var="submit" value="false" />
    <c:set var="approved" value="false" />
    <c:set var="rejected" value="false" />
    <c:set var="publish" value="false" />
    <c:set var="unpublish" value="false" />
    <c:set var="alloc" value="false" />
    <c:set var="pubmgmt" value="false" />
        <c:set var="preview" value="false"/>
	<c:forEach var="item" items="${functionList}">   
		<c:if test="${item eq 'add'}">     
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
		<c:if test="${item eq 'column'}">   
			<c:set var="column" value="true" />  
		</c:if> 
		
		<c:if test="${item eq 'submit'}">   
			<c:set var="submit" value="true" />  
		</c:if> 
		<c:if test="${item eq 'approved'}">   
			<c:set var="approved" value="true" />  
		</c:if> 
		<c:if test="${item eq 'rejected'}">   
			<c:set var="rejected" value="true" />  
		</c:if> 
		<c:if test="${item eq 'publish'}">   
			<c:set var="publish" value="true" />  
		</c:if> 
		<c:if test="${item eq 'unpublish'}">   
			<c:set var="unpublish" value="true" />  
		</c:if> 
		
				<c:if test="${item eq 'alloc'}">   
			<c:set var="alloc" value="true" />  
		</c:if> 
<%-- 		<c:if test="${item eq 'album'}">    --%>
<%-- 			<c:set var="album" value="true" />   --%>
<%-- 		</c:if>  --%>
			<c:if test="${item eq 'pubmgmt'}">   
			<c:set var="pubmgmt" value="true" />  
		</c:if> 
		<c:if test="${item eq 'preview'}">   
			<c:set var="preview" value="true" />  
		</c:if> 
	</c:forEach>
	
    <c:if test="${add}">   
		 <input class="btn btn80" type="button" value="新增" onclick="toEditPage()">
	</c:if>
    <c:if test="${delete}">
	    <input type="button" class="btn btn80" value="删除" onclick="deleteSets('')">
	</c:if>
	
	<c:if test="${submit}">
	    <input type="button" class="btn btn80" value="提交审核" onclick="audit('文章',auditUrl, STATUS_SUBMIT, '#articleForm')">
	</c:if>
	<c:if test="${approved}">
	    <input type="button" class="btn btn80" value="审核通过" onclick="audit('文章',auditUrl, STATUS_PASS, '#articleForm')">
	</c:if>
	<c:if test="${rejected}">
	    <input type="button" class="btn btn80" value="审核不通过" onclick="audit('文章',auditUrl, STATUS_FAIL, '#articleForm')">
	</c:if>
	<!-- 
	<c:if test="${publish}">
	    <input type="button" class="btn btn80" value="发布" onclick="articlePublish('${path}/textmgmt/article/toArticlePublish.action')">
	</c:if>
	 -->
<%-- 	<c:if test="${unpublish}">
	    <input type="button" class="btn btn80" value="取消发布" onclick="audit('文章',auditUrl, STATUS_UNPUBLISH, '#articleForm')">
	</c:if> --%>
	
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'rtId')"></th>
          <th width="*%">文章ID</th>
          <th width="*%">文章名称</th>
          <th width="*%">文章标题</th>
          <th width="*%">状态</th>
          <th width="*%">所属运营商</th>
          <th width="*%">创建者</th>
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
		<c:forEach items="${list}" var="u">
		<input type="hidden" name="oldStatus${u.articleNo}" id="oldStatus${u.articleNo}" value="${u.status}" />
	    <input type="hidden" name="templateId${u.articleNo}" id="templateId${u.articleNo}" value="${u.templateId}" />
	    <input type="hidden" name="allocRes${u.articleNo}" id="allocRes${u.articleNo}" value="${u.allocRes}" />
        <tr>
          	<td align="center">
	          	<c:if test="${u.articleNo != null && u.articleNo != -1}">
	          	<input type="checkbox" name="rtId" value="${u.articleNo }">
	          	</c:if>
          	</td>
          	<td align="center">${u.articleId }</td>
          	<td align="center">${u.articleName }</td>
          	<td align="center">${u.title }</td>
          	<td align="center">${u.statusName}</td>
          	<td align="center">${u.company.companyName}</td>
          	<td align="center">${u.operator.operatorName}</td>
          	<td align="center">${u.createTime}</td>
          	<td align="center">${u.updateTime}</td>
          	<td class="tdOpera2" align="center">
	          	<c:if test="${detail}"><a href='javascript:;' onclick='detail(${u.articleNo})'>详情</a></c:if>
	          	<c:if test="${edit and u.allocRes == null}"><a href="javascript:;" onclick="toEditPage(${u.articleNo })">编辑</a></c:if>
	          	<c:if test="${delete and u.allocRes == null}">
	                <a href="javascript:;" onclick="deleteSets(${u.articleNo})">删除</a>
	          	</c:if>
	          	<c:if test="${preview and u.templateId != null}"> <a href="javascript:;" onclick="preview('${path}','${u.articleNo }',5)">预览</a></c:if>
				<c:if test="${column}">
	           		<!-- commented but please do not really remove 
	           		<a href="javascript:;" onclick="columnManage(${u.articleNo})">板块设置</a> -->
	          	</c:if>
	         	<!-- 
	            <c:if test="${alloc && activeOperator.type == 0}"> <a href="javascript:;" onclick="allocMgmt('${u.articleNo}','5','${u.operatorNo }')">分配管理</a></c:if>
	             -->
	            <c:if test="${pubmgmt && u.status>=2}"> <a href="javascript:;" onclick="pubMgmt(ENTITY_TYPE_ARTICLE,'${u.articleNo}','${path}/picmgmt/publish/pubMgmt.action')">发布管理</a></c:if> 
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
	
	<c:if test="${submit}">
	    <input type="button" class="btn btn80" value="提交审核" onclick="audit('文章',auditUrl, STATUS_SUBMIT, '#articleForm')">
	</c:if>
	<c:if test="${approved}">
	    <input type="button" class="btn btn80" value="审核通过" onclick="audit('文章',auditUrl, STATUS_PASS, '#articleForm')">
	</c:if>
	<c:if test="${rejected}">
	    <input type="button" class="btn btn80" value="审核不通过" onclick="audit('文章',auditUrl, STATUS_FAIL, '#articleForm')">
	</c:if>
	<!-- 
	<c:if test="${publish}">
	    <input type="button" class="btn btn80" value="发布" onclick="articlePublish('${path}/textmgmt/article/toArticlePublish.action')">
	</c:if>
	 -->
<%-- 	<c:if test="${unpublish}">
	    <input type="button" class="btn btn80" value="取消发布" onclick="audit('文章',auditUrl, STATUS_UNPUBLISH, '#articleForm')">
	</c:if> --%>
    
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
