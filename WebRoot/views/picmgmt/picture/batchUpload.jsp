<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量上传</title>
<script src="${path}/js/jquery/jquery-1.6.4.min.js"></script>
<script src="${path}/js/jquery/jquery.form.js" type="text/javascript"></script>
<!-- 弹出窗控件 -->
<link rel="stylesheet" href="${path}/js/artDialog/skins/blue.css" type="text/css" />
<script src="${path}/js/artDialog/artDialog.js"></script>
<script src="${path}/js/artDialog/plugins/iframeTools.js"></script>
<link rel="stylesheet" href="${path}/css/common.css" type="text/css" />
<!-- 表单校验控件 -->
<script src="${path}/js/formvalidator/formValidator-4.1.1.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/formvalidator/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script src="${path}/js/win/win.js" type="text/javascript"></script>
 
<script src="${path}/js/swfupload/swfupload.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/plugins/swfupload.queue.js" type="text/javascript"></script>
<script src="${path}/js/swfupload/myhandlers.js" type="text/javascript"></script>
<script type="text/javascript">
var djwlSWF;
$(document).ready(function(){

 	$("input[name='uploadType']").change(function() {
		var uploadType = $("input[name='uploadType']:checked").val();
		if (uploadType == 1) {
			$("#ftpTable").show();
			$("#localTable").hide();
		} else {
			$("#ftpTable").hide();
			$("#localTable").show();
		}
	});
 	
/*  	$("input[name='voteFlagLocal']").change(function() {
		var voteFlag = $("input[name='voteFlagLocal']:checked").val();
		var postParam = "{voteFlag:" + voteFlag + "}";
		djwlSWF.swfu.addPostParams(voteFlag);

	});  */
 	
	
	//上传组件初始化
	var param = new Object();
	param.ele = document.getElementById("selectfiles"); //dom  
	param.loadingEle = document.getElementById("showUploadFiles"); //显示上传中图标的位置
	var fileType = "*.gif;*.jpg;*.png";
	var picFormatPattern = '${album.picFormatPattern}';
	if (picFormatPattern != null && picFormatPattern != ''){
		fileType = picFormatPattern;
	}
	param.fileType = fileType; //格式限制，中间用英文分号隔开  
	var albumNo = '${album.albumNo}';
	var operatorNo = '${activeOperator.operatorNo}';
	var parameter = "?albumNo=" + albumNo + "&operatorNo=" + operatorNo;
	param.uploadurl = "${path}/picmgmt/picture/pictureBatchUploadFile.action" + parameter;
	var fileSizeLimit = "30 MB";
	var picSize = '${album.picSize}';
	if (picSize != null && picSize > 0){
		fileSizeLimit = picSize + " KB";
	}
	param.fileSizeLimit = fileSizeLimit;
	param.paddingLeft=17;
	param.fileCount=20;
	param.uploadProgress=uploadProgress;
	djwlSWF = new DjwlSWF();
	djwlSWF.init(param, showUploadFile);
});

function showUploadFile(data){
	art.dialog.list['broadcastLoading'].close();
	var rsObj = eval("("+data+")");
	if (rsObj!="undefined") {
		if (rsObj.result == 'true' || rsObj.result == true){
			art.dialog.alert("批量上传图片成功！");
		}else{
			if(rsObj.desc == 'spaceSizeLimit'){
				art.dialog.alert("上传失败，您的使用空间不足，请与管理员联系！");
			}else if (rsObj.desc == 'resolutionLimit'){
				art.dialog.alert("图片分辨率超出最大限制！");
			}else{
				art.dialog.alert("批量上传图片失败！");
			}
		}
	}else{
		art.dialog.alert("批量上传图片失败！");
	}
}

function uploadProgress(){
	art.dialog.through({
		id: 'broadcastLoading',
		title: "正在批量上传图片",
	    content: '<img src="${path}/images/08.gif" />',
	    lock: true
	});
}

function browseFtpServer()
{
	var albumNo = '${album.albumNo}';
	art.dialog.open("${path}/opermgmt/ftp/browseFtpServerList.action?albumNo=" + albumNo, 
			{
				title: "FTP服务器列表", 
				width: 980,
				height: 450,
				lock: true

			});
}

