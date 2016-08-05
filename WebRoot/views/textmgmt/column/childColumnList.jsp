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
<script src="${path}/js/win/win.js" type="text/javascript"></script>
<script src="${path}/js/checkbox.js"></script>
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
	
	$("#eColumnName").keyup(function(){
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
	var url="${path}/textmgmt/column/columnDelete.action";
	if (id == '') {
		var sets = $("input[name='rtId']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}

		for(i=0;i<sets.length;i++) {
			var oldStatus = $("#oldStatus" + sets[i].value).val();
			if (oldStatus == STATUS_PUBLISH){
				art.dialog.alert("不能删除已经发布的版块！");
				return false;
			}
		}		
	}
	else {
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus == STATUS_PUBLISH){
			art.dialog.alert("不能删除已经发布的版块！");
			return false;
		}
		
		var rtId = document.getElementsByName("rtId");
		for (var i=0; i<rtId.length; i++) {
			rtId[i].checked = false;
		}
		url="${path}/textmgmt/column/columnDelete.action?rtId="+id;
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
						art.dialog.alert("该板块或者所包含的下属板块被操作员引用，不能删除！");
					}
					else if (rsobj.desc == 'role') {
						art.dialog.alert("该板块或者所包含的下属板块被角色引用，不能删除！");
					}
					else {
						art.dialog.alert("删除失败！");
					}
					*/
				}
			}
		};
		jQuery('#form1').ajaxSubmit(options);
	}, function(){
		art.dialog.tips('您取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/textmgmt/column/showChildColumnList.action";
	document.getElementById("form1").submit();
}
//进入新增/编辑页面
function toEditPage(id){
	var url = "${path}/textmgmt/column/columnEdit.action";
	if(id==null || typeof(id)=="undefined"){
		id = "";
		url = "${path}/textmgmt/column/columnNew.action";
	}
	else {
		var oldStatus = $("#oldStatus" + id).val();
		if (oldStatus == STATUS_PUBLISH){
			art.dialog.alert("不能编辑已经发布的版块！");
			return false;
		}
	}
	
	$("#id").val(id);
	document.getElementById("form1").action = url;
	document.getElementById("form1").submit();
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
	<h2>子版块列表</h2>
</div>
<form action="${path}/textmgmt/column/showChildColumnList.action" method="post" name="form1" id="form1">
<input type="hidden" name="columnNo" id="id" value="${columnNo }" />
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
                  	<input id="eColumnName" name="columnName" class="form120px" value="${search.columnName}" 
                  	onMouseOver="toolTip('版块名称由中文、大小写英文字母、数字、以及下划线组成')" onMouseOut="toolTip()" autocomplete="off" oncontextmenu="return false;"/>
                	
<%--                 	<td width="90px" height="30">排序方式：</td>
                 	<td width="160">
	                 	<select name="orderRule" class="form130px">
				  			<option value="">--请选择--</option>
				  			<option value="1" <c:if test="${column.orderRule=='1'}">selected</c:if>>板块名称</option>
				  			<option value="2" <c:if test="${column.orderRule=='2'}">selected</c:if>>创建时间</option>
						</select>
                 	</td> --%>               	
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

    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'rtId')"></th>
          <th width="*%">版块ID</th>
          <th width="*%" id="columnName" class="sortRow">版块名称</th>
          <th width="*%">所属版块</th>
          <th width="*%">封面</th>
          <th width="*%">状态</th>
          <th width="*%">所属运营商</th>
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
      	<input type="hidden" name="oldStatus${u.columnNo}" id="oldStatus${u.columnNo}" value="${u.status}" />
      	<input type="hidden" name="allocRes${u.columnNo}" id="allocRes${u.columnNo}" value="${u.allocRes}" />
        <tr>
          <td align="center">
	          <c:if test="${u.columnNo != null && u.columnNo != -1}">
	          	<input type="checkbox" name="rtId" value="${u.columnNo }">
	          </c:if>
          </td>
          <td align="center">${u.columnId }</td>
          <td align="center">${u.columnName }</td>
          <td align="center">${u.parent.columnName}</td>
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
          	 <c:if test="${u.allocRes == null}">
			 	<a href="javascript:;" onclick="deleteSets('${u.columnNo }')">删除</a>
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

    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>