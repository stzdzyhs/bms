<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>更新授权</title>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 上传文件控件 -->
<script src="${path}/js/swfupload/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/plugins/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/myhandlers.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link type="text/css" rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
</head>
<body>
<div class="searchWrap">
  <div id="content">
	<h2>更新步骤：</h2>
	<p>一. 点击<a href="#" onclick="downloadRegistFile()">&nbsp;这&nbsp;里&nbsp;</a>下载注册文件</p><br/>
    <p>二. 将注册文件发给系统维护人员</p><br/>
    <p>三. 接收系统维护人员发回的授权文件</p><br/>
    <p>四. <span id="selectfiles">点击上传</span><span id="showUploadFiles"></span>授权文件</p><br/>
    <p>五. 完成</p>
  </div>
</div>
</body>
<script type="text/javascript">
var projectPath = '${path}';
$(document).ready(function(){
	//上传组件初始化
	var param = new Object();
	param.ele = document.getElementById("selectfiles"); //dom  
	param.loadingEle = document.getElementById("showUploadFiles"); //显示上传中图标的位置
	param.fileType = "*.dat"; //格式限制，中间用英文分号隔开  
	param.uploadurl = "${path}/uploadAuthFile.action;jsessionid=${pageContext.session.id }";
	param.fileSizeLimit = "1 MB";
	param.paddingLeft=10;
	(new DjwlSWF()).init(param, showUploadFile);
});
function showUploadFile(data){
	var rsObj = eval("("+data+")");
	if (rsObj!="undefined") {
		if(rsObj.desc!=''){
			var win = art.dialog.opener;
			art.dialog.alert(rsObj.desc, function(){
					if(rsObj.result){
						win.location.reload();
					}
				});
		}
	}
}
function downloadRegistFile(){
	window.open("${path}/createRegistFile.action");
}
</script>
</html>