function ftpBatchUpload(){
	var filePaths = $("input[name='filePaths']");
	if(filePaths.length<=0){
		art.dialog.alert("请选择要上传的文件！");
		return false;
	}
	
	if(filePaths.length > 20){
		art.dialog.alert("最大只能上传20张图片！");
		return false;
	}
	
	var albumNo = '${album.albumNo}';
	var operatorNo = '${activeOperator.operatorNo}';
	var parameter = "?albumNo=" + albumNo + "&operatorNo=" + operatorNo;
	art.dialog.confirm('你确认上传操作？', function(){
			var options = {
				url: "${path}/picmgmt/picture/pictureFtpBatchUploadFile.action" +　parameter,
				dataType: 'html',
				beforeSend: function() {
					art.dialog.through({
						id: 'broadcastLoading',
						title: "正在批量上传图片",
					    content: '<img src="${path}/images/08.gif" />',
					    lock: true
					});
				},
				error: function(a, b) {
					art.dialog.list['broadcastLoading'].close();
					art.dialog.alert("上传失败！");
				},
				success: function(data) {
					art.dialog.list['broadcastLoading'].close();
					eval("var rsObj = "+data+";");
					if(rsObj.resultCode == 0 && rsObj.successCount > 0){
						if (rsObj.desc == 'spaceSizeLimit'){
							art.dialog.alert("FTP批量上传成功，您的使用空间不足，总文件数：" + rsObj.fileTotal + "，总条数：" + rsObj.total + "，成功条数：" + rsObj.successCount + "，失败条数：" + rsObj.failCount);
						}else{
							art.dialog.alert("FTP批量上传成功，总文件数：" + rsObj.fileTotal + "，总条数：" + rsObj.total + "，成功条数：" + rsObj.successCount + "，失败条数：" + rsObj.failCount+"，失败图片："+rsObj.failDesc);
						}
	
					}else
						if (rsObj.desc == 'spaceSizeLimit'){
							art.dialog.alert("上传失败，您的使用空间不足，请与管理员联系！");
						}else if(rsObj.desc == 'resolutionLimit'){
							art.dialog.alert("图片宽高不符合相册约束："+rsObj.failDesc);
						}else if(rsObj.desc == 'fileSizeLimit'){
							art.dialog.alert("图片大小不符合相册约束："+rsObj.failDesc);
						}else{
							art.dialog.alert("FTP批量上传失败！");
						}

					}
				}
		
			jQuery('#form1').ajaxSubmit(options);
		}, function(){
	    art.dialog.tips('你取消了操作！');
	});
}
</script>
</head>

<body>
<form id="form1" name="form1">
<table class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="20%" class="tdBlue2">上传方式</td>
    <td class="tdBlue">
	    <input type="radio" value="0" name="uploadType" checked>本地文件&nbsp;&nbsp;
	    <input type="radio" value="1" name="uploadType">FTP文件
    </td>
  </tr>
</table>
<table id="localTable" class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0">
<!--   <tr>
   <td width="20%" class="tdBlue2">参与投票</td>
    <td class="tdBlue">
	     <input type="radio" value="0" name="voteFlagLocal" checked>否&nbsp;&nbsp;
	    <input type="radio" value="1" name="voteFlagLocal">是
     </td>
  </tr> -->
  <tr>
   <td width="20%" class="tdBlue2">请选择文件</td>
    <td class="tdBlue">
	<div style="width:250px; float:left;">
		<span id="selectfiles">请选择</span>
	</div>
     </td>
  </tr>
  <tr>
<td class="tdBlue" style="color: #FF6600 ;" colspan="2">
<p>
1、支持<c:if test="${album.picFormatStr != null}">${album.picFormatStr }</c:if> <c:if test="${album.picFormatStr == null}">jpg,gif,png</c:if>图片格式。
</p>
</td>
  </tr>
  <tr>
<td class="tdBlue" style="color: #FF6600 ;" colspan="2">
<p>
2、支持按住Ctrl键，进行批量上传。
</p>
</td>
  </tr>
</table>
<table id="ftpTable" class="tableCommon tdBorWhite" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none;" >
  <tr>
   <td width="20%" class="tdBlue2">请选择文件</td>
    <td class="tdBlue">
       <input type="button" class="btnQuery" value="请选择" onClick="browseFtpServer()" >
     </td>
  </tr>
  <tr id="fileListTr" style="display: none;">
   <td class="tdBlue2">已选择文件</td>
    <td class="tdBlue">
       <div id="fileList" style="width:260px;height:150px; overflow:auto; background-color:white;"></div> 
       <div style="float:right; margin-left:0px; "><input type="button" class="btnQuery" value="开始上传" onClick="ftpBatchUpload()" ></div>
     </td>
  </tr>
    <tr>
   <td width="20%" class="tdBlue2">参与投票</td>
    <td class="tdBlue">
	     <input type="radio" value="0" name="voteFlag" checked>否&nbsp;&nbsp;
	    <input type="radio" value="1" name="voteFlag">是
     </td>
  </tr> 
  <tr>
<td class="tdBlue" style="color: #FF6600 ;" colspan="2">
<p>
1、文件内容格式为：图片名称,图片文件,投票标志；请点击
<a title="下载模板" target="_blank" href="${path}/picmgmt/picture/downloadPicturerTemplate.action?fileName=PictureImport.zip">模板文件</a>
下载。
</p>
</td>
  </tr>
  <tr>
<td class="tdBlue" style="color: #FF6600 ;" colspan="2">
<p>
2、支持<c:if test="${album.picFormatStr != null}">${album.picFormatStr }</c:if> <c:if test="${album.picFormatStr == null}">jpg,gif,png</c:if>图片格式。
</p>
</td>
</tr>
<tr>
<td class="tdBlue" style="color: #FF6600 ;" colspan="2">
<p>
3、支持同时选择多个excel文件和图片文件进行批量上传。
</p>
</td>
  </tr>
</table>
</form>
</body>
</html>
