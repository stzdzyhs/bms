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




function detail(id){

	art.dialog.open("${path}/opermgmt/space/spaceDetail.action?spaceNo=" + id, 
			{
				title: "空分组详情", 
				width: 450,
				height: 400,
				lock: true

			});
}

function go(){
	art.dialog.close();
}

function saveSpace(){
	var rtIds = $("input[name='rtId']:checked");
	var rtIs="";
	if(rtIds.length<=0){
		art.dialog.alert("请选择选项！");
		return false;
	}else{
		for(var i=0;i<rtIds.length;i++){
			rtIs=rtIs+rtIds[i].value+",";
		}
		
	}
	alert(rtIs);
	  var options = {
				url: "${path}/opermgmt/strategy/saveSpace.action?rtIs="+rtIs,
				beforeSend: function() {
					
				},
				error: function(a, b) {
					art.dialog.list['broadcastLoading'].close();
					art.dialog.alert("删除失败！");
				},
				success: function(data) {
					art.dialog.close();
				}
			};
			jQuery('#spaceForm').ajaxSubmit(options);
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
          <a href='javascript:;' onclick='detail(${u.spaceNo})'>详情</a>
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
    <!--对表格数据的操作 end-->
  </div>
  <div style="width:100%; text-align:center; margin-top:10px;">
        <input value="保存" type="submit" class="btnQuery" onClick="saveSpace()"/>
          &nbsp;&nbsp;
        <input value="取消" type="button" class="btnQuery" onClick="go()" />
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>