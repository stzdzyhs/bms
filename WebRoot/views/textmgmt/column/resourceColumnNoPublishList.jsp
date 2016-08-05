<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>版块列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<script src="${path}/js/common.js" type="text/javascript"></script>
<script src="${path}/js/textmgmt/column/column.js" type="text/javascript"></script>
<script src="${path}/js/checkbox.js"></script>
 <script src="${path}/js/picmgmt/publish/publish.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	$("#columnId").keyup(function(){
		var columnId = $(this).val();
		var resNameReg = /^[0-9A-Za-z_\u4E00-\u9FA5\uF900-\uFA2D]+$/;
		if (columnId != null && columnId != '' && !resNameReg.test(columnId)){	
			$("#btnQuery").attr({"disabled":"disabled"});
		}else{
			$("#btnQuery").removeAttr("disabled");
		}
	});
	
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


function goBack(){
	document.getElementById("columnForm").action = "${path}/textmgmt/column/resourceColumnNoPublishList.action";
	document.getElementById("columnForm").submit();
}


function detail(id){
	art.dialog.open("${path}/textmgmt/column/columnDetail.action?columnNo=" + id, 
		{
			title: "版块详情", 
			width: 750,
			height: 450,
			lock: true
		}
	);
}

</script>
</head>
<body>
<div class="title">
	<h2>版块列表</h2>
</div>
<form action="${path}/textmgmt/column/resourceColumnNoPublishList.action" method="post" name="columnForm" id="columnForm">
<input type="hidden" name="resourceId" id="resourceId" value="${resourceId}" />
  <div class="searchWrap">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody>
        <tr>
          <td class="searchLeft"></td>
          <td class="searchBg">
            <table class="search" id="searchb" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="90px" height="30">版块ID：</td>
                  <td width="160">
                         <input id="columnId" name="columnId" class="form120px" value="${search.columnId}" 
                         onMouseOver="toolTip('版块ID由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                  
<!--                     <select name="parentNo" class="form130px"> -->
<!-- 					  <option value="">--请选择--</option> -->
<%-- 					 	<c:forEach items="${belongList}" var="u" >  --%>
<%-- 							<option value="${u.columnNo}" <c:if test="${search.parentNo==u.columnNo}"> selected="selected" </c:if>>${u.columnName}</option> --%>
<%-- 				 		</c:forEach>  --%>
<!-- 					</select> -->
                  </td>
                  <td width="90px" height="30">版块名称：</td>
                  <td width="160">
                  	<input id="columnName" name="columnName" class="form120px" value="${search.columnName}" 
                  	onMouseOver="toolTip('版块名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                	
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
      <input class="btn btn80" type="button" value="发布" onclick="articleSinglePublish('${path}/textmgmt/article/toArticleSinglePublish.action','${resourceId }')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'parentIds')"></th>
          <th width="*%">版块ID</th>
          <th width="*%">版块名称</th>
          <th width="*%">所属版块</th>
          <th width="*%">封面</th>
          <th width="*%">状态</th>
          <th width="*%">所属运营商</th>
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
        <tr>
          <td align="center">
	          <c:if test="${u.columnNo != null && u.columnNo != -1}">
	          	<input type="checkbox" name="parentIds" value="${u.columnNo }">
	          </c:if>
          </td>
          <td align="center">${u.columnId }</td>
          <td align="center">${u.columnName }</td>
          <td align="center">${u.parentColumnName}</td>
          <td align="center">
			<c:if test="${u.cover!=null}">
		    	<img src="${path}/${u.cover}" alt='${u.cover}' style='width:32px; height:32px' id='toolTipImg'/>
			</c:if>
          </td>
          <td align="center">${u.statusName}</td>
          <td align="center">${u.company.companyName}</td>          
          <td align="center">${u.operator.operatorName}</td>
          <td align="center">${u.createTime}</td>
          <td class="tdOpera2" align="center">
          <a href='javascript:;' onclick='detail(${u.columnNo})'>详情</a>
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
     <input class="btn btn80" type="button" value="发布" onclick="articleSinglePublish('${path}/textmgmt/article/toArticleSinglePublish.action','${resourceId }')">
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>