<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>资源发布列表</title>
<link rel="stylesheet" href="${path}/css/themes/cupertino/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>

<script src="${path}/js/checkbox.js"></script>
<script src="${path}/js/win/win.js" type="text/javascript"></script>
<script src="${path}/js/common.js"></script>
 
 <script src="${path}/js/picmgmt/publish/publish.js"></script>
<script type="text/javascript">
var activeOpNo;
$(document).ready(function(){
	var type = ${activeOperator.type};
	activeOpNo = ${activeOperator.operatorNo};
	if(type == 2){
		activeOpNo = ${activeOperator.createBy};
	}
});
//提交表单
function deleteSets(id) {
	if (id == '') {
		var sets = $("input[name='publishIds']:checked");
		if(sets.length<=0){
			art.dialog.alert("请选择要删除的选项！");
			return false;
		}
		if(activeOpNo != -1){
			for (var i=0; i<sets.length; i++){
				var allocRes = $("#allocRes" + sets[i].value).val();
				if(allocRes == 1){
					art.dialog.alert("无权操作分配的资源发布！");
					return false;
				}
			}
		}
	} else {
		var aa = document.getElementsByName("publishIds");
		for (var i=0; i<aa.length; i++){
			aa[i].checked = false;
		}
		if(activeOpNo != -1){
			var allocRes = $("#allocRes" + id).val();
			if(allocRes == 1){
				art.dialog.alert("无权操作分配的资源发布！");
				return false;
			}
		}
	}
	
	var param = id != '' ? "?publishIds="+id : '';
	art.dialog.confirm('你确认删除操作？', function(){
		var options = {
			url: "${path}/picmgmt/publish/resourcePublishDelete.action" + param,
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
					if (rsobj.authText != null && rsobj.authText != ""){
						art.dialog.alert(rsobj.authText);
					}else{
						art.dialog.alert("删除失败！");
					}
				}
			}
		};
		jQuery('#form1').ajaxSubmit(options);
	}, function(){
    art.dialog.tips('你取消了操作！');
	});
}

function goBack(){
	document.getElementById("form1").action = "${path}/picmgmt/publish/pubMgmt.action";
	document.getElementById("form1").submit();
}


function detail(publishId){
	art.dialog.open("${path}/picmgmt/publish/resourcePublishDetail.action?publishId=" + publishId, 
	{
		title: "发布详情", 
		width: 750,
		height: 450,
		lock: true
	});
}

function resMgmt(){
	var type = $("#type").val();
	var resourceId = $("#resourceId").val();
	art.dialog.open("${path}/picmgmt/publish/resMgmt.action" + "?type=" + type + "&resourceId=" + resourceId, 
	{
		title: "资源管理", 
		width: 980,
		height: 450,
		lock:true,
		close:function(){
			refresh();
		}
	});
}

function refresh(){
	document.getElementById("form1").action = "${path}/picmgmt/publish/pubMgmt.action";
	document.getElementById("form1").submit();
}

</script>
</head>
<body>
<div class="title">
  <h2>发布资源列表</h2>
</div>
<form action="${path}/picmgmt/publish/pubMgmt.action" method="post" name="form1" id="form1">
<input type="hidden" name="type" id="type" value="${search.type}" />
<input type="hidden" name="resourceId" id="resourceId" value="${search.resourceId}" />
  <div class="searchWrap">

  </div>
  <!--标题 end-->
  <div class="toolBar">
    <jsp:include page="/views/common/pageTemplate.jsp" />
    <!--翻页 end-->
    <div class="operation">
    <input class="btn btn80" type="button" value="新增发布" onclick="resMgmt()">
    <input type="button" class="btn btn80" value="删除发布" onclick="deleteSets('')">
     <input type="button" class="btn btn80" value="刷新" onclick="refresh()">
    </div>
    <!--对表格数据的操作 end-->
  </div>
  <!--工具栏 end-->
  <div class="tableWrap">
    <table class="tableCommon tableInterlace" width="100%" border="0" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th width="5px"><input type="checkbox" id="pDel" onclick="checkAll(this, 'publishIds')"></th>
          <th width="*%">资源名称</th>
          <th width="*%">资源类型</th>
          <th width="*%">所属资源名称</th>
          <th width="*%">所属资源类型</th>
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
      	<c:forEach items="${list}" var="p">
      		<input type="hidden" name="allocRes${p.id}" id="allocRes${p.id}" value="${p.allocRes}" />
        <tr>
          <td align="center"><input type="checkbox" name="publishIds" value="${p.id}">
          </td>
         <td align="center">
            <c:choose>
             <c:when test="${fn:length(p.resourceName) > 10}">
                 ${fn:substring(p.resourceName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${p.resourceName}
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:forEach items="${resourceTypeMap}" var="m" > 
					<c:if test="${p.type==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td align="center">
            <c:choose>
             <c:when test="${fn:length(p.parentName) > 10}">
                 ${fn:substring(p.parentName, 0, 10)}...
             </c:when>
             <c:otherwise>
                 ${p.parentName}
             </c:otherwise>
            </c:choose> 
          </td>
          <td align="center">
             <c:forEach items="${resourceTypeMap}" var="m" > 
					<c:if test="${p.parentType==m.key }"> ${m.value }</c:if>
		    </c:forEach> 
          </td>
          <td class="tdOpera2" align="center">
             <a href="javascript:;" onclick="detail('${p.id}')">详情</a>
             <c:if test="${p.allocRes == null}">
             	<a href="javascript:;" onclick="deleteSets('${p.id}')">删除</a>
             </c:if>
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
    <input class="btn btn80" type="button" value="新增发布" onclick="resMgmt()">
    <input type="button" class="btn btn80" value="删除发布" onclick="deleteSets('')">
     <input type="button" class="btn btn80" value="刷新" onclick="refresh()">
 
    </div>
    <!--对表格数据的操作 end-->
  </div>
</body>
</html>
<script src="${path}/js/ToolTip.js"></script>
