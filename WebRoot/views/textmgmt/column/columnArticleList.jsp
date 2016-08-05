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

<script src="${path}/js/checkbox.js"></script>
<script type="text/javascript">
var activeOpNo;
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
	$("#eArticleName").keyup(function(){
		var eArticleName = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (eArticleName != null && eArticleName != '' && !resNameReg.test(eArticleName)){	
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
	var type = ${activeOperator.type};
	activeOpNo = ${activeOperator.operatorNo};
	if(type == 2){
		activeOpNo = ${activeOperator.createBy};
	}
});

//提交表单
function deleteSets(id) {
	var url="${path}/textmgmt/column/deleteArticleInColumn.action";
	var i;
	if (id == '') {
		var sets = $("input[name='rtId']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		if(activeOpNo != -1){
			for (var i=0; i<sets.length; i++){
				var createdBy = $("#createdBy" + sets[i].value).val();
				if(activeOpNo != createdBy){
					art.dialog.alert("无权删除板块下其他人的文章！");
					return false;
				}
			}
		}	
	}
	else {
		if(activeOpNo != -1){
			var createdBy = $("#createdBy" + id).val();
			if(activeOpNo != createdBy){
				art.dialog.alert("无权删除板块下其他人的文章！");
				return false;
			}
		}
		url="${path}/textmgmt/column/deleteArticleInColumn.action?rtId="+id;
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
				art.dialog.list[''].close();
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

				}
			}
		};
		jQuery('#form1').ajaxSubmit(options);
	}, function(){
		art.dialog.tips('您取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/textmgmt/column/columnArticleList.action";
	document.getElementById("form1").submit();
}


function detail(id){
	var columnNo = $("#columnNo").val();
	art.dialog.open("${path}/textmgmt/article/articleDetail.action?articleNo=" + id+"&columnNo="+columnNo, {
		title: "文章详情", 
		width: 750,
		height: 450,
		lock: true
	});
}


function addNewArticle(){
	var columnCreatedBy = $("#columnCreatedBy").val();
	if(activeOpNo != -1 && activeOpNo != columnCreatedBy){
		var cmds = "${cmdStr}";
		if(cmds == null || typeof(cmds)=="undefined" || cmds.indexOf("7")<0){
			art.dialog.alert("无权为分配的板块添加文章！");
			return false;
		}
	}
	var columnNo = $("#columnNo").val();
	art.dialog.open("${path}/textmgmt/column/columnAddNewArticle.action?columnNo=" + columnNo, {
		title: "文章列表", 
		width: 980,
		height: 450,
		lock: true,
		close:function(){
			var btn1 = $("#btnQuery"); 
			btn1.click();
		},
		okVal:"保存",
		ok:function(){
			var iframe = this.iframe.contentWindow;
			iframe.saveArticleColumnMap();
			return false;
		},
		cancel:function(){
			return true;
		}
	});
}
</script>
</head>
<body>
<div class="title">
<h2>版块所包含文章</h2>
</div>
<form action="${path}/textmgmt/column/columnArticleList.action" method="post" name="form1" id="form1">
<input type="hidden" name="columnNo" id="columnNo" value="${columnNo}" />
<input type="hidden" name="columnCreatedBy" id="columnCreatedBy" value="${columnCreatedBy}" />
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
                  	<input id="eArticleName" name="articleName" class="form120px" value="${search.articleName}" 
                  	onMouseOver="toolTip('文章名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  	
                  <td width="90px" height="30">文章标题：</td>
                  <td width="160">
                  	<input id="title" name="title" class="form120px" value="${search.title}" 
                  	onMouseOver="toolTip('文章标题由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
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
	    <input class="btn btn80" type="button" value="新增关联" onclick="addNewArticle()">
	    <input type="button" class="btn btn80" value="删除关联" onclick="deleteSets('')">
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
          <th width="*%" id="articleName" class="sortRow">文章名称</th>
          <th width="*%">文章标题</th>
          <th width="*%">状态</th>
         <!--  <th width="*%">关联模板</th>
          <th width="*%">所属运营商</th> -->
          <th width="*%">创建者</th>
          <th width="*%" id="createTime" class="sortRow">创建时间</th>
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
      <c:forEach items="${list}" var="u">
       <tr>
          <td align="center">
	          <c:if test="${u.articleNo != null && u.articleNo != -1}">
	          	<input type="checkbox" name="rtId" value="${u.columnArticleNo }">
	          	<input type="hidden" name="createdBy${u.columnArticleNo}" id="createdBy${u.columnArticleNo}" value="${u.article.createdBy}" />
	          </c:if>
          </td>
          <td align="center">${u.article.articleId }</td>
          <td align="center">${u.article.articleName }</td>
          <td align="center">${u.article.title }</td>
          <td align="center">${u.article.statusName}</td>
           <%-- <td align="center">suoshu模块</td>
          <td align="center">所属运营商</td>  --%>
          <td align="center">${u.operator.operatorName }</td>
          <td align="center">${u.article.createTime}</td>
          <td class="tdOpera2" align="center">
	        <a href="javascript:;" onclick="detail(${u.articleNo})">详情</a>
	        <c:if test="${(activeOperator.superAdmin) or (activeOperator.type == 2 and activeOperator.createBy == u.article.createdBy) or (activeOperator.type != 2 and activeOperator.operatorNo == u.article.createdBy)}">
            	<a href="javascript:;" onclick="deleteSets(${u.columnArticleNo})">删除</a>
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <jsp:include page="/common/sortRow.jsp" />
  </div>
  </form>
  <!--表格 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <div class="operation">
	    <input class="btn btn80" type="button" value="新增关联" onclick="addNewArticle()">
	    <input type="button" class="btn btn80" value="删除关联" onclick="deleteSets('')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>