<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源管理</title>
<link rel="stylesheet" type="text/css" href="${path}/css/themes/cupertino/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="${path}/css/common.css" />
<script src="${path}/js/checkbox.js" type="text/javascript"></script>
<script src="${path}/js/jquery/jquery-1.6.4.min.js" type="text/javascript"></script>
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<style type="text/css">
#cwtab_nav {
	width:100%;
	height:41px;
	background:#FFFFFF;
}
#cwtab_nav li {
	float:left;
	margin:5px 0 0 12px;
	padding: 6px 15px 0 15px;
	line-height:24px;
	text-align:center;
	font-weight:bold;
	cursor:pointer;
	height:30px;
}
</style>
<script language="javascript">
function dyniframesize() {
	var height = 300;
	var dyniframe = null;
	if (document.getElementById) {
		//自动调整iframe高度
		dyniframe = document.getElementById('contentFrame');
		if (dyniframe && !window.opera) {
			if (dyniframe.contentDocument && dyniframe.contentDocument.body.offsetHeight) { //如果用户的浏览器是NetScape
				if (height < (dyniframe.contentDocument.body.offsetHeight+20))
				{
					height = dyniframe.contentDocument.body.offsetHeight+20;
				}
			} else if (dyniframe.Document && dyniframe.Document.body.scrollHeight) { //如果用户的浏览器是IE
				if (height < (dyniframe.Document.body.scrollHeight+20))
				{
					height = dyniframe.Document.body.scrollHeight+20;
				}
			}
			dyniframe.height = height;
		}
	}
}

function selectTab(curTab, url){
	if($(curTab).hasClass("ui-tabs-selected ui-state-active")){
		$("#cwtab_nav li").addClass("ui-tabs-selected ui-state-active");
		$(curTab).removeClass("ui-tabs-selected ui-state-active");
		
		$("#contentFrame").attr("src", url);
		
		var iframed = art.dialog.parent.mainFrame;
		if($(curTab).attr("id")=='cw_tab0'){		
			art.dialog.data('contentType',0);	
		}
		
		if($(curTab).attr("id")=='cw_tab1'){								
			art.dialog.data('contentType',1);
		}
	}
}

$(document).ready(function(){
	var index = '0';
	var contentType = art.dialog.data('contentType');//记录上次选择的窗口
	if(contentType!=null && typeof(contentType)!='undefined' && contentType!=""){
		index = contentType;
	}	
	$("#cw_tab"+index).click();
	
});


</script>
</head>
<body>
<div id="cwtab_nav">
    <ul>
       <c:if test="${search.type == 3 }">
        <li id="cw_tab0" onclick="selectTab(this, '${path}/picmgmt/topic/resourceTopicNoPublishList.action?resourceId=' + ${search.resourceId});" class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">专题</li>
        <!-- 
        <li id="cw_tab1" onclick="selectTab(this, '${path}/picmgmt/column/resourceColumnNoPublishList.action?resourceId=' + ${search.resourceId});" class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">栏目</li>
       	 -->
       </c:if>
       <c:if test="${search.type == 6 }">
        <li id="cw_tab0" onclick="selectTab(this, '${path}/textmgmt/column/resourceColumnNoPublishList.action?resourceId=' + ${search.resourceId});" class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">版块</li>
       </c:if>
    </ul>
</div>
<iframe id="contentFrame" name="contentFrame" style="width: 100%;" frameborder="0" scrolling="no" src="" onload="dyniframesize();"></iframe>
</body>
</html